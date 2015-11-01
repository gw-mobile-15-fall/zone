package com.zone.zoneapp.utils;

import android.util.Log;

import com.parse.ParseObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hpishepei on 11/1/15.
 */
public class Utils {


    public static void insertToParse(String object, HashMap<String,String> value){
        ParseObject parseObject = new ParseObject(object);
        Iterator it = value.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            parseObject.put((String) pair.getKey(), (String) pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        Log.i("aaa","save");
        parseObject.saveInBackground();
    }
}
