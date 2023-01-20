package com.example.akelny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class Reservations extends AppCompatActivity {

    //The appointment date has to be in this format "dd/MM/yyyy"
    int checkIfDatePassed(String appointmentDateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String currentDateString = formatter.format(currentDate);

        try {
            currentDate = formatter.parse(currentDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date appointmentDate = null;
        try {
            appointmentDate = formatter.parse(appointmentDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (currentDate.compareTo(appointmentDate));
    }
//    int checkIfTimePassed(int appointmentHour, int appointmentMin, int appointmentSec) {
//        LocalTime currentTime, appointmentTime;
//        currentTime = java.time.LocalTime.now();
//        appointmentTime = LocalTime.of(appointmentHour, appointmentMin, appointmentSec);
//        return currentTime.compareTo(appointmentTime);
//    }

    Boolean ifCanGiveFeedback(String appointmentDateString, int appointmentHour, int appointmentMin, int appointmentSec) {
        int dateResult = checkIfDatePassed(appointmentDateString);
        if (dateResult>0) //currentDate occurs after appointmentDate
            return true; //user can give feedback
        else if(dateResult<0) //currentDate occurs before appointmentDate
            return false; //user can cancel the appointment
//        else //Both dates are equal, so comapare time
//        {
//            int timeResult = checkIfTimePassed(appointmentHour, appointmentMin, appointmentSec);
//            if (timeResult>0) //currentTime occurs after appointmentTime
//                return true; //user can give feedback
//            else if(timeResult<0) //currentTime occurs before appointmentTime
//                return false; //user can cancel the appointment
//            else //Both times are equal
//        }
        return false;
    }

    Button giveFeedback, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        giveFeedback = findViewById(R.id.givefeedback);
        cancel = findViewById(R.id.cancel);

        //boolean feedbackResult = ifCanGiveFeedback();

        // giveFeedback.setOnClickListener(new View.OnClickListener() {
        //   @Override
//            public void onClick(View v) {
//                if(feedbackResult)
//                {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Feedback Form");
//                    builder.setMessage("Please give us your feedback for the appointment.");
//                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    });
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                }
//                else
        //  }
        //});
    }
}




