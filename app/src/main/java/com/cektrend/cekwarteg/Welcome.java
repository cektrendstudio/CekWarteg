package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.cektrend.cekwarteg.adapter.ListWartegAdapter;
import com.cektrend.cekwarteg.model.DataWarteg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Welcome extends AppCompatActivity {
    EditText edtSearchMenu;
    ProgressDialog pDialog;
    ListWartegAdapter adapter;
    ArrayList<DataWarteg> dataWartegs = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initComponents();
        AndroidNetworking.initialize(getApplicationContext());
        recyclerView = findViewById(R.id.rv_warteg);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        showWarteg();

    }

    private void initComponents() {
        edtSearchMenu = findViewById(R.id.edt_search_menu);

    }

    private void showWarteg() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Memuat ...");
        showDialog();
        AndroidNetworking.get("http://103.146.203.97/api/warteg")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hideDialog();
                        try {
                            for (int i = 0; i < response.length();
                                 i++) {
                                JSONObject data =
                                        response.getJSONObject(i);
                                dataWartegs.add(new DataWarteg(data.getString("code"), data.getString("name"), data.getString("email"), data.getString("owner_name"), data.getString("address"), data.getString("phone"), data.getString("description"), data.getString("photo_profile")));
                            }
                            adapter = new ListWartegAdapter(dataWartegs, getApplicationContext(), Welcome.this);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), "Gagal memuat, cobalah periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
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