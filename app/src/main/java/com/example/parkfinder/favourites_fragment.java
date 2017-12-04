package com.example.parkfinder;

/**
 * Created by Алексей on 04.12.2017.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class favourites_fragment extends Fragment{
    private CustomList Myadapter;
    int count = 10;

    class GetFavourite extends AsyncTask<String, Void, String> {
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

    class DeleteFromFavourites extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        HttpURLConnection urlConnection;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Deleting from favourite...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                return PostData(params[0]);
            }
            catch(IOException ex){
                return "Network Error!";
            }
            catch(JSONException ex){
                return "Wrong Data!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog != null){
                progressDialog.dismiss();
            }
        }

        private String PostData(String urlpath) throws IOException, JSONException{
            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            try {
                //JSONObject DataToSend = new JSONObject();
                //DataToSend.put("_name", PName);

                URL url = new URL(urlpath);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                //bufferedWriter.write(DataToSend.toString());
                bufferedWriter.flush();

                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            }
            finally {
                if (bufferedReader != null){
                    bufferedReader.close();
                }
            }
            return result.toString();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        new GetFavourite().execute("https://parkfinderapp.herokuapp.com/GetFavourites/Alex");
        final View rootview = inflater.inflate(R.layout.fragment_favourites, container, false);
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
                String Name = null;
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(Global.login_info);
                    Name = jObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new DeleteFromFavourites().execute("https://parkfinderapp.herokuapp.com/deleteFromFavourite/" + PlaceName[position] + "/" + Name);
                Toast.makeText(getActivity(), PlaceName[position] + " removed from favourite", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

                return true;
            }
        });
        return rootview;
    }
}
