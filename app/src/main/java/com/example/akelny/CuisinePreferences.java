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
    CheckBox japeneseChk;

    Button doneBtn;

    String userNum;

    ArrayList<String> cuisines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_preferences);

        userNum = getIntent().getExtras().getString("phone");

        internationalChk= (CheckBox) findViewById(R.id.checkBox1);
        japeneseChk= (CheckBox) findViewById(R.id.checkBox2);
        doneBtn= (Button) findViewById(R.id.button_done);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internationalChk.isChecked())
                {
                    cuisines.add("International");
                }
                if (japeneseChk.isChecked())
                {
                    cuisines.add("Japenese");
                }
                if (!japeneseChk.isChecked() && !internationalChk.isChecked())
                {
                    cuisines.add("");
                }
                myRef.child("User").child(userNum).child("cuisines").setValue(cuisines);
                Intent intent= new Intent(CuisinePreferences.this, Login.class);
                startActivity(intent);
            }
        });

    }
}