package com.example.parkfinder;

/**
 * Created by Алексей on 04.12.2017.
 */
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class account_fragment extends Fragment{
    TextView name;
    TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        name = (TextView) rootView.findViewById(R.id.user_profile_name);
        email = (TextView) rootView.findViewById(R.id.user_profile_email);
        if (Global.login_info == null){
            Toast.makeText(getActivity(), "Login First!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
        else {
            try {
                JSONObject jObject = new JSONObject(Global.login_info);
                String myname = jObject.getString("name");
                String myemail = jObject.getString("email");
                name.setText(myname);
                email.setText(myemail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }
}
