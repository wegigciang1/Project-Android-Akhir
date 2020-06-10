package com.example.easyhealthy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ResetBeratActivity extends AppCompatActivity {
    private FirebaseAuth mfirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mfirebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_berat);

        final EditText txtBerat = findViewById(R.id.editTextResetBerat);
        Button btnSaveBerat = findViewById(R.id.btnResetBerat);
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        final Date date = new Date();
        final ProgressBar progressBar = findViewById(R.id.progressBarReset);


        btnSaveBerat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> beratUser = new HashMap<>();
                beratUser.put("berat", txtBerat.getText().toString());
                beratUser.put("tanggal", sdf.format(date));

                mfirebaseFirestore.collection("Users").document(mfirebaseAuth.getCurrentUser().getUid()).collection("Berat Badan").document().set(beratUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "Berhasil : " + mfirebaseAuth.getCurrentUser().getUid());
                                progressBar.setVisibility(View.GONE);

                                Intent gotoRencana = new Intent(ResetBeratActivity.this, RencanaActivity.class);

                                startActivity(gotoRencana);
                                finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", e.toString());
                            }
                        });
            }
        });
    }
}
