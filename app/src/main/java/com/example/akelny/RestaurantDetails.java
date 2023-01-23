package com.example.akelny;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;



public class RestaurantDetails extends AppCompatActivity {

    ImageButton homeBtn;
    ImageButton profileBtn;
    Button reserveBtn;

    TextView addressText;
    TextView cuisineText;
    TextView ratingText;
    TextView menuText;
    TextView WHText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        String resturantName = getIntent().getExtras().getString("Restaurant Name");
        String userName = getIntent().getExtras().getString("user name");
        String userNumber = getIntent().getExtras().getString("user number");
        final String[] workingHours = new String[1];

        addressText= (TextView) findViewById(R.id.textViewA);
        cuisineText= (TextView) findViewById(R.id.textViewB);
        ratingText= (TextView) findViewById(R.id.textViewC);
        menuText= (TextView) findViewById(R.id.textViewD);
        WHText= (TextView) findViewById(R.id.textViewE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        homeBtn     = (ImageButton) findViewById(R.id.imageButton);
        profileBtn  = (ImageButton) findViewById(R.id.imageButton2);
        reserveBtn= (Button) findViewById(R.id.reserveBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RestaurantDetails.this, Homepage.class);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNumber);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RestaurantDetails.this, MainActivity.class);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNumber);
                startActivity(intent);
            }
        });


        DatabaseReference rest = database.getReference("Restaurants");
        rest.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        String Name = child.getKey();
                        if (Name.equals(resturantName)) {
                            Float rating = child.child("Rating").getValue(Float.class);
                            String address = child.child("Address").getValue().toString();
                            String menuURL = child.child("Menu URL").getValue().toString();
                            workingHours[0] = child.child("Working hours").getValue().toString();
                            String image = child.child("Image").getValue().toString();
                            String cuisine = child.child("Cuisine").getValue().toString();


                            TextView addressTV = findViewById(R.id.textView15);
                            addressTV.setText(address);
                            TextView RestTV= findViewById(R.id.restNameEntry);
                            RestTV.setText(Name);
                            ImageView imageView=findViewById(R.id.imageView);
                            // FIXME: Find a way to display the image into the imageView
                            TextView textView2=findViewById(R.id.textView16);
                            textView2.setText(cuisine);
                            TextView textView3=findViewById(R.id.textView17);
                            textView3.setText(rating.toString());

                            Button menuButton = (Button) findViewById(R.id.menuButton);
                            menuButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(menuURL));
                                    startActivity(i);
                                }
                            });

                            TextView textView5=findViewById(R.id.textView18);
                            textView5.setText(workingHours[0]);

                            break;
                        }
                    }


                } else {
                    Log.e("Restaurant", "Error fetching data: " + task.getException().getMessage());

                }
            }
        });
       reserveBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent= new Intent(RestaurantDetails.this, TableReservation.class);
               intent.putExtra("user name", userName);
               intent.putExtra("user number", userNumber);
               intent.putExtra("Restaurant name", resturantName);
               intent.putExtra("Working hours", workingHours[0]);
               System.out.println(workingHours[0]);
               startActivity(intent);
           }
       });
    }
    }
