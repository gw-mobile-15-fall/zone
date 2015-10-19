
package com.zone.zoneapp;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by YangLiu on 10/19/2015.
 */
public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
    }
}