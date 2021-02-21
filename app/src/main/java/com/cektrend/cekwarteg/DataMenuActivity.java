package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DataMenuActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvMenuEvent;
    EditText edtMenuName, edtMenuDesc;
    Button btnChooseImg, btnAddMenu;
    private Toolbar toolbar;

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
        btnChooseImg = findViewById(R.id.btn_choose_img);
        btnAddMenu = findViewById(R.id.btn_add_menu);
        toolbar = findViewById(R.id.toolbar_add_datamenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void clickListener() {
        btnChooseImg.setOnClickListener(this);
        btnAddMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_choose_img) {
            //    Event Choose IMG
        } else if (id == R.id.btn_add_menu) {
            //    Event Add Menu
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}