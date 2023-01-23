package com.example.akelny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
//public class sortByCuisine extends Comparator<Restaurants.Restaurant> {
//
//    ArrayList<Restaurants.Restaurant> RestaurantsList;
//    ArrayList<String> cuisines;
//
//    void swap(Restaurants.Restaurant r1, Restaurants.Restaurant r2) {
//        Restaurants.Restaurant swap = r1;
//        r1 = r2;
//        r2 = swap;
//    }
//Line 209


//    ArrayList<Restaurants.Restaurant> sort (ArrayList<Restaurants.Restaurant> list, String cuisine)
//    {
//        int indexForLastMatch=0;
//        for (int i=0; i<list.size()-1; i++)
//        {
//            for (int j=0; j<list.size()-1; j++)
//            {
//                if(list.get(j).Cuisine.equals(cuisine))
//                {
//                    swap(list.get(j), list.get(indexForLastMatch));
//                    indexForLastMatch=j;
//                }
//            }
//        }
//        return list;
//    }
//}


public class Homepage extends AppCompatActivity {

    Button signOutButton;
    ListView listView;

    ImageButton homeBtn;
    ImageButton profileBtn;

    String userName;
    String userNum;
    ArrayList<String> userCuisines;

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
        private String imgUrl;

        public String getCuisine() {
            return Cuisine;
        }

        public void setCuisine(String cuisine) {
            Cuisine = cuisine;
        }

        public float getRating() {
            return rating;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public Restaurant(String Name,String Cuisine, float rating, String imgUrl)
        {
            this.Name = Name;
            this.Cuisine = Cuisine;
            this.rating = rating;
            this.imgUrl=imgUrl;
        }
    }

    private ArrayList<Restaurant> Restaurants = new ArrayList<Restaurant>();

    boolean checkIfPreference (String restaurantCuisine, ArrayList<String> preferences)
    {
        for (int i=0; i<preferences.size(); i++)
        {
            if (restaurantCuisine.equals(preferences.get(i)))
                return true;
        }
        return false;
    }

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
        userName = getIntent().getExtras().getString("user name");
        userNum = getIntent().getExtras().getString("user number");
        userCuisines = getIntent().getExtras().getStringArrayList("cuisines");
        AppCompatActivity thisActivity = this;

        listView = (ListView) findViewById(R.id.listview);
        SearchView searchView = findViewById(R.id.SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty())
                {
                    HomescreenAdapter adapter = new HomescreenAdapter(thisActivity, Restaurants, userName, userNum);
                    listView.setAdapter((ListAdapter) adapter);
                    return true;
                }
                ArrayList<Restaurant> finalList = new ArrayList<>();
                query = query.toLowerCase(Locale.ROOT);

                for (Restaurant res : Restaurants)
                {
                    String cuisine = res.getCuisine();
                    cuisine = cuisine.toLowerCase(Locale.ROOT);
                    if (cuisine.contains(query))
                    {
                        finalList.add(res);
                    }
                }
                HomescreenAdapter adapter = new HomescreenAdapter(thisActivity, finalList, userName, userNum);
                listView.setAdapter((ListAdapter) adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty())
                {
                    HomescreenAdapter adapter = new HomescreenAdapter(thisActivity, Restaurants, userName, userNum);
                    listView.setAdapter((ListAdapter) adapter);
                    return true;
                }
                newText = newText.toLowerCase(Locale.ROOT);
                ArrayList<Restaurant> finalList = new ArrayList<>();
                for (Restaurant res : Restaurants)
                {
                    String cuisine = res.getCuisine();
                    cuisine = cuisine.toLowerCase(Locale.ROOT);
                    if (cuisine.contains(newText))
                    {
                        finalList.add(res);
                    }
                }
                HomescreenAdapter adapter = new HomescreenAdapter(thisActivity, finalList, userName, userNum);
                listView.setAdapter((ListAdapter) adapter);
                return true;
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference rest = database.getReference("Restaurants");
        rest.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        String Name = child.getKey();
                        String cuisine = child.child("Cuisine").getValue().toString();
                        Float rating = child.child("Rating").getValue(Float.class);
                        String imgUrl = child.child("Image").getValue().toString();
                        Restaurant restaurant = new Restaurant(Name, cuisine, rating.floatValue(), imgUrl);

                        boolean preferred = checkIfPreference(cuisine, userCuisines);
                        if(preferred==true)
                            Restaurants.add(0, restaurant);
                        else
                            Restaurants.add(restaurant);

                        System.out.println("Found restaurant: " + restaurant.getName());
                    }

                    HomescreenAdapter adapter = new HomescreenAdapter(thisActivity, Restaurants, userName, userNum);
                    listView.setAdapter((ListAdapter) adapter);
                } else {
                    Log.e("Restaurant", "Error fetching data: " + task.getException().getMessage());

                }
            }
        });

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
                intent.putExtra("Restaurant Name", Restaurants.get(i).Name);
                intent.putExtra("Restaurant Cuisine", Restaurants.get(i).Cuisine);
                intent.putExtra("Restaurant Rating", Restaurants.get(i).rating);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNum);
                startActivity(intent);
            }
        });

        /*homeBtn = (ImageButton) findViewById(R.id.imageButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alertDialog();
            }
        });*/

        profileBtn = (ImageButton) findViewById(R.id.imageButton2);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, MainActivity.class);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNum);
                startActivity(intent);

            }
        });
    }
}