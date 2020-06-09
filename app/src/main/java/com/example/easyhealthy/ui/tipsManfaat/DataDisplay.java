package com.example.easyhealthy.ui.tipsManfaat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyhealthy.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class DataDisplay extends AppCompatActivity {

    TextView textViewjudul;
    TextView textViewdetail;
    String tempJudul, tempVideo, tempDetail, tempLabel;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        //intent
        final Intent intent = getIntent();
        tempJudul = intent.getStringExtra("judul");
        tempDetail = intent.getStringExtra("detail");
        tempVideo = intent.getStringExtra("video");
        tempLabel = intent.getStringExtra("label");

        //init
        textViewjudul = findViewById(R.id.tv_judul);
        textViewdetail = findViewById(R.id.tv_detail);
        start = findViewById(R.id.startWorkout);


        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = tempVideo;
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
        if (!tempJudul.isEmpty() && !tempDetail.isEmpty()) {
            textViewjudul.setText(tempJudul);
            textViewdetail.setText(tempDetail);

        }

        if (tempLabel.equals("1")) {
            start.setText("MULAI WORKOUT");

        } else if (tempLabel.equals("0") || tempLabel.equals("2")) {
            start.setText("TAMBAH MAKANAN");
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempLabel.equals("1")) {
                    // intent ke class workout

                } else if (tempLabel.equals("0") || tempLabel.equals("2")) {
                    //(makanan)intent ke main activity tambah kalori ke dashboard eaten
                }

            }
        });
    }
}
