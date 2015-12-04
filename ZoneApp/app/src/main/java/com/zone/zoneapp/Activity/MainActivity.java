package com.zone.zoneapp.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.zone.zoneapp.R;
import com.zone.zoneapp.utils.LocationFinder;

public class MainActivity extends AppCompatActivity implements LocationFinder.LocationDetector{

    private String mUserName;
    private TextView mWelcomeTextView;

    private Button mCreateRequest;
    private Button mNearbyRequset;
    private Button mRequestHistory;
    private Button mEditProfile;
    private Button mPrivate;
    private Button mLogout;
    private final String TAG = "MainActivity";
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "main activity launched");
        mCurrentLocation = null;

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
                Intent i = new Intent(MainActivity.this, RequestsNearbyActivity.class);
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

        mPrivate = (Button)findViewById(R.id.private_message_Button);
        mPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PrivateMessageActivity.class);
                startActivity(i);
            }
        });


        mLogout = (Button)findViewById(R.id.logout_Button);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*
         the following location functions are to report
         users current location to the Parse backend
          */
        LocationFinder locationFinder = new LocationFinder(this,this);
        locationFinder.detectLocationOneTime();
    }

    @Override
    public void locationFound(Location location) {
        Log.d(TAG,"new location found");
        /*
        when a location was found,
        if the mCurrentLocation has no value we will assign this location to the mCurrentLocation and store it to Parse
        if the mCurrentLocation has the save value as the newly detected location, we save the trouble of uploading it to Parse
         */
        if (mCurrentLocation == null){
            Log.d(TAG,"the user has no location history, store the new location to Parse");
            mCurrentLocation = location;
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser!=null){
                ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
                currentUser.put("location",parseGeoPoint);
                currentUser.saveInBackground();
            }
        } else if (location.getLatitude()!=mCurrentLocation.getLatitude() || location.getLongitude()!=mCurrentLocation.getLongitude()){
            Log.d(TAG,"the user's current location is different from it was last time, store the new location to Parse");
            mCurrentLocation = location;
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser!=null){
                ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
                currentUser.put("location",parseGeoPoint);
                currentUser.saveInBackground();
            }
        } else {
            Log.d(TAG, "apparently the user's current location is the same as it was found last time, no need to save to Parse");
        }
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        Log.d(TAG,"Failed to find the user's current location due to "+ failureReason.toString());
    }
}
