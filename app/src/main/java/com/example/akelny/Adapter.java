package com.example.akelny;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

//public class Adapter extends android.widget.BaseAdapter {
public class Adapter extends BaseAdapter {

    AppCompatActivity parentPage;
    private Context context;
    private ArrayList<Reservation> reservationsList;
    //LayoutInflater inflater;

    Button giveFeedback, cancel;

    //The appointment date has to be in this format "dd/MM/yyyy"
    int checkIfDatePassed(String appointmentDateString) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));

        Date appointmentDate = formatter.parse(appointmentDateString);

        Date currentDate = new Date();
        String currentDateString = formatter.format(currentDate);
        currentDate = formatter.parse(currentDateString);

        System.out.println("Appointment Date: "+appointmentDate+"\n");
        System.out.println("Current Date: "+currentDate+"\n");

        return (currentDate.compareTo(appointmentDate));
    }

    //The appointment time has to be in this format "HH:mm" or "HH:mm:ss"
    //24-hour format
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

    boolean ifCanGiveFeedback(String appointmentDateString, String appointmentTimeString) throws ParseException {
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

    public void updateDatabase(Reservation oneReservation, String feedback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservation = database.getReference("Reservations");
        reservation.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
            if (task.isSuccessful()) {
                for (DataSnapshot child : task.getResult().getChildren()) {
                    if (((child.child("userNumber").getValue().toString()).equals(oneReservation.userNumber)) &&
                            ((child.child("restaurantName").getValue().toString()).equals(oneReservation.restaurantName)) &&
                            ((child.child("reservationDate").getValue().toString()).equals(oneReservation.reservationDate)) &&
                            ((child.child("reservationTime").getValue().toString()).equals(oneReservation.reservationTime))) {
                        String uniqueID = oneReservation.uniqueId;
                        System.out.println("THIS IS THE UNIQUE ID: " + uniqueID);
                        reservation.child(uniqueID).child("feedback").setValue(feedback);
                        //child.child("feedback").setValue(feedback);
                    }
                }
            } else {
                Log.e("Reservation", "Error updating database: " + task.getException().getMessage());
            }
            }
        });
    }

    public void changeReservationStatus(Reservation oneReservation) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservation = database.getReference("Reservations");
        reservation.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        if (((child.child("userNumber").getValue().toString()).equals(oneReservation.userNumber)) &&
                                ((child.child("restaurantName").getValue().toString()).equals(oneReservation.restaurantName)) &&
                                ((child.child("reservationDate").getValue().toString()).equals(oneReservation.reservationDate)) &&
                                ((child.child("reservationTime").getValue().toString()).equals(oneReservation.reservationTime))) {
                            String uniqueID = oneReservation.uniqueId;
                            System.out.println("THIS IS THE UNIQUE ID: " + uniqueID);
                            reservation.child(uniqueID).child("status").setValue("Cancelled");
                        }
                    }
                } else {
                    Log.e("Reservation", "Error updating database: " + task.getException().getMessage());
                }
            }
        });
    }

    //public Adapter(AppCompatActivity parentPage, ArrayList<Reservation> reservationsList) {
    public Adapter(Context context, ArrayList<Reservation> reservationsList) {
        this.context=context;
        //this.parentPage=parentPage;
        this.reservationsList=reservationsList;
        //inflater=LayoutInflater.from(parentPage.getApplicationContext());
    }
    @Override
    public int getCount() {
        return reservationsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        System.out.println("\nI AM INSIDE ADAPTER\n");

        view = inflater.inflate(R.layout.activity_reservations, null);

        giveFeedback =  view.findViewById(R.id.givefeedback);
        cancel = view.findViewById(R.id.cancel);

        TextView restaurantName = (TextView) view.findViewById(R.id.name);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView people = (TextView) view.findViewById(R.id.numOfPeople);
        TextView order = (TextView) view.findViewById(R.id.order);
        TextView specialRequests = (TextView) view.findViewById(R.id.specialRequests);
        TextView status = (TextView) view.findViewById(R.id.status);

        restaurantName.setText("Restaurant Name: " + reservationsList.get(i).restaurantName);
        date.setText("Reservation Date: " + reservationsList.get(i).reservationDate);
        time.setText("Reservation Time: " + reservationsList.get(i).reservationTime);
        people.setText("Number of people: " + reservationsList.get(i).numberOfPeople);
        order.setText("Order: " + reservationsList.get(i).order);
        specialRequests.setText("Special Requests: " + reservationsList.get(i).specialRequests);
        status.setText("Status: " + reservationsList.get(i).status);

        boolean feedbackResult = false;
        try {
            feedbackResult = ifCanGiveFeedback(reservationsList.get(i).reservationDate, reservationsList.get(i).reservationTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean finalFeedbackResult = feedbackResult;
        giveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalFeedbackResult)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parentPage.getApplicationContext());
                    builder.setTitle("Feedback Form");
                    builder.setMessage("Please give us your feedback for the appointment.");

                    final EditText input = new EditText(parentPage.getApplicationContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String feedback = input.getText().toString();
                            reservationsList.get(i).feedback=feedback;
                            updateDatabase(reservationsList.get(i), feedback);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(parentPage.getApplicationContext());
                    String message;
                    dialog.setTitle("Sorry, you cannot give feedback now. Please wait for the appointment date and time to pass.");
                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });

                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalFeedbackResult==false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parentPage.getApplicationContext());
                    builder.setTitle("Cancellation Form");
                    builder.setMessage("Your order has been cancelled.");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            changeReservationStatus(reservationsList.get(i));
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(parentPage.getApplicationContext());
                    dialog.setTitle("Sorry, you cannot cancel this reservation because the reservation date and time have already passed.");
                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });

                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
            }
        });

        return view;
    }
}


