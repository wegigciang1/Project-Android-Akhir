package com.example.easyhealthy.ui.tipsManfaat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

                final DocumentReference collref = mFirebaseFirestore.collection("Kalori").document( Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid()).collection(sdf.format(date)).document(mFirebaseAuth.getCurrentUser().getUid());
                collref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String kalori = String.valueOf(document.get("kaloriHarian"));
                                String burned = String.valueOf(document.get("burned"));
                                int total = 0;
                                int totalKalori = 0;
                                total = Integer.parseInt(tempKalori) - Integer.parseInt(burned);
                                totalKalori = total + Integer.parseInt(kalori);
                                updateDataBurned(collref, total, totalKalori);
                            }
                        }
                    }
                });

            }
        } );
    }
    private void updateDataBurned(DocumentReference collref, final int total, final int totalKalori) {
        collref
                .update(
                        "burned", String.valueOf(total),
                        "kaloriHarian", String.valueOf(totalKalori)
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent goToMainActivity = new Intent(Workout.this, MainActivity.class);
//                        goToMainActivity.putExtra("burned", String.valueOf(total));
//                        goToMainActivity.putExtra("kalori", String.valueOf(totalKalori));
//                        goToMainActivity.putExtra("burned", "0");
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
