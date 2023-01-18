package com.example.akelny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class Reservation{
    public String numberOfPeople;
    public String reservationDate;
    public String reservationTime;
    public String specialRequests;
    public String status;
    public String userNumber;
    public String restaurantName;

    public Reservation(String numberOfPeople, String reservationDate, String reservationTime, String specialRequests, String userNumber, String status,String restaurantName)
    {
        this.numberOfPeople= numberOfPeople;
        this.reservationDate= reservationDate;
        this.reservationTime= reservationTime;
        this.specialRequests= specialRequests;
        this.userNumber= userNumber;
        this.status= status;
        this.restaurantName = restaurantName;
    }
}


public class TableReservation extends AppCompatActivity {

    EditText numberOfPeopleEntry;
    EditText reservationDateEntry;
    EditText reservationTimeEntry;
    EditText specialRequestsEntry;
    TextView restaurantName;

    String numberOfPeople, reservationDate, reservationTime, specialRequests, userNumber;

    Button reserveBtn;
    Button cancelBtn;

    public DatabaseReference myRef;
    public FirebaseDatabase database;

    public void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        String message;
        message= "Reservation request is sent to restaurant. Please wait for confirmation.";
        dialog.setMessage(message);
        dialog.setTitle("Reservation details sent");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent= new Intent(TableReservation.this, Homepage.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_reservation);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        numberOfPeopleEntry = (EditText) findViewById(R.id.editTextNumber);
        reservationDateEntry = (EditText) findViewById(R.id.editTextDate);
        reservationTimeEntry = (EditText) findViewById(R.id.editTextTime);
        specialRequestsEntry = (EditText) findViewById(R.id.editTextTextMultiLine);
        restaurantName= (TextView) findViewById(R.id.textView);

        reserveBtn = (Button) findViewById(R.id.reserveBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfPeople = String.valueOf(numberOfPeopleEntry.getText());
                reservationDate = String.valueOf(reservationDateEntry.getText());
                reservationTime= String.valueOf(reservationTimeEntry.getText());
                specialRequests= String.valueOf(specialRequestsEntry.getText());

                userNumber= getIntent().getExtras().getString("user number");
                Reservation reservation= new Reservation(numberOfPeople, reservationDate, reservationTime, specialRequests, userNumber, "pending", restaurantName.getText().toString());
                myRef.child("Reservations").push().setValue(reservation);
                alertDialog();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TableReservation.this, Homepage.class);
                startActivity(intent);
            }
        });

    }
}

