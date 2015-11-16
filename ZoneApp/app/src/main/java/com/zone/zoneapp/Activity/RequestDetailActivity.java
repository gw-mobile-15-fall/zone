package com.zone.zoneapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.zone.zoneapp.R;
import com.zone.zoneapp.model.ListItem;
import com.zone.zoneapp.model.Response;

import java.util.ArrayList;
import java.util.List;

public class RequestDetailActivity extends AppCompatActivity {

    private ListItem mItem;
    private TextView mDetailTextView;
    private FloatingActionButton mChatButton;
    private ArrayList<Response> mList;
    private ListView mListView;
    private ResponseListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    private void getList(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Responses");
        query.whereEqualTo("postId", mItem.getmId());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject i : objects) {
                    Response response = new Response(i.getString("userId"),i.getCreatedAt().toString(),i.getParseUser("userId").getUsername(),i.getString("text"));
                    mList.add(response);
                }
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatingActionButton mChatButton = (FloatingActionButton) findViewById(R.id.fab);
        mChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RequestDetailActivity.this, WriteResponseActivity.class);
                i.putExtra("postItem", mItem);
                startActivity(i);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mList = new ArrayList<Response>();
        //Response response = new Response("123","11","hp","body");
        //mList.add(response);


        mItem = (ListItem)getIntent().getSerializableExtra("ItemDetail");
        mDetailTextView = (TextView)findViewById(R.id.detail_desciprtion);
        mDetailTextView.setText(mItem.getmDetail());


        mListView = (ListView)findViewById(R.id.response_list_container);

        mAdapter = new ResponseListAdapter();
        mListView.setAdapter(mAdapter);
        getList();

    }


    private class ResponseListAdapter extends ArrayAdapter<Response>{

        public ResponseListAdapter() {
            super(RequestDetailActivity.this, 0, mList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try{
                if (convertView==null){
                    convertView = RequestDetailActivity.this.getLayoutInflater().inflate(R.layout.list_response,null);
                }

                Response response = getItem(position);


                TextView username = (TextView)convertView.findViewById(R.id.response_user_name);
                username.setText(response.getUsername());

                TextView time = (TextView)convertView.findViewById(R.id.response_time);
                time.setText(response.getTime());


                TextView text = (TextView)convertView.findViewById(R.id.response_text);
                text.setText(response.getText());
            }catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }
    }

}
