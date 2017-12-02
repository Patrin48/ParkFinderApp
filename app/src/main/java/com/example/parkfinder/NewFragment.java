package com.example.parkfinder;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.EventLogTags;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewFragment extends Fragment {
    private CustomList Myadapter;
    int count = 4;
    class GetDataTask extends AsyncTask<String, Void, String>{
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
            String out="";
            try {
                JSONArray jsonarray = new JSONArray(result);
                count = jsonarray.length();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    PlaceName[i] = jsonobject.getString("PlaceName");
                    description[i] = jsonobject.getString("Description");
                    SaleDescription[i] = jsonobject.getString("SaleDescription");
                    SecretCode[i] = jsonobject.getString("SecretCode");
                    imageId[i] = jsonobject.getString("image");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updatePlace(PlaceName, description, SaleDescription, SecretCode, imageId, count);
            Myadapter.notifyDataSetChanged();
            if (progressDialog != null){
                progressDialog.dismiss();
            }
        }
    }

    ListView list;

    public void updatePlace(String[] PlaceName, String[] Description, String[] SaleDescription, String[] SecretCode, String[] imageId, int count) {
        this.PlaceName = PlaceName;
        this.description = Description;
        this.SaleDescription = SaleDescription;
        this.SecretCode = SecretCode;
        this.imageId = imageId;
        this.count = count;
    }
    ImageView imageView;

    String[] PlaceName = new String[count];
    String[] description = new String[count];
    String[] imageId = new String[count];
    String[] SaleDescription = new String[count];
    String[] SecretCode = new String[count];

   public boolean[] FavouriteBool = {
            false,
            false,
            false,
            false
    } ;
    public NewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new GetDataTask().execute("https://parkfinderapp.herokuapp.com/PlacesList");
        final View rootview = inflater.inflate(R.layout.fragment_new, container, false);
        Myadapter = new CustomList(getActivity(), PlaceName, description, imageId);
        list=(ListView)rootview.findViewById(R.id.ListView);
        list.setAdapter(Myadapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity().getApplication(), TestActivity.class);
                intent.putExtra("logotype", imageId[position]);
                intent.putExtra("saledescript", SaleDescription[position]);
                intent.putExtra("mycode", SecretCode[position]);
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                if (FavouriteBool[position] == false) {
                    Toast.makeText(getActivity(), PlaceName[position] + " in favourite", Toast.LENGTH_SHORT).show();
                    view.findViewById(R.id.star_favourite).setBackgroundResource(R.drawable.favourite_green);
                    FavouriteBool[position] = true;
                }
                else if (FavouriteBool[position] == true){
                    Toast.makeText(getActivity(), PlaceName[position] + " removed from favourite", Toast.LENGTH_SHORT).show();
                    view.findViewById(R.id.star_favourite).setBackgroundResource(0);
                    FavouriteBool[position] = false;
                }
                    return true;
            }
        });
        return rootview;
    }
}
