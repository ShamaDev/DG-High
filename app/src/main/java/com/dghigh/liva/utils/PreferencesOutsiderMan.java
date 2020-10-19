// поменять package и import на актуальные
package com.dghigh.liva.utils;


import com.dghigh.liva.App;

public class PreferencesOutsiderMan {

  private static String DEEPLINK_KEY = "deeplinkKey";

  public static boolean getisHashReady() {
    return App.hashSettings.getBoolean("isHashReady",false);
  }

  public static void setisHashReady(boolean value) {
    App.hashSettings.edit().putBoolean("isHashReady", value).apply();
  }

  public static void setParam(String param, String value) {
    App.hashSettings.edit().putString(param, value).apply();
  }

  public static String getParam(String param) {
    return App.hashSettings.getString(param, "");
  }

  public static String getPublic() {
    return App.hashSettings.getString("public","");
  }

  public static void setPublic(String value) {
    App.hashSettings.edit().putString("public", value).apply();
  }

  public static String getPrivate() {
    return App.hashSettings.getString("private","");
  }

  public static void setPrivate(String value) {
    App.hashSettings.edit().putString("private", value).apply();
  }

  public static void setDeeplinkKey(String deeplinkKey) {
    App.hashSettings.edit().putString(DEEPLINK_KEY, deeplinkKey).apply();
  }

  public static String getDeeplinkKey() {
    return App.hashSettings.getString(DEEPLINK_KEY, "");
  }
}

