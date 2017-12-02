package com.example.parkfinder;

/**
 * Created by Алексей on 19.10.2017.
 */

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] PlaceName;
    private final String[] description;
    private final String[] imageId;
    public CustomList(Activity context,
                      String[] PlaceName, String[] description, String[] imageId) {
        super(context, R.layout.row, PlaceName);
        this.context = context;
        this.PlaceName = PlaceName;
        this.description = description;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.first_text_view);
        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.second_text_view);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageLogo);
        txtTitle.setText(PlaceName[position]);
        txtTitle1.setText(description[position]);

        Picasso.with(context).load(imageId[position]).into(imageView);
        //imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
