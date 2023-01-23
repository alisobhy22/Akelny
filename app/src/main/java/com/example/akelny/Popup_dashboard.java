package com.example.akelny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class Popup_dashboard extends AppCompatActivity {

    TextView usernumber;
    TextView name;
    TextView date;
    TextView num;
    TextView Time;
    TextView req;
    TextView order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_dashboard);

        Point size = new Point();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(size);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Reservation r = (Reservation) getIntent().getSerializableExtra("Reservation");

        usernumber = (TextView) findViewById(R.id.user_number);
        name = (TextView) findViewById(R.id.restaurant_name);
        num = (TextView) findViewById(R.id.number_of_people);
        date = (TextView) findViewById(R.id.reservation_date);
        Time = (TextView) findViewById(R.id.reservation_time);
        req = (TextView) findViewById(R.id.special_requests);
        order = (TextView) findViewById(R.id.order);

        usernumber.setText(r.userNumber);
        name.setText("Restaurant Name: " + r.restaurantName);
        num.setText("Number of people: " + r.numberOfPeople);
        date.setText("Reservation Date: " + r.reservationDate);
        Time.setText("Reservation Time: " + r.reservationTime);
        req.setText("Special Requests: " + r.specialRequests);
        order.setText("Order: " + r.order);

    }
}