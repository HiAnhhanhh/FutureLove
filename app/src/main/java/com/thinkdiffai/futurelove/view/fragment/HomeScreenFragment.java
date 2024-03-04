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
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.CustomMyOwnDialogFragmentBinding;
import com.thinkdiffai.futurelove.databinding.FragmentHomeScreenBinding;
import com.thinkdiffai.futurelove.model.comment.DetailUser;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.fragment.activity.SignInSignUpActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreenFragment extends Fragment {

    private int id_user;
    String email,nameUser;
    boolean check_state = true ;
    TextView headerView,headerEmail;
    CustomMyOwnDialogFragmentBinding customMyOwnDialogFragmentBinding;
    Dialog dialog_log_out;
    ImageView headerImageView;
    private FragmentHomeScreenBinding fragmentHomeScreenBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeScreenBinding = FragmentHomeScreenBinding.inflate(inflater,container, false);
        return fragmentHomeScreenBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initAction();
    }
    private void initAction() {

        fragmentHomeScreenBinding.swapImageVideoBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.videoSwapHomeFragment);
        });

        fragmentHomeScreenBinding.genImageFutureBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.pairingFragment);
        });

        fragmentHomeScreenBinding.genBabyBtn.setOnClickListener(v->{
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("swap_image",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("type_swap","gen_Baby");
            editor.apply();
            NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.swapGenImageFragment);
        });

        fragmentHomeScreenBinding.genTwoImageBtn.setOnClickListener(v->{
            SharedPreferences sharedPreferences_1 = requireActivity().getSharedPreferences("swap_image",0);
            SharedPreferences.Editor editor_1 = sharedPreferences_1.edit();
            editor_1.putString("type_swap","gen_two_image");
            editor_1.apply();
            NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.swapGenImageFragment);
        });
    }

    private void initData() {
        loadIdUser();
        loadProfileUser();
    }

    private void loadIdUser() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str","");
        if(id_user_str.equals("")){
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
        Log.d("check_share_id", "loadIdUser: "+ id_user );
    }
    private void loadProfileUser() {
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<DetailUser> call = apiService.getProfileUser(id_user);
        call.enqueue(new Callback<DetailUser>() {
            @Override
            public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
                if(response.isSuccessful()){
                    DetailUser detailUser = response.body();
                    nameUser = detailUser.getUser_name();
                    email = detailUser.getEmail();
                }
            }
            @Override
            public void onFailure(Call<DetailUser> call, Throwable t) {
                Log.d("check_profile", "onFailure: "+ t.getMessage());
            }
        });
    }
    private void initView() {
        fragmentHomeScreenBinding.drawLayoutBtn.setOnClickListener(v -> {
            fragmentHomeScreenBinding.drawerView.openDrawer(fragmentHomeScreenBinding.navView);
        });
        fragmentHomeScreenBinding.navView.getHeaderView(0).findViewById(R.id.imageViewUser).setOnClickListener( v -> {
            NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.profileUserFragment);
            fragmentHomeScreenBinding.drawerView.closeDrawer(fragmentHomeScreenBinding.navView);
        });

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("image_profile",0);
        check_state = sharedPreferences.getBoolean("check_state",true);
        if (check_state){
            headerImageView = fragmentHomeScreenBinding.navView.getHeaderView(0).findViewById(R.id.imageViewUser);
            Glide.with(this).load(R.drawable.baseline_account_circle_24).into(headerImageView);
        }else {
            loadHeader();
        }
        fragmentHomeScreenBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:{
                        NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.homeFragment);
                        fragmentHomeScreenBinding.drawerView.closeDrawer(fragmentHomeScreenBinding.navView);
                        return true;
                    }
                    case R.id.gen_baby:{
                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("swap_image",0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("type_swap","gen_Baby");
                        editor.apply();
                        NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.swapGenImageFragment);
                        fragmentHomeScreenBinding.drawerView.closeDrawer(fragmentHomeScreenBinding.navView);
                        return true;
                    }
                    case R.id.videoSwapHomeFragment :
                        NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.videoSwapHomeFragment);
                        fragmentHomeScreenBinding.drawerView.closeDrawer(fragmentHomeScreenBinding.navView);
                        return true;

                    case R.id.swap_Image:
                        SharedPreferences sharedPreferences_1 = requireActivity().getSharedPreferences("swap_image",0);
                        SharedPreferences.Editor editor_1 = sharedPreferences_1.edit();
                        editor_1.putString("type_swap","gen_two_image");
                        editor_1.apply();
                        NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.swapGenImageFragment);
                        fragmentHomeScreenBinding.drawerView.closeDrawer(fragmentHomeScreenBinding.navView);
                        return true;
                    
                    case R.id.logout:
                        fragmentHomeScreenBinding.drawerView.closeDrawer(fragmentHomeScreenBinding.navView);
                        clickLogout();
                        return true;

                    case R.id.change_password:
                        NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.changePasswordFragment);
                        fragmentHomeScreenBinding.drawerView.closeDrawer(fragmentHomeScreenBinding.navView);
                        return true;

                    case R.id.comment:
                        NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.commentFragment);
                        fragmentHomeScreenBinding.drawerView.closeDrawer(fragmentHomeScreenBinding.navView);
                        return true;

                    case R.id.pairingFragment:
                        NavHostFragment.findNavController(HomeScreenFragment.this).navigate(R.id.pairingFragment);
                        fragmentHomeScreenBinding.drawerView.closeDrawer(fragmentHomeScreenBinding.navView);
                        return true;
               }
                return false;
            }
        });
    }
    private void loadHeader() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("image_profile",0);
        String email = sharedPreferences.getString("email","");
        String name_user =sharedPreferences.getString("name_user","");
        String image_url = sharedPreferences.getString("your_url_profile","");
        Log.d("check_data_profile", "loadHeader: "+ email+ name_user+image_url);
        loadHeaderDetail(email,name_user,image_url);
    }
    private void loadHeaderDetail(String email, String name_user, String image_url) {
        headerView = fragmentHomeScreenBinding.navView.getHeaderView(0).findViewById(R.id.name_user);
        headerEmail = fragmentHomeScreenBinding.navView.getHeaderView(0).findViewById(R.id.email_user);
        headerImageView = fragmentHomeScreenBinding.navView.getHeaderView(0).findViewById(R.id.imageViewUser);
        headerView.setText(name_user);
        headerEmail.setText(email);
        Glide.with(this).load(image_url).into(headerImageView);
    }
    private void clickLogout() {
        openDialogLogout();
    }
    private void openDialogLogout() {
        customMyOwnDialogFragmentBinding = CustomMyOwnDialogFragmentBinding.inflate(LayoutInflater.from(requireActivity()));
        dialog_log_out = new Dialog(requireActivity());
        dialog_log_out.setContentView(customMyOwnDialogFragmentBinding.getRoot());
        dialog_log_out.show();
        dialog_log_out.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog_log_out.dismiss();
            }
        });
        customMyOwnDialogFragmentBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_log_out.dismiss();
            }
        });

        customMyOwnDialogFragmentBinding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLoginState();
                NavigateToLoginAndSignOutActivity();
            }
        });
    }
    private void changeLoginState() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LOGIN_STATE", false);
        editor.apply();
    }
    private void NavigateToLoginAndSignOutActivity() {
        Intent i = new Intent(getActivity(), SignInSignUpActivity.class);
        dialog_log_out.dismiss();
        startActivity(i);
    }
}