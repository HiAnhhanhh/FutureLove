package com.thinkdiffai.futurelove.service.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*
* Usage: call api for local ip address
* */
public class RetrofitIp {

    private static Retrofit retrofit;

    private static RetrofitIp instance;

    private RetrofitIp(String domain) {
//        interceptor.setLevel(Interceptor.Level.BODY);
        Interceptor interceptor = chain -> {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            return chain.proceed(builder.build());
        };
        OkHttpClient.Builder okBuilder =
                new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .callTimeout(200000, TimeUnit.MILLISECONDS) // Timeout cho toàn bộ cuộc gọi (bao gồm cả kết nối và đọc/phản hồi)
                        .connectTimeout(200000, TimeUnit.MILLISECONDS)
                        .readTimeout(200000, TimeUnit.MILLISECONDS);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(domain)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okBuilder.build())
                .build();
    }

    public static synchronized RetrofitIp getInstance(String domain) {
        if (instance == null) {
            instance = new RetrofitIp(domain);
        }
        return instance;
    }

    // Get the Retrofit instance
    public Retrofit getRetrofit() {
        return retrofit;
    }

}
