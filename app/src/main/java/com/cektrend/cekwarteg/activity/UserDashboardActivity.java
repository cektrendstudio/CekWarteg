package com.cektrend.cekwarteg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cektrend.cekwarteg.BuildConfig;
import com.cektrend.cekwarteg.R;
import com.cektrend.cekwarteg.activity.DashboardActivity;
import com.cektrend.cekwarteg.adapter.ListWartegAdapter;
import com.cektrend.cekwarteg.data.DataWarteg;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MY_SHARED_PREFERENCES;
import static com.cektrend.cekwarteg.utils.ConstantUtil.SESSION_STATUS;
import static com.cektrend.cekwarteg.utils.ConstantUtil.SESSION_USERNAME;
import static com.cektrend.cekwarteg.utils.ConstantUtil.WARTEG_PHOTO;

public class UserDashboardActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    SearchView searchView;
    TextView tvProfileUsername;
    ListWartegAdapter adapter;
    ArrayList<DataWarteg> dataWartegs = new ArrayList<>();
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    Menu menu;
    boolean doubleBack = false;
    SwipeRefreshLayout swipeRefresh;
    ShimmerFrameLayout shimmerWarteg;
    SharedPreferences sharedPreferences;
    Boolean session = false;
    String sessionUsername, sessionWartegPhoto;
    CircleImageView profilePhoto;
    LinearLayout profileToolbar;
    ImageButton btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        initComponents();
        AndroidNetworking.initialize(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        showWarteg();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<DataWarteg> dataFilter = new ArrayList<>();
                for (DataWarteg data : dataWartegs) {
                    String warteg = data.getName().toLowerCase();
                    if (warteg.contains(newText)) {
                        dataFilter.add(data);
                    }
                }
                if (adapter != null) {
                    adapter.setFilter(dataFilter);
                }
                return true;
            }
        });

    }

    private void initComponents() {
        recyclerView = findViewById(R.id.rv_warteg);
        searchView = findViewById(R.id.searchView);
        shimmerWarteg = findViewById(R.id.shimmer_warteg);
        profileToolbar = findViewById(R.id.profile_toolbar);
        profilePhoto = findViewById(R.id.profile_photo);
        tvProfileUsername = findViewById(R.id.profile_username);
        btnAccount = findViewById(R.id.btn_account);
        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        sessionAccount();
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        btnAccount.setOnClickListener(this);
        profileToolbar.setOnClickListener(this);
    }

    private void sessionAccount() {
        sharedPreferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(SESSION_STATUS, false);
        sessionUsername = sharedPreferences.getString(SESSION_USERNAME, null);
        sessionWartegPhoto = sharedPreferences.getString(WARTEG_PHOTO, null);
        if (session) {
            profileToolbar.setVisibility(View.VISIBLE);
            btnAccount.setVisibility(View.GONE);
            tvProfileUsername.setText(sessionUsername);
            Glide.with(this)
                    .load(sessionWartegPhoto)
                    .error(R.drawable.ic_baseline_image_50).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop().placeholder(R.drawable.ic_baseline_image_not_supported_24)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(profilePhoto);
        } else {
            btnAccount.setVisibility(View.VISIBLE);
            profileToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sessionAccount();
    }

    private void goAccount() {
        Intent akunOwner = new Intent(UserDashboardActivity.this, LoginActivity.class);
        startActivity(akunOwner);
    }

    private void showWarteg() {
        showDialog();
        AndroidNetworking.get(BuildConfig.BASE_URL + "api/warteg")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            //                            Log.d("TAG", "Array " + response.getJSONArray("data").get(0));
                            //                            Log.d("TAG", "String " + response.getString("data"));
                            JSONArray jsonArray = response.getJSONArray("data");
                            //                            Log.d("TAG", "Lenght" + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //                                Log.d("TAG", "jason" + jsonArray.get(i));
                                JSONObject data = jsonArray.getJSONObject(i);
                                dataWartegs.add(new DataWarteg(data.getString("id"),
                                        data.getString("code"),
                                        data.getString("name"),
                                        data.getString("email"),
                                        data.getString("owner_name"),
                                        data.getString("address"),
                                        data.getString("phone"),
                                        data.getString("description"),
                                        data.getString("photo_profile")));
                            }
                            adapter = new ListWartegAdapter(dataWartegs, getApplicationContext(), UserDashboardActivity.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG", "test ", e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), "Gagal memuat, cobalah periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "Pesan", error);
                        hideDialog();
                    }
                });
    }

    private void showDialog() {
        shimmerWarteg.startShimmer();
        shimmerWarteg.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        shimmerWarteg.stopShimmer();
        shimmerWarteg.setVisibility(View.GONE);
        if (swipeRefresh.isRefreshing()) {
            dataWartegs.clear();
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            finish();
            return;
        }
        Toast.makeText(this, "Tekan sekali lagi untuk keluar!", Toast.LENGTH_SHORT).show();

        doubleBack = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        showDialog();
        showWarteg();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_account) {
            goAccount();
        } else if (id == R.id.profile_toolbar) {
            goAccount();
        }
    }
}