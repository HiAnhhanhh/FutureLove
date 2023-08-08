package com.thinkdiffai.futurelove.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ActivityMainBinding;
import com.thinkdiffai.futurelove.view.adapter.MainViewPagerAdapter;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

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
}