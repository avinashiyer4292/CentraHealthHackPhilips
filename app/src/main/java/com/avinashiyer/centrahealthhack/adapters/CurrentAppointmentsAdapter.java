package com.avinashiyer.centrahealthhack.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.model.Appointment;
import com.avinashiyer.centrahealthhack.model.ScheduledAppointment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by avinashiyer on 4/7/17.
 */

public class CurrentAppointmentsAdapter extends RecyclerView.Adapter<CurrentAppointmentsAdapter.CurrentAppointmentHolder> {

    List<ScheduledAppointment> appointments;
    Context context;

    public CurrentAppointmentsAdapter(List<ScheduledAppointment> appointments, Context context){
        this.appointments = appointments;
        this.context = context;
    }

    @Override
    public CurrentAppointmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_current_appointment_row,parent,false);
        CurrentAppointmentHolder c = new CurrentAppointmentHolder(v);
        return c;
    }

    @Override
    public void onBindViewHolder(CurrentAppointmentHolder holder, int position) {
        holder.doctorName.setText("Dr. "+appointments.get(position).getDoctorName());
        holder.date.setText(appointments.get(position).getDate());
        holder.appointmentTime.setText(appointments.get(position).getStartTime()+" - "+
                                        appointments.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class CurrentAppointmentHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView doctorName, appointmentTime, date;
        public CurrentAppointmentHolder(View v){
            super(v);
            img = (CircleImageView)v.findViewById(R.id.currentAppDocImage);
            doctorName = (TextView)v.findViewById(R.id.currentAppDocName);
            appointmentTime = (TextView)v.findViewById(R.id.currentAppTime);
            date = (TextView)v.findViewById(R.id.currentAppDate);
        }


    }
}
