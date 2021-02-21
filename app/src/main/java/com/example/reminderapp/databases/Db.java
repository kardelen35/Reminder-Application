package com.example.reminderapp.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.DataTruncation;

public class Db extends SQLiteOpenHelper {

    SQLiteDatabase database;
    final static String DATABASE_NAME = "EventReminder";
    final static int VERSION = 1;

    public Db(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
               createTableEventReminders();
               createTableUser();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTableEventReminders() {
        try {
            String query = " CREATE TABLE IF NOT EXISTS EventReminders (Id INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT NOT NULL, Date  TEXT, Time TEXT,Repeat BOOLEAN,RepeatSize INTEGER,RepeatInterval TEXT);";
            database.execSQL(query);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void createTableUser() {
        try {
            String query = " CREATE TABLE IF NOT EXISTS Users (Id INTEGER PRIMARY KEY AUTOINCREMENT, FullName TEXT NOT NULL,  Email TEXT NOT NULL, Password TEXT NOT NULL);";
            database.execSQL(query);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
