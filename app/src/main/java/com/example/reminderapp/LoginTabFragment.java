package com.example.reminderapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminderapp.databases.UserDbManager;
import com.google.android.material.snackbar.Snackbar;


public class LoginTabFragment extends Fragment {
    Button btnLogin;
    RelativeLayout relativeLayout;
    Snackbar snackbar;
    UserDbManager userDbManager;
    TextView txtEmail, txtPassword;

    public static LoginTabFragment getInstance() {
        LoginTabFragment loginTabFragment = new LoginTabFragment();
        return loginTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);
        txtEmail = root.findViewById(R.id.txtEmail);
        relativeLayout = root.findViewById(R.id.relativeLl);
        txtPassword = root.findViewById(R.id.txtPassword);
        btnLogin = root.findViewById(R.id.buttonLogin);

        userDbManager = new UserDbManager(getContext());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        return root;
    }





    private void login() {
        try {
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();

            if (email.trim().length() == 0) {
                Snackbar.make(relativeLayout, R.string.email_can_not_be_blank, Snackbar.LENGTH_LONG).show();
            } else if (password.trim().length() == 0) {
                Snackbar.make(relativeLayout, R.string.password_can_not_be_blank , Snackbar.LENGTH_LONG).show();
            } else  {
                Cursor cursor = userDbManager.login(email, password);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("FullName"));
                       Toast.makeText(getActivity(),"Transaction successfully" + name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } else {
                   Toast.makeText(getActivity(), R.string.user_not_found , Toast.LENGTH_SHORT).show();

                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }

    }
}