package com.example.akelny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Homepage extends AppCompatActivity {

    Button signOutButton;
    ListView listView;

    ImageButton homeBtn;
    ImageButton profileBtn;

    String userName;
    String userNum;


    public class Restaurant {
        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        private String Name;
        private String Cuisine;
        private float rating;

        public String getCuisine() {
            return Cuisine;
        }

        public void setCuisine(String cuisine) {
            Cuisine = cuisine;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }



        public Restaurant(String Name,String Cuisine, float rating)
        {
            this.Name = Name;
            this.Cuisine = Cuisine;
            this.rating = rating;
        }
    }

    private ArrayList<Restaurant> Restaurants = new ArrayList<Restaurant>();


    public Homepage()
    {

    }
    public void alertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        String message;
        dialog.setTitle("This is the homepage");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        listView = (ListView) findViewById(R.id.listview);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        AppCompatActivity thisActivity = this;
        DatabaseReference rest = database.getReference("Restaurants");
        rest.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        String Name = child.getKey();
                        String cuisine = child.child("Cuisine").getValue().toString();
                        Float rating = child.child("Rating").getValue(Float.class);
                        Restaurant restaurant = new Restaurant(Name, cuisine, rating.floatValue());
                        Restaurants.add(restaurant);
                        System.out.println("Found restaurant: " + restaurant.getName());
                    }

                    HomescreenAdapter adapter = new HomescreenAdapter(thisActivity,Restaurants);
                    listView.setAdapter((ListAdapter) adapter);
                } else {
                    Log.e("Restaurant", "Error fetching data: " + task.getException().getMessage());

                }
            }
        });

        // FIXME: We need to send the user name and number from the last layer (Activity)
//        userName = getIntent().getExtras().getString("user name");
//        userNum = getIntent().getExtras().getString("user number");

        signOutButton = (Button) findViewById(R.id.signoutbutton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, MainScreen.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Homepage.this, RestaurantDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("Restaurant Name", Restaurants.get(i).Name);
                bundle.putString("Restaurant Cuisine", Restaurants.get(i).Cuisine);
                bundle.putFloat("Restaurant Rating", Restaurants.get(i).rating);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        homeBtn = (ImageButton) findViewById(R.id.imageButton);
        profileBtn = (ImageButton) findViewById(R.id.imageButton2);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNum);
                startActivity(intent);

            }
        });
    }
}