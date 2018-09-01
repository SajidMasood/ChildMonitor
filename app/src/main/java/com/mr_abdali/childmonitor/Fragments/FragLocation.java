package com.mr_abdali.childmonitor.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mr_abdali.childmonitor.R;

import java.util.regex.Pattern;


public class FragLocation extends Fragment implements OnMapReadyCallback {

    View mView;
    GoogleMap googleMap;
    String PassChildId;
    MapView mMapView;

    public FragLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_frag_location, container, false);
        //
        PassChildId = getActivity().getIntent().getStringExtra("childId");
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Location");
        reference.orderByChild("ID").equalTo(PassChildId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(PassChildId)) {
                    String abc = dataSnapshot.getValue().toString();
                    Log.e("sdsd", " " + abc);
                    String[] abcd = abc.split("latlong=");
                    String[] abcd2 = abcd[1].split(Pattern.quote("}"));
                    abc = abcd2[0];


                    String latlong = abc;
                    String[] la = latlong.split(",");
                    LatLng childLocation = new LatLng(Double.valueOf(la[0]), Double.valueOf(la[1]));
                    googleMap.addMarker(new MarkerOptions().position(childLocation).title("Child Location"));

                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(childLocation));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(childLocation, 16.0f));

                    // TODO: 8/9/2018 Current Login User Location...
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);
                    LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    // TODO: 8/9/2018 If GPS is Null
                    if (location == null){
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                        String provider = locationManager.getBestProvider(criteria, true);
                        location = locationManager.getLastKnownLocation(provider);
                    }

                    // TODO: 8/9/2018 IF GPS is not Null...
                    if (location != null){
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation,16.0f),1500,null);
                    }


                }else {
                    //
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
