package com.example.akelny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter extends android.widget.BaseAdapter {

    Context context;
    String namesList[];
    String datesList[];
    String timesList[];
    String numberOfPeople[];
    LayoutInflater inflater;

    public Adapter(Context context, String[] namesList, String[] datesList, String[] timesList, String[] numberOfPeople) {

        this.context=context;
        this.namesList=namesList;
        this.datesList=datesList;
        this.timesList=timesList;
        this.numberOfPeople=numberOfPeople;
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
        view = inflater.inflate(R.layout.activity_reservations, null);
        TextView restaurantName = (TextView) view.findViewById(R.id.name);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView people = (TextView) view.findViewById(R.id.numOfPeople);

        restaurantName.setText(namesList[i]);
        date.setText(datesList[i]);
        time.setText(timesList[i]);
        people.setText(numberOfPeople[i]);
        return view;
    }
}
