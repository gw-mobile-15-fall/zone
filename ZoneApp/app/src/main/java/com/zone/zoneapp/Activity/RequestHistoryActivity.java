package com.zone.zoneapp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zone.zoneapp.model.ListItem;
import com.zone.zoneapp.R;

import java.util.ArrayList;

public class RequestHistoryActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_history);
        populateListView();
    }
    //The following populate method may still contain problems
    private void populateListView(){
        ListView requestHistoryListView = (ListView) findViewById(R.id.requestHistoryList);
        ArrayList<ListItem> array = new ArrayList<>();
        ListItem request1  = new ListItem("SEH","10-04-15", "Lost Book");
        ListItem request2  = new ListItem("Gelman","10-05-15", "IT help needed");
        array.add(request1);
        array.add(request2);
        //TODO During practial implementation, the populateListView Method may want to take in an ArrayList as input.
        //eg. refresh the list==> populate using the new list.
        ArrayAdapter<ListItem> adapter = new MyAdapter(this, R.layout.list_item,array);
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
