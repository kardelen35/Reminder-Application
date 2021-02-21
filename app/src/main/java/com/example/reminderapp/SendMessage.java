package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class SendMessage extends AppCompatActivity {

    EditText txt_phone_number, txt_message;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        toolbar = findViewById(R.id.messageToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        txt_phone_number = findViewById(R.id.txtPhoneNumber);
        txt_message = findViewById(R.id.txtMessage);
    }

    public void onClickSend(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Message();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);

        }
    }

    private void Message() {
        try {
            String phoneNumber = txt_phone_number.getText().toString().trim();
            String Message = txt_message.getText().toString().trim();

            if (!txt_phone_number.getText().toString().equals("") || !txt_message.getText().toString().equals("")) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, Message, null, null);

                Toast.makeText(this, R.string.message_sent, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.please_enter_number_or_message, Toast.LENGTH_SHORT).show();

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:

                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Message();
                } else {

                    Toast.makeText(this, R.string.do_not_have_required_permission, Toast.LENGTH_SHORT).show();


                }

        }

    }
}