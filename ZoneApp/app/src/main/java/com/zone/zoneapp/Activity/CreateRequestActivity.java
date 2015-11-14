package com.zone.zoneapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.zone.zoneapp.R;
import com.zone.zoneapp.utils.LocationFinder;

public class CreateRequestActivity extends AppCompatActivity implements LocationFinder.LocationDetector{

    private static final int MAPREQUESTCODE = 12314;

    private EditText mDes;
    private Button mMapLoc;
    private Button mCurrentLoc;
    private Button mCreate;
    private TextView mLocationDisplay;
    private EditText mTit;

    private ProgressDialog mProgressDislog;

    private Location mLocation;

    private String mDesciption;
    private String mTitle;
    private String mLatitude;
    private String mLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        mDesciption="";
        mLocation = null;
        mProgressDislog = new ProgressDialog(this);


        mTit = (EditText)findViewById(R.id.request_Title_EditText);
        mDes = (EditText)findViewById(R.id.request_description_EditText);
        mMapLoc = (Button)findViewById(R.id.find_location_on_google_map);
        mCurrentLoc = (Button)findViewById(R.id.use_current_location);
        mCreate = (Button)findViewById(R.id.create_request_Button);
        mLocationDisplay = (TextView)findViewById(R.id.text_specify_location);

        /**
        Button mFindLocationOnGoogleMap = (Button) findViewById(R.id.find_location_on_google_map);
        mFindLocationOnGoogleMap.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(),MapActivity.class);
                startActivity(intent);
            }
        });
         */
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains("\n")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mTit.getWindowToken(), 0);
                    mTit.setText(s.toString().replace("\n", ""));
                }
                mTitle = mTit.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains("\n")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mDes.getWindowToken(), 0);
                    mDes.setText(s.toString().replace("\n",""));
                }
                mDesciption = mDes.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMapLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateRequestActivity.this, MapActivity.class);
                startActivityForResult(i, MAPREQUESTCODE);
            }
        });

        mCurrentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDislog.setIndeterminate(true);
                mProgressDislog.setMessage(CreateRequestActivity.this.getString(R.string.fetching_current_location));
                mProgressDislog.show();

                LocationFinder locationFinder = new LocationFinder(CreateRequestActivity.this, CreateRequestActivity.this);
                locationFinder.detectLocationOneTime();
            }
        });

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDesciption.equals("")){
                    makeToast(CreateRequestActivity.this.getString(R.string.please_type_des));
                }
                if (mLocation == null){
                    makeToast(CreateRequestActivity.this.getString(R.string.please_specify_your_location));
                }
                else {
                    createPost();
                    CreateRequestActivity.this.finish();
                }

            }
        });
    }

    @Override
    public void locationFound(Location location) {

        mProgressDislog.dismiss();
        mLocation = location;
        mLocationDisplay.setText("Latitude: " + Double.toString(location.getLatitude()) + " Longitude: " + Double.toString(location.getLongitude()));
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        mProgressDislog.dismiss();
        makeToast(this.getString(R.string.location_not_found));
    }

    private void makeToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    private void createPost(){
        ParseObject parseObject = new ParseObject("Posts");
        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(mLocation.getLatitude(),mLocation.getLongitude());
        parseObject.put("postLocation",parseGeoPoint);
        parseObject.put("postText",mDesciption);
        parseObject.put("postOwner", ParseUser.getCurrentUser());
        parseObject.put("postTitle",mTitle);
        parseObject.saveInBackground();
    }
}

