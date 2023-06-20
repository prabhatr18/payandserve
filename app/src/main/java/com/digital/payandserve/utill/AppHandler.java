package com.digital.payandserve.utill;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.digital.payandserve.customer_care.model.SelectSubjectModel;
import com.digital.payandserve.intro_slider.model.SliderModel;
import com.digital.payandserve.views.DashBoardActivity;
import com.digital.payandserve.views.dmt.Benedatum;
import com.digital.payandserve.views.dmt.BenedatumPgPayout;
import com.digital.payandserve.views.ourproducts.BuyProductListModel;
import com.digital.payandserve.views.reports.model.AdharPayStatmentModel;
import com.digital.payandserve.views.reports.model.BuyProductModel;
import com.digital.payandserve.views.reports.model.CashbackModel;
import com.digital.payandserve.views.reports.model.IdStockModel;
import com.digital.payandserve.views.reports.model.ticketModel.TicketModel;
import com.digital.payandserve.views.select_state_district.district_model.DistrictModel;
import com.digital.payandserve.views.select_state_district.state_model.StateModel;
import com.digital.payandserve.views.tutorial.model.TutorialModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.views.browseplan.model.DataItem;
import com.digital.payandserve.views.browseplan.model.GenericModel;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.invoice.wd;
import com.digital.payandserve.views.member.model.AppMember;
import com.digital.payandserve.views.moneytransfer.model.BenModel;
import com.digital.payandserve.views.operator.model.OperatorModel;
import com.digital.payandserve.views.reports.model.AEPSFundReqReportModel;
import com.digital.payandserve.views.reports.model.AEPSReportModel;
import com.digital.payandserve.views.reports.model.AEPSWalletModel;
import com.digital.payandserve.views.reports.model.BillRechargeModel;
import com.digital.payandserve.views.reports.model.DMTModel;
import com.digital.payandserve.views.reports.model.FundRequestModel;
import com.digital.payandserve.views.reports.model.UPIModel;
import com.digital.payandserve.views.reports.model.WalletBankModel;
import com.digital.payandserve.views.walletsection.model.ModeModel;
import com.digital.payandserve.views.walletsection.model.RequestBankModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppHandler {
    public static void initAepsTransactionReportData(AEPSReportModel model, Context context) {
        Constants.INVOICE_DATA = new ArrayList<>();
        Constants.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Txnid", model.getTxnid()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Terminal Id", model.getTerminalid()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Bcid", model.getAadhar()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Charge", MyUtil.formatWithRupee(context, model.getCharge())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Balance", MyUtil.formatWithRupee(context, model.getBalance())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Type", model.getType()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", model.getTranstype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initBuyProductFundRequestReportReportData(BuyProductModel model, Context context) {
        Constants.INVOICE_DATA = new ArrayList<>();
        Constants.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Balance", model.getBalance()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Number", model.getNumber()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Product", model.getProduct()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Price", model.getPrice()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Date", model.getCreatedAt()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initWalletStatement(WalletBankModel model, Context context, double closing, double amount) {
        Constants.INVOICE_DATA = new ArrayList<>();
        Constants.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Txnid", model.getTxnid()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Number", model.getNumber()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Provider ID", model.getProviderId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Opening Balance", MyUtil.formatWithRupee(context, model.getBalance())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Closing Balance", MyUtil.formatWithRupee(context, closing)));
        Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, amount)));
        Constants.INVOICE_DATA.add(new InvoiceModel("ST Type", model.getRtype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Product", model.getProduct()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initAepsFundRequestReportReportData(AEPSFundReqReportModel model, Context context) {
        Constants.INVOICE_DATA = new ArrayList<>();
        Constants.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Account", model.getAccount()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Bank", model.getBank()));
        Constants.INVOICE_DATA.add(new InvoiceModel("IFSC", model.getIfsc()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Type", model.getType()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Request Type", model.getRequesttype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Pay Type", model.getPayType()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Payout Id", model.getPayoutid()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }


    public static void initAadhaarReportData(AdharPayStatmentModel model, Context context) {
        Constants.INVOICE_DATA = new ArrayList<>();
        Constants.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Txn Id", model.getTxnid()));
        Constants.INVOICE_DATA.add(new InvoiceModel("User Name", model.getUsername()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Amount", model.getAmount()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Aadhaar", model.getAadhar()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Bank", model.getBank()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Pay Id", model.getPayid()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Balance", model.getBalance()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Type", model.getType()));
        Constants.INVOICE_DATA.add(new InvoiceModel("R Type", model.getRtype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", model.getTranstype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Provider Name", model.getProvidername()));


    }

    public static void initBillReportData(BillRechargeModel model, Context context) {
        Constants.INVOICE_DATA = new ArrayList<>();
        Constants.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Txn Id", model.getTxnid()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Ref No", model.getRefno()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Number", model.getNumber()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Charge", MyUtil.formatWithRupee(context, model.getCharge())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Balance", MyUtil.formatWithRupee(context, model.getBalance())));
//        Constants.INVOICE_DATA.add(new InvoiceModel("Profit", MyUtil.formatWithRupee(context, model.getProfit())));
        Constants.INVOICE_DATA.add(new InvoiceModel("R Type", model.getRtype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Via", model.getVia()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", model.getTransType()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Product", model.getProduct()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Provider Name", model.getProvidername()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initWalletFundReqReportData(FundRequestModel model, Context context) {
        Constants.INVOICE_DATA = new ArrayList<>();
        Constants.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Type", model.getType().toUpperCase()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Fundbank ID", model.getFundbankId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Pay Mode", model.getPaymode()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Actual Amount", MyUtil.formatWithRupee(context, model.getActualAmount())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Ref Number", model.getRefNo()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Pay Data", model.getPaydate()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initDMTReportData(DMTModel model, Context context) {
        Constants.INVOICE_DATA = new ArrayList<>();
        Constants.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Txn Id", model.getTxnid()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Ref No", model.getRefno()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Number", model.getNumber()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Charge", MyUtil.formatWithRupee(context, model.getCharge())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Balance", MyUtil.formatWithRupee(context, model.getBalance())));
//        Constants.INVOICE_DATA.add(new InvoiceModel("Profit", MyUtil.formatWithRupee(context, model.getProfit())));
        Constants.INVOICE_DATA.add(new InvoiceModel("GST", MyUtil.formatWithRupee(context, model.getGst())));
        Constants.INVOICE_DATA.add(new InvoiceModel("TDS", MyUtil.formatWithRupee(context, model.getTds())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Description", model.getDescription()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Via", model.getVia()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", model.getTransType()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Bank", model.getOption3()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initAEPSWalletStatement(AEPSWalletModel model, Context context) {
        Constants.INVOICE_DATA = new ArrayList<>();
        Constants.INVOICE_DATA.add(new InvoiceModel("Id", "" + model.getId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Txnid", model.getTxnid()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Bank", model.getBank()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Ref NO", model.getRefno()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Opening Balance", MyUtil.formatWithRupee(context, model.getBalance())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Closing Balance", MyUtil.formatWithRupee(context, model.getClosingAmount())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getShowAmount())));
        Constants.INVOICE_DATA.add(new InvoiceModel("ST Type", model.getRtype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Tran Type", model.getTranstype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("AEPS Type", model.getAepstype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Product", model.getProduct()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void initUPIReport(UPIModel model, Context context) {
        Constants.INVOICE_DATA = new ArrayList<>();
        Constants.INVOICE_DATA.add(new InvoiceModel("Id", model.getId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Txn Id", model.getTxnid()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Mobile", model.getMobile()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Api Id", model.getApiId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(context, model.getAmount())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Charge", MyUtil.formatWithRupee(context, model.getCharge())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Balance", MyUtil.formatWithRupee(context, model.getBalance())));
        Constants.INVOICE_DATA.add(new InvoiceModel("Type", model.getType()));
        Constants.INVOICE_DATA.add(new InvoiceModel("R Type", model.getRtype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Transaction Type", model.getTranstype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Aeps Type", model.getAepstype()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Via", model.getVia()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Provider Id", model.getProviderId()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Product", model.getProduct()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Date", model.getCreatedAt()));
        Constants.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
    }

    public static void printInvoiceFromAdapter(View view, Context context) {
        if (Build.VERSION.SDK_INT >= 23 && context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            ((Activity) context).requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else {
            new wd().NUL(view, context);
        }
    }

    public static List<WalletBankModel> parsewalletTransaction(Activity activity, JSONArray jsonArray) {
        List<WalletBankModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<WalletBankModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<SliderModel> parseSliderData(Activity activity, JSONArray jsonArray) {
        List<SliderModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<SliderModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<BuyProductModel> BuyProductParseTransaction(Activity activity, JSONArray jsonArray) {
        List<BuyProductModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<BuyProductModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public static List<AEPSWalletModel> parseAepsWalletTransaction(Activity activity, JSONArray jsonArray) {
        List<AEPSWalletModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<AEPSWalletModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<TutorialModel> parseTutorialVideo(Activity activity, JSONArray jsonArray) {
        List<TutorialModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<TutorialModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<AEPSReportModel> parseAepsTransaction(Activity activity, JSONArray jsonArray) {
        List<AEPSReportModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<AEPSReportModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<AppMember> parseMemberRecord(Activity activity, JSONArray jsonArray) {
        List<AppMember> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<AppMember>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<BillRechargeModel> parseBillRechargeData(Activity activity, JSONArray jsonArray) {
        List<BillRechargeModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<BillRechargeModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<BillRechargeModel> parseRecentBillRechargeData(Activity activity, JSONArray jsonArray) {
        List<BillRechargeModel> data = new ArrayList<>();
        try {
            Gson gson = new GsonBuilder().create();
            int size = 5;
            if (jsonArray.length() < size)
                size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String operator = obj.getString("providername");
                if (isMobileProvider(operator)) {
                    BillRechargeModel model = gson.fromJson(obj.toString(), BillRechargeModel.class);
                    data.add(model);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<StateModel> parseStateList(Activity activity, JSONArray jsonArray){
        List<StateModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<StateModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public static List<SelectSubjectModel> parseSubjectList(Activity activity, JSONArray jsonArray){
        List<SelectSubjectModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<SelectSubjectModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public static List<DistrictModel> parseDistrictList(Activity activity, JSONArray jsonArray){
        List<DistrictModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<DistrictModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
    public static List<BenedatumPgPayout> parseBenListPgOut(Activity activity, JSONArray jsonArray) {
        List<BenedatumPgPayout> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<BenedatumPgPayout>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<Benedatum> parseBenListP(Activity activity, JSONArray jsonArray) {
        List<Benedatum> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<Benedatum>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private static boolean isMobileProvider(String str) {
        switch (str) {
            case "VODAFONE":
            case "BSNL":
            case "BSNL SPECIAL":
            case "JIORECH":
            case "Idea":
            case "Airtel":
                return true;
            default:
                return false;
        }
    }

    public static List<AEPSFundReqReportModel> parseAepsFundReqTransaction(Activity activity, JSONArray jsonArray) {
        List<AEPSFundReqReportModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<AEPSFundReqReportModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<IdStockModel> parseIdStock(JSONArray jsonArray) {
        List<IdStockModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<IdStockModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<AdharPayStatmentModel> parseAadharPayList(JSONArray jsonArray) {
        List<AdharPayStatmentModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<AdharPayStatmentModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<CashbackModel> parseCashbackList(JSONArray jsonArray) {
        List<CashbackModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<CashbackModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<TicketModel> parseTicket(Activity activity, JSONArray jsonArray) {
        List<TicketModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<TicketModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<UPIModel> parseUpiModelReport(Activity activity, JSONArray jsonArray) {
        List<UPIModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<UPIModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<FundRequestModel> parseWalletFundReqTransaction(Activity activity, JSONArray jsonArray) {
        List<FundRequestModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<FundRequestModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<OperatorModel> parse(String parseType, JSONArray jsonArray) {
        List<OperatorModel> data = new ArrayList<>();
        try {
            if (parseType.equalsIgnoreCase("getbank")) {
          //  if (parseType.equalsIgnoreCase("data")) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String bankid = jsonObject.getString("bankid");
                  //  String bankcode = jsonObject.getString("bankcode");
                    String bankcode = "";
                    String bankname = jsonObject.getString("name");
                    String masterifsc = jsonObject.getString("ifsc");
                    String url = jsonObject.getString("url");
                    OperatorModel m = new OperatorModel(bankname, url, id, bankid, bankcode, masterifsc);
                    data.add(m);
                }
            } else {
                Type type = new TypeToken<List<OperatorModel>>() {
                }.getType();
                Gson gson = new GsonBuilder().create();
                data.addAll(gson.fromJson(jsonArray.toString(), type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<BenModel> parseBenList(Activity activity, JSONArray jsonArray) {
        List<BenModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<BenModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<BuyProductListModel> parseProductList(JSONArray jsonArray) {
        List<BuyProductListModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<BuyProductListModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<RequestBankModel> parseBankListRes(Activity activity, JSONArray jsonArray) {
        List<RequestBankModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<RequestBankModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<ModeModel> parseModeListRes(Activity activity, JSONArray jsonArray) {
        List<ModeModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<ModeModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<DMTModel> parseDMTReport(Activity activity, JSONArray jsonArray) {
        List<DMTModel> data = new ArrayList<>();
        try {
            Type type = new TypeToken<List<DMTModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            data.addAll(gson.fromJson(jsonArray.toString(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static GenericModel parse(JSONObject jsonObjectMain) {

        List<DataItem> planDataList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        String s = jsonObjectMain.toString();
        try {
            if (s.contains("Plan") && s.contains("MONTHS") && s.contains("rs")) {
                JSONObject jsonObject = jsonObjectMain.getJSONObject("data");
                JSONArray jsonArray = jsonObject.getJSONArray("Plan");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    JSONObject rsObj = obj.getJSONObject("rs");
                    Iterator<String> rsKeys = rsObj.keys();
                    while (rsKeys.hasNext()) {
                        String key = rsKeys.next();
                        String priceValues = rsObj.getString(key);
                        if (!titleList.contains(key))
                            titleList.add(key);
                        planDataList.add(new DataItem(priceValues, obj.getString("desc"), obj.getString("plan_name"), key, obj.getString("last_update")));
                    }
                }
            } else if (s.contains("recharge_short_description") || s.contains("recharge_description")) {
                JSONArray array = jsonObjectMain.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String recharge_value = obj.getString("recharge_value");
                    String recharge_validity = obj.getString("recharge_validity");
                    String recharge_short_description = obj.getString("recharge_short_description");
                    String recharge_description = obj.getString("recharge_description");
                    String last_updated_dt = obj.getString("last_updated_dt");
                    if (!titleList.contains(recharge_short_description))
                        titleList.add(recharge_short_description);
                    planDataList.add(new DataItem(recharge_value, recharge_description,
                            recharge_validity, recharge_short_description, last_updated_dt));
                }
            } else {
                JSONObject jsonObject = jsonObjectMain.getJSONObject("data");
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    titleList.add(key);
                    JSONArray array = jsonObject.getJSONArray(key);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        planDataList.add(new DataItem(obj.getString("rs"), obj.getString("desc"),
                                obj.getString("validity"), key, obj.getString("last_update")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GenericModel(planDataList, titleList);
    }

    public static boolean editTextRequiredValidation(EditText[] data) {
        boolean flag = true;
        for (EditText et : data) {
            if (et.getText().toString() == null || et.getText().toString().length() == 0) {
                et.setError("This field is required");
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean checkStatus(String json, Activity context) {
        boolean flag = false;
        try {
            JSONObject obj = new JSONObject(json);
            String status = "";
            if (obj.has("status")) {
                status = obj.getString("status");
            } else if (obj.has("statuscode")) {
                status = obj.getString("statuscode");
            }
            if (status.equalsIgnoreCase("success") || status.equalsIgnoreCase("TXN") ||
                    status.equalsIgnoreCase("RNF") || status.equalsIgnoreCase("OTP") ||
                    status.equalsIgnoreCase("TXNOTP")) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String getStatus(String json) {
        String status = "";
        try {
            JSONObject obj = new JSONObject(json);
            if (obj.has("status")) {
                status = obj.getString("status");
            } else if (obj.has("statuscode")) {
                status = obj.getString("statuscode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public static String getMessage(String json) {
        String status = "";
        try {
            JSONObject obj = new JSONObject(json);
            if (obj.has("message")) {
                status = obj.getString("message");
            } else if (obj.has("msg")) {
                status = obj.getString("msg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public static String parseExceptionMsg(Exception e) {
        String m = "Some thing went wrong please try again after some time";
        try {
            if (e.getMessage() != null) {
                m += "\nError : " + e.getMessage();
            }
        } catch (Exception d) {
            d.printStackTrace();
        }
        return m;
    }

    public static void parseAuthRes(String inputMobile, String res, Activity context) {
        try {
            JSONObject jsonObjectMain = new JSONObject(res);
            JSONObject userObject = new JSONObject(jsonObjectMain.getString("userdata"));
            SharedPrefs.setValue(context, SharedPrefs.USER_ID, userObject.getString("id"));
            SharedPrefs.setValue(context, SharedPrefs.USER_NAME, userObject.getString("name"));
            SharedPrefs.setValue(context, SharedPrefs.USER_EMAIL, userObject.getString("email"));
            SharedPrefs.setValue(context, SharedPrefs.USER_CONTACT, userObject.getString("mobile"));

            if (userObject.has("forceUpdate"))
                SharedPrefs.setValue(context, SharedPrefs.IS_FORCE_UPDATE_ENABLE, userObject.getString("forceUpdate"));
            else
                SharedPrefs.setValue(context, SharedPrefs.IS_FORCE_UPDATE_ENABLE, "");

            if (userObject.has("versioncode"))
                SharedPrefs.setValue(context, SharedPrefs.APP_VERSION_CODE, userObject.getString("versioncode"));
            else
                SharedPrefs.setValue(context, SharedPrefs.APP_VERSION_CODE, "");

            if (userObject.has("news"))
                SharedPrefs.setValue(context, SharedPrefs.NEWS, userObject.getString("news"));

            if (userObject.has("otpverify"))
                SharedPrefs.setValue(context, SharedPrefs.OTP_VERIFY, userObject.getString("otpverify"));

            if (userObject.has("mainwallet"))
                SharedPrefs.setValue(context, SharedPrefs.MAIN_WALLET, userObject.getString("mainwallet"));
            else
                SharedPrefs.setValue(context, SharedPrefs.MAIN_WALLET, userObject.getString("balance"));

            if (userObject.has("profile")){
                String url = Constants.URL.BASE_URL+userObject.getString("profile");
                SharedPrefs.setValue(context, SharedPrefs.USER_PROFILE_PIC, url);
            } else {
                SharedPrefs.setValue(context, SharedPrefs.USER_PROFILE_PIC, userObject.getString("profile"));
            }

            if (userObject.has("nsdlwallet")) {
                SharedPrefs.setValue(context, SharedPrefs.NSDL_WALLET, userObject.getString("nsdlwallet"));
            } else {
                SharedPrefs.setValue(context, SharedPrefs.NSDL_WALLET, userObject.getString("nsdlbalance"));
            }

            if (userObject.has("status")) {
                SharedPrefs.setValue(context, SharedPrefs.STATUS, userObject.getString("status"));
            } else {
                SharedPrefs.setValue(context, SharedPrefs.STATE, userObject.getString("status_id"));
            }
            if (userObject.has("microatmbalance")) {
                SharedPrefs.setValue(context, SharedPrefs.MICRO_ATM_BALANCE, userObject.getString("microatmbalance"));
            } else {
                SharedPrefs.setValue(context, SharedPrefs.MICRO_ATM_BALANCE, "NO");
            }

            if (userObject.has("devserialno")) {
                SharedPrefs.setValue(context, SharedPrefs.MATM_SERIAL_NUMBER, userObject.getString("devserialno"));
            } else {
                SharedPrefs.setValue(context, SharedPrefs.MATM_SERIAL_NUMBER, "NA");
            }

            if (userObject.has("tokenamount")) {
                SharedPrefs.setValue(context, SharedPrefs.TOKEN_AMOUNT, userObject.getString("tokenamount"));
            }

            if (userObject.has("utiid")) {
                SharedPrefs.setValue(context, SharedPrefs.UTI_ID, userObject.getString("utiid"));
            }

            if (userObject.has("slides")) {
                try {
                    JSONArray banners = userObject.getJSONArray("slides");
                    String bannerarray = banners.toString();
                    SharedPrefs.setValue(context, SharedPrefs.BANNERARRAY, bannerarray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            SharedPrefs.setValue(context, SharedPrefs.STATE, userObject.getString("state"));
            SharedPrefs.setValue(context, SharedPrefs.APES_BALANCE, userObject.getString("aepsbalance"));
            SharedPrefs.setValue(context, SharedPrefs.LOCKED_AMOUNT, userObject.getString("lockedamount"));
            SharedPrefs.setValue(context, SharedPrefs.ROLE_ID, userObject.getString("role_id"));
            SharedPrefs.setValue(context, SharedPrefs.PARENT_ID, userObject.getString("parent_id"));
            SharedPrefs.setValue(context, SharedPrefs.COMPANY_ID, userObject.getString("scheme_id"));
            SharedPrefs.setValue(context, SharedPrefs.ADDRESS, userObject.getString("address"));
            SharedPrefs.setValue(context, SharedPrefs.SHOP_NAME, userObject.getString("shopname"));
            SharedPrefs.setValue(context, SharedPrefs.GSTIN, userObject.getString("gstin"));
            SharedPrefs.setValue(context, SharedPrefs.CITY, userObject.getString("city"));
            SharedPrefs.setValue(context, SharedPrefs.PINCODE, userObject.getString("pincode"));
            SharedPrefs.setValue(context, SharedPrefs.PANCARD, userObject.getString("pancard"));
            SharedPrefs.setValue(context, SharedPrefs.AADHAR_CARD, userObject.getString("aadharcard"));
            SharedPrefs.setValue(context, SharedPrefs.KYC, userObject.getString("kyc"));
            SharedPrefs.setValue(context, SharedPrefs.ACCOUNT, userObject.getString("account"));
            SharedPrefs.setValue(context, SharedPrefs.BANK, userObject.getString("bank"));
            SharedPrefs.setValue(context, SharedPrefs.IFSC, userObject.getString("ifsc"));
            SharedPrefs.setValue(context, SharedPrefs.APP_TOKEN, userObject.getString("apptoken"));

            if (userObject.has("refercode")) {
                SharedPrefs.setValue(context, SharedPrefs.REF_CODE, userObject.getString("refercode"));
            } else {
                SharedPrefs.setValue(context, SharedPrefs.REF_CODE, "");
            }

            if (userObject.has("account2")) {
                SharedPrefs.setValue(context, SharedPrefs.ACCOUNT2, userObject.getString("account2"));
                SharedPrefs.setValue(context, SharedPrefs.BANK2, userObject.getString("bank2"));
                SharedPrefs.setValue(context, SharedPrefs.IFSC2, userObject.getString("ifsc2"));
            }

            if (userObject.has("account3")) {
                SharedPrefs.setValue(context, SharedPrefs.ACCOUNT3, userObject.getString("account3"));
                SharedPrefs.setValue(context, SharedPrefs.BANK3, userObject.getString("bank3"));
                SharedPrefs.setValue(context, SharedPrefs.IFSC3, userObject.getString("ifsc3"));
            }

            if (userObject.has("bcid"))
                SharedPrefs.setValue(context, SharedPrefs.BC_ID, userObject.getString("bcid"));
            if (userObject.has("psaid"))
                SharedPrefs.setValue(context, SharedPrefs.PSA_ID, userObject.getString("psaid"));
            if (userObject.has("bbpsid"))
                SharedPrefs.setValue(context, SharedPrefs.BBPS_ID, userObject.getString("bbpsid"));
            SharedPrefs.setValue(context, SharedPrefs.LOGIN_ID, inputMobile);

            // Role Object
            JSONObject roleObject = new JSONObject(userObject.getString("role"));
            SharedPrefs.setValue(context, SharedPrefs.ROLE_NAME, roleObject.getString("name"));
            SharedPrefs.setValue(context, SharedPrefs.ROLE_SLUG, roleObject.getString("slug"));

            // Company Object
            JSONObject companyObject = new JSONObject(userObject.getString("company"));
            SharedPrefs.setValue(context, SharedPrefs.COMPANY_ID, companyObject.getString("id"));
            SharedPrefs.setValue(context, SharedPrefs.COMPANY_NAME, companyObject.getString("companyname"));
            SharedPrefs.setValue(context, SharedPrefs.WEBSITE, companyObject.getString("website"));
            SharedPrefs.setValue(context, SharedPrefs.LOGO, companyObject.getString("logo"));
            context.finishAffinity();
            new PrefLoginManager(context).setFarmerLoginRes("1");

            String slug = SharedPrefs.getValue(context, SharedPrefs.ROLE_SLUG);
            if (slug.equalsIgnoreCase("retailer")) {
                context.startActivity(new Intent(context, DashBoardActivity.class));
            } else {
                context.startActivity(new Intent(context, DashBoardActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isPackageInstalled(String packagename, Context c) {
        PackageManager packageManager = c.getPackageManager();
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void dialContactPhone(final String phoneNumber, Activity con) {
        con.startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.fromParts("tel", phoneNumber, null)));
    }

    public static void openGmail(String id, Activity con) {
        String[] addresses = {id};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Support");
        con.startActivity(intent);
    }

    public static void openWhatsApp(String number, Activity context) {
        try {
            number = "91" + number;
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            if (isPackageInstalled("com.whatsapp", context)){
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");
                context.startActivity(sendIntent);
            }else if(isPackageInstalled("com.whatsapp.w4b", context)){
                sendIntent.setComponent(new ComponentName("com.whatsapp.w4b", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");
                context.startActivity(sendIntent);
            }else{
                Toast.makeText(context, "App is not available in your device", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Getting issues in opening whatsapp please try this option after some time.",
                    Toast.LENGTH_SHORT).show();
        }
    }



}
