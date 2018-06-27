package com.avinashiyer.centrahealthhack.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.activities.BookAppointmentActivity;
import com.avinashiyer.centrahealthhack.utils.Constants;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by avinashiyer on 4/12/17.
 */

public class AlexaDoctorNotificationService extends Service {

    SharedPreferences prefs;
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
                getNotification(id);
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
    public AlexaDoctorNotificationService(){
        super();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences(Constants.APPTAG,Context.MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.APPTAG,"Notification retrieval service started");
        final int id = intent.getIntExtra("patientID",-1);
       startTimer(id);
        return START_NOT_STICKY;
    }

    private void getNotification(int id){
        //SharedPreferences prefs = getSharedPreferences(Constants.APPTAG,Context.MODE_PRIVATE);
        String url = Constants.url + Constants.get_alexa_status+id;
        Log.d(Constants.APPTAG,"Get notification called! "+url);
        StringRequest str = new StringRequest(Request.Method.GET,
                            url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Alexa notification","Notification response: "+response);
                        if(Integer.parseInt(response)==1){
                            //display notification
                            Log.d(Constants.APPTAG,"Display notification and stop service");
                            displayNotification();
                            //stoptimertask();
                            //stopSelf();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.APPTAG,"Error: "+error);
                    }
                });

        MyApplication.getInstance().addToRequestQueue(str);
    }

    private void displayNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.notification_icon_24);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle("Hygieia");
        mBuilder.setContentText("Doctors list available!");
        Intent resultIntent = new Intent(this, BookAppointmentActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,22,resultIntent,0);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
