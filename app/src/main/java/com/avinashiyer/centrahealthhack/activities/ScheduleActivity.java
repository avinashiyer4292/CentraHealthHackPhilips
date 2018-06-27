package com.avinashiyer.centrahealthhack.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.adapters.ScheduleAdapter;
import com.avinashiyer.centrahealthhack.model.Appointment;
import com.avinashiyer.centrahealthhack.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ScheduleActivity extends AppCompatActivity implements ScheduleAdapter.ScheduleBookClick {
    int[] images = {R.drawable.face5,
            R.drawable.face6,
            R.drawable.face3,
            R.drawable.face10,
            R.drawable.face1,
            R.drawable.face2,
            R.drawable.face7,
            R.drawable.face8,
            R.drawable.face9,
            R.drawable.face4};
    CircleImageView docImage;
    TextView docName, docSpt;
    ListView lv;
    int docId=-1;
    String name=null, speciality=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            docId = extras.getInt("doc_id");
            name = extras.getString("doc_name");
            speciality = extras.getString("speciality");
        }
        Log.d(Constants.APPTAG,"Doc ID: "+docId+" ,Name: "+name+" ,speciality"+speciality);

//        int docId = 1;
//        String name = "Patrick Dobra";
//        String speciality = "Physician";

        docImage = (CircleImageView)findViewById(R.id.docImage);
        docImage.setImageResource(images[docId-1]);

        docName = (TextView)findViewById(R.id.docName);
        docSpt = (TextView)findViewById(R.id.docSpt);

        docName.setText(name);
        docSpt.setText(speciality);

        lv = (ListView)findViewById(R.id.scheduleListView);
        //String[] slots = {"11:00 - 11:30 AM", "13:00-13:30 PM","14:00 - 14:30 PM", "15:00 - 15:30 PM"};

        final List<Appointment> slots = new ArrayList<>();
        String url = Constants.url+Constants.get_available_slots+docId;
        JsonArrayRequest jArr = new JsonArrayRequest(Request.Method.GET,url
                                                ,null, new Response.Listener<JSONArray>(){
                                                    @Override
                                                    public void onResponse(JSONArray response) {
                                                        Log.d(Constants.APPTAG,"Response received: "+response.toString());

                                                        for(int i=0;i<response.length();i++) {
                                                            try {
                                                                JSONObject jsonObject = response.getJSONObject(i);
                                                                int slotID = jsonObject.getInt("Slot_ID");
                                                                String startTime = jsonObject.getString("ST");
                                                                String endTime = jsonObject.getString("ET");

                                                                startTime = startTime.substring(0,startTime.lastIndexOf(':'));
                                                                endTime = endTime.substring(0,endTime.lastIndexOf(':'));
                                                                SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                                                int patientID = prefs.getInt("patientID",-1);

                                                                slots.add(new Appointment(patientID,docId,slotID,startTime,endTime));


                                                                } catch (JSONException E) {
                                                            }
                                                        }
                                                        lv.setAdapter(new ScheduleAdapter(name,slots,ScheduleActivity.this, ScheduleActivity.this));

                                                    }
                                                },
                                                        new Response.ErrorListener(){
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {

                                                            }
                                                        });

        MyApplication.getInstance().addToRequestQueue(jArr);





        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Log.d(Constants.APPTAG,"Position: "+l);
                Button b = (Button)view.findViewById(R.id.listButton);
                b.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {

                    }
                });



            }
        });

    }
    private void sendAppointmentData(List<Appointment> slots,int position){
        JSONObject map = new JSONObject();
        Log.d(Constants.APPTAG,"Patient ID: "+slots.get(position).getPatientID());
        Log.d(Constants.APPTAG,"Doctor ID: "+slots.get(position).getDoctorID());
        Log.d(Constants.APPTAG,"Slot ID: "+slots.get(position).getSlotID());
        Log.d(Constants.APPTAG,"Appointment start: "+slots.get(position).getStartTime());
        try{
            Calendar c = Calendar.getInstance();
            map.put("patientID",slots.get(position).getPatientID());
            map.put("docID",slots.get(position).getDoctorID());
            map.put("slotID",slots.get(position).getSlotID());
            map.put("appointmentST",getDate(slots.get(position).getStartTime(),c));

        }catch(JSONException E){}
        final String requestBody = map.toString();

        StringRequest str = new StringRequest(Request.Method.POST, Constants.url+Constants.schedule_appointment,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d(Constants.APPTAG,"Response received: "+response);
                        //SharedPreferences prefs = getSharedPreferences()
//
//                        Intent i =new Intent(ScheduleActivity.this,MainActivity.class);
//                        startActivity(i);
//                        finish();

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
    public void onScheduleClicked(List<Appointment> slots,int p, int d, int slot, String startTime) {
        sendAppointmentData(slots,slot);
        SharedPreferences prefs= getSharedPreferences(Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        prefs.edit().putBoolean("booked",true).commit();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScheduleActivity.this);
        alertDialogBuilder.setMessage("Appointment Booked!");
        alertDialogBuilder.setIcon(R.drawable.check);
        alertDialogBuilder.setTitle("Status");
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent i = new Intent(ScheduleActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private String getDate(String time, Calendar c){
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(c.getTime()).toString()+" "+time;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
