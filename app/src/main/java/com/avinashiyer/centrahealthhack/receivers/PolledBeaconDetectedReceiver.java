package com.avinashiyer.centrahealthhack.receivers;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.util.Log;

import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.services.CheckBeaconAtBufferEndService;
import com.avinashiyer.centrahealthhack.services.UpdateAppointmentService;
import com.avinashiyer.centrahealthhack.utils.Constants;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by avinashiyer on 4/7/17.
 */

public class PolledBeaconDetectedReceiver extends BroadcastReceiver implements BeaconConsumer,RangeNotifier {
    String[] uArray = {Constants.BEACON_UUID};
    List<String> uuidList = new ArrayList(Arrays.asList(uArray));

    private SharedPreferences prefs;
    private static final String TAG = "PollBeacon";
    private BeaconManager mBeaconManager;
    static long pollCounter=0;
    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.setRangeNotifier(this);
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        Log.d(TAG,"Service unbinded!!");
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        Log.d(TAG,"Service binded!");
        return false;
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

        for(Beacon beacon:collection){
            String url1 = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
            Log.d("UUID", "UUID IS: " + beacon.getId1());

//            //uuidList.contains(beacon.getId1().toString()) &&
            if (uuidList.contains(beacon.getId1().toString()) && beacon.getBeaconTypeCode() == 0x10) {

                if(System.currentTimeMillis()-returnCounterValue()>15000) {
                        try {
                            Log.d(TAG, "Stopping detecting beacons!!");
                            mBeaconManager.stopRangingBeaconsInRegion(region);
                        } catch (RemoteException e) {
                    }
                    pollCounter=-1;
                    Log.d(TAG, "posting exit room data!!");
                }
            }
        }

    }

    public PolledBeaconDetectedReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"Received broadcast for starting long polling!!");

        int poll = intent.getIntExtra("poll",-1);
        Log.d(TAG, "Poll value: "+poll);

        Log.d(TAG,"Initialising beacon manager!!");
        mBeaconManager = MyApplication.getInstance().getBeaconManager();
        Log.d(TAG,"Size: "+mBeaconManager.getBeaconParsers().size());
        if(mBeaconManager!=null) {
            Log.d(TAG,"Beacon manager is not null!!");
            //mBeaconManager.bind();

        }

    }

    private long returnCounterValue(){
        pollCounter = System.currentTimeMillis();
        return pollCounter;
    }
}
