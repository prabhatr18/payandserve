package com.digital.payandserve.utill;

import android.content.Context;
import android.content.SharedPreferences;

import com.digital.payandserve.security.EncryptionUtils;

import static com.digital.payandserve.utill.SharedPrefs.PREFS_NAME;

public class PrefLoginManager {
    // Shared preferences file name
    private static final String PREF_NAME = "login_pref";
    private static final String USER_LOGIN_RES = "user_login_res";
    public static  String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;


    public PrefLoginManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFarmerLoginRes(String a) {
        editor.putString(USER_LOGIN_RES, a);
        editor.commit();
    }

    public String getFarmerLoginRes() {
        return pref.getString(USER_LOGIN_RES, "");
    }

    public void clearFarmerLoginRes() {
        editor.putString(USER_LOGIN_RES, "");
        editor.commit();
    }
}
