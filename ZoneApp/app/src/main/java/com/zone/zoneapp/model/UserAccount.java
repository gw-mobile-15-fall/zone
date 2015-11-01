package com.zone.zoneapp.model;

/**
 * Created by hpishepei on 10/4/15.
 */
public class UserAccount {

    private String mUserName;
    private String mPassword;
    private String mEmail;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }
}
