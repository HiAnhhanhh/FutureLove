package com.thinkdiffai.futurelove.view.fragment.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ActivityAddEventBinding;
import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingBinding;
import com.thinkdiffai.futurelove.databinding.CustomDialogLoadingImageBinding;
import com.thinkdiffai.futurelove.model.Comon;
import com.thinkdiffai.futurelove.model.ErrorModel;
import com.thinkdiffai.futurelove.model.EventHomeDto;
import com.thinkdiffai.futurelove.model.IpNetworkModel;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.RetrofitIp;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventActivity extends AppCompatActivity {
    private ActivityAddEventBinding activityAddEventBinding;
    Bitmap bitmap;

    String ip_them_su_kien,deviceName;
    String token_auth;
    int id_user;
    String uriResponse;
    Call<String> call_image_upload;
    CustomDialogLoadingImageBinding customDialogLoadingImageBinding;
    Dialog dialog_image;
    String UriResponseReplace;

    final int GALERY_REQUEST = 456;
    String imgBase64Female= "";
    String urlImageComment= "";

    private KProgressHUD kProgressHUD;
    private MainActivity mainActivity;
    String urlImage="https://i.pinimg.com/564x/35/b1/2c/35b12ccefb3f9946e62d95d656845be3.jpg";
    int id_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        activityAddEventBinding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(activityAddEventBinding.getRoot());
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        //   kProgressHUD = mainActivity.createHud();
        Bundle bundle=getIntent().getBundleExtra("send_id");
         id_event=bundle.getInt("id_event");

         openDialog();
         initData();

       // String Content = activityAddEventBinding.btnSelectImage.getText().toString().trim();

        activityAddEventBinding.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AddEventActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},GALERY_REQUEST);
            }
        });
        activityAddEventBinding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activityAddEventBinding.addEvnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEventDetail();
            }
        });

    }

    private void initData() {
        loadIdUser();
        callApiAddress();
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
                Toast.makeText(AddEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialog() {
        customDialogLoadingImageBinding = CustomDialogLoadingImageBinding.inflate(LayoutInflater.from(AddEventActivity.this));
        dialog_image = new Dialog(AddEventActivity.this);
        dialog_image.setContentView(customDialogLoadingImageBinding.getRoot());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==GALERY_REQUEST){
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALERY_REQUEST);
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "You have not granted access permission", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_REQUEST && resultCode == RESULT_OK && data!=null) {
            Uri uri =data.getData();
            postImageFile(uri);
            Log.d("check_uri_image", "onActivityResult: "+ uri);
            try {
                  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                bitmap = rotaImageHadlee(selectedMediaUri);
                //  if (uri.toString().contains("image")) {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                //     bitmap = BitmapFactory.decodeStream(inputStream);
                 imgBase64Female = Util.convertBitmapToBase64(bitmap);
                        activityAddEventBinding.btnSelectImage.setImageBitmap(bitmap);
                //  }
            } catch (IOException e) {
                e.printStackTrace();
            throw new RuntimeException(e);
            }
        }
    }
    private void loadIdUser() {
        SharedPreferences sharedPreferences = AddEventActivity.this.getSharedPreferences("id_user",0);
        String id_user_str = sharedPreferences.getString("id_user_str","");
        token_auth = sharedPreferences.getString("token","");
        Log.d("check_share_id", "loadIdUser: "+ id_user_str +"_" +token_auth);
        if(id_user_str.equals("")){
            id_user = 0;
        }else{
            id_user = Integer.parseInt(id_user_str);
        }
    }
    private void postImageFile(Uri uri) {
        dialog_image.show();
        String filePath = getRealPathFromURI(AddEventActivity.this, uri);
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
                    UriResponseReplace = uriResponse.replace("/var/www/build_futurelove","https://futurelove.online");
                    dialog_image.dismiss();
                    Log.d("check_upload_image_your_video", "onResponse: " + uriResponse);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check_upload_image_your_video", "onFailure: " + t.getMessage());
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

    private void postEventDetail() {
        String NumberOrder = activityAddEventBinding.tvNumberOrder.getText().toString().trim();
        float numberOrder =Float.parseFloat(NumberOrder);
        String Date = activityAddEventBinding.tvDate.getText().toString().trim();
        String Title = activityAddEventBinding.tvTitle.getText().toString().trim();
        String Content = activityAddEventBinding.tvContent.getText().toString().trim();
        if(!Content.isEmpty()&&!Title.isEmpty()&&!Date.isEmpty()&&!NumberOrder.isEmpty()) {
            if (!kProgressHUD.isShowing()) {
                kProgressHUD.show();
            }
            Log.d("PairingFragmentPostRequest", UriResponseReplace + token_auth + id_event  );

            EventHomeDto eventHomeDto = new EventHomeDto(id_event,  UriResponseReplace, Comon.link_nam_chua_swap, Comon.link_nam_goc, Comon.link_nu_chua_swap, Comon.link_nu_goc, Content, Date, numberOrder, Title, Comon.tom_Luoc_Text);
            Toast.makeText(AddEventActivity.this,  id_event+"", Toast.LENGTH_SHORT).show();

        ApiService apiService = RetrofitClient.getInstance("").getRetrofit().create(ApiService.class);
        Call<Object> call = apiService.postListEventDetail(String.valueOf(id_event),eventHomeDto.getLink_nam_chua_swap(),eventHomeDto.getLink_nu_chua_swap(),"Bearer "+ token_auth,id_user,urlImage,"male","female",Content,deviceName,ip_them_su_kien,Title,1,"");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.d("PairingFragmentPostRequest","thành công" );
                    Toast.makeText(AddEventActivity.this,  "thành công", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        // Parse the error response body
                        String errorBodyString = response.errorBody().source().readUtf8();
                        Gson gson = new Gson(); // Tạo một đối tượng Gson để phân tích chuỗi JSON
                        ErrorModel errorModel = gson.fromJson(errorBodyString, ErrorModel.class);
                        // Now you can access the error message
                        String errorMessage = errorModel.getMessage();
                        Log.d("PairingFragmentPostRequest", "onResponse: "+ response.code() + " "+ errorMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(AddEventActivity.this, t.toString() + "thất bại", Toast.LENGTH_SHORT).show();
                Log.d("PairingFragmentPostRequest", t.toString());
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
        });
        }
    }
}