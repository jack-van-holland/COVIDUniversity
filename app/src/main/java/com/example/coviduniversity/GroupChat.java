package com.example.coviduniversity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class GroupChat extends AppCompatActivity {

    private FirebaseDatabase dbase;
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    private FirebaseUser user;

    List<Message> chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        chat = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();


        //get the room number
        String room = getIntent().getExtras().getString("room");

        update(room);

        LinearLayout chatRoom = findViewById(R.id.chat_room);
        chatRoom.setOrientation(LinearLayout.VERTICAL);
        chatRoom.setGravity(Gravity.BOTTOM);

        updateChat(chatRoom);
        Button sendButton = findViewById(R.id.sendText);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textMessage = findViewById(R.id.messageText);
                String text = textMessage.getText().toString();
                String name = user.getDisplayName();
                String id = user.getUid();
                Message m = new Message(text, name, id, new Date().getTime());

                dbref.child("brody_rooms").child(room).push().setValue(m);
                textMessage.setText("");
            }
        });
    }


    public void update(String room) {
        dbref.child("brody_rooms").child(room).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                chat.add(dataSnapshot.getValue(Message.class));
                Collections.sort(chat);
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
        for (Message m : chat) {
            TextView t = new TextView(this);
            t.setText(m.getText());
            chatRoom.addView(t);
        }
    }


}
