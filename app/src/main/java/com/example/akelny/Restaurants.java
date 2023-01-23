package com.example.akelny;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Restaurants {


    public class Restaurant {
        private String Name;
        private int Account_ID;
        private String Account_Password;
        private String Address;
        private float avg_spending;
        public String Cuisine;
        private String image;
        private String menu;
        private String hotline;
        private float rating;
        private String working_hours;

        public Restaurant(String Name,int Account_ID,String Account_Password, String Address,float avg_spending,String Cuisine, String image, String menu, String hotline, float rating, String working_hours)
        {
            this.Name = Name;
            this.Account_ID = Account_ID;
            this.Account_Password = Account_Password;
            this.Address = Address;
            this.avg_spending = avg_spending;
            this.Cuisine = Cuisine;
            this.image = image;
            this.menu = menu;
            this.hotline = hotline;
            this.rating = rating;
            this.working_hours = working_hours;
        }
    }

    private ArrayList<Restaurant> Restaurants;


    public Restaurants()
    {
        Restaurants = new ArrayList<Restaurant>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rest = database.getReference("Restaurants");
        rest.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        String Name = child.getKey();
                        int accountID = child.child("Account ID").getValue(Integer.class);
                        String accountPassword = child.child("Account Password").getValue().toString();
                        String address = child.child("Address").getValue().toString();
                        float averageSpending = child.child("Average spending per person").getValue(Float.class);
                        String cuisine = child.child("Cuisine").getValue().toString();
                        String hotline = child.child("Hotline").getValue().toString();
                        String image = child.child("Image").getValue().toString();
                        String menuURL = child.child("Menu URL").getValue().toString();
                        float rating = child.child("Rating").getValue(Float.class);
                        String workingHours = child.child("Working hours").getValue().toString();
                        Restaurant restaurant = new Restaurant(Name,accountID, accountPassword, address, averageSpending, cuisine, image, menuURL,hotline, rating, workingHours);
                        Restaurants.add(restaurant);
                    }
                } else {
                    Log.e("Restaurant", "Error fetching data: " + task.getException().getMessage());

                }
                for (int i = 0; i < Restaurants.size(); i++) {
                    Log.d(
                            "Restaurant","Name: " + Restaurants.get(i).Name +
                                    ", Account ID: " + Restaurants.get(i).Account_ID +
                            ", Account Password: " + Restaurants.get(i).Account_Password +
                            ", Address: " + Restaurants.get(i).Address +
                            ", Average Spending: " +Restaurants.get(i).avg_spending +
                            ", Cuisine: " + Restaurants.get(i).Cuisine +
                            ", Image: " + Restaurants.get(i).image +
                            ", Menu: " + Restaurants.get(i).menu +
                            ", Hotline: " + Restaurants.get(i).hotline +
                            ", Rating: " + Restaurants.get(i).rating +
                            ", Working Hours: " + Restaurants.get(i).working_hours);
                }
            }
        });
    }


}

