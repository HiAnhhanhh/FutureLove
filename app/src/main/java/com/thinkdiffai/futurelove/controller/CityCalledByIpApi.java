package com.thinkdiffai.futurelove.controller;

import android.util.Log;

import com.thinkdiffai.futurelove.service.api.ApiService;
import com.thinkdiffai.futurelove.service.api.QueryValueCallback;
import com.thinkdiffai.futurelove.service.api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityCalledByIpApi {

    private ApiService apiService;
    private String city = "Hanoi";

    public static CityCalledByIpApi getInstance() {
        return new CityCalledByIpApi();
    }

    public String getCityFromIpAddress(String domain, String ip) {
        apiService = RetrofitClient.getInstance(domain).getRetrofit().create(ApiService.class);
        Call<String> call = apiService.getCityNameFromIpAddress(ip);
        Log.d("CITY_NAME","Callback: " + call.toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String returnCity = response.body();
                    Log.d("CITY_NAME","City name:" + returnCity);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("CITY_NAME", "CALL FAILED");
            }
        });
        return city;
    }
}
