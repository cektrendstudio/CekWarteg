package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailWartegActivity extends AppCompatActivity {
    ImageView imgWarteg;
    TextView tvWartegName, tvWartegDesc;
    Button btnFavorite;
    RatingBar ratingWarteg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_warteg);
        initComponents();
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Event Favorite
            }
        });
    }

    private void initComponents() {
        imgWarteg = findViewById(R.id.img_warteg);
        tvWartegName = findViewById(R.id.tv_warteg_name);
        tvWartegDesc = findViewById(R.id.tv_warteg_desc);
        btnFavorite = findViewById(R.id.btn_favorite);
        ratingWarteg = findViewById(R.id.rating_warteg);
    }
}