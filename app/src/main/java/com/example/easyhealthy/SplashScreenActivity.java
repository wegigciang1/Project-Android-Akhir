package com.example.easyhealthy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {


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
                Intent goToActivityLogin = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(goToActivityLogin);
                finish();
            }
        }, SPLASH_SCREEN_TIME);
    }
}
