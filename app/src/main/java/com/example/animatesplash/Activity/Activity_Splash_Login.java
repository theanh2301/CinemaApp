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
    private EditText userTxt, passwordTxt;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(view -> startActivity(new Intent(Activity_Splash_Login.this, IntroActivity.class)));

        initView();
    }

    private void initView(){
        userTxt = findViewById(R.id.userTxt);
        passwordTxt = findViewById(R.id.passTxt);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userTxt.getText().toString().isEmpty() || passwordTxt.getText().toString().isEmpty()) {
                    Toast.makeText(Activity_Splash_Login.this, "Please fill your username and password", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Activity_Splash_Login.this, IntroActivity.class));
                }
            }
        });
    }
}