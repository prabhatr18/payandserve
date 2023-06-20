package com.digital.payandserve.dataModel;

import android.content.Context;

import com.digital.payandserve.R;
import com.digital.payandserve.model.ActivityListModel;
import com.digital.payandserve.online_service.model.OnlienServiceModel;
import com.digital.payandserve.utill.SharedPrefs;

import java.util.ArrayList;

public class MainLocalData {

    private static final String contactInfo = "For this service activation contact our Sales Team +91 9504-06-205";

    public static ArrayList<ActivityListModel> getHomeGridRecord() {
        ArrayList<ActivityListModel> activityListModelArrayList = new ArrayList<>();
        activityListModelArrayList.add(new ActivityListModel("Mobile", R.drawable.ic_phone));
        activityListModelArrayList.add(new ActivityListModel("DTH", R.drawable.ic_dth));
        activityListModelArrayList.add(new ActivityListModel("Electricity", R.drawable.ic_electric));
     /*   activityListModelArrayList.add(new ActivityListModel("Gas Bill", R.drawable.ic_gas));
        activityListModelArrayList.add(new ActivityListModel("Landline", R.drawable.ic_landline));
        activityListModelArrayList.add(new ActivityListModel("Broadband", R.drawable.ic_broadband));
        activityListModelArrayList.add(new ActivityListModel("Water", R.drawable.ic_water));*/
        activityListModelArrayList.add(new ActivityListModel("View More", R.drawable.more_icon));
        return activityListModelArrayList;
    }

    public static ArrayList<ActivityListModel> getMoneyTransferGrid() {
        ArrayList<ActivityListModel> activityListModelArrayList = new ArrayList<>();
        activityListModelArrayList.add(new ActivityListModel("AEPS", R.drawable.ic_fingerprint_scan_new));
        activityListModelArrayList.add(new ActivityListModel("MATM", R.drawable.ic_printing_icon));
        activityListModelArrayList.add(new ActivityListModel("DMT", R.drawable.dmt_new));
        activityListModelArrayList.add(new ActivityListModel("PHONE", R.drawable.ic_phone_new));
//        activityListModelArrayList.add(new ActivityListModel("DTH", R.drawable.ic_satellite_dish));
        activityListModelArrayList.add(new ActivityListModel("BUY", R.drawable.shoppingcart_buy));
        return activityListModelArrayList;
    }

    public static ArrayList<ActivityListModel> getHomeGridAllRecord(Context context) {
        ArrayList<ActivityListModel> list = new ArrayList<>();
        list.add(new ActivityListModel(R.drawable.ic_phone, context.getString(R.string.mobile_recharge), "0"));
        list.add(new ActivityListModel(R.drawable.ic_dth, context.getString(R.string.dth), "1"));
        list.add(new ActivityListModel(R.drawable.ic_electric, context.getString(R.string.electricity_bill), "2"));
        list.add(new ActivityListModel(R.drawable.ic_gas, context.getString(R.string.lpg_gas), "3"));
        list.add(new ActivityListModel(R.drawable.ic_landline, context.getString(R.string.landline_bills), "4")); // => Holidays
        list.add(new ActivityListModel(R.drawable.ic_broadband, context.getString(R.string.broadband_bills), "5")); // => Cash back
        list.add(new ActivityListModel(R.drawable.ic_water, context.getString(R.string.water_bills), "6"));
        list.add(new ActivityListModel(R.drawable.ic_loan_repayment, context.getString(R.string.loan_repayment), "7"));
        list.add(new ActivityListModel(R.drawable.ic_life_insurance, context.getString(R.string.life_insurance), "8"));
        list.add(new ActivityListModel(R.drawable.ic_fasttag, context.getString(R.string.fasttag), "9"));
        list.add(new ActivityListModel(R.drawable.ic_loan, context.getString(R.string.loan), "10"));
        list.add(new ActivityListModel(R.drawable.ic_cable_tv, context.getString(R.string.cable_tv), "11"));
        list.add(new ActivityListModel(R.drawable.ic_health_insurance, context.getString(R.string.health_insurance), "12"));
        list.add(new ActivityListModel(R.drawable.ic_education_fee, context.getString(R.string.education_fees), "13"));
        list.add(new ActivityListModel(R.drawable.ic_tax, context.getString(R.string.minciple_taxes), "14"));
        list.add(new ActivityListModel(R.drawable.ic_landline, context.getString(R.string.postpaid), "15"));
        list.add(new ActivityListModel(R.drawable.ic_housing_society, context.getString(R.string.housing_society), "16"));
        return list;
    }

    public static ArrayList<ActivityListModel> getReportGridRecord() {
        ArrayList<ActivityListModel> activityReportArrayList = new ArrayList<>();
        activityReportArrayList.add(new ActivityListModel("AEPS", R.drawable.aeps_report_icon));
        activityReportArrayList.add(new ActivityListModel("Recharge", R.drawable.recharge_statement_icon));
        activityReportArrayList.add(new ActivityListModel("Bill Pay", R.drawable.bill_report_icon));
        activityReportArrayList.add(new ActivityListModel("DMT", R.drawable.dmt_report_icon));
        activityReportArrayList.add(new ActivityListModel("All Reports", R.drawable.ic_folder));
        //activityReportArrayList.add(new ActivityListModel("Wallet", R.drawable.wallet_report_icon));
        return activityReportArrayList;
    }

    public static ArrayList<ActivityListModel> getReportAllGridRecord(String mBal) {
        ArrayList<ActivityListModel> activityReportArrayList = new ArrayList<>();
        activityReportArrayList.add(new ActivityListModel("Kiosk Banking Statement", R.drawable.aeps_report_icon));
        activityReportArrayList.add(new ActivityListModel("Billpay Statement", R.drawable.aeps_report_icon));
        activityReportArrayList.add(new ActivityListModel("Money Transfer Statement", R.drawable.aeps_report_icon));
        activityReportArrayList.add(new ActivityListModel("Recharge Statement", R.drawable.ic_wallet_report));
        final boolean no = mBal != null && !mBal.equalsIgnoreCase("NO") && mBal.length() > 0;
        if (no)
            activityReportArrayList.add(new ActivityListModel("Card Statement", R.drawable.recharge_statement_icon));
        activityReportArrayList.add(new ActivityListModel("Wallet Balance", R.drawable.bill_report_icon));
        activityReportArrayList.add(new ActivityListModel("Kiosk Balance", R.drawable.dmt_report_icon));
        activityReportArrayList.add(new ActivityListModel("Kuber Balance", R.drawable.dmt_report_icon));
        if (no)
            activityReportArrayList.add(new ActivityListModel("Card Balance", R.drawable.dmt_report_icon));
        activityReportArrayList.add(new ActivityListModel("Wallet Balance settlement", R.drawable.ic_matm_tran_report));
        activityReportArrayList.add(new ActivityListModel("Kiosk Balance settlement", R.drawable.ic_matm_tran_report));
        activityReportArrayList.add(new ActivityListModel("Kuber Balance Settlement", R.drawable.ic_matm_tran_report));
        if (no)
            activityReportArrayList.add(new ActivityListModel("Card Balance settlement", R.drawable.ic_matm_tran_report));
        activityReportArrayList.add(new ActivityListModel("Self Shopping Report", R.drawable.shoppingcart_buy));
        activityReportArrayList.add(new ActivityListModel("Ticket Status", R.drawable.icon_complaint_history));
        activityReportArrayList.add(new ActivityListModel("Id Stock Report", R.drawable.dmt_report_icon));
//        activityReportArrayList.add(new ActivityListModel("Cashback Statement", R.drawable.dmt_report_icon));
        activityReportArrayList.add(new ActivityListModel("Aadhaar Pay Statement", R.drawable.dmt_report_icon));
        return activityReportArrayList;
    }

    public static ArrayList<ActivityListModel> getProfilePageOptions(Context con) {
        ArrayList<ActivityListModel> activityReportArrayList = new ArrayList<>();
        activityReportArrayList.add(new ActivityListModel("Profile", R.drawable.ic_user));
        activityReportArrayList.add(new ActivityListModel("Bank Account", R.drawable.ic_settings_user));
        String retailer = SharedPrefs.getValue(con, SharedPrefs.ROLE_SLUG);
        if (!retailer.equalsIgnoreCase("retailer"))
            activityReportArrayList.add(new ActivityListModel("Manage Members", R.drawable.add_member_group));
        return activityReportArrayList;
    }


    public static ArrayList<ActivityListModel> getBankingService() {
        ArrayList<ActivityListModel> activityListModelArrayList = new ArrayList<>();
        activityListModelArrayList.add(new ActivityListModel("ICICI Kiosk Services", R.drawable.icic));
        activityListModelArrayList.add(new ActivityListModel("Kiosk Services 2.O", R.drawable.aeps_logo));
        activityListModelArrayList.add(new ActivityListModel("Money Transfer", R.drawable.money_transfer_ic));
        activityListModelArrayList.add(new ActivityListModel("DMT 2.O", R.drawable.money_transfer_ic));
        activityListModelArrayList.add(new ActivityListModel("Mini-ATM", R.drawable.mini_atm));
        activityListModelArrayList.add(new ActivityListModel("Insta Account Opening", R.drawable.insta_account));
        return activityListModelArrayList;
    }

    public static ArrayList<OnlienServiceModel> getLendingService(Context context) {
        ArrayList<OnlienServiceModel> lendingList = new ArrayList<>();
        lendingList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.personal_loan_ic),
                "Personal Loan", "https://bit.ly/Personalloanpsispl", "url", true));
        lendingList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.bussiness_loan_ic),
                "Business Loan", "https://bit.ly/Personalloanpsispl", "url", true));
        lendingList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.home_loan_ic),
                "Home Loan", "https://bit.ly/Personalloanpsispl", "url", true));
        lendingList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.credit_card_ic),
                "Apply For Credit Card", "https://bit.ly/3yYbWHtcreditcardICICI", "url", true));

        return lendingList;
    }

    public static ArrayList<OnlienServiceModel> getGoverenceService(Context context) {
        ArrayList<OnlienServiceModel> governanceList = new ArrayList<>();
        governanceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.pan_ic),
                "Pan Card", "https://www.psaonline.utiitsl.com/psaonline/showLogin", "url", true));
        governanceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.ayushman),
                "PM-JAY", "https://www.pmjay.utiitsl.com/pmjayecard/", "url", true));
        governanceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.fast_tag_ic),
                "Fast Tag", "contactInfo", "Fast Tag", true));
        governanceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.itr_filling),
                "ITR Filing", contactInfo, "msg", false));
        governanceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.gst_ic),
                "GST Services", contactInfo, "msg", false));

        return governanceList;
    }

    public static ArrayList<OnlienServiceModel> getToursTravelService(Context context) {
        ArrayList<OnlienServiceModel> tourList = new ArrayList<>();

        tourList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.flight),
                "Flight Booking", contactInfo, "msg", false));
        tourList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.bus_booking_ic),
                "Bus Booking", contactInfo, "msg", false));
        tourList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.hotel_booking_ic),
                "Hotel booking", contactInfo, "msg", false));
        tourList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.irctc_ic),
                "IRCTC", contactInfo, "msg", false));

        return tourList;
    }


    public static ArrayList<OnlienServiceModel> getInsuranceService(Context context) {
        ArrayList<OnlienServiceModel> insuranceList = new ArrayList<>();

        insuranceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.buy_insurance_ic),
                "Buy Insurance", contactInfo, "msg", false));
        insuranceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.mutul_fund_ic),
                "Mutul Fund Invest", contactInfo, "msg", false));
        insuranceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.fixed_deposit_ic),
                "Fixed Deposit", contactInfo, "msg", false));
        insuranceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.gold_investment),
                "Gold Investment", contactInfo, "msg", false));

        return insuranceList;
    }


    public static ArrayList<OnlienServiceModel> getEcommerceService(Context context) {
        ArrayList<OnlienServiceModel> ecommerceList = new ArrayList<>();

        ecommerceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.amazon),
                "Amazon Store", "https://amzn.to/3rssDYS/", "url", true));
        ecommerceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.online_shopping_ic),
                "Online Shopping", "https://bit.ly/PayandServeOnlineOrderPortal", "url", true));

        ecommerceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.giftcard_ic),
                "Gift Card", contactInfo, "msg", false));
        ecommerceList.add(new OnlienServiceModel(context.getResources().getDrawable(R.drawable.prepaid_card_ic),
                "Prepaid Card", contactInfo, "msg", false));

        return ecommerceList;
    }
}
