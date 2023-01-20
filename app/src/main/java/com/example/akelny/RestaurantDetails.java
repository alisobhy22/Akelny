package com.example.akelny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RestaurantDetails extends AppCompatActivity {

    Button reserveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        reserveBtn= findViewById(R.id.reserveBtn);
        ImageButton homeBtn;
        ImageButton profileBtn;
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RestaurantDetails.this, TableReservation.class);
                startActivity(intent);
            }
        });

        homeBtn = (ImageButton) findViewById(R.id.imageButton);
        profileBtn = (ImageButton) findViewById(R.id.imageButton2);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RestaurantDetails.this, Homepage.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RestaurantDetails.this, MainActivity.class);
                //intent.putExtra("user name", userName);
                //intent.putExtra("user number", userNum);
                startActivity(intent);
            }
        });
    }
}