package com.payment.aeps.app;

import android.annotation.SuppressLint;

import com.payment.aeps.fingpay_microatm.InvocieModel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleConstants {
    public static final String baseUrl = "http://login.payandserve.com/api/android/";
    public  static final String bankList = baseUrl + "faeps/getdata";
    public  static final String AEPS_API = baseUrl + "faeps/transaction";
    public  static final String CASH_DEPO_API = baseUrl + "faeps/cdo/transaction";
   // public static String EKYC_URL = baseUrl + "faeps/transaction";
    public static String EKYC_URL = baseUrl + "iaeps/transaction";
    public static String FMATM_UPDATE = baseUrl + "secure/microatm/update";
    public static String FMATM_INITIATE = baseUrl + "secure/microatm/initiate";
    public static String SPRINT_ONBOARDING = baseUrl + "paysprint/onboard";
    public static Map<String, String> LOCAL_Map;
    public static HashMap<String, String> ReceiptMap;
    public static List<InvocieModel> InvoiceModelList;
    public static String FING_TXN_ID = "";

    public static final String PAYSPRINT_AEPS = "PAYSPRINT";
    public static final String FING_AEPS = "FING";
    public static String AEPS_TYPE = PAYSPRINT_AEPS;

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat SHOWING_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat COMMON_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat YEAR_DATE_FORMAT = new SimpleDateFormat("yyyy");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat MONTH_DATE_FORMAT = new SimpleDateFormat("MMMM");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat REPORT_DATE_FORMAT = new SimpleDateFormat("ddMMyyyy_HHmm");
}
