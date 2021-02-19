package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class Welcome extends AppCompatActivity {
    EditText edtSearchMenu;

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