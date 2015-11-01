package com.zone.zoneapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zone.zoneapp.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText mUserNameEditText;
    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mReEnterPasswordEditText;
    Button mUpdate;

    private String mUr;
    private String mEm;
    private String mPs;
    private String mRePs;

    ParseUser mCurrentUser;

    private void updateProfile() {
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


        if (validationError == false) {

            // Set up a progress dialog
            final ProgressDialog dialog = new ProgressDialog(UpdateProfileActivity.this);
            dialog.setMessage(getString(R.string.progress_signup));
            dialog.show();


            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

            ParseQuery<ParseObject> usr = ParseQuery.getQuery("User");
            usr.whereEqualTo("username", username);

            ParseQuery<ParseObject> em = ParseQuery.getQuery("User");
            em.whereEqualTo("email", email);

            List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
            queries.add(usr);
            queries.add(em);

            ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
            mainQuery.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> results, ParseException e) {
                    // results has the list of players that win a lot or haven't won much.
                    if (results.size() == 0) {
                        mCurrentUser.setUsername(username);
                        mCurrentUser.setEmail(email);
                        mCurrentUser.setPassword(password);
                        mCurrentUser.saveInBackground();
                        Intent i = new Intent(UpdateProfileActivity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, UpdateProfileActivity.this.getString(R.string.duplicate_account), Toast.LENGTH_LONG).show();
                        updateView();
                    }
                }
            });


        }
    }

    private void updateView(){
        mUserNameEditText = (EditText)findViewById(R.id.update_username_EditText);
        mEmailEditText = (EditText)findViewById(R.id.update_email_EditText);
        mPasswordEditText = (EditText)findViewById(R.id.update_password_EditText);
        mReEnterPasswordEditText = (EditText)findViewById(R.id.update_re_password_EditText);
        mUpdate = (Button)findViewById(R.id.update_button);


        mUserNameEditText.setHint(mCurrentUser.getUsername());
        mEmailEditText.setHint(mCurrentUser.getEmail());

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        mCurrentUser = ParseUser.getCurrentUser();

        updateView();
    }

}
