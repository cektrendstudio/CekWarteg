package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MY_SHARED_PREFERENCES;
import static com.cektrend.cekwarteg.utils.ConstantUtil.SESSION_STATUS;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin, btnStarted;
    SharedPreferences sharedPreferences;
    Boolean session = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_start);
        initComponents();
        clickListener();
    }

    private void initComponents() {
        btnLogin = findViewById(R.id.btn_login);
        btnStarted = findViewById(R.id.btn_started);
        sharedPreferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(SESSION_STATUS, false);
        if (session) {
            Intent intent = new Intent(StartActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void clickListener() {
        btnLogin.setOnClickListener(this);
        btnStarted.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            Intent login = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        } else if (id == R.id.btn_started) {
            Intent start = new Intent(StartActivity.this, Welcome.class);
            startActivity(start);
            finish();
        }
    }
}