package com.example.easyhealthy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RencanaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rencana);

        CardView planMenurunkan = findViewById(R.id.pilihMenurunkan);
        CardView planMenaikkan = findViewById(R.id.pilihMenaikkan);
        final TextView planPilihan = findViewById(R.id.pilihanRencana);
        final TextView autoLama = findViewById(R.id.autoLamaRencana);
        final EditText targetBerat = findViewById(R.id.inputTargetBerat);


        Button btnHitung = findViewById(R.id.btnHitungRencana);

        final Button btnNext = findViewById(R.id.btnNextToHome);

        planMenurunkan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                planPilihan.setText("Menurunkan Berat Badan");
            }
        });

        planMenaikkan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                planPilihan.setText("Menaikkan Berat Badan");
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(planPilihan.getText().toString().isEmpty()){
                    Toast.makeText(RencanaActivity.this, "Pilih Rencana", Toast.LENGTH_SHORT).show();
                }else{
                    Intent goToHome = new Intent(getApplicationContext(), TahapRencanaActivity.class);
                    startActivity(goToHome);
                    finish();
                }
            }
        });
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int target = Integer.parseInt(targetBerat.getText().toString());
                int total = target * 2;

                autoLama.setText(total + " Minggu");
                btnNext.setVisibility(View.VISIBLE);
            }
        });

    }
}
