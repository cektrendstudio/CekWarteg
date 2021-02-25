package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.cektrend.cekwarteg.activity.StartActivity;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class SplashActivity extends AppCompatActivity {
    TextView tvAppName, tvNameDev;
    Animation fromBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCenter.start(getApplication(), "0af73395-7caf-4073-9e68-12105c20be50",
                Analytics.class, Crashes.class);
        initComponents();
    }

    private void initComponents() {
        tvAppName = findViewById(R.id.splashText);
        tvNameDev = findViewById(R.id.nameDev);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        tvAppName.setAnimation(fromBottom);
        tvNameDev.setAnimation(fromBottom);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}