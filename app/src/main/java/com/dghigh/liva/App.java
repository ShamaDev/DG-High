package com.dghigh.liva;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dghigh.liva.utils.Params;
import com.dghigh.liva.utils.PreferencesOutsiderMan;
import com.yandex.metrica.AppMetricaDeviceIDListener;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;


public class App extends Application {
    public static SharedPreferences hashSettings;


    @Override
    public void onCreate() {
        super.onCreate();

        hashSettings = getSharedPreferences( getPackageName() + "_sharedprefs", MODE_PRIVATE);

        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(Params.APPMETRICA_DEV_KEY).build();
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking(this);
        YandexMetrica.requestAppMetricaDeviceID(new AppMetricaDeviceIDListener() {
            @Override
            public void onLoaded(@Nullable String s) {
                PreferencesOutsiderMan.setParam(Params.APPMETRICA_ID, s);
            }

            @Override
            public void onError(@NonNull Reason reason) {
                Log.e("DeviceIDListener", reason.toString());
            }
        });
    }
}
