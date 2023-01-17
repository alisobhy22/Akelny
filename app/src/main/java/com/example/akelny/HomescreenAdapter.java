package com.example.akelny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomescreenAdapter extends android.widget.BaseAdapter {

    Context context;
    String namesList[];
    String cuisinesList[];
    String ratingsList[];
    LayoutInflater inflater;

    public HomescreenAdapter(Context context, String[] namesList, String[] cuisinesList, String[] ratingsList) {

        this.context=context;
        this.namesList=namesList;
        this.cuisinesList=cuisinesList;
        this.ratingsList=ratingsList;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return namesList.length;
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

        restaurantName.setText(namesList[i]);
        cuisine.setText(cuisinesList[i]);
        rating.setText(ratingsList[i]);
        return view;
    }

}
