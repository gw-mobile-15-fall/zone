package com.zone.zoneapp.model;

import java.io.Serializable;

/**
 * Created by YangLiu on 10/4/2015.
 */
public class ListItem implements Serializable{
    private String mUser;
    private String mTime;
    private String mSubject;
    private Double mLatitude;
    private Double mLongitude;
    private String mDetail;
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



    public ListItem(String user, String time, String subject, Double latitude, Double longitude, String text) {
        this.mUser = user;
        this.mTime = time;
        this.mSubject = subject;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mDetail = text;
    }

    public Double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public Double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmDetail() {
        return mDetail;
    }

    public void setmDetail(String mDetail) {
        this.mDetail = mDetail;
    }
}
