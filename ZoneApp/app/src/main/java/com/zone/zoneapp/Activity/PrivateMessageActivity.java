package com.zone.zoneapp.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.zone.zoneapp.R;

import java.util.ArrayList;

public class PrivateMessageActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> mList;
    private ContactListAdapter mAdpter;
    private ParseUser mParseUser;

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
        mList = (ArrayList)mParseUser.getList("contactList");

    }


    @Override
    protected void onResume() {
        super.onResume();
        populateList();

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
                        mParseUser.remove("contactList");
                        mParseUser.saveInBackground();



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
        mParseUser.addAllUnique("contactList",mList);
    }
}
