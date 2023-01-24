package com.example.akelny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    public ArrayList<Reservation> pending_reservations;
    public ArrayList<Reservation> waiting_reservations;
    public ArrayList<Reservation> accepted_reservations;
    public pending_reservationAdaptor pend_res_adaptor;
    public waiting_reservationAdaptor wait_res_adaptor;
    public accepted_reservationAdaptor acc_res_adaptor;

    ListView reservation_list;
    ListView waiting_list_list;
    ListView Accepted_reservations_list;

    Button signOutButton;
    public class pending_reservationAdaptor extends ArrayAdapter<Reservation>{
        public pending_reservationAdaptor(Context context,ArrayList<Reservation> reservations){
            super(context,0,reservations);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            Reservation reservation = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_dashboard_reservation_list,parent,false);
            }
            TextView reservation_name = (TextView) convertView.findViewById(R.id.reservation_name);
            Button accept_button = (Button) convertView.findViewById(R.id.Accept_button);
            Button decline_button = (Button) convertView.findViewById(R.id.Decline_button);
            Button waiting_list_button = (Button) convertView.findViewById(R.id.waiting_list_button);


            fetchName(reservation.userNumber,reservation_name);

            accept_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Accept Button","Clicked");
                    pending_reservations.remove(reservation);
                    reservation.status = "Ready";
                    changestatus(reservation,"Ready");
                    accepted_reservations.add(reservation);
                    acc_res_adaptor.notifyDataSetChanged();
                    notifyDataSetChanged();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Dashboard.this,"My Notification");
                    builder.setContentTitle("Reservation is confirmed");
                    builder.setSmallIcon(R.drawable.request);
                    builder.setContentText("Your reservation request has been accepted by the restaurant");
                    builder.setAutoCancel(true);
                    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(Dashboard.this);
                    managerCompat.notify(1,builder.build());
                }
            });

            decline_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Decline Button","Clicked");
                    pending_reservations.remove(reservation);
                    reservation.status = "Canceled";
                    changestatus(reservation,"Canceled");
                    notifyDataSetChanged();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Dashboard.this,"My Notification");
                    builder.setContentTitle("Reservation has been cancelled");
                    builder.setSmallIcon(R.drawable.request);
                    builder.setContentText("Your reservation request has been declined by the restaurant");
                    builder.setAutoCancel(true);
                    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(Dashboard.this);
                    managerCompat.notify(1,builder.build());
                }

            });

            waiting_list_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Waiting List Button","Clicked");
                    pending_reservations.remove(reservation);
                    reservation.status = "Waiting";
                    changestatus(reservation,"Waiting");
                    waiting_reservations.add(reservation);
                    wait_res_adaptor.notifyDataSetChanged();
                    notifyDataSetChanged();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Dashboard.this,"My Notification");
                    builder.setContentTitle("You have been added to the waiting list");
                    builder.setSmallIcon(R.drawable.request);
                    builder.setContentText("We will notify you as soon as it's your turn");
                    builder.setAutoCancel(true);
                    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(Dashboard.this);
                    managerCompat.notify(1,builder.build());
                }
            });


            return convertView;
        }
    }


    public class waiting_reservationAdaptor extends ArrayAdapter<Reservation>{
        public waiting_reservationAdaptor(Context context,ArrayList<Reservation> reservations){
            super(context,0,reservations);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            Reservation reservation = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_dashboard_waiting_list,parent,false);
            }
            TextView reservation_name = (TextView) convertView.findViewById(R.id.reservation_name);
            Button Mark_Ready_button = (Button) convertView.findViewById(R.id.Mark_Ready);
            Button Remove_button = (Button) convertView.findViewById(R.id.Remove_Button);


            fetchName(reservation.userNumber,reservation_name);

            Mark_Ready_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Mark Ready","Clicked");
                    waiting_reservations.remove(reservation);
                    reservation.status = "Ready";
                    changestatus(reservation,"Ready");
                    accepted_reservations.add(reservation);
                    notifyDataSetChanged();
                    acc_res_adaptor.notifyDataSetChanged();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Dashboard.this,"My Notification");
                    builder.setContentTitle("It is your turn!");
                    builder.setSmallIcon(R.drawable.request);
                    //builder.setContentText("We will notify you as soon as it's your turn");
                    builder.setAutoCancel(true);
                    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(Dashboard.this);
                    managerCompat.notify(1,builder.build());
                }
            });

            Remove_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Remove Button","Clicked");
                    waiting_reservations.remove(reservation);
                    reservation.status = "Canceled";
                    changestatus(reservation,"Canceled");
                    notifyDataSetChanged();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Dashboard.this,"My Notification");
                    builder.setContentTitle("You have been removed from the waiting list");
                    builder.setSmallIcon(R.drawable.request);
                    //builder.setContentText("We will notify you as soon as it's your turn");
                    builder.setAutoCancel(true);
                    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(Dashboard.this);
                    managerCompat.notify(1,builder.build());
                }
            });
            return convertView;
        }
    }


    public class accepted_reservationAdaptor extends ArrayAdapter<Reservation>{
        public accepted_reservationAdaptor(Context context,ArrayList<Reservation> reservations){
            super(context,0,reservations);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            Reservation reservation = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_dashboard_accepted_reservation_list,parent,false);
            }
            TextView reservation_name = (TextView) convertView.findViewById(R.id.reservation_name);
            Button Check_in_button = (Button) convertView.findViewById(R.id.Check_In);
            Button Cancel_button = (Button) convertView.findViewById(R.id.Cancel);

            fetchName(reservation.userNumber,reservation_name);

            Check_in_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Check in","Clicked");
                    accepted_reservations.remove(reservation);
                    reservation.status = "Finished";
                    changestatus(reservation,"Finished");
                    notifyDataSetChanged();
                }

            });

            Cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Cancel Button","Clicked");
                    accepted_reservations.remove(reservation);
                    reservation.status = "Canceled";
                    changestatus(reservation,"Canceled");
                    notifyDataSetChanged();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Dashboard.this,"My Notification");
                    builder.setContentTitle("The reservation has been cancelled");
                    builder.setSmallIcon(R.drawable.request);
                    builder.setAutoCancel(true);
                    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(Dashboard.this);
                    managerCompat.notify(1,builder.build());
                }

            });
            return convertView;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        String name = intent.getStringExtra("Signed_in");

        create_reservations(name);
        reservation_list = (ListView) findViewById(R.id.reservations_list);
        pend_res_adaptor = new pending_reservationAdaptor(this,pending_reservations);
        reservation_list.setAdapter(pend_res_adaptor);

        waiting_list_list = (ListView) findViewById(R.id.waiting_list_list);
        wait_res_adaptor = new waiting_reservationAdaptor(this,waiting_reservations);
        waiting_list_list.setAdapter(wait_res_adaptor);

        Accepted_reservations_list = (ListView) findViewById(R.id.Accepted_reservations_list);
        acc_res_adaptor = new accepted_reservationAdaptor(this,accepted_reservations);
        Accepted_reservations_list.setAdapter(acc_res_adaptor);

       reservation_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("CLICKED");
                Reservation reservation_selected = (Reservation) adapterView.getItemAtPosition(i);
                Intent intent = new Intent (getApplicationContext(), Popup_dashboard.class);
                intent.putExtra("Reservation",reservation_selected);
                startActivity(intent);
            }
        });

       waiting_list_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("CLICKED");
                Reservation reservation_selected = (Reservation) adapterView.getItemAtPosition(i);
                Intent intent = new Intent (getApplicationContext(), Popup_dashboard.class);
                intent.putExtra("Reservation",reservation_selected);
                startActivity(intent);
            }
        });

       Accepted_reservations_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("CLICKED");
                Reservation reservation_selected = (Reservation) adapterView.getItemAtPosition(i);
                Intent intent = new Intent (getApplicationContext(), Popup_dashboard.class);
                intent.putExtra("Reservation",reservation_selected);
                startActivity(intent);
            }
        });
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){

            NotificationChannel channel= new NotificationChannel("My Notification","My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        signOutButton = (Button) findViewById(R.id.restSignOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, MainScreen.class);
                startActivity(intent);
            }
        });
    }


    public void create_reservations(String name) //for testing purposes
    {
        accepted_reservations = new ArrayList<>();
        waiting_reservations = new ArrayList<>();
        pending_reservations = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservations_ref = database.getReference("Reservations");
        reservations_ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    for (DataSnapshot child : task.getResult().getChildren())
                    {
                        if(child.child("restaurantName").getValue().toString().equals(name))
                        {
                            if(child.child("status").getValue().toString().equals("pending"))
                            {
                                pending_reservations.add(getReservation(child));
                                pend_res_adaptor.notifyDataSetChanged();
                            }
                            else if (child.child("status").getValue().toString().equals("Waiting"))
                            {
                                waiting_reservations.add(getReservation(child));
                                wait_res_adaptor.notifyDataSetChanged();
                            }
                            else if (child.child("status").getValue().toString().equals("Ready"))
                            {
                                accepted_reservations.add(getReservation(child));
                                acc_res_adaptor.notifyDataSetChanged();
                            }
                        }
                    }
                }
                else
                {
                    Log.e("Reservation", "Error fetching data: " + task.getException().getMessage());
                }
            }
        });

    }
    public Reservation getReservation(DataSnapshot child)
    {
        String UniqueID = child.getKey();
        String numberofpeople = child.child("numberOfPeople").getValue().toString();
        String reservationdate = child.child("reservationDate").getValue().toString();
        String reservationTime = child.child("reservationTime").getValue().toString();
        String specialRequests = child.child("specialRequests").getValue().toString();
        String order = child.child("order").getValue().toString();
        String status = child.child("status").getValue().toString();
        String userNumber = child.child("userNumber").getValue().toString();
        String restaurantName = child.child("restaurantName").getValue().toString();
        String feedback = child.child("feedback").getValue().toString();
        Reservation r = new Reservation(UniqueID,numberofpeople,reservationdate,reservationTime,specialRequests,order,userNumber,status,restaurantName,feedback);
        return r;
    }
    public void changestatus(Reservation r,String status)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservations_ref = database.getReference("Reservations");
        reservations_ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    System.out.println("Changed Reservation with Unique ID " + r.uniqueId + " from " + r.status + " to " +  status );
                    reservations_ref.child(r.uniqueId).child("status").setValue(status);
                    System.out.println("Proof: " + reservations_ref.child(r.uniqueId).child("status").toString());
                }
                else
                {
                    Log.e("Reservation", "Error fetching data: " + task.getException().getMessage());
                }
            }
        });
    }
    public void fetchName(String phone,TextView t)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference user_ref = database.getReference("User");
        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t.setText(snapshot.child(phone).child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}