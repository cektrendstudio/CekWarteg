package com.cektrend.cekwarteg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailMenuActivity extends AppCompatActivity {
    ImageView imgMenu;
    TextView tvMenuName, tvMenuDesc;
    RatingBar ratingMenu;
    EditText edtReviewerName, edtUlasan;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        initComponents();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Event Submit Review
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
}