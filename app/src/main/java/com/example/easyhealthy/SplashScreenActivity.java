package com.example.easyhealthy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        //Pengenalan Variable
        ImageView imgViewLogo = findViewById(R.id.gambarLogoSplashScreen);
        TextView txtViewSlogan = findViewById(R.id.textViewLogoSplashScreen);

        //animasi Program
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //setting animasi
        imgViewLogo.setAnimation(topAnim);
        txtViewSlogan.setAnimation(bottomAnim);

        //untuk mengaktifkan splash screen yang berlangsung 5 detik dan lempar ke activity Login
        //Pengenalan variable
        // 5 detik

        int SPLASH_SCREEN_TIME = 5000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mFirebaseAuth.getCurrentUser() != null) {

                    final CollectionReference collref = mFirebaseFirestore.collection("Users")
                            .document(mFirebaseAuth.getCurrentUser().getUid())
                            .collection("Kalori");
                    cekDataRencana(collref);

                } else {
                    Intent goToActivityLogin = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(goToActivityLogin);
                    finish();
                }
            }
        }, SPLASH_SCREEN_TIME);
    }

    private void cekDataRencana(final CollectionReference collref) {
        DocumentReference docRef = mFirebaseFirestore.collection("Users").document( Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String rencana = (String) document.get("Rencana");
                        if (rencana.isEmpty()) {
                            Intent halAwal = new Intent(getApplicationContext(), RencanaActivity.class);
                            startActivity(halAwal);
                            finish();
                        } else {
                            final SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                            final Date date = new Date();
                            collref.whereEqualTo("tanggal", sdf.format(date))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().isEmpty()) {
                                                    Intent halAwal = new Intent(getApplicationContext(), LoginActivity.class);
                                                    startActivity(halAwal);
                                                    finish();
                                                } else {
                                                    Intent halAwal = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(halAwal);
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
