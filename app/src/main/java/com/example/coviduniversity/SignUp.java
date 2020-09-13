package com.example.coviduniversity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUp extends AppCompatActivity {

    private FirebaseDatabase dbase;
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private StorageReference rootStorageRef;
    private Uri profilePicUri;
    private ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final TextView nameBox = findViewById(R.id.signup_name);

        final TextView emailBox = findViewById(R.id.sign_up_email);

        final TextView passwordBox = findViewById(R.id.signup_password);
        final Spinner major_spinner = (Spinner) findViewById(R.id.sign_up_major);
        ArrayAdapter<CharSequence> major_adapter = ArrayAdapter.createFromResource(this,
                R.array.majors_array, android.R.layout.simple_spinner_item);
        major_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        major_spinner.setAdapter(major_adapter);

        final Spinner year_spinner = (Spinner) findViewById(R.id.sign_up_year);
        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this,
                R.array.years_array, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(year_adapter);

        Button submit = findViewById(R.id.sign_up_button);


        //PROFILE PIC SETUP
        profilePic = findViewById(R.id.sign_up_photo);
        profilePic.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfilePic();
            }
        });
        rootStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference defaultPfp = rootStorageRef.child("profilePics/default_profile_picture.jpg");
        defaultPfp.getBytes(2000*2000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePic.setImageBitmap(bitmap);
            }
        });


        auth = FirebaseAuth.getInstance();
        dbase = FirebaseDatabase.getInstance();
        dbref = dbase.getReference();
        user = auth.getCurrentUser();


        final String[] info = new String[6];
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info[0] = emailBox.getText().toString();
                info[1] = passwordBox.getText().toString();
                info[2] = nameBox.getText().toString();
                info[3] = major_spinner.getSelectedItem().toString();
                info[4] = year_spinner.getSelectedItem().toString();
                info[5] = profilePicUri != null ? profilePicUri.toString() : null;//if null, then didn't select a photo
                if (!info[0].contains("@jhu.edu")) {
                    Toast.makeText(getApplicationContext(), "Must sign up with a  JHU email.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (info[2].equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your name.", Toast.LENGTH_SHORT).show();
                } else if (info[3].equals("Major")){
                    Toast.makeText(getApplicationContext(), "Please select a major.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (info[4].equals("Year")) {
                    Toast.makeText(getApplicationContext(), "Please select a year.", Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    createAccount(info);
                }
            }
        });
    }
    public void createAccount(final String[] info) {
        auth.createUserWithEmailAndPassword(info[0], info[1])
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser(), info[2], info[3], info[4], info[5]);
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(SignUp.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;                        }
                    }
                });
    }
    private void onAuthSuccess(FirebaseUser user, String name, String major, String year, String profilePicUri) {
        // Add name field to user
        UserProfileChangeRequest nameChange = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        user.updateProfile(nameChange);

        //add the pfp to the database storage and save a reference to it
        String profilePicStorageName;
        if (profilePicUri != null) {
            //did select a pic
            profilePicStorageName = "pfp_for_" + user.getUid();
            StorageReference profilePicStorageLocation = rootStorageRef.child("profilePics/" + profilePicStorageName);
            profilePicStorageLocation.putFile(Uri.parse(profilePicUri));
        } else {
            //didn't select a pic
            profilePicStorageName = "default_profile_picture.jpg";
        }

        dbref.child("users").child(user.getUid()).child("name").setValue(name);
        dbref.child("users").child(user.getUid()).child("major").setValue(major);
        dbref.child("users").child(user.getUid()).child("year").setValue(year);
        dbref.child("users").child(user.getUid()).child("profilePicStorageName").setValue(profilePicStorageName);

        user.updateProfile(nameChange);

        // Sign out so user can log in with created information
        auth.signOut();

        //go to login
        startActivity(new Intent(SignUp.this, Login.class));
        finish();
        String success = "Successfully signed up!";
        Toast.makeText(getApplicationContext(), success, Toast.LENGTH_SHORT).show();
    }


    //HANDLE USER CHOSE PROFILE PIC
    private void chooseProfilePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profilePicUri = data.getData();
            profilePic.setImageURI(profilePicUri);
        }
    }


}
