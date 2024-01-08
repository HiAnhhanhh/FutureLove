package com.thinkdiffai.futurelove.view.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thinkdiffai.futurelove.databinding.DialogBottomSheetSelectedHomeBinding;
import com.thinkdiffai.futurelove.databinding.FragmentHomeSwapImageBinding;
import com.thinkdiffai.futurelove.databinding.FragmentSwapGenImageBinding;
import com.thinkdiffai.futurelove.model.GenBabyModel;
import com.thinkdiffai.futurelove.model.GenImageModel;
import com.thinkdiffai.futurelove.model.IpNetworkModel;
import com.thinkdiffai.futurelove.model.ListImageUploadModel;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.RetrofitIp;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.ImageUploadAdapter;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwapGenImageFragment extends Fragment {

    private BottomSheetDialog bottomSheetDialog;

    String selectedImage1Uri;

    String deviceName;

    String uriResponseBaby;

    String ip_them_su_kien;


    int picked_1 = 0;

    int picked_2 = 0 ;
    LinearLayoutManager linearLayoutManager;


    private ImageUploadAdapter imageUploadAdapter;


    private ArrayList<String> listImageUpload;

    String selectedImage2Uri;

    String typeGenImage;

    String token_auth;
    int id_user;

    String uriResponse1;

    String uriResponse2;


    int check = 0;

    private DialogBottomSheetSelectedHomeBinding dialogBinding;


    private static final int PERMISSION_REQUEST_CODE = 2;

    FragmentSwapGenImageBinding fragmentSwapGenBabyBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSwapGenBabyBinding = FragmentSwapGenImageBinding.inflate(inflater,container,false);
        return fragmentSwapGenBabyBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAction();
        initData();
    }

    private void initData() {
        loadIdUser();
        loadImageUpload();
        deviceName = getDeviceName();
        callApiAddress();
        loadTypeImage();
        loadSwapImage();
    }

    private void loadSwapImage() {
        if(typeGenImage.equals("GenBaby")){
            fragmentSwapGenBabyBinding.textView.setText("Gen Baby");
        }else{
            fragmentSwapGenBabyBinding.textView.setText("Gen 2 Image");
        }
    }

    private void loadTypeImage() {
        Bundle args = getArguments();
        typeGenImage = args.getString("genSuKien");
    }

    private void callApiAddress() {
        ApiService apiService = RetrofitIp.getInstance(Server.GET_CITY_NAME_FROM_IP).getRetrofit().create(ApiService.class);
        Call<IpNetworkModel> call = apiService.getIpApiResponse();
        call.enqueue(new Callback<IpNetworkModel>() {
            @Override
            public void onResponse(Call<IpNetworkModel> call, Response<IpNetworkModel> response) {
                if(response.body()!= null && response.isSuccessful()){
                    Log.d("check_ip", "onResponse: "+ response.body().getIp());
                    ip_them_su_kien = response.body().getIp();
                }
            }

            @Override
            public void onFailure(Call<IpNetworkModel> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private void initAction() {
        fragmentSwapGenBabyBinding.pickImage1Btn.setOnClickListener(v -> {
            fragmentSwapGenBabyBinding.textImage.setText("Image 1");
            fragmentSwapGenBabyBinding.imageIv2.setVisibility(View.GONE);
            fragmentSwapGenBabyBinding.imageIv1.setVisibility(View.VISIBLE);
            if(picked_1 == 0){
                picked_1=1;
                check =2;
            }
            openDialog();
        });
        fragmentSwapGenBabyBinding.pickImage2Btn.setOnClickListener(v -> {
            fragmentSwapGenBabyBinding.textImage.setText("Image 2");
            fragmentSwapGenBabyBinding.imageIv1.setVisibility(View.GONE);
            fragmentSwapGenBabyBinding.imageIv2.setVisibility(View.VISIBLE);
            if(picked_2 == 0){
                picked_2=1;
                check =1;
            }
            openDialog();
        });

        fragmentSwapGenBabyBinding.createVideoBtn.setOnClickListener(v ->{
            if(picked_1 ==0 && picked_2 ==0){
                Toast.makeText(getActivity(), "Choose Image", Toast.LENGTH_SHORT).show();
            }else if(typeGenImage.equals("Gen_2_Image")){
                genImageSwap(uriResponse1,uriResponse2);
                fragmentSwapGenBabyBinding.view.setVisibility(View.VISIBLE);
                fragmentSwapGenBabyBinding.progressBar.setVisibility(View.VISIBLE);
            }else{
                genSuKienBaby(uriResponse1,uriResponse2);
                fragmentSwapGenBabyBinding.view.setVisibility(View.VISIBLE);
                fragmentSwapGenBabyBinding.progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void genImageSwap(String uriResponse1, String uriResponse2) {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Call<GenImageModel> call = apiService.getUrlImageSwap("Bearer "+ token_auth,uriResponse1,uriResponse2,deviceName,ip_them_su_kien,id_user);
        call.enqueue(new Callback<GenImageModel>() {
            @Override
            public void onResponse(Call<GenImageModel> call, Response<GenImageModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    uriResponseBaby = response.body().getSuKienImageModel().getLink_da_swap();
                    check = 0;
                    picked_2=0;
                    picked_1=0;
                    Log.d("check_genSK_baby", "onResponse: "+ uriResponseBaby + response.body().getSuKienImageModel().getLink_da_swap());
                    fragmentSwapGenBabyBinding.view.setVisibility(View.GONE);
                    fragmentSwapGenBabyBinding.progressBar.setVisibility(View.GONE);
                    SwapGenImageFragmentDirections .ActionSwapGenBabyFragmentToGenBabyResultFragment action = SwapGenImageFragmentDirections.actionSwapGenBabyFragmentToGenBabyResultFragment(uriResponseBaby);
                    NavHostFragment.findNavController(SwapGenImageFragment.this).navigate(action);
                }else {
                    Log.d("check_genSK_baby", "onResponse: fail");
                }
            }
            @Override
            public void onFailure(Call<GenImageModel> call, Throwable t) {

            }
        });
    }

    private void genSuKienBaby(String uriResponse1, String uriResponse2) {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_genSK_baby", "genSuKienBaby: "+ token_auth + uriResponse1+ uriResponse2+ deviceName+ ip_them_su_kien + id_user);
        Call<GenBabyModel> call = apiService.getUrlImageBaby("Bearer "+ token_auth,uriResponse1,uriResponse2,deviceName,ip_them_su_kien,id_user);
        call.enqueue(new Callback<GenBabyModel>() {
            @Override
            public void onResponse(@NonNull Call<GenBabyModel> call, @NonNull Response<GenBabyModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    uriResponseBaby = response.body().getGenBabyModel().get(0).getLink_da_swap();
                    check = 0;
                    picked_2=0;
                    picked_1=0;
                    Log.d("check_genSK_baby", "onResponse: "+ uriResponseBaby + response.body().getGenBabyModel().get(0).getId_image());
                    fragmentSwapGenBabyBinding.view.setVisibility(View.GONE);
                    fragmentSwapGenBabyBinding.progressBar.setVisibility(View.GONE);
                    SwapGenImageFragmentDirections.ActionSwapGenBabyFragmentToGenBabyResultFragment action = SwapGenImageFragmentDirections.actionSwapGenBabyFragmentToGenBabyResultFragment(uriResponseBaby);
                    NavHostFragment.findNavController(SwapGenImageFragment.this).navigate(action);
                }else {
                    Log.d("check_genSK_baby", "onResponse: fail");
                }
            }
            @Override
            public void onFailure(@NonNull Call<GenBabyModel> call, @NonNull Throwable t) {
                Log.d("check_genSK_baby", "onFailure: "+ t.getMessage());
            }
        });
    }
    private void openDialog() {
        dialogBinding = DialogBottomSheetSelectedHomeBinding.inflate(LayoutInflater.from(getContext()));
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(dialogBinding.getRoot());
        bottomSheetDialog.show();
        dialogBinding.btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        dialogBinding.btnSelectImage.setOnClickListener( v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) requireContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                openStorage();
            } else {
                openStorage();
            }
        });
    }

    private void loadIdUser() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user","");
        token_auth = sharedPreferences.getString("token","");
        if(id_user_str.equals("")){
            id_user =0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }

    private void openStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launchSomeActivity.launch(intent);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK){
                    Intent data = result.getData();
                    if ( data != null && check == 2) {
                        selectedImage1Uri = data.getData().toString();
                        fragmentSwapGenBabyBinding.imageIv1.setImageURI(Uri.parse(selectedImage1Uri));
                        bottomSheetDialog.dismiss();
                        postImageResponse(Uri.parse(selectedImage1Uri));
                    } else if(data!=null && check==1){
                        selectedImage2Uri = data.getData().toString();
                        fragmentSwapGenBabyBinding.imageIv2.setImageURI(Uri.parse(selectedImage2Uri));
                        bottomSheetDialog.dismiss();
                        postImageResponse_1(Uri.parse(selectedImage2Uri));
                    }
                }
            }
    );

    private void postImageResponse_1(Uri selectedImage2Uri) {
        String filePath = getRealPathFromURI(requireContext(), selectedImage2Uri);
        File imageFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiService.class);
        Call<String> call = apiService.uploadImage(id_user,"src_nam",imagePart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null ){
                    uriResponse2 = response.body();
                }
                Log.d("check_genSK_baby", "onResponse_image: "+ uriResponse1 + uriResponse2);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: "+ t.getMessage());
            }
        });
    }

    private void postImageResponse(Uri ImageUri) {
        String filePath = getRealPathFromURI(requireContext(), ImageUri);
        File imageFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiService.class);
        Call<String> call = apiService.uploadImage(id_user,"src_nam",imagePart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null ){
                    uriResponse1 = response.body();
                }
                Log.d("check_genSK_baby", "onResponse_image: "+ uriResponse1 + uriResponse2);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: "+ t.getMessage());
            }
        });
    }

    private void loadImageUpload() {
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Log.d("check_id_user", "loadImageUpload: " + id_user);
        Call<ListImageUploadModel> call = apiService.getImageUpload(id_user, "nam");
        call.enqueue(new Callback<ListImageUploadModel>() {
            @Override
            public void onResponse(Call<ListImageUploadModel> call, Response<ListImageUploadModel> response) {
                if (response.isSuccessful()) {
                    listImageUpload = new ArrayList<>();
                    listImageUpload.addAll(response.body().getImage_links_anh());
                    Log.d("check_image_upload", "onResponse: " + response.body());
                    imageUploadAdapter = new ImageUploadAdapter(listImageUpload, getContext(), link_img -> {
                        loadImage(link_img);
                    });
                    linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    fragmentSwapGenBabyBinding.RecImageUpload.setAdapter(imageUploadAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListImageUploadModel> call, Throwable t) {

            }
        });
    }

    private void loadImage(String link_img) {

    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return null;
    }
    private void openCamera() {

    }

}