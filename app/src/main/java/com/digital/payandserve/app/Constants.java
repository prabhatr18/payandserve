package com.digital.payandserve.app;

import android.annotation.SuppressLint;

import com.digital.payandserve.BuildConfig;
import com.digital.payandserve.views.billpayment.model.BillPayModel;
import com.digital.payandserve.views.commission.gsonModel.CommissionModel;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.invoice.model.ReportPDFModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static ArrayList<CommissionModel> commissionDataList;

    public static String[] images = {
            "https://bankingblog.accenture.com/wp-content/uploads/2016/08/Accenture-Banking-PSD2.jpg",
            "https://ak.picdn.net/shutterstock/videos/12236027/thumb/1.jpg",
            "https://datasearchconsulting.com/wp-content/uploads/2019/11/5bffb21c221adb7a3231f2cc_Digital4.jpg",
            "https://www.businessliveme.com/wp-content/uploads/2020/06/unnamed-4.jpg"
    };

    public static String[] imagesBottom = {
            "https://png.pngtree.com/thumb_back/fw800/back_our/20190625/ourmid/pngtree-blue-mobile-phone-scan-code-payment-technology-banner-background-image_260854.jpg",
            "https://png.pngtree.com/thumb_back/fh260/back_our/20190625/ourmid/pngtree-vector-business-mobile-payment-poster-image_261492.jpg",
            "https://png.pngtree.com/thumb_back/fh260/back_our/20190621/ourmid/pngtree-mobile-phone-fast-payment-cartoon-geometry-red-envelope-orange-banner-image_177859.jpg",
            "https://png.pngtree.com/thumb_back/fw800/back_our/20190621/ourmid/pngtree-investment-financial-management-financial-background-image_194572.jpg",
            "https://png.pngtree.com/thumb_back/fw800/back_our/20190617/ourmid/pngtree-corporate-atmosphere-gray-financial-background-poster-image_126027.jpg"
    };

    public static String UPI_IMAGE = "https://images.financialexpress.com/2020/07/UPI-1.jpg";

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
    public static int GLOBAL_POSITION = 0;

    public static List<InvoiceModel> INVOICE_DATA;
    public static ReportPDFModel REPORT_PDF_MODEL;
    public static boolean IS_RELOAD_REQUEST = false;
    public static BillPayModel BILL_MODEL;
    public static String DMT_RID = "";

    public static class URL {
        public static final String BASE_URL = BuildConfig.BASE_URL;
        public static final String COMMON_PATH = BASE_URL + "api/android/";

        //....................................................
        public static final String PDMT_TRANSACTION = BASE_URL + "api/android/pdmt/transaction";
        public static final String PGDMT_DELETE = BASE_URL + "api/android/payoutdmt/bene/delete";
        public static final String GET_OPERATOR = BASE_URL + "api/android/recharge/getoperator";

        //.......................................................................
        public static final String SLIDER = BASE_URL + "api/android/slider";
        public static final String LOGIN = BASE_URL + "api/android/auth";
        public static final String REPORT = BASE_URL + "api/android/transaction";
        public static final String PROVIDER = BASE_URL + "api/android/recharge/providers";
        public static final String GET_PLAN = BASE_URL + "api/android/recharge/getplan";
        public static final String MOBILE_RECHARGE_PAY = BASE_URL + "api/android/recharge/pay";
        public static final String BBPS_BILL_PAY = BASE_URL + "api/android/billpay/transaction";
        public static final String BBPS_BILL_PAY_PARAM = BASE_URL + "api/android/billpay/getprovider";
        public static final String BALANCE_UPDATE = BASE_URL + "api/android/getbalance";
        public static final String FUND_REQUEST = BASE_URL + "api/android/fundrequest";
        public static final String DMT_TRANSACTION = BASE_URL + "api/android/pdmt/transaction";
        public static final String AEPS_INITIATE = BASE_URL + "api/android/aeps/initiate";
        public static final String MATM_INITIATE = BASE_URL + "api/android/secure/microatm/initiate";
        public static final String BUY_PRODUCT = BASE_URL + "api/android/device/purchase";
        public static final String LOGOUT_USER = BASE_URL + "api/android/auth/logout";
        public static final String MATM_DATA_UPDATE = BASE_URL + "api/android/secure/microatm/update";
        public static final String REPORT_CHECK_STATUS = BASE_URL + "api/android/transaction/status";
        public static final String RECHARGE_CHECK_STATUS = BASE_URL + "api/android/recharge/status";
        public static final String BILLPAY_CHECK_STATUS = BASE_URL + "api/android/billpay/status";
        public static final String PASSWORD_RESET_REQ = BASE_URL + "api/android/auth/reset/request";
        public static final String PASSWORD_RESET = BASE_URL + "api/android/auth/reset";
        public static final String AEPS_REGISTER = BASE_URL + "api/android/aeps/register";
        public static final String UPI_CASH = BASE_URL + "api/android/upicash/transaction";
        public static final String UPI_CASH_STATUS = BASE_URL + "api/android/upicash/status";
        public static final String CREATE_MEMBER = BASE_URL + "api/android/member/create";
        public static final String ALL_MEMBERS = BASE_URL + "api/android/member/list";
        public static final String COMPLAIN = BASE_URL+"api/android/complaint/store" +"";
        public static final String PIN_GET_OTP = BASE_URL + "api/android/tpin/getotp";
        public static final String GENERATE_PIN = BASE_URL + "api/android/tpin/generate";
        public static final String GET_COMMISSION = BASE_URL + "api/android/getcommission";
        public static final String PASSWORD_CHANGE = BASE_URL + "api/android/auth/password/change";
        public static final String STATE_LIST = BASE_URL +"api/android/GetState";
        public static final String AEPS_REGISTRATION = BASE_URL+ "api/android/aepsregistration";
        public static final String DISTRICT_LIST = BASE_URL +"api/android/GetDistrictByState";
        public static final String VIDEO_LINK = BASE_URL +"api/android/videolink";
        public static final String CONTACT_SUPPORT = BASE_URL + "api/android/support/store";
        public static final String COMPLAIN_SUBJECT = BASE_URL + "api/android/complaint/subject";
        public static final String PRODUCT_LIST = BASE_URL + "api/android/device/list";
        public static final String PAYLATER_ONBOARD = BASE_URL + "api/android/paylater/useronboard";
        public static final String PAYLATER_PAYMENT = BASE_URL + "api/android/paylater/payment";

        //............................................
        public static final String AEPS_ONBOARD = BASE_URL + "api/android/paysprint/onboard";


    }

    public static int BILL_PAYMENT_VERSION = 1;    // 1 => NEW && 2 => OLD
    public static int AUTO_LOGOUT_TIME = 20;       // minutes
    public static boolean isDemoNewsSec = true;
    public static String MOBILE_ID;

    public static class BBPS {

        public static final int NEW = 1;
        public static final int OLD = 2;
    }
}
