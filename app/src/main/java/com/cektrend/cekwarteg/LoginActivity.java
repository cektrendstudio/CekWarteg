package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtUsername, edtPassword;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        AndroidNetworking.initialize(getApplicationContext());
        clickListener();
    }

    private void initComponents() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void clickListener() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if (!username.isEmpty() || !password.isEmpty()) {
                login(username, password);
            } else {
                edtUsername.setError("Please insert your username");
                edtPassword.setError("Please insert your password");
            }
        } else if (id == R.id.btn_register) {
            startActivity(new Intent(LoginActivity.this, RegisterAdmin.class));
            finish();
        }
    }

    private void login(String username, String password) {
        AndroidNetworking.post("http://103.146.203.97/api/...")
                .addBodyParameter("username", username)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}