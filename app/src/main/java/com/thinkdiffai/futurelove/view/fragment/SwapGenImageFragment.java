package com.thinkdiffai.futurelove.view.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingBinding;
import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingImageBinding;
import com.thinkdiffai.futurelove.databinding.DialogBottomSheetSelectedHomeBinding;
import com.thinkdiffai.futurelove.databinding.DialogErrorBinding;
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

import javax.crypto.spec.GCMParameterSpec;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwapGenImageFragment extends Fragment {

    private BottomSheetDialog bottomSheetDialog;

    private File imageFile;

    Call<GenImageModel> genImageModelCall;

    Call<GenBabyModel> genBabyModelCall;

    String check_type_image = "";
    private static final int CAMERA_REQUEST = 1888;

    private static final int REQUEST_CODE_PERMISSIONS = 100;

    String selectedImage1Uri;

    String deviceName;
    CustomDialogLoadingImageBinding customDialogLoadingImageBinding;
    CustomDialogLoadingBinding customDialogLoadingBinding;

    DialogErrorBinding dialogErrorBinding;

    Dialog dialog,dialog_image,dialog_error;

    String imageUpload;

    String uriResponseBaby;

    String ip_them_su_kien;


    int picked_1 = 0;

    int picked_2 = 0 ;
    LinearLayoutManager linearLayoutManager;


    private ImageUploadAdapter imageUploadAdapter;


    private ArrayList<String> listImageUpload;

    String selectedImage2Uri;

    String typeGenImage="";

    String token_auth;
    int id_user;

    String uriResponse1;

    String uriResponse2;

    int check = 0;

    private DialogBottomSheetSelectedHomeBinding dialogBinding;


    private static final int PERMISSION_REQUEST_CODE = 2;

    FragmentSwapGenImageBinding fragmentSwapGenImageBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSwapGenImageBinding = FragmentSwapGenImageBinding.inflate(inflater,container,false);
        return fragmentSwapGenImageBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAction();
        initData();
        createDialog();
    }
    private void createDialog() {
        customDialogLoadingBinding = CustomDialogLoadingBinding.inflate(LayoutInflater.from(requireActivity()));
        dialog = new Dialog(requireActivity());
        dialog.setContentView(customDialogLoadingBinding.getRoot());

        customDialogLoadingImageBinding = CustomDialogLoadingImageBinding.inflate(LayoutInflater.from(requireActivity()));
        dialog_image = new Dialog(requireActivity());
        dialog_image.setContentView(customDialogLoadingImageBinding.getRoot());

        dialogErrorBinding = DialogErrorBinding.inflate(LayoutInflater.from(requireActivity()));
        dialog_error = new Dialog(requireActivity());
        dialog_error.setContentView(dialogErrorBinding.getRoot());
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
        if(typeGenImage.equals("gen_two_image")){
            fragmentSwapGenImageBinding.textView.setText("Gen 2 Image");
        }else{
            fragmentSwapGenImageBinding.textView.setText("Gen Baby");
        }
    }

    private void loadTypeImage() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("swap_image",0);
        typeGenImage = sharedPreferences.getString("type_swap","");
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
        fragmentSwapGenImageBinding.pickImage1Btn.setOnClickListener(v -> {
            check = 2 ;
            fragmentSwapGenImageBinding.textImage.setText("Image 1");
            fragmentSwapGenImageBinding.imageIv2.setVisibility(View.GONE);
            fragmentSwapGenImageBinding.imageIv1.setVisibility(View.VISIBLE);
            if(picked_1 == 0){
                picked_1=1;
            }
        });
        fragmentSwapGenImageBinding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        fragmentSwapGenImageBinding.imageIv1.setOnClickListener(v -> {
            check = 2;
            openDialog();
        });
        fragmentSwapGenImageBinding.imageIv2.setOnClickListener(v -> {
            check = 1;
            openDialog();
        });


        fragmentSwapGenImageBinding.pickImage2Btn.setOnClickListener(v -> {
            check = 1;
            fragmentSwapGenImageBinding.textImage.setText("Image 2");
            fragmentSwapGenImageBinding.imageIv1.setVisibility(View.GONE);
            fragmentSwapGenImageBinding.imageIv2.setVisibility(View.VISIBLE);
            if(picked_2 == 0){
                picked_2=1;
            }
        });

        fragmentSwapGenImageBinding.createVideoBtn.setOnClickListener(v ->{
            if(picked_1 ==0 && picked_2 ==0){
                Toast.makeText(getActivity(), "Choose Image", Toast.LENGTH_SHORT).show();
            }else if(typeGenImage.equals("gen_two_image")){
                openLoadingDialogGen2Image();
                genImageSwap(uriResponse1,uriResponse2);
            }else{
                genSuKienBaby(uriResponse1,uriResponse2);
                openLoadingDialog();
            }
        });
    }

    private void openLoadingDialogGen2Image() {
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                genImageModelCall.cancel();
            }
        });

        customDialogLoadingBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genImageModelCall.cancel();
                dialog.dismiss();
            }
        });
    }
    private void openLoadingDialog() {
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                genBabyModelCall.cancel();
            }
        });
        customDialogLoadingBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genImageModelCall.cancel();
                dialog.dismiss();
            }
        });
    }

    private void genImageSwap(String uriResponse1, String uriResponse2) {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_image_gen_sk", "genImageSwap: "+token_auth + uriResponse1 + uriResponse2+ deviceName+ip_them_su_kien+id_user);
        genImageModelCall = apiService.getUrlImageSwap("Bearer "+ token_auth,uriResponse1,uriResponse2,deviceName,ip_them_su_kien,id_user);
        genImageModelCall.enqueue(new Callback<GenImageModel>() {
            @Override
            public void onResponse(Call<GenImageModel> call, Response<GenImageModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    uriResponseBaby = response.body().getSuKienImageModel().getLink_da_swap();
                    GenImageModel suKienImageModel = response.body();
                    check = 0;
                    picked_2=0;
                    picked_1=0;
                    Log.d("check_genSK_baby", "onResponse: "+ uriResponseBaby + response.body().getSuKienImageModel().getLink_da_swap());
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("check_state_swap",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("state","image");
                    editor.apply();
                    dialog.dismiss();
                    Bundle bundle_1 = new Bundle();
                    bundle_1.putSerializable("genImage",suKienImageModel);
                    NavHostFragment.findNavController(SwapGenImageFragment.this).navigate(R.id.action_swapGenImageFragment_to_genBabyResultFragment,bundle_1);
//                    SwapGenImageFragmentDirections.ActionSwapGenImageFragmentToGenBabyResultFragment action = SwapGenImageFragmentDirections.actionSwapGenImageFragmentToGenBabyResultFragment(uriResponseBaby);
//                    NavHostFragment.findNavController(SwapGenImageFragment.this).navigate(action);

//                    SwapGenImageFragmentDirections.ActionSwapGenImageFragmentToGenBabyResultFragment action_genImageModel = SwapGenImageFragmentDirections.actionSwapGenImageFragmentToGenBabyResultFragment(suKienImageModel);
//                    NavHostFragment.findNavController(SwapGenImageFragment.this).navigate(action_genImageModel);

                }
            }
            @Override
            public void onFailure(Call<GenImageModel> call, Throwable t) {
                dialogErrorBinding.tvError.setText(t.getMessage());
                dialog.dismiss();
                dialog_error.show();

                dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        genImageModelCall.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        genImageModelCall.cancel();
                    }
                });
            }
        });
    }

    private void genSuKienBaby(String uriResponse1, String uriResponse2) {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_genSK_baby_camera", "genSuKienBaby: "+ token_auth + uriResponse1+ uriResponse2+ deviceName+ ip_them_su_kien + id_user);
        genBabyModelCall = apiService.getUrlImageBaby("Bearer "+ token_auth,uriResponse1,uriResponse2,deviceName,ip_them_su_kien,id_user);
        genBabyModelCall.enqueue(new Callback<GenBabyModel>() {
            @Override
            public void onResponse(@NonNull Call<GenBabyModel> call, @NonNull Response<GenBabyModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    uriResponseBaby = response.body().getGenBabyModel().get(0).getLink_da_swap();
                    GenBabyModel detailGenBabyModel = response.body();
                    check = 0;
                    picked_2=0;
                    picked_1=0;
                    Log.d("check_genSK_baby", "onResponse: "+ uriResponseBaby + response.body().getGenBabyModel().get(0).getId_image());
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("check_state_swap",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("state","baby");
                    editor.apply();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("genBaby",detailGenBabyModel);
                    NavHostFragment.findNavController(SwapGenImageFragment.this).navigate(R.id.action_swapGenImageFragment_to_genBabyResultFragment,bundle);
//                    com.thinkdiffai.futurelove.view.fragment.SwapGenImageFragmentDirections.ActionSwapGenImageFragmentToGenBabyResultFragment action = SwapGenImageFragmentDirections.actionSwapGenImageFragmentToGenBabyResultFragment(uriResponseBaby);
//                    NavHostFragment.findNavController(SwapGenImageFragment.this).navigate(action);
//                    SwapGenImageFragmentDirections.ActionSwapGenImageFragmentToGenBabyResultFragment action = SwapGenImageFragmentDirections.actionSwapGenImageFragmentToGenBabyResultFragment(uriResponseBaby);
//                    NavHostFragment.findNavController(SwapGenImageFragment.this).navigate(action);
//                    SwapGenImageFragmentDirections.ActionSwapGenImageFragmentToGenBabyResultFragment action_genBaby = SwapGenImageFragmentDirections.actionSwapGenImageFragmentToGenBabyResultFragment(detailGenBabyModel);

                }else {
                    Log.d("check_genSK_baby", "onResponse: fail");
                }
            }
            @Override
            public void onFailure(@NonNull Call<GenBabyModel> call, @NonNull Throwable t) {
                dialogErrorBinding.tvError.setText(t.getMessage());
                dialog.dismiss();
                dialog_error.show();

                dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        genImageModelCall.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        genImageModelCall.cancel();
                    }
                });
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
        String id_user_str = sharedPreferences.getString("id_user_str","");
        token_auth = sharedPreferences.getString("token","");
        Log.d("check_share_id", "loadIdUser: "+ id_user_str +"_" +token_auth);
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
                    if ( data != null && check == 0 || check ==2) {
                        selectedImage1Uri = data.getData().toString();
                        fragmentSwapGenImageBinding.imageIv1.setImageURI(Uri.parse(selectedImage1Uri));
                        bottomSheetDialog.dismiss();
                        dialog_image.show();
                        postImageResponse(Uri.parse(selectedImage1Uri));
                    } else if(data!=null && check==1){
                        selectedImage2Uri = data.getData().toString();
                        dialog_image.show();
                        fragmentSwapGenImageBinding.imageIv2.setImageURI(Uri.parse(selectedImage2Uri));
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
                    dialog_image.dismiss();
                }
                Log.d("check_genSK_baby_camera", "onResponse_image_2: "+  uriResponse2);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: "+ t.getMessage());
                dialogErrorBinding.tvError.setText(t.getMessage());
                dialog.dismiss();
                dialog_error.show();

                dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        genImageModelCall.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        genImageModelCall.cancel();
                    }
                });
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
                    dialog_image.dismiss();
                }
                Log.d("check_genSK_baby_camera", "onResponse_image_1: "+ uriResponse1 );
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: "+ t.getMessage());
                dialogErrorBinding.tvError.setText(t.getMessage());
                dialog.dismiss();
                dialog_error.show();

                dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        genImageModelCall.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        genImageModelCall.cancel();
                    }
                });
            }
        });
    }
    private void postImageResponseWithCamera(File ImageUri) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), ImageUri);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiService.class);
        Call<String> call = apiService.uploadImage(id_user,"src_nam",imagePart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null && check == 2 || check ==0){
                    dialog_image.dismiss();
                    uriResponse1 = response.body();
                } else if (response.isSuccessful() && response.body() != null && check == 1) {
                    dialog_image.dismiss();
                    uriResponse2 = response.body();
                }
                Log.d("check_genSK_baby_camera", "onResponse_image_2: "+ uriResponse2);
                Log.d("check_genSK_baby_camera", "onResponse_image_1: "+ uriResponse1);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: "+ t.getMessage());
                dialogErrorBinding.tvError.setText(t.getMessage());
                dialog.dismiss();
                dialog_error.show();

                dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        genImageModelCall.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        genImageModelCall.cancel();
                    }
                });
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
                    fragmentSwapGenImageBinding.RecImageUpload.setAdapter(imageUploadAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ListImageUploadModel> call, Throwable t) {

            }
        });
    }

    private void loadImage(String link_img) {
        imageUpload = link_img;
        if (check==0 || check == 2){
            Glide.with(this)
                    .load(link_img)
                    .into(fragmentSwapGenImageBinding.imageIv1);
            imageUpload = link_img.replace("https://futurelove.online","/var/www/build_futurelove");
            uriResponse1 = imageUpload;
        }else if (check == 1){
            Glide.with(this)
                    .load(link_img)
                    .into(fragmentSwapGenImageBinding.imageIv2);

            imageUpload = link_img.replace("https://futurelove.online","/var/www/build_futurelove");
            uriResponse2 = imageUpload;
        }
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
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startCamera() {
        File cacheDir = requireActivity().getApplicationContext().getCacheDir();
        // start default camera
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            String folderPath = cacheDir.getPath() + "/image/";
            File photoFile = new File(folderPath);
            if (!photoFile.exists()) {
                photoFile.mkdirs();
            }
            imageFile = new File(photoFile, System.currentTimeMillis() + ".jpg");

            Uri photoURI = FileProvider.getUriForFile(requireContext(),
                    "com.thinkdiffai.futurelove.fileprovider",
                    imageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bottomSheetDialog.dismiss();
        String imagefile = imageFile.getAbsolutePath();
        Log.d("check_image_file", "onActivityResult: "+ imagefile+ imageFile);
        if(check == 2){
            Glide.with(requireActivity()).load(imagefile).into(fragmentSwapGenImageBinding.imageIv1);
            dialog.show();
            postImageResponseWithCamera(imageFile);
        } else if (check == 1) {
            Glide.with(requireActivity()).load(imagefile).into(fragmentSwapGenImageBinding.imageIv2);
            dialog.show();
            postImageResponseWithCamera(imageFile);
        }
    }
}