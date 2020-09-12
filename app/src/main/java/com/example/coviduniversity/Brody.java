package com.example.coviduniversity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class Brody extends AppCompatActivity {

    private FirebaseDatabase dbase;
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brody);

        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();

        final int[] curr = new int[1];

        dbref.child("brody").child(user.getUid()).setValue(1);

    }

    protected void onStop() {
        super.onStop();
        final int[] curr = new int[1];

        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();

        dbref.child("brody").child(user.getUid()).removeValue();

    }
}
