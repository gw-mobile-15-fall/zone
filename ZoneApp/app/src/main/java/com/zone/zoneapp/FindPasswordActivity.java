package com.zone.zoneapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPasswordActivity extends AppCompatActivity {


    EditText mUsername;
    EditText mEmail;
    Button mFind;
    String mUserInfor;
    String mEmailInfor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        mUsername = (EditText)findViewById(R.id.find_password_username_EditText);
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserInfor = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mEmail = (EditText)findViewById(R.id.find_password_email_EditText);
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmailInfor = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mFind = (Button)findViewById(R.id.find_password_submit);
        mFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true){
                    //send email
                    Toast.makeText(FindPasswordActivity.this, "Retrieving Password Email Sent!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(FindPasswordActivity.this, "Incorrect Information!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
