package com.zone.zoneapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hpishepei on 12/5/15.
 */
public class PersistanceManager {

    public static int getRadius(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt("Radius_Settings",1);
    }

    public static void setRadius(Context context, int radius){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("Radius_Settings", radius);
        editor.apply();
    }
}
