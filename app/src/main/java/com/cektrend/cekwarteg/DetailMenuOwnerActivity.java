package com.cektrend.cekwarteg;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_DESC;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_ID;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_NAME;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_PHOTO;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_PRICE;

public class DetailMenuOwnerActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnEditMenu;
    TextView tvMenuName, tvMenuDesc;
    ImageView imgMenu;
    String menuName, menuDesc, menuPhoto, menuPrice, menuId;
    ProgressBar progressBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu_owner);
        initComponents();
        initValue();
        btnEditMenu.setOnClickListener(this);
    }

    private void initComponents() {
        btnEditMenu = findViewById(R.id.btn_edit_menu);
        tvMenuName = findViewById(R.id.tv_menu_name);
        tvMenuDesc = findViewById(R.id.tv_menu_desc);
        imgMenu = findViewById(R.id.img_menu);
        progressBar = findViewById(R.id.loading_img);
        menuId = getIntent().getStringExtra(MENU_ID);
        menuName = getIntent().getStringExtra(MENU_NAME);
        menuDesc = getIntent().getStringExtra(MENU_DESC);
        menuPrice = getIntent().getStringExtra(MENU_PRICE);
        menuPhoto = getIntent().getStringExtra(MENU_PHOTO);
        toolbar = findViewById(R.id.toolbar_detail_datamenu_owner);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Data Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initValue() {
        tvMenuName.setText(menuName);
        tvMenuDesc.setText(menuDesc);
        Glide.with(DetailMenuOwnerActivity.this)
                .load(menuPhoto)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.ic_baseline_image_not_supported_24).diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into(imgMenu);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_edit_menu) {
            Intent editMenu = new Intent(DetailMenuOwnerActivity.this, EditMenuActivity.class);
            editMenu.putExtra(MENU_ID, menuId);
            editMenu.putExtra(MENU_NAME, menuName);
            editMenu.putExtra(MENU_DESC, menuDesc);
            editMenu.putExtra(MENU_PRICE, menuPrice);
            editMenu.putExtra(MENU_PHOTO, menuPhoto);
            startActivity(editMenu);
            finish();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}