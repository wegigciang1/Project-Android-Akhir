package com.example.easyhealthy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AboutButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_button);


        Intent intent = getIntent();
        getSupportActionBar().setTitle("About Us");


        //Pengenalan Variable
        ImageView imageAbout = findViewById(R.id.gambarAbout);
        TextView textAbout = findViewById(R.id.textAbout);


    }
}
