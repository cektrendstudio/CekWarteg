package com.cektrend.cekwarteg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.cektrend.cekwarteg.utils.ConstantUtil.MY_SHARED_PREFERENCES;
import static com.cektrend.cekwarteg.utils.ConstantUtil.SESSION_STATUS;
import static com.cektrend.cekwarteg.utils.ConstantUtil.WARTEG_NAME;

public class DashboardActivity extends AppCompatActivity {

    boolean doubleBack = false;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    protected NavigationView nvDrawer;
    Button btnLogout;
    ImageView imgWartegPhoto;
    TextView tvWartegNameDrawer;
    SharedPreferences sharedPreferences;
    Menu menu;
    String wartegName, wartegPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initComponents();
        clickListener();
        showOverflowMenu(false);
    }

    private void initComponents() {
        btnLogout = findViewById(R.id.btn_logout);
        mDrawer = findViewById(R.id.drawer_layout);
        nvDrawer = findViewById(R.id.nvView);
        sharedPreferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        wartegName = sharedPreferences.getString(WARTEG_NAME, "No Name");
        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header_menu);
        tvWartegNameDrawer = headerLayout.findViewById(R.id.tv_warteg_name_drawer);
        tvWartegNameDrawer.setText(wartegName);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        mDrawer.addDrawerListener(drawerToggle);
        setupDrawerContent(nvDrawer);
        nvDrawer.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, new DashboardFragment())
                .commit();
    }

    private void clickListener() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SESSION_STATUS, false);
                editor.commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setCancelable(false);
                builder.setMessage("Apakah kamu ingin logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    //if user select "No", just cancel this dialog and continue with app
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_owner_datamenu, menu);
        showOverflowMenu(false);
        return true;
    }

    public void showOverflowMenu(boolean showMenu) {
        if (menu == null)
            return;
        menu.setGroupVisible(R.id.main_menu_group, showMenu);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_dashboard:
                fragmentClass = DashboardFragment.class;
                showOverflowMenu(false);
                break;
            case R.id.nav_data_menu:
                fragmentClass = DataMenuFragment.class;
                showOverflowMenu(true);
                break;
            case R.id.nav_ulasan:
                fragmentClass = UlasanFragment.class;
                showOverflowMenu(false);
                break;
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                showOverflowMenu(false);
                break;
            default:
                fragmentClass = DashboardFragment.class;
                showOverflowMenu(false);
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        menuItem.setChecked(true);
        mDrawer.closeDrawers();

        if (menuItem.getItemId() == R.id.nav_dashboard) {
            setTitle("Dashboard");
        } else {
            setTitle(menuItem.getTitle());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.action_add_datamenu) {
            Intent addMenu = new Intent(DashboardActivity.this, AddMenuActivity.class);
            startActivity(addMenu);
            return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
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
}
