package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MY_SHARED_PREFERENCES;
import static com.cektrend.cekwarteg.utils.ConstantUtil.TOKEN;

public class AddMenuActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvMenuEvent;
    EditText edtMenuName, edtMenuDesc, edtMenuPrice;
    Button btnChooseImg, btnAddMenu;
    private Toolbar toolbar;
    SharedPreferences sharedPreferences;
    String myToken, messageResponse;
    Boolean isSuccess;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_menu);
        initComponents();
        clickListener();
    }

    private void initComponents() {
        tvMenuEvent = findViewById(R.id.tv_menu_event);
        edtMenuName = findViewById(R.id.edt_menu_name);
        edtMenuDesc = findViewById(R.id.edt_menu_desc);
        edtMenuPrice = findViewById(R.id.edt_menu_price);
        btnChooseImg = findViewById(R.id.btn_choose_img);
        btnAddMenu = findViewById(R.id.btn_add_menu);
        sharedPreferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        myToken = sharedPreferences.getString(TOKEN, null);
        toolbar = findViewById(R.id.toolbar_add_datamenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Data Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void clickListener() {
        btnChooseImg.setOnClickListener(this);
        btnAddMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String menuName = edtMenuName.getText().toString().trim();
        String menuDesc = edtMenuDesc.getText().toString().trim();
        String menuPrice = edtMenuPrice.getText().toString().trim();
        int id = view.getId();
        if (id == R.id.btn_choose_img) {
            //    Event Choose IMG
        } else if (id == R.id.btn_add_menu) {
            if (!menuName.isEmpty() || !menuDesc.isEmpty() || !menuPrice.isEmpty()) {
                simpandata(menuName, menuDesc, menuPrice);
            } else {
                edtMenuName.setError("Jangan kosongkan kolom ini!");
                edtMenuDesc.setError("Jangan kosongkan kolom ini!");
                edtMenuPrice.setError("Jangan kosongkan kolom ini!");
            }
        }
    }

    public void simpandata(String menuName, String menuDesc, String menuPrice) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi ...");

        showDialog();
        AndroidNetworking.post(BuildConfig.BASE_URL + "api/menu/create")
                .addHeaders("Authorization", myToken)
                .setContentType("multipart/form-data")
                .addBodyParameter("name", menuName)
                .addBodyParameter("description", menuDesc)
                .addBodyParameter("price", menuPrice)
                // .addFileBody(file)
                .addBodyParameter("isHaveStock", "1")
                .setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                try {
                    isSuccess = response.getBoolean("isSuccess");
                    messageResponse = response.getString("message");
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), messageResponse, Toast.LENGTH_SHORT).show();
                        resetInput();
                    } else {
                        Toast.makeText(getApplicationContext(), messageResponse, Toast.LENGTH_SHORT).show();
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
        edtMenuName.setText("");
        edtMenuDesc.setText("");
        edtMenuPrice.setText("");
    }

    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}