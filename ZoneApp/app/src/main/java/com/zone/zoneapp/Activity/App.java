
package com.zone.zoneapp.Activity;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by YangLiu on 10/19/2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "TEAkAZ3fPkc2P7z4CPrziklJjMBYgB5VcMy2ByEo", "WFsWpRLUe55pDEzwvPwBt5ErQhuQ6Z6TETOPOn7b");
    }
}