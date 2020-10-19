package com.dghigh.liva.utils;

import android.content.Context;
import android.os.Build;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.TimeZone;

public class DataGenerator {

    public String generate(Context context) {

        String resultString = "";
        JSONObject mainObject = new JSONObject();
        JSONObject deeplinks;
        JSONObject params;
        JSONObject pushTokens;

        try {
            deeplinks = new JSONObject(PreferencesOutsiderMan.getParam(Params.DEEPLINKS));
            params = new JSONObject();
            pushTokens = new JSONObject();

            params.put(Params.APP_ID, context.getPackageName());
            params.put(Params.APPSFLYER_ID, PreferencesOutsiderMan.getParam(Params.APPSFLYER_ID));
            params.put(Params.APPMETRICA_ID, PreferencesOutsiderMan.getParam(Params.APPMETRICA_ID));
            params.put(Params.APPMETRICA_POST_API_KEY, Params.APPMETRICA_POST_API_KEY_VALUE);
            params.put(Params.APPMETRICA_APPLICATION_ID, Params.APPMETRICA_APPLICATION_ID_VALUE);
            pushTokens.put(Params.FIREBASE_TOKEN, PreferencesOutsiderMan.getParam(Params.FIREBASE_TOKEN));
            deeplinks.put(Params.ADVERTISING_DEVICE_ID, PreferencesOutsiderMan.getParam(Params.ADVERTISING_DEVICE_ID));
            deeplinks.put(Params.VRT, String.valueOf(isEmulator()));
            deeplinks.put(Params.TIMEZONE, TimeZone.getDefault().getID());
            deeplinks.put(Params.MODEL, Build.MODEL);
            deeplinks.put(Params.MANUFACTURER, Build.MANUFACTURER);
            deeplinks.put(Params.LANG, Locale.getDefault().getLanguage());
            deeplinks.put(Params.LOCALE, String.valueOf(getCurrentLocale(context)));

            if (!PreferencesOutsiderMan.getParam(Params.WEB_ID).equals("")) {
                deeplinks.put(Params.WEB_ID, PreferencesOutsiderMan.getParam(Params.WEB_ID));
                deeplinks.put(Params.WEB_SUB, PreferencesOutsiderMan.getParam(Params.WEB_SUB));
            }

            mainObject.put(Params.DEEPLINKS, deeplinks);
            mainObject.put(Params.PARAMS, params);
            mainObject.put(Params.PUSH_TOKENS, pushTokens);

            resultString = URLEncoder
                    .encode(Base64
                    .encodeToString(mainObject.toString().getBytes(StandardCharsets.UTF_8), Base64.DEFAULT), StandardCharsets.UTF_8.displayName())
                    .replace("%0A", "");

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return resultString;
    }

    private Locale getCurrentLocale(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return context.getResources().getConfiguration().getLocales().get(0);
        else
            return context.getResources().getConfiguration().locale;

    }

    private boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }
}
