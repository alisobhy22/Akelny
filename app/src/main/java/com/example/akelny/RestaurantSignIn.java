package com.example.akelny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurantSignIn extends AppCompatActivity {

    TextInputEditText restNameEntry;
    EditText passwordEntry;

    String restaurantName, restaurantPassword;

    Button signInBtn;
    Button goToUserLogin;

    public void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        String message;
        message= "Login was not successful";
        dialog.setMessage(message);
        dialog.setTitle("Invalid login");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });

        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_sign_in);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        restNameEntry= (TextInputEditText) findViewById(R.id.restNameEntry);
        passwordEntry= (EditText) findViewById(R.id.restPassword);

        signInBtn= (Button) findViewById(R.id.button);
        goToUserLogin= (Button) findViewById(R.id.button2);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantName = String.valueOf(restNameEntry.getText());
                restaurantPassword = String.valueOf(passwordEntry.getText());

                DatabaseReference restaurants = database.getReference("Restaurants");

                restaurants.child(restaurantName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot child = task.getResult();
                            if (task.getResult().exists()) {
                                if (child.child("Account Password").getValue().toString().equals(restaurantPassword))
                                {
                                    Intent intent = new Intent(RestaurantSignIn.this, Homepage.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    alertDialog();
                                }
                            } else {
                                alertDialog();
                            }
                        } else {
                            alertDialog();
                        }
                    }
                });
            }
        });

    }
}