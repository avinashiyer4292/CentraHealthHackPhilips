package com.avinashiyer.centrahealthhack.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

public class ShowAllDoctorsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager llm;
    private List<Doctor> doctors;
    private DoctorRecyclerViewAdapter adapter;
    private static final String TAG="ShowAllDocotrsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_doctors);
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        llm = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(llm);
//        doctors = new ArrayList<>();
//        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                DividerItemDecoration.VERTICAL);final ProgressDialog pDialog = new ProgressDialog(this);
//        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.horizontal_divider);pDialog.setMessage("Loading doctors...");
//        horizontalDecoration.setDrawable(horizontalDivider);pDialog.show();
//        mRecyclerView.addItemDecoration(horizontalDecoration);
//        SharedPreferences prefs= getSharedPreferences(Constants.APPTAG, Context.MODE_PRIVATE);

//        JsonArrayRequest jsonArrReq = new JsonArrayRequest(Request.Method.GET,
//                url+Constants.get_all_doctors, null,
//                new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//                        pDialog.hide();
//                        try {
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject jObj = response.getJSONObject(i);
//                                int docId = Integer.parseInt(jObj.getString("Doc_ID"));
//                                String docName = jObj.getString("Name");
//                                String speciality = jObj.getString("Speciality");
//                                Doctor d = new Doctor(docId,docName,speciality);
//                                doctors.add(d);
//                            }
//                        }catch(JSONException e){
//
//                        }
//
//                        adapter = new DoctorRecyclerViewAdapter(doctors,ShowAllDoctorsActivity.this);
//                        mRecyclerView.setAdapter(adapter);
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG,"Volley error occured!! "+error.toString());
//            }
//        });
//
//        MyApplication.getInstance().addToRequestQueue(jsonArrReq);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
