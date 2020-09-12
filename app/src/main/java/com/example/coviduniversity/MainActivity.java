package com.example.coviduniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase dbase;
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    AlertDialog signOutWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();

        dbref.child("brody").setValue(1);


        Button b = findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Brody.class);
                startActivity(intent);
            }
        });

        Button signout = findViewById(R.id.button6);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, Landing.class);
                startActivity(intent);
            }
        });

        Button testImageUploadButton = findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestImageUpload.class);
                startActivity(intent);
            }
        });

        //onclick method for the three bubbles
        findViewById(R.id.main_brody_bubble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent brody_activity = new Intent(getApplicationContext(), Brody.class);
                startActivity(brody_activity);
            }
        });

        findViewById(R.id.main_gym_bubble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gym_activity = new Intent(getApplicationContext(), Gym.class);
                startActivity(gym_activity);
            }
        });

        findViewById(R.id.main_levering_bubble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent levering_activity = new Intent(getApplicationContext(), FFC.class);
                startActivity(levering_activity);
            }
        }); 

    }
}
