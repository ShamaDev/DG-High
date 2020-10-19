package com.dghigh.liva.utils;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AppLogsInterface {

    @POST("/applogs")
    Call<String> sendLogs(@Header("content-type") String contentType,
                          @Body JsonObject body);
}
