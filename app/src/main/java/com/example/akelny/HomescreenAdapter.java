package com.example.akelny;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomescreenAdapter extends android.widget.BaseAdapter {

    AppCompatActivity parentPage;
    LayoutInflater inflater;

    private ArrayList<Homepage.Restaurant> Restaurants;
    private String userName;
    private String userNum;
    public HomescreenAdapter(AppCompatActivity parentPage, ArrayList<Homepage.Restaurant> res, String userName, String userNum) {

        this.parentPage=parentPage;
        this.Restaurants=res;
        this.userName= userName;
        this.userNum= userNum;
        inflater=LayoutInflater.from(parentPage.getApplicationContext());
    }
    @Override
    public int getCount() {
        return Restaurants.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.homepage_single_restaurant, null);
        TextView restaurantName = (TextView) view.findViewById(R.id.name);
        TextView cuisine = (TextView) view.findViewById(R.id.cuisine);
        TextView rating = (TextView) view.findViewById(R.id.rating);

        restaurantName.setText(Restaurants.get(i).getName());
        cuisine.setText(Restaurants.get(i).getCuisine());
        rating.setText(Float.toString(Restaurants.get(i).getRating()));

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(parentPage.getApplicationContext(), RestaurantDetails.class);
                Bundle bundle = new Bundle();
                //bundle.putString("Restaurant Name", Restaurants.get(i).getName());
                //intent.putExtras(bundle);
                intent.putExtra("Restaurant Name", Restaurants.get(i).getName());
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNum);
                System.out.println("yarab teshta3'al "+userName);
                parentPage.startActivity(intent);
            }
        });
        return view;
    }

}
