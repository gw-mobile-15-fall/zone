package com.zone.zoneapp.model;

import android.location.Location;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by YangLiu on 11/1/2015.
 */
@ParseClassName("Posts")
public class Post extends ParseObject {
    public void setLocation (ParseGeoPoint location){
        this.put("postLocation",location);
    }
    public void setText (String text){
        this.put("postText",text);
    }
    public void setOwner(ParseUser owner){
        this.put("postOwner",owner);
    }
    public ParseGeoPoint getLocation(){
        return this.getParseGeoPoint("postLocation");
    }
    public String getText(){
        return this.getString("postText");
    }
    public ParseUser getOwner(){
        return this.getParseUser("postOwner");
    }
    public Post(ParseGeoPoint location, String text, ParseUser owner){
        this.setLocation(location);
        this.setText(text);
        this.setOwner(owner);
    }
    public Post(){}


}
