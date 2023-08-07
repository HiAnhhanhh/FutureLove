package com.thinkdiffai.futurelove;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.thinkdiffai.futurelove.databinding.DialogRegistrationSuccessBinding;
import com.thinkdiffai.futurelove.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private DialogRegistrationSuccessBinding dialogBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // When click on Register Button
        clickOnRegisterBtn();
        // When click on Login Button
        clickOnLoginBtn();
        // When users want to see password clearly or not
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

    private void clickOnRegisterBtn() {
        // show dialog
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegistrationSuccessDialog();
            }
        });
    }

    private void showRegistrationSuccessDialog() {

        dialogBinding = DialogRegistrationSuccessBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(true);

        // Customize dialog properties (optional)
        // For example, you can set the dialog's width and height
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        dialogBinding.btnLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(RegisterFragment.this);
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
                dialog.dismiss(); // Close the dialog
            }
        });

        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void clickOnLoginBtn() {
        binding.btnAskBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(RegisterFragment.this);
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}