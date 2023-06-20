package com.digital.payandserve.utill;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DarkThemeApplication extends Application {

    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String themePref = sharedPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE);
        ThemeHelper.applyTheme(themePref);
    }
}