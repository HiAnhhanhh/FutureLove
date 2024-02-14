package com.thinkdiffai.futurelove.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingBinding;
import com.thinkdiffai.futurelove.databinding.DialogErrorBinding;
import com.thinkdiffai.futurelove.databinding.FragmentChangePasswordBinding;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.fragment.activity.SignInSignUpActivity;
import com.thinkdiffai.futurelove.view.fragment.dialog.MyOwnDialogFragment;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    FragmentChangePasswordBinding fragmentChangePasswordBinding;
    Call<Object> callChangePassword;

    CustomDialogLoadingBinding customDialogLoadingBinding;

    DialogErrorBinding dialogErrorBinding;
    Dialog dialog,dialog_error;
    String token_auth;
    String oldPassword, newPassword;
    int id_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentChangePasswordBinding = FragmentChangePasswordBinding.inflate(LayoutInflater.from(requireActivity()),container,false);
        return fragmentChangePasswordBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createDialog();
        initData();
        initAction();
    }

    private void createDialog() {
        customDialogLoadingBinding = CustomDialogLoadingBinding.inflate(LayoutInflater.from(requireActivity()));
        dialog = new Dialog(requireActivity());
        dialog.setContentView(customDialogLoadingBinding.getRoot());

        dialogErrorBinding = DialogErrorBinding.inflate(LayoutInflater.from(requireActivity()));
        dialog_error = new Dialog(requireActivity());
        dialog_error.setContentView(dialogErrorBinding.getRoot());

    }

    private void initAction() {
        fragmentChangePasswordBinding.icShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText new_edtPassword = fragmentChangePasswordBinding.edtNewPassword;

                if (new_edtPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    // Show password
                    new_edtPassword.setTransformationMethod(null);
                    fragmentChangePasswordBinding.icShowPassword.setImageResource(R.drawable.hide_password_black); // Change to an icon for hiding the password
                } else {
                    // Hide password
                    new_edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    fragmentChangePasswordBinding.icShowPassword.setImageResource(R.drawable.display_password); // Change to an icon for showing the password
                }
            }
        });

        fragmentChangePasswordBinding.icShowPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText old_edtPassword = fragmentChangePasswordBinding.edtOldPassword;
                if (old_edtPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    // Show password
                    old_edtPassword.setTransformationMethod(null);
                    fragmentChangePasswordBinding.icShowPassword1.setImageResource(R.drawable.hide_password_black); // Change to an icon for hiding the password
                } else {
                    // Hide password
                    old_edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    fragmentChangePasswordBinding.icShowPassword1.setImageResource(R.drawable.display_password); // Change to an icon for showing the password
                }
            }
        });

        fragmentChangePasswordBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                customDialogLoadingBinding.titleDialog.setText("Updating Password, Please Wait ...");
                openDialogLoading();
                postData();
            }
        });
    }

    private void openDialogLoading() {
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                callChangePassword.cancel();
            }
        });

        customDialogLoadingBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callChangePassword.cancel();
                dialog.dismiss();
            }
        });
    }

    private void initData() {
        loadIdUser();
    }

    private void getData() {
        oldPassword = fragmentChangePasswordBinding.edtOldPassword.getText().toString();
        newPassword = fragmentChangePasswordBinding.edtNewPassword.getText().toString();
        if(oldPassword.equals("") || newPassword.equals("")){
            Toast.makeText(getActivity(), "Enter password", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadIdUser() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str","");
        token_auth = sharedPreferences.getString("token","");
        Log.d("check_share_id", "loadIdUser: "+ id_user_str +"_" +token_auth);
        if(id_user_str.equals("")){
            id_user =0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }
    private void postData() {
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Log.d("change_password", "onResponse: "+ id_user+ oldPassword+ newPassword+ token_auth);
        callChangePassword = apiService.Change_Password(id_user,"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MDg1ODkyOTgsInVzZXJuYW1lIjoiaG9hbmdhbmgxMjQzNTZAZ21haWwuY29tIn0.jZOEW5o6Y3Uh04jtVbMT1fsU4fisi2dyFGl2PWgW5Ic",oldPassword,newPassword);
        callChangePassword.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful() && response!=null){
                    NavToLoginFragment();
                }else {
                    dialog.dismiss();
                    openDialogError();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        }
    private void openDialogError() {
        dialogErrorBinding.tvError.setText("Password Change Failed");
        dialog_error.show();
        dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                callChangePassword.cancel();
            }
        });

        dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callChangePassword.cancel();
                dialog_error.dismiss();
            }
        });
    }

    private void NavToLoginFragment() {
        MyOwnDialogFragment myOwnDialogFragment = new MyOwnDialogFragment(
                "Change Password now?",
                "Are you sure to quit the app?",
                R.drawable.ic_warning,
                new MyOwnDialogFragment.MyOwnDialogListener() {
                    @Override
                    public void onConfirm() {
                        // Navigate to Login Fragment
                        NavigateToLoginAndSignOutActivity();
                        // Update the LOGIN_STATE
                        // Change the LOGIN_STATE is FALSE - Not to keep the login state
                        changeLoginState();
                    }
                }
        );
        myOwnDialogFragment.show(getActivity().getSupportFragmentManager(), "change_password");
    }

    private void changeLoginState() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LOGIN_STATE", false);
        editor.apply();
    }

    private void NavigateToLoginAndSignOutActivity() {
        Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getActivity(), SignInSignUpActivity.class);
        startActivity(i);
    }
}