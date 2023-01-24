package com.example.akelny;

import static android.graphics.BlendMode.COLOR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SignUp extends AppCompatActivity {

    TextInputEditText nameEntry;
    EditText numberEntry;

    String name, number;

    Users users;

    Button registerButton;
    Button goToSignInBtn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        String verified_number = getIntent().getExtras().getString("number");





        //IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);

        nameEntry = (TextInputEditText) findViewById(R.id.nameEntry);
        //numberEntry = (EditText) findViewById(R.id.numberEntry);


        registerButton = (Button) findViewById(R.id.registerButton);
        //goToSignInBtn = (Button) findViewById(R.id.goToSignInBtn);

        users = new Users();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = String.valueOf(nameEntry.getText());
                int checkRegisterReturn;

                            Intent intent= new Intent(SignUp.this, CuisinePreferences.class);
                            intent.putExtra("phone",verified_number);
                            intent.putExtra("name", name);
                            startActivity(intent);


                    //Task<Void> task = SmsRetriever.getClient(getApplicationContext()).startSmsUserConsent(null);

                    //IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
                    //registerReceiver(smsVerificationReceiver, intentFilter);




                /*checkRegisterReturn= users.register(name, number);
                if (checkRegisterReturn==0)
                {
                    Intent intent= new Intent(SignUp.this, Login.class);
                    startActivity(intent);
                }
                else if(checkRegisterReturn==1)
                {
                    numberEntry.setHint("Phone number already exists");
                    numberEntry.setHintTextColor(Integer.parseInt("FF0000"));
                }else if(checkRegisterReturn==2)
                {
                    numberEntry.setHint("Your phone number is not valid");
                    numberEntry.setHintTextColor(Integer.parseInt("FF0000"));
                }*/
            }
        });



    }



}