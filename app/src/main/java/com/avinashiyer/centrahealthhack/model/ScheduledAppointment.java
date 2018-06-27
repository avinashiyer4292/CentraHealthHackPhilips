package com.avinashiyer.centrahealthhack.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by avinashiyer on 4/11/17.
 */

public class ScheduledAppointment {
    private String doctorName;
    private String startTime;
    private String endTime;
    private String date;

    public ScheduledAppointment(){}
    public ScheduledAppointment(String doctorName, String startTime, String endTime,String date) {
        this.doctorName = doctorName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public String formatDate(String time){

        int length = time.length()-5;
        time = time.substring(0,length);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            time = sdf.parse(time).toString();
        }catch (Exception e){}

        return time;
    }

    public String formatTime(String time){

        return time.substring(time.indexOf(':')-2,time.length());

    }



}
