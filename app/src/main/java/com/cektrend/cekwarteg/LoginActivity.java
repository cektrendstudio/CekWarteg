package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtUsername, edtPassword;
    Button btnLogin, btnRegister;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    Boolean iSuccess = false;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    public static final String session_username = "session_username";
    public static final String session_password = "session_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        sharedPreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
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
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi...");
        showDialog();
        AndroidNetworking.post("http://103.146.203.97/api/auth/login")
                .addBodyParameter("username", username)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hideDialog();
                        String username = "", password = "", messages = "";
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject data = response.getJSONObject(i);
                                username = data.getString("username");
                                password = data.getString("password");
                                messages = data.getString("messages");
                                iSuccess = data.getBoolean("iSuccess");
                            }

                            if (iSuccess == true) {
                                Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(session_status, true);
                                editor.putString(session_username, username);
                                editor.putString(session_password, password);
                                editor.apply();
                                iSuccess = sharedPreferences.getBoolean(session_status, false);
                                if (iSuccess) {
                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_SHORT).show();
                                edtUsername.setText("");
                                edtPassword.setText("");
                                hideDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Gagal terhubung, cobalah periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                });
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}