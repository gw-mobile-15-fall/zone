package com.zone.zoneapp;
/**
 * Created by YangLiu on 10/4/2015.
 */
public class ListItem {
    private String mLocation;
    private String mTime;
    private String mSubject;
    public String getLocation(){
        return this.mLocation;
    }
    public String getTime(){
        return this.mTime;
    }
    public String getSubject(){
        return this.mSubject;
    }
    public void setLocation(String location){
        this.mLocation = location;
    }
    public void setTime(String time){
        this.mTime = time;
    }
    public void setSubject(String subject){
        this.mSubject = subject;
    }
    public ListItem(String location, String time, String subject) {
        this.mLocation = location;
        this.mTime = time;
        this.mSubject = subject;
    }
}
