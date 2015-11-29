package com.zone.zoneapp.Activity;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zone.zoneapp.R;

public class RequestDetailMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private LatLng mRequestLocation;
    static final String TAG = "DetailMapActivity";
    String mRequestSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail_map);
        MapFragment mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.request_detail_map);
        mMapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        mRequestLocation = new LatLng(extras.getDouble("Lat"),extras.getDouble("Lng"));
        mRequestSubject = extras.getString("Subject");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_detail_map, menu);
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
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mRequestLocation)
                .zoom(17)
                .build();
        googleMap.addMarker(new MarkerOptions()
                .position(mRequestLocation)
                .title(mRequestSubject));
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
