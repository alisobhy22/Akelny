package com.example.akelny;

import android.util.Log;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Users {

    public FirebaseDatabase database;
    public DatabaseReference myRef;
    public ArrayList<user> users;
    public boolean validated;
    public static class user {
        public String name;
        public String phoneNum;
        public ArrayList<String> favCuisines;

        public user(String name, String Phone, ArrayList<String> favCuisines) {
            this.name= name;
            this.phoneNum = Phone;
            this.favCuisines= favCuisines;
        }
    }
    public Users() { // intialzing array
        users = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }
    public boolean validateNumber(String number) {
        boolean validated = false;
        String regex;
        regex = "010\\d{8}|011\\d{8}|012\\d{8}|015\\d{8} ";
        Pattern pt = Pattern.compile(regex);
        Matcher mt = pt.matcher(number);
        validated = mt.matches();


        return validated;
    }
    public boolean validateLogin(String name, String number) {
        final boolean[] validated = {false};
        DatabaseReference userValidation = database.getReference("User");
        userValidation.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete (@NonNull Task< DataSnapshot > task) {
            if (task.isSuccessful()) {
                for (DataSnapshot child : task.getResult().getChildren()) {
                    String phoneNumber = child.getKey();
                    String userName = child.child("name").getValue().toString();

                    if (phoneNumber.equals(number) && userName.equals(name)) {
                        validated[0]= true;
                    }
                }
            } else {
                Log.e("User validation", "Error fetching data: " + task.getException().getMessage());
            }
        }
        });
        return validated[0];
    }
    public void register(String name, String phoneNum) {

            user userToRegister = new user(name, phoneNum, null);
            users.add(userToRegister);
            myRef.child("User").child(phoneNum).setValue(userToRegister);

    }
}