package com.cektrend.cekwarteg.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cektrend.cekwarteg.BuildConfig;
import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.utils.FileUtils;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_DESC;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_NAME;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_PHOTO;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_PRICE;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MY_SHARED_PREFERENCES;
import static com.cektrend.cekwarteg.utils.ConstantUtil.TOKEN;
import static com.cektrend.cekwarteg.utils.ConstantUtil.MENU_ID;

public class EditMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    Button btnEditMenu, btnChooseImg;
    ImageView imgEditMenu;
    EditText edtMenuName, edtMenuDesc, edtMenuPrice;
    String menuName, menuDesc, menuPhoto, menuPrice, myToken, messageResponse, menuId, imagePath = null;
    ProgressDialog pDialog;
    Boolean isSuccess;
    SharedPreferences sharedPreferences;
    File imageFile;
    private static final int MY_REQUEST_CODE_PERMISSION = 500;
    private static final int MY_RESULT_CODE_FILECHOOSER = 700;
    private static final String LOG_TAG = "AndroidExample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        initComponents();
        initValue();
        btnEditMenu.setOnClickListener(this);
        btnChooseImg.setOnClickListener(this);
    }

    private void initComponents() {
        edtMenuName = findViewById(R.id.edt_menu_name);
        edtMenuDesc = findViewById(R.id.edt_menu_desc);
        edtMenuPrice = findViewById(R.id.edt_menu_price);
        btnEditMenu = findViewById(R.id.btn_edit_menu);
        btnChooseImg = findViewById(R.id.btn_choose_img);
        imgEditMenu = findViewById(R.id.img_edit_menu);
        toolbar = findViewById(R.id.toolbar_edit_datamenu);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        myToken = sharedPreferences.getString(TOKEN, null);
        getSupportActionBar().setTitle("Edit Data Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menuId = getIntent().getStringExtra(MENU_ID);
        menuName = getIntent().getStringExtra(MENU_NAME);
        menuDesc = getIntent().getStringExtra(MENU_DESC);
        menuPrice = getIntent().getStringExtra(MENU_PRICE);
        menuPhoto = getIntent().getStringExtra(MENU_PHOTO);
    }

    private void initValue() {
        edtMenuName.setText(menuName);
        edtMenuDesc.setText(menuDesc);
        edtMenuPrice.setText(menuPrice);
        Glide.with(this)
                .load(menuPhoto)
                .error(R.drawable.ic_baseline_image_50).diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into(imgEditMenu);
    }

    @Override
    public void onClick(View view) {
        String menuName = edtMenuName.getText().toString().trim();
        String menuDesc = edtMenuDesc.getText().toString().trim();
        String menuPrice = edtMenuPrice.getText().toString().trim();
        int id = view.getId();
        if (id == R.id.btn_edit_menu) {
            if (!menuName.isEmpty() || !menuDesc.isEmpty() || !menuPrice.isEmpty()) {
                editdata(menuName, menuDesc, menuPrice, imageFile);
            } else {
                edtMenuName.setError("Jangan kosongkan kolom ini!");
                edtMenuDesc.setError("Jangan kosongkan kolom ini!");
                edtMenuPrice.setError("Jangan kosongkan kolom ini!");
            }
        } else if (id == R.id.btn_choose_img) {
            askPermissionAndBrowseFile();
        }
    }

    private void askPermissionAndBrowseFile() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // Level 23
            int permisson = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }
        }
        this.doBrowseFile();
    }

    private void doBrowseFile() {
        Intent chooseImage = new Intent(Intent.ACTION_GET_CONTENT);
        chooseImage.setType("image/*");
        chooseImage.addCategory(Intent.CATEGORY_OPENABLE);
        chooseImage = Intent.createChooser(chooseImage, "Choose a file");
        startActivityForResult(chooseImage, MY_RESULT_CODE_FILECHOOSER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE_PERMISSION) {// Note: If request is cancelled, the result arrays are empty.
            // Permissions granted (CALL_PHONE).
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(EditMenuActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();
                this.doBrowseFile();
            }
            // Cancelled or denied.
            else {
                Toast.makeText(EditMenuActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MY_RESULT_CODE_FILECHOOSER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri fileUri = data.getData();
                    Log.e(LOG_TAG, "Uri: " + fileUri);
                    try {
                        imagePath = FileUtils.getPath(this, fileUri);
                        Log.e(LOG_TAG, "Image Path: " + imagePath);
                        imageFile = new File(imagePath);
                        Log.e(LOG_TAG, "Image File: " + imageFile);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error: " + e);
                        Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                    if (imageFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        imgEditMenu.setImageBitmap(myBitmap);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void editdata(String menuNames, String menuDescs, String menuPrices, File imageFile) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi ...");

        showDialog();
        AndroidNetworking.upload(BuildConfig.BASE_URL + "api/menu/{idMenu}/update")
                .addHeaders("Authorization", myToken)
                .addPathParameter("idMenu", String.valueOf(menuId))
                .setContentType("multipart/form-data")
                .addMultipartFile("photo", imageFile)
                .addMultipartParameter("name", menuNames)
                .addMultipartParameter("description", menuDescs)
                .addMultipartParameter("price", menuPrices)
                .addMultipartParameter("isHaveStock", "1")
                .setPriority(Priority.HIGH).build().setUploadProgressListener(new UploadProgressListener() {
            @Override
            public void onProgress(long bytesUploaded, long totalBytes) {
                if (imageFile != null) {
                    pDialog.setMessage("Proses upload gambar ...");
                }
            }
        }).getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                try {
                    isSuccess = response.getBoolean("isSuccess");
                    messageResponse = response.getString("message");
                    menuName = response.getJSONObject("data").getString("name");
                    menuDesc = response.getJSONObject("data").getString("description");
                    menuPrice = response.getJSONObject("data").getString("price");
                    menuPhoto = response.getJSONObject("data").getString("photo");
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), messageResponse, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), messageResponse, Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError error) {
                Toast.makeText(getApplicationContext(), error.getErrorBody(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }

    private void goBackDeail() {
        Intent backToDetail = new Intent(EditMenuActivity.this, DetailMenuOwnerActivity.class);
        backToDetail.putExtra(MENU_NAME, menuName);
        backToDetail.putExtra(MENU_DESC, menuDesc);
        backToDetail.putExtra(MENU_PRICE, menuPrice);
        backToDetail.putExtra(MENU_PHOTO, menuPhoto);
        startActivity(backToDetail);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        goBackDeail();
        return true;
    }

    @Override
    public void onBackPressed() {
        goBackDeail();
        super.onBackPressed();
    }
}