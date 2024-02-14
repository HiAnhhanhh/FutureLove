package com.thinkdiffai.futurelove.view.fragment.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ActivitySwapFeatureBinding;

public class SwapFeatureActivity extends AppCompatActivity {

    ActivitySwapFeatureBinding swapFeatureBinding;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swapFeatureBinding = ActivitySwapFeatureBinding.inflate(getLayoutInflater());
        setContentView(swapFeatureBinding.getRoot());

        BottomNavigationView bottomNavigationView = swapFeatureBinding.bottomNavigationView;
       NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
       navController = navHostFragment.getNavController();
       NavigationUI.setupWithNavController(bottomNavigationView, navController);


//        BottomNavigationView bottomNavigationView = swapFeatureBinding.bottomNavigationView;
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.homeFragment:
//                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.homeFragment);
//                        navController = navHostFragment.getNavController();
//                    case R.id.commentFragment:
//                        NavHostFragment navHostFragment_1 = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.commentFragment);
//                        navController = navHostFragment_1.getNavController();
//                }
//                return false;
//            }
//        });

    }
}