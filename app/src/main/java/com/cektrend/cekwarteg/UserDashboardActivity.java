package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.cektrend.cekwarteg.adapter.ListWartegAdapter;
import com.cektrend.cekwarteg.data.DataWarteg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDashboardActivity extends AppCompatActivity {
    EditText edtSearchMenu;
    ProgressDialog pDialog;
    ListWartegAdapter adapter;
    ArrayList<DataWarteg> dataWartegs = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        initComponents();
        AndroidNetworking.initialize(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        showWarteg();

    }

    private void initComponents() {
        edtSearchMenu = findViewById(R.id.edt_search_menu);
        recyclerView = findViewById(R.id.rv_warteg);
    }

    private void showWarteg() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Memuat ...");
        showDialog();
        AndroidNetworking.get("http://103.146.203.97/api/warteg")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            Log.d("TAG", "Array " + response.getJSONArray("data").get(0));
                            Log.d("TAG", "String " + response.getString("data"));
                            JSONArray jsonArray = response.getJSONArray("data");
                            Log.d("TAG", "Lenght" + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d("TAG", "jason" + jsonArray.get(i));
                                JSONObject data = jsonArray.getJSONObject(i);
                                dataWartegs.add(new DataWarteg(data.getString("id"),
                                        data.getString("code"),
                                        data.getString("name"),
                                        data.getString("email"),
                                        data.getString("owner_name"),
                                        data.getString("address"),
                                        data.getString("phone"),
                                        data.getString("description"),
                                        data.getString("photo_profile")));
                            }
                            adapter = new ListWartegAdapter(dataWartegs, getApplicationContext(), UserDashboardActivity.this);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}