package com.thinkdiffai.futurelove.service.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IpRetrofitClient {
//    private static final String BASE_URL = "http://ipinfo.io/";
//    private static final String BASE_URL = "http://192.168.50.146:9090/";

    private static final String BASE_URL = "https://metatechvn.store/";


    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
