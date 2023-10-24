package com.thinkdiffai.futurelove.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ActivityMainBinding;
import com.thinkdiffai.futurelove.view.fragment.HomeFragment;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class MainActivity extends AppCompatActivity {

    // --------------------------------------------
    // Params: check that home or comment or pairing fragment to user detail fragment
    public boolean homeToUserDetail = false;
    public boolean commentToUserDetail = false;
    public boolean pairingToUserDetail = false;
    // --------------------------------------------

    private SharedPreferences sharedPreferences;
    private ActivityMainBinding activityMainBinding;
    private KProgressHUD kProgressHUD;

    public Bitmap maleImage;
    public Bitmap femaleImage;
    public long waitingSwapFaceTime = 0L;

    public NavController navController;

    // checking login flag
    private boolean userLoggedIn = false;

    private boolean loginState;
    public long eventSummaryCurrentId = -1;

    private String user_id;

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