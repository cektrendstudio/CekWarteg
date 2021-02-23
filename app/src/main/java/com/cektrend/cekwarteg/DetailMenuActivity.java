package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailMenuActivity extends AppCompatActivity {
    ImageView imgMenu;
    TextView tvMenuName, tvMenuDesc;
    RatingBar ratingMenu;
    EditText edtReviewerName, edtUlasan;
    Button btnSubmit;
    ProgressDialog pDialog;
    Boolean isSuccess = false;
    Integer id;
    String menuName, menuDesc, menuImg, menuId, message, messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        menuId = getIntent().getStringExtra("id");
        menuName = getIntent().getStringExtra("name");
        menuDesc = getIntent().getStringExtra("description");
        menuImg = getIntent().getStringExtra("photo_profile");

        initComponents();
        _setComponents();
        AndroidNetworking.initialize(getApplicationContext());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String name = edtReviewerName.getText().toString().trim();
               String review = edtUlasan.getText().toString().trim();
                if (!name.isEmpty() || !review.isEmpty()) {
                    saveData(name, review);
                } else {
                    edtReviewerName.setError("Please insert your Name");
                    edtUlasan.setError("Please insert your review");
                }
            }
        });
    }

    private void initComponents() {
        imgMenu = findViewById(R.id.img_menu);
        tvMenuName = findViewById(R.id.tv_menu_name);
        tvMenuDesc = findViewById(R.id.tv_menu_desc);
        ratingMenu = findViewById(R.id.rating_menu);
        edtReviewerName = findViewById(R.id.edt_reviewer_name);
        edtUlasan = findViewById(R.id.edt_ulasan);
        btnSubmit = findViewById(R.id.btn_submit);
    }
    private  void _setComponents(){
        tvMenuName.setText(menuName);
        tvMenuDesc.setText(menuDesc);
        Glide.with(this)
                .load(menuImg) // image url
                .placeholder(R.drawable.ic_baseline_image_not_supported_24) // any placeholder to load at start
                .error(R.drawable.ic_baseline_image_not_supported_24)
                .override(328, 233)
                .centerCrop()
                .into(imgMenu);
    }

    private void saveData(String name, String review){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi ...");
        showDialog();
        AndroidNetworking.post(BuildConfig.BASE_URL + "api/menu/{menu_id}/review")
                .addBodyParameter("menu_id", menuId)
                .addBodyParameter("name", name)
                .addBodyParameter("review_text", review)
//                .addBodyParameter("reviewText", ulas)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            id = response.getJSONObject("data").getInt("id");
                            isSuccess = response.getBoolean("isSuccess");
                            message = response.getString(    "message");
                            messages = response.getString("messages");

                            if (isSuccess) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_SHORT).show();
                                hideDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG", "JSONE", e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), "Gagal terhubung, cobalah periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "AnError" + error.getErrorBody());
                        hideDialog();
                    }
                });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}