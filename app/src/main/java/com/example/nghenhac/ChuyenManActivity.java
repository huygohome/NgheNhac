package com.example.nghenhac;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class ChuyenManActivity extends AppCompatActivity {
    Button btnOffline, btnOnline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_man);
        btnOffline = findViewById(R.id.NgheNhacOffline);
        btnOnline = findViewById(R.id.KhamPhaOnline);
        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ListActivity for offline music
                Intent intentOffline = new Intent(ChuyenManActivity.this, ListActivity.class);
                startActivity(intentOffline);
            }
        });

        // Set OnClickListener for the online button
        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to OnlineMusicActivity for online music
                Intent intentOnline = new Intent(ChuyenManActivity.this, OnlineMusicActivity.class);
                startActivity(intentOnline);
            }
        });
    }
}