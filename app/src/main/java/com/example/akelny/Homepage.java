package com.example.akelny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Homepage extends AppCompatActivity {

    Button signOutButton;
    ListView listView;

    String namesList[] = {"Afifi", "Ali"};
    String cuisinesList[] = {"Italian", "Egyptian"};
    String ratingsList[] = {"4", "5"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        listView= (ListView) findViewById(R.id.listview);
        HomescreenAdapter adapter = new HomescreenAdapter(getApplicationContext(), namesList, cuisinesList, ratingsList);
        listView.setAdapter((ListAdapter) adapter);

        signOutButton = (Button) findViewById(R.id.signoutbutton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Homepage.this, MainScreen.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Homepage.this, RestaurantDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("Restaurant Name", namesList[i]);
                bundle.putString("Restaurant Cuisine", cuisinesList[i]);
                bundle.putString("Restaurant Rating", ratingsList[i]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}