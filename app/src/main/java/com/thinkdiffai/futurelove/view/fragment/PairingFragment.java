package com.thinkdiffai.futurelove.view.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingBinding;
import com.thinkdiffai.futurelove.databinding.DialogBottomSheetSelectedHomeBinding;
import com.thinkdiffai.futurelove.databinding.FragmentPairingBinding;
import com.thinkdiffai.futurelove.model.IpNetworkModel;
import com.thinkdiffai.futurelove.model.event.UploadingEvent;
import com.thinkdiffai.futurelove.presenter.api.CreateImplicitEventImpl;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.RetrofitIp;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.util.MyDialog;
import com.thinkdiffai.futurelove.util.Util;
import com.thinkdiffai.futurelove.view.fragment.activity.MainActivity;
import com.thinkdiffai.futurelove.view.fragment.dialog.MyOwnDialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PairingFragment extends Fragment {

    Call<String> call_image_upload;

    Call<Object> callPostEvent;
    String uriResponse;

    CustomDialogLoadingBinding customDialogLoadingBinding;
    Dialog dialog;
    String selectedImageMale, selectedImageFemale;
    Uri selectedImageUri;
    private static final String NO_FACE_DETECTED = "No faces detected";
    private static final String MORE_THAN_ONE_FACE = "More than one face is recognized";
    private FragmentPairingBinding fragmentPairingBinding;
    private static final int IMAGE_PICKER_SELECT = 1889;
    private BottomSheetDialog bottomSheetDialog;
    private static final int CAMERA_REQUEST = 1888;

    private BubbleNavigationLinearView bubbleNavigationLinearView;

    private static final String TAG = "CameraActivity";

    private static final String TAG1 = MainActivity.class.getName();
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final int REQUEST_CODE_PERMISSIONS_STORAGE = 101;
    private boolean checkClickSetImageMale= true;
    private boolean isCheckSetImageFemale = false;

    private int checkCLickImage = 0;
    String token_auth,deviceName,ip_them_su_kien;
    private boolean isCheckSetImageMale = false;
    private File imageFile;
    private KProgressHUD kProgressHUD;
    private String resultDetech;
    private String imgBase64Male;
    int id_user ;
    private String imgBase64Female;
    private String urlImageMale;
    private String urlImageFemale;

    private String maleName = "";
    private String femaleName = "";
    private MainActivity mainActivity;
    private MyDialog myDialog;

    private SimpleDateFormat dateFormat;

    private static final String PATTERN_DATE = "yyyy-MM-dd, HH:mm:ss";
    private long startPairingTimeStamp;
    private Handler handler;
    private Runnable runnable;
    private long elapsedTime;
    private DialogBottomSheetSelectedHomeBinding bottomDialogBinding;
    private boolean isNameFilled = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPairingBinding = FragmentPairingBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();
        kProgressHUD = mainActivity.createHud();
        try {
            initDialog();
            initUi();
            initListener();
            initData();
        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }
        return fragmentPairingBinding.getRoot();
    }

    private void initDialog() {
        dialog = new Dialog(requireActivity());
        customDialogLoadingBinding = CustomDialogLoadingBinding.inflate(getLayoutInflater());
        dialog.setContentView(customDialogLoadingBinding.getRoot());
        customDialogLoadingBinding.titleDialog.setText("Initialization is in progress, Please Wait ...");
    }

    private void initData() {
        deviceName = getDeviceName();
        loadIdUser();
        callApiAddress();
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

    private void initUi() {

        if (mainActivity.waitingSwapFaceTime != 0 && mainActivity.maleImage != null && mainActivity.femaleImage != null) {
            Toast.makeText(mainActivity, "Running Process ...", Toast.LENGTH_SHORT).show();
            getTimeIncreaseAndShow(Util.getTimeStampNow(), mainActivity.waitingSwapFaceTime);
            fragmentPairingBinding.cvImageMale.setVisibility(View.VISIBLE);
            fragmentPairingBinding.imgMale.setImageBitmap(mainActivity.maleImage);
            fragmentPairingBinding.cvImageFemale.setVisibility(View.VISIBLE);
            fragmentPairingBinding.imgFemale.setImageBitmap(mainActivity.femaleImage);
            lockBtnSelectImage();
        } else {
            resetDetect();
        }
    }
    private void lockBtnSelectImage() {
        fragmentPairingBinding.btnSelectPersonMale.setEnabled(false);
        fragmentPairingBinding.btnSelectPersonFemale.setEnabled(false);
        fragmentPairingBinding.btnSubmit.setEnabled(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        navigateToOtherFragments();
    }

    private void initListener() {
        fragmentPairingBinding.btnUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(PairingFragment.this).navigate(R.id.profileUserFragment);
            }
        });
        fragmentPairingBinding.shapeAbleImageViewMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkClickSetImageMale = false;
                checkCLickImage =1;
                femaleName = String.valueOf(fragmentPairingBinding.edtEnterFemaleName.getText());
                setUpTextWatcher((AppCompatEditText) fragmentPairingBinding.edtEnterFemaleName);
                isNameFilled = true ;
                openDialog(view);
            }
        });

        fragmentPairingBinding.shapeAbleImageViewFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkClickSetImageMale = true;
                checkCLickImage =2;
                maleName = String.valueOf(fragmentPairingBinding.edtEnterMaleName.getText());
                setUpTextWatcher((AppCompatEditText) fragmentPairingBinding.edtEnterMaleName);
                isNameFilled = true;
                openDialog(view);
            }
        });

        fragmentPairingBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override


            public void onClick(View view) {

                initDialog();
                openDialogLoading();

                postData(selectedImageMale,selectedImageFemale);
//
//                if (!isCheckSetImageFemale || !isCheckSetImageMale) {
//                    myDialog = getDialog();
//                    myDialog.setTitle("Can not Face recognition");
//                    myDialog.setContent("Not enough faces have been identified");
//                    myDialog.setContentButton("Ok");
//                    myDialog.show();
//                } else {
//                    Toast.makeText(getActivity(), "Please waiting a time!", Toast.LENGTH_SHORT).show();
//                    // Lock all buttons when running
//                    lockBtnSelectImage();
//                    // Start counting time
//                    startPairingTimeStamp = startCountingTime();
//                    mainActivity.waitingSwapFaceTime = startPairingTimeStamp;
//                    // Compare timestamps and set on view
//                    getTimeIncreaseAndShow(Util.getTimeStampNow(), startPairingTimeStamp);
//
//                    new AsyncTask<Void, Void, Void>() {
//                        @SuppressLint("StaticFieldLeak")
//                        @Override
//                        protected Void doInBackground(Void... params) {
//
//                            urlImageMale = Util.uploadImage2(imgBase64Male, getActivity());
//                            urlImageFemale = Util.uploadImage2(imgBase64Female, getActivity());
//
//                            Log.d("PhongNN", "Male Image URL: " + urlImageMale + "\nFemale Image URL: " + urlImageFemale);
//                            return null;
//                        }
//                        @SuppressLint("StaticFieldLeak")
//                        @Override
//                        protected void onPostExecute(Void result) {
////                            Picasso.get().load(urlImageMale).error(R.drawable.img_heart).into(fragmentPairingBinding.img1);
////                            Picasso.get().load(urlImageFemale).error(R.drawable.img_heart).into(fragmentPairingBinding.img2);
//                            Handler handler = new Handler();
//                            handler.postDelayed(() -> {
//                                postData(urlImageMale, urlImageFemale);
//                            }, 4000);
//                        }
//                    }.execute();
//                }
            }
        });
//        fragmentPairingBinding.genBabyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavHostFragment.findNavController(PairingFragment.this).navigate(R.id.action_pairingFragment_to_genBabyFragment);
//            }
//        });
    }

    private long startCountingTime() {
        long currentTimestamp = Util.getTimeStampNow();
        Log.d("TIME_DURATION", "currentTimestamp: " + currentTimestamp);
        return currentTimestamp;
    }

    private void openDialogLoading() {
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                    callPostEvent.cancel();
            }
        });
        customDialogLoadingBinding.buttonStop.setOnClickListener(v -> {
            callPostEvent.cancel();
            dialog.dismiss();
        });
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

    private void postData(String imageUrl1, String imageUrl2) {
        String nameMale = fragmentPairingBinding.edtEnterMaleName.getText().toString();
        String nameFemale = fragmentPairingBinding.edtEnterFemaleName.getText().toString();
        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Log.d("check_token", "postData: "+ token_auth);

        callPostEvent = apiService.postEvent(imageUrl1,imageUrl2,"Bearer "+token_auth,deviceName,ip_them_su_kien,String.valueOf(id_user) ,nameMale,nameFemale);
        callPostEvent.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    NavHostFragment.findNavController(PairingFragment.this).navigate(R.id.profileUserFragment);
                }else{
                    Toast.makeText(mainActivity, "Error :"+ response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(mainActivity, "Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        UploadingEvent uploadingEvent = new UploadingEvent(
//                deviceName,
//                ip_them_su_kien,
//                id_user,
//                maleName,
//                femaleName
//        );
//
//        Log.d("Anh_check", "postData: "+token_auth+imageUrl1+
//                imageUrl2+
//                uploadingEvent.getDevice_them_su_kien()+
//                uploadingEvent.getIp_them_su_kien()+
//                uploadingEvent.getId_user()+
//                uploadingEvent.getTen_nam()+
//                uploadingEvent.getTen_nu());
//
//        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
//        Call<Object> call = apiService.postEvent(
//                "Bearer "+ token_auth,
//                imageUrl1,
//                imageUrl2,
//                uploadingEvent.getDevice_them_su_kien(),
//                uploadingEvent.getIp_them_su_kien(),
//                id_user,
//                uploadingEvent.getTen_nam(),
//                uploadingEvent.getTen_nu());
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Log.d("Anh_check", "Name: " + response.body());
//
//                    MyOwnDialogFragment myOwnDialogFragment = new MyOwnDialogFragment("Success!",
//                            "Couple Pairing Successfully",
//                            R.drawable.register_success, new MyOwnDialogFragment.MyOwnDialogListener() {
//                        @Override
//                        public void onConfirm() {
//                            // Nav to home fragment
////                            mainActivity.navController.navigate(R.id.fragment_home);
//                            Intent intent = new Intent(getActivity(), MainActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    myOwnDialogFragment.show(getActivity().getSupportFragmentManager(), "pairing_dialog");
//                    // Stop timer when finish merging
//                    stopTimer();
//                }else {
//                    Log.d("Anh_check", "onResponse: "+ response.errorBody().toString());
//                }
//                resetDetect();
//
//                // Create some implicit events
//                CreateImplicitEventImpl.getInstance().generateImplicitEvent(
//                        requireContext(),
//                        uploadingEvent
//                );
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                Log.d("Anh_check", "Name: " + t.getMessage());
////                resetDetect();
////                if (!isCheckSetImageFemale || !isCheckSetImageMale) {
////                    myDialog = getDialog();
////                    myDialog.setTitle("internet connection error");
////                    myDialog.setContent("Make sure internet connection and try again!");
////                    myDialog.setContentButton("OK");
////                    myDialog.show();
////                }
////                stopTimer();
//            }
//        });
    }

    private void getTimeIncreaseAndShow(long stop, long start) {
        long increase = stop - start;
        changeIntoDateTime(increase);
    }

    private void changeIntoDateTime(long increase) {
        long second = increase / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        final long[] finalDays = {hour / 24};
        final long[] finalSeconds = {second % 60};
        final long[] finalMinutes = {minute % 60};
        final long[] finalHours = {hour % 24};

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                String secondsStr, minutesStr, hoursStr, daysStr;
                finalSeconds[0]++;
                if (finalSeconds[0] == 60) {
                    finalSeconds[0] = 0;
                    finalMinutes[0]++;
                }
                if (finalMinutes[0] == 60) {
                    finalMinutes[0] = 0;
                    finalHours[0]++;
                }
                if (finalHours[0] == 24) {
                    finalHours[0] = 0;
                    finalDays[0]++;
                }

                if (finalSeconds[0] < 10) {
                    secondsStr = "0" + finalSeconds[0];
                } else {
                    secondsStr = String.valueOf(finalSeconds[0]);
                }
                if (finalMinutes[0] < 10) {
                    minutesStr = "0" + finalMinutes[0];
                } else {
                    minutesStr = String.valueOf(finalMinutes[0]);
                }
                if (finalHours[0] < 10) {
                    hoursStr = "0" + finalHours[0];
                } else {
                    hoursStr = String.valueOf(finalHours[0]);
                }
                if (finalDays[0] < 10) {

                    daysStr = "0" + finalDays[0];
                } else {
                    daysStr = String.valueOf(finalDays[0]);
                }
                fragmentPairingBinding.timeDay.setText(daysStr);
                fragmentPairingBinding.timeHours.setText(hoursStr);
                fragmentPairingBinding.timeMinute.setText(minutesStr);
                fragmentPairingBinding.timeSecond.setText(secondsStr);
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.post(runnable);
    }
    public void stopTimer() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            elapsedTime = 0;
            mainActivity.waitingSwapFaceTime = 0L;
            mainActivity.maleImage = null;
            mainActivity.femaleImage = null;
        }
    }
    private void navToHomeFragment() {
        NavHostFragment.findNavController(PairingFragment.this).navigate(R.id.action_pairingFragment_to_homeFragment);
    }


    private void openStorage() {
        // Check permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startOpenStorage();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_STORAGE);
        }
    }

    private void openCamera() throws FileNotFoundException {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    startCamera();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getActivity(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_CODE_PERMISSIONS_STORAGE) {
            startOpenStorage();
        }
    }

    private void startOpenStorage() {
        closeDialog();
        isNameFilled = false;
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
                        bottomSheetDialog.dismiss();
                        postImageFile(selectedImageUri);
                    }
                }
            }
    );

    private void loadImage(String link_img) {
        if(checkCLickImage==1){
            Glide.with(this)
                    .load(link_img)
                    .into(fragmentPairingBinding.shapeAbleImageViewMale);
            checkCLickImage =0;
        } else if (checkCLickImage==2) {
            Glide.with(this)
                    .load(link_img)
                    .into(fragmentPairingBinding.shapeAbleImageViewFemale);
            checkCLickImage =0;
        }
    }


    private void startCamera() throws FileNotFoundException {
        closeDialog();
        isNameFilled = false;
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

    @SuppressLint("StaticFieldLeak")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                String imagefile = imageFile.getAbsolutePath();

                Bitmap bitmap = rotaImageHadlee(imagefile);
                if (!checkClickSetImageMale) {
                    detectionFace(bitmap);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        if (resultDetech != null && Objects.equals(resultDetech, "ok")) {
                            try {
                                imgBase64Female = Util.convertBitmapToBase64(bitmap);
                                mainActivity.femaleImage = bitmap;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            isCheckSetImageFemale = true;
//                            fragmentPairingBinding.cvImageFemale.setVisibility(View.VISIBLE);
//                            fragmentPairingBinding.imgFemale.setImageBitmap(bitmap);
                        } else {
                            isCheckSetImageFemale = false;
                        }
                    }, 4000);
                } else {
                    detectionFace(bitmap);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        if (resultDetech != null && Objects.equals(resultDetech, "ok")) {
                            try {
                                imgBase64Male = Util.convertBitmapToBase64(bitmap);
                                mainActivity.maleImage = bitmap;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            isCheckSetImageMale = true;
//                            fragmentPairingBinding.cvImageMale.setVisibility(View.VISIBLE);
//                            fragmentPairingBinding.imgMale.setImageBitmap(bitmap);
                        } else {
                            isCheckSetImageMale = false;
                        }
                    }, 4000);
                }
            } catch (Exception e) {

            }
        }
//        if (resultCode == RESULT_OK && requestCode == IMAGE_PICKER_SELECT) {
//            try {
//                Uri selectedMediaUri = data.getData();
//                Bitmap bitmap;
////                getData(selectedMediaUri);
//
//                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
////                if (selectedMediaUri.toString().contains("image")) {
//                    if (!checkClickSetImageMale) {
//                        Log.d("check_upload_image_your_video", "onActivityResult: "+ selectedMediaUri);
//                        postImageFile(selectedMediaUri);
//                        detectionFace(bitmap);
//                        Handler handler = new Handler();
//                        handler.postDelayed(() -> {
//                            if (resultDetech != null && Objects.equals(resultDetech, "ok")) {
//                                try {
//                                    imgBase64Female = Util.convertBitmapToBase64(bitmap);
//                                    mainActivity.femaleImage = bitmap;
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                                isCheckSetImageFemale = true;
////                                fragmentPairingBinding.cvImageFemale.setVisibility(View.VISIBLE);
////                                fragmentPairingBinding.imgFemale.setImageBitmap(bitmap);
//                            } else {
//                                isCheckSetImageFemale = false;
//                                mainActivity.femaleImage = null;
//                            }
//
//                        }, 4000);
//                    } else {
//                        Log.d("check_upload_image_your_video", "onActivityResult: "+ selectedMediaUri);
//                        detectionFace(bitmap);
//                        Handler handler = new Handler();
//                        handler.postDelayed(() -> {
//                            if (resultDetech != null && Objects.equals(resultDetech, "ok")) {
//                                try {
//                                    imgBase64Male = Util.convertBitmapToBase64(bitmap);
//                                    mainActivity.maleImage = bitmap;
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
////                                imgMalePath = uriToFilePath(selectedMediaUri);
//
//                                isCheckSetImageMale = true;
////                                fragmentPairingBinding.cvImageMale.setVisibility(View.VISIBLE);
////                                fragmentPairingBinding.imgMale.setImageBitmap(bitmap);
//                            } else {
//                                isCheckSetImageMale = false;
//                                mainActivity.maleImage = null;
//                            }
//                        }, 4000);
//                    }
////                }
//            } catch (Exception e) {
//            }
//        }
    }

    private void postImageFile(Uri selectedMediaUri) {
        String filePath = getRealPathFromURI(requireContext(), selectedMediaUri);
        File imageFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN4).getRetrofit().create(ApiService.class);
        call_image_upload = apiService.uploadImage(id_user, "src_nam", imagePart);
        call_image_upload.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
//                    uriResponse = response.body();
                    if(checkCLickImage == 1){
                        selectedImageMale = response.body();
                        loadImage(selectedImageUri.toString());
                    }else{
                        selectedImageFemale = response.body();
                        loadImage(selectedImageUri.toString());
                    }
                    Log.d("check_upload_image_your_video", "onResponse: " + selectedImageFemale+ selectedImageFemale);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("check_upload_image_your_video", "onFailure: " + t.getMessage());
//                dialogErrorBinding.tvError.setText(t.getMessage());
//                dialog_image.dismiss();
//                dialog_error.show();
//                dialog_error.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        call_template_video.cancel();
//                    }
//                });
//                dialogErrorBinding.buttonStop.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        call_template_video.cancel();
//                    }
//                });
            }
        });
    }

//    private void getData(Uri selectedMediaUri) {
//        String filePath = getRealPathFromURI(getContext(), selectedImageUri);
//        File imageFile = new File(filePath);
//        Log.d("check_upload_image", "getData_0: "+ imageFile);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
//        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("src_img", imageFile.getName(), requestBody);
//        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
//        Log.d("check_upload_image", "getData: "+ id_user + imagePart);
//        Call<String> call = apiService.uploadImage(id_user,"src_nam",imagePart);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if(response.isSuccessful()){
//                    Log.d("check_upload_image", "onResponse: "+ response.body());
//                    uriResponse =  response.body();
//                    Log.d("check_upload_image", "onResponse_2: "+ uriResponse);
////                    getVideoSwap(uriResponse);
//                }else {
//                }
//            }
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("check_upload_image", "onFailure: "+ t.getMessage());
//            }
//        });
//    }

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


    private Bitmap rotaImageHadlee(Uri uri) {
        try {
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
            // Tạo Bitmap từ URI
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Đọc thông tin Exif của ảnh
            ExifInterface exifInterface = new ExifInterface(requireActivity().getContentResolver().openInputStream(uri));
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            return rotateBitmap(bitmap, orientation);

            // Hiển thị ảnh đã xoay
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return bitmap;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    private Bitmap rotaImageHadlee(String path) {
        Bitmap bitmap;
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        // Tạo ma trận xoay để hiển thị ảnh đúng hướng
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                // Không xoay ảnh
                break;
        }

// Đọc ảnh từ đường dẫn và áp dụng ma trận xoay (nếu có)
        bitmap = BitmapFactory.decodeFile(path);
        Bitmap photo = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return photo;
    }

    private void openDialog(View view) {

        bottomDialogBinding = DialogBottomSheetSelectedHomeBinding.inflate(LayoutInflater.from(getContext()));
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomDialogBinding.getRoot());

//        setUpTextWatcher(bottomDialogBinding.edtEnterName);
//
//        if (!checkClickSetImageMale) {
//            // get name
//            femaleName = bottomDialogBinding.edtEnterName.getText().toString();
//        } else {
//            maleName = bottomDialogBinding.edtEnterName.getText().toString();
//        }

        bottomDialogBinding.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameFilled)
                    openStorage();
                else {
                    Toast.makeText(mainActivity, "Please enter name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomDialogBinding.btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isNameFilled)
                        openCamera();
                    else {
                        Toast.makeText(mainActivity, "Please enter name", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        bottomSheetDialog.show();
    }

    private void setUpTextWatcher(AppCompatEditText edtEnterName) {
        edtEnterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNameFilled = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void closeDialog() {
        if (bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
    }


    private String detectionFace(Bitmap bitmap) {
        if (!kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
        resultDetech = "";
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setMinFaceSize(0.15f)
                        .enableTracking()
                        .build();

        FaceDetector faceDetector = FaceDetection.getClient(options);
        // Tải ảnh từ storage
        //        String imagePath = ...; // Đường dẫn tới ảnh trong storage
        //        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        // Tạo đối tượng InputImage từ bitmap
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        // Nhận dạng khuôn mặt từ ảnh
        Task<List<Face>> result = faceDetector.process(image)
                .addOnSuccessListener(faces -> {
                    resultDetech = processFaceDetectionResult(faces);
                    if (!resultDetech.equals("ok")) {
                        myDialog = getDialog();
                        myDialog.setTitle("Can not process the face recognition");
                        myDialog.setContent(resultDetech);
                        myDialog.setContentButton("OK");
                        myDialog.show();
                    } else {
                        if (!checkClickSetImageMale) {
                            fragmentPairingBinding.cvImageFemale.setVisibility(View.VISIBLE);
                            fragmentPairingBinding.imgFemale.setImageBitmap(bitmap);
                        } else {
                            fragmentPairingBinding.cvImageMale.setVisibility(View.VISIBLE);
                            fragmentPairingBinding.imgMale.setImageBitmap(bitmap);
                        }
                    }
                    hideHub();
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi có lỗi xảy ra trong quá trình nhận dạng khuôn mặt
                    fragmentPairingBinding.cvImageFemale.setVisibility(View.GONE);
                    fragmentPairingBinding.cvImageMale.setVisibility(View.GONE);
                    Toast.makeText(mainActivity, "Fail to recognize face", Toast.LENGTH_SHORT).show();
                    hideHub();
                });

        return resultDetech;
    }

    private String processFaceDetectionResult(List<Face> faces) {
        String result = "ok";
        List<Face> faceList = new ArrayList<>();

        for (Face face : faces) {
            Rect bounds = face.getBoundingBox();
            if (bounds.width() >= 150 || bounds.height() >= 150) {
                faceList.add(face);
            }
        }
        if (faceList.size() == 0) {
            return NO_FACE_DETECTED;
        }
        if (faceList.size() > 1) {
            return MORE_THAN_ONE_FACE;
        }
//


        Face face = faceList.get(0);
//        Rect bounds = face.getBoundingBox();
//        if (bounds.width() <= 30 || bounds.height() <= 30) {
//            return "face size is too small ";
//        }

        float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
        float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
        if (rotY > 30 || rotZ > 30 || rotZ < -30 || rotY < -30
        ) {
            return "the photo is tilted or because there are not enough eyes, nose, mouth";
        }
        FaceLandmark leftEye = face.getLandmark(FaceLandmark.LEFT_EYE);
        FaceLandmark rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE);
        FaceLandmark nose = face.getLandmark(FaceLandmark.NOSE_BASE);
        FaceLandmark mouth = face.getLandmark(FaceLandmark.MOUTH_BOTTOM);
        FaceLandmark cheekRight = face.getLandmark(FaceLandmark.RIGHT_CHEEK);
        FaceLandmark cheekLeft = face.getLandmark(FaceLandmark.LEFT_CHEEK);
        if (leftEye == null || rightEye == null || nose == null || mouth == null || cheekLeft == null || cheekRight == null) {
            return "the picture is too blurry or because there are not enough eyes, nose, mouth";
        }
        return result;
    }


//    private void goToEventDetail(int id) {
//        mainActivity.eventSummaryCurrentId = id;
//        mainActivity.setCurrentPage(3);
//
//    }

    private void hideHub() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
        }, 2000);
    }

    private MyDialog getDialog() {
        if (myDialog == null) {
            myDialog = MyDialog.getInstance(getContext());
            Window window = myDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.gravity = Gravity.CENTER;// Thiết lập vị trí ở giữa dưới
                layoutParams.y = 300; // Đặt khoảng cách dịch chuyển theo chiều dọc (30dp)
                window.setAttributes(layoutParams);
            }
        }
        return myDialog;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void resetDetect() {
        fragmentPairingBinding.imgFemale.setImageDrawable(null);
        fragmentPairingBinding.imgMale.setImageDrawable(null);
        fragmentPairingBinding.cvImageFemale.setVisibility(View.GONE);
        fragmentPairingBinding.cvImageMale.setVisibility(View.GONE);

        fragmentPairingBinding.btnSelectPersonMale.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.img_add_male));
        fragmentPairingBinding.btnSelectPersonFemale.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.img_add_female));

        isCheckSetImageFemale = false;
        isCheckSetImageMale = false;
        imgBase64Female = "";
        imgBase64Male = "";
    }
}
