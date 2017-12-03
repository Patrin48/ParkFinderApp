package com.example.parkfinder;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    EditText name;
    EditText Email;
    EditText password;
    EditText password2;
    TextView link_signin;

    String _name;
    String _email;
    String _pass;
    String _pass2;
    AppCompatButton regButton;
    public RegisterFragment() {
        // Required empty public constructor
    }

    class PostRegisterTask extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        HttpURLConnection urlConnection;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Register new user...");
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
            try {
                int code = urlConnection.getResponseCode();
                if (code == 200) {
                    JSONObject jObject = new JSONObject(result);
                    String log = jObject.getString("name");
                    Toast.makeText(getActivity(), "You registered as " + log, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(),"Invalid data! Check out all the fileds!", Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (progressDialog != null){
                progressDialog.dismiss();
            }
        }

        private String PostData(String urlpath) throws IOException, JSONException{
            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            try {
                JSONObject DataToSend = new JSONObject();
                DataToSend.put("name", _name);
                DataToSend.put("email", _email);
                DataToSend.put("password", _pass);
                DataToSend.put("password2", _pass2);

                URL url = new URL(urlpath);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(DataToSend.toString());
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
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            }
            return result.toString();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_register, container, false);
        name = (EditText)rootview.findViewById(R.id.input_name);
        Email = (EditText)rootview.findViewById(R.id.input_email);
        password = (EditText)rootview.findViewById(R.id.input_password);
        password2 = (EditText)rootview.findViewById(R.id.input_password2);
        link_signin = (TextView)rootview.findViewById(R.id.link_login);
        regButton = (AppCompatButton)rootview.findViewById(R.id.btn_signup);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _name = name.getText().toString();
                _email = Email.getText().toString();
                _pass = password.getText().toString();
                _pass2 = password2.getText().toString();
                new PostRegisterTask().execute("https://parkfinderapp.herokuapp.com/register");
            }
        });

        link_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment nextFrag = new LoginFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return rootview;
    }

}
