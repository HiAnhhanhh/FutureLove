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

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.thinkdiffai.futurelove.databinding.DialogBottomSheetSelectedHomeBinding;
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
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickImageFragment extends Fragment {

    int id_user;


    int id_video;

    int picked = 0;

    String receivedData;

    String receivedData_1;

    private String token_au;

    private String ip_them_su_kien;

    Uri selectedImageUri;

    String uriResponse;

    private ArrayList<String> listImageUpload;

    private BottomSheetDialog bottomSheetDialog;
    private DialogBottomSheetSelectedHomeBinding dialogBinding;

    private String deviceName;

    private static final int PERMISSION_REQUEST_CODE = 2;


    LinearLayoutManager linearLayoutManager;

    private FragmentPickImageBinding fragmentPickImageBinding;


    private ImageUploadAdapter imageUploadAdapter;

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
    }

    private void initAction() {
        fragmentPickImageBinding.backBtn.setOnClickListener(v -> requireActivity().onBackPressed());

        fragmentPickImageBinding.imagePickBtn.setOnClickListener(v -> openDialog());

        fragmentPickImageBinding.createVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri == null) {
                    Toast.makeText(getActivity(), "Choose Image", Toast.LENGTH_SHORT).show();
                } else if(receivedData_1!= null && receivedData == null) {
                    fragmentPickImageBinding.view.setVisibility(View.VISIBLE);
                    fragmentPickImageBinding.progressBar.setVisibility(View.VISIBLE);
                    getVideoSwap();
                }else {
                    fragmentPickImageBinding.view.setVisibility(View.VISIBLE);
                    fragmentPickImageBinding.progressBar.setVisibility(View.VISIBLE);
                    getVideoSwapWithYourUrl();
                }
            }
        });

    }

    private void getVideoSwapWithYourUrl() {
        String filePath = getRealPathVideoFromURI(requireContext(), Uri.parse(receivedData));
        File videoFile = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("src_vid", videoFile.getName(), requestFile);
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_swap_video_1", "getVideoSwap: "+ deviceName + ip_them_su_kien + id_user + uriResponse + videoPart.body()+ filePath+ "Bearer "+token_au);
        Call<GetYourVideoSwapModel> call = apiService.PostVid("Bearer "+token_au,deviceName,ip_them_su_kien,id_user,uriResponse,videoPart);
        call.enqueue(new Callback<GetYourVideoSwapModel>() {
            @Override
            public void onResponse(Call<GetYourVideoSwapModel> call, Response<GetYourVideoSwapModel> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    String link_video_goc = response.body().suKienVideoSwap.getLinkVidGoc();
                    String link_vid_swap = response.body().suKienVideoSwap.getLink_video_da_swap();
                    fragmentPickImageBinding.progressBar.setVisibility(View.GONE);
                    fragmentPickImageBinding.view.setVisibility(View.GONE);

                    navToVideResultsFragment(link_vid_swap,link_video_goc);
                }
            }
            @Override
            public void onFailure(Call<GetYourVideoSwapModel> call, Throwable t) {
                Log.d("check_swap_video_1", "onFailure: "+ t.getMessage());
            }
        });
    }

    private void getVideoSwap() {
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_id_user", "getVideoSwap: " + id_user);
        Call<GetVideoSwapResponse> call = apiService.getUrlVideoSwap("Bearer " + token_au, id_video, deviceName, ip_them_su_kien, 50, uriResponse, "swapvideo.mp4");
        call.enqueue(new Callback<GetVideoSwapResponse>() {
            @Override
            public void onResponse(Call<GetVideoSwapResponse> call, Response<GetVideoSwapResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String link_video_da_swap = response.body().sukien_video.link_vid_swap;
                    String link_vid_goc = response.body().sukien_video.link_vid_goc;
                    fragmentPickImageBinding.view.setVisibility(View.GONE);
                    fragmentPickImageBinding.progressBar.setVisibility(View.GONE);
                    navToVideResultsFragment(link_video_da_swap, link_vid_goc);
                    Log.d("check_video_swap", "onResponse: " + link_video_da_swap + link_vid_goc);
                }
            }

            @Override
            public void onFailure(Call<GetVideoSwapResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("check_swap_face", "onFailure: " + t.getMessage());
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

    private void openCamera() {
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
                    }
                }
            }
    );
    private void postImageFile(Uri selectedImageUri) {
        String filePath = getRealPathFromURI(requireContext(), selectedImageUri);
        File imageFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiService.class);
        Call<String> call = apiService.uploadImage(id_user, "src_nam", imagePart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    uriResponse = response.body();
                    Log.d("check_upload_image_your_video", "onResponse: " + uriResponse);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: " + t.getMessage());
            }
        });
    }
    private void initData() {
        loadYourUrlVideo();
        loadIdUser();
        loadImageUpload();
        callApiAddress();
        deviceName = getDeviceName();
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
        String id_user_str = sharedPreferences.getString("id_user_str", "");
        String token = sharedPreferences.getString("token","o");
        token_au = token;
        Log.d("check_user_id", "loadIdUser: "+ token_au);
        if (id_user_str.equals("")) {
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
            public void onFailure(Call<ListImageUploadModel> call, Throwable t) {

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
    }

}