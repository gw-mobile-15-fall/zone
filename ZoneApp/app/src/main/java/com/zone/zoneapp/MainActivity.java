package com.zone.zoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String mUserName;
    TextView mWelcomeTextView;

    Button mCreateRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserName = getIntent().getStringExtra(LoginActivity.EXTRA_USERNAME);

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
    }


}
