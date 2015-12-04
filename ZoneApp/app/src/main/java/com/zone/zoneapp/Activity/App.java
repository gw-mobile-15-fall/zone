
package com.zone.zoneapp.Activity;

import android.app.Application;

import com.firebase.client.Firebase;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.zone.zoneapp.utils.Constants;
/**
 * Created by YangLiu on 10/19/2015.
 */
public class App extends Application {

    //initial parse service and firebase
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, Constants.PARSE_APPLICATION_ID,Constants.PARSE_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        Firebase.setAndroidContext(this);


    }
}