package com.avinashiyer.centrahealthhack.utils;

/**
 * Created by avinashiyer on 4/1/17.
 */

public class Constants {
    public static final String url = "http://ec2-54-172-76-118.compute-1.amazonaws.com:80/";
    public static final String enter_doc_room = "enterDoctorRoom";
    public static final String exit_doc_room="exitDoctorRoom";
    public static final String get_all_doctors="getDoctorList";
    public static final String get_sym_doctors="getDoctorList?patientID=";
    public static final String get_appointments="getCurrentAppointments";
    public static final String register_patient="register";
    public static final String schedule_appointment = "scheduleAppointment";
    public static final String get_available_slots = "getAvailability?docID=";
    public static final String get_current_appointments = "getCurrentAppointments?patientID=";
    public static final String get_alexa_status = "getAlexaStatus?patientID=";
    public static final String get_delay = "getDelay";
    public static final String reset_data_changed = "resetDataChanged";
    public static final String SHARED_PREF_NAME = "Avinash";
    public static final String BROADCAST_FILTER_ACTION = "com.avinashiyer.centrahealthhack";
    public static final String APPTAG="Centra Health";
    public static final String BEACON_UUID="0x02676f6f676c6507";

    //Fitbit params
    public static final String CLIENT_ID = "2288H9";
    public static final String CLIENT_SECRET = "071190c538ac770d02df5852268bb1e3";
    public static final String AUTH_URI = "https://www.fitbit.com/oauth2/authorize";
    public static final String REDIRECT_URI="fitbitmcapp://auth/fitbit/callback";
    public static final String ACCESS_TOKEN_URI="https://api.fitbit.com/oauth2/token";
    public static final String SCOPES="activity%20heartrate%20nutrition%20sleep";
    public static final String EXPIRES_IN="604800";

    //fitbit URLs for accessing data
    public static final String PROFILE_URL="https://api.fitbit.com/1/user/-/profile.json";


    //fitbit data
    public static final String steps = "6081";
    public static final String heart_rate = "67";
    public static final String calories = "704";
    public static final String kmsWalked = "10.4";
    public static final String floors = "14";
    public static final String activeMinutes = "156";


}
