package com.zone.zoneapp.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zone.zoneapp.R;
import com.zone.zoneapp.model.ListItem;

public class RequestDetailActivity extends AppCompatActivity {

    private ListItem mListItem;
    private TextView mDetailTextView;
    private FloatingActionButton mChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton mChatButton = (FloatingActionButton) findViewById(R.id.fab);
        mChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mListItem = (ListItem)getIntent().getSerializableExtra("ItemDetail");
        mDetailTextView = (TextView)findViewById(R.id.detail_desciprtion);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView(){
        mDetailTextView.setText(mListItem.getmDetail());
    }
}
