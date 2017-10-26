package com.example.parkfinder;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */

public class MainFragment extends Fragment {

    private SupportMapFragment mSupportMapFragment;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FABToolbarLayout fabToolbar;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_main, container, false);
        fabToolbar = ((FABToolbarLayout) v.findViewById(R.id.fabtoolbar));
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {

                        googleMap.getUiSettings().setAllGesturesEnabled(true);
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(52.601818, 39.5047206))
                                .title("Your car's here!"))
                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(52.601818, 39.5047206)).zoom(14.0f).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);

                    }

                }
            });


        }
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabtoolbar_fab);
        ImageView im1 = (ImageView) v.findViewById(R.id.one);
        ImageView im2 = (ImageView) v.findViewById(R.id.two);
        ImageView im3 = (ImageView) v.findViewById(R.id.three);





        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabToolbar.hide();
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "You're near LSTU Universuty", Snackbar.LENGTH_SHORT)
                        .show();
                fabToolbar.hide();
            }

        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "You've saved car's location", Snackbar.LENGTH_SHORT)
                        .show();
                fabToolbar.hide();
            }

        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        fabToolbar.show();
            }
        });
        return v;
    }
}



