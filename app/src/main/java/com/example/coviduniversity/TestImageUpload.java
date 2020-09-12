package com.example.coviduniversity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.storage.StorageReference;

public class TestImageUpload extends AppCompatActivity {

    Button choose, upload;
    ImageView img;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image_upload);
        choose = (Button)findViewById(R.id.button_ch);
        upload = (Button)findViewById(R.id.button_up);
        img = (ImageView)findViewById(R.id.image_view);
    }


}