package com.example.akelny;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    String namesList[] = {"Amy", "Menna"};
    String datesList[] = {"Jan", "Feb"};
    String timesList[] = {"10 AM", "9 AM"};
    String numberOfPeople[] = {"3", "2"};

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView= (ListView) findViewById(R.id.listview);
        Adapter baseAdapter = new Adapter(getApplicationContext(), namesList, datesList, timesList, numberOfPeople);
        listView.setAdapter(baseAdapter);
    }
}