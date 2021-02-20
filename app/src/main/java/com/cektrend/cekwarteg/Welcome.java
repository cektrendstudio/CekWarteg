package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cektrend.cekwarteg.model.DataWarteg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Welcome extends AppCompatActivity {
    EditText edtSearchMenu;
    ProgressDialog pDialog;
    ArrayList<DataWarteg> dataWartegs = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initComponents();
    }

    private void initComponents() {
        edtSearchMenu = findViewById(R.id.edt_search_menu);

    }

}