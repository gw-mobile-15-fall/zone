package com.zone.zoneapp.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zone.zoneapp.R;

import java.util.ArrayList;
import java.util.List;

public class PrivateMessageActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> mList;
    private ContactListAdapter mAdpter;
    private ParseUser mParseUser;
    private ParseObject mParseObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mList = new ArrayList<String>();

        mListView = (ListView)findViewById(R.id.private_message_list_container);


        mParseUser = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Contacts");
        Log.i("aaa","email"+mParseUser.getEmail());
        query.whereEqualTo("currentUser", mParseUser.getEmail());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size()==0){
                    Log.i("aaa","null"+mParseUser.getEmail());

                    mList = new ArrayList<String>();
                    mParseObject = null;
                }
                else if(objects.size()==1){
                    Log.i("aaa","foind"+mParseUser.getEmail());

                    mParseObject = objects.get(0);
                    mList = (ArrayList)mParseObject.getList("contactList");
                    mList.remove(mParseUser.getEmail());
                    Log.i("aaa", mList.toString());
                    populateList();

                }
            }
        });




    }



    private void populateList(){

        mAdpter = new ContactListAdapter();
        mListView.setAdapter(mAdpter);

    }



    private class ContactListAdapter extends ArrayAdapter<String> {

        public ContactListAdapter() {
            super(PrivateMessageActivity.this, 0, mList);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            try{
                if (convertView==null){
                    convertView = PrivateMessageActivity.this.getLayoutInflater().inflate(R.layout.list_private_message,null);
                }

                final String userId = getItem(position);


                TextView userIdView = (TextView)convertView.findViewById(R.id.userid_private);
                userIdView.setText(userId);

                ImageView deleteButton = (ImageView)convertView.findViewById(R.id.Button_delete_private);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mList.remove(getItem(position));
                        mAdpter.notifyDataSetChanged();

                        //mParseObject.remove("contactList");
                        //mParseObject.saveInBackground();
                        //mParseUser.remove("contactList");
                        //mParseUser.saveInBackground();



                    }
                });


            }catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mParseObject != null && mParseObject.getList("contactList").size()!=0){
            mParseObject.remove("contactList");
            mParseObject.addAllUnique("contactList", mList);
            mParseObject.saveInBackground();
        }

    }
}
