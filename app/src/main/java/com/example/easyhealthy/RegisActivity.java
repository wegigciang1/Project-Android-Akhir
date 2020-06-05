package com.example.easyhealthy;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RegisActivity extends AppCompatActivity {

    private EditText txtnama, txtemail, txtpass, txtusia, txttinggi, txtberat;
    private ProgressBar progressBar;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore firebaseFirestoreDb;
    private AwesomeValidation awesomeValidation;
    private String UserID;
    private RadioGroup groupJnsKlmin;
    private RadioButton radioJnsKlmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        txtnama = findViewById(R.id.editTextNamaRegis);
        txtemail = findViewById(R.id.editTextEmailRegis);
        txtpass = findViewById(R.id.editTextPassRegis);
        txtusia = findViewById(R.id.editTextUsiaRegis);
        groupJnsKlmin = findViewById(R.id.radioGroup);
        txttinggi = findViewById(R.id.editTextTinggiRegis);
        txtberat = findViewById(R.id.editTextBeratRegis);
        Button btnRegis = findViewById(R.id.btnRegis);

        progressBar = findViewById(R.id.progressBarRegis);

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestoreDb = FirebaseFirestore.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.editTextNamaRegis, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.editTextEmailRegis, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.editTextPassRegis, ".{6,}", R.string.invalid_pass);
        awesomeValidation.addValidation(this, R.id.editTextUsiaRegis, Range.closed(17, 50), R.string.invalid_age);
        awesomeValidation.addValidation(this, R.id.editTextTinggiRegis, Range.closed(140, 190), R.string.invalid_height);
        awesomeValidation.addValidation(this, R.id.editTextBeratRegis, Range.closed(30, 70), R.string.invalid_weight);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = groupJnsKlmin.getCheckedRadioButtonId();
                radioJnsKlmin = findViewById(radioId);
                if (radioId == -1) {
                    Toast.makeText(RegisActivity.this, String.valueOf(radioId), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (awesomeValidation.validate()) {
                    String email = txtemail.getText().toString();
                    String pass = txtpass.getText().toString();
                    final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
                    final Date date = new Date();

                    progressBar.setVisibility(View.VISIBLE);

                    mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserID = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();

                                Map<String, Object> user = new HashMap<>();
                                user.put("Nama", txtnama.getText().toString());
                                user.put("Usia", txtusia.getText().toString());
                                user.put("Jenis Kelamin", radioJnsKlmin.getText().toString());
                                user.put("Tinggi Badan", txttinggi.getText().toString());
                                user.put("FotoKey", "");
                                user.put("Rencana", "");
                                user.put("Target", "");

                                Map<String, Object> beratUser = new HashMap<>();
                                beratUser.put("berat", txtberat.getText().toString());
                                beratUser.put("tanggal", sdf.format(date));
                                beratUser.put("id", UserID);

                                firebaseFirestoreDb.collection("Users").document(UserID).set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "Berhasil : " + UserID);

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("TAG", e.toString());
                                            }
                                        });
                                firebaseFirestoreDb.collection("Berat Badan").document().set(beratUser)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "Berhasil : " + UserID);
                                                progressBar.setVisibility(View.GONE);
                                                mFirebaseAuth.signOut();
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
                                } catch (FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(getApplicationContext(), "Email is Already Exist", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseNetworkException e) {
                                    Toast.makeText(getApplicationContext(), "Your Network is Disconnect", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
                }

            }
        });
    }

}
