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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

class Reservation implements Serializable {
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
    public ArrayList<String> cuisines;
    String numberOfPeople, reservationDate, reservationTime, specialRequests, order, userNumber;

    Button reserveBtn;
    Button cancelBtn;

    String userName;
    public DatabaseReference myRef;
    public FirebaseDatabase database;

    int checkIfDatePassed(String appointmentDateString) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));

        Date appointmentDate = formatter.parse(appointmentDateString);

        Date currentDate = new Date();
        String currentDateString = formatter.format(currentDate);
        currentDate = formatter.parse(currentDateString);

        return (currentDate.compareTo(appointmentDate));
    }

    int checkIfTimePassed(String appointmentTimeString) {
        LocalTime appointmentTime = null;
        ZoneId id = null;
        LocalTime currentTime = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            appointmentTime = LocalTime.parse(appointmentTimeString);
            id = ZoneId.of("Africa/Cairo");
            currentTime = LocalTime.now(id);
        }

        System.out.println("Appointment Time: "+appointmentTime+"\n");
        System.out.println("Local Time: "+currentTime+"\n");

        return currentTime.compareTo(appointmentTime);
    }

    boolean ifCanReserve(String appointmentDateString, String appointmentTimeString) throws ParseException {
        int dateResult = checkIfDatePassed(appointmentDateString);
        if (dateResult>0) //currentDate occurs after appointmentDate
            return true; //User can give feedback
        else if(dateResult<0) //currentDate occurs before appointmentDate
            return false; //user can cancel the appointment
        else //Both dates are equal, so compare time
        {
            int timeResult = checkIfTimePassed(appointmentTimeString);
            if (timeResult>0) //currentTime occurs after appointmentTime
                return true; //user can give feedback
            else if(timeResult<0) //currentTime occurs before appointmentTime
                return false; //user can cancel the appointment
            else //Both times are equal
                return false;
        }
    }

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
                        intent.putExtra("cuisines", cuisines);
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
        cuisines = getIntent().getExtras().getStringArrayList("cuisines");

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

        if (endHrs.equals("00"))
        {
            endHrs="24";
        }

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
                boolean cantReserve = false;
                int userHrs=0;
                int usrMins=0;

                if (numberOfPeople.equals("") || reservationDate.equals("")|| reservationTime.equals(""))
                {
                    alertDialogCantReserve();
                }else
                {
                    user_startHrs=reservationTime.substring(0,2);
                    user_startMins=reservationTime.substring(3,5);

                    userHrs=Integer.parseInt(user_startHrs);
                    usrMins=Integer.parseInt(user_startMins);


                    cantReserve= false;
                    try {
                        cantReserve= ifCanReserve(reservationDate, reservationTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (cantReserve){
                        AlertDialog.Builder dialog=new AlertDialog.Builder(TableReservation.this);
                        String message;
                        message= "The date/time you entered have passed. Please choose a coming date/time.";
                        dialog.setMessage(message);
                        dialog.setTitle("Cannot reserve");
                        dialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                });
                        AlertDialog alertDialog=dialog.create();
                        alertDialog.show();
                    } else if (userHrs<starthours_res||(userHrs==starthours_res && usrMins<startmins_res)||userHrs>endhours_res-1){

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
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TableReservation.this, Homepage.class);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNumber);
                intent.putExtra("cuisines", cuisines);
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
                intent.putExtra("cuisines", cuisines);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TableReservation.this, MainActivity.class);
                intent.putExtra("user name", userName);
                intent.putExtra("user number", userNumber);
                intent.putExtra("cuisines", cuisines);
                startActivity(intent);
            }
        });

    }
}

