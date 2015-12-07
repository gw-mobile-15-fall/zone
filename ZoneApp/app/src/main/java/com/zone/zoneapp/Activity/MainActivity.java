package com.zone.zoneapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.zone.zoneapp.R;
import com.zone.zoneapp.utils.LocationFinder;
import com.zone.zoneapp.utils.Utils;

public class MainActivity extends AppCompatActivity implements LocationFinder.LocationDetector{

    private String mUserName;
    private TextView mWelcomeTextView;
    private Button mCreateRequest;
    private Button mNearbyRequset;
    private Button mRequestHistory;;
    private Button mPrivate;
    private Button mLogout;
    private final String TAG = "MainActivity";
    //private static final String KEY_LOCATION = "location_index_main";
    private Location mCurrentLocation;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        ParseUser currentUser = ParseUser.getCurrentUser();
        mUserName = currentUser.getUsername();
        mWelcomeTextView = (TextView)findViewById(R.id.welcome_user_textView);
        mWelcomeTextView.setText(this.getString(R.string.welcome) + mUserName + " !");
        mCreateRequest = (Button)findViewById(R.id.create_request_Button);
        mCreateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check existance of location information
                if (!checkInternet()){
                    return;
                }
                Intent i = new Intent(MainActivity.this, CreateRequestActivity.class);
                startActivity(i);
            }
        });

        mNearbyRequset = (Button)findViewById(R.id.nearby_request_Button);
        mNearbyRequset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInternet()) {
                    return;
                }
                Intent i = new Intent(MainActivity.this, RequestsNearbyActivity.class);
                i.putExtra(LoginActivity.EXTRA_USERNAME, mUserName);
                startActivity(i);
            }
        });

        mRequestHistory = (Button)findViewById(R.id.request_history_Button);
        mRequestHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInternet()){
                    return;
                }
                Intent i = new Intent(MainActivity.this, RequestHistoryActivity.class);
                i.putExtra(LoginActivity.EXTRA_USERNAME, mUserName);
                startActivity(i);
            }
        });


        mPrivate = (Button)findViewById(R.id.private_message_Button);
        mPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInternet()){
                    return;
                }
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
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_bar_update_profile:
                Intent intent = new Intent(MainActivity.this,UpdateProfileActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void updateLocation(){
        /**
         * the following location functions are to report
         * users current location to the Parse backend
         */
        //boolean firstTimeLaunched = PersistanceManager.getWhetherFirstTimeLaunched(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(this.getString(R.string.loading));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        LocationFinder locationFinder = new LocationFinder(this,this);
        locationFinder.detectLocationOneTime();
    }

    @Override
    public void locationFound(Location location) {
        mProgressDialog.dismiss();
        Log.d(TAG, "new location found");
        /*
        when a location was found,
        if the mCurrentLocation has no value we will assign this location to the mCurrentLocation and store it to Parse
        if the mCurrentLocation has the same value as the newly detected location, we save the trouble of uploading it to Parse
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

    private boolean checkInternet(){
        if (!Utils.isNetworkConnected(this)){
            Toast.makeText(this,this.getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }




    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        Log.d(TAG,"Failed to find the user's current location due to "+ failureReason.toString());
    }
}
