package com.example.easyhealthy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RencanaActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collref = mFirebaseFirestore.collection("Users").document(mFirebaseAuth.getCurrentUser().getUid()).collection("Berat Badan");


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
                    DocumentReference washingtonRef = mFirebaseFirestore.collection("Users").document(mFirebaseAuth.getCurrentUser().getUid());
                    washingtonRef
                            .update(
                                    "Rencana", planPilihan.getText().toString(),
                                    "Target", targetBerat.getText().toString()
                            )
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    tambahKalori(); //method panggil
                                    Intent goToHome = new Intent(getApplicationContext(), TahapRencanaActivity.class);
                                    startActivity(goToHome);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                   /* DocumentReference washington = mFirebaseFirestore.collection("Users").document(mFirebaseAuth.getCurrentUser().getUid());
                    washington
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String tinggi="", target="", jenisKelamin = "", usia="";
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            tinggi= (String) document.get("Tinggi Badan");
                                            target= (String) document.get("Target");
                                            jenisKelamin= (String) document.get("Jenis Kelamin");
                                            usia = (String) document.get("Usia");
                                        }
                                        if(jenisKelamin.equals("Laki-Laki")){
                                            Double kalori= (66.5 + (13.8 * Double.parseDouble(target)) + ((5 * Double.parseDouble(tinggi)) / (6.8 * Double.parseDouble(usia))));

                                        }else{
                                            Double kalori= (655.1 + (9.6 * Double.parseDouble(target)) + ((1.9 * Double.parseDouble(tinggi)) / (4.7 * Double.parseDouble(usia))));
                                        }
                                    } else {

                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            }); */

                }
            }
        });
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (planPilihan.getText().toString().isEmpty()) {
                    Toast.makeText(RencanaActivity.this, "Pilih Rencana Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    if (targetBerat.getText().toString().isEmpty()) {
                        Toast.makeText(RencanaActivity.this, "Isi Target Berat", Toast.LENGTH_SHORT).show();
                    } else {
                        final int target = Integer.parseInt(targetBerat.getText().toString());
                        collref.get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            String tampung = "";
                                            int total = 0;
                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                tampung = (String) document.get("berat");
                                            }
                                            if (planPilihan.getText().toString().equalsIgnoreCase("Menurunkan Berat Badan")) {
                                                total = (Integer.parseInt(tampung) - target) * 2;
                                                if (total <= 0) {
                                                    Toast.makeText(RencanaActivity.this, "Input Target Salah", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    autoLama.setText(total + " Minggu");
                                                    btnNext.setVisibility(View.VISIBLE);

                                                }
                                            }

                                            if (planPilihan.getText().toString().equalsIgnoreCase("Menaikkan Berat Badan")) {
                                                total = (target - Integer.parseInt(tampung)) * 2;
                                                if (total <= 0) {
                                                    Toast.makeText(RencanaActivity.this, "Input Target Salah", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    autoLama.setText(total + " Minggu");
                                                    btnNext.setVisibility(View.VISIBLE);
                                                }
                                            }


                                        }
                                    }
                                });
                    }
                }



            }
        });

    }

    private void tambahKalori() {
        final SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        final Date date = new Date();

        final DocumentReference collref = mFirebaseFirestore.collection("Users")
                .document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("Kalori")
                .document();
        Map<String, Object> dataAwal = new HashMap<>();
        dataAwal.put("burned", "0");
        dataAwal.put("eaten", "0");
        dataAwal.put("kaloriHarian", "0");
        dataAwal.put("tanggal", sdf.format(date));
        collref.set(dataAwal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Kalo berhasil
                Toast.makeText(RencanaActivity.this, "Kalori Berhasil", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
