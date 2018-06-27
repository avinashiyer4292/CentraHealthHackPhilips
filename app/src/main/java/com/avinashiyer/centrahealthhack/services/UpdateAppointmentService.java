package com.avinashiyer.centrahealthhack.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.activities.BookAppointmentActivity;
import com.avinashiyer.centrahealthhack.activities.MainActivity;
import com.avinashiyer.centrahealthhack.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by avinashiyer on 4/7/17.
 */

public class UpdateAppointmentService extends Service {

    SharedPreferences prefs;
    public UpdateAppointmentService(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs =getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);


        Log.d("Update appointment","update appointment service started!!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //timeUpdatePoll();
        int id = prefs.getInt("patientID",-1);
        startTimer(id);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void timeUpdatePoll(final int id){

        int hasExit = prefs.getInt("hasExit",-1);
        Log.d(Constants.APPTAG,"Has exited? "+hasExit);
        if(hasExit==1){
            stopSelf();
        }
        else{
            getDelay(id);
        }
    }
    private void getDelay(final int id){

        String url = Constants.url+Constants.get_delay+"?patientID="+id+"&docID=2";
        JsonArrayRequest str = new JsonArrayRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Update getDelay","Response received: "+response.toString());
                        try{
                               int isDeleted = Integer.parseInt(response.getJSONObject(0).getString("isDeleted").toString());
                                int isDataChanged = Integer.parseInt(response.getJSONObject(0).getString("isDataChanged").toString());
                                if(isDeleted==0 && isDataChanged>0){
                                    stoptimertask();
                                    resetIsDataChanged(id);
                                    displayNotification(isDataChanged);
                                }
                        }catch(Exception e){}

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.APPTAG,"Error!! "+error);
                    }
                });




        MyApplication.getInstance().addToRequestQueue(str);
    }

    private void resetIsDataChanged(int id){
        String url = Constants.APPTAG+Constants.reset_data_changed+"?patientID="+id+"&docID=2";
        StringRequest str = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("Update resetData","Response received: "+response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.APPTAG,"Error!! "+error);
                    }
                });
        MyApplication.getInstance().addToRequestQueue(str);

    }
    private void displayNotification(int delay){
        Log.d(Constants.APPTAG,"Displaying notification for time change!");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.notification_icon_24);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle("Hygieia");
        mBuilder.setContentText("Your appointment has been delayed by "+ delay+" minutes!");
        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,23,resultIntent,0);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
    private Timer timer;
    private TimerTask timerTask;

    public void startTimer(int id) {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask(id);

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 10000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask(final int id) {
        timerTask = new TimerTask() {
            public void run() {
                timeUpdatePoll(id);
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
