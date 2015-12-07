package com.zone.zoneapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.zone.zoneapp.R;

public class SignUpActivity extends AppCompatActivity {
    Button mSignUpButton;
    EditText mUserNameEditText;
    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mReEnterPasswordEditText;
    private StringBuilder mUserName;
    private StringBuilder mPassword;
    private StringBuilder mPasswordAgain;
    private StringBuilder mEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeView();
    }

    private void initializeView(){
        mSignUpButton = (Button) findViewById(R.id.signup_submit_button);
        mUserNameEditText = (EditText) findViewById(R.id.signup_username_EditText);
        mPasswordEditText = (EditText) findViewById(R.id.signup_password_EditText);
        mReEnterPasswordEditText = (EditText) findViewById(R.id.signup_re_password_EditText);
        mEmailEditText = (EditText) findViewById(R.id.signup_email_EditText);

        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserName = new StringBuilder(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword = new StringBuilder(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail = new StringBuilder(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mReEnterPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPasswordAgain = new StringBuilder(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup(){
        final String username = mUserName.toString();
        final String password = mPassword.toString();
        String passwordAgain = mPasswordAgain.toString();
        final String email = mEmail.toString();
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



        if (validationError==false){

            // Set up a progress dialog
            final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
            dialog.setMessage(getString(R.string.progress_signup));
            dialog.show();

            // Set up a new Parse user
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);

            // Call the Parse signup method
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    dialog.dismiss();
                    if (e != null) {
                        // Show the error message
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        // Start an intent for the dispatch activity
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });



        }
        // If there is a validation error, display the error
        else {
            Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
            initializeView();
        }
    }
}
