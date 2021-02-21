package com.example.reminderapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import com.example.reminderapp.databases.EventReminderDbManager;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, NavigationView.OnNavigationItemSelectedListener {

    EventReminderDbManager eventReminderDbManager;
    int id;
    private EditText title;
    private TextView date, time, repeatSize, repeatInterval;
    private Switch repeat;
    private Button btnSave;



    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.writeStorage);

        title = findViewById(R.id.txtTitle);
        date = findViewById(R.id.txtDate);
        time = findViewById(R.id.txtTime);
        repeat = findViewById(R.id.chkRepeat);
        repeatSize = findViewById(R.id.txtRepeatSize);
        repeatInterval = findViewById(R.id.txtRepeatInterval);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        Intent intent = getIntent();
        eventReminderDbManager = new EventReminderDbManager(this);

        id = intent.getIntExtra("id", 0);


        get();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });
        repeatSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this);
                alert.setTitle("Enter a Number");
                final EditText input = new EditText(DetailActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);

                alert.setPositiveButton("Ok", (dialog, whichButton) -> {

                    if (input.getText().toString().length() == 0) {
                        repeatSize.setText("1");
                    } else {
                        repeatSize.setText(input.getText().toString());
                    }
                });
                alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
                    Toast.makeText(getApplication(), "Exit from the app", Toast.LENGTH_SHORT).show();
                });
                alert.show();
            }
        });
        repeatInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[5];
                items[0] = "Minute";
                items[1] = "Hour";
                items[2] = "Day";
                items[3] = "Week";
                items[4] = "Month";

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("Select Type");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        repeatInterval.setText(items[item]);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    private void delete() {
        AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this);
        alert.setTitle("Data is removing. are you sure?");
        alert.setPositiveButton("Yes", (dialog, which) -> {
            boolean result = eventReminderDbManager.delete(id);
            if (!result) {
                Toast.makeText(this, R.string.occured_while_deleting_record, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        alert.setNegativeButton(R.string.no, (dialog, which) -> Toast.makeText(DetailActivity.this, R.string.cancelled, Toast.LENGTH_SHORT).show());
        alert.show();
    }

    private void save() {

        String titleValue = title.getText().toString();
        String dateValue = date.getText().toString();
        String timeValue = time.getText().toString();
        boolean repeatValue = repeat.isChecked();
        int repeatSizeValue = Integer.parseInt(repeatSize.getText().toString());
        String repeatIntervalValue = repeatInterval.getText().toString();
        boolean status = false;
        if (id == 0) {
            status = eventReminderDbManager.add(titleValue, dateValue, timeValue, repeatValue, repeatSizeValue, repeatIntervalValue);
        } else {
            status = eventReminderDbManager.update(id, titleValue, dateValue, timeValue, repeatValue, repeatSizeValue, repeatIntervalValue);
        }
        if (status) {
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.error_occured_while_recording, Toast.LENGTH_SHORT).show();
        }
    }

    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            repeat.setText("On");
        } else {
            repeat.setText("Off");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        date.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
     Calendar calNow=Calendar.getInstance();
     Calendar calSet=(Calendar) calNow.clone();

     if(calSet.compareTo(calNow) <= 0){
         calSet.add(Calendar.DATE,1);
     }
        time.setText("Hour:" + hourOfDay + "Minute: " + minute);

     setAlarm(calSet);
    }
    public void setAlarm(Calendar alarmCalendar){
        Toast.makeText(getApplicationContext(),R.string.alarm_is_set,Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getBaseContext(),AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getBaseContext(),1,intent,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alarmCalendar.getTimeInMillis(),pendingIntent);
    }

    void get() {
        if (id > 0) {
            // Cursor cursor= eventReminderDbManager.get(id);
            Cursor cursor = eventReminderDbManager.get(id);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    title.setText(cursor.getString(cursor.getColumnIndex("Title")));
                    date.setText(cursor.getString(cursor.getColumnIndex("Date")));
                    time.setText(cursor.getString(cursor.getColumnIndex("Time")));
                    int rep = cursor.getInt(cursor.getColumnIndex("Repeat"));
                    if (rep == 1) {
                        repeat.setText("on");
                        repeat.setChecked(true);
                    } else {
                        repeat.setText("off");
                        repeat.setChecked(false);
                    }
                    repeatSize.setText(cursor.getString(cursor.getColumnIndex("RepeatSize")));
                    repeatInterval.setText(cursor.getString(cursor.getColumnIndex("RepeatInterval")));

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.delete:
                delete();
                break;
            case R.id.share:
                ApplicationInfo api = getApplicationContext().getApplicationInfo();
                String appPath = api.sourceDir;
                Intent intent1 = new Intent((Intent.ACTION_SEND));
                intent1.setType("application/vnd.android.package.archive");
                intent1.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(appPath)));
                startActivity(Intent.createChooser(intent1, "Share Via"));

            case R.id.alarmoff:
                closeAlarm();

        }
        return true;
    }

    private void closeAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem register = menu.findItem(R.id.delete);
        register.setVisible(id > 0);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.writeStorage:
                Intent intent = new Intent(DetailActivity.this, ExternalStorage.class);
                startActivity(intent);
                break;

            case R.id.nav_message:
                Intent intent1 = new Intent(DetailActivity.this, SendMessage.class);
                startActivity(intent1);
                break;

            case R.id.photo:
                Intent intent2 = new Intent(DetailActivity.this, Photo.class);
                startActivity(intent2);
            case R.id.aboutUs:
                Intent intent3 = new Intent(DetailActivity.this, AboutUsActivity.class);
                startActivity(intent3);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
