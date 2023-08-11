package com.thinkdiffai.futurelove.service.api;

import com.thinkdiffai.futurelove.model.ipinfor.IpInfoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IpInfoService {
    @GET("{ip}/json")
    Call<IpInfoResponse> getIpInfo(@Path("ip") String ip);
}

