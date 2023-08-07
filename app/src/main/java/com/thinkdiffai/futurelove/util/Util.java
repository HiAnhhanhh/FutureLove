package com.thinkdiffai.futurelove.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Util {
    public static KProgressHUD kProgressHUD;
    public static String patternDate = "yyyy-MM-dd HH:mm:ss";
    public static String patternDate2 = "yyyy-MM-dd, HH:mm:ss";
    public static SimpleDateFormat dateFormat;
    public static KProgressHUD getkProgressHUD(Context context){
        if (kProgressHUD==null){
            kProgressHUD = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Downloading data")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        }
        return kProgressHUD;
    }

    public static long getTimeStampNow() {
        Instant instant = null;
        long timestamp = 0;
        ZonedDateTime zonedDateTime = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            ZoneId zoneId = ZoneId.of("Pacific/Kiritimati");

            zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), zoneId);
            instant = zonedDateTime.toInstant();
            timestamp = instant.toEpochMilli();
        }
        return timestamp;
    }


    //    public String uriToFilePath(Uri uri) {
//        String filePath = null;
//        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
//                if (columnIndex != -1) {
//                    filePath = cursor.getString(columnIndex);
//                }
//                cursor.close();
//            }
//        } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
//            filePath = uri.getPath();
//        }
//        return filePath;
//    }

//    private String getRealPathFromURI(Uri contentURI) {
//        String result;
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }

    public  static String uploadImage2(String imageBase64, Context context) {
        OkHttpClient client = new OkHttpClient();

//        String boundary = "Boundary-" + UUID.randomUUID().toString();
//        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=" + boundary);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", Constant.KEY_IMG_BB)
                .addFormDataPart("image", imageBase64)
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(requestBody)
                .build();
        String imageUrl = "";
        try {
            // Thực hiện yêu cầu và lấy phản hồi trả về
            okhttp3.Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            response.close();
            // Trích xuất URL của hình ảnh từ phản hồi JSON của ImgBB
            JSONObject jsonObject = new JSONObject(responseBody);
            imageUrl = jsonObject.getJSONObject("data").getString("url");
            // In ra URL của hình ảnh đã tải lê
        } catch (IOException | JSONException e) {
            Util.getkProgressHUD(context).dismiss();
            e.printStackTrace();
        }
        return imageUrl;
    }

    public static String uploadImage(String path) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        File imageFile = new File(path);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile))
                .addFormDataPart("key", Constant.KEY_IMG_BB)
                .build();

        Request request = new Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            String imageUrl = json.getJSONObject("data").getString("url");
            // Use the imageUrl as needed
            return imageUrl;
        } else {
            return "";
        }

    }

    public static String convertBitmapToBase64(Bitmap bitmap) throws IOException {

//        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private static long convertDateToTimeStamp(String timeString) {
        long timestamp;
        try {
            Date date ;
            if(isFormat(timeString, patternDate)){
                date = getDateFormat().parse(timeString);
            }else {
                date = getDateFormat2().parse(timeString);
            }

            timestamp = date.getTime();
        } catch (ParseException e) {
            timestamp = 0;
        }
        return timestamp;
    }

    public static String calTimeStampComment(String date) {
        Date date1 = new Date();
        long testTimeSta = date1.getTime();
        long currentTimestampGMT = Util.getTimeStampNow();
        long elapsedTimeMillis = currentTimestampGMT - convertDateToTimeStamp(date);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis);
        long days = TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis);
        long months = days / 30;
        long years = months / 12;

        if (years > 0) {
            return years + " year ago";
        } else if (months > 0) {
            return months + " mon ago";
        } else if (days > 0) {
            return days + " day ago";
        } else if (hours > 0) {
            return hours + " hour ago";
        } else if (minutes > 0) {
            return minutes + " min ago";
        } else {
            return " just now";
        }
    }

    private static boolean isFormat(String timeString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);

        try {
            // Kiểm tra xem chuỗi thời gian có phù hợp với định dạng không
            Date date = sdf.parse(timeString);
            String formattedTimeString = sdf.format(date);

            // Kiểm tra xem chuỗi thời gian đã được chuyển đổi thành đúng định dạng chưa
            return timeString.equals(formattedTimeString);
        } catch (ParseException e) {
            // Nếu có lỗi ParseException, tức là chuỗi thời gian không phù hợp với định dạng
            return false;
        }
    }

    public static SimpleDateFormat getDateFormat(){
        return   dateFormat = new SimpleDateFormat(patternDate);
    }
    public static SimpleDateFormat getDateFormat2(){
        return   dateFormat = new SimpleDateFormat(patternDate2);
    }

}