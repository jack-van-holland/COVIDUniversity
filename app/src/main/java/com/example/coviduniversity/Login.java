package com.example.coviduniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private FirebaseDatabase dbase;
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();


        Button submit = findViewById(R.id.login_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeLogIn(v);
            }
        });

    }
    public void completeLogIn(View v) {
        TextView emailBox = findViewById(R.id.login_email);
        TextView passwordBox = findViewById(R.id.login_password);

        String email = emailBox.getText().toString();
        String password = passwordBox.getText().toString();

        if (email.equals("") || password.equals("")) {
            String notFilled = "Missing fields";
            Toast.makeText(getApplicationContext(), notFilled, Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(Login.this, "Log In Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void onAuthSuccess(FirebaseUser user) {
        // Go to MainActivity
        String name = user.getDisplayName().split(" ")[0];

        setResult(RESULT_OK);
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
        String success = "Welcome, " + name + "!";
        Toast.makeText(getApplicationContext(), success, Toast.LENGTH_SHORT).show();

    }
}
