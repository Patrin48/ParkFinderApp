package com.example.parkfinder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFragment extends Fragment {


    public NewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_new, container, false);
        ImageView ImageBrand = (ImageView) rootview.findViewById(R.id.primary_action);
        TextView MainText = (TextView) rootview.findViewById(R.id.first_text_view);
        TextView SecondaryTest = (TextView)  rootview.findViewById(R.id.second_text_view);
        ImageView FavouriteStar = (ImageView) rootview.findViewById(R.id.secondary_action);

        return rootview;
    }

}
