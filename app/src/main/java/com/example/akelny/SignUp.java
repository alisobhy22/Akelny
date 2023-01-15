package com.example.akelny;

import static android.graphics.BlendMode.COLOR;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class SignUp extends AppCompatActivity {

    TextInputEditText nameEntry;
    TextInputEditText userNameEntry;
    EditText passwordEntry;
    EditText confirmPasswordEntry;
    EditText emailEntry;
    EditText numberEntry;

    String name, username, password, confirmPassword, email, number;

    Users users;

    Button registerButton;
    Button goToSignInBtn;

    public void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        String message;
        message= "Please enter one that is at least 8 characters, includes one uppercase letter, one lowercase letter, one number, one special character, does not start or end with a number and should not be the same as the username or in reverse";
        dialog.setMessage(message);
        dialog.setTitle("Invalid password");
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
        setContentView(R.layout.activity_sign_up);

        nameEntry= (TextInputEditText) findViewById(R.id.nameEntry);
        userNameEntry= (TextInputEditText) findViewById(R.id.userNameEntry);
        passwordEntry= (EditText) findViewById(R.id.passwordEntry);
        confirmPasswordEntry= (EditText) findViewById(R.id.confirmPasswordEntry);
        emailEntry= (EditText) findViewById(R.id.emailEntry);
        numberEntry= (EditText) findViewById(R.id.numberEntry);



        registerButton= (Button) findViewById(R.id.registerButton);
        goToSignInBtn= (Button) findViewById(R.id.goToSignInBtn);

        users= new Users();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name= String.valueOf(nameEntry.getText());
                username= String.valueOf(userNameEntry.getText());
                password= passwordEntry.getText().toString();
                confirmPassword= confirmPasswordEntry.getText().toString();
                email= String.valueOf(emailEntry.getText());
                number= String.valueOf(numberEntry.getText());
                int checkRegisterReturn;
                checkRegisterReturn= users.register(name, username, String.valueOf(passwordEntry.getText()), confirmPassword, email, number);
                if (checkRegisterReturn==0)
                {
                    Intent intent= new Intent(SignUp.this, Login.class);
                    intent.putExtra("name", name);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("confirmed password", password);
                    intent.putExtra("email", email);
                    intent.putExtra("number", number);
                    startActivity(intent);

                }
                else if(checkRegisterReturn==1)
                {
                    userNameEntry.setHint("Username already exists");
                    userNameEntry.setHintTextColor(Integer.parseInt("FF0000"));
                }else if(checkRegisterReturn==2)
                {
                    alertDialog();
                }
                else if(checkRegisterReturn==3)
                {
                    confirmPasswordEntry.setHint("Passwords do not match");
                    confirmPasswordEntry.setHintTextColor(Integer.parseInt("FF0000"));
                }
                else if(checkRegisterReturn==4)
                {
                    numberEntry.setHint("Your phone number is not valid");
                    numberEntry.setHintTextColor(Integer.parseInt("FF0000"));
                }else if (checkRegisterReturn==5)
                {
                    emailEntry.setHint("Your email is not valid");
                    emailEntry.setHintTextColor(Integer.parseInt("FF0000"));
                }
            }
        });

        goToSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });

    }
}