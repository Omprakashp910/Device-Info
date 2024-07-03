package com.example1.cpu_x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    private int dotCount = 0;
    TextView textView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textView = findViewById(R.id.textView);
        handler = new Handler(Looper.getMainLooper());
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // Set the status bar color to white
        window.setStatusBarColor(getResources().getColor(R.color.blue));

        // Set system UI elements to dark color
        View decorView = window.getDecorView();
        int flags = decorView.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(flags);

        startDotAnimation();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent iHome = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(iHome);
                finish();
            }
        },2000);

    }
    private void startDotAnimation() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLoadingText();
                startDotAnimation();
            }
        }, 500);
    }

    private void updateLoadingText() {
        dotCount = (dotCount % 4) + 1; // Cycle through 1 to 4 dots
        StringBuilder loadingText = new StringBuilder("Loading");
        for (int i = 0; i < dotCount; i++) {
            loadingText.append(".");
        }
        textView.setText(loadingText.toString());
    }
}