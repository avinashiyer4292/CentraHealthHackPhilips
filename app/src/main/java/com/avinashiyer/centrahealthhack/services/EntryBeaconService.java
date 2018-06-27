package com.avinashiyer.centrahealthhack.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.activities.BookAppointmentActivity;
import com.avinashiyer.centrahealthhack.activities.FormFillActivity2;
import com.avinashiyer.centrahealthhack.activities.MainActivity;
import com.avinashiyer.centrahealthhack.utils.Constants;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by avinashiyer on 4/9/17.
 */

public class EntryBeaconService extends Service implements BeaconConsumer,RangeNotifier{


    private static final String TAG = "EntryBeaconService";
    private BeaconManager mBeaconManager;
    String[] uArray = {Constants.BEACON_UUID}; //,"0x027961686f6f07","0x0266616365626f6f6b07"
    List<String> uuidList = new ArrayList(Arrays.asList(uArray));
    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        for (Beacon beacon : collection) {

            String url1 = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
            Log.d("UUID", "UUID IS: " + beacon.getId1());

            //uuidList.contains(beacon.getId1().toString()) &&
            if (uuidList.contains(beacon.getId1().toString()) && beacon.getBeaconTypeCode() == 0x10) {

                //mBeaconManager.unbind(this);
                try {
                    Log.d(TAG,"Stopping detecting beacons!!");
                    mBeaconManager.stopRangingBeaconsInRegion(region);
                    mBeaconManager.unbind(this);

                }catch(RemoteException e){}

                sendEnterDoctorRoomData();
                sendPatientEntryData();
                Log.d(TAG, "Beacon detected!!");
                initBeaconPollingService(5);

                Intent i = new Intent(this, UpdateAppointmentService.class);
                startService(i);

                stopSelf();

            }
        }
    }

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

    private void initBeaconPollingService(int minutesAfter){

        Log.d(TAG,"Polling service initialised!!");
        Calendar target_cal = new GregorianCalendar();
        target_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR, 1);
//        calendar.set(Calendar.MINUTE, 40);
//        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.setTimeInMillis(System.currentTimeMillis());

        Log.d(TAG, "Target calendar: "+calendar.getTime());
        Intent intent = new Intent(this, CheckBeaconAtBufferEndService.class);
        intent.putExtra("poll",0);
        PendingIntent pintent = PendingIntent.getService(this, 1, intent, 0);

        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC, System.currentTimeMillis()+(minutesAfter*1000), pintent);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG,"Entry beacon service started!");

        mBeaconManager = MyApplication.getInstance().getBeaconManager();
        mBeaconManager.bind(this);
        return START_NOT_STICKY;
    }

    private void sendPatientEntryData(){
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        JSONObject map = new JSONObject();
        try{
            map.put("patientID",prefs.getInt("patientID",-1));
            //map.put("docID",2);

        }catch(JSONException E){}
        final String requestBody = map.toString();
        Log.d("sednign patient entry","patien is entering.....");
        String url = "http://10.136.100.144:4000/socket";
        StringRequest str = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d(Constants.APPTAG,"Response received: "+response);

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

    private void sendEnterDoctorRoomData(){
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        JSONObject map = new JSONObject();
        try{
            map.put("patientID",prefs.getInt("patientID",-1));
            map.put("docID",2);

        }catch(JSONException E){}
        final String requestBody = map.toString();

        StringRequest str = new StringRequest(Request.Method.POST,Constants.url+Constants.enter_doc_room,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d(Constants.APPTAG,"Response received: "+response);

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
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Entry beacon service created!");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
