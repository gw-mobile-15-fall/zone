package com.zone.zoneapp.model;

/**
 * Created by hpishepei on 11/16/15.
 */
public class Response {
    private String id;
    private String time;
    private String username;
    private String text;

    public Response(String id, String time, String username, String text){
        this.id = id;
        this.text = text;
        this.time = time;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
