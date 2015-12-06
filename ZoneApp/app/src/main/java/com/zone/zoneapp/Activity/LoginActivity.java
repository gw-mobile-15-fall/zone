package com.zone.zoneapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.zone.zoneapp.R;
import com.zone.zoneapp.model.UserAccount;

public class LoginActivity extends AppCompatActivity{
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private ImageView mSignInButton;
    private ImageView mForgetButton;
    private ImageView mCreateButton;
    private UserAccount mUser;
    private static final String TAG = "LoginActivity";
    public static final String EXTRA_USERNAME = "com.zone.app.username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Login Activity Launched");
        ParseUser currentUser = ParseUser.getCurrentUser();
        setContentView(R.layout.activity_login);
        initializeView();
        /*
        check if there is a user in the current session
        if so, login directly and lead user to the homepage of the app
        otherwise user is shown the login page
         */
        if (currentUser != null) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void initializeView(){
        mUser = new UserAccount();
        mUserNameEditText = (EditText)findViewById(R.id.login_username_EditText);
        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUser.setUserName(s.toString());
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
                mUser.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSignInButton = (ImageView)findViewById(R.id.sign_in_Button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserNameEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                if (InputIsValid(username,password)) {
                    Log.d(TAG,"input is valid, trying to log in");
                    login(username,password);
                }
            }
        });

        mForgetButton = (ImageView)findViewById(R.id.forget_password_Button);
        mForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        mCreateButton = (ImageView)findViewById(R.id.create_account_Button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    // Validate the data in the login fields (in case it's blank) before passing them to Parse
    private Boolean InputIsValid(String username, String password){
        if (username.length() == 0) {
            Toast.makeText(LoginActivity.this, getString(R.string.error_blank_username), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (password.length() == 0) {
            Toast.makeText(LoginActivity.this, getString(R.string.error_blank_password), Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }


    // login with the data in the two edit-texts
    private void login(String username, String password) {
        // Set up a progress dialog while waiting for the login
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage(getString(R.string.progress_login));
        dialog.show();
        // Call the Parse login method
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                // close the progress dialog
                dialog.dismiss();
                if (e == null && user != null) {
                    // Login is successful
                    Log.d(TAG, "Logged-in successfully");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Login failed
                    Log.d(TAG, "Login failed due to " + e.getMessage());
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    // clear up the two edit texts
                    mPasswordEditText.setText("");
                    mUserNameEditText.setText("");
                    }
                }
            });
        }
    }
