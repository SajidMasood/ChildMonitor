package com.mr_abdali.childmonitor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mr_abdali.childmonitor.Fragments.pMessages;
import com.mr_abdali.childmonitor.R;

import java.util.ArrayList;
import java.util.Date;

public class AdapterMessage extends BaseAdapter{

    ArrayList<pMessages> list;
    Context con;
    LayoutInflater inflter;

    public AdapterMessage(Context con , ArrayList<pMessages> list){
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
        View view = inflter.inflate(R.layout.single_messageview, null);
        TextView number = view.findViewById(R.id.number);
        number.setText(list.get(position).getAddress());

        TextView date = view.findViewById(R.id.date);
        date.setText(list.get(position).getMsg());

        TextView dur = view.findViewById(R.id.dur);
        Date callDayTime = new Date(Long.valueOf(list.get(position).getTime()));
        dur.setText(callDayTime.toString());

        TextView type = view.findViewById(R.id.type);
        type.setText(list.get(position).getFolderName());

        return view;
    }
}
