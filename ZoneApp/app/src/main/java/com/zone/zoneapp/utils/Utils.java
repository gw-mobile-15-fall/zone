package com.zone.zoneapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by hpishepei on 11/1/15.
 */
public class Utils {
    private static final String TAG = "Utils";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json?key=";
    private static final String PLACE_URL = "&placeid=";
    public static void insertToParse(String object, HashMap<String,String> value){
        ParseObject parseObject = new ParseObject(object);
        Iterator it = value.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            parseObject.put((String) pair.getKey(), pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        Log.i("aaa","save");
        parseObject.saveInBackground();
    }

    public static LatLng getLatLngFromPlaceId(String placeId, Context context) throws ExecutionException, InterruptedException {
        LatLng result;
        String url = BASE_URL + Constants.GOOGLE_PLACES_SERVER_API_KEY + PLACE_URL + placeId;
        JsonObject placeDetails = Ion.with(context).load(url).asJsonObject().get();
        Double lat = Double.parseDouble(placeDetails.get("result").getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lat").getAsString());
        Double lng = Double.parseDouble(placeDetails.get("result").getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lng").getAsString());
        result = new LatLng(lat,lng);
        Log.d(TAG, "place detail retrieved");
        return result;
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
