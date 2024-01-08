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

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thinkdiffai.futurelove.databinding.DialogBottomSheetSelectedHomeBinding;
import com.thinkdiffai.futurelove.databinding.FragmentGenBabyBinding;
import com.thinkdiffai.futurelove.model.GenBabyModel;
import com.thinkdiffai.futurelove.model.IpNetworkModel;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.RetrofitIp;
import com.thinkdiffai.futurelove.service.api.Server;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenBabyFragment extends Fragment {

    private FragmentGenBabyBinding fragmentGenBabyBinding ;

     String uriResponseFemale;

     String token_auth;

     String uriResponseMale;

     String uriResponseBaby;

    private int id_user;

    String deviceName;
    private DialogBottomSheetSelectedHomeBinding dialogBinding;

    private Uri selectedImageMaleUri;
    private Uri selectedImageFemaleUri;

    String ip_them_su_kien;

    private int check = 0;

    private BottomSheetDialog bottomSheetDialog;

    private static final int PERMISSION_REQUEST_CODE = 2;


    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        fragmentGenBabyBinding = FragmentGenBabyBinding.inflate(inflater,container, false);
        return fragmentGenBabyBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initAction();

    }


    private void initAction() {
        pickImageFemale();
        pickImageMale();
        fragmentGenBabyBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageFemaleUri.equals("") && selectedImageMaleUri.equals("")){
                    Toast.makeText(getActivity(), "Choose your photo", Toast.LENGTH_SHORT).show();
                } if (fragmentGenBabyBinding.edtEnterFemaleName.equals("")  && fragmentGenBabyBinding.edtEnterMaleName.equals("")){
                    Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT).show();
                }
                else{
                    genSuKienBaby(uriResponseMale,uriResponseFemale);
                }
            }
        });
    }

    private void initData() {
        loadIdUser();
        deviceName = getDeviceName();
        callApiAddress();
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


    private void loadIdUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user","");
        token_auth = sharedPreferences.getString("token","");
        if(id_user_str.equals("")){
            id_user =0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }



    private void pickImageFemale() {
        fragmentGenBabyBinding.btnSelectPersonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 0;
                openDialog();
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

        dialogBinding.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) requireContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    openStorage();
                } else {
                    openStorage();
                }
            }
        });
    }
    private void openStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launchSomeActivity.launch(intent);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK){
                    Intent data = result.getData();
                    if ( data != null && check == 0) {
                        selectedImageFemaleUri = data.getData();
                        fragmentGenBabyBinding.btnSelectPersonFemale.setImageURI(selectedImageFemaleUri);
                        bottomSheetDialog.dismiss();
                        postImageResponse(selectedImageFemaleUri);
                    } else if (data != null && check ==1) {
                        selectedImageMaleUri = data.getData();
                        fragmentGenBabyBinding.btnSelectPersonMale.setImageURI(selectedImageMaleUri);
                        bottomSheetDialog.dismiss();
                        postImageResponse(selectedImageMaleUri);
                    }
                }
            }
    );

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
                if(response.isSuccessful() && response.body() != null && check == 0){
                    uriResponseFemale = response.body();
                } else if (response.isSuccessful() && response.body() != null && check == 1) {
                    uriResponseMale = response.body();
                }
                Log.d("check_genSK_baby", "onResponse_image: "+ uriResponseFemale + uriResponseMale);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: "+ t.getMessage());
            }
        });
    }

    private void genSuKienBaby(String uriResponseMale, String uriResponseFemale) {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_genSK_baby", "genSuKienBaby: "+ token_auth + uriResponseFemale+ uriResponseMale+ deviceName+ ip_them_su_kien + id_user);
        Call<GenBabyModel> call = apiService.getUrlImageBaby("Bearer "+ token_auth,uriResponseMale,uriResponseFemale,deviceName,ip_them_su_kien,id_user);
        call.enqueue(new Callback<GenBabyModel>() {
            @Override
            public void onResponse(Call<GenBabyModel> call, Response<GenBabyModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d("check_genSK_baby", "onResponse: "+ response.body());
                }else {
                    Log.d("check_genSK_baby", "onResponse: fail");
                }
            }

            @Override
            public void onFailure(Call<GenBabyModel> call, Throwable t) {

            }
        });
    }

    private void openCamera() {
    }

    private void pickImageMale() {
        fragmentGenBabyBinding.btnSelectPersonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 1;
                openDialog();
            }
        });
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

}