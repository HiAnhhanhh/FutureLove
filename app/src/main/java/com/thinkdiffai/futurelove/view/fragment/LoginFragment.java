package com.thinkdiffai.futurelove.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentLoginBinding;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.QueryValueCallback;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.activity.MainActivity;
import com.thinkdiffai.futurelove.view.activity.SignInSignUpActivity;
import com.thinkdiffai.futurelove.view.fragment.dialog.MyOwnDialogFragment;

import java.util.Objects;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private SignInSignUpActivity signInSignUpActivity;
    private KProgressHUD kProgressHUD;

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        signInSignUpActivity = (SignInSignUpActivity) getActivity();
        kProgressHUD = signInSignUpActivity.createHud();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up TextWatcher to see that edit texts are valid or not and will show alerts or nothing
        setUpTextWatcher();

        // When click Login btn
        loginExecute();

        // When click Reset btn
        resetPasswordExecute();

        // When users haven't already had any account
        navToRegisterExecute();

        // When users want to see their password clearly
        showPasswordClearly();

    }

    private void setUpTextWatcher() {
        binding.edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailAlertVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordAlertVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void emailAlertVisibility() {
        String email = Objects.requireNonNull(binding.edtUserName.getText()).toString();
        boolean isValidEmail = isValidEmail(email);
        binding.tvUserNameAlert.setVisibility(isValidEmail ? View.GONE : View.VISIBLE);
    }

    private void passwordAlertVisibility() {
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
        boolean isValidPassword = isValidPassword(password);
        binding.tvPasswordAlert.setVisibility(isValidPassword ? View.GONE : View.VISIBLE);
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
                String email = String.valueOf(binding.edtUserName.getText());
                String password = String.valueOf(binding.edtPassword.getText());
                // Check that it is full of needed information
                if (isCompletedInformation(email, password)) {
                    // Call API and Login
                    checkAccountRegistered(email, password);
                } else {
                    showFailedLoginDialog();
                }
            }
        });
    }

    private void checkAccountRegistered(String email, String password) {
        // Call back function to listen the response value being returned
        callLoginApi(new QueryValueCallback() {
            @Override
            public void onQueryValueReceived(String queryValue) {
                if (!queryValue.contains("{ketqua=")){
                    navToMainActivity();
                    Log.d("PHONG", "queryValue = " + queryValue);

                    // Store LOGIN STATE not to login again
                    sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("LOGIN_STATE", true);
                    editor.apply();
                } else{
                    showErrorLoginDialog();
                }
            }
            @Override
            public void onApiCallFailed(Throwable t) {
            }
        }, email, password);
    }

    private void showFailedLoginDialog() {
        MyOwnDialogFragment myOwnDialogFragment = new MyOwnDialogFragment();
        myOwnDialogFragment.setDialogTitle("Incomplete Form");
        myOwnDialogFragment.setDialogMessage("Click \"YES\" for Register now!");
        myOwnDialogFragment.setImageSrc(R.drawable.ic_alert);
        myOwnDialogFragment.setListener(new MyOwnDialogFragment.MyOwnDialogListener() {
            @Override
            public void onConfirm() {
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
        myOwnDialogFragment.show((signInSignUpActivity).getSupportFragmentManager(), "login_dialog");
    }

    private void showErrorLoginDialog() {
        MyOwnDialogFragment myOwnDialogFragment = new MyOwnDialogFragment();
        myOwnDialogFragment.setDialogTitle("Invalid Account");
        myOwnDialogFragment.setDialogMessage("Click \"YES\" for Register now!");
        myOwnDialogFragment.setImageSrc(R.drawable.ic_alert);
        myOwnDialogFragment.setListener(new MyOwnDialogFragment.MyOwnDialogListener() {
            @Override
            public void onConfirm() {
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
        myOwnDialogFragment.show((signInSignUpActivity).getSupportFragmentManager(), "login_dialog");
    }

    private void callLoginApi(QueryValueCallback callback, String email, String password) {
        if (!kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
        // Call login Api
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<Object> call = apiService.login(email, password);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful() && response.body() != null) {

                    callback.onQueryValueReceived(response.body().toString());
                    Log.d("PHONG", "callback: " + response.body().toString());
                }
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
        });
    }

    private boolean isCompletedInformation(String email, String password) {

        if (isValidEmail(email) && isValidPassword(password)) {
            return true;
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && !containSpecialCharacters(password);
    }

    private boolean containSpecialCharacters(String password) {
        // Define a list of special characters that are not allowed
        String specialCharacters = "~`!@#$%^&*()+={}[]|\\:;\"<>,.?/ ";
        for (int i = 0; i < password.length(); i++) {
            if (specialCharacters.contains(String.valueOf(password.charAt(i)))) {
                return true; // Found any special character
            }
        }
        return false;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    // Child Classes
    private void navToRegisterFragment() {
        NavController nav = NavHostFragment.findNavController(this);
        nav.navigate(R.id.action_loginFragment_to_registerFragment);
    }


}