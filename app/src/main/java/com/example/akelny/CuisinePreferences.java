package com.example.akelny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CuisinePreferences extends AppCompatActivity {

    public FirebaseDatabase database;
    public DatabaseReference myRef;

    CheckBox internationalChk;
    CheckBox japaneseChk;
    CheckBox italianChk;
    CheckBox egyptianChk;
    CheckBox mexicanChk;
    CheckBox lebaneseChk;

    Button doneBtn;
    Button skipBtn;

    String userNum, name;

    ArrayList<String> cuisines= new ArrayList<>();
    Users users = new Users();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_preferences);

        internationalChk= (CheckBox) findViewById(R.id.checkBox1);
        japaneseChk= (CheckBox) findViewById(R.id.checkBox2);
        italianChk= (CheckBox) findViewById(R.id.checkBox3);
        egyptianChk= (CheckBox) findViewById(R.id.checkBox4);
        mexicanChk= (CheckBox) findViewById(R.id.checkBox5);
        lebaneseChk= (CheckBox) findViewById(R.id.checkBox6);
        doneBtn= (Button) findViewById(R.id.button_done);
        skipBtn= (Button) findViewById(R.id.button_skip);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internationalChk.isChecked())
                {
                    cuisines.add("International");
                }
                if (japaneseChk.isChecked())
                {
                    cuisines.add("Japanese");
                }
                if (italianChk.isChecked())
                {
                    cuisines.add("Italian");
                }
                if (egyptianChk.isChecked())
                {
                    cuisines.add("Egyptian");
                }
                if (mexicanChk.isChecked())
                {
                    cuisines.add("Mexican");
                }
                if (lebaneseChk.isChecked())
                {
                    cuisines.add("Lebanese");
                }
                else if(!internationalChk.isChecked() && !japaneseChk.isChecked()
                        && !italianChk.isChecked() && !egyptianChk.isChecked() && !mexicanChk.isChecked()
                        && !lebaneseChk.isChecked())
                    cuisines.add("");
                userNum = getIntent().getExtras().getString("phone");
                name= getIntent().getExtras().getString("name");
                users.register(name, userNum, cuisines);
                Intent intent= new Intent(CuisinePreferences.this, Homepage.class);
                intent.putExtra("user name",name);
                intent.putExtra("user number",userNum);
                intent.putExtra("cuisines", cuisines);
                startActivity(intent);
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisines.add("");
                userNum = getIntent().getExtras().getString("phone");
                name= getIntent().getExtras().getString("name");
                users.register(name, userNum, cuisines);
                Intent intent= new Intent(CuisinePreferences.this, Homepage.class);
                intent.putExtra("user name",name);
                intent.putExtra("user number",userNum);
                intent.putExtra("cuisines", cuisines);
                startActivity(intent);
            }
        });
    }
}