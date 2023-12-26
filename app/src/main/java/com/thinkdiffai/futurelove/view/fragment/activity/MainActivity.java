package com.thinkdiffai.futurelove.view.fragment.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ActivityMainBinding;
import com.thinkdiffai.futurelove.view.BroadcastReceiver.InternetReceiver;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class MainActivity extends AppCompatActivity {

    // --------------------------------------------
    // Params: check that home or comment or pairing fragment to user detail fragment
    public boolean homeToUserDetail = false;
    public boolean commentToUserDetail = false;
    public boolean pairingToUserDetail = false;

    private NavController navController;
    // --------------------------------------------

    private SharedPreferences sharedPreferences;
    private ActivityMainBinding activityMainBinding;
    private KProgressHUD kProgressHUD;

    private BroadcastReceiver broadcastReceiver = null;

    public Bitmap maleImage;
    public Bitmap femaleImage;
    public long waitingSwapFaceTime = 0L;


    // checking login flag
    private boolean userLoggedIn = false;

    private boolean loginState;
    public long eventSummaryCurrentId = -1;
    Context context;

    private String user_id;

    public int soThuTuSuKien = 0;

    @Nullable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

       broadcastReceiver = new InternetReceiver();
       InternetStatus();

//        NavController navController = Navigation.findNavController(MainActivity.this, R.id.activity_main_nav_host_fragment);
//        navController.navigateUp();

       BottomNavigationView bottomNavigationView = activityMainBinding.activityMainBottomNavigationView;
       NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_nav_host_fragment);
       navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

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

    public void InternetStatus(){
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

}


