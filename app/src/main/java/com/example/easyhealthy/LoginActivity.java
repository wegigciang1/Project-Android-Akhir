package com.example.easyhealthy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextView goToRegis;
    private EditText email, password;
    private ProgressBar progressBar;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser() != null){
            Intent halUtama = new Intent(getApplicationContext(), UtamaActivity.class);
            startActivity(halUtama);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        goToRegis = findViewById(R.id.textViewToRegis);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPass);
        progressBar =findViewById(R.id.progressBarLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_ = email.getText().toString();
                String pass = password.getText().toString();

                if (email_.isEmpty()) {
                    email.setError("Silahkan masukkan Email");
                    return;
                }
                if (pass.isEmpty()) {
                    password.setError("Silahkan masukkan Password");
                    return;
                }
                if (pass.length() < 6) {
                    password.setError("Password harus lebih dari 6 karakter");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mFirebaseAuth.signInWithEmailAndPassword(email_, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            email.getText().clear();
                            password.getText().clear();
                            startActivity(new Intent(getApplicationContext(), UtamaActivity.class));
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        goToRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent halRegis = new Intent(LoginActivity.this, RegisActivity.class);
                startActivity(halRegis);
            }
        });

    }
}
