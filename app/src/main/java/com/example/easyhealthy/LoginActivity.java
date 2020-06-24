package com.example.easyhealthy;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText email = findViewById(R.id.editTextEmail);
        final EditText password = findViewById(R.id.editTextPass);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.editTextPass, ".{6,}", R.string.invalid_pass);

        Button btnLogin = findViewById(R.id.btnLogin);
        final TextView goToRegis = findViewById(R.id.textViewToRegis);
        TextView goToForgetPass = findViewById(R.id.textViewForgetPass);

        progressBar = findViewById(R.id.progressBarLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    final SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                    final Date date = new Date();
                    progressBar.setVisibility(View.VISIBLE);

                    mFirebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                final DocumentReference collref = mFirebaseFirestore.collection("Users")
                                        .document(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid())
                                        .collection("Kalori")
                                        .document();
                                cekDataRencana(collref, sdf.format(date));

                            } else {
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (FirebaseAuthInvalidUserException e) {
                                    Toast.makeText(getApplicationContext(), "User belum terdaftar", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseNetworkException e) {
                                    Toast.makeText(getApplicationContext(), "Belum Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(getApplicationContext(), "Username or Password is Wrong", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

        goToRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegis = new Intent(LoginActivity.this, RegisActivity.class);
                startActivity(goToRegis);
            }
        });

        goToForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                @SuppressLint("InflateParams") final View dialogForgetPass = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
                final EditText emailForgetPass = dialogForgetPass.findViewById(R.id.editTextEmailForgetPass);

                builder.setView(dialogForgetPass);
                builder.setTitle("Forget Password");
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email_ = emailForgetPass.getText().toString().trim();
                        mFirebaseAuth.sendPasswordResetEmail(email_).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(LoginActivity.this, "Please cek your email", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog mAlertDialog = builder.create();
                mAlertDialog.show();
            }

        });

    }

    private void cekDataRencana(final DocumentReference collref, final String tanggal) {
        DocumentReference docRef = mFirebaseFirestore.collection("Users").document(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        String rencana = (String) document.get("Rencana");
                        assert rencana != null;
                        if (rencana.isEmpty()) {
                            Intent halAwal = new Intent(getApplicationContext(), RencanaActivity.class);
                            startActivity(halAwal);
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        } else {
                            final CollectionReference collref2 = mFirebaseFirestore.collection("Users")
                                    .document(mFirebaseAuth.getCurrentUser().getUid())
                                    .collection("Kalori");

                            collref2.whereEqualTo("tanggal", tanggal).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                                            DocumentReference docRef = mFirebaseFirestore.collection("Users").document(mFirebaseAuth.getCurrentUser().getUid());
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        assert document != null;
                                                        if (document.exists()) {
                                                            Map<String, Object> dataAwal = new HashMap<>();
                                                            dataAwal.put("burned", "0");
                                                            dataAwal.put("eaten", "0");
                                                            dataAwal.put("kaloriHarian", document.get("kaloriHarian"));
                                                            dataAwal.put("tanggal", tanggal);
                                                            collref.set(dataAwal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Intent halUtama = new Intent(getApplicationContext(), MainActivity.class);
                                                                    startActivity(halUtama);
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    finish();
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });


                                        } else {
                                            Intent halUtama = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(halUtama);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            finish();
                                        }

                                    }
                                }
                            });

                        }
                    }
                }
            }
        });
    }

}
