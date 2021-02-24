package com.cektrend.cekwarteg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.cektrend.cekwarteg.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MY_SHARED_PREFERENCES;
import static com.cektrend.cekwarteg.utils.ConstantUtil.TOKEN;

public class AddMenuActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvMenuEvent;
    EditText edtMenuName, edtMenuDesc, edtMenuPrice;
    Button btnChooseImg, btnAddMenu;
    ImageView imgAddMenu;
    private Toolbar toolbar;
    SharedPreferences sharedPreferences;
    String myToken, messageResponse, imagePath = null;
    Boolean isSuccess;
    ProgressDialog pDialog;
    File imageFile;
    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private static final String LOG_TAG = "AndroidExample";

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
        edtMenuPrice = findViewById(R.id.edt_menu_price);
        btnChooseImg = findViewById(R.id.btn_choose_img);
        btnAddMenu = findViewById(R.id.btn_add_menu);
        imgAddMenu = findViewById(R.id.img_add_menu);
        sharedPreferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        myToken = sharedPreferences.getString(TOKEN, null);
        toolbar = findViewById(R.id.toolbar_add_datamenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Data Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void clickListener() {
        btnChooseImg.setOnClickListener(this);
        btnAddMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String menuName = edtMenuName.getText().toString().trim();
        String menuDesc = edtMenuDesc.getText().toString().trim();
        String menuPrice = edtMenuPrice.getText().toString().trim();
        int id = view.getId();
        if (id == R.id.btn_choose_img) {
            askPermissionAndBrowseFile();
        } else if (id == R.id.btn_add_menu) {
            if (!menuName.isEmpty() || !menuDesc.isEmpty() || !menuPrice.isEmpty()) {
                simpandata(menuName, menuDesc, menuPrice, imageFile);
            } else {
                edtMenuName.setError("Jangan kosongkan kolom ini!");
                edtMenuDesc.setError("Jangan kosongkan kolom ini!");
                edtMenuPrice.setError("Jangan kosongkan kolom ini!");
            }
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
                Toast.makeText(AddMenuActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();
                this.doBrowseFile();
            }
            // Cancelled or denied.
            else {
                Toast.makeText(AddMenuActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
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
                        imgAddMenu.setImageBitmap(myBitmap);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void simpandata(String menuName, String menuDesc, String menuPrice, File imageFile) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Verifikasi ...");

        showDialog();
        AndroidNetworking.upload(BuildConfig.BASE_URL + "api/menu/create")
                .addHeaders("Authorization", myToken)
                .setContentType("multipart/form-data")
                .addMultipartFile("photo", imageFile)
                .addMultipartParameter("name", menuName)
                .addMultipartParameter("description", menuDesc)
                .addMultipartParameter("price", menuPrice)
                .addMultipartParameter("isHaveStock", "1")
                .setPriority(Priority.HIGH).build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        if (imageFile != null) {
                            pDialog.setMessage("Proses upload gambar ...");
                        }
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            isSuccess = response.getBoolean("isSuccess");
                            messageResponse = response.getString("message");
                            if (isSuccess) {
                                Toast.makeText(getApplicationContext(), messageResponse, Toast.LENGTH_SHORT).show();
                                resetInput();
                            } else {
                                Toast.makeText(getApplicationContext(), messageResponse, Toast.LENGTH_SHORT).show();
                                hideDialog();
                            }
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

    private void resetInput() {
        edtMenuName.setText("");
        edtMenuDesc.setText("");
        edtMenuPrice.setText("");
    }

    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}