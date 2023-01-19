package com.example.akelny;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.util.Log;
import com.google.android.gms.tasks.Task;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String namesList[] = {"Amy", "Menna"};
    String datesList[] = {"Jan", "Feb"};
    String timesList[] = {"10 AM", "9 AM"};
    String numberOfPeople[] = {"3", "2"};

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


        listView = (ListView) findViewById(R.id.listview);
        Adapter baseAdapter = new Adapter(getApplicationContext(), namesList, datesList, timesList, numberOfPeople);
        listView.setAdapter(baseAdapter);


        homeBtn = (ImageButton) findViewById(R.id.imageButton);
        profileBtn = (ImageButton) findViewById(R.id.imageButton2);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Homepage.class);
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

    public void getReservationsForAUser() {
        ArrayList<Reservation> Reservations = new ArrayList<Reservation>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservation = database.getReference("Reservations");
        reservation.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        String numberOfPeople = child.child("numberOfPeople").getValue().toString();
                        String reservationDate = child.child("reservationDate").getValue().toString();
                        String reservationTime = child.child("reservationTime").getValue().toString();
                        String specialRequests = child.child("specialRequests").getValue().toString();
                        String order = child.child("order").getValue().toString();
                        String status = child.child("status").getValue().toString();
                        String userNumber = child.child("userNumber").getValue().toString();
                        String restaurantName = child.child("restaurantName").getValue().toString();

                        Reservation reservation = new Reservation(numberOfPeople, reservationDate, reservationTime, specialRequests, order, status, userNumber, restaurantName);

                        if ((child.child("User Number").getValue().toString()) == "") {
                            Reservations.add(reservation);
                        }
                    }
                } else {
                    Log.e("Reservation", "Error fetching data: " + task.getException().getMessage());
                }
            }
        });
    }
}