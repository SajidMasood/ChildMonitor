package com.mr_abdali.childmonitor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mr_abdali.childmonitor.Fragments.pCallLogs;
import com.mr_abdali.childmonitor.R;

import java.util.ArrayList;
import java.util.Date;

public class AdapterCallLogs extends BaseAdapter {

    ArrayList<pCallLogs> list;
    Context con;
    LayoutInflater inflter;

    public AdapterCallLogs(Context con , ArrayList<pCallLogs> list){
        this.con = con;
        this.list = list;
        inflter = (LayoutInflater.from(con));
    }

    @Override
    public int getCount()
    {
        return this.list .size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = inflter.inflate(R.layout.single_calllogsview, null);

        TextView number = view.findViewById(R.id.number);
        number.setText(list.get(position).getPhNumber());

        TextView date = view.findViewById(R.id.date);
        Date callDayTime = new Date(Long.valueOf(list.get(position).getCallDate()));
        date.setText(callDayTime.toString());

        TextView dur = view.findViewById(R.id.dur);
        dur.setText(list.get(position).getCallDuration() + " s");

        TextView type = view.findViewById(R.id.type);
        type.setText(list.get(position).getCallType());

        return view;
    }
}
