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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.zone.zoneapp.R;
import com.zone.zoneapp.model.UserAccount;

public class LoginActivity extends AppCompatActivity{

    EditText mUsernameEditText;
    EditText mPasswordEditText;
    Button mSignInButton;
    Button mForgetButton;
    Button mCreateButton;
    UserAccount user;

    public static final String EXTRA_USERNAME = "com.zone.app.username";
    


    private void updateView(){
        user = new UserAccount();

        mUsernameEditText = (EditText)findViewById(R.id.login_username_EditText);
        mUsernameEditText.addTextChangedListener(new TextWatcher() {
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


        mPasswordEditText = (EditText)findViewById(R.id.login_password_EditText);
        mPasswordEditText.addTextChangedListener(new TextWatcher() {
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


        mSignInButton = (Button)findViewById(R.id.sign_in_Button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            // do stuff with the user
            //Log.i("aaa", currentUser.getUsername());

            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);

        } else {
            // show the signup or login screen
            setContentView(R.layout.activity_login);

            updateView();
        }





    }
    private void login() {
        final String username = mUsernameEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        // Validate the log in data
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
        validationErrorMessage.append(getString(R.string.error_end));


        if (validationError) {
            Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (validationError==false){
            // Set up a progress dialog
            final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage(getString(R.string.progress_login));
            dialog.show();
            // Call the Parse login method
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    dialog.dismiss();
                    if (user != null) {

                        //LocationFinder locationFinder = new LocationFinder(LoginActivity.this,LoginActivity.this);
                        //locationFinder.detectLocationOneTime();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        //i.putExtra(EXTRA_USERNAME, username);
                        startActivity(i);


                        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        //startActivity(intent);

                    } else {
                        // Show the error message
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        updateView();
                    }
                }
            });
        }

    }

}
