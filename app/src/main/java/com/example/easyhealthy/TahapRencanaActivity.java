package com.example.easyhealthy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TahapRencanaActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collref = mFirebaseFirestore.collection("Users").document(mFirebaseAuth.getCurrentUser().getUid()).collection("Kalori");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tahap_rencana);
        CardView pilihMakanan = findViewById(R.id.pilihMakanan);
        CardView pilihOlahraga = findViewById(R.id.pilihOlahraga);

        final Intent intent = getIntent();


        pilihMakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hitungKalori(collref,intent.getStringExtra("kalori"), 1.53);

            }
        });
        pilihOlahraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hitungKalori(collref,intent.getStringExtra("kalori"), 1.76);
            }
        });

    }

    private void hitungKalori(CollectionReference collref, String data, double nilai) {
        final SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        final Date date = new Date();
        final double kalori = Double.parseDouble(data) * (nilai);
        final DocumentReference docRef = mFirebaseFirestore.collection("Users").document(mFirebaseAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        docRef
                                .update("kaloriHarian", String.format(Locale.US,"%.2f", kalori))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                    }
                }
            }
        });


        collref.whereEqualTo("tanggal", sdf.format(date)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference docref = mFirebaseFirestore.collection("Users").document(mFirebaseAuth.getCurrentUser().getUid()).collection("Kalori").document(document.getId());
                        docref
                                .update("kaloriHarian", String.format(Locale.US,"%.2f", kalori))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent goToHome = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(goToHome);
                                        finish();
                                    }
                                });
                    }
                }
            }
        });
    }
}
