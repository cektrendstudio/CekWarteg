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
    Integer idMenu;
    Boolean isSuccess = false;
    Integer menuId, id;
    String name, review, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        idMenu = 1;

        initComponents();

//        ratingMenu.setRating((float) 4.5);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String nama = edtReviewerName.getText().toString();
               String ulas = edtUlasan.getText().toString();
                if (!nama.isEmpty() || !ulas.isEmpty()) {
                    saveData(nama, ulas);
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

    private void saveData(String nama, String ulas){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi ...");
        showDialog();
        AndroidNetworking.post(BuildConfig.BASE_URL + "api/menu/{menu_id}/review")
                .addBodyParameter("menu_id", String.valueOf(1))
                .addBodyParameter("name", nama)
                .addBodyParameter("review_text", ulas)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hideDialog();
//                        try {

//                            Log.d("TAG", "info" + response.getJSONArray(0));

//                            for (int i = 0; i < response.length(); i++) {
//                                Log.d("TAG", "respon" + response);
//                                JSONObject data = response.getJSONObject("data");
//                                menuId = data.getInt("menu_id");
//                                id = data.getInt("id");
//                                name = data.getString("name");
//                                review = data.getString("review_text");
//                                isSuccess = response.getBoolean("isSuccess");
//                                message = response.getString("message");
//                            }
//                            if (isSuccess) {
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                                hideDialog();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.e("TAG", "JSONE", e);
//                        }
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