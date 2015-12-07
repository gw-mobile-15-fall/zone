package com.zone.zoneapp.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.zone.zoneapp.R;
import com.zone.zoneapp.model.ListItem;
import com.zone.zoneapp.utils.LocationFinder;
import com.zone.zoneapp.utils.MyListViewAdapter;
import com.zone.zoneapp.utils.PersistanceManager;
import com.zone.zoneapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class RequestsNearbyActivity extends AppCompatActivity implements LocationFinder.LocationDetector{

    private Location mLocation;
    private ProgressDialog mProgressDialog;
    private ArrayList<ListItem> mList;
    private ListView mListView;
    private int mDistance;
    private String mFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_nearby);
        update();

    }


    private void update(){
        if (!Utils.isNetworkConnected(this)){
            Toast.makeText(this,getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();
            return;
        }

        //get searching radius settings from sharedPreference
        mDistance = PersistanceManager.getRadius(this);
        mLocation = new Location("");
        mListView = (ListView) findViewById(R.id.requestsNearbyList);
        mList = new ArrayList<ListItem>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(this.getString(R.string.loading));
        mProgressDialog.show();

        LocationFinder locationFinder = new LocationFinder(this,this);
        locationFinder.detectLocationOneTime();

    }

    private void populateListView(){
        ArrayAdapter<ListItem> adapter = new MyListViewAdapter(this, R.layout.list_item, mList);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(RequestsNearbyActivity.this,RequestDetailActivity.class);
                i.putExtra("ItemDetail", (ListItem) parent.getItemAtPosition(position));
                startActivity(i);
            }
        });
        mListView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requests_nearby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refesh_button_nearby){
            update();
        }

        //change searching radius
        if (id == R.id.setting_button){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);

            //get existing setting from sharedpreferecne
            input.setText(String.valueOf(mDistance = PersistanceManager.getRadius(this)));

            builder.setView(input);
            builder.setTitle(this.getString(R.string.change_settings))
                    .setMessage(this.getString(R.string.change_radius))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            mDistance = Integer.parseInt(input.getText().toString());
                            PersistanceManager.setRadius(RequestsNearbyActivity.this, mDistance);
                            update();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);

            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void locationFound(Location location) {
        loadInformation();


    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        mProgressDialog.dismiss();
        Toast.makeText(this, this.getString(R.string.cannot_load), Toast.LENGTH_SHORT).show();
        return;

    }

    private void loadInformation(){

        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(mLocation.getLatitude(),mLocation.getLongitude());
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Posts");

        //find all the posts within certain miles
        parseQuery.whereWithinMiles("postLocation", parseGeoPoint, mDistance);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() == 0) {

                    mProgressDialog.dismiss();
                    Toast.makeText(RequestsNearbyActivity.this, RequestsNearbyActivity.this.getString(R.string.no_nearby_found), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mList = new ArrayList<ListItem>();

                    for (ParseObject i : objects) {
                        ListItem item = new ListItem(i.getObjectId(), i.getString("postOwner"), i.getCreatedAt().toString(), i.getString("postTitle"), i.getParseGeoPoint("postLocation").getLatitude(), i.getParseGeoPoint("postLocation").getLongitude(), i.getString("postText"));
                        mList.add(item);
                    }
                    mProgressDialog.dismiss();

                    populateListView();
                }
            }
        });

    }


    //although we implement this for rotation, we still think it is better to let user update data after rotation. Therefore, we didn't use this
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save location information before rotation
        outState.putSerializable("retrieve_list", mList);
    }
}
