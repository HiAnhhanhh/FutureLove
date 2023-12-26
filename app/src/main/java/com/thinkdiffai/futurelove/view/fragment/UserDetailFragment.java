package com.thinkdiffai.futurelove.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.presenter.crud.DeleteAccount;
import com.thinkdiffai.futurelove.databinding.FragmentUserDetailBinding;
import com.thinkdiffai.futurelove.model.comment.CommentUser;
import com.thinkdiffai.futurelove.model.comment.DetailUser;
import com.thinkdiffai.futurelove.model.comment.UserComment;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.fragment.activity.MainActivity;
import com.thinkdiffai.futurelove.view.fragment.activity.SignInSignUpActivity;
import com.thinkdiffai.futurelove.view.adapter.UserCommentAdapter;
import com.thinkdiffai.futurelove.view.fragment.dialog.MyOwnDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailFragment extends Fragment {

    private FragmentUserDetailBinding binding;
    private List<CommentUser> commentList = new ArrayList<>();
    private UserCommentAdapter commentAdapter;

    private int id_user ;

    private MainActivity mainActivity;
    private KProgressHUD kProgressHUD;
    private LinearLayoutManager linearLayoutManager;
    private SharedPreferences sharedPreferences;

    private MyOwnDialogFragment myOwnDialogFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserDetailBinding.inflate(getLayoutInflater());
        // Initialize UI
        initUi();

        initData();
        // Load user details from API
        loadingUserDetailFromApi();
        // Load user comments from API
        loadingUserCommentsFromApi();
        return binding.getRoot();
    }

    private void initData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user", "");
        if (id_user_str == "") {
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
        Log.d("id_user_detail", "initData: "+ id_user);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userClickAnyButtonEventListener();
    }

    private void initUi() {
        // Create a loading widget from lib
        mainActivity = (MainActivity) getActivity();
        kProgressHUD = mainActivity.createHud();
        // Init Recycler view
        initializeRecyclerView();
    }

    private void loadingUserDetailFromApi() {
        try {
            getDataUserDetail();
        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }
    }

    private void loadingUserCommentsFromApi() {
        try {
            getDataCommentUser();
        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }
    }


    private void initializeRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL, false);
        binding.rcvPersonalComments.setLayoutManager(linearLayoutManager);
        commentAdapter = new UserCommentAdapter(commentList, new UserCommentAdapter.IOnClickItemListener() {
            @Override
            public void onClickItem(long idToanBoSuKien, int soThuTuSuKienCon) {
                // store id_toan_bo_su_kien to open the detail event
                mainActivity.eventSummaryCurrentId = idToanBoSuKien;
                mainActivity.soThuTuSuKien = soThuTuSuKienCon;
                showPersonalDetailEvent();
            }
        });
        binding.rcvPersonalComments.setAdapter(commentAdapter);
    }

    private void showPersonalDetailEvent() {
        NavHostFragment.findNavController(UserDetailFragment.this).navigate(R.id.action_userDetailFragment_to_timelineFragment);
    }

    private void getDataCommentUser() {
//        if (!kProgressHUD.isShowing()) {
//            kProgressHUD.show();
//        }
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<UserComment> call = apiService.getCommentUser(id_user);

        call.enqueue(new Callback<UserComment>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<UserComment> call, Response<UserComment> response) {
                if(id_user==0){
                    commentAdapter.setData(commentList);
                } else if (response.isSuccessful() && response.body() != null) {
                    UserComment userComment = response.body();
                    Log.d("check_comment_user", "onResponse: "+ id_user );
                    commentList = userComment.getComment_user();
                    if (!commentList.isEmpty()) {
                        commentAdapter.setData(commentList);
                        commentAdapter.notifyDataSetChanged();
                    }
                }
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserComment> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
                Log.d("check_user_comment", "onFailure: "+ t.getMessage());
//                if (kProgressHUD.isShowing()) {
//                    kProgressHUD.dismiss();
//                }
            }
        });
    }

    private void getDataUserDetail() {
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        // Id for test
        Call<DetailUser> call = apiService.getProfileUser(id_user);
        Log.d("check_user", "getDataUserDetail: "+ call.toString());
        call.enqueue(new Callback<DetailUser>() {
            @Override
            public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
                if (response.isSuccessful()) {
                    DetailUser detailUsers = response.body();
                    Log.d("check_user", "onResponse: "+ detailUsers.getUser_name() + detailUsers.getId_user()+ detailUsers.getEmail());
                    binding.tvEmailUser.setText(String.valueOf(Objects.requireNonNull(detailUsers).getEmail()));
                    binding.tvUserName.setText(String.valueOf(Objects.requireNonNull(detailUsers).getUser_name()));
                    binding.tvUserEventsNumber.setText(String.valueOf(detailUsers.getCount_sukien()));
                    binding.tvUserCommentNumber.setText(String.valueOf(detailUsers.getCount_comment()));
                    binding.tvUserViewNumber.setText(String.valueOf(detailUsers.getCount_view()));
                    Glide.with(requireActivity()).load(detailUsers.getLink_avatar()).into(binding.imgUserAvatar);
                } else {
                    binding.tvUserName.setText("null");
                    binding.tvUserEventsNumber.setText("null");
                    binding.tvUserCommentNumber.setText("null");
                    binding.tvUserViewNumber.setText("null");
                }
            }

            @Override
            public void onFailure(Call<DetailUser> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHomeFragment() {
        NavHostFragment.findNavController(UserDetailFragment.this).navigate(R.id.action_userDetailFragment_to_homeFragment);
    }


    private void userClickAnyButtonEventListener() {
        clickComeBack();
        clickLogout();
        binding.btnShowDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickShowDropDown();
            }
        });
    }

    private void clickShowDropDown() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), binding.btnShowDropDown);
        popupMenu.getMenuInflater().inflate(R.menu.drop_down_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_logout:
                        // Show dialog to ask logout confirmation
                        myOwnDialogFragment = new MyOwnDialogFragment(
                                "Log-out now?",
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
                        myOwnDialogFragment.show(mainActivity.getSupportFragmentManager(), "logout_dialog");
                        return true;
                    case R.id.item_delete_account:
                        Toast.makeText(mainActivity, "Delete Account Processing...", Toast.LENGTH_SHORT).show();
                        deleteAccountProcess(99L);
                        return true;

                    case R.id.item_change_password:
                        Toast.makeText(mainActivity, "Change Password Processing...", Toast.LENGTH_SHORT).show();
                        Dialog_PassWord();
                        return true;

                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    private void deleteAccountProcess(long l) {
        DeleteAccount.getInstance().deleteAccount(requireContext(), l);
    }


    private void clickComeBack() {
        binding.btnComeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mainActivity.homeToUserDetail) {
                    navigateToHomeFragment();
                    mainActivity.homeToUserDetail = false;
                } else if (mainActivity.commentToUserDetail) {
                    navigateToCommentFragment();
                    mainActivity.commentToUserDetail = false;
                } else if (mainActivity.pairingToUserDetail) {
                    navigateToPairingFragment();
                    mainActivity.pairingToUserDetail = false;
                }

            }
        });
    }

    private void navigateToPairingFragment() {
        NavHostFragment.findNavController(UserDetailFragment.this).navigate(R.id.action_userDetailFragment_to_pairingFragment);
    }

    private void navigateToCommentFragment() {
        NavHostFragment.findNavController(UserDetailFragment.this).navigate(R.id.action_userDetailFragment_to_commentFragment);
    }

//    private void clickLogout() {
//        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Show dialog to ask logout confirmation
//                myOwnDialogFragment = new MyOwnDialogFragment(
//                        "Log-out now?",
//                        "Are you sure to quit the app?",
//                        R.drawable.ic_warning,
//                        new MyOwnDialogFragment.MyOwnDialogListener() {
//                            @Override
//                            public void onConfirm() {
//                                // Navigate to Login Fragment
//                                NavigateToLoginAndSignOutActivity();
//                                // Update the LOGIN_STATE
//                                // Change the LOGIN_STATE is FALSE - Not to keep the login state
//                                changeLoginState();
//                            }
//                        }
//                );
//                myOwnDialogFragment.show(mainActivity.getSupportFragmentManager(), "logout_dialog");
//            }
//        });
//    }

    private void changeLoginState() {
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LOGIN_STATE", false);
        editor.apply();
    }

    private void NavigateToLoginAndSignOutActivity() {
        Intent i = new Intent(getActivity(), SignInSignUpActivity.class);
        startActivity(i);
    }

    private void clickLogout() {
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show dialog to ask logout confirmation
                myOwnDialogFragment = new MyOwnDialogFragment(
                        "Log-out now?",
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
                myOwnDialogFragment.show(mainActivity.getSupportFragmentManager(), "logout_dialog");
            }
        });
    }
//    private void changeLoginState() {
//        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("LOGIN_STATE", false);
//        editor.apply();
//    }
//
//    private void NavigateToLoginAndSignOutActivity() {
//        Intent i = new Intent(getActivity(), SignInSignUpActivity.class);
//        startActivity(i);
//    }
    // xu ly menu tu chon
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_detail    , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Handle menu item selections
        switch (id) {
            case R.id.action_edit:
                Dialog_PassWord();
                // Handle Edit
                return true;
            case R.id.action_delete:
                // Handle Delete
                return true;
            case R.id.action_login:
                clickLogout();
                initUi();
                // Handle login

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void Dialog_PassWord(){
        final Dialog dialog= new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_password);
        Window window= dialog.getWindow();
        if(window== null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes= window.getAttributes();
        windowAttributes.gravity= Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);
        // anh xa dialog
        AppCompatEditText newPassWord,oldPassWord,newPassWord1;
        Button done;
        newPassWord= dialog.findViewById(R.id.etnewPassword);
        oldPassWord=dialog.findViewById(R.id.etOldPassword);
        newPassWord1=dialog.findViewById(R.id.etConfirmPassword);
        done=dialog.findViewById(R.id.btnChangePassword);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pw,pw1;
                pw=newPassWord.getText().toString();
                pw1=newPassWord1.getText().toString();
                if(pw.contains(pw1)){
                    if(isValidPassword(newPassWord.getText().toString())){

                        ChangePassWordAPi(20,binding.tvUserName.getText().toString(),oldPassWord.getText().toString(),newPassWord.getText().toString());
                        dialog.dismiss();
                        Toast.makeText(mainActivity, "Change Password Successfully!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mainActivity, "Change Password Incompletely!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mainActivity, "Invalid Confirm Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //
        dialog.show();
    }
    private  void ChangePassWordAPi(long id,String name,String oldpassword,String newpassword){
        // Call login Api
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<Object> call = apiService.Change_Password(id,oldpassword,newpassword);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.d("check_password", "onResponse: oke");
                } else {
                    Toast.makeText(getContext(), ""+response.body().toString(), Toast.LENGTH_SHORT).show();
                    // Xử lý lỗi khi API trả về mã lỗi
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // check password
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
}