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
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_DESC;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_NAME;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_PHOTO;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_PRICE;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MY_SHARED_PREFERENCES;
import static com.cektrend.cekwarteg.utils.ConstantUtil.TOKEN;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_ID;

public class EditMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    Button btnEditMenu;
    EditText edtMenuName, edtMenuDesc, edtMenuPrice;
    String menuName, menuDesc, menuPhoto, menuPrice, myToken, messageResponse, menuId;
    ProgressDialog pDialog;
    Boolean isSuccess;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        initComponents();
        initValue();
        btnEditMenu.setOnClickListener(this);
    }

    private void initComponents() {
        edtMenuName = findViewById(R.id.edt_menu_name);
        edtMenuDesc = findViewById(R.id.edt_menu_desc);
        edtMenuPrice = findViewById(R.id.edt_menu_price);
        btnEditMenu = findViewById(R.id.btn_edit_menu);
        toolbar = findViewById(R.id.toolbar_edit_datamenu);
        sharedPreferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        myToken = sharedPreferences.getString(TOKEN, null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Data Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menuId = getIntent().getStringExtra(MENU_ID);
        menuName = getIntent().getStringExtra(MENU_NAME);
        menuDesc = getIntent().getStringExtra(MENU_DESC);
        menuPrice = getIntent().getStringExtra(MENU_PRICE);
        menuPhoto = getIntent().getStringExtra(MENU_PHOTO);
    }

    private void initValue() {
        edtMenuName.setText(menuName);
        edtMenuDesc.setText(menuDesc);
        edtMenuPrice.setText(menuPrice);
    }

    @Override
    public void onClick(View view) {
        String menuName = edtMenuName.getText().toString().trim();
        String menuDesc = edtMenuDesc.getText().toString().trim();
        String menuPrice = edtMenuPrice.getText().toString().trim();
        int id = view.getId();
        if (id == R.id.btn_edit_menu) {
            if (!menuName.isEmpty() || !menuDesc.isEmpty() || !menuPrice.isEmpty()) {
                editdata(menuName, menuDesc, menuPrice);
            } else {
                edtMenuName.setError("Jangan kosongkan kolom ini!");
                edtMenuDesc.setError("Jangan kosongkan kolom ini!");
                edtMenuPrice.setError("Jangan kosongkan kolom ini!");
            }
        }
    }

    public void editdata(String menuNames, String menuDescs, String menuPrices) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi ...");

        showDialog();
        AndroidNetworking.post(BuildConfig.BASE_URL + "api/menu/{idMenu}/update")
                .addHeaders("Authorization", myToken)
                .addPathParameter("idMenu", String.valueOf(menuId))
                .setContentType("multipart/form-data")
                .addBodyParameter("name", menuNames)
                .addBodyParameter("description", menuDescs)
                .addBodyParameter("price", menuPrices)
                .addBodyParameter("isHaveStock", "1")
                // .addFileBody(file)
                .setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                try {
                    isSuccess = response.getBoolean("isSuccess");
                    messageResponse = response.getString("message");
                    menuName = response.getJSONObject("data").getString("name");
                    menuDesc = response.getJSONObject("data").getString("description");
                    menuPrice = response.getJSONObject("data").getString("price");
                    menuPhoto = response.getJSONObject("data").getString("photo");
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), messageResponse, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), messageResponse, Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();
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

    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }

    private void goBackDeail() {
        Intent backToDetail = new Intent(EditMenuActivity.this, DetailMenuOwnerActivity.class);
        backToDetail.putExtra(MENU_NAME, menuName);
        backToDetail.putExtra(MENU_DESC, menuDesc);
        backToDetail.putExtra(MENU_PRICE, menuPrice);
        backToDetail.putExtra(MENU_PHOTO, menuPhoto);
        startActivity(backToDetail);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        goBackDeail();
        return true;
    }

    @Override
    public void onBackPressed() {
        goBackDeail();
        super.onBackPressed();
    }
}