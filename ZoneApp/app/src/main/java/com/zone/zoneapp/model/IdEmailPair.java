package com.zone.zoneapp.model;

import java.io.Serializable;

/**
 * Created by hpishepei on 12/3/15.
 */
public class IdEmailPair implements Serializable{
    private String id;
    private String email;
    private boolean selectFlag;

    public IdEmailPair() {
        this.email = null;
        this.id = null;
        this.selectFlag = false;
    }

    public IdEmailPair(String email, String id, boolean flag) {
        this.email = email;
        this.id = id;
        this.selectFlag = flag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }
}
