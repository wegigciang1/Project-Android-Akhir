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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
                final SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                final Date date = new Date();
                progressBarDetailItem.setVisibility(View.VISIBLE);

                if (data.equals( "workout" )){
                    Intent goToWorkout = new Intent( DetailItemActivity.this, Workout.class );
                    goToWorkout.putExtra("kalori", tempKalori);
                    startActivity(goToWorkout);
                }
                else {

                    final CollectionReference collref2 = mFirebaseFirestore.collection("Users").document(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid()).collection("Kalori");
                    collref2.whereEqualTo("tanggal", sdf.format(date)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String kalori = String.valueOf( document.get( "kaloriHarian" ) );
                                    String eaten = String.valueOf( document.get( "eaten" ) );
                                    double total = 0;
                                    double totalKalori = 0;
                                    total = Double.parseDouble(tempKalori) + Double.parseDouble(eaten);
                                    totalKalori = Double.parseDouble(kalori) - Double.parseDouble(tempKalori);
                                    final DocumentReference collref = mFirebaseFirestore.collection("Users").document(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid()).collection("Kalori").document(document.getId());
                                    updateDataEarned( collref, total, totalKalori );
                                }
                            }
                        }
                    });

                }

            }
        });

    }

    private void updateDataEarned(DocumentReference collref, final double total, final double totalKalori) {
        collref
                .update(
                        "eaten", String.format("%.0f", total),
                        "kaloriHarian", String.format("%.2f", totalKalori)
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent goToMainActivity = new Intent(DetailItemActivity.this, MainActivity.class);
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
