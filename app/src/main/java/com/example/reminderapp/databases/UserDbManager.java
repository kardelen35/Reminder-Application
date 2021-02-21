package com.example.reminderapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

public class UserDbManager extends Db {

    SQLiteDatabase db;
    String TABLE_NAME = "Users";

    public UserDbManager(@Nullable Context context) {
        super(context);
        db = getWritableDatabase();

    }


    //region Register
    public boolean register(String fullName, String email, String pwd) {
      RegisterAsync registerAsync=new RegisterAsync();
      return registerAsync.doInBackground(new Register(fullName,email,pwd));
    }

    private class RegisterAsync extends AsyncTask<Register, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Register... registers) {

            try {
                Register register = registers[0];
                ContentValues data = new ContentValues();
                data.put("FullName", register.fullName);
                data.put("Email", register.email);
                data.put("Password", register.pwd);
                db.insert(TABLE_NAME, null, data);
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }
    //endregion

    private class Register {
        String fullName;
        String email;
        String pwd;

        public Register(String fullName, String email, String pwd) {
            this.fullName = fullName;
            this.email = email;
            this.pwd = pwd;
        }
    }

    //region Login
    public Cursor login(String email, String pwd) {
        LoginAsync loginAsync=new LoginAsync();
        return loginAsync.doInBackground(new Login(email,pwd));
    }

    private class LoginAsync extends AsyncTask<Login,Void,Cursor>{
        @Override
        protected Cursor doInBackground(Login... logins) {

            try {
                Login login=logins[0];
                String query = "SELECT * FROM " + TABLE_NAME + " Where Email='" + login.email + "' and Password='" + login.pwd + "'";
                Cursor cursor = null;
                if (db != null) {
                    cursor = db.rawQuery(query, null);
                }
                return cursor;

            }catch (Exception e){
                e.printStackTrace();
                return null;

            }

        }
    }
    //endregion

    private class Login {

        String email;
        String pwd;

        public Login( String email, String pwd) {

            this.email = email;
            this.pwd = pwd;
        }
    }
}
