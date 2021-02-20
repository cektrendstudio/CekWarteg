package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtUsername, edtPassword;
    Button btnLogin, btnRegister;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    Boolean is_approve = false;
    Boolean session = false;
    String userName;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    public static final String session_username = "session_username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        sharedPreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        clickListener();
        session = sharedPreferences.getBoolean(session_status, false);
        if (session) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
        AndroidNetworking.initialize(getApplicationContext());
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
            String user = edtUsername.getText().toString().trim();
            String passwd = edtPassword.getText().toString().trim();
            if (!user.isEmpty() || !passwd.isEmpty()) {
                login(user, passwd);
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
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject("user");
                                userName = data.getString("username");
                                is_approve = data.getBoolean("is_approve");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (is_approve) {
                            Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(session_status, true);
                            editor.putString(session_username, userName);
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal Login", Toast.LENGTH_SHORT).show();
                            edtPassword.setText("");
                            edtUsername.setText("");
                            hideDialog();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Gagal terhubung, cobalah periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "bagian ieu :", anError);
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