package com.example.easyhealthy;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password, emailForgetPass;
    private ProgressBar progressBar;

    private FirebaseAuth mFirebaseAuth;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() != null) {
            Intent halUtama = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(halUtama);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView goToRegis = findViewById(R.id.textViewToRegis);
        TextView goToForgetPass = findViewById(R.id.textViewForgetPass);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPass);
        progressBar = findViewById(R.id.progressBarLogin);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.editTextPass, ".{6,}", R.string.invalid_pass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    String email_ = email.getText().toString();
                    String pass = password.getText().toString();

                    progressBar.setVisibility(View.VISIBLE);

                    mFirebaseAuth.signInWithEmailAndPassword(email_, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                email.getText().clear();
                                password.getText().clear();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
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
                final View dialogForgetPass = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
                emailForgetPass = dialogForgetPass.findViewById(R.id.editTextEmailForgetPass);

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
}
