package com.zone.zoneapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zone.zoneapp.R;

import java.util.List;

public class FindPasswordActivity extends AppCompatActivity {


    EditText mUsername;
    EditText mEmail;
    Button mFind;
    String mUserInfor;
    String mEmailInfor;
    private static final String TAG = "FindPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        mUsername = (EditText)findViewById(R.id.find_password_username_EditText);
        mUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * Since we recover password with either email or username
             * Only one can be used as the criteria
             * When one Edit Text is on focus, clear the other one
             * @param v
             * @param hasFocus
             */
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mEmail.setText("");
                }
            }
        });
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
        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * Since we recover password with either email or username
             * Only one can be used as the criteria
             * When one Edit Text is on focus, clear the other one
             * @param v
             * @param hasFocus
             */
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mUsername.setText("");
                }
            }
        });
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
                /**
                 * query Parse backend to see if the user name or email exist
                 * if so, reset the password
                 * otherwise pop a toast saying no profile found
                 */
                String criteria;
                if (mEmailInfor.equals("") && mUserInfor.equals("")){
                    Toast.makeText(FindPasswordActivity.this,"Please enter either user name or email to find your password",Toast.LENGTH_LONG).show();
                } else if (mEmailInfor.equals("")){
                    criteria = mUserInfor;
                    resetPassword(criteria,"username");
                } else {
                    criteria = mEmailInfor;
                    resetPassword(criteria,"email");
                }

            }
        });
    }

    private void resetPassword(String criteria,String criteriaType){
        /**
         * function returns the ToastText for the recover password action
         * criteriaType is either "email" or "username"
         * criteria is the value of either email or username
         */
        Log.d(TAG,"criteria is " + criteria);
        Log.d(TAG,"criteria type is " + criteriaType);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(criteriaType,criteria);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e==null && objects.size()!=0){
                    // user found basing on the criteria
                    // reset the password by sending email to the email address user registered with
                    try {
                        ParseUser.requestPasswordReset(objects.get(0).getEmail().toString());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    Toast.makeText(FindPasswordActivity.this,"User information found baing on your input. An email has been sent to the email you registered with",Toast.LENGTH_LONG).show();
                } else {
                    // user not found basing on the criteria
                    Toast.makeText(FindPasswordActivity.this,"Your information does not match any user. Please verify your input",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
