
package com.zone.zoneapp.Activity;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by YangLiu on 10/19/2015.
 */
public class App extends Application {

    //initial parse service
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "TEAkAZ3fPkc2P7z4CPrziklJjMBYgB5VcMy2ByEo", "WFsWpRLUe55pDEzwvPwBt5ErQhuQ6Z6TETOPOn7b");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        //Firebase.setAndroidContext(this);


    }
}