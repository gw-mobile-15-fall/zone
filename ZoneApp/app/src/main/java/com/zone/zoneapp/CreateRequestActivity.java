package com.zone.zoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CreateRequestActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        Button mFindLocationOnGoogleMap = (Button) findViewById(R.id.find_location_on_google_map);
        mFindLocationOnGoogleMap.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(),MapActivity.class);
                startActivity(intent);
            }
        });
    }

}
