package com.avinashiyer.centrahealthhack.model;

/**
 * Created by avinashiyer on 4/1/17.
 */

public class Patient {
    int _id;
    String first_name;
    String last_name;
    String policy_number;
    String group_number;
    String dob;
    String gender;
    String address;
    String phone_number;

    public Patient(String first_name,String last_name,String policy_number
                    ,String group_number,String dob,String gender
                    ,String address,String phone_number){

        this.first_name = first_name;
        this.last_name = last_name;
        this.policy_number = policy_number;
        this.group_number = group_number;
        this.dob = dob;
        this.gender = gender;
        this.phone_number = phone_number;
        this.address = address;

    }

    public Patient(String policy_number, String first_name,String last_name,String address, String dob){
        this.policy_number = policy_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.dob = dob;
    }
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPolicy_number() {
        return policy_number;
    }

    public void setPolicy_number(String policy_number) {
        this.policy_number = policy_number;
    }

    public String getGroup_number() {
        return group_number;
    }

    public void setGroup_number(String group_number) {
        this.group_number = group_number;
    }

    public String getdob() {
        return dob;
    }

    public void setdob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
