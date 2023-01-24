package com.example.akelny;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomescreenAdapter extends android.widget.BaseAdapter {

    AppCompatActivity parentPage;
    LayoutInflater inflater;


    private ArrayList<Homepage.Restaurant> Restaurants;
    private String userName;
    private String userNum;

    public ArrayList<String> cuisines;
    public HomescreenAdapter(AppCompatActivity parentPage, ArrayList<Homepage.Restaurant> res, String userName, String userNum, ArrayList<String> cuisines) {

        this.parentPage=parentPage;
        this.Restaurants=res;
        this.userName= userName;
        this.userNum= userNum;
        this.cuisines=cuisines;
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
        String imgUrl;

        TextView restaurantName = (TextView) view.findViewById(R.id.name);
        TextView cuisine = (TextView) view.findViewById(R.id.cuisine);
        TextView rating = (TextView) view.findViewById(R.id.rating);
        ImageView restaurantImg= (ImageView) view.findViewById(R.id.restaurantImg);

        restaurantName.setText(Restaurants.get(i).getName());
        cuisine.setText(Restaurants.get(i).getCuisine());
        rating.setText("⭐ "+ Float.toString(Restaurants.get(i).getRating()));
        imgUrl= Restaurants.get(i).getImgUrl();

        Picasso.get().load(imgUrl).resize(150, 150).into(restaurantImg);


        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(parentPage.getApplicationContext(), RestaurantDetails.class);
                Bundle bundle = new Bundle();
                intent.putExtra("Restaurant Name", Restaurants.get(i).getName());
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNum);
                intent.putExtra("cuisines", cuisines);
                parentPage.startActivity(intent);
            }
        });
        return view;
    }

}
