package com.zone.zoneapp.Activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zone.zoneapp.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final LatLng GWU = new LatLng(38.90, -77.05);
    private LatLng mSelectedLocation;
    private Button mUseSelectedLocationButton;
    private Button mCancelButton;
    static final String TAG = "MapActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapFragment mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        //The default location is nothing is specified. A better default location might be the current location?
        //TODO possibility of implementing a search input field where you can search the location on google map?
        mSelectedLocation = new LatLng(GWU.latitude,GWU.longitude);
        mUseSelectedLocationButton = (Button) findViewById(R.id.use_google_map_location_button);
        mUseSelectedLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("MapLocationLat", mSelectedLocation.latitude);
                returnIntent.putExtra("MapLocationLng", mSelectedLocation.longitude);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                Log.d(TAG,"button clicked and location " + mSelectedLocation.latitude + "," + mSelectedLocation.longitude +" was sent back");
            }
        });
        mCancelButton = (Button) findViewById(R.id.cancel_select_location_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,cancelIntent);
                finish();
                Log.d(TAG,"cancel button clicked, no location data returned from the map");
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        focusOnLocation(GWU,googleMap);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng clickedLocation) {
                mSelectedLocation = clickedLocation;
                focusOnLocation(clickedLocation,googleMap);
                Log.d(TAG,"new location selected at "+ mSelectedLocation.latitude + "," + mSelectedLocation.longitude);
            }
        });


    }

    private void focusOnLocation(LatLng location, GoogleMap googleMap){
        googleMap.clear();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(17)
                .build();
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(getString(R.string.google_map_marker_text)));
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
