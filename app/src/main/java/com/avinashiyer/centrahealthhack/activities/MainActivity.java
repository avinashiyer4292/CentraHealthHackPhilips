package com.avinashiyer.centrahealthhack.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.adapters.CurrentAppointmentsAdapter;
import com.avinashiyer.centrahealthhack.model.ScheduledAppointment;
import com.avinashiyer.centrahealthhack.services.AlexaDoctorNotificationService;
import com.avinashiyer.centrahealthhack.services.EntryBeaconService;
import com.avinashiyer.centrahealthhack.utils.Constants;

import org.altbeacon.beacon.BeaconManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity{
    static int pollFlag=0, newPollFlag=-1;
    TextView estTime;
    Button btn;
    TabLayout tabLayout;
    ViewPager viewPager;
    String[] uArray = {Constants.BEACON_UUID}; //,"0x027961686f6f07","0x0266616365626f6f6b07"
    List<String> uuidList = new ArrayList(Arrays.asList(uArray));
    private BeaconManager mBeaconManager;
    private static final String TAG = "MainActivity";
    RecyclerView mRecyclerView;
    LinearLayoutManager llm;
    TextView emptyView;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Your Appointments");


        prefs = getSharedPreferences(Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        Log.d(TAG,"patient id in main: "+prefs.getInt("patientID",-1));
        Toast.makeText(MainActivity.this,"Your patient ID is: "+prefs.getInt("patientID",-1),Toast.LENGTH_SHORT).show();
        String urlToHit = Constants.url+Constants.get_current_appointments+prefs.getInt("patientID",-1);

        mRecyclerView = (RecyclerView)findViewById(R.id.currentAppointmentRecyclerView);
        emptyView = (TextView)findViewById(R.id.emptyview);
        if (prefs.getBoolean("booked", false)) {
            Log.d(TAG,"At least one appointment already booked!");
            emptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            getCurrentAppointments(urlToHit);
            Intent entryService = new Intent(this, EntryBeaconService.class);
            startService(entryService);
        }
        else {
            Log.d(TAG, "No bookings done till now!");
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

            Intent intent = getIntent();
            if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, true)) {
                Bundle parameters = intent.getExtras();
                if(parameters!=null) {
                    Log.d(TAG, "Parameters: " + parameters);
                    String idString = parameters.getString("id");
                    // Do something with idString
                    Log.d(TAG, "Received from deep link: " + idString);
                }
            }
            Intent i = new Intent(this, AlexaDoctorNotificationService.class);
            i.putExtra("patientID",prefs.getInt("patientID",-1));
            startService(i);

        }



    }

    private void getCurrentAppointments(String urlToHit){
        Log.d(TAG,"URL: "+urlToHit);
        final List<ScheduledAppointment> appointmentList = new ArrayList<>();

        llm = new LinearLayoutManager(this);

        JsonArrayRequest jRequest = new JsonArrayRequest(Request.Method.GET,urlToHit,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Current Appointments", response.toString());
                try{
                    for(int i=0;i<response.length();i++){
                        JSONObject j = response.getJSONObject(i);
                        String doctorName = j.getString("NAME");
                        String startTime = j.getString("AppointmentEST");
                        String endTime = j.getString("AppointmentET");
                        ScheduledAppointment a = new ScheduledAppointment();
                        Log.d(TAG,"start time received: "+startTime);
                        String sTime = a.formatTime(startTime),
                                eTime = a.formatTime(endTime);
                        String date = a.formatDate(startTime);
                        appointmentList.add(new ScheduledAppointment(doctorName,sTime,eTime,date));

                    }
                    mRecyclerView.setLayoutManager(llm);
                    mRecyclerView.setAdapter(new CurrentAppointmentsAdapter(appointmentList,MainActivity.this));

                }catch(JSONException e){}
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Current Appointments", "Error occurred: "+error.toString());
                    }
                });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jRequest.setRetryPolicy(policy);
        MyApplication.getInstance().addToRequestQueue(jRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.fitbit_icon:{
                    accessFitbitData();
                return true;
            }
            case R.id.patient_icon:{
                Toast.makeText(MainActivity.this,"Your patient ID is: "+prefs.getInt("patientID",-1),Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.doctor_icon:{
                //Toast.makeText(MainActivity.this,"Your patient ID is: "+prefs.getInt("patientID",-1),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,ShowAllDoctorsActivity.class);
                startActivity(i);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void accessFitbitData(){
        String url = Constants.AUTH_URI
                        +"?response_type=token&client_id="+Constants.CLIENT_ID
                        +"&redirect_uri="+Constants.REDIRECT_URI
                        +"&scope=activity%20heartrate%20nutrition%20sleep&expires_in="+Constants.EXPIRES_IN;
        Log.d(TAG,url);
        Log.d(TAG,"As url: "+Uri.parse(url));
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));


        //web.loadUrl(OAUTH_URL+"?redirect_uri="+REDIRECT_URI+"&response_type=code&client_id="+CLIENT_ID+"&scope="+OAUTH_SCOPE);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
