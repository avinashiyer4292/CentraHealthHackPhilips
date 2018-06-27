package com.avinashiyer.centrahealthhack;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by avinashiyer on 4/1/17.
 */

public class MyApplication extends Application {
    public static final String TAG = MyApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private BeaconManager mBeaconManager;

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Iconify
                .with(new FontAwesomeModule());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("gotham-book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public BeaconManager getBeaconManager(){
        Log.d(TAG, "Trying to get beacon manager instance...");
        if(mBeaconManager==null){
            mBeaconManager= BeaconManager.getInstanceForApplication(this);
            Log.d(TAG,"Beacon parsers old size: "+mBeaconManager.getBeaconParsers().size());

            // Detect the main Eddystone-UID frame:
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-21v"));
            Log.d(TAG,"Beacon parsers new size: "+mBeaconManager.getBeaconParsers().size());
        }
        return mBeaconManager;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
