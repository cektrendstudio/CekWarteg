package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;

public class DetailMenuActivity extends AppCompatActivity {
    EditText edtName, edtUlas;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        initComponent();
    }

    private void initComponent(){
        edtName = findViewById(R.id.edt_nama);
        edtUlas = findViewById(R.id.edt_ulasan);
        ratingBar = findViewById(R.id.rating_menu);
    }
}