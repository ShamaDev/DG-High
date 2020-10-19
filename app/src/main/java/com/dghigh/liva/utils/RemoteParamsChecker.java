package com.dghigh.liva.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.google.android.gms.instantapps.InstantApps;

public class RemoteParamsChecker {

    public boolean isAllowed (boolean allowedGeo, boolean allowedGeoInstant, boolean modelCheck, boolean simCheck, boolean dpCheck, boolean dpInstantCheck, Context context) {

        boolean geoResult = true;
        boolean geoInstantResult = true;
        boolean modelResult = true;
        boolean simResult = true;
        boolean dpResult = true;
        boolean dpInstantResult = true;
        boolean overallResult = false;

        if (isInstantApp(context)) {
            if (!allowedGeoInstant)
                geoInstantResult = false;
        } else {
            if (!allowedGeo)
                geoResult = false;
        }

        if (modelCheck)
            if (Build.MODEL.contains("Pixel")) modelResult = false;

        if (simCheck)
            if (!isSimActive(context)) simResult = false;

        if (isInstantApp(context)) {
            if (dpInstantCheck)
                if (!isInstantNonOrganic()) dpInstantResult = false;
        } else {
            if (dpCheck)
                if (!isNonOrganic()) dpResult = false;
        }

        // Log.e("RemoteParamsChecker",
        //         "geoResult: " + geoResult
        //         + ", geoInstantResult: " + geoInstantResult
        //         + ", simResult: " + simResult
        //         + ", dpResult: " + dpResult
        //         + ", dpInstantResult: " + dpInstantResult
        //         + ", modelResult: " + modelResult);

        if (geoResult
                && geoInstantResult
                && simResult
                && dpResult
                && dpInstantResult
                && modelResult)

            overallResult = true;

        return overallResult;
    }

    private boolean isSimActive(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (tm.getSimState()) {

            case TelephonyManager.SIM_STATE_READY:
            case TelephonyManager.SIM_STATE_NOT_READY:
                return true;

            default:
                return false;
        }
    }

    private boolean isNonOrganic() {
        return !PreferencesOutsiderMan.getParam(Params.WEB_ID).equals("")
            || PreferencesOutsiderMan.getParam(Params.INSTALL_REFERRER).contains("pcampaignid")
            || PreferencesOutsiderMan.getParam(Params.INSTALL_REFERRER).contains("gclid");
    }

    private boolean isInstantNonOrganic() {
        return !PreferencesOutsiderMan.getParam(Params.WEB_ID).equals("");
    }

    private boolean isInstantApp(Context context) {
       return InstantApps.getPackageManagerCompat(context).isInstantApp();
    }
}
