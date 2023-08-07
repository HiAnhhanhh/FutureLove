package com.thinkdiffai.futurelove.view.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.FragmentPairingBinding;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.util.MyDialog;
import com.thinkdiffai.futurelove.util.Util;
import com.thinkdiffai.futurelove.view.activity.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;

public class PairingFragment extends Fragment {
    private FragmentPairingBinding fragmentPairingBinding;
    private static final int IMAGE_PICKER_SELECT = 1889;
    private BottomSheetDialog bottomSheetDialog;
    private static final int CAMERA_REQUEST = 1888;


    private static final String TAG = "CameraActivity";
    private static final String TAG1 = MainActivity.class.getName();

    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final int REQUEST_CODE_PERMISSIONS_STORAGE = 101;
    private boolean checkClickSetImageMale;
    private boolean isCheckSetImageFemale = false;
    private boolean isCheckSetImageMale = false;
    private File imageFile;
    private KProgressHUD kProgressHUD;
    private String resultDetech;
    private String imgBase64Male;
    private String imgBase64Female;
    private String urlImageMale;
    private String urlImageFemale;
    private MainActivity mainActivity;
    private MyDialog myDialog;

//    private ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    Log.e(TAG1, "onActivityResult");
//                    if (result.getResultCode() == RESULT_OK) {
//                        // There are no request code
//                        Intent data = result.getData();
//                        if (data == null) {
//                            return;
//                        }
//
//                        Uri uri = data.getData();
//                        try {
//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), uri);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            }
//    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPairingBinding = FragmentPairingBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();
        kProgressHUD = KProgressHUD.create(requireContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


//        checkClickSetImageMale =  true;
        try {
            initListener();

        } catch (Exception e) {
            Log.e("ExceptionRuntime", e.toString());
        }

        return fragmentPairingBinding.getRoot();
    }

    private void initListener() {
        fragmentPairingBinding.btnSelectPersonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkClickSetImageMale = false;
                openDialog(view);
            }
        });

        fragmentPairingBinding.btnSelectPersonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkClickSetImageMale = true;
                openDialog(view);
            }
        });

        fragmentPairingBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                if (!isCheckSetImageFemale || !isCheckSetImageMale) {
                    myDialog = getDialog();
                    myDialog.setTitle("Can not Face recognition");
                    myDialog.setContent("Not enough faces have been identified");
                    myDialog.setContentButton("Ok");
                    myDialog.show();
                } else {
                    kProgressHUD.show();
//                    urlImageMale = uploadImage2(imgBase64Male);
//                    urlImageFemale = uploadImage2(imgBase64Female);
//                    Handler handler = new Handler();
//                    handler.postDelayed(() -> {
//                        postData(urlImageMale, urlImageFemale);
//                    }, 4000);
//
//                    if (kProgressHUD.isShowing())
//                        kProgressHUD.dismiss();

                    new AsyncTask<Void, Void, Void>() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected Void doInBackground(Void... params) {

                            urlImageMale = Util.uploadImage2(imgBase64Male, getActivity());
                            urlImageFemale = Util.uploadImage2(imgBase64Female, getActivity());

                            Log.d("PhongNN", "Male Image URL: " + urlImageMale + "\nFemale Image URL: " + urlImageFemale);
                            return null;
                        }

                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected void onPostExecute(Void result) {
//                            Picasso.get().load(urlImageMale).error(R.drawable.img_heart).into(fragmentPairingBinding.img1);
//                            Picasso.get().load(urlImageFemale).error(R.drawable.img_heart).into(fragmentPairingBinding.img2);
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                postData(urlImageMale, urlImageFemale);
                            }, 4000);
                        }
                    }.execute();
                }


            }
        });
    }

    private void postData(String imageUrl1, String imageUrl2) {
        if (!kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
//        Map<String, String> headers = new HashMap<>();
//        headers.put(Server.KEY_HEADER1, imageUrl1);
//        headers.put(Server.KEY_HEADER2, imageUrl2);
        String deviceThemSuKien = "Iphone 14 Pro mux";
        String ipThemSuKien = "3.3.3.3";
        int idUser = 1;
        String tenNam = "Lam";
        String tenNu = "Lu";

        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<Object> call = apiService.postEvent(imageUrl1, imageUrl2, deviceThemSuKien, ipThemSuKien, idUser, tenNam, tenNu);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("PhongNN", "Post Image Success");
                    myDialog = getDialog();
                    myDialog.setTitle("Successfully!");
                    myDialog.setContent("Post the two images of a couple successfully!");
                    myDialog.setContentButton("OK");
                    myDialog.show();
//                    List<ResponsePairingDto.TimeResponse> eventFutures = responsePairingDto.getJson2();
//                    mainActivity.eventSummaryCurrentId = eventFutures.get(0).getId_toan_bo_su_kien();
//
//                    goToEventDetail(mainActivity.eventSummaryCurrentId);

                }
                resetDetect();
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                resetDetect();
                if (!isCheckSetImageFemale || !isCheckSetImageMale) {
                    myDialog = getDialog();
                    myDialog.setTitle("internet connection error");
                    myDialog.setContent("Make sure internet connection and try again!");
                    myDialog.setContentButton("Ok");
                    myDialog.show();
                }

                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
        });
    }


    private void openStorage() {
        // Check permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startOpenStorage();
        } else {
            // If not granted, request permission
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
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
    }

    private void startCamera() throws FileNotFoundException {
        closeDialog();
        File cacheDir = Objects.requireNonNull(getActivity()).getApplicationContext().getCacheDir();
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
        kProgressHUD.show();
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                String imagefile = imageFile.getAbsolutePath();

                Bitmap bitmap = rotaImageHadlee(imagefile);
                if (!checkClickSetImageMale) {

//                    mActivityMainBinding.btnSelectPersonFemale.setImageBitmap(bitmap);

                    detectionFace(bitmap);

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        if (resultDetech != null && Objects.equals(resultDetech, "")) {
                            imgBase64Female = imagefile;
                            isCheckSetImageFemale = true;
                            fragmentPairingBinding.cvImageFemale.setVisibility(View.VISIBLE);
                            fragmentPairingBinding.imgFemale.setImageBitmap(bitmap);
                        } else {
                            isCheckSetImageFemale = false;
                        }

                    }, 4000);
                } else {

                    detectionFace(bitmap);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        if (Objects.equals(resultDetech.toString(), "")) {
                            imgBase64Male = imagefile;
                            isCheckSetImageMale = true;
                            fragmentPairingBinding.cvImageMale.setVisibility(View.VISIBLE);
                            fragmentPairingBinding.imgMale.setImageBitmap(bitmap);
                        } else {
                            isCheckSetImageMale = false;
                        }
                    }, 4000);

                }
            } catch (Exception e) {

            }
        }
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICKER_SELECT) {
            try {
                Uri selectedMediaUri = data.getData();
                Bitmap bitmap;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
//                bitmap = rotaImageHadlee(selectedMediaUri);
                if (selectedMediaUri.toString().contains("image")) {
                    if (!checkClickSetImageMale) {

                        detectionFace(bitmap);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            if (Objects.equals(resultDetech.toString(), "")) {
                                try {
                                    imgBase64Female = Util.convertBitmapToBase64(bitmap);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                isCheckSetImageFemale = true;
                                fragmentPairingBinding.cvImageFemale.setVisibility(View.VISIBLE);
                                fragmentPairingBinding.imgFemale.setImageBitmap(bitmap);
                            } else {
                                isCheckSetImageFemale = false;
                            }

                        }, 4000);

                    } else {

                        detectionFace(bitmap);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            if (Objects.equals(resultDetech.toString(), "")) {
                                try {
                                    imgBase64Male = Util.convertBitmapToBase64(bitmap);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
//                                imgMalePath = uriToFilePath(selectedMediaUri);

                                isCheckSetImageMale = true;
                                fragmentPairingBinding.cvImageMale.setVisibility(View.VISIBLE);
                                fragmentPairingBinding.imgMale.setImageBitmap(bitmap);
                            } else {
                                isCheckSetImageMale = false;
                            }

                        }, 4000);
                    }
//
                }
//

            } catch (Exception e) {
            }
        }
        hideHub();

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
        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet_selected_home, null);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(viewDialog);
        ImageButton btnSelectImage = viewDialog.findViewById(R.id.btn_select_image);
        ImageButton btnOpenCamera = viewDialog.findViewById(R.id.btn_open_camera);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStorage();
            }
        });

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openCamera();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        bottomSheetDialog.show();
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
////// Tải ảnh từ storage
////        String imagePath = ...; // Đường dẫn tới ảnh trong storage
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

// Tạo đối tượng InputImage từ bitmap
        InputImage image = InputImage.fromBitmap(bitmap, 0);

// Nhận dạng khuôn mặt từ ảnh
        Task<List<Face>> result = faceDetector.process(image)
                .addOnSuccessListener(faces -> {
//                    resultDetech.set(processFaceDetectionResult(faces));

                    resultDetech = processFaceDetectionResult(faces);
                    if (!resultDetech.toString().equals("")) {
                        myDialog = getDialog();
                        myDialog.setTitle("Can not process the face recognition");
                        myDialog.setContent(resultDetech.toString());
                        myDialog.setContentButton("Ok");
                        myDialog.show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi có lỗi xảy ra trong quá trình nhận dạng khuôn mặt
                });

        return resultDetech;
    }

    private String processFaceDetectionResult(List<Face> faces) {
        String result = "";
        List<Face> faceList = new ArrayList<>();

        for (Face face : faces) {
            Rect bounds = face.getBoundingBox();
            if (bounds.width() >= 150 || bounds.height() >= 150) {
                faceList.add(face);
            }
        }
        if (faceList.size() == 0) {
            return "No faces detected";
        }
        if (faceList.size() > 1) {
            return "more than one face is recognized";
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


    private void goToEventDetail(int id) {
        mainActivity.eventSummaryCurrentId = id;
        mainActivity.setCurrentPage(3);

    }

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
        isCheckSetImageFemale = false;
        isCheckSetImageMale = false;
        imgBase64Female = "";
        imgBase64Male = "";
    }
}
