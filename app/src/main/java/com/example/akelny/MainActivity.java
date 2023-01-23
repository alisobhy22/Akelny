package com.example.akelny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String userName;
    String userNumberFromIntent;

    TextView textV;
    ListView listView;
    ImageButton homeBtn;
    ImageButton profileBtn;

    public void alertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        String message;
        dialog.setTitle("This is the profile page");
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
        setContentView(R.layout.activity_main);

        final ArrayList<Reservation> reservationsList = getReservationsForAUser();

        userName = getIntent().getExtras().getString("user name");
        userNumberFromIntent = getIntent().getExtras().getString("user number");
        System.out.println(userName + userNumberFromIntent);

        textV = (TextView) findViewById(R.id.textView);
        textV.setText("Name: " + userName);

        AppCompatActivity thisActivity = this;
        System.out.println("\nLINE 74\n");

        listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, RestaurantDetails.class);
            }
        });

        homeBtn = (ImageButton) findViewById(R.id.imageButton);
        profileBtn = (ImageButton) findViewById(R.id.imageButton2);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Homepage.class);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNumberFromIntent);
                startActivity(intent);
            }
        });
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });
    }

    public ArrayList<Reservation> getReservationsForAUser() {
        ArrayList<Reservation> Reservations = new ArrayList<Reservation>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservation = database.getReference("Reservations");
        reservation.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        String uniqueId = child.getKey();

                        if(child.child("numberOfPeople").getValue()==null) {
                            System.err.println("NUMBER OF PEOPLE IS NULL");
                            continue;
                        }

                        String numberOfPeople = child.child("numberOfPeople").getValue().toString();
                        String reservationDate = child.child("reservationDate").getValue().toString();
                        String reservationTime = child.child("reservationTime").getValue().toString();
                        String specialRequests = child.child("specialRequests").getValue().toString();
                        String order = child.child("order").getValue().toString();
                        String status = child.child("status").getValue().toString();
                        String userNumber = child.child("userNumber").getValue().toString();
                        String restaurantName = child.child("restaurantName").getValue().toString();
                        String feedback = child.child("feedback").getValue().toString();

                        Reservation reservation = new Reservation(uniqueId, numberOfPeople, reservationDate, reservationTime, specialRequests, order, userNumber, status, restaurantName,feedback);

                        if ((child.child("userNumber").getValue().toString()).equals(userNumberFromIntent)) {
                            Reservations.add(reservation);
                        }
                    }
                } else {
                    Log.e("Reservation", "Error fetching data: " + task.getException().getMessage());
                }
                for (int i = 0; i < Reservations.size(); i++) {
                    Log.d(
                            "Reservation\n",
                            "Reservation ID: " + Reservations.get(i).uniqueId +
                                    ", Name: " + Reservations.get(i).restaurantName +
                                    ", Date: " + Reservations.get(i).reservationDate +
                                    ", Time: " + Reservations.get(i).reservationTime +
                                    ", Number of People: " + Reservations.get(i).numberOfPeople +
                                    ", Special Requests: " +Reservations.get(i).specialRequests +
                                    ", Order: " + Reservations.get(i).order +
                                    ", Status: " + Reservations.get(i).status +
                                    ", User Number: " + Reservations.get(i).userNumber);
                }
                Adapter baseAdapter = new Adapter(MainActivity.this, MainActivity.this, Reservations);
                listView.setAdapter((ListAdapter) baseAdapter);
            }
        });
        return Reservations;
    }

}