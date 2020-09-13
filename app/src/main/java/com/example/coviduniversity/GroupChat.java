package com.example.coviduniversity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class GroupChat extends AppCompatActivity {

    private FirebaseDatabase dbase;
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String room;

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
        room = getIntent().getExtras().getString("room");

        dbref.child("brody_rooms").child(room).child("roster").child(user.getUid()).setValue(1);

        LinearLayout chatRoom = findViewById(R.id.chat_room);
        chatRoom.setOrientation(LinearLayout.VERTICAL);
        chatRoom.setGravity(Gravity.BOTTOM);
        update(room, chatRoom);
        Button sendButton = findViewById(R.id.sendText);
        EditText textMessage = findViewById(R.id.messageText);
        //textMessage.requestFocus();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textMessage = findViewById(R.id.messageText);
                String text = textMessage.getText().toString();
                String name = user.getDisplayName();
                String id = user.getUid();
                Message m = new Message(text, name, id, new Date().getTime());

                dbref.child("brody_rooms").child(room).child("chats").push().setValue(m);
                textMessage.setText("");
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();

        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();

        dbref.child("brody_rooms").child(room).child("roster").child(user.getUid()).removeValue();
        dbref.child("brody_rooms").child(room).child("roster").setValue(1);

    }

    public void update(String room, LinearLayout l) {
        dbref.child("brody_rooms").child(room).child("chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                chat.add(dataSnapshot.getValue(Message.class));
                Collections.sort(chat);
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
        for (int i = 0; i < chat.size(); i++) {
            Message m = chat.get(i);
            TextView t = new TextView(this);
            if (m.getId().equals(user.getUid())) {
                t.setBackgroundResource(R.drawable.sent_text_bubble);
            } else {
                TextView name = new TextView(this);
                Date d = new Date(m.getTime());
                name.setText(m.getName() + " " + d.toString());
                name.setPadding(10, 0, 10, 10);
                chatRoom.addView(name);
                t.setBackgroundResource(R.drawable.recieved_text_bubble);
            }
            t.setText(m.getText());
            t.setPadding(50,5,50,25);
            chatRoom.addView(t);
            TextView dummy = new TextView(this);
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
