package com.avinashiyer.centrahealthhack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avinashiyer.centrahealthhack.R;

/**
 * Created by avinashiyer on 4/10/17.
 */

public class FormFillAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    String[] formNames = {"First name","Last name","Gender","D.O.B","Address","City","State"
                            ,"Hispanic/Latino","Insurance Number","Phone Number"};
    public FormFillAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return 11;
        else if(position==4)
            return 14;
        else if(position==1)
            return 2;
        else if(position==2)
            return 3;
        else if(position==3)
            return 43;
        else if(position==6)
            return 46;
        else if(position==7)
            return 47;
        else
            return 5;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getCount() {
        return formNames.length;
    }

    @Override
    public Object getItem(int i) {
        return formNames[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        int type = getItemViewType(i);
        if(convertView==null) {
            switch (type) {
                case 11:{
                   convertView= inflater.inflate(R.layout.form_type_1,parent,false);
                    TextView tv1 = (TextView)convertView.findViewById(R.id.formFirstName);
                    TextView tv2 = (TextView)convertView.findViewById(R.id.formLastName);
                    tv1.setHint("First name");
                    tv2.setHint("Last name");

                }break;
                case 14:{
                    convertView= inflater.inflate(R.layout.form_type_1,parent,false);
                    TextView tv1 = (TextView)convertView.findViewById(R.id.formFirstName);
                    TextView tv2 = (TextView)convertView.findViewById(R.id.formLastName);
                    tv1.setHint("City");
                    tv2.setHint("State");

                }break;

                case 2:{
                    convertView=  inflater.inflate(R.layout.form_type_2,parent,false);
                }break;

                case 3: {
                    convertView= inflater.inflate(R.layout.form_type_3,parent,false);
                }break;

                case 4:{
                    convertView=  inflater.inflate(R.layout.form_type_4,parent,false);
                }break;

                case 5:{
                    convertView= inflater.inflate(R.layout.form_type_5,parent,false);
                }break;
            }
        }
        return convertView;
    }
}
