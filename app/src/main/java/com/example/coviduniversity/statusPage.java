package com.example.coviduniversity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    List<User> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_page);
        users = new ArrayList<>();
        allUsers = new ArrayList<>();
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
        dbref.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                allUsers.add(dataSnapshot.getValue(User.class));
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
            for (User u: allUsers) {
                if (u.getId().equals(users.get(i))) {
                    if (!u.getId().equals(user.getUid())) {
                        TextView nameView = new TextView(this);
                        nameView.setText(u.getName());
                        nameView.setPadding(10, 0, 10, 10);
                        chatRoom.addView(nameView);
                        TextView majorView = new TextView(this);
                        majorView.setText(u.getMajor());
                        majorView.setPadding(10, 0, 10, 10);
                        chatRoom.addView(majorView);
                        TextView yearView = new TextView(this);
                        yearView.setText(u.getYear());
                        yearView.setPadding(10, 0, 10, 10);
                        chatRoom.addView(yearView);
                        TextView picView = new TextView(this);
                        picView.setText(u.getProfilePicStorageName());
                        picView.setPadding(10, 0, 10, 10);
                        chatRoom.addView(picView);
                        //profile photo
                        final ImageView pfpView = new ImageView(this);
                        chatRoom.addView(pfpView);
                        StorageReference userPfpInStorage = FirebaseStorage.getInstance().getReference().child("profilePics/" + u.getProfilePicStorageName());
                        userPfpInStorage.getBytes(2000*2000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                pfpView.setImageBitmap(bitmap);
                            }
                        });
                    }
                    dummy = new TextView(this);
                    dummy.setPadding(0, 5, 0, 5);
                    chatRoom.addView(dummy);
                }
            }
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
