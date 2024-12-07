package com.example.animatesplash.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.animatesplash.R;
import com.example.animatesplash.databinding.ActivityIntroBinding;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.intro_logo, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.intro_logo, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.intro_logo, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.intro_logo, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.intro_logo, ScaleTypes.FIT));

        binding.imageSlide.setImageList(slideModels, ScaleTypes.FIT);

        binding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isFirstTime", false);
                editor.apply();
                navigateToMain();
            }
        });

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
       

    }

    private void navigateToMain() {
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}