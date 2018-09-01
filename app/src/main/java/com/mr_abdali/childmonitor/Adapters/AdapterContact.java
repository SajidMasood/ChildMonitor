package com.mr_abdali.childmonitor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mr_abdali.childmonitor.R;
import com.mr_abdali.childmonitor.Fragments.pNumber;

import java.util.ArrayList;

public class AdapterContact extends BaseAdapter {

    ArrayList<pNumber> list;
    Context con;
    LayoutInflater inflter;

    public AdapterContact(Context con , ArrayList<pNumber> list){
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
        View view = inflter.inflate(R.layout.single_contactview, null);
        TextView name = view.findViewById(R.id.name);
        name.setText(list.get(position).getName());
        TextView number = view.findViewById(R.id.number);
        number.setText(list.get(position).getNumber());
        return view;
    }
}
