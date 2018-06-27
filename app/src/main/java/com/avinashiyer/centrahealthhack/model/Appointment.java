package com.avinashiyer.centrahealthhack.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by avinashiyer on 4/7/17.
 */

public class Appointment {
    private int patientID;
    private int doctorID;
    private int slotID;
    private String startTime;
    private String endTime;
    public Appointment(){}
    public Appointment(int patientID,int doctorID,int slotID,String startTime, String endTime){
        this.doctorID = doctorID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.patientID = patientID;
        this.slotID = slotID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setTime(String time) {

    }



}
