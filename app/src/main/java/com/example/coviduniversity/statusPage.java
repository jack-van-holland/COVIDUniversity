package com.example.coviduniversity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class statusPage extends AppCompatActivity {

    private FirebaseDatabase dbase;
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    private FirebaseUser user;

    List<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_page);
        users = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();

        LinearLayout userPage = findViewById(R.id.userPage);
        update(userPage);
    }

    public void update(LinearLayout l) {
        dbref.child("brody").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                users.add(dataSnapshot.getKey().toString());
                updateChat(l);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateChat(LinearLayout chatRoom) {
        chatRoom.removeAllViews();
        chatRoom.setPadding(50, 50, 50, 200);
        TextView dummy = new TextView(this);
        dummy.setPadding(0, 5, 0, 5);
        chatRoom.addView(dummy);
        dummy = new TextView(this);
        dummy.setPadding(0, 5, 0, 5);
        chatRoom.addView(dummy);
        dummy = new TextView(this);
        dummy.setPadding(0, 5, 0, 5);
        chatRoom.addView(dummy);
        for (int i = 0; i < users.size(); i++) {
            String userId = users.get(i);
            TextView t = new TextView(this);
            String name = dbref.child("users").child(userId).child("name").toString();
            String major = dbref.child("users").child(userId).child("major").toString();
            String year = dbref.child("users").child(userId).child("year").toString();
            String pic = dbref.child("users").child(userId).child("profilePicStorageName").toString();

            if (!userId.equals(user.getUid())) {
                TextView nameView = new TextView(this);
                nameView.setText(name);
                nameView.setPadding(10, 0, 10, 10);
                chatRoom.addView(nameView);
                TextView majorView = new TextView(this);
                majorView.setText(major);
                majorView.setPadding(10, 0, 10, 10);
                chatRoom.addView(majorView);
                TextView yearView = new TextView(this);
                yearView.setText(year);
                yearView.setPadding(10, 0, 10, 10);
                chatRoom.addView(yearView);
                TextView picView = new TextView(this);
                picView.setText(pic);
                picView.setPadding(10, 0, 10, 10);
                chatRoom.addView(picView);
            }
            dummy = new TextView(this);
            dummy.setPadding(0, 5, 0, 5);
            chatRoom.addView(dummy);
        }
        ScrollView s = (ScrollView)chatRoom.getParent();
        s.post(new Runnable() {
            @Override
            public void run() {
                s.scrollTo(0, s.getBottom());
            }
        });
    }
}
