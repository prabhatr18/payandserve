package com.payment.aeps.fingpay_microatm.FingSdkUtill;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import com.fingpay.microatmsdk.data.MiniStatementModel;

import java.util.List;

public class FingUtil {

    public static List<MiniStatementModel> StatementList ;

    public static String getTypeName(int type) {
        String typeName = "";
        switch (type) {
            case 2:
                typeName = "CASH WITHDRAWAL";
                break;
            case 3:
                typeName = "CASH DEPOSIT";
                break;
            case 4:
                typeName = "BALANCE ENQUIRY";
                break;
            case 7:
                typeName = "MINI STATEMENT";
                break;
            case 10:
                typeName = "PIN RESET";
                break;
            case 8:
                typeName = "CHANGE PIN";
                break;
            case 9:
                typeName = "CARD ACTIVATION";
                break;
            default:
                typeName = String.valueOf(type);
        }
        return typeName;
    }

    public static String getTypeName(String type) {
        String typeName = "";
        switch (type) {
            case "CW":
                typeName = "CASH WITHDRAWAL";
                break;
            case "CD":
                typeName = "CASH DEPOSIT";
                break;
            case "BE":
                typeName = "BALANCE ENQUIRY";
                break;
            case "MS":
                typeName = "MINI STATEMENT";
                break;
            case "PR":
                typeName = "PIN RESET";
                break;
            case "CP":
                typeName = "CHANGE PIN";
                break;
            case "CA":
                typeName = "CARD ACTIVATION";
                break;
            default:
                typeName = String.valueOf(type);
        }
        return typeName;
    }

    public static String getImei(Context context) {
        String deviceId = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < 29) {
                deviceId = telephonyManager.getImei(0);
            } else if (Build.VERSION.SDK_INT >= 29) {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                deviceId = telephonyManager.getDeviceId();
            }
        } catch (Exception e) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }
}
