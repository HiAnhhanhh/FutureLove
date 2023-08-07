package com.thinkdiffai.futurelove.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.thinkdiffai.futurelove.R;
import com.thinkdiffai.futurelove.databinding.ActivityAddEventBinding;
import com.thinkdiffai.futurelove.model.Comon;
import com.thinkdiffai.futurelove.model.EventHomeDto;
import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;
import com.thinkdiffai.futurelove.service.api.Server;
import com.thinkdiffai.futurelove.util.Util;

import java.io.IOException;
import java.io.InputStream;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventActivity extends AppCompatActivity {
    private ActivityAddEventBinding activityAddEventBinding;
    Bitmap bitmap;
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
             //   postEventDetail();

            }
        });

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
            try {
                  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                bitmap = rotaImageHadlee(selectedMediaUri);
                //  if (uri.toString().contains("image")) {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                //     bitmap = BitmapFactory.decodeStream(inputStream);
                 imgBase64Female = Util.convertBitmapToBase64(bitmap);

              /*  new AsyncTask<Void, Void, Void>() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    protected Void doInBackground(Void... params) {
                        urlImageComment = Util.uploadImage2(imgBase64Female, AddEventActivity.this);
                        Toast.makeText(AddEventActivity.this,  urlImageComment, Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    @SuppressLint("StaticFieldLeak")
                    @Override
                    protected void onPostExecute(Void result) {


                    }
                }.execute();*/


                //   urlImageComment = Util.uploadImage2(imgBase64Female, AddEventActivity.this);

                        /* if (imgBase64Female != null && !imgBase64Female.trim().isEmpty() ) {
                            urlImageComment = Util.uploadImage2(imgBase64Female, AddEventActivity.this);
                       } */
                        activityAddEventBinding.btnSelectImage.setImageBitmap(bitmap);
                //  }
            } catch (IOException e) {
                e.printStackTrace();
            throw new RuntimeException(e);
            }
        }
    }

    private void postEventDetail() {

        String NumberOrder = activityAddEventBinding.tvNumberOrder.getText().toString().trim();
        float numberOrder =Float.parseFloat(NumberOrder);
        String Date = activityAddEventBinding.tvDate.getText().toString().trim();
        String Title = activityAddEventBinding.tvTitle.getText().toString().trim();
        String Content = activityAddEventBinding.tvContent.getText().toString().trim();
        if(!Content.isEmpty()&&!Title.isEmpty()&&!Date.isEmpty()&&!NumberOrder.isEmpty()) {


            Toast.makeText(AddEventActivity.this,  Content, Toast.LENGTH_SHORT).show();
            if (!kProgressHUD.isShowing()) {
                kProgressHUD.show();
            }

        EventHomeDto eventHomeDto = new EventHomeDto(id_event,  urlImage, Comon.link_nam_chua_swap, Comon.link_nam_goc, Comon.link_nu_chua_swap, Comon.link_nu_goc, Content, Date, numberOrder, Title, Comon.tom_Luoc_Text);
            Toast.makeText(AddEventActivity.this,  id_event+"", Toast.LENGTH_SHORT).show();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("noi_dung_cmt", comment.getNoi_dung_cmt());
//        headers.put("device_cmt", comment.getDevice_cmt());
//        headers.put("id_toan_bo_su_kien", String.valueOf(comment.getId_toan_bo_su_kien()));
//        headers.put("ipComment", comment.getDia_chi_ip());
//        headers.put("imageattach", comment.getImageattach());

        ApiService apiService = RetrofitClient.getInstance(Server.DOMAIN2).getRetrofit().create(ApiService.class);
        Call<Object> call = apiService.postListEventDetail(String.valueOf(eventHomeDto.getId()), eventHomeDto.getLink_da_swap(),
                (eventHomeDto.getLink_nam_chua_swap()), eventHomeDto.getLink_nam_goc(), eventHomeDto.getLink_nu_chua_swap(),eventHomeDto.getLink_nu_goc(), eventHomeDto.getNoi_dung_su_kien(), eventHomeDto.getReal_time(),String.valueOf( eventHomeDto.getSo_thu_tu_su_kien()), eventHomeDto.getTen_su_kien(), eventHomeDto.getTom_Luoc_Text());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    xu ly sau khi commrnt
                    //  getDataComment();
                    Log.d("PairingFragmentPostRequest","thành công" );
                    Toast.makeText(AddEventActivity.this,  "thành công", Toast.LENGTH_SHORT).show();
                    finish();
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