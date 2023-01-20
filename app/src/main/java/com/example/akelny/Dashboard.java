package com.example.akelny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    public ArrayList<Reservation> reservations;

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


            reservation_name.setText(reservation.userNumber);

            accept_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Accept Button","Clicked");
                }
            });

            decline_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Decline Button","Clicked");
                }
            });

            waiting_list_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Waiting List Button","Clicked");
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


            reservation_name.setText(reservation.userNumber);

            Mark_Ready_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Mark Ready","Clicked");
                }
            });

            Remove_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Remove Button","Clicked");
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


            reservation_name.setText(reservation.userNumber);

            Check_in_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Mark Ready","Clicked");
                }
            });

            Cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Cancel Button","Clicked");
                }
            });
            return convertView;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        reservations = create_reservations();

        ListView reservation_list = (ListView) findViewById(R.id.reservations_list);
        pending_reservationAdaptor pend_res_adaptor = new pending_reservationAdaptor(this,reservations);
        reservation_list.setAdapter(pend_res_adaptor);

        ListView waiting_list_list = (ListView) findViewById(R.id.waiting_list_list);
        waiting_reservationAdaptor wait_res_adaptor = new waiting_reservationAdaptor(this,reservations);
        waiting_list_list.setAdapter(wait_res_adaptor);

        ListView Accepted_reservations_list = (ListView) findViewById(R.id.Accepted_reservations_list);
        accepted_reservationAdaptor acc_res_adaptor = new accepted_reservationAdaptor(this,reservations);
        Accepted_reservations_list.setAdapter(acc_res_adaptor);

    }


    public ArrayList<Reservation> create_reservations() //for testing purposes
    {
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();

        for(int i = 0; i < 10; i++)
        {
            Reservation r = new Reservation(Integer.toString(i),"1/18/2023",Integer.toString(i) +
                    ":00 PM",Integer.toString(i), "pasta", "01060188396","Pending","Crave");
            reservations.add(r);
        }


        return reservations;
    }
}