package com.thinkdiffai.futurelove.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ActivitySignInSignUpBinding;

public class SignInSignUpActivity extends AppCompatActivity {

    private ActivitySignInSignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}