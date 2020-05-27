package com.example.easyhealthy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileButtonActivity extends AppCompatActivity {


    TextView nama, usia, jns_kelamin, tinggi;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button update;
    DocumentReference documentReference;
    String current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_button);

        Intent intent = getIntent();
        getSupportActionBar().setTitle("Profile");

        nama = findViewById(R.id.editText_namaProfile);
        usia = findViewById(R.id.editText_usiaProfile);
        jns_kelamin = findViewById(R.id.editText_jenisKelamin);
        tinggi = findViewById(R.id.editText_tbProfile);
        update = findViewById(R.id.btnUpdateProfile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        //button update set onClickListener
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();
                getData();

                Intent halFragmentHome = new Intent(ProfileButtonActivity.this, MainActivity.class);
                startActivity(halFragmentHome);

            }
        });

        //Fetching data from firestore into editText
        documentReference = fStore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                nama.setText(documentSnapshot.getString("Nama"));
                usia.setText(documentSnapshot.getString("Usia"));
                jns_kelamin.setText(documentSnapshot.getString("Jenis Kelamin"));
                tinggi.setText(documentSnapshot.getString("Tinggi Badan"));

                //getData();

            }
        });

    }

    //method to update data in firestore
    private void UpdateData() {
        String username = nama.getText().toString();
        String usiapengguna = usia.getText().toString();
        String jeniskelamin = jns_kelamin.getText().toString();
        String tinggibadan = tinggi.getText().toString();

        documentReference.update("Nama", username);
        documentReference.update("Usia", usiapengguna);
        documentReference.update("Jenis Kelamin", jeniskelamin);
        documentReference.update("Tinggi Badan", tinggibadan);
    }

    private void getData() {
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final String current = firebaseUser.getUid();

        fStore.collection("Users")
                .whereEqualTo("uid", current)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                nama.setText((CharSequence) document.get("Nama"));
                                usia.setText((CharSequence) document.get("Usia"));
                                jns_kelamin.setText((CharSequence) document.get("Jenis Kelamin"));
                                tinggi.setText((CharSequence) document.get("Tinggi Badan"));
                            }
                        }
                    }
                });


    }
}
