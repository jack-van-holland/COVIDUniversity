package com.example.coviduniversity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Brody extends AppCompatActivity {

    private FirebaseDatabase dbase;
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    private FirebaseUser user;

    //The table list
    RecyclerView existing_table;
    RecyclerView.LayoutManager table_layoutManager;
    List_adapter table_adapter;
    private static ArrayList<Brody_table_list> table_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brody);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();

        final int[] curr = new int[1];

        dbref.child("brody").child(user.getUid()).setValue(user.getUid());

        //set onclick listener for the back to map button
        findViewById(R.id.brody_back_to_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_to_map = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(back_to_map);
            }
        });
       // existing_table.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        populateList();
        buildRecyclerView();
        dbref.child("brody").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView brodyNum = findViewById(R.id.numUsers);
                long num = dataSnapshot.getChildrenCount();
                brodyNum.setText(String.valueOf(num) + " students active");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button users = findViewById(R.id.Users);
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Brody.this, statusPage.class);
                startActivity(intent);
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        final int[] curr = new int[1];

        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();

        dbref.child("brody").child(user.getUid()).removeValue();
        dbref.child("brody").setValue(1);

    }

    public void buildRecyclerView() {
        existing_table = findViewById(R.id.brody_table_list);
        existing_table.addItemDecoration(new DividerItemDecoration(existing_table.getContext(), DividerItemDecoration.VERTICAL));
        existing_table.setHasFixedSize(true);
        table_layoutManager = new LinearLayoutManager(this);
        table_adapter = new List_adapter(table_list);
        existing_table.setLayoutManager(table_layoutManager);
        existing_table.setAdapter(table_adapter);
        table_adapter.SetOnItemClickListener(new List_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(Brody.this, GroupChat.class);
                intent.putExtra("room", "Table Name " + position);
                startActivity(intent);
            }
        });

    }

    public void populateList() {
        table_list = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            table_list.add(new Brody_table_list("Table Name " + i, "Doing sth"));
            Log.d("List", "added" + i);
        }
    }
}
