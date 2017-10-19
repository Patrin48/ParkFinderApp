package com.example.parkfinder;

/**
 * Created by Алексей on 19.10.2017.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] PlaceName;
    private final String[] description;
    private final Integer[] imageId;
    public CustomList(Activity context,
                      String[] PlaceName, String[] description, Integer[] imageId) {
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
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
