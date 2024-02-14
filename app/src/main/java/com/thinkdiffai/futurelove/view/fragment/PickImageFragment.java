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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
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

import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingBinding;
import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingImageBinding;
import com.thinkdiffai.futurelove.databinding.DialogBottomSheetSelectedHomeBinding;
import com.thinkdiffai.futurelove.databinding.DialogErrorBinding;
import com.thinkdiffai.futurelove.databinding.FragmentPickImageBinding;
import com.thinkdiffai.futurelove.model.GetVideoSwapResponse;
import com.thinkdiffai.futurelove.model.GetYourVideoSwapModel;
import com.thinkdiffai.futurelove.model.IpNetworkModel;
import com.thinkdiffai.futurelove.model.ListImageUploadModel;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.RetrofitIp;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.ImageUploadAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickImageFragment extends Fragment {
    int id_user;
    private File imageFile;
    String uriImageUpload;
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final int CAMERA_REQUEST = 1888;
    int id_video;
    Dialog dialog;

    Dialog dialog_image;

    Dialog dialog_error;
    String token_auth;

    Call<String> call_image_upload;
    CustomDialogLoadingBinding customDialogLoadingBinding;
    int picked = 0;
    String receivedData;
    String receivedData_1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String ip_them_su_kien;
    Uri selectedImageUri;
    String uriResponse;

    private CustomDialogLoadingImageBinding customDialogLoadingImageBinding;

    private DialogErrorBinding dialogErrorBinding;
    private ArrayList<String> listImageUpload;
    private BottomSheetDialog bottomSheetDialog;
    private DialogBottomSheetSelectedHomeBinding dialogBinding;
    private String deviceName;
    private static final int PERMISSION_REQUEST_CODE = 2;
    LinearLayoutManager linearLayoutManager;
    private FragmentPickImageBinding fragmentPickImageBinding;
    private ImageUploadAdapter imageUploadAdapter;
    Call<GetYourVideoSwapModel> call_your_video;

    Call<GetVideoSwapResponse> call_template_video;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPickImageBinding = FragmentPickImageBinding.inflate(inflater, container, false);
        return fragmentPickImageBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initAction();
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

    private void initAction() {
        fragmentPickImageBinding.backBtn.setOnClickListener(v -> requireActivity().onBackPressed());
        fragmentPickImageBinding.imagePickBtn.setOnClickListener(v -> openDialog());
        fragmentPickImageBinding.createVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriResponse == null) {
                    Toast.makeText(getActivity(), "Choose Image", Toast.LENGTH_SHORT).show();
                } else if(receivedData_1!= null && receivedData == null) {
                    openDialogLoading();
                    if(uriResponse!=null){
                        getVideoSwap();
                    }else{
                        Toast.makeText(getActivity(), "Url image Error", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    openDialogLoading();
                    if(uriResponse!=null){
                        getVideoSwapWithYourUrl();
                    }else{
                        Toast.makeText(getActivity(), "Url image Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void openDialogLoading() {
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(receivedData_1!= null && receivedData == null){
                    call_template_video.cancel();
                }else {
                    call_your_video.cancel();
                }
            }
        });
        customDialogLoadingBinding.buttonStop.setOnClickListener(v -> {
            if(receivedData_1!= null && receivedData == null){
                call_template_video.cancel();
            }else {
                call_your_video.cancel();
            }
            dialog.dismiss();
        });
    }

    private void getVideoSwapWithYourUrl() {
        String filePath = getRealPathVideoFromURI(requireContext(), Uri.parse(receivedData));
        File videoFile = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("src_vid", videoFile.getName(), requestFile);
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_swap_video_1", "getVideoSwap: "+ deviceName + ip_them_su_kien + id_user + uriResponse + videoPart.body()+ filePath+ "Bearer "+token_auth);
        call_your_video = apiService.PostVid("Bearer "+token_auth,deviceName,ip_them_su_kien,id_user,uriResponse,videoPart);
        call_your_video.enqueue(new Callback<GetYourVideoSwapModel>() {
            @Override
            public void onResponse(Call<GetYourVideoSwapModel> call, Response<GetYourVideoSwapModel> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    String link_video_goc = response.body().suKienVideoSwap.getLinkVidGoc();
                    String link_vid_swap = response.body().suKienVideoSwap.getLink_video_da_swap();
                    dialog.dismiss();
                    navToVideResultsFragment(link_vid_swap,link_video_goc);
                }
            }
            @Override
            public void onFailure(Call<GetYourVideoSwapModel> call, Throwable t) {
                Log.d("check_swap_video_1", "onFailure: "+ t.getMessage());
                dialogErrorBinding.tvError.setText(t.getMessage());
                dialog.dismiss();
                dialog_error.show();
                dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        call_template_video.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call_template_video.cancel();
                    }
                });
            }
        });
    }
    private void getVideoSwap() {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_id_user", "getVideoSwap: " + id_user);
        Log.d("check_video_swap", "onResponse: " +"Bearer " + token_auth + id_video + deviceName + ip_them_su_kien + 50 + uriResponse + "swapvideo.mp4");
        call_template_video = apiService.getUrlVideoSwap("Bearer " + token_auth, id_video, deviceName, ip_them_su_kien, id_user, uriResponse, "swapvideo.mp4");
        call_template_video.enqueue(new Callback<GetVideoSwapResponse>() {
            @Override
            public void onResponse(Call<GetVideoSwapResponse> call, Response<GetVideoSwapResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String link_video_da_swap = response.body().sukien_video.link_vid_swap;
                    String link_vid_goc = response.body().sukien_video.link_vid_goc;

                    dialog.dismiss();
                    navToVideResultsFragment(link_video_da_swap, link_vid_goc);
                    Log.d("check_video_swap", "onResponse: " + link_video_da_swap + link_vid_goc);
                }
            }
            @Override
            public void onFailure(Call<GetVideoSwapResponse> call, Throwable t) {
                dialogErrorBinding.tvError.setText(t.getMessage());
                dialog.dismiss();
                dialog_error.show();

                dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        call_template_video.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call_template_video.cancel();
                    }
                });
            }
        });
    }
    private void navToVideResultsFragment(String link_vid_swap, String link_video_goc) {
        PickImageFragmentDirections.ActionPickImageToSwapFragmentToVideoResultsFragment action = PickImageFragmentDirections.actionPickImageToSwapFragmentToVideoResultsFragment(link_video_goc,link_vid_swap);
        NavHostFragment.findNavController(PickImageFragment.this).navigate(action);
    }
    private void openDialog() {
        dialogBinding = DialogBottomSheetSelectedHomeBinding.inflate(LayoutInflater.from(getContext()));
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(dialogBinding.getRoot());
        bottomSheetDialog.show();
        dialogBinding.btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openCamera();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
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

    private void openCamera() throws FileNotFoundException {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startCamera() throws FileNotFoundException {
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bottomSheetDialog.dismiss();
        String imagefile = imageFile.getAbsolutePath();
        Log.d("check_image_file", "onActivityResult: "+ imagefile+ imageFile);
        Glide.with(requireActivity()).load(imagefile).into(fragmentPickImageBinding.imageIv);
        openDialogLoadImage();
        postImageFileWithCamera(imageFile);
    }

    private void openStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcherVideo.launch(intent);
    }
    ActivityResultLauncher<Intent> launcherVideo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        selectedImageUri = data.getData();
                        loadImage(selectedImageUri.toString());
                        bottomSheetDialog.dismiss();
                        postImageFile(selectedImageUri);
                        openDialogLoadImage();
                    }
                }
            }
    );
    private void openDialogLoadImage() {
        dialog_image.show();
        dialog_image.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                call_image_upload.cancel();
            }
        });
        customDialogLoadingImageBinding.buttonStop.setOnClickListener(v -> {
            call_image_upload.cancel();
            dialog_image.dismiss();
        });
    }
    private void postImageFile(Uri selectedImageUri) {
        String filePath = getRealPathFromURI(requireContext(), selectedImageUri);
        File imageFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiService.class);
        call_image_upload = apiService.uploadImage(id_user, "src_nam", imagePart);
        call_image_upload.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    uriResponse = response.body();
                    dialog_image.dismiss();
                    Log.d("check_upload_image_your_video", "onResponse: " + uriResponse);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: " + t.getMessage());
                dialogErrorBinding.tvError.setText(t.getMessage());
                dialog_image.dismiss();
                dialog_error.show();
                dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        call_template_video.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call_template_video.cancel();
                    }
                });
            }
        });
    }
    private void postImageFileWithCamera(File imageFile) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiService.class);
        Call<String> call = apiService.uploadImage(id_user, "src_nam", imagePart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    uriResponse = response.body();
                    dialog_image.dismiss();
                    Log.d("check_upload_image_your_video", "onResponse: " + uriResponse);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: " + t.getMessage());
                dialogErrorBinding.tvError.setText(t.getMessage());
                dialog_image.dismiss();
                dialog_error.show();
                dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        call_template_video.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call_template_video.cancel();
                    }
                });
            }
        });
    }

    private void initData() {
        loadIdUser();
        callApiAddress();
        deviceName = getDeviceName();
        loadYourUrlVideo();
        loadImageUpload();
    }

    private void loadYourUrlVideo() {
        Bundle args = getArguments();
        if(args!=null){
            receivedData = args.getString("url_your_video");
            id_video = args.getInt("id_video");
            receivedData_1 = args.getString("url_video_1");
            Log.d("check_url_your_video", "loadYourUrlVideo: "+ receivedData+ id_video + receivedData_1);
        }else{
            Log.d("check_url_your_video", "loadYourUrlVideo: NoNoNo");
        }
    }
    private void loadIdUser() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str","");
        token_auth = sharedPreferences.getString("token","");
        Log.d("check_share_id", "loadIdUser: "+ id_user_str +"_" +token_auth);
        if(id_user_str.equals("")){
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }
    private void callApiAddress() {
        ApiService apiService = RetrofitIp.getInstance(Server.GET_CITY_NAME_FROM_IP).getRetrofit().create(ApiService.class);
        Call<IpNetworkModel> call = apiService.getIpApiResponse();
        call.enqueue(new Callback<IpNetworkModel>() {
            @Override
            public void onResponse(Call<IpNetworkModel> call, Response<IpNetworkModel> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d("check_ip", "onResponse: " + response.body().getIp());
                    ip_them_su_kien = response.body().getIp();
                }
            }
            @Override
            public void onFailure(Call<IpNetworkModel> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    fragmentPickImageBinding.RecImageUpload.setAdapter(imageUploadAdapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ListImageUploadModel> call, @NonNull Throwable t) {
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

    public static String getRealPathVideoFromURI(Context context, Uri contentUri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
    private void loadImage(String link_img) {
        Glide.with(this)
                .load(link_img)
                .into(fragmentPickImageBinding.imageIv);

        uriImageUpload = link_img.replace("https://futurelove.online","/var/www/build_futurelove");
        Log.d("check_url", "loadImage: "+ uriImageUpload);
        uriResponse= uriImageUpload;
    }
}