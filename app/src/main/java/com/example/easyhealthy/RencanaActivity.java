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
import com.google.firebase.firestore.DocumentSnapshot;
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
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
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
        final DocumentReference docRef = mFirebaseFirestore.collection("Users").document(mFirebaseAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String tinggi = (String) document.get("Tinggi Badan");
                        String target = (String) document.get("Target");
                        String jenisKelamin = (String) document.get("Jenis Kelamin");
                        String usia = (String) document.get("Usia");

                        final SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                        final Date date = new Date();

                        Map<String, Object> dataAwal = new HashMap<>();
                        dataAwal.put("burned", "0");
                        dataAwal.put("eaten", "0");
                        dataAwal.put("tanggal", sdf.format(date));

                        final double kalori;

                        if (jenisKelamin.equals("Laki-Laki")) {

                            kalori = (10 * Double.parseDouble(target)) + (6.25 * Double.parseDouble(tinggi)) - (5 * Double.parseDouble(usia)) + 5;
                        } else {

                            kalori = (10 * Double.parseDouble(target)) + (6.25 * Double.parseDouble(tinggi)) - (5 * Double.parseDouble(usia)) - 161;
                        }
                        final String hasilFormat = String.format(Locale.US,"%.2f", kalori);
                        dataAwal.put("kaloriHarian", hasilFormat);
                        final DocumentReference collref = mFirebaseFirestore.collection("Users")
                                .document(mFirebaseAuth.getCurrentUser().getUid())
                                .collection("Kalori")
                                .document();
                        collref.set(dataAwal).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Kalo berhasil
                                Toast.makeText(RencanaActivity.this, "Kalori Berhasil", Toast.LENGTH_SHORT).show();

                            }
                        });

                        docRef
                                .update("kaloriHarian", hasilFormat)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent goToHome = new Intent(getApplicationContext(), TahapRencanaActivity.class);
                                        goToHome.putExtra("kalori", hasilFormat);
                                        startActivity(goToHome);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });


                    }
                }
            }
        });
    }
}
