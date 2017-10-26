package com.example.parkfinder;


import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.w3c.dom.Text;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFragment extends Fragment {
    ListView list;
    public boolean isBackGroundChanged = false;
    ImageView imageView;
    String[] PlaceName = {
            "H&M",
            "BurgerKing",
            "MCdonalds",
            "IMAX"
    } ;
    String[] description = {
            "Here you can buy clothes",
            "Here you can buy food",
            "Here you can buy humburgers",
            "Here you can see IMAX films"
    } ;
    Integer[] imageId = {
            R.drawable.hmlogo,
            R.drawable.burgking,
            R.drawable.mcdonalds,
            R.drawable.imax
    };
    String[] SaleDescription = {
            "При заказе 3 пар штанов - Скидка на шорты 30%. Просто назовите код с экрана!",
            "При покупке 2 гамбургеров ItalianMacho - соус бесплатно. Просто назовите код с экрана!",
            "При покупке большой порции картошки фри - кофе со скидкой 20%. Просто назовите код с экрана!",
            "При заказе билета онлайн и вводе данного кода - скидка 10% на любой фильм в зале IMAX 3D. Просто введите код с экрана в форму на сайте!"
    } ;
    String[] SecretCode = {
            "4j5nb",
            "67b3h",
            "8fghu",
            "5hb34"
    } ;
    public NewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_new, container, false);
        CustomList adapter = new CustomList(getActivity(), PlaceName, description, imageId);
        list=(ListView)rootview.findViewById(R.id.ListView);

        //imageView=(ImageView)rootview.findViewById(R.id.star_favourite);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getActivity(), "You Clicked at " +SaleDescription[position], Toast.LENGTH_SHORT).show();
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
                if (isBackGroundChanged == false) {
                    Toast.makeText(getActivity(), PlaceName[position] + " in favourite", Toast.LENGTH_SHORT).show();
                    view.findViewById(R.id.star_favourite).setBackgroundResource(R.drawable.favourite_green);
                    isBackGroundChanged = true;
                }
                else if (isBackGroundChanged == true){
                    Toast.makeText(getActivity(), PlaceName[position] + " removed from favourite", Toast.LENGTH_SHORT).show();
                    view.findViewById(R.id.star_favourite).setBackgroundResource(0);
                    isBackGroundChanged = false;
                }
                    return true;
            }
        });
        return rootview;
    }
}
