package com.avinashiyer.centrahealthhack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.avinashiyer.centrahealthhack.model.Patient;

/**
 * Created by avinashiyer on 4/1/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "CentraDB";

    // Contacts table name
    private static final String TABLE_PATIENT = "patient";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_POLICY_NUMBER = "policy_number";
    private static final String KEY_GROUP_NUMBER = "group_number";
    private static final String KEY_DOB = "dob";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PH_NO = "phone_number";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_POLICY_NUMBER + " TEXT,"
                + KEY_GROUP_NUMBER + " TEXT,"+ KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"+ KEY_DOB + " TEXT,"
                + KEY_GENDER + " TEXT,"+ KEY_ADDRESS + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_PATIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addPatient(Patient p) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POLICY_NUMBER, p.getPolicy_number());
        values.put(KEY_GROUP_NUMBER,p.getGroup_number());
        values.put(KEY_FIRST_NAME,p.getFirst_name());
        values.put(KEY_LAST_NAME,p.getLast_name());
        values.put(KEY_DOB,p.getdob());
        values.put(KEY_GENDER,p.getGender());
        values.put(KEY_ADDRESS,p.getAddress());
        values.put(KEY_PH_NO,p.getPhone_number());

        // Inserting Row
        db.insert(TABLE_PATIENT, null, values);
        db.close(); // Closing database connection
    }

    public Patient getPatient(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PATIENT, new String[] { KEY_POLICY_NUMBER,KEY_GROUP_NUMBER,
                KEY_FIRST_NAME,KEY_LAST_NAME,KEY_DOB,KEY_GENDER,KEY_ADDRESS,KEY_PH_NO
                        }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Patient contact = new Patient(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7));
        // return contact
        return contact;
    }
}
