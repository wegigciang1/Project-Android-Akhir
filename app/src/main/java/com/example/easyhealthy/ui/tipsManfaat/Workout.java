package com.example.easyhealthy.ui.tipsManfaat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyhealthy.MainActivity;
import com.example.easyhealthy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import ticker.views.com.ticker.widgets.circular.timer.callbacks.CircularViewCallback;
import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;

public class Workout extends AppCompatActivity {

    Button pause;
    Button finish,resume,start;
    CircularView circularViewWithTimer;
    ProgressBar progressBarKirimWorkout;
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_workout );

        //intent
        final Intent intent = getIntent();
        final String tempKalori = intent.getStringExtra("kalori");
        start =findViewById( R.id.startWorkout );
        pause =findViewById( R.id.pauseWorkout );
        finish=findViewById( R.id.finishWorkout );
        resume=findViewById( R.id.resumeWorkout );
        progressBarKirimWorkout.findViewById(R.id.progressBarKirimWorkout);
        finish.setVisibility( View.INVISIBLE );
        pause.setVisibility( View.INVISIBLE );
        resume.setVisibility( View.INVISIBLE );


        circularViewWithTimer = findViewById(R.id.progressBarWorkout);

        CircularView.OptionsBuilder builderWithTimer =
                new CircularView.OptionsBuilder()
                        .shouldDisplayText(true)
                        .setCounterInSeconds(30)
                        .setCircularViewCallback(new CircularViewCallback() {
                            @Override
                            public void onTimerFinish() {

                                // Will be called if times up of countdown timer
                                Toast.makeText(Workout.this, "KERJA KERAS MU TAKKAN SIA SIA", Toast.LENGTH_SHORT).show();
                                finish.setVisibility( View.VISIBLE );
                            }

                            @Override
                            public void onTimerCancelled() {

                                // Will be called if stopTimer is called
                                Toast.makeText(Workout.this, "CircularCallback: Timer Cancelled ", Toast.LENGTH_SHORT).show();
                            }
                        });

        circularViewWithTimer.setOptions(builderWithTimer);

        start.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularViewWithTimer.startTimer();
                pause.setVisibility( View.VISIBLE );
            }
        } );
        pause.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularViewWithTimer.pauseTimer();
                resume.setVisibility( View.VISIBLE );
            }
        } );
        resume.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularViewWithTimer.resumeTimer();
            }
        } );

        finish.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent update
                final SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                final Date date = new Date();

                progressBarKirimWorkout.setVisibility(View.VISIBLE);

                final CollectionReference collref2 = mFirebaseFirestore.collection("Users").document(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid()).collection("Kalori");
                collref2.whereEqualTo("tanggal", sdf.format(date)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String kalori = String.valueOf(document.get("kaloriHarian"));
                                String burned = String.valueOf(document.get("burned"));
                                double total = 0;
                                double totalKalori = 0;
                                total = Double.parseDouble(tempKalori) + Double.parseDouble(burned);
                                totalKalori = Double.parseDouble(kalori) + Double.parseDouble(tempKalori);
                                final DocumentReference collref = mFirebaseFirestore.collection("Users").document(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid()).collection("Kalori").document(document.getId());
                                updateDataBurned(collref, total, totalKalori);
                                progressBarKirimWorkout.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });

            }
        } );
    }

    private void updateDataBurned(DocumentReference collref, final double total, final double totalKalori) {
        collref
                .update(
                        "burned", String.format("%.0f", total),
                        "kaloriHarian", String.format("%.2f", totalKalori)
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent goToMainActivity = new Intent(Workout.this, MainActivity.class);
                        startActivity(goToMainActivity);
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
