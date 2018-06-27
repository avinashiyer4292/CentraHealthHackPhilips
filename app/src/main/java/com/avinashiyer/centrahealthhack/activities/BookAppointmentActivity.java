package com.avinashiyer.centrahealthhack.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.adapters.DoctorRecyclerViewAdapter;
import com.avinashiyer.centrahealthhack.model.Doctor;
import com.avinashiyer.centrahealthhack.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


import static com.avinashiyer.centrahealthhack.utils.Constants.url;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BookAppointmentActivity extends AppCompatActivity {
    private static final String TAG="BookAppointmentActivity";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager llm;
    private List<Doctor> doctors;
    private DoctorRecyclerViewAdapter adapter;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_appointment);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);
        doctors = new ArrayList<>();
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);final ProgressDialog pDialog = new ProgressDialog(this);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.horizontal_divider);pDialog.setMessage("Loading doctors...");
        horizontalDecoration.setDrawable(horizontalDivider);pDialog.show();
        mRecyclerView.addItemDecoration(horizontalDecoration);
        SharedPreferences prefs= getSharedPreferences(Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        String url = Constants.url+Constants.get_sym_doctors+prefs.getInt("patientID",-1);
        Log.d(TAG,"symptom doctor url is: "+url);
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();
                             try {
                                 for (int i = 0; i < response.length(); i++) {
                                     JSONObject jObj = response.getJSONObject(i);
                                     int docId = Integer.parseInt(jObj.getString("Doc_ID"));
                                     String docName = jObj.getString("Name");
                                     String speciality = jObj.getString("Speciality");
                                     Doctor d = new Doctor(docId,docName,speciality);
                                     doctors.add(d);
                                 }
                             }catch(JSONException e){

                             }

                        adapter = new DoctorRecyclerViewAdapter(doctors,BookAppointmentActivity.this);
                        mRecyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                          Log.d(TAG,"Volley error occured!! "+error.toString());
            }
        });

        MyApplication.getInstance().addToRequestQueue(jsonArrReq);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}