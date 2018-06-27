package com.avinashiyer.centrahealthhack.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.adapters.FitbitDataAdapter;
import com.avinashiyer.centrahealthhack.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DeepLink("fitbitmcapp://avinashiyer.com/auth/fitbit/{id}")
public class FitbitActivity extends AppCompatActivity {

    private GridView gridView;
    private ProgressDialog pDialog;
    private static final String TAG ="FitbitActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit);
        Uri data = getIntent().getData();
        Intent intent = getIntent();
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, true)) {
            Bundle parameters = intent.getExtras();
            Log.d(TAG,"Parameters: "+parameters);
            String idString = parameters.getString("id");
            // Do something with idString
            Log.d(TAG,"Received from deep link: "+idString);
        }
        Log.d("FitbitActivity","Data: "+data.getPath()+" - "+data.toString());
        String returnUrl = data.toString();
        returnUrl = returnUrl.substring(returnUrl.indexOf('=')+1,returnUrl.indexOf('&'));
        Log.d(TAG,"Access token: "+returnUrl);


        gridView = (GridView)findViewById(R.id.fitbitGridview);

        String[] d = new String[]{Constants.steps,Constants.kmsWalked,Constants.floors,Constants.activeMinutes
                        ,Constants.calories,Constants.heart_rate};
        List<String> list = new ArrayList<>(Arrays.asList(d));

        d = new String[]{"Steps", "Kms walked","Floors climbed","Active minutes","Calories burned","Heart Rate"};
        gridView.setAdapter(new FitbitDataAdapter(this,list,d));


        //getStepsData(returnUrl);
    }

    private void getStepsData(final String accessToken){

        String url = "https://api.fitbit.com/1/user/3S75LN/profile.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,"Response obtained!! "+response.toString());
                try {
                    JSONObject j = response.getJSONObject("user");
                    Log.d(TAG,"User json object: "+j.toString());
                    int steps = j.getInt("averageDailySteps");
                    Log.d(TAG,"Average daily steps: "+steps);
                    //pDialog.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error in fetching response");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+accessToken);
                Log.d(TAG,"Header: "+map.get("Authorization"));
                return map;
            }
        };
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
