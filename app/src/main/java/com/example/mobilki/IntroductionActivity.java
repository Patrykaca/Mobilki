package com.example.mobilki;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;


public class IntroductionActivity extends AppIntro {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.title_1),
                getResources().getString(R.string.description_1),
                R.drawable.img1, ContextCompat.getColor(getApplicationContext(), R.color.appIntro1)
        ));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.title_2),
                getResources().getString(R.string.description_2),
                R.drawable.img2, ContextCompat.getColor(getApplicationContext(), R.color.appIntro2)
        ));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.title_3),
                getResources().getString(R.string.description_3), R.drawable.img3,
                ContextCompat.getColor(getApplicationContext(), R.color.appIntro3)
        ));
        setTransformer(AppIntroPageTransformerType.Flow.INSTANCE);
        setColorTransitionsEnabled(true);
        setProgressIndicator();
        setImmersiveMode();

        sharedPreferences = getApplicationContext().getSharedPreferences("introPreferences"
                , Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences != null) {
            boolean checkShared = sharedPreferences.getBoolean("checkStated", false);

            if(checkShared) {
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                finish();
            }
        }

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        editor.putBoolean("checkStated", true).commit();
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        editor.putBoolean("checkStated", true).commit();
        finish();
    }
}