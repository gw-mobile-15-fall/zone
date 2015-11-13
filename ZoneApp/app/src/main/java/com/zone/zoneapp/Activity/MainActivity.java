package com.zone.zoneapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
import com.zone.zoneapp.R;

public class MainActivity extends AppCompatActivity {

    private String mUserName;
    private TextView mWelcomeTextView;

    private Button mCreateRequest;
    private Button mNearbyRequset;
    private Button mRequestHistory;
    private Button mEditProfile;
    private Button mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();

        mUserName = currentUser.getUsername();

        mWelcomeTextView = (TextView)findViewById(R.id.welcome_user_textView);
        mWelcomeTextView.setText("Welcome " + mUserName + " !");


        mCreateRequest = (Button)findViewById(R.id.create_request_Button);
        mCreateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateRequestActivity.class);
                i.putExtra(LoginActivity.EXTRA_USERNAME, mUserName);
                startActivity(i);
            }
        });

        mNearbyRequset = (Button)findViewById(R.id.nearby_request_Button);
        mNearbyRequset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RequestsNearby.class);
                i.putExtra(LoginActivity.EXTRA_USERNAME, mUserName);
                startActivity(i);
            }
        });

        mRequestHistory = (Button)findViewById(R.id.request_history_Button);
        mRequestHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RequestHistoryActivity.class);
                i.putExtra(LoginActivity.EXTRA_USERNAME, mUserName);
                startActivity(i);
            }
        });

        mEditProfile = (Button)findViewById(R.id.edit_profile_Button);
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UpdateProfileActivity.class);
                i.putExtra(LoginActivity.EXTRA_USERNAME, mUserName);
                startActivity(i);
            }
        });

        mLogout = (Button)findViewById(R.id.logout_Button);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }






}
