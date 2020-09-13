package com.example.coviduniversity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.invoke.ConstantCallSite;

public class FFC extends AppCompatActivity {

    Button mButtonToMenu;
    Button mButtonToChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_f_c);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mButtonToMenu = findViewById(R.id.button_to_menu);
        mButtonToChat = findViewById(R.id.button_to_levering_chat);

        mButtonToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ImagesActivity.class);
                startActivity(intent);
            }
        });

        mButtonToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LeveringChat.class);
                startActivity(intent);
            }
        });


    }


}
