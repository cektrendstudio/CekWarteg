package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText edtFullName, edtUsername, edtEmail, edtPassword;
    Button btnRegister;
    String messageResponse, failedResponse;
    Boolean isSuccess;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
        clickListener();
    }

    private void initComponents() {
        edtFullName = findViewById(R.id.edt_fullname);
        edtUsername = findViewById(R.id.edt_username);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void clickListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = edtFullName.getText().toString().trim();
                String username = edtUsername.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                int id = view.getId();
                if (id == R.id.btn_register) {
                    if (!fullName.isEmpty() || !username.isEmpty() || !email.isEmpty() || !password.isEmpty()) {
                        registerData(fullName, username, email, password);
                    } else {
                        edtFullName.setError("Jangan kosongkan kolom ini!");
                        edtUsername.setError("Jangan kosongkan kolom ini!");
                        edtEmail.setError("Jangan kosongkan kolom ini!");
                        edtPassword.setError("Jangan kosongkan kolom ini!");
                    }
                }
            }
        });
    }

    public void registerData(String fullName, String username, String email, String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi ...");

        showDialog();
        AndroidNetworking.post(BuildConfig.BASE_URL + "api/warteg/create")
                .addBodyParameter("ownerName", fullName)
                .addBodyParameter("username", username)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                try {
                    isSuccess = response.getBoolean("isSuccess");
                    messageResponse = response.getString("message");
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), "Akun berhasil didaftarkan!, Silahkan menunggu untuk proses review :)", Toast.LENGTH_LONG).show();
                        resetInput();
                    } else {
                        failedResponse = response.getString("messages");
                        Toast.makeText(getApplicationContext(), failedResponse, Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError error) {
                Toast.makeText(getApplicationContext(), error.getErrorBody(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    private void resetInput() {
        edtFullName.setText("");
        edtUsername.setText("");
        edtEmail.setText("");
        edtPassword.setText("");
    }

    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }
}