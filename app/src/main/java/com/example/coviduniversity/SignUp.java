package com.example.coviduniversity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private FirebaseDatabase dbase;
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final TextView nameBox = findViewById(R.id.signup_name);

        final TextView emailBox = findViewById(R.id.sign_up_email);

        final TextView passwordBox = findViewById(R.id.signup_password);

        Button submit = findViewById(R.id.sign_up_button);

        auth = FirebaseAuth.getInstance();

        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();

        final String[] info = new String[3];
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info[0] = emailBox.getText().toString();
                info[1] = passwordBox.getText().toString();
                info[2] = nameBox.getText().toString();
                createAccount(info);
            }
        });
    }
    public void createAccount(final String[] info) {
        auth.createUserWithEmailAndPassword(info[0], info[1])
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser(), info[2]);
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(SignUp.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;                        }
                    }
                });
    }
    private void onAuthSuccess(FirebaseUser user, String name) {
        // Add name field to user
        UserProfileChangeRequest nameChange = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        user.updateProfile(nameChange);

        // Sign out so user can log in with created information
        auth.signOut();

        //go to login
        startActivity(new Intent(SignUp.this, Login.class));
        finish();
        String success = "Successfully signed up!";
        Toast.makeText(getApplicationContext(), success, Toast.LENGTH_SHORT).show();
    }
}
