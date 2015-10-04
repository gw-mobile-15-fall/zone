package com.zone.zoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText mUsernameTextView;
    EditText mPasswordTextView;
    Button mSignButton;
    Button mForgetButton;
    Button mCreateButton;
    UserAccount user;

    public static final String EXTRA_USERNAME = "com.zone.app.username";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = new UserAccount();

        mUsernameTextView = (EditText)findViewById(R.id.login_username_EditText);
        mUsernameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setUserName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mPasswordTextView = (EditText)findViewById(R.id.login_password_EditText);
        mPasswordTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSignButton = (Button)findViewById(R.id.sign_in_Button);
        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra(EXTRA_USERNAME, user.getUserName());
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Incorrect Account Information !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mForgetButton = (Button)findViewById(R.id.forget_password_Button);
        mForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(i);
            }
        });

        mCreateButton = (Button)findViewById(R.id.create_account_Button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

    }





}
