package com.example.akelny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class Reservation{
    public String numberOfPeople;
    public String reservationDate;
    public String reservationTime;
    public String specialRequests;
    public String order;
    public String status;
    public String userNumber;
    public String restaurantName;
    public String feedback= "";

    public Reservation(String numberOfPeople, String reservationDate, String reservationTime, String specialRequests,
                       String order, String userNumber, String status,String restaurantName)
    {
        this.numberOfPeople= numberOfPeople;
        this.reservationDate= reservationDate;
        this.reservationTime= reservationTime;
        this.specialRequests= specialRequests;
        this.order= order;
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
    EditText orderEntry;
    TextView restaurantName;

    String numberOfPeople, reservationDate, reservationTime, specialRequests, order, userNumber;

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
    protected void sendSMS(String phoneNumber, String message) {
        /*Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String ("5551212"));
        smsIntent.putExtra("sms_body"  , "Test ");

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(TableReservation.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);*/
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
        orderEntry = (EditText) findViewById(R.id.orderEntry);
        restaurantName= (TextView) findViewById(R.id.textView);

        reserveBtn = (Button) findViewById(R.id.reserveBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        ImageButton homeBtn;
        ImageButton profileBtn;
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfPeople = String.valueOf(numberOfPeopleEntry.getText());
                reservationDate = String.valueOf(reservationDateEntry.getText());
                reservationTime= String.valueOf(reservationTimeEntry.getText());
                specialRequests= String.valueOf(specialRequestsEntry.getText());
                order= String.valueOf(orderEntry.getText());

                userNumber= getIntent().getExtras().getString("user number");
                Reservation reservation= new Reservation(numberOfPeople, reservationDate, reservationTime, specialRequests,
                        order, userNumber, "pending", restaurantName.getText().toString());
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

        homeBtn = (ImageButton) findViewById(R.id.imageButton);
        profileBtn = (ImageButton) findViewById(R.id.imageButton2);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TableReservation.this, Homepage.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TableReservation.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}

