package com.thinkdiffai.futurelove.presenter.api;

import android.util.Log;

import com.thinkdiffai.futurelove.model.ipinfor.IpInfoResponse;
import com.thinkdiffai.futurelove.service.api.IpInfoService;
import com.thinkdiffai.futurelove.service.api.IpRetrofitClient;
import com.thinkdiffai.futurelove.service.api.QueryValueCallback;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityCalledByIpApi {

    private ArrayList<String> cityList;

    private IpInfoService ipInfoService;

    public CityCalledByIpApi() {
        ipInfoService = IpRetrofitClient.getClient().create(IpInfoService.class);
    }

    public static CityCalledByIpApi getInstance() {
        return new CityCalledByIpApi();
    }

    public void getCityName(String ip, QueryValueCallback callback) {
        Call<IpInfoResponse> call = ipInfoService.getIpInfo(ip);
        call.enqueue(new Callback<IpInfoResponse>() {
            @Override
            public void onResponse(Call<IpInfoResponse> call, Response<IpInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    IpInfoResponse ipInfoResponse = response.body();
                    String cityName = ipInfoResponse.getCity();
                    if (!Objects.equals(cityName, "")) {
                        callback.onQueryValueReceived(cityName);
                    }
                } else {
                    Log.e("City", "Error: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<IpInfoResponse> call, Throwable t) {
                Log.e("City", "Error: " + t.getMessage());
            }
        });
    }

}
