package com.example.akelny;

import static android.graphics.BlendMode.COLOR;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SignUp extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    TextInputEditText nameEntry;
    EditText numberEntry;

    String name, number;

    Users users;

    Button registerButton;
    Button goToSignInBtn;

    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);

        nameEntry = (TextInputEditText) findViewById(R.id.nameEntry);
        numberEntry = (EditText) findViewById(R.id.numberEntry);


        registerButton = (Button) findViewById(R.id.registerButton);
        goToSignInBtn = (Button) findViewById(R.id.goToSignInBtn);

        users = new Users();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = String.valueOf(nameEntry.getText());
                number = String.valueOf(numberEntry.getText());
                int checkRegisterReturn;

                if (users.validateNumber(number))
                {
                    users.register(name, number);
                    Intent intent= new Intent(SignUp.this, Login.class);
                    startActivity(intent);
                }
                else
                {
                    numberEntry.setHint("Phone number is incorrect");
                    numberEntry.setHintTextColor(Integer.parseInt("FF0000"));
                }
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

        goToSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SignUp.this, Login.class);
                intent.putExtra("code", "0");
                startActivity(intent);
            }
        });

    }
}