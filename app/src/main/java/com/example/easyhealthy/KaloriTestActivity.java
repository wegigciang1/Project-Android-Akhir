package com.example.easyhealthy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class KaloriTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalori_test);

        ImageView gambarProfile = findViewById(R.id.gambarKaloriBackground);
        TextView beratA = findViewById(R.id.textViewBeratA);
        TextView beratB = findViewById(R.id.textViewBeratB);
        TextView beratC = findViewById(R.id.textViewBeratC);

        TextView keteranganA = findViewById(R.id.textViewKeteranganA);
        TextView keteranganB = findViewById(R.id.textViewKeteranganB);
        TextView keteranganC = findViewById(R.id.textViewKeteranganC);

        EditText inputDate = findViewById(R.id.editTextDate);
        Button btnPilih = findViewById(R.id.btnPilihKalori);
    }
}
