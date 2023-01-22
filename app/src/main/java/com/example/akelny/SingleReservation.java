package com.example.akelny;

import android.widget.Button;

public class SingleReservation {

    public String name;
    public String date;
    public String time;
    public String people;
    public String order;
    public String specialRequests;
    public String status;

    public Button feedback;
    public Button cancel;

    // create constructor to set the values for all the parameters of the each single view
    public SingleReservation(String Name, String Date, String Time, String People, String Order, String SpecialRequests, String Status, Button Feedback, Button Cancel) {
        name = Name;
        date = Date;
        time = Time;
        people = People;
        order = Order;
        specialRequests = SpecialRequests;
        status = Status;
        feedback = Feedback;
        cancel = Cancel;
    }
}
