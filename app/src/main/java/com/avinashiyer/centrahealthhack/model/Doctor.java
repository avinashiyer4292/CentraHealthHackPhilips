package com.avinashiyer.centrahealthhack.model;

/**
 * Created by avinashiyer on 4/1/17.
 */

public class Doctor {
    int id;
    String name;
    String speciality;

    public Doctor(int id,String name, String speciality){
        this.id = id;
        this.name = name;
        this.speciality =speciality;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
