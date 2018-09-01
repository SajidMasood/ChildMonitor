package com.mr_abdali.childmonitor.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import com.mr_abdali.childmonitor.R;
import com.mr_abdali.childmonitor.Adapters.AdapterContact;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class FragContactList extends Fragment {

    // TODO: 8/9/2018 Variables Declaration Section...
    private View v;
    String PassChildId;
    ListView contactList;
    Context context;
    ArrayList<pNumber> list = new ArrayList<pNumber>();

    public FragContactList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_frag_contact_list, container, false);
        contactList = (ListView) v.findViewById(R.id.contactList);
        context = getActivity();

        PassChildId = getActivity().getIntent().getStringExtra("childId");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ContactList");
        reference.orderByChild("ID").equalTo(PassChildId).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(PassChildId)) {
                    String abc = dataSnapshot.getValue().toString();
                    String[] abcd = abc.split("contacts=");
                    String[] abcd2 = abcd[1].split(Pattern.quote("1016199}"));
                    abc = abcd2[0];

                    String[] myContact = abc.split("1016199");
                    Gson gson = new Gson();

                    for (int i = 0; i < myContact.length; i++) {
                        try {
                            pNumber object = gson.fromJson(myContact[i], pNumber.class);
                            list.add(object);
                        } catch (Exception e) {
                            //
                        }
                    }

                    AdapterContact adapterContact = new AdapterContact(context, list);
                    contactList.setAdapter(adapterContact);
                }else {

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
                //
            }
        });
        return v;
    }
}
