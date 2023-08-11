package com.thinkdiffai.futurelove.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentUserDetailBinding;
import com.thinkdiffai.futurelove.model.comment.CommentUser;
import com.thinkdiffai.futurelove.model.comment.DetailUser;
import com.thinkdiffai.futurelove.model.comment.UserComment;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.activity.MainActivity;
import com.thinkdiffai.futurelove.view.activity.SignInSignUpActivity;
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
        // Load user details from API
        loadingUserDetailFromApi();
        // Load user comments from API
        loadingUserCommentsFromApi();
        return binding.getRoot();
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
            public void onClickItem(int idToanBoSuKien, int soThuTuSuKienCon) {
                // store id_toan_bo_su_kien to open the detail event
                mainActivity.eventSummaryCurrentId = idToanBoSuKien;
                mainActivity.soThuTuSuKien = soThuTuSuKienCon;
                showPersonalDetailEvent();
            }
        });
        binding.rcvPersonalComments.setAdapter(commentAdapter);
    }

    private void showPersonalDetailEvent() {
        NavHostFragment.findNavController(UserDetailFragment.this).navigate(R.id.action_userDetailFragment_to_timeLineFragment);
    }

    private void getDataCommentUser() {
        if (!kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<UserComment> call = apiService.getCommentUser(5);
        call.enqueue(new Callback<UserComment>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<UserComment> call, Response<UserComment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserComment userComment = response.body();
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
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
        });
    }

    private void getDataUserDetail() {

        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        // Id for test
        Call<DetailUser> call = apiService.getProfileUser(5);
        call.enqueue(new Callback<DetailUser>() {
            @Override
            public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
                if (response.isSuccessful()) {
                    DetailUser detailUsers = response.body();
                    binding.tvUserName.setText(String.valueOf(Objects.requireNonNull(detailUsers).getUser_name()));
                    binding.tvUserEventsNumber.setText(String.valueOf(detailUsers.getCount_sukien()));
                    binding.tvUserCommentNumber.setText(String.valueOf(detailUsers.getCount_comment()));
                    binding.tvUserViewNumber.setText(String.valueOf(detailUsers.getCount_view()));
                    Glide.with(Objects.requireNonNull(getActivity())).load(detailUsers.getLink_avatar()).into(binding.imgUserAvatar);
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
        clickCancel();
        clickLogout();
    }

    private void clickCancel() {
        binding.btnCancelUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHomeFragment();
            }
        });
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

    private void changeLoginState() {
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LOGIN_STATE", false);
        editor.apply();
    }

    private void NavigateToLoginAndSignOutActivity() {
        Intent i = new Intent(getActivity(), SignInSignUpActivity.class);
        startActivity(i);
    }
}