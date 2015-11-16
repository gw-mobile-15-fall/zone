package com.zone.zoneapp.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.zone.zoneapp.R;
import com.zone.zoneapp.model.ListItem;
import com.zone.zoneapp.model.Response;

public class WriteResponseActivity extends AppCompatActivity {

    private EditText mTextEditeText;
    private String mText;
    private Response mResponse;
    private ParseUser mUser;
    private ListItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_response);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mItem = (ListItem)getIntent().getSerializableExtra("postItem");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addResponse();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mTextEditeText = (EditText)findViewById(R.id.write_response);
        mUser = ParseUser.getCurrentUser();
        mResponse = null;
        mTextEditeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mText = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addResponse(){
        ParseObject parseObject = new ParseObject("Responses");
        parseObject.put("userId", mUser);
        parseObject.put("postId", mItem.getmId());
        parseObject.put("text",mText);

        parseObject.saveInBackground();
        this.finish();
    }

}
