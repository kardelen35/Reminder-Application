package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class LoginPage extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    float v=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        LoginAdapter loginAdapter = new LoginAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_Pager);
        viewPager.setAdapter(loginAdapter);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        TextView textViewW=findViewById(R.id.txtText);



    }
}