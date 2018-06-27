package com.avinashiyer.centrahealthhack.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.avinashiyer.centrahealthhack.R;

import java.util.Calendar;

public class ShowPrescriptionActivity extends AppCompatActivity {

    private static final String TAG = "PRESCRIPTIONACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prescription);
        Bundle extras = getIntent().getExtras();
        Calendar c=Calendar.getInstance();
        if(extras!=null){
            long time = extras.getLong("actualEndTime");
            Log.d(TAG,"Time obtained: "+time);

            c.setTimeInMillis(time);
        }
        Log.d(TAG,"Calendar object: "+c.getTime());

        Toast.makeText(ShowPrescriptionActivity.this,"You can view your prescription here when the hospital uploads it",Toast.LENGTH_LONG).show();
    }


}
