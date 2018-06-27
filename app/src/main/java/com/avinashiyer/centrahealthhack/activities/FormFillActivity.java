package com.avinashiyer.centrahealthhack.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.database.DatabaseHelper;
import com.avinashiyer.centrahealthhack.model.Patient;

import org.json.JSONObject;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

import static com.avinashiyer.centrahealthhack.utils.Constants.register_patient;
import static com.avinashiyer.centrahealthhack.utils.Constants.url;

public class FormFillActivity extends AppCompatActivity implements VerticalStepperForm {

    private static final String TAG= "FormFillActivity";
    private VerticalStepperFormLayout verticalStepperFormLayout;
    private EditText firstName, lastName, policyNumber, groupNumber, age,address,phone_number,gender;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fill);

        verticalStepperFormLayout = (VerticalStepperFormLayout)findViewById(R.id.verticalForm);

        String[] fields = {"Policy number","Group number","First Name","Last Name","Date of birth","Gender","Address","Phone Number"};

        VerticalStepperFormLayout.Builder.newInstance(verticalStepperFormLayout,fields,this,this)
                .primaryColor(R.color.colorPrimary)
                .primaryDarkColor(R.color.colorPrimaryDark)
                .init();

    }

    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case 2:
                {       firstName = new EditText(this);
                        firstName.setSingleLine(true);
                        firstName.setHint("First Name");
                        view = firstName;
                }
                break;
            case 0:
            {       policyNumber = new EditText(this);
                policyNumber.setSingleLine(true);
                policyNumber.setHint("Policy number");
                view = policyNumber;
            }
                break;
            case 1:
            {       groupNumber = new EditText(this);
                groupNumber.setSingleLine(true);
                groupNumber.setHint("Group Number");
                view = groupNumber;
            }
                break;

            case 3:

                {       lastName = new EditText(this);
                    lastName.setSingleLine(true);
                    lastName.setHint("Last Name");
                    view = lastName;
                }
            break;

            case 4:

            {       age = new EditText(this);
                age.setSingleLine(true);
                age.setHint("Date of birth");
                view = age;
            }
            break;

            case 5:

            {       gender = new EditText(this);
                gender.setSingleLine(true);
                gender.setHint("Gender");
                view = gender;
            }
            break;

            case 6:

            {       address = new EditText(this);
                address.setSingleLine(true);
                address.setHint("Address");
                view = address;
            }
            break;

            case 7:

            {       phone_number = new EditText(this);
                phone_number.setSingleLine(true);
                phone_number.setHint("Phone number");
                view = phone_number;
            }
            break;

            }
        return view;
    }

    @Override
    public void onStepOpening(int stepNumber) {
        switch(stepNumber){
            case 0:
                        verticalStepperFormLayout.setStepAsCompleted(0);
                        //verticalStepperFormLayout.goToNextStep();

            break;
            case 1:
                        verticalStepperFormLayout.setStepAsCompleted(1);

            break;
            case 2: verticalStepperFormLayout.setStepAsCompleted(2);break;
            case 3: verticalStepperFormLayout.setStepAsCompleted(3);break;
            case 4: verticalStepperFormLayout.setStepAsCompleted(4);break;
            case 5: verticalStepperFormLayout.setStepAsCompleted(5);break;
            case 6: verticalStepperFormLayout.setStepAsCompleted(6);break;
            case 7: verticalStepperFormLayout.setStepAsCompleted(7);break;

        }
    }

    @Override
    public void sendData() {
        prefs = getSharedPreferences("avinash", Context.MODE_PRIVATE);
        prefs.edit().putString("Form","filled");
        prefs.edit().commit();

        Log.d(TAG,"In send data method!!");
        DatabaseHelper dbHelper = new DatabaseHelper(FormFillActivity.this);
        Patient p  = new Patient(policyNumber.getText().toString(),
                                groupNumber.getText().toString(),
                                firstName.getText().toString(),
                                lastName.getText().toString(),
                                age.getText().toString(),
                                gender.getText().toString(),
                                address.getText().toString(),
                                phone_number.getText().toString());
        dbHelper.addPatient(p);
        postPatientData(p);

    }
    private void postPatientData(final Patient p){
        String getUrl = url+register_patient+"?pname="+p.getPolicy_number()+" "+p.getLast_name()
                        +"&dob="+p.getdob()+"&address="+p.getAddress()+"&policy_number="+p.getFirst_name();
        Log.d(TAG,"URL is: "+getUrl);
        //using GET for now, have to use POST for MC Project
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,"Response success!!  "+response);
                Intent i =new Intent(FormFillActivity.this,BookAppointmentActivity.class);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Response failure!!  "+error.toString());
                Intent i =new Intent(FormFillActivity.this,BookAppointmentActivity.class);
                startActivity(i);
            }
        });

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
