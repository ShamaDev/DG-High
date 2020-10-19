package com.dghigh.liva.utils;

import android.content.Context;
import android.provider.Settings;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

public class AdvertisingIdCollector {

    public String collect(Context context) {

        AdvertisingIdClient.Info idInfo;
        String advertisingId;

        try {

            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            advertisingId = idInfo.getId();

        } catch (GooglePlayServicesNotAvailableException
                | GooglePlayServicesRepairableException
                | IOException
                | NullPointerException e) {

            advertisingId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            e.printStackTrace();
        }

        return advertisingId;
    }
}
