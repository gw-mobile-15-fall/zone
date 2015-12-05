package com.zone.zoneapp.model;

/**
 * Created by hpishepei on 11/16/15.
 */
public class Response {
    private String userId;
    private String userEmail;
    private String time;
    private String text;

    public Response(String id, String email, String time, String text){
        this.userId = id;
        this.text = text;
        this.time = time;
        this.userEmail = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
