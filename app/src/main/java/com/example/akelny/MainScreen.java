package com.example.akelny;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainScreen extends AppCompatActivity {

    Button userBtn;
    Button restaurantBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        userBtn = (Button) findViewById(R.id.userBtn);
        restaurantBtn = (Button) findViewById(R.id.restaurantBtn);

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();
                AuthUI.IdpConfig number_auth = new AuthUI.IdpConfig.PhoneBuilder().build();
                Intent intent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(number_auth))
                        .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                        .setAlwaysShowSignInMethodScreen(true)
                        .setIsSmartLockEnabled(false)
                        .build();

                startActivityForResult(intent, 10001);

            }
        });
        restaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainScreen.this, RestaurantSignIn.class);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            if (resultCode == RESULT_OK) {
                // We have signed in the user or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("TAG", "onActivityResult: " + user.toString());
                //Checking for User (New/Old)
                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                    //This is a New User
                    //Log.d("TAG", "New USer " + user.toString());


                } else {
                    //This is a returning user

                }


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference users = database.getReference("User");
                users.child(user.getPhoneNumber()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                for (DataSnapshot child : task.getResult().getChildren()) {
                                    ArrayList<String> cuisines;
                                    String userName = child.getValue().toString();
                                    String phoneNumber = child.child(user.getPhoneNumber()).getKey();
                                    cuisines= child.child("favCuisines").getValue(new ArrayList<String>().getClass());

                                    System.out.println("HAAAI");
                                    for (String cuisine:cuisines) {
                                        System.out.println(cuisine);
                                    }
                                    if (phoneNumber.equals(user.getPhoneNumber())) {
                                        Intent intent = new Intent(MainScreen.this, Homepage.class);
                                        intent.putExtra("user number",user.getPhoneNumber());
                                        intent.putExtra("user name",userName);
                                        startActivity(intent);
                                    }

                                }
                            }else {
                                Intent intent = new Intent(MainScreen.this,SignUp.class);
                                intent.putExtra("number",user.getPhoneNumber());
                                startActivity(intent);

                            }
                        } else {
                            Intent intent = new Intent(MainScreen.this,SignUp.class);
                            intent.putExtra("number",user.getPhoneNumber());
                            startActivity(intent);
                        }
                    }
                });

            } else {
                // Signing in failed
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Log.d("TAG", "onActivityResult: the user has cancelled the sign in request");
                } else {
                    Log.e("TAG", "onActivityResult: ", response.getError());
                }
            }
        }
    }
}