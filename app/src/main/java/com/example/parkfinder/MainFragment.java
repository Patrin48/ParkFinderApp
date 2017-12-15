package com.example.parkfinder;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */

public class MainFragment extends Fragment {
    int count = 0;
    private SupportMapFragment mSupportMapFragment;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FABToolbarLayout fabToolbar;
    ImageView im1;
    ImageView im2;
    ImageView im3;
    public MainFragment() {
        // Required empty public constructor
    }
    class Getcount extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Data");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine()) != null){
                    result.append(line).append("\n");
                }
            } catch (IOException e) {
                return "Network Error";
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONArray jsonarray = new JSONArray(result);
                count = jsonarray.length();
                Global.Count_Of_ListItems = count;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null){
                progressDialog.dismiss();
            }
        }
    }

    class GetLocations extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Locations...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine()) != null){
                    result.append(line).append("\n");
                }
            } catch (IOException e) {
                return "Network Error";
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String out="";
            try {
                JSONArray jsonarray = new JSONArray(result);
                count = jsonarray.length();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    Global.placesName[i] = jsonobject.getString("PlaceName");
                    Global.width_places[i] = jsonobject.getString("Width");
                    Global.length_places[i] = jsonobject.getString("Lenght");
                }
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
                                        .position(new LatLng(Global.Latitude, Global.Longitude))
                                        .title("Your car's here!"))
                                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin));
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Global.Latitude, Global.Longitude)).zoom(10.0f).build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                googleMap.moveCamera(cameraUpdate);
                                for (int i= 0; i<count; i++)
                                {
                                    if (i==0) {
                                        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(Global.width_places[i]), Double.parseDouble(Global.length_places[i])))
                                                .title(Global.placesName[i]+ " See our discount offer."))
                                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_hm));
                                    }
                                    if (i==1) {
                                        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(Global.width_places[i]), Double.parseDouble(Global.length_places[i])))
                                                .title(Global.placesName[i]+ " Winter collection!"))
                                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_zara));
                                    }
                                    if (i==2) {
                                        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(Global.width_places[i]), Double.parseDouble(Global.length_places[i])))
                                                .title(Global.placesName[i]+" Add to favourite and get the limited burger here!"))
                                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_burger_king));
                                    }

                                    if (i==3) {
                                        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(Global.width_places[i]), Double.parseDouble(Global.length_places[i])))
                                                .title(Global.placesName[i]+" Use our discount! Only today!"))
                                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_imax));
                                    }

                                }
                            }

                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null){
                progressDialog.dismiss();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new GetLocations().execute("https://parkfinderapp.herokuapp.com/Location");
        new Getcount().execute("https://parkfinderapp.herokuapp.com/PlacesList");
        View v =  inflater.inflate(R.layout.fragment_main, container, false);
        fabToolbar = ((FABToolbarLayout) v.findViewById(R.id.fabtoolbar));
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabtoolbar_fab);

        im1 = (ImageView) v.findViewById(R.id.one);
        im2 = (ImageView) v.findViewById(R.id.two);
        im3 = (ImageView) v.findViewById(R.id.three);

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabToolbar.hide();
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Global.Longitude==0.0 || Global.Latitude==0.0) {
                    Snackbar.make(v, "We haven't got your coordinates yet :(", Snackbar.LENGTH_SHORT)
                            .show();
                    fabToolbar.hide();
                }
            }

        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "You are here!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
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



