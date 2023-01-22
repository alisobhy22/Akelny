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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    TextInputEditText userNameEntry;
    EditText numberEntry;

    String username, number;

    Button signInBtn;
    Button registerBtn;

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
        setContentView(R.layout.activity_login);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userNameEntry= (TextInputEditText) findViewById(R.id.userNameEntry);
        numberEntry= (EditText) findViewById(R.id.numberEntry);

        signInBtn= (Button) findViewById(R.id.button);
        registerBtn= (Button) findViewById(R.id.button2);

        Users users = new Users();

        final boolean[] success = {false};
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = String.valueOf(userNameEntry.getText());
                number = String.valueOf(numberEntry.getText());
                System.out.println("In `click");
                DatabaseReference users = database.getReference("User");

                users.child(number).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        System.out.println("Completed!");

                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                for (DataSnapshot child : task.getResult().getChildren()) {

                                    System.out.println("Found data!!!!!!");
                                    String userName = child.getValue().toString();
                                    String phoneNumber = child.child(number).getKey();
                                    System.out.println(userName);
                                    System.out.println(phoneNumber);
                                    if (phoneNumber.equals(number) && userName.equals(username)) {
                                        Intent intent = new Intent(Login.this, Homepage.class);
                                        intent.putExtra("user name", username);
                                        intent.putExtra("user number", number);
                                        startActivity(intent);
                                    }
                                }
                            } else {
                                alertDialog();
                            }
                        } else {
                            alertDialog();
                        }
                    }
                });

                users.child(number).get().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("FAILED TO GET THE DATA FROM THE DATABASE LOGIN");
                    }
                });

                users.child(number).get().addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        System.out.println("CANCELLED TO GET THE DATA FROM THE DATABASE LOGIN");
                    }
                });

                users.child(number).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        System.out.println("SUCCESS TO GET THE DATA FROM THE DATABASE LOGIN");
                    }
                });
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}