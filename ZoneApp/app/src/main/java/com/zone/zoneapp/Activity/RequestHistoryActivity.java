package com.zone.zoneapp.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zone.zoneapp.R;
import com.zone.zoneapp.model.ListItem;

import java.util.ArrayList;
import java.util.List;

public class RequestHistoryActivity extends Activity {

    private ProgressDialog mProgressDialog;
    private ArrayList<ListItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_history);

        mList = new ArrayList<ListItem>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(this.getString(R.string.loading));
        mProgressDialog.show();

        loadInformation();

        //populateListView();
    }

    //search all the posts of current user from parse
    private void loadInformation() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("postOwner", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject i : objects){
                    ListItem item = new ListItem(i.getParseUser("postOwner").getUsername().toString(),i.getCreatedAt().toString(),i.getString("postTitle"),i.getParseGeoPoint("postLocation").getLatitude(),i.getParseGeoPoint("postLocation").getLongitude(),i.getString("postText"));

                    mList.add(item);
                }
                mProgressDialog.dismiss();
                populateListView();
            }
        });
    }

    //The following populate method may still contain problems
    private void populateListView(){

        ListView requestHistoryListView = (ListView) findViewById(R.id.requestHistoryList);
        //ArrayList<ListItem> array = new ArrayList<>();
        //ListItem request1  = new ListItem("SEH","10-04-15", "Lost Book");
        //ListItem request2  = new ListItem("Gelman","10-05-15", "IT help needed");
        //array.add(request1);
        //array.add(request2);
        //TODO During practial implementation, the populateListView Method may want to take in an ArrayList as input.
        //eg. refresh the list==> populate using the new list.
        ArrayAdapter<ListItem> adapter = new MyAdapter(this, R.layout.list_item,mList);
        requestHistoryListView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_history, menu);
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
