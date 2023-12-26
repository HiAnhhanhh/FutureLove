package com.thinkdiffai.futurelove.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.DialogRegistrationSuccessBinding;
import com.thinkdiffai.futurelove.databinding.FragmentRegisterBinding;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.QueryValueCallback;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.fragment.activity.SignInSignUpActivity;
import com.thinkdiffai.futurelove.view.fragment.dialog.MyOwnDialogFragment;

import java.util.Objects;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private DialogRegistrationSuccessBinding dialogBinding;
    private SignInSignUpActivity signInSignUpActivity;
    private KProgressHUD kProgressHUD;
    private final String MY_OWN_TAG = "PHONG";


    private final String EXISTED_ACCOUNT_STR = "{message=Account already exists!}";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        signInSignUpActivity = (SignInSignUpActivity) getActivity();
        kProgressHUD = signInSignUpActivity.createHud();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up TextWatcher to see that edit texts are valid or not and will show alerts or nothing
        setUpTextWatcher();
        // When click on Register Button
        clickOnRegisterBtn();
        // When click on Login Button
        clickOnLoginBtn();
        // When users want to see password clearly or not
        showPasswordClearly();
    }

    private void setUpTextWatcher() {
        binding.edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameAlertVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.edtEmail.addTextChangedListener(new TextWatcher() {
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
//                resizeLayoutComment();
                passwordAlertVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void passwordAlertVisibility() {
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
        boolean isValidPassword = isValidPassword(password);
        Log.d(MY_OWN_TAG, "isValidPassword: " + isValidPassword);
        binding.tvPasswordAlert.setVisibility(isValidPassword ? View.GONE : View.VISIBLE);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && !containSpecialCharacters(password);
    }

    private void emailAlertVisibility() {
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString();
        boolean isValidEmail = isValidEmail(email);
        Log.d(MY_OWN_TAG, "isValidEmail: " + isValidEmail);
        binding.tvEmailAlert.setVisibility(isValidEmail ? View.GONE : View.VISIBLE);
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void usernameAlertVisibility() {
        String username = Objects.requireNonNull(binding.edtUserName.getText()).toString();
        boolean isValidUserName = isValidUserName(username);
        Log.d(MY_OWN_TAG, "isValidUserName: " + isValidUserName);
        binding.tvUserNameAlert.setVisibility(isValidUserName ? View.GONE : View.VISIBLE);
    }

    private boolean isValidUserName(String username) {
        return username.length() >= 8 && !containSpecialCharacters(username);
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
        closeKeyboard();
        Log.i(MY_OWN_TAG, "clickOnRegisterBtn");
        // show dialog
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Step 1: Check valid forms
                String userName = String.valueOf(binding.edtUserName.getText());
                String email = String.valueOf(binding.edtEmail.getText());
                String password = String.valueOf(binding.edtPassword.getText());

                // Check that it is full of needed information
                if (isCompletedInformation(userName, email, password)) {
                    // Step 2: Call API for checking and Login
                    checkAccountRegistered(userName, email, password);
                } else {
                    showInCompleteFormRegisterDialog();
                }
            }
        });
    }

    private void checkAccountRegistered(String username, String email, String password) {
        Log.i(MY_OWN_TAG, "isCompletedInformation");
        // Call back function to listen the response value being returned
        callSignUpApi(new QueryValueCallback() {
            @Override
            public void onQueryValueReceived(String queryValue) {

                // Check that: the account is existed or not
                if (!queryValue.contains(EXISTED_ACCOUNT_STR)){
                    Log.d("PHONG", "queryValue = " + queryValue);
                    Dialog successDialog = showRegistrationSuccessDialog();
                    // After 1.5s, navigate to login fragment
                    new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            successDialog.dismiss();
                            navToLoginFragment();
                        }
                    }, 5000);
                } else{
                    MyOwnDialogFragment ownDialogFragment = new MyOwnDialogFragment("Existed Account", "Would you like to login now?", R.drawable.ic_warning);
                    ownDialogFragment.setListener(new MyOwnDialogFragment.MyOwnDialogListener() {
                        @Override
                        public void onConfirm() {
                            navToLoginFragment();
                        }
                    });
                    ownDialogFragment.show(signInSignUpActivity.getSupportFragmentManager(), "register_dialog");
                }
            }
            @Override
            public void onApiCallFailed(Throwable t) {
            }
        }, email, password);
    }

    private void navToLoginFragment() {
        NavHostFragment.findNavController(RegisterFragment.this).navigate(R.id.action_registerFragment_to_loginFragment);
    }

    private void callSignUpApi(QueryValueCallback callback, String email, String password) {
        Log.i(MY_OWN_TAG, "callSignUpApi");
        if (!kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
        // Call login Api
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Log.i(MY_OWN_TAG, "API Service: " + apiService);
        Call<Object> call = apiService.signUp(email, password,"abc.jpg", "1.1.1.1", "Nokia 1280");
        Log.d("PHONG", "api call: " + call.toString());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("Phong", "onResponse: "+ response.body());
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(MY_OWN_TAG, "response.body() calling");
                    callback.onQueryValueReceived(response.body().toString());
                    Log.d("PHONG", "callback: " + response.body());
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

    private boolean isCompletedInformation(String userName, String email, String password) {
        Log.i(MY_OWN_TAG, "isCompletedInformation");
        if (isValidUserName(userName) && isValidEmail(email) && isValidPassword(password)) {
            return true;
        }
        return false;
    }

    private void showFailedRegisterDialog() {
        MyOwnDialogFragment myOwnDialogFragment = new MyOwnDialogFragment();
        myOwnDialogFragment.setDialogTitle("Register Failed");
        myOwnDialogFragment.setDialogMessage("Would you like to login now?");
        myOwnDialogFragment.setImageSrc(R.drawable.ic_alert);
        myOwnDialogFragment.setListener(new MyOwnDialogFragment.MyOwnDialogListener() {
            @Override
            public void onConfirm() {
                navToLoginFragment();
            }
        });
        myOwnDialogFragment.show((signInSignUpActivity).getSupportFragmentManager(), "register_dialog");
    }

    private void showInCompleteFormRegisterDialog() {
        MyOwnDialogFragment myOwnDialogFragment = new MyOwnDialogFragment();
        myOwnDialogFragment.setDialogTitle("Incomplete Register Form");
        myOwnDialogFragment.setDialogMessage("If you have any account\nClick \"YES\" to Login");
        myOwnDialogFragment.setImageSrc(R.drawable.ic_alert);
        myOwnDialogFragment.setListener(new MyOwnDialogFragment.MyOwnDialogListener() {
            @Override
            public void onConfirm() {
                navToLoginFragment();
            }
        });
        myOwnDialogFragment.show((signInSignUpActivity).getSupportFragmentManager(), "register_dialog");
    }

    private Dialog showRegistrationSuccessDialog() {
        dialogBinding = DialogRegistrationSuccessBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(true);
        // Customize dialog properties (optional)
        // For example, you can set the dialog's width and height
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        return dialog;
    }

    private void clickOnLoginBtn() {
        binding.btnAskBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navToLoginFragment();
            }
        });
    }

    private void resizeLayoutComment() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.btnAskBackToLogin.getLayoutParams();
        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        binding.btnAskBackToLogin.setLayoutParams(layoutParams);
    }

    private void closeKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}