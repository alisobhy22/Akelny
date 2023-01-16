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
    public class user {
        public String name;
        public String phoneNum;

        public user(String name, String Phone) {
            this.name= name;
            this.phoneNum = Phone;
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
    public int register(String name, String phoneNum) {
        if(validateNumber(phoneNum)) {
            user userToRegister = new user(name, phoneNum);
            users.add(userToRegister);
            myRef.child("User").child(phoneNum).setValue(userToRegister);
            return 0;//successful registration
        }
        else
            return 1;
    }
}