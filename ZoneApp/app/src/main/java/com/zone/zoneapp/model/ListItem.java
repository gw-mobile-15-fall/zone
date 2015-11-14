package com.zone.zoneapp.model;

import com.parse.ParseGeoPoint;

import java.io.Serializable;

/**
 * Created by YangLiu on 10/4/2015.
 */
public class ListItem implements Serializable{
    private String mUser;
    private String mTime;
    private String mSubject;
    private ParseGeoPoint mLocation;
    public String getUser(){
        return this.mUser;
    }
    public String getTime(){
        return this.mTime;
    }
    public String getSubject(){
        return this.mSubject;
    }
    public void setUser(String user){
        this.mUser = user;
    }
    public void setTime(String time){
        this.mTime = time;
    }
    public void setSubject(String subject){
        this.mSubject = subject;
    }

    public ParseGeoPoint getLocation() {
        return mLocation;
    }

    public void setLocation(ParseGeoPoint mLocation) {
        this.mLocation = mLocation;
    }

    public ListItem(String user, String time, String subject, ParseGeoPoint location) {
        this.mUser = user;
        this.mTime = time;
        this.mSubject = subject;
        this.mLocation = location;
    }
}
