package com.avinashiyer.centrahealthhack.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.activities.BookAppointmentActivity;
import com.avinashiyer.centrahealthhack.activities.FormFillActivity2;
import com.avinashiyer.centrahealthhack.activities.MainActivity;
import com.avinashiyer.centrahealthhack.model.Appointment;
import com.avinashiyer.centrahealthhack.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by avinashiyer on 4/2/17.
 */

public class ScheduleAdapter extends BaseAdapter implements View.OnClickListener {

    List<Appointment> slots;
    Context context;
    String docName;
    ScheduleBookClick s;
    public ScheduleAdapter(String docName,List<Appointment> slots, Context context, ScheduleBookClick listener){
        this.slots = slots;
        this.context = context;
        this.docName = docName;
        this.s = listener;


    }
    @Override
    public int getCount() {
        return slots.size();
    }

    @Override
    public Object getItem(int i) {
        return slots.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.single_schedule_layout, parent, false);
        }

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.listText);
        Button btn = (Button)
                convertView.findViewById(R.id.listButton);

        btn.setTag(pos+" "+slots.get(pos).getSlotID());
        Log.d(Constants.APPTAG,"Slot ID going to be clicked! "+slots.get(pos).getSlotID());
        btn.setOnClickListener(this);
        //sets the text for item name and item description from the current item object
        textViewItemName.setText(slots.get(pos).getStartTime()+" - "+slots.get(pos).getEndTime());



        // returns the view for the current row
        return convertView;
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.listButton:{

                    String[] data = view.getTag().toString().split(" ");
                    int pos = Integer.parseInt(data[0]);
                    int slot = Integer.parseInt(data[1]);
                    Log.d(Constants.APPTAG,"position: "+pos +" slot:"+slot);
                    Log.d(Constants.APPTAG,"Patient: "+slots.get(pos).getPatientID());
                    Log.d(Constants.APPTAG,"Doctor: "+slots.get(pos).getDoctorID());
                    Log.d(Constants.APPTAG,"Slot: "+slots.get(pos).getSlotID());
                    Log.d(Constants.APPTAG,"start time: "+slots.get(pos).getStartTime());
                    s.onScheduleClicked(slots
                                        ,slots.get(pos).getPatientID()
                                        ,slots.get(pos).getDoctorID()
                                        ,slot
                                        ,slots.get(pos).getStartTime());

            }
        }
    }

    public interface ScheduleBookClick{
        void onScheduleClicked(List<Appointment> slots,int p, int d, int slot, String startTime);
    }
}
