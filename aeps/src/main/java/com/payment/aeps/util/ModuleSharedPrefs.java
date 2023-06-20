package com.payment.aeps.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class ModuleSharedPrefs {

    private static SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "MODULE_PREFS";

    public static String Mobile = "set_lang";
    public static String USER_ID = "user_id";
    public static String LOGIN_ID = "login_id";
    public static String APP_TOKEN = "token";
    public static String USER_NAME = "user_name";
    public static String USER_CONTACT = "user_contact";
    public static String USER_EMAIL = "user_email";
    public static String SELECTED_DEVICE = "selected_device";
    public static String SELECTED_DEVICE_INDEX = "selected_device_INDEX";
    public static String IS_KYC_VARIFIED = "is_kyc_varified";

    public ModuleSharedPrefs() {
        super();
    }

    public static void setValue(Context context, String PREF_KEY, String PREF_VALUE) {
        SharedPreferences.Editor editor;
        sharedPreferences =  context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = sharedPreferences.edit(); //2
        editor.putString(PREF_KEY, PREF_VALUE); //3
        editor.commit(); //4
    }

    public static void setIntValue(Context context, String PREF_KEY, int PREF_VALUE) {
        SharedPreferences.Editor editor;
        sharedPreferences =  context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = sharedPreferences.edit(); //2
        editor.putInt(PREF_KEY, PREF_VALUE); //3
        editor.commit(); //4
    }

    public static String getValue(Context context, String PREF_KEY) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_KEY, null);
    }


    public static void setValue(Context context, String PREF_KEY, Set<String> PREF_VALUE) {
        SharedPreferences.Editor editor;
        sharedPreferences =  context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = sharedPreferences.edit(); //2
        Set<String> set = new HashSet<>();
        set.addAll(PREF_VALUE);
        editor.putStringSet(PREF_KEY, set); //3
        editor.commit(); //4
    }

    public static Set<String> getValue(Context context, String PREF_KEY, String temp){
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(PREF_KEY, null);
    }

    public static void setValue(Context context, String PREF_KEY, boolean PREF_VALUE) {
        SharedPreferences.Editor editor;
        sharedPreferences =  context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = sharedPreferences.edit(); //2
        editor.putBoolean(PREF_KEY, PREF_VALUE); //3
        editor.commit(); //4
    }

    public static boolean getValue(Context context, String PREF_KEY, boolean x) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREF_KEY, false);
    }

    public static int getIntValue(Context context, String PREF_KEY) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PREF_KEY, 0);
    }

    public static void clearAllPreferences(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }


}
