package com.thinkdiffai.futurelove.view.fragment.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.thinkdiffai.futurelove.databinding.ActivitySignInSignUpBinding;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class SignInSignUpActivity extends AppCompatActivity {

    private ActivitySignInSignUpBinding binding;

    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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