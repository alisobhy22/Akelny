package com.example.akelny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class CustomAA extends ArrayAdapter<Reservation> {

    ArrayList<Reservation> list;

    // invoke the suitable constructor of the ArrayAdapter class
    public CustomAA(@NonNull Context context, ArrayList<Reservation> reservationsList) {

        //list = reservationsList;

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, reservationsList);
    }

    @NonNull
    @Override
    public View getView(int i, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View view = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_reservations, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Reservation singleR = getItem(i);

        TextView restaurantName = (TextView) view.findViewById(R.id.name);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView people = (TextView) view.findViewById(R.id.numOfPeople);
        TextView order = (TextView) view.findViewById(R.id.order);
        TextView specialRequests = (TextView) view.findViewById(R.id.specialRequests);
        TextView status = (TextView) view.findViewById(R.id.status);

        restaurantName.setText("Restaurant Name: " + singleR.restaurantName);
        date.setText("Reservation Date: " + singleR.reservationDate);
        time.setText("Reservation Time: " + singleR.reservationTime);
        people.setText("Number of people: " + singleR.numberOfPeople);
        order.setText("Order: " + singleR.order);
        specialRequests.setText("Special Requests: " + singleR.specialRequests);
        status.setText("Status: " + singleR.status);

        return view;
    }
}
