package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MonthViewAdapter extends BaseAdapter {

    private Context mContext;
    private int mResource;
    private ArrayList<My_date> mdates;


    public MonthViewAdapter(Context context, int Resource, ArrayList<My_date> dates) {
        mContext = context;
        mResource = Resource;
        mdates = dates;
    }

    @Override
    public int getCount() {
        return mdates.size();
    }

    @Override
    public Object getItem(int position) {
        return mdates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent,false);
        }

        TextView tv_date = convertView.findViewById(R.id.textview_date);
        if(mdates.get(position).date > 0)
            tv_date.setText(String.valueOf(mdates.get(position).date));
        else
            tv_date.setText(" ");

        return convertView;
    }
}
