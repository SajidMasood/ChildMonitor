package com.mr_abdali.childmonitor.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mr_abdali.childmonitor.Adapters.AdapterCallLogs;
import com.mr_abdali.childmonitor.R;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class FragCallLogs extends Fragment {

    // TODO: 8/9/2018 Variables Declaration Section...
    private View v;
    String PassChildId;
    ListView contactList;
    Context context;
    ArrayList<pCallLogs> list = new ArrayList<pCallLogs>();

    public FragCallLogs() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_frag_call_logs, container, false);
        contactList = (ListView) v.findViewById(R.id.contactList);
        context = getActivity();

        PassChildId = getActivity().getIntent().getStringExtra("childId");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CallLog");
            reference.orderByChild("ID").equalTo(PassChildId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(PassChildId))
                    {
                    String abc = dataSnapshot.getValue().toString();
                    android.util.Log.e("sdsd", " " + abc);
                    String[] abcd = abc.split("callLog=");
                    String[] abcd2 = abcd[1].split(Pattern.quote("1016199}"));
                    abc = abcd2[0];
                    String[] myContact = abc.split("1016199");
                    Gson gson = new Gson();

                    for (int i = 0; i < myContact.length; i++) {
                        try {
                            pCallLogs object = gson.fromJson(myContact[i], pCallLogs.class);
                            list.add(object);
                        } catch (Exception e) {
                            //
                        }
                    }

                    AdapterCallLogs adapterContact = new AdapterCallLogs(context, list);
                    contactList.setAdapter(adapterContact);
                }
                else{
                       // Toast.makeText(getContext(),"NO child found",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //
                }
            });
            return v;

    }



}
