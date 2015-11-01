
package com.zone.zoneapp.Activity;

import android.app.Application;

import com.parse.Parse;
import com.zone.zoneapp.R;

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