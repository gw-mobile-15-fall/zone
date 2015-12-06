package com.zone.zoneapp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zone.zoneapp.R;
import com.zone.zoneapp.model.ListItem;
import com.zone.zoneapp.model.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ListItem mItem;
    private TextView mDetailTextView;
    private FloatingActionButton mChatButton;
    private ArrayList<Response> mList;
    private ListView mListView;
    private ResponseListAdapter mAdapter;
    private Button mShowMapButton;
    private ParseUser mCurrentUesr;
    private LatLng mRequestLocation;
    private String mRequestSubject;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_request_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.refesh_button_detail){
            getList();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCurrentUesr = ParseUser.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mItem = (ListItem)getIntent().getSerializableExtra("ItemDetail");
        mDetailTextView = (TextView)findViewById(R.id.detail_desciprtion);
        mDetailTextView.setText("Description: " + mItem.getmDetail());
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

        MapFragment mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.request_detail_map);
        mMapFragment.getMapAsync(this);

        mRequestLocation = new LatLng(mItem.getmLatitude(),mItem.getmLongitude());
        mRequestSubject = mItem.getSubject();

        mListView = (ListView)findViewById(R.id.response_list_container);
        getList();

    }

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

    private void getList(){
        mList = new ArrayList<Response>();
        mAdapter = new ResponseListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                final Response response = (Response)parent.getItemAtPosition(position);
                if (response.getUserEmail().equals(ParseUser.getCurrentUser().getEmail())){
                    Toast.makeText(RequestDetailActivity.this,RequestDetailActivity.this.getString(R.string.can_not_add_yourself),Toast.LENGTH_SHORT).show();
                }
                else{
                    new AlertDialog.Builder(RequestDetailActivity.this)
                            .setTitle("Add to contact")
                            .setMessage("Add this user to your private contact list?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Contacts");
                                    query1.whereEqualTo("currentUser",mCurrentUesr.getEmail());

                                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Contacts");
                                    query2.whereEqualTo("currentUser",response.getUserEmail());

                                    List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                                    queries.add(query1);
                                    queries.add(query2);

                                    ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
                                    mainQuery.findInBackground(new FindCallback<ParseObject>() {
                                        public void done(List<ParseObject> results, ParseException e) {
                                            if (results.size()==0){
                                                ParseObject parseObject1 = new ParseObject("Contacts");
                                                ParseObject parseObject2 = new ParseObject("Contacts");
                                                parseObject1.addUnique("contactList",mCurrentUesr.getEmail()+"-"+mCurrentUesr.getObjectId());
                                                parseObject1.put("currentUser", response.getUserEmail());
                                                parseObject2.addUnique("contactList", response.getUserEmail() + "-" + response.getUserId());
                                                parseObject2.put("currentUser",mCurrentUesr.getEmail());
                                                parseObject1.saveInBackground();
                                                parseObject2.saveInBackground();
                                            }

                                            else if(results.size()==1){
                                                results.get(0).addAllUnique("contactList", Arrays.asList(mCurrentUesr.getEmail() + "-" + mCurrentUesr.getObjectId(),
                                                        response.getUserEmail() + "-" + response.getUserId()));
                                                results.get(0).saveInBackground();

                                                if (results.get(0).getString("currentUser").equals(response.getUserEmail())){
                                                    ParseObject parseObject = new ParseObject("Contacts");
                                                    parseObject.put("currentUser", mCurrentUesr.getEmail());
                                                    parseObject.addUnique("contactList", response.getUserEmail() + "-" + response.getUserId());
                                                    parseObject.saveInBackground();
                                                }
                                                else {
                                                    ParseObject parseObject = new ParseObject("Contacts");
                                                    parseObject.put("currentUser", response.getUserEmail());
                                                    parseObject.addUnique("contactList", mCurrentUesr.getEmail() + "-" + mCurrentUesr.getObjectId());
                                                    parseObject.saveInBackground();
                                                }

                                            }

                                            else if (results.size()==2){
                                                for (ParseObject p : results){
                                                    p.addAllUnique("contactList", Arrays.asList(mCurrentUesr.getEmail() + "-" + mCurrentUesr.getObjectId(),
                                                            response.getUserEmail() + "-" + response.getUserId()));
                                                    p.saveInBackground();

                                                }
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


            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Responses");
        query.whereEqualTo("postId", mItem.getmId());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject i : objects) {
                    Response response = new Response(i.getString("userId"), i.getString("userEmail"), i.getCreatedAt().toString(), i.getString("text"));
                    mList.add(response);
                }
                mAdapter.notifyDataSetChanged();

            }
        });
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
                TextView time = (TextView)convertView.findViewById(R.id.response_time);
                time.setText(response.getTime());
                TextView text = (TextView)convertView.findViewById(R.id.response_text);
                text.setText(response.getUserEmail()+": "+response.getText());
            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
