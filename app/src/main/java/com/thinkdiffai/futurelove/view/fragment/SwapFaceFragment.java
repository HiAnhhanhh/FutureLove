package com.thinkdiffai.futurelove.view.fragment;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.DialogBottomSheetSelectedHomeBinding;
import com.thinkdiffai.futurelove.databinding.FragmentSwapFaceBinding;
import com.thinkdiffai.futurelove.model.GetVideoSwapResponse;
import com.thinkdiffai.futurelove.model.IpNetworkModel;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.RetrofitIp;
import com.thinkdiffai.futurelove.service.api.Server;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwapFaceFragment extends Fragment {


    private ImageView IVPreviewImage;

    private List<GetVideoSwapResponse> listSuKienVideoModelArrayList;


    private static final int PERMISSION_REQUEST_CODE = 2;


    private int SELECT_PICTURE = 2;
    private static final int PICK_IMAGE_REQUEST = 1;

    String ip_them_su_kien ;

    private String token_au;
    private File imageFile;

    Uri selectedImageUri;

    String uriResponse;

    String urlVideo;
    String nameVideo;
    String id_video;

    String deviceName;

    int id_user;
    private FragmentSwapFaceBinding fragmentSwapFaceBinding;

    private BottomSheetDialog bottomSheetDialog;
    private DialogBottomSheetSelectedHomeBinding dialogBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentSwapFaceBinding = FragmentSwapFaceBinding.inflate(inflater,container,false);
        return fragmentSwapFaceBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAction();
        initData();

        deviceName = getDeviceName();
        Log.d("check_name_device", "onViewCreated: "+ deviceName);
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
        callApiAddress();
        fragmentSwapFaceBinding.btnSelectPersonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

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
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            selectedImageUri = data.getData();
            fragmentSwapFaceBinding.shapeImageView.setImageURI(selectedImageUri);
            getData();
            bottomSheetDialog.dismiss();
        }
    }

    private void openCamera() {
    }
    private void initData() {
        loadIdUser();
        loadUrlVideo();
        fragmentSwapFaceBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedImageUri == null){
                    Toast.makeText(getActivity(), "Choose your Photo", Toast.LENGTH_SHORT).show();
                } if ( fragmentSwapFaceBinding.edtEnterMaleName.equals("")){
                    Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT).show();
                }
                else{
                    fragmentSwapFaceBinding.cardView.setVisibility(View.GONE);
                    fragmentSwapFaceBinding.view.setVisibility(View.VISIBLE);
                    fragmentSwapFaceBinding.progressBar.setVisibility(View.VISIBLE);
                    getVideoSwap();
                }
            }
        });
    }
    private void loadIdUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str", "");
        String token = sharedPreferences.getString("token","o");
        token_au = token;
        Log.d("check_user_id", "loadIdUser: "+ token_au);
        if (id_user_str == "") {
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }
    private void getData() {
        String filePath = getRealPathFromURI(requireContext(), selectedImageUri);
        imageFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<String> call = apiService.uploadImage(id_user,"src_nam",imagePart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    uriResponse =  response.body();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void getVideoSwap() {
        Log.d("check_swap_face", "getVideoSwap: "+ "Bearer "+token_au + id_video + deviceName + ip_them_su_kien + id_user + uriResponse + "swapvideo.mp4");
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Call<GetVideoSwapResponse> call = apiService.getUrlVideoSwap("Bearer "+token_au, Integer.parseInt(id_video),deviceName,ip_them_su_kien,50,uriResponse,"swapvideo.mp4");
        call.enqueue(new Callback<GetVideoSwapResponse>() {
            @Override
            public void onResponse(Call<GetVideoSwapResponse> call, Response<GetVideoSwapResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    String link_video_da_swap = response.body().sukien_video.link_vid_swap;
                    String link_vid_goc = response.body().sukien_video.link_vid_goc;
                    fragmentSwapFaceBinding.view.setVisibility(View.GONE);
                    fragmentSwapFaceBinding.progressBar.setVisibility(View.GONE);
                    navToVideResultsFragment(link_video_da_swap, link_vid_goc);
                }
            }
            @Override
            public void onFailure(Call<GetVideoSwapResponse> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("check_swap_face", "onFailure: "+t.getMessage());
            }
        });
    }
    private void navToVideResultsFragment(String link_video_da_swap, String link_vid_goc) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("link_video_swap",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("link_vid_swap",link_video_da_swap);
        editor.putString("link_vid_goc",link_vid_goc);
        editor.apply();
        NavHostFragment.findNavController(SwapFaceFragment.this).navigate(R.id.action_swapFaceFragment_to_videoResultsFragment);
    }

    private void navigateToVideoResultsFragment() {
        NavHostFragment.findNavController(SwapFaceFragment.this).navigate(R.id.action_swapFaceFragment_to_videoResultsFragment);
    }
    private void loadUrlVideo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("urlVideo",0);
        urlVideo = sharedPreferences.getString("url_video", "");
        id_video = String.valueOf(sharedPreferences.getInt("id_video",0));
        nameVideo = sharedPreferences.getString("name_video","");
        Log.d("check_click_video", "loadUrlVideo: "+ urlVideo);
        StyledPlayerView styledPlayerView = fragmentSwapFaceBinding.videoSwapFace;
        ExoPlayer exoPlayer = new ExoPlayer.Builder(getActivity()).build();
        styledPlayerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(urlVideo);
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
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