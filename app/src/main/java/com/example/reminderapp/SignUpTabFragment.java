package com.example.reminderapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminderapp.databases.UserDbManager;
import com.google.android.material.snackbar.Snackbar;


public class SignUpTabFragment extends Fragment {
    Button btnSignUp;
    UserDbManager userDbManager;
    Snackbar snackbar;
    CoordinatorLayout coordinatorLayout;
    TextView txtName,txtEmail,txtPassword,txtRePassword;
    public static SignUpTabFragment getInstance() {
        SignUpTabFragment signUpTabFragment = new SignUpTabFragment();
        return signUpTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_tab, container, false);
        txtName=root.findViewById(R.id.txtName);
        txtEmail=root.findViewById(R.id.txtEmail);
        txtName=root.findViewById(R.id.txtName);
        txtPassword=root.findViewById(R.id.txtPassword);
        txtRePassword=root.findViewById(R.id.txtRePassword);
        btnSignUp = root.findViewById(R.id.btnSignUp);
        userDbManager=new UserDbManager(getContext());
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        return root;
    }

    private void register() {
        try {
            String name=txtName.getText().toString();
            String email=txtEmail.getText().toString();
            String password=txtPassword.getText().toString();
            String rePassword=txtRePassword.getText().toString();

            if (name.trim().length()==0){
                Toast.makeText(getActivity(), R.string.name_can_not_be_blank, Toast.LENGTH_SHORT).show();
            }
            else if (email.trim().length()==0){
                Toast.makeText(getActivity(), R.string.email_can_not_be_blank, Toast.LENGTH_SHORT).show();
            }
            else if (password.trim().length()==0){
                Toast.makeText(getActivity(), R.string.password_can_not_be_blank, Toast.LENGTH_SHORT).show();
            }
            else if (!password.equals(rePassword)){
                Toast.makeText(getActivity(), R.string.password_can_not_be_matches, Toast.LENGTH_SHORT).show();
            }
            else
            {
                boolean result=userDbManager.register(name,email,password);
                if (!result)
                    Toast.makeText(getActivity(), R.string.register_transaction_stopped , Toast.LENGTH_SHORT).show();
                else {

                    Toast.makeText(getActivity(), R.string.register_transaction_is_successfully, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}