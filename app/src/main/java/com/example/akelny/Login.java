package com.example.akelny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    TextInputEditText userNameEntry;
    EditText passwordEntry;

    String username1, password1;

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

        userNameEntry= (TextInputEditText) findViewById(R.id.userNameEntry);
        passwordEntry= (EditText) findViewById(R.id.editTextTextPassword1);

        signInBtn= (Button) findViewById(R.id.button);
        registerBtn= (Button) findViewById(R.id.button2);

        Users user1 = new Users();

        String name= getIntent().getStringExtra("name");
        String username= getIntent().getStringExtra("username");
        String email= getIntent().getStringExtra("email");
        String number= getIntent().getStringExtra("number");
        String password= getIntent().getStringExtra("password");
        String confirmed_password= getIntent().getStringExtra("confirmed password");
        user1.register(name,username,password,confirmed_password, email, number);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username1= String.valueOf(userNameEntry.getText());
                password1= String.valueOf(passwordEntry.getText());
                if (user1.ValidateUser(username1, password1))
                {
                    Intent intent= new Intent(Login.this, Homepage.class);
                    startActivity(intent);
                }else
                {
                    alertDialog();
                }
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