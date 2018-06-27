package com.avinashiyer.centrahealthhack.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avinashiyer.centrahealthhack.R;
import com.avinashiyer.centrahealthhack.activities.ScheduleActivity;
import com.avinashiyer.centrahealthhack.model.Doctor;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by avinashiyer on 4/1/17.
 */

public class DoctorRecyclerViewAdapter extends RecyclerView.Adapter<DoctorRecyclerViewAdapter.DoctorViewHolder> {

    static List<Doctor> doctors;
    Context context;
    int[] images = {R.drawable.face5,
            R.drawable.face6,
            R.drawable.face3,
            R.drawable.face10,
            R.drawable.face1,
            R.drawable.face2,
            R.drawable.face7,
            R.drawable.face8,
            R.drawable.face9,
            R.drawable.face4};
    public DoctorRecyclerViewAdapter(List<Doctor> doctors,Context context){
        this.doctors = doctors;
        this.context = context;
    }
    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_doctor_layout, parent, false);
        DoctorViewHolder d = new DoctorViewHolder(v);
        return d;
    }

    @Override
    public void onBindViewHolder(DoctorViewHolder holder, int position) {
        holder.drName.setText(doctors.get(position).getName());
        holder.drSpeciality.setText(doctors.get(position).getSpeciality());
        holder.drImage.setImageResource(images[position]);
        holder.book.setTag(position);

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView drName,drSpeciality;
        Button book;
        CircleImageView drImage;
        //List<Doctor> doctors;
        public DoctorViewHolder(View v){
            super(v);
            drName = (TextView)v.findViewById(R.id.doctorName);
            drSpeciality = (TextView)v.findViewById(R.id.doctorSpeciality);
            drImage = (CircleImageView)v.findViewById(R.id.doctorImage);
            book = (Button)v.findViewById(R.id.doctorBook);
            book.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int tag = Integer.parseInt(view.getTag().toString()) + 1;
            int id = doctors.get(tag-1).getId();
            Log.d("RECYCLERVIEWADAPTER","Value of clicked doc id: "+id);
            Intent i= new Intent(view.getContext(),ScheduleActivity.class);
            i.putExtra("doc_id",id);
            i.putExtra("doc_name",doctors.get(tag-1).getName());
            i.putExtra("speciality",doctors.get(tag-1).getSpeciality());
            view.getContext().startActivity(i);
        }
    }
}
