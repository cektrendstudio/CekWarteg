package com.cektrend.cekwarteg.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cektrend.cekwarteg.BuildConfig;
import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.adapter.UlasanAdapter;
import com.cektrend.cekwarteg.data.DataUlasan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailMenuActivity extends AppCompatActivity {
    ImageView imgMenu;
    TextView tvMenuName, tvMenuDesc;
    RatingBar ratingMenu;
    EditText edtReviewerName, edtUlasan;
    Button btnSubmit;
    ProgressDialog pDialog;
    Boolean isSuccess = false;
    private Toolbar toolbar;

    String menuName, menuDesc, menuImg, menuId, message;
    UlasanAdapter adapter;
    ArrayList<DataUlasan> dataUlasans = new ArrayList<>();
    private RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("TAG", "id" + menuId);
        showData();
        setRefreshLayout();

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
        toolbar = findViewById(R.id.toolbar_detail_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Informasi Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtReviewerName = findViewById(R.id.edt_reviewer_name);
        edtUlasan = findViewById(R.id.edt_ulasan);
        btnSubmit = findViewById(R.id.btn_submit);
        recyclerView = findViewById(R.id.rv_ulasan);
        refreshLayout = findViewById(R.id.swipe_refresh);
    }

    private void _setComponents() {
        tvMenuName.setText(menuName);
        tvMenuDesc.setText(menuDesc);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(10));
        Glide.with(this)
                .load(menuImg)
                .placeholder(R.drawable.ic_baseline_image_not_supported_24) // any placeholder to load at start
                .error(R.drawable.ic_baseline_image_not_supported_24)
                .apply(requestOptions)
                .into(imgMenu);
    }

    private void setRefreshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 3000);
                dataUlasans.clear();
                showData();
            }
        });
    }

    private void saveData(String name, String review) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi ...");
        showDialog();
        AndroidNetworking.post(BuildConfig.BASE_URL + "api/menu/{menu_id}/review")
                .addPathParameter("menu_id", menuId)
                .addBodyParameter("name", name)
                .addBodyParameter("reviewText", review)
                //                .addBodyParameter("reviewText", ulas)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            isSuccess = response.getBoolean("isSuccess");
                            message = response.getString("message");
                            if (isSuccess) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                edtReviewerName.setText("");
                                edtUlasan.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

    private void showData() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Memuat ...");
        showDialog();
        AndroidNetworking.get(BuildConfig.BASE_URL + "api/menu/{id}")
                .addPathParameter("id", menuId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            //                            Log.d("TAG", "Array " + response.getJSONArray("data").get(0));
                            //                            Log.d("TAG", "String " + response.getString("data"));
                            JSONObject jsonObject = response.getJSONObject("data");
                            JSONArray jsonArray = jsonObject.getJSONArray("review");
                            //                            Log.d("TAG", "Lenght" + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d("TAG", "jason" + jsonArray.get(i));
                                JSONObject data = jsonArray.getJSONObject(i);
                                //                                Log.d("TAG", "jason" + data);
                                dataUlasans.add(new DataUlasan(data.getString("name"),
                                        data.getString("review_text"),
                                        data.getString("created_at")));
                            }
                            adapter = new UlasanAdapter(dataUlasans, getApplicationContext(), DetailMenuActivity.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG", "test ", e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), "Gagal memuat, cobalah periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "Pesan", error);
                        hideDialog();
                    }
                });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
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