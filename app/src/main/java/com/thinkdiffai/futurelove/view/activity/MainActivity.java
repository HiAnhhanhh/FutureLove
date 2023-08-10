package com.thinkdiffai.futurelove.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ActivityMainBinding;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ActivityMainBinding activityMainBinding;
    private KProgressHUD kProgressHUD;
    public int eventSummaryCurrentId = -1;
    // checking login flag
    private boolean userLoggedIn = false;

    private boolean loginState;
    public int soThuTuSuKien = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        // Check LOGIN_STATE
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        loginState = sharedPreferences.getBoolean("LOGIN_STATE", false);
        if (!loginState) {
            // Get Check that a user log-in successfully or not
            userLoggedIn = getIntent().getBooleanExtra("LOGIN_SUCCESS", false);
            if (!userLoggedIn) {
                Intent intent = new Intent(this, SignInSignUpActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }
        setContentView(activityMainBinding.getRoot());
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