package com.example.reminderapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.example.reminderapp.databases.Db;

public class EventReminderDbManager extends Db {

    SQLiteDatabase db;
    String TABLE_NAME = "EventReminders";

    public EventReminderDbManager(@Nullable Context context) {
        super(context);
        db = getWritableDatabase();
    }

    //region ReadAllData
    public Cursor readAllData() {
        ReadAllDataAsync action = new ReadAllDataAsync();
        return action.doInBackground();
    }

    private class ReadAllDataAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            try {
                String query = "SELECT Id,Title FROM " + TABLE_NAME;
                Cursor cursor = null;
                if (db != null) {
                    cursor = db.rawQuery(query, null);
                }
                return cursor;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }
    //endregion


    //region Get
    public Cursor get(int id) {
        GetAsync getAsync = new GetAsync();
        return getAsync.doInBackground(id);

    }

    private class GetAsync extends AsyncTask<Integer, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Integer... integers) {
            try {
                String query = "SELECT * FROM " + TABLE_NAME + " Where Id=" + integers[0];
                Cursor cursor = null;
                if (db != null) {
                    cursor = db.rawQuery(query, null);
                }
                return cursor;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    //endregion


    //region add
    public boolean add(String title, String date, String time, Boolean repeat, int repeatSize, String repeatInterval) {
        addAsync addAsync = new addAsync();
        return addAsync.doInBackground(new EventModel(0, title, date, time, repeat, repeatSize, repeatInterval));
    }

    private class addAsync extends AsyncTask<EventModel, Void, Boolean> {

        @Override
        protected Boolean doInBackground(EventModel... eventModels) {

            try {
                EventModel eventModel = eventModels[0];
                ContentValues data = new ContentValues();
                data.put("Title", eventModel.title);
                data.put("Date", eventModel.date);
                data.put("Time", eventModel.time);
                data.put("Repeat", eventModel.repeat);
                data.put("RepeatSize", eventModel.repeatSize);
                data.put("RepeatInterval", eventModel.repeatInterval);
                db.insert(TABLE_NAME, null, data);
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }
    //endregion

    //region Update
    public boolean update(int id, String title, String date, String time, Boolean repeat, int repeatSize, String repeatInterval) {

        UpdateAsync updateAsync = new UpdateAsync();
        return updateAsync.doInBackground(new EventModel(id, title, date, time, repeat, repeatSize, repeatInterval));
    }

    private class UpdateAsync extends AsyncTask<EventModel, Void, Boolean> {

        @Override
        protected Boolean doInBackground(EventModel... eventModels) {

            try {
                EventModel eventModel = eventModels[0];
                ContentValues data = new ContentValues();
                data.put("Title", eventModel.title);
                data.put("Date", eventModel.date);
                data.put("Time", eventModel.time);
                data.put("Repeat", eventModel.repeat);
                data.put("RepeatSize", eventModel.repeatSize);
                data.put("RepeatInterval", eventModel.repeatInterval);
                db.update(TABLE_NAME, data, "Id=?", new String[]{String.valueOf(eventModel.id)});
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }
    //endregion

    //region Delete
    public boolean delete(int id) {
        DeleteAsync deleteAsync = new DeleteAsync();
        return deleteAsync.doInBackground(id);

    }

    private class DeleteAsync extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... integers) {
            try {
                db.delete(TABLE_NAME, "Id=?", new String[]{String.valueOf(integers[0])});
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }
    //endregion


    private class EventModel {
        int id;
        String title;
        String date;
        String time;
        Boolean repeat;
        int repeatSize;
        String repeatInterval;

        public EventModel(int id, String title, String date, String time, Boolean repeat, int repeatSize, String repeatInterval) {
            this.title = title;
            this.date = date;
            this.time = time;
            this.repeat = repeat;
            this.repeatSize = repeatSize;
            this.repeatInterval = repeatInterval;
            this.id = id;
        }
    }

}
