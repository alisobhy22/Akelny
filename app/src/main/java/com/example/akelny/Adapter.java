package com.example.akelny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends android.widget.BaseAdapter {

    Context context;
    ArrayList<Reservation> reservationsList;
    LayoutInflater inflater;

    public Adapter(Context context, ArrayList<Reservation> reservationsList) {
        this.context=context;
        this.reservationsList=reservationsList;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return reservationsList.size();
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

        System.out.println("I LOVE MENNA");

        TextView restaurantName = (TextView) view.findViewById(R.id.name);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView people = (TextView) view.findViewById(R.id.numOfPeople);
        TextView order = (TextView) view.findViewById(R.id.order);
        TextView specialRequests = (TextView) view.findViewById(R.id.specialRequests);
        TextView status = (TextView) view.findViewById(R.id.status);

        restaurantName.setText("Restaurant Name: " + reservationsList.get(i).restaurantName);
        date.setText("Reservation Date: " + reservationsList.get(i).reservationDate);
        time.setText("Reservation Time: " + reservationsList.get(i).reservationTime);
        people.setText("Number of people: " + reservationsList.get(i).numberOfPeople);
        order.setText("Order: " + reservationsList.get(i).order);
        specialRequests.setText("Special Requests: " + reservationsList.get(i).specialRequests);
        status.setText("Status: " + reservationsList.get(i).status);

        return view;
    }
}
