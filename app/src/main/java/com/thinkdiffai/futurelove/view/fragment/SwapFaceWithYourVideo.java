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

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.DialogBottomSheetSelectedHomeBinding;
import com.thinkdiffai.futurelove.databinding.FragmentSwapfaceWithYourVideoBinding;
import com.thinkdiffai.futurelove.model.GetYourVideoSwapModel;
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

public class SwapFaceWithYourVideo extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private Uri selectedVideoUri;
    private Uri selectedImageUri;

    private String token_au;

    private String ip_them_su_kien;

    private BottomSheetDialog bottomSheetDialog;

    private DialogBottomSheetSelectedHomeBinding dialogBinding;

    private String uriResponse;

    private String deviceName;

    private static final int PERMISSION_REQUEST_CODE = 2;


    private int id_user;


    private static final int PICK_VIDEO_REQUEST = 1;

    private static final int PICK_IMAGE_REQUEST = 2;


    private FragmentSwapfaceWithYourVideoBinding fragmentSwapfaceWithYourVideoBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSwapfaceWithYourVideoBinding = FragmentSwapfaceWithYourVideoBinding.inflate(inflater,container,false);
        return fragmentSwapfaceWithYourVideoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAction();
        initView();
        initData();
    }
    private void initData() {
        callApiAddress();
        loadIdUser();
        deviceName = getDeviceName();

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
        String token = sharedPreferences.getString("token","o");
        Log.d("check_token", "loadIdUser: "+ token);
        token_au = token;
        if(id_user_str.equals("")){
            id_user =0;
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
    private void initView() {

    }
    private void initAction() {
        fragmentSwapfaceWithYourVideoBinding.btnSelectPersonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        fragmentSwapfaceWithYourVideoBinding.chooseVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStorageVid();
                fragmentSwapfaceWithYourVideoBinding.cardView.setVisibility(View.VISIBLE);
            }
        });

        fragmentSwapfaceWithYourVideoBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedImageUri == null){
                    Toast.makeText(getActivity(), "Choose your image", Toast.LENGTH_SHORT).show();
                } if (selectedVideoUri == null){
                    Toast.makeText(getActivity(), "Choose your video", Toast.LENGTH_SHORT).show();
                } if (fragmentSwapfaceWithYourVideoBinding.edtEnterMaleName.equals("")){
                    Toast.makeText(getActivity(), "Enter name", Toast.LENGTH_SHORT).show();
                }
                else {
                    fragmentSwapfaceWithYourVideoBinding.imageView.setVisibility(View.VISIBLE);
                    fragmentSwapfaceWithYourVideoBinding.view.setVisibility(View.VISIBLE);
                    fragmentSwapfaceWithYourVideoBinding.cardView.setVisibility(View.GONE);
                    fragmentSwapfaceWithYourVideoBinding.chooseVideoBtn.setVisibility(View.GONE);
                    fragmentSwapfaceWithYourVideoBinding.progressBar.setVisibility(View.VISIBLE);
                    getVideoSwap();
                }
            }
        });
    }

    private void getVideoSwap() {
        String filePath = getRealPathVideoFromURI(requireContext(), selectedVideoUri);
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
                    fragmentSwapfaceWithYourVideoBinding.progressBar.setVisibility(View.GONE);
                    fragmentSwapfaceWithYourVideoBinding.view.setVisibility(View.GONE);

                    navToVideResultsFragment(link_vid_swap,link_video_goc);
                }
            }
            @Override
            public void onFailure(Call<GetYourVideoSwapModel> call, Throwable t) {
                Log.d("check_swap_video_1", "onFailure: "+ t.getMessage());
            }
        });
    }

    private void navToVideResultsFragment(String link_vid_swap, String link_video_goc) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("link_video_swap",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("link_vid_swap",link_vid_swap);
        editor.putString("link_vid_goc",link_video_goc);
        editor.apply();
        NavHostFragment.findNavController(SwapFaceWithYourVideo.this).navigate(R.id.action_swapFaceWithYourVideo_to_videoResultsFragment);
    }

    private void openStorageVid() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        launcherVideo.launch(intent);
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
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK ){
                    Intent data = result.getData();
                    if(data != null){
                        selectedImageUri = data.getData();
                        fragmentSwapfaceWithYourVideoBinding.shapeImageView.setImageURI(selectedImageUri);
                        bottomSheetDialog.dismiss();
                        postImageFile(selectedImageUri);
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> launcherVideo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if( result.getResultCode() == RESULT_OK){
                    Intent data = result.getData();
                    if( data != null) {
                        selectedVideoUri = data.getData();
                        loadVideoUri(selectedVideoUri);
                    }
                }
            }
    );

    private void openCamera() {
        
    }
    private void postImageFile(Uri selectedImageUri) {
        String filePath = getRealPathFromURI(requireContext(), selectedImageUri);
        File imageFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiService.class);
        Call<String> call = apiService.uploadImage(id_user,"src_nam",imagePart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    uriResponse = response.body();
                    Log.d("check_upload_image_your_video", "onResponse: "+ uriResponse);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: "+ t.getMessage());
            }
        });
    }
    private void loadVideoUri(Uri selectedVideoUri) {
        StyledPlayerView styledPlayerView = fragmentSwapfaceWithYourVideoBinding.videoSwapFace;
        ExoPlayer exoPlayer = new ExoPlayer.Builder(getActivity()).build();
        styledPlayerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(selectedVideoUri);
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
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
}