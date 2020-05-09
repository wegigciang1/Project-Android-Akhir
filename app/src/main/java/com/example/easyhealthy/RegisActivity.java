package com.example.easyhealthy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisActivity extends AppCompatActivity {

    private EditText txtnama,txtemail,txtpass,txtusia,txtjns_kelamin,txttinggi,txtberat;
    private Button btnRegis;
    private ProgressBar progressBar;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore firebaseFirestoreDb;
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        txtnama = findViewById(R.id.editTextNamaRegis);
        txtemail = findViewById(R.id.editTextEmailRegis);
        txtpass = findViewById(R.id.editTextPassRegis);
        txtusia = findViewById(R.id.editTextUsiaRegis);
        txtjns_kelamin = findViewById(R.id.editTextJnsKlmnRegis);
        txttinggi = findViewById(R.id.editTextTinggiRegis);
        txtberat = findViewById(R.id.editTextBeratRegis);
        btnRegis = findViewById(R.id.btnRegis);
        progressBar = findViewById(R.id.progressBarRegis);

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestoreDb = FirebaseFirestore.getInstance();

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nama = txtnama.getText().toString();
                String email = txtemail.getText().toString();
                String pass = txtpass.getText().toString();
                final String usia = txtusia.getText().toString();
                final String jns_kelamin = txtjns_kelamin.getText().toString();
                final String tinggi = txttinggi.getText().toString();
                final String berat = txtberat.getText().toString();
                if (email.isEmpty()) {
                    txtemail.setError("Silahkan masukkan Email");
                    return;
                }
                if (pass.isEmpty()) {
                    txtpass.setError("Silahkan masukkan Password");
                    return;
                }
                if (pass.length() < 6) {
                    txtpass.setError("Password harus lebih dari 6 karakter");
                    return;
                }
                if (nama.isEmpty()) {
                    txtnama.setError("Silahkan masukkan Email");
                    return;
                }
                if (usia.isEmpty()) {
                    txtusia.setError("Silahkan masukkan Password");
                    return;
                }
                if (jns_kelamin.isEmpty()) {
                    txtjns_kelamin.setError("Silahkan masukkan Email");
                    return;
                }
                if (tinggi.isEmpty()) {
                    txttinggi.setError("Silahkan masukkan Password");
                    return;
                }
                if (berat.isEmpty()) {
                    txtberat.setError("Silahkan masukkan Email");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserID = mFirebaseAuth.getCurrentUser().getUid();
                            Map<String,Object> user = new HashMap<>();
                            user.put("Nama",nama);
                            user.put("Usia",usia);
                            user.put("Jenis Kelamin",jns_kelamin);
                            user.put("Tinggi Badan",tinggi);
                            Map<String,Object> beratUser = new HashMap<>();
                            user.put("Berat Badan",berat);
                            firebaseFirestoreDb.collection("Users").document(UserID).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "Berhasil : "+UserID);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("TAG", e.toString());
                                        }
                                    });
                            firebaseFirestoreDb.collection("Berat User").document(UserID).set(beratUser)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "Berhasil : "+UserID);
                                            FirebaseAuth.getInstance().signOut();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("TAG", e.toString());
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}
