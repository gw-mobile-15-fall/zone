package com.zone.zoneapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RequestsNearby extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_nearby);
        populateListView();
    }

    private void populateListView(){
        ListView requestsNearbyListView = (ListView) findViewById(R.id.requestsNearbyList);
        ArrayList<ListItem> array = new ArrayList<>();
        ListItem request1  = new ListItem("GW Hospital","01-01-15", "doctor needed");
        ListItem request2  = new ListItem("the White House","10-10-14", "Bodyguard needed");
        array.add(request1);
        array.add(request2);
        //TODO During practial implementation, the populateListView Method may want to take in an ArrayList as input.
        //eg. refresh the list==> populate using the new list.
        ArrayAdapter<ListItem> adapter = new MyAdapter(this, R.layout.list_item,array);
        requestsNearbyListView.setAdapter(adapter);
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
}
