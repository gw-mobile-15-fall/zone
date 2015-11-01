package com.zone.zoneapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.zone.zoneapp.R;
import com.zone.zoneapp.utils.Utils;

import java.util.HashMap;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    Button mSignUpButton;
    EditText mUserNameEditText;
    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mReEnterPasswordEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        updateView();
    }

    private void updateView(){
        mSignUpButton = (Button) findViewById(R.id.signup_submit_button);
        mUserNameEditText = (EditText) findViewById(R.id.signup_username_EditText);
        mPasswordEditText = (EditText) findViewById(R.id.signup_password_EditText);
        mEmailEditText = (EditText) findViewById(R.id.signup_email_EditText);
        mReEnterPasswordEditText = (EditText) findViewById(R.id.signup_re_password_EditText);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup() {
        final String username = mUserNameEditText.getText().toString().trim();
        final String password = mPasswordEditText.getText().toString().trim();
        String passwordAgain = mReEnterPasswordEditText.getText().toString().trim();
        final String email = mEmailEditText.getText().toString().trim();
        // Validate the sign up data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }
        if (password.length() == 0) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }
        if (!password.equals(passwordAgain)) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
        }
        validationErrorMessage.append(getString(R.string.error_end));

        if (!email.contains("@")) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_email));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
        }
        validationErrorMessage.append(getString(R.string.error_end));



        if (validationError==false){

            // Set up a progress dialog
            final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
            dialog.setMessage(getString(R.string.progress_signup));
            dialog.show();


            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserInfo");
            query.whereEqualTo("username", username);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        Log.d("score", "Retrieved " + list.size());

                        if (list.size()==0){
                            HashMap<String,String> map = new HashMap<String,String>();
                            map.put("username",username);
                            map.put("email",email);
                            map.put("password",password);
                            Utils.insertToParse("UserInfo", map);
                            dialog.dismiss();
                            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(i);
                        }

                        else {
                            dialog.dismiss();
                            Toast.makeText(SignUpActivity.this,R.string.duplicate_username,Toast.LENGTH_SHORT).show();
                            updateView();

                        }


                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });

        }
        // If there is a validation error, display the error
        else {
            Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
            updateView();
        }

    }


}
