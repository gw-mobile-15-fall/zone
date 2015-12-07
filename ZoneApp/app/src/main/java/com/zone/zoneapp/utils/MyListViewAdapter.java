package com.zone.zoneapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zone.zoneapp.R;
import com.zone.zoneapp.model.ListItem;

import java.util.ArrayList;

/**
 * Created by YangLiu on 10/4/2015.
 */
public class MyListViewAdapter extends ArrayAdapter<ListItem>{
    private ArrayList<ListItem> items;

    public MyListViewAdapter(Context context, int textViewResourceId, ArrayList<ListItem> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item, null);
        }

        ListItem item = items.get(position);

        if (item != null) {
            TextView location = (TextView) v.findViewById(R.id.location);
            TextView time = (TextView) v.findViewById(R.id.time);
            TextView subject = (TextView) v.findViewById(R.id.subject);

            if (location != null) {
                location.setText("User: "+item.getUser());
            }

            if(time != null) {
                time.setText(item.getTime() );
            }

            if(subject != null) {
                subject.setText("Subject: " + item.getSubject());
            }
        }
        return v;
    }

}
