package com.zone.zoneapp.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.zone.zoneapp.R;
import com.zone.zoneapp.model.Post;
import com.zone.zoneapp.utils.LocationFinder;

public class CreateRequestActivity extends AppCompatActivity implements LocationFinder.LocationDetector{
    Button mFindLocationOnGoogleMapButton;
    Button mUseCurrentLocationButton;
    Button mCreateRequestButton;
    EditText mPostText;
    LocationFinder mLocationFinder;
    Post mPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        mPost = new Post();
        mFindLocationOnGoogleMapButton = (Button) findViewById(R.id.find_location_on_google_map);
        mUseCurrentLocationButton = (Button) findViewById(R.id.use_current_location_button);
        mCreateRequestButton = (Button) findViewById(R.id.create_request_button);
        mPostText = (EditText) findViewById(R.id.request_text_edit_text);
        mFindLocationOnGoogleMapButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(),MapActivity.class);
                startActivity(intent);
            }
        });
        mUseCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationFinder = new LocationFinder(CreateRequestActivity.this,CreateRequestActivity.this);
                mLocationFinder.detectLocation();
            }
        });
        mCreateRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPostText.getText().toString().trim().length()==0){
                    Toast.makeText(CreateRequestActivity.this,"Please enter a description about your request",Toast.LENGTH_LONG).show();
                    return;
                }
                if (mPost.getLocation()==null){
                    Toast.makeText(CreateRequestActivity.this,"Please enter a valid location for your request",Toast.LENGTH_LONG).show();
                    return;
                }
                mPost.setOwner(ParseUser.getCurrentUser());
                mPost.setText(mPostText.getText().toString().trim());
                ParseACL acl = new ParseACL();
                acl.setPublicReadAccess(true);
                mPost.setACL(acl);
                mPost.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(CreateRequestActivity.this,RequestHistoryActivity.class);
                        startActivity(intent);
//                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void locationFound(Location location) {
        Toast.makeText(this,"Location found",Toast.LENGTH_LONG).show();
        mPost.setLocation(new ParseGeoPoint(location.getLatitude(),location.getLongitude()));
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        Toast.makeText(this,"Location not found",Toast.LENGTH_LONG).show();
    }
}
