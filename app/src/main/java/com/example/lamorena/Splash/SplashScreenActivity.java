package com.example.lamorena.Splash;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lamorena.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        finish();
    }
}
