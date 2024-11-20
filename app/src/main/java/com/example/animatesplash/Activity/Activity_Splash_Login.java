package com.example.animatesplash.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.animatesplash.R;

public class Activity_Splash_Login extends AppCompatActivity {

    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToIntro();
            }
        });



    }

    private void navigateToIntro() {
        Intent intent = new Intent(Activity_Splash_Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}