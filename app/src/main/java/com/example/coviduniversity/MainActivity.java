package com.example.coviduniversity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase dbase;
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();

        dbref.child("brody").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView brodyNum = findViewById(R.id.textView2);
                long num = dataSnapshot.getChildrenCount();
                brodyNum.setText(String.valueOf(num) + " students active");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button signout = findViewById(R.id.button6);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, Landing.class);
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
