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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class RequestsNearby extends AppCompatActivity implements LocationFinder.LocationDetector{

    private Location mLocation;
    private ProgressDialog mProgressDialog;
    private ArrayList<ListItem> mList;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_nearby);
        mLocation = null;
        mListView = (ListView) findViewById(R.id.requestsNearbyList);
        mList = new ArrayList<ListItem>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(this.getString(R.string.loading));
        //populateListView();
        mProgressDialog.show();

        LocationFinder locationFinder = new LocationFinder(this,this);
        locationFinder.detectLocationOneTime();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void populateListView(){

        //ArrayList<ListItem> array = new ArrayList<>();
        //ListItem request1  = new ListItem("GW Hospital","01-01-15", "doctor needed");
        //ListItem request2  = new ListItem("the White House","10-10-14", "Bodyguard needed");
        //array.add(request1);
        //array.add(request2);
        //TODO During practial implementation, the populateListView Method may want to take in an ArrayList as input.
        //eg. refresh the list==> populate using the new list.
        //ArrayAdapter<ListItem> adapter = new MyAdapter(this, R.layout.list_item,array);

        Log.i("aaa", "2");

        ArrayAdapter<ListItem> adapter = new MyAdapter(this, R.layout.list_item, mList);
        Log.i("aaa", "3");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(RequestsNearby.this,RequestDetailActivity.class);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void locationFound(Location location) {
        if (mLocation == null){
            mLocation = location;
            loadInformation();
        }


    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        mProgressDialog.dismiss();
        Toast.makeText(this,this.getString(R.string.cannot_load),Toast.LENGTH_SHORT).show();

    }

    private void loadInformation(){
        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(mLocation.getLatitude(),mLocation.getLongitude());

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Posts");

        //find all the posts within 1 mile
        parseQuery.whereWithinMiles("postLocation", parseGeoPoint, 1);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size()==0){
                    mProgressDialog.dismiss();
                    Toast.makeText(RequestsNearby.this,RequestsNearby.this.getString(R.string.no_nearby_found),Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    for (ParseObject i : objects){
                        ListItem item = new ListItem(i.getObjectId(),i.getParseUser("postOwner").getUsername(),i.getCreatedAt().toString(),i.getString("postTitle"),i.getParseGeoPoint("postLocation").getLatitude(),i.getParseGeoPoint("postLocation").getLongitude(),i.getString("postText"));
                        mList.add(item);
                    }

                    mProgressDialog.dismiss();
                    populateListView();
                }



            }
        });

    }
}
