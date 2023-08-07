package com.thinkdiffai.futurelove.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ActivityMainBinding;
import com.thinkdiffai.futurelove.view.adapter.MainViewPagerAdapter;
import com.google.android.material.navigation.NavigationBarView;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private KProgressHUD kProgressHUD;
    public int eventSummaryCurrentId = -1;

    // checking login flag
    private boolean userLoggedIn = false;

    public int soThuTuSuKien = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        // Get Check that a user log-in successfully or not
        userLoggedIn = getIntent().getBooleanExtra("LOGIN_SUCCESS", false);
        if (!userLoggedIn) {
            Intent intent = new Intent(this, SignInSignUpActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        activityMainBinding.viewpager2.setUserInputEnabled(false);
        mainViewPagerAdapter = new MainViewPagerAdapter(this);
        activityMainBinding.viewpager2.setAdapter(mainViewPagerAdapter);
        onPageChangeViewPager();
        onClickBottomNavigation();
    }
    public void onPageChangeViewPager() {
        activityMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        activityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        return;

                    case 1:
                        activityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_comment).setChecked(true);
                        return;

                    case 2:
                        activityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_pairing).setChecked(true);
                        return;
                    case 3:
                        activityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_timeline).setChecked(true);
                        return;
                    case 4:
                        activityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(false);
                        activityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_comment).setChecked(false);
                        activityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_pairing).setChecked(false);
                        activityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_timeline).setChecked(false);
                        break;
                }
            }
        });
    }


    public void onClickBottomNavigation() {

        activityMainBinding.bottomNav.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home)
                    activityMainBinding.viewpager2.setCurrentItem(0);
                else if (id == R.id.nav_comment)
                    activityMainBinding.viewpager2.setCurrentItem(1);

                else if (id == R.id.nav_pairing)
                    activityMainBinding.viewpager2.setCurrentItem(2);
                else if (id == R.id.nav_timeline)
                    activityMainBinding.viewpager2.setCurrentItem(3);
                else
                    activityMainBinding.viewpager2.setCurrentItem(0);
                return true;
            }
        });
    }

    public KProgressHUD createHud() {
        return kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    public void setCurrentPage(int currentPage) {
        activityMainBinding.viewpager2.setCurrentItem(currentPage);
    }


}