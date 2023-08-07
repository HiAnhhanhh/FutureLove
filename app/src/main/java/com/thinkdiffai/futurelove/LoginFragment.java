package com.thinkdiffai.futurelove;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.thinkdiffai.futurelove.databinding.FragmentLoginBinding;
import com.thinkdiffai.futurelove.view.activity.MainActivity;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // When click Login btn
        loginExecute();

        // When click Reset btn
        resetPasswordExecute();

        // When users haven't already had any account
        navToRegisterExecute();

        // When users want to see their password clearly
        showPasswordClearly();

    }

    private void showPasswordClearly() {
        binding.icShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    /*
    * USAGE: Show and Hide Password Edit Text
    * */
    private void togglePasswordVisibility() {
        EditText edtPassword = binding.edtPassword;
        if (edtPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
            // Show password
            edtPassword.setTransformationMethod(null);
            binding.icShowPassword.setImageResource(R.drawable.hide_password_icon); // Change to an icon for hiding the password
        } else {
            // Hide password
            edtPassword.setTransformationMethod(new PasswordTransformationMethod());
            binding.icShowPassword.setImageResource(R.drawable.eye_password_icon); // Change to an icon for showing the password
        }
        // Move the cursor to the end of the text to ensure it stays visible
        edtPassword.setSelection(edtPassword.getText().length());
    }

    private void loginExecute() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navToMainActivity();
            }
        });
    }

    private void resetPasswordExecute() {

    }

    private void navToRegisterExecute() {
        binding.btnNavRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navToRegisterFragment();
            }
        });
    }

    private void navToMainActivity() {
        boolean isLoginSuccess = true;
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra("LOGIN_SUCCESS", isLoginSuccess);
        startActivity(i);
    }

    private void navToRegisterFragment() {
        NavController nav = NavHostFragment.findNavController(this);
        nav.navigate(R.id.action_loginFragment_to_registerFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}