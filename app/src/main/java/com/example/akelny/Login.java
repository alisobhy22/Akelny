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

import com.google.android.gms.tasks.OnCompleteListener;
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

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = String.valueOf(userNameEntry.getText());
                number = String.valueOf(numberEntry.getText());

                DatabaseReference users = database.getReference("User");
                users.child(number).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().exists()) {
                                Log.d("msg", "First");
                                Intent intent = new Intent(Login.this, TableReservation.class);
                                intent.putExtra("user number", number);
                                startActivity(intent);

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
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}