package com.avinashiyer.centrahealthhack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avinashiyer.centrahealthhack.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by avinashiyer on 4/10/17.
 */

public class FitbitDataAdapter extends BaseAdapter {

    Context context;
    List<String> list;
    String[] desc;
    FontAwesomeIcons[] icons = {FontAwesomeIcons.fa_flash,FontAwesomeIcons.fa_road,FontAwesomeIcons.fa_building_o
                                ,FontAwesomeIcons.fa_flash,FontAwesomeIcons.fa_fire,FontAwesomeIcons.fa_heart_o};
    public FitbitDataAdapter(Context context, List<String> list,String[] desc){

        this.context = context;
        this.list = list;
        this.desc = desc;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.single_fitbit_grid_layout, parent, false);
        }

        ImageView img = (ImageView)convertView.findViewById(R.id.fibitDataImage);
        TextView data = (TextView)convertView.findViewById(R.id.fitbitData);
        TextView descText = (TextView)convertView.findViewById(R.id.fitbitDataDesc);

        img.setImageDrawable(new IconDrawable(context,icons[i]).colorRes(R.color.colorPrimary));
        data.setText(list.get(i));
        descText.setText(desc[i]);


        return convertView;
    }
}
