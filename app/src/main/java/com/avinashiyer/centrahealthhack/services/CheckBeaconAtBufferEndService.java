package com.avinashiyer.centrahealthhack.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.activities.MainActivity;
import com.avinashiyer.centrahealthhack.activities.ShowPrescriptionActivity;
import com.avinashiyer.centrahealthhack.receivers.PolledBeaconDetectedReceiver;
import com.avinashiyer.centrahealthhack.utils.Constants;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static com.avinashiyer.centrahealthhack.utils.Constants.APPTAG;

/**
 * Created by avinashiyer on 4/7/17.
 */

public class CheckBeaconAtBufferEndService extends Service implements BeaconConsumer,RangeNotifier,MonitorNotifier {

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(Constants.APPTAG,"Beacon detected!!");
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(Constants.APPTAG,"Beacon not detected!!");
    }

    private BeaconManager mBeaconManager;
    String[] uArray = {Constants.BEACON_UUID}; //,"0x027961686f6f07","0x0266616365626f6f6b07"
    List<String> uuidList = new ArrayList(Arrays.asList(uArray));
    static long pollCounter=0;

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

        Log.d(Constants.APPTAG,"did range in beacons called");
        pollCounter+=1;


        Log.d(Constants.APPTAG,"Poll value: "+pollCounter);
            for (Beacon beacon : collection) {
                Log.d(Constants.APPTAG,"Beacons found!! SHIT!! ");
                pollCounter=0;
                String url1 = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
                Log.d("UUID", "UUID IS: " + beacon.getId1());

                //uuidList.contains(beacon.getId1().toString()) &&
                if (uuidList.contains(beacon.getId1().toString()) && beacon.getBeaconTypeCode() == 0x10) {

                }
            }
        if(pollCounter>=20){
            try{
                mBeaconManager.stopRangingBeaconsInRegion(region);
                mBeaconManager.unbind(this);
                Calendar c = Calendar.getInstance();

                sendExitDoctorRoomData();
                Intent i = new Intent(this, ShowPrescriptionActivity.class);
                i.putExtra("actualEndTime",c.getTimeInMillis());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                stopSelf();
            }catch(RemoteException e){}
        }

    }
    private void sendExitDoctorRoomData(){
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        JSONObject map = new JSONObject();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(c.getTime());
        Log.d("Patient ID",""+prefs.getInt("patientID",-1));
        Log.d("Date",""+date);
        try{
            map.put("patientID",prefs.getInt("patientID",-1));
            map.put("docID",2);
            map.put("appointmentST",date);

        }catch(JSONException E){}
        final String requestBody = map.toString();

        StringRequest str = new StringRequest(Request.Method.POST,Constants.url+Constants.exit_doc_room,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("Exit Room service","Response received: "+response);
                        if(Integer.parseInt(response)==1){
                            SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                            prefs.edit().putInt("hasExit",1).commit();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.APPTAG,"Error!! "+error);
                    }
                }){


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        MyApplication.getInstance().addToRequestQueue(str);

    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(Constants.APPTAG,"Beacon service connected!!");
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.setRangeNotifier(this);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(Constants.APPTAG+" Check","Check beacon service started!!");
        mBeaconManager = MyApplication.getInstance().getBeaconManager();
        mBeaconManager.bind(this);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private long returnCounterValue(){
        pollCounter = System.currentTimeMillis();
        return pollCounter;
    }

    public CheckBeaconAtBufferEndService(){
        super();
    }


}
