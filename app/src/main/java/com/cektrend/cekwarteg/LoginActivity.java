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
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MY_SHARED_PREFERENCES;
import static com.cektrend.cekwarteg.utils.ConstantUtil.SESSION_STATUS;
import static com.cektrend.cekwarteg.utils.ConstantUtil.SESSION_USERNAME;
import static com.cektrend.cekwarteg.utils.ConstantUtil.TOKEN;
import static com.cektrend.cekwarteg.utils.ConstantUtil.WARTEG_ID;
import static com.cektrend.cekwarteg.utils.ConstantUtil.WARTEG_NAME;
import static com.cektrend.cekwarteg.utils.ConstantUtil.WARTEG_PHOTO;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtUsername, edtPassword;
    Button btnLogin, btnRegister;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    Boolean is_approve = false;
    Boolean isSuccess = false;
    Boolean session = false;
    String userName, myToken, wartegName, wartegPhoto;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        sharedPreferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        clickListener();
        session = sharedPreferences.getBoolean(SESSION_STATUS, false);
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
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    }

    private void login(String username, String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi...");
        showDialog();
        AndroidNetworking.post(BuildConfig.BASE_URL + "api/auth/login")
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
                                id = data.getInt("id");
                                wartegName = data.getString("name");
                                wartegPhoto = data.getString("photo_profile");
                                myToken = response.getString("access_token");
                                isSuccess = response.getBoolean("isSuccess");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (isSuccess) {
                            Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(SESSION_STATUS, true);
                            editor.putString(SESSION_USERNAME, userName);
                            editor.putString(WARTEG_ID, String.valueOf(id));
                            editor.putString(WARTEG_NAME, wartegName);
                            editor.putString(WARTEG_PHOTO, wartegPhoto);
                            editor.putString(TOKEN, myToken);
                            editor.apply();
                            Intent login = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(login);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal Login", Toast.LENGTH_SHORT).show();
                            edtPassword.setText("");
                            edtUsername.setText("");
                            hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), anError.getErrorBody(), Toast.LENGTH_SHORT).show();
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