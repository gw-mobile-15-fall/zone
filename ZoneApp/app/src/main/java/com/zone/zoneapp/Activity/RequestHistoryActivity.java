package com.zone.zoneapp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
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
    private HistoryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_history);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(this.getString(R.string.loading));
        mProgressDialog.show();

        loadInformation();

    }

    //search all the posts of current user from parse
    private void loadInformation() {

        mList = new ArrayList<ListItem>();

        mAdapter = new HistoryListAdapter();
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("postOwner", user.getEmail());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject i : objects){
                    ListItem item = new ListItem(i.getObjectId(), i.getString("postOwner"),i.getCreatedAt().toString(),i.getString("postTitle"),i.getParseGeoPoint("postLocation").getLatitude(),i.getParseGeoPoint("postLocation").getLongitude(),i.getString("postText"));

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
        requestHistoryListView.setAdapter(mAdapter);
        requestHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(RequestHistoryActivity.this,RequestDetailActivity.class);
                i.putExtra("ItemDetail", (ListItem) parent.getItemAtPosition(position));
                startActivity(i);
            }
        });
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


    private class HistoryListAdapter extends ArrayAdapter<ListItem>{
        public HistoryListAdapter() {
            super(RequestHistoryActivity.this, 0, mList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try{
                if (convertView==null){
                    convertView = RequestHistoryActivity.this.getLayoutInflater().inflate(R.layout.list_history_item,null);
                }

                final ListItem item = getItem(position);


                TextView time = (TextView)convertView.findViewById(R.id.textview_time);
                time.setText(item.getTime());

                TextView title = (TextView)convertView.findViewById(R.id.textview_title);
                title.setText(item.getSubject());

                ImageView deleteButton = (ImageView)convertView.findViewById(R.id.Button_delete);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(RequestHistoryActivity.this)
                                .setTitle("Request get solved?")
                                .setMessage("Are you sure you want to delete this entry?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete

                                        ParseQuery<ParseObject> q = ParseQuery.getQuery("Responses");
                                        q.whereEqualTo("postId",item.getmId());
                                        q.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                for (ParseObject o : objects){
                                                    o.deleteInBackground();
                                                }
                                            }
                                        });


                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
                                        query.getInBackground(item.getmId(), new GetCallback<ParseObject>() {
                                            public void done(ParseObject object, ParseException e) {
                                                if (e == null) {
                                                    // Now let's update it with some new data. In this case, only cheatMode and score
                                                    // will get sent to the Parse Cloud. playerName hasn't changed.
                                                    object.deleteInBackground();
                                                    mList.remove(item);
                                                    mAdapter.notifyDataSetChanged();
                                                    //loadInformation();
                                                }
                                            }
                                        });

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                        return;
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }
    }
}
