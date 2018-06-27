package com.avinashiyer.centrahealthhack.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.avinashiyer.centrahealthhack.MyApplication;
import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.adapters.FormFillAdapter;
import com.avinashiyer.centrahealthhack.utils.Constants;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import info.hoang8f.android.segmented.SegmentedGroup;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FormFillActivity2 extends AppCompatActivity {

    TextInputEditText firstName,lastName,city,state,address,insurance, phone;
    SegmentedGroup genderGroup;
    TextView date;
    SwitchCompat switchButton;
    Button submit;
    String gText="Male";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fill2);
        Log.d(Constants.APPTAG, "Form fill activity");
        firstName = (TextInputEditText)findViewById(R.id.formFirstName);
        lastName = (TextInputEditText)findViewById(R.id.formLastName);
        city = (TextInputEditText)findViewById(R.id.formCity);
        state = (TextInputEditText)findViewById(R.id.formState);
        address = (TextInputEditText)findViewById(R.id.formStreetAddress);
        insurance = (TextInputEditText)findViewById(R.id.formInsurance);
        phone = (TextInputEditText)findViewById(R.id.formPhone);

        genderGroup = (SegmentedGroup)findViewById(R.id.formGenderSelect);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dpg = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };
        date = (TextView)findViewById(R.id.formDOBSelect);
        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FormFillActivity2.this, dpg, 1990, 0, 1).show();
            }
        });
        switchButton = (SwitchCompat)findViewById(R.id.formHispanicSelect);

        submit = (Button)findViewById(R.id.formSubmit);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    private void showDialog(){
        final AlertDialog alertBuilder = new AlertDialog.Builder(FormFillActivity2.this)
                                            .setTitle("Submit data")
                                            .setMessage("Are you sure you want to submit data?")
                                            .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    showSignatureDialog();
                                                    //sendData();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            }).create();
        alertBuilder.show();
    }

    private void showSignatureDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View signLayout = inflater.inflate(R.layout.signature_dialog_view,null);
        //Button b = (Button)signLayout.findViewById(R.id.signSubmit);
        final AlertDialog alert = new AlertDialog.Builder(FormFillActivity2.this).create();
        final SignaturePad sp = (SignaturePad)signLayout.findViewById(R.id.signature_pad);
        sp.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                String myBase64Image = encodeToBase64(sp.getSignatureBitmap(), Bitmap.CompressFormat.JPEG, 100);
                Log.d(Constants.APPTAG,"Base 64 signature: "+myBase64Image);
                sendData(myBase64Image);
                alert.dismiss();

            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });

        alert.setView(signLayout);
        alert.show();




    }
    private void sendData(String imageData){
        String first = firstName.getText().toString();
        String last = lastName.getText().toString();
        String cText = city.getText().toString();
        String sText = state.getText().toString();
        String aText = address.getText().toString();
        String iText = insurance.getText().toString();
        String phText = phone.getText().toString();
        String dob = date.getText().toString();
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton)findViewById(i);
                 String d= rb.getText().toString();
                Log.d(Constants.APPTAG,"Gender selected: "+d);
                gText=d;
            }
        });
        String hText="";
        if(switchButton.isChecked())
            hText="Hispanic/Latino";

        postData(first,last,gText,dob,aText,cText,sText,hText,iText,phText);

    }

    private void postData(final String f,final String l,final String g,
                          final String d, final String a,final String c,final String s,
                          final String h,final String i,final String p){
        Log.d(Constants.APPTAG,"F: "+f);
        Log.d(Constants.APPTAG,"L: "+l);
        Log.d(Constants.APPTAG,"G: "+g);
        Log.d(Constants.APPTAG,"A: "+a);
        Log.d(Constants.APPTAG,"C: "+c);
        Log.d(Constants.APPTAG,"S: "+s);
        Log.d(Constants.APPTAG,"H: "+h);
        Log.d(Constants.APPTAG,"I: "+i);
        Log.d(Constants.APPTAG,"P: "+p);
        Log.d(Constants.APPTAG,"d: "+d);

        JSONObject map = new JSONObject();
        try{
            map.put("pname",f+" "+l);
            map.put("gender",g);
            map.put("dob",d);
            map.put("address",a+";"+c+";"+s);
            map.put("policyno",i);
            map.put("phoneno",p);
        }catch(JSONException E){}
        final String requestBody = map.toString();

        StringRequest str = new StringRequest(Request.Method.POST,Constants.url+Constants.register_patient,
                                    new Response.Listener<String>(){
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(Constants.APPTAG,"Response received: "+response);
                                            SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                                            prefs.edit().putInt("patientID",Integer.parseInt(response)).putBoolean("form_filled",true).commit();
                                            Intent i =new Intent(FormFillActivity2.this,MainActivity.class);
                                            startActivity(i);
                                            finish();

                                        }
                                    },
                                    new Response.ErrorListener(){
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(Constants.APPTAG,"Error!! "+error);
                                            SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                                            prefs.edit().putInt("patientID",1).putBoolean("form_filled",true).commit();
                                            Intent i =new Intent(FormFillActivity2.this,MainActivity.class);
                                            startActivity(i);
                                            finish();
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

    private void updateLabel(Calendar c){
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(c.getTime()));
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
