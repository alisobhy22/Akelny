package com.example.akelny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
    public String uniqueId;
    public String numberOfPeople;
    public String reservationDate;
    public String reservationTime;
    public String specialRequests;
    public String order;
    public String status;
    public String userNumber;
    public String restaurantName;
    public String feedback;

    public Reservation(String uniqueId, String numberOfPeople, String reservationDate, String reservationTime, String specialRequests,
                       String order, String userNumber, String status,String restaurantName,String feedback)
    {
        this.uniqueId=uniqueId;
        this.numberOfPeople= numberOfPeople;
        this.reservationDate= reservationDate;
        this.reservationTime= reservationTime;
        this.specialRequests= specialRequests;
        this.order= order;
        this.userNumber= userNumber;
        this.status= status;
        this.restaurantName = restaurantName;
        this.feedback = feedback;
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

    String userName;
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
                        intent.putExtra("user name", userName);
                        intent.putExtra("user number", userNumber);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    public void alertDialogCantReserve() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        String message;
        message= "Please enter the number of people, reservation date and time";
        dialog.setMessage(message);
        dialog.setTitle("Cannot proceed with reservation");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });

        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    String restName;
    String workingHours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_reservation);

        userName = getIntent().getExtras().getString("user name");
        userNumber= getIntent().getExtras().getString("user number");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        numberOfPeopleEntry = (EditText) findViewById(R.id.editTextNumber);
        reservationDateEntry = (EditText) findViewById(R.id.editTextDate);
        reservationTimeEntry = (EditText) findViewById(R.id.editTextTime);
        specialRequestsEntry = (EditText) findViewById(R.id.editTextTextMultiLine);
        orderEntry = (EditText) findViewById(R.id.orderEntry);
        restaurantName= (TextView) findViewById(R.id.textView);
        restName= getIntent().getExtras().getString("Restaurant name");
        workingHours=getIntent().getExtras().getString("Working hours");
        if (restName==null)
            restaurantName.setText("Restaurant name");
        else
            restaurantName.setText(restName);

        String startHrs; // restaurant
        String startMins;
        String endHrs;
        String endMins;
        startHrs=workingHours.substring(0,2);
        startMins=workingHours.substring(3,5);
        endHrs=workingHours.substring(6,8);
        endMins=workingHours.substring(9,11);

        int starthours_res=Integer.parseInt(startHrs);
        int startmins_res=Integer.parseInt(startMins);
        int endhours_res=Integer.parseInt(endHrs);
        int endmins_res=Integer.parseInt(endMins);




        reserveBtn = (Button) findViewById(R.id.reserveBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        ImageButton homeBtn;
        ImageButton profileBtn;

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel= new NotificationChannel("My notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numberOfPeople = String.valueOf(numberOfPeopleEntry.getText());
                reservationDate = String.valueOf(reservationDateEntry.getText());
                reservationTime= String.valueOf(reservationTimeEntry.getText());
                specialRequests= String.valueOf(specialRequestsEntry.getText());
                order= String.valueOf(orderEntry.getText());
                String user_startHrs; // user input
                String user_startMins;

                user_startHrs=reservationTime.substring(0,2);
                user_startMins=reservationTime.substring(3,5);

                int userHrs=Integer.parseInt(user_startHrs);
                int usrMins=Integer.parseInt(user_startMins);
                if (numberOfPeople.equals("") || reservationDate.equals("")|| reservationTime.equals(""))
                {
                    alertDialogCantReserve();
                }else if (userHrs<starthours_res||(userHrs==starthours_res && usrMins<startmins_res)||userHrs>endhours_res-1){

                    AlertDialog.Builder dialog=new AlertDialog.Builder(TableReservation.this);
                    String message;
                    message= "The time entered is not within the working hours of the restaurant. Please choose another time.";
                    dialog.setMessage(message);
                    dialog.setTitle("Restaurant is closed");
                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    AlertDialog alertDialog=dialog.create();
                    alertDialog.show();

                }else {
                    Reservation reservation= new Reservation(" ",numberOfPeople, reservationDate, reservationTime, specialRequests,
                            order, userNumber, "pending", restaurantName.getText().toString(),"");
                    myRef.child("Reservations").push().setValue(reservation);
                    alertDialog();
                }


                /*NotificationCompat.Builder builder= new NotificationCompat.Builder(TableReservation.this, "Reserved");
                builder.setContentTitle("Reservation is confirmed");
                builder.setSmallIcon(R.drawable.request);
                builder.setAutoCancel(true);
                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(TableReservation.this);
                managerCompat.notify(1, builder.build());*/

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TableReservation.this, Homepage.class);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNumber);
                startActivity(intent);
            }
        });

        homeBtn = (ImageButton) findViewById(R.id.imageButton);
        profileBtn = (ImageButton) findViewById(R.id.imageButton2);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TableReservation.this, Homepage.class);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNumber);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TableReservation.this, MainActivity.class);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNumber);
                startActivity(intent);
            }
        });

    }
}

