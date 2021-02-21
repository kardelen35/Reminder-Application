package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.reminderapp.databases.EventReminderDbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    FloatingActionButton fabAddButton;
    EventReminderDbManager eventReminderDbManager;
    CustomAdapter customAdapter;
    ArrayList<Integer> idArray;
    ArrayList<String> titleArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase db = openOrCreateDatabase("EventReminder", Context.MODE_PRIVATE,null);
        eventReminderDbManager =new EventReminderDbManager(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fabAddButton = findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.recyclerView);
        idArray=new ArrayList<>();
        titleArray=new ArrayList<>();
        get();
        fabAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id",0);
                startActivity(intent);
            }
        });

        customAdapter = new CustomAdapter(MainActivity.this,idArray,titleArray);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
    void get(){
        Cursor cursor= eventReminderDbManager.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                idArray.add(cursor.getInt(cursor.getColumnIndex("Id")));
                titleArray.add(cursor.getString(cursor.getColumnIndex("Title")));
            }
        }
    }
}