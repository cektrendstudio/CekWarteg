package com.cektrend.cekwarteg;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.cektrend.cekwarteg.adapter.DetailWartegAdapter;
import com.cektrend.cekwarteg.data.DataMenuOwner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailWartegActivity extends AppCompatActivity {
    ImageView imgWarteg;
    TextView tvWartegName, tvWartegDesc;
    DetailWartegAdapter adapter;
    ArrayList<DataMenuOwner> dataMenuOwner = new ArrayList<DataMenuOwner>();
    ProgressDialog pDialog;
    RatingBar ratingWarteg;
    private RecyclerView recyclerView;

    private String wartegId;
    private String descWarteg;
    private String nameWarteg;
    private String scrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_warteg);

        wartegId = getIntent().getStringExtra("id");
        scrImage = getIntent().getStringExtra("photo_profile");
        nameWarteg = getIntent().getStringExtra("name");
        descWarteg = getIntent().getStringExtra("description");
        recyclerView = findViewById(R.id.rv_best_menu);
        initComponents();
        _setComponents();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        showDetailWarteg();

    }

    private void showDetailWarteg(){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Memuat ...");
        showDialog();
        AndroidNetworking.get(BuildConfig.BASE_URL + "api/warteg/{idWarteg}")
                .addPathParameter("idWarteg", String.valueOf(wartegId))
                .setTag(this)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                           // Log.e("TAGt", "Nama Wateg : " + response.getJSONObject("data").getString("name"));
                            JSONArray jsonArray = new JSONArray(response.getJSONObject("data").getString("menu"));
                            //Log.e("TAGmenut", "menu : " +jsonArray);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    Log.d("TAG", "data" + jsonArray.getJSONObject(i));
                                    dataMenuOwner.add(new DataMenuOwner(data.getInt("id"),
                                            data.getString("code"),
                                            data.getString("name"),
                                            data.getInt("warteg_id"),
                                            data.getInt("price"),
                                            data.getBoolean("is_have_stock"),
                                            data.getString("created_at"),
                                            data.getString("updated_at"),
                                            data.getString("photo") ,
                                            data.getString("description")));
                                    adapter = new DetailWartegAdapter(dataMenuOwner, getApplicationContext(), DetailWartegActivity.this);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setHasFixedSize(true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("TAGerr", "onResponse: ", e);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG", "onResponse: ", e);
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        //    Handle error
                        Toast.makeText(getApplicationContext(), "Gagal memuat, cobalah periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
                        //    memunculkan Toast saat data gagal ditampilkan
                        hideDialog();
                        Log.e("TAG", "onResponse: ",error );
                    }
                });
    }


    private void initComponents() {
        imgWarteg = findViewById(R.id.img_warteg);
        tvWartegName = findViewById(R.id.tv_warteg_name);
        tvWartegDesc = findViewById(R.id.tv_warteg_desc);
        ratingWarteg = findViewById(R.id.rating_warteg);
    }

    private  void _setComponents(){
     tvWartegName.setText(nameWarteg);
     tvWartegDesc.setText(descWarteg);
        Glide.with(this)
                .load(scrImage) // image url
                .placeholder(R.drawable.ic_baseline_image_not_supported_24) // any placeholder to load at start
                .error(R.drawable.ic_baseline_image_not_supported_24)
                .override(300, 300)
                .centerCrop()
                .into(imgWarteg);
    }


    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }
}