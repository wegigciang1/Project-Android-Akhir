package com.example.easyhealthy.ui.tipsManfaat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

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

public class DetailItemActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        //intent
        final Intent intent = getIntent();
        final String tempKalori = intent.getStringExtra("kalori");
        final String data = intent.getStringExtra( "data" );

        //init
        TextView textViewdetail = findViewById(R.id.tv_detail);
        TextView textViewjudul = findViewById(R.id.tv_judul);
        Button start = findViewById(R.id.startWorkout);
        VideoView videoView = findViewById(R.id.ttampilkanVideo);
        final ProgressBar progressBarDetailItem = findViewById(R.id.progressBarDetailItem);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        Uri uri = Uri.parse(intent.getStringExtra("video"));

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.start();

        textViewjudul.setText(intent.getStringExtra("judul"));
        textViewdetail.setText(intent.getStringExtra("detail"));

        if (data.equals( "workout" )){
            start.setText( "MULAI WORKOUT" );
        }
        else {
            start.setText( "TAMBAH MAKANAN" );
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
                final Date date = new Date();
                progressBarDetailItem.setVisibility(View.VISIBLE);

                if (data.equals( "workout" )){
                    Intent goToWorkout = new Intent( DetailItemActivity.this, Workout.class );
                    goToWorkout.putExtra("kalori", tempKalori);
                    startActivity(goToWorkout);
                }
                else {

                    final DocumentReference collref = mFirebaseFirestore.collection( "Kalori" ).document( Objects.requireNonNull( mFirebaseAuth.getCurrentUser() ).getUid() ).collection( sdf.format( date ) ).document( mFirebaseAuth.getCurrentUser().getUid() );
                    collref.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String kalori = String.valueOf( document.get( "kaloriHarian" ) );
                                    String eaten = String.valueOf( document.get( "eaten" ) );
                                    int total = 0;
                                    int totalKalori = 0;
                                    total = Integer.parseInt( tempKalori ) + Integer.parseInt( eaten );
                                    totalKalori = total + Integer.parseInt( kalori );
                                    updateDataEarned( collref, total, totalKalori );
                                }
                            }
                        }
                    } );
                }

            }
        });

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
                        Intent goToMainActivity = new Intent(DetailItemActivity.this, MainActivity.class);
                        goToMainActivity.putExtra("eaten", String.valueOf(total));
                        goToMainActivity.putExtra("kalori", String.valueOf(totalKalori));
                        goToMainActivity.putExtra("burned", "0");
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

    private void updateDataEarned(DocumentReference collref, final int total, final int totalKalori) {
        collref
                .update(
                        "eaten", String.valueOf(total),
                        "kaloriHarian", String.valueOf(totalKalori)
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent goToMainActivity = new Intent(DetailItemActivity.this, MainActivity.class);
                        goToMainActivity.putExtra("eaten", String.valueOf(total));
                        goToMainActivity.putExtra("kalori", String.valueOf(totalKalori));
                        goToMainActivity.putExtra("burned", "0");
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
