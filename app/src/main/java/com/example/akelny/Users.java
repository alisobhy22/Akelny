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

    public boolean validateEmail(String email) {
        boolean validated = false;
        String regex;
        regex = "\\w*@\\w*.edu";
        Pattern pt = Pattern.compile(regex);
        Matcher mt = pt.matcher(email);
        validated = mt.matches();

        return validated;
    }


    public boolean ValidateUser(String name, String phone) //login
    {
        DatabaseReference users = database.getReference("User");
        users.child(phone).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("phone",phone);
                if (task.isSuccessful()){

                    if (task.getResult().exists()){
                        Log.d("msg","First");

                        validated = true;
                    }else {
                        Log.d("msg","Second");
                        validated = false;
                    }
                }else {
                    Log.d("msg","Third");
                    validated = false;
                }
            }
        });
        Log.d("return",String.valueOf(validated));
        return validated;
    }


    boolean verifyPassword(String password, String username) {

        if (password.length() < 8) {
            return false;
        }
        /*boolean hasLower = false, hasUpper = false, hasDigit = false, hasSpecial = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (c == '!' || c == '"' || c == '#' || c == '$' || c == '%' || c == '&' || c == '(' || c == ')' || c == '*' || c == '+' || c == ',' || c == '-' || c == '.' || c == '/' || c == ':' || c == ';' || c == '<' || c == '=' || c == '>' || c == '?' || c == '@' || c == '['  || c == ']' || c == '^' || c == '_' || c == '`' || c == '{' || c == '|' || c == '}' || c == '~') {
                hasSpecial = true;
            }
        }
        if (!hasLower || !hasUpper || !hasDigit || !hasSpecial) {
            return false;
        }
        if (Character.isDigit(password.charAt(0)) || Character.isDigit(password.charAt(password.length() - 1))) {
            return false;
        }
        if (password.equals(username) || new StringBuilder(password).reverse().toString().equals(username)) {
            return false;
        }*/
        return true;
    }

    public int register(String name, String phoneNum) {

      /*  for (int i = 0; i < users.size(); i++) {
            user temp;
            temp = users.get(i);
            if (temp.phoneNum.equals(phoneNum))
                return 1;//phone number already exists
        }

        if (!validateNumber(phoneNum)) {
            return 2;//number is not valid (note, it should start with 0020)
        }*/


        user userToRegister = new user(name, phoneNum);
        users.add(userToRegister);
        myRef.child("User").child(phoneNum).setValue(userToRegister);
        return 0;//successful registration
    }
}