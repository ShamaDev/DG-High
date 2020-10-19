package com.dghigh.liva.utils;

import android.content.Context;

import com.google.gson.JsonObject;
import com.dghigh.liva.R;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppLogsRest {

    public void sendLogs(Context context, boolean error, String error_message) {

        OkHttpClient.Builder client = new OkHttpClient.Builder();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(Params.APP_LOGS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        AppLogsInterface appLogsInterface = mRetrofit.create(AppLogsInterface.class);

        String contentType = Params.CONTENT_TYPE;
        JsonObject logsRequest = new JsonObject();
        JsonObject conversionDataStatus = new JsonObject();

        conversionDataStatus.addProperty("error", error);
        conversionDataStatus.addProperty("error_message", error_message);
        logsRequest.addProperty("app_name", context.getResources().getString(R.string.app_name));
        logsRequest.addProperty("app_id", context.getPackageName());
        logsRequest.add("conversion_data_status", conversionDataStatus);

        Call<String> call = appLogsInterface.sendLogs(contentType, logsRequest);

        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
}
