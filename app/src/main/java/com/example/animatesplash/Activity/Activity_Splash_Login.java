package com.example.animatesplash.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.animatesplash.R;

public class Activity_Splash_Login extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        runnable = new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                boolean isFirstTime = preferences.getBoolean("isFirstTime", true);

                if (isFirstTime) {
                    startActivity(new Intent(Activity_Splash_Login.this, IntroActivity.class));
                } else {
                    startActivity(new Intent(Activity_Splash_Login.this, MainActivity.class));
                }

                finish();
            }
        };
        handler.postDelayed(runnable, 6000);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

}