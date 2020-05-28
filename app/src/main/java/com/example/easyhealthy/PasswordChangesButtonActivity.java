package com.example.easyhealthy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordChangesButtonActivity extends AppCompatActivity {

    Button buttonChange;
    private EditText oldPass, newPass, reTypePass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass_button);

        oldPass = findViewById(R.id.editText_oldpass);
        newPass = findViewById(R.id.editText_newpass);
        reTypePass = findViewById(R.id.editText_retype_pass);
        buttonChange = findViewById(R.id.btnChangePass);

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String passwordLama, passwordBaru, passwordRetype;
                passwordLama = oldPass.getText().toString();
                passwordBaru = newPass.getText().toString();
                passwordRetype = reTypePass.getText().toString();

                if (passwordLama.equals("")) {
                    Toast.makeText(PasswordChangesButtonActivity.this, "Password is Required", Toast.LENGTH_SHORT).show();
                } else if (passwordBaru.equals("")) {
                    Toast.makeText(PasswordChangesButtonActivity.this, "Password is Required", Toast.LENGTH_SHORT).show();
                } else if (passwordBaru.length() < 6 && passwordLama.length() < 6) {
                    Toast.makeText(PasswordChangesButtonActivity.this, "Password to short", Toast.LENGTH_SHORT).show();
                } else if (!passwordRetype.equals(passwordBaru)) {
                    Toast.makeText(PasswordChangesButtonActivity.this, "Password must be same with your new pass", Toast.LENGTH_SHORT).show();
                } else {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), passwordLama);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(passwordBaru).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(PasswordChangesButtonActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(PasswordChangesButtonActivity.this, "Password Not Changed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    });

                }
            }
        });
    }

}
