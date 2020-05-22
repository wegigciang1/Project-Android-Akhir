package com.example.easyhealthy;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisActivity extends AppCompatActivity {

    private EditText txtnama,txtemail,txtpass,txtusia,txttinggi,txtberat,txtjns_kelamin;
    private ProgressBar progressBar;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore firebaseFirestoreDb;
    private AwesomeValidation awesomeValidation;
    private String UserID;
    private String[] listItems;

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
        Button btnRegis = findViewById(R.id.btnRegis);
        Button btnPilih = findViewById(R.id.btnPilihKelaminRegis);

        progressBar = findViewById(R.id.progressBarRegis);

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestoreDb = FirebaseFirestore.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.editTextNamaRegis, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.editTextEmailRegis,Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        awesomeValidation.addValidation(this,R.id.editTextPassRegis, ".{6,}",R.string.invalid_pass);
        awesomeValidation.addValidation(this,R.id.editTextUsiaRegis, Range.closed(17, 50),R.string.invalid_age);
        awesomeValidation.addValidation(this,R.id.editTextTinggiRegis, Range.closed(140, 190),R.string.invalid_height);
        awesomeValidation.addValidation(this,R.id.editTextBeratRegis, Range.closed(30, 70),R.string.invalid_weight);

        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems = new String[]{"Laki-Laki","Perempuan"};
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisActivity.this);
                builder.setTitle("Pilih Jenis Kelamin");
                builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtjns_kelamin.setText(listItems[which]);
                        txtjns_kelamin.setError(null);
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog mAlertDialog = builder.create();
                mAlertDialog.show();
            }
        });

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cekJnsKlmin = validateJnsKlmin(txtjns_kelamin);
                if(awesomeValidation.validate() && cekJnsKlmin){
                    final String nama = txtnama.getText().toString();
                    String email = txtemail.getText().toString();
                    String pass = txtpass.getText().toString();
                    final String usia = txtusia.getText().toString();
                    final String jns_kelamin = txtjns_kelamin.getText().toString();
                    final String tinggi = txttinggi.getText().toString();
                    final String berat = txtberat.getText().toString();

                    progressBar.setVisibility(View.VISIBLE);

                    mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserID = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();
                                Map<String,Object> user = new HashMap<>();
                                user.put("Nama",nama);
                                user.put("Usia",usia);
                                user.put("Jenis Kelamin",jns_kelamin);
                                user.put("Tinggi Badan",tinggi);
                                Map<String,Object> beratUser = new HashMap<>();
                                beratUser.put("Berat Badan",berat);
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
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                }catch (FirebaseAuthUserCollisionException e){
                                    Toast.makeText(getApplicationContext(),"Email is Already Exist", Toast.LENGTH_SHORT).show();
                                }catch (FirebaseNetworkException e){
                                    Toast.makeText(getApplicationContext(),"Your Network is Disconnect", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }

            }
        });
    }
    private boolean validateJnsKlmin(EditText txtjns_kelamin){
        boolean hasilcek = true;
        String jnsKlmin = txtjns_kelamin.getText().toString();
        if(jnsKlmin.isEmpty()){
            txtjns_kelamin.setError("Masukkan Jenis Kelamin");
            hasilcek = false;
        }
        return hasilcek;
    }
}
