package com.hazloakki.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.hazloakki.MainActivity;
import com.hazloakki.R;

/*
 * Created by Jovani Arzate on 03/08/2019.
 * Splash inicial de hazloakki
 */
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent i = MainActivity.getStartIntent(this);

        new Handler().postDelayed(() -> {
            startActivity(i);
            finish();
        }, 3000);
    }
}
