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

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.CustomBottomSheetChangeAvatarBinding;
import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingBinding;
import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingImageBinding;
import com.thinkdiffai.futurelove.databinding.DialogErrorBinding;
import com.thinkdiffai.futurelove.databinding.FragmentProfileUserBinding;
import com.thinkdiffai.futurelove.model.EventCreateByUser;
import com.thinkdiffai.futurelove.model.comment.DetailUser;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.view.adapter.EventUserAdapter;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileUserFragment extends Fragment {

    private File imageFile;

    String url_image;
    String image_page;
    boolean checkImage ;

    int id_toan_bo_su_kien;
    Call<String> callPostImageFile;
    BottomSheetDialog bottomSheetDialog;
    FragmentProfileUserBinding fragmentProfileUserBinding;

    private static final int REQUEST_CODE_PERMISSIONS = 100;
    String nameUser,email;
    String imageUpload;
    private static final int CAMERA_REQUEST = 1888;


    int check_info = 0;
    String uriResponse;

    CustomDialogLoadingImageBinding customDialogLoadingImageBinding;
    CustomDialogLoadingBinding customDialogLoadingBinding;

    DialogErrorBinding dialogErrorBinding;

    Dialog dialog,dialog_image,dialog_error;
    
    Call<String> call_image_upload;
    Uri selectedImageUri;

    private static final int PERMISSION_REQUEST_CODE = 2;

    String link_image_avatar;
    String eventCount, viewCount, commentCount;

    int id_user;

    private ArrayList<EventCreateByUser.EventDetail> eventDetailModelArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentProfileUserBinding = FragmentProfileUserBinding.inflate(inflater,container,false);
        return fragmentProfileUserBinding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadInfoProfile();
        createDialog();
        initData();
        initAction();
    }
    private void loadInfoProfile() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("image_profile",0);
        checkImage = sharedPreferences.getBoolean("check_state",true);

        if(checkImage){
            fragmentProfileUserBinding.shapeAbleImageView.setImageResource(R.drawable.baseline_account_circle_24);
            fragmentProfileUserBinding.imageView6.setImageResource(R.drawable.baseline_account_circle_24);
        }else{
            url_image = sharedPreferences.getString("your_url_profile","");
            Glide.with(this).load(Uri.parse(url_image)).into(fragmentProfileUserBinding.shapeAbleImageView);
            image_page = sharedPreferences.getString("your_url_profile_page","");
            Glide.with(this).load(Uri.parse(image_page)).into(fragmentProfileUserBinding.imageView6);
        }
    }
    private void createDialog() {
        customDialogLoadingImageBinding = CustomDialogLoadingImageBinding.inflate(LayoutInflater.from(requireActivity()));
        dialog_image = new Dialog(requireActivity());
        dialog_image.setContentView(customDialogLoadingImageBinding.getRoot());

        dialogErrorBinding = DialogErrorBinding.inflate(LayoutInflater.from(requireActivity()));
        dialog_error = new Dialog(requireActivity());
        dialog_error.setContentView(dialogErrorBinding.getRoot());
    }
    private void initViewRec() {
        EventUserAdapter eventUserAdapter = new EventUserAdapter(getContext(),eventDetailModelArrayList, (position, so_thu_tu_su_kien) -> {
            goToDetailEvent(position,so_thu_tu_su_kien);
        });
        Log.d("check_event_list", "initViewRec: "+ eventDetailModelArrayList.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        fragmentProfileUserBinding.recEventCreateByUser.setLayoutManager(linearLayoutManager);
        fragmentProfileUserBinding.recEventCreateByUser.setAdapter(eventUserAdapter);
    }

    private void goToDetailEvent(String id_toan_bo_su_kien, int so_thu_tu_su_kien) {
        Bundle bundle = new Bundle();
        bundle.putString("id_toan_bo_su_kien",id_toan_bo_su_kien);
        bundle.putString("so_thu_tu_su_kien",String.valueOf(so_thu_tu_su_kien));
        NavHostFragment.findNavController(ProfileUserFragment.this).navigate(R.id.action_profileUserFragment_to_eventDetailFragment,bundle);
    }
    private void initAction() {



        fragmentProfileUserBinding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        fragmentProfileUserBinding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_info =1;
                View bottomSheetView = getLayoutInflater().inflate(R.layout.custom_bottom_sheet_change_avatar, null);
                CustomBottomSheetChangeAvatarBinding bottomSheetBinding = CustomBottomSheetChangeAvatarBinding.bind(bottomSheetView);
                bottomSheetDialog = new BottomSheetDialog(requireActivity());
                bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
                bottomSheetDialog.show();

                bottomSheetBinding.viewAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProfileUserFragmentDirections.ActionProfileUserFragmentToFullScreenImageFragment action = ProfileUserFragmentDirections.actionProfileUserFragmentToFullScreenImageFragment(image_page);
                        NavHostFragment.findNavController(ProfileUserFragment.this).navigate(action);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetBinding.imageToDevice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) requireContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                            openStorage();
                        } else {
                            openStorage();
                        }
                    }
                });
                bottomSheetBinding.pickImageCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        openCamera();
                    }

                });
            }
        });
        fragmentProfileUserBinding.shapeAbleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_info = 2;
                View bottomSheetView = getLayoutInflater().inflate(R.layout.custom_bottom_sheet_change_avatar, null);
                CustomBottomSheetChangeAvatarBinding bottomSheetBinding = CustomBottomSheetChangeAvatarBinding.bind(bottomSheetView);
                bottomSheetDialog = new BottomSheetDialog(requireActivity());
                bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
                bottomSheetDialog.show();

                bottomSheetBinding.viewAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProfileUserFragmentDirections.ActionProfileUserFragmentToFullScreenImageFragment action = ProfileUserFragmentDirections.actionProfileUserFragmentToFullScreenImageFragment(url_image);
                        NavHostFragment.findNavController(ProfileUserFragment.this).navigate(action);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetBinding.imageToDevice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) requireContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                            openStorage();
                        } else {
                            openStorage();
                        }
                    }
                });
                bottomSheetBinding.pickImageCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        openCamera();
                    }
                });
            }
        });
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
        openDialogLoadImage();
        postImageFileWithCamera(imageFile);
    }
    private void postImageFileWithCamera(File imageFile) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiService.class);
        callPostImageFile = apiService.uploadImage(id_user, "src_nam", imagePart);
        callPostImageFile.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    uriResponse = response.body();
                    loadImage(uriResponse);
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
                        callPostImageFile.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callPostImageFile.cancel();
                    }
                });
            }
        });
    }
    private void openDialogLoadImage() {
        dialog_image.show();
        dialog_image.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                call_image_upload.cancel();
            }
        });
        customDialogLoadingImageBinding.buttonStop.setOnClickListener(v -> {
            dialog_image.dismiss();
            call_image_upload.cancel();
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
                    if(data != null){
                        selectedImageUri = data.getData();
                        dialog_image.show();
                        postImageFile(selectedImageUri);
                    }
                }
            }
    );
    private void initView() {
        displayViewScreen();
    }

    private void displayViewScreen() {
        Log.d("check_data_profile", "displayViewScreen: "+ email+ nameUser+ commentCount+ eventCount+ viewCount);
        fragmentProfileUserBinding.viewCountComment.setText(commentCount);
        fragmentProfileUserBinding.viewCountEvent.setText(eventCount);
        fragmentProfileUserBinding.viewCountView.setText(viewCount);
        fragmentProfileUserBinding.nameUser.setText(nameUser);
        fragmentProfileUserBinding.emailUser.setText(email);
    }
    private void initData() {
        loadIdUser();
        loadProfile();
        loadEventCreateByUser();
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
                    if (check_info==2){
                        loadImage(uriResponse);
                        check_info=0;
                    }else {
                        loadImagePage(uriResponse);
                        check_info=0;
                    }
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
                        call_image_upload.cancel();
                    }
                });
                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call_image_upload.cancel();
                        dialog_error.dismiss();
                    }
                });
            }
        });
    }
    private void loadEventCreateByUser() {
        Log.d("check_profile", "onResponse: "+ id_user);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<EventCreateByUser> call = apiService.getEventCreateByUser(id_user);
        call.enqueue(new Callback<EventCreateByUser>() {
            @Override
            public void onResponse(Call<EventCreateByUser> call, Response<EventCreateByUser> response) {
                if(response.isSuccessful()){
                    eventDetailModelArrayList = new ArrayList<>();
                    eventDetailModelArrayList.addAll(response.body().getEventDetails());
                    Log.d("check_resposne_profile", "onResponse: "+ response.body());
                    fragmentProfileUserBinding.layoutProgressBar.setVisibility(View.GONE);
                    fragmentProfileUserBinding.recEventCreateByUser.setVisibility(View.VISIBLE);
                    initViewRec();
                }
            }
            @Override
            public void onFailure(Call<EventCreateByUser> call, Throwable t) {
                Log.d("check_resposne_profile", "onResponse: "+ t.getMessage());
            }
        });
    }
    private void loadProfile() {
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<DetailUser> call = apiService.getProfileUser(id_user);
        call.enqueue(new Callback<DetailUser>() {
            @Override
            public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
                if(response.isSuccessful()){
                    DetailUser detailUser = response.body();
                    nameUser = detailUser.getUser_name();
                    email = detailUser.getEmail();
                    viewCount = String.valueOf(detailUser.getCount_view()) ;
                    commentCount =String.valueOf(detailUser.getCount_comment() );
                    eventCount = String.valueOf(detailUser.getCount_sukien()) ;
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("image_profile",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email",email);
                    editor.putString("name_user",nameUser);
                    editor.apply();
                    initView();
                    Log.d("check_profile", "onResponse: "+ nameUser + eventCount);
                }
            }
            @Override
            public void onFailure(Call<DetailUser> call, Throwable t) {
                Log.d("check_profile", "onFailure: "+ t.getMessage());
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
    private void loadImagePage(String link_img) {
        imageUpload = link_img;
        imageUpload = link_img.replace("/var/www/build_futurelove", "https://futurelove.online");
        Glide.with(this)
                .load(imageUpload)
                .into(fragmentProfileUserBinding.imageView6);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("image_profile",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("your_url_profile_page",imageUpload);
        editor.apply();
        dialog_image.dismiss();
    }
    private void loadImage(String link_img) {
        imageUpload = link_img;
        imageUpload = link_img.replace("/var/www/build_futurelove", "https://futurelove.online");
        Glide.with(this)
                .load(imageUpload)
                .into(fragmentProfileUserBinding.shapeAbleImageView);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("image_profile",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("your_url_profile",imageUpload);
        editor.putBoolean("check_state",false);
        editor.apply();
        dialog_image.dismiss();
    }
}
