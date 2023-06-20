package com.digital.payandserve.views.mhagram_aeps_matm;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.views.on_boarding.OnBoardingActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.invoice.MatmInvoice;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.mhagram_aeps_matm.model.Mo;
import com.google.gson.GsonBuilder;

import org.egram.aepslib.DashboardActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HandlerActivity extends AppCompatActivity implements RequestResponseLis {
    private Mo model;
    private String type = "";
    private String appUserId = "";
    int flag = 0;
    String clientrefid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.aeps_matm_activity_handler);
        type = getIntent().getStringExtra("type");
        appUserId = getIntent().getStringExtra("appUserId");
        if (type != null && type.equalsIgnoreCase("aeps")) {
            String bcidStatus = "aa";
            if (MyUtil.isNN(bcidStatus)) {
                networkCallUsingVolleyApi(Constants.URL.AEPS_INITIATE, false);
            } else {

            }
        } else if (type != null && type.equalsIgnoreCase("matm")) {
            networkCallUsingVolleyApi(Constants.URL.MATM_INITIATE, false);
        } else {
            finishWithResult("Type value is not correct please check that and try again", 5000);
        }
    }

    private void callAeps(final String saltKey, final String secretKey, final String BcId, final String userId,
                          final String emailId, final String phone, final String cpid) {
        flag = 10;
        Intent intent = new Intent((Context) this, DashboardActivity.class);
        intent.putExtra("saltKey", saltKey);
        intent.putExtra("secretKey", secretKey);
        intent.putExtra("BcId", BcId);
        intent.putExtra("UserId", userId);
        intent.putExtra("bcEmailId", emailId);
        intent.putExtra("Phone1", phone);
        intent.putExtra("cpid", cpid);//(If any)
        startActivityForResult(intent, 1);
    }

    private void mCallMicroATMIntent(final String saltKey, final String secretKey, final String BcId,
                                     final String userId, final String emailId, final String phone, final String rId) {

        PackageManager packageManager = getPackageManager();
        if (isPackageInstalled("org.egram.microatm", packageManager)) {
            Intent intent = new Intent();
            intent.setComponent(new
                    ComponentName("org.egram.microatm", "org.egram.microatm.BluetoothMacSearchActivity"));
            intent.putExtra("saltkey", saltKey);
            intent.putExtra("secretkey", secretKey);
            intent.putExtra("bcid", BcId);
            intent.putExtra("userid", userId);
            intent.putExtra("bcemailid", emailId);
            intent.putExtra("phone1", phone);
            intent.putExtra("clientrefid", rId);
            intent.putExtra("vendorid", "");
            intent.putExtra("udf1", "");
            intent.putExtra("udf2", "");
            intent.putExtra("udf3", "");
            intent.putExtra("udf4", "");
            startActivityForResult(intent, 123);
        } else {
            AlertDialog.Builder alertDialog = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog);
            alertDialog.setTitle("Get Service");
            alertDialog.setMessage("MicroATM Service not installed. Click OK to download .");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String appPackageName = "org.egram.microatm";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            alertDialog.setNegativeButton("Cancel", (dialog, i) -> dialog.dismiss());
            alertDialog.show();
        }
    }

    public static boolean isPackageInstalled(String packagename, PackageManager
            packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        flag = 1;
        try {
            if (requestCode == 1) {
                String msg = data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message");
                finishWithResult(msg, 5000);
            }  else if (requestCode == 123) {
                if (resultCode == RESULT_OK) {
                    Constants.INVOICE_DATA = new ArrayList<>();
                    String status = "";
                    String msg = "";
                    String respCode = data.getStringExtra("respcode");
                    if (respCode.equals("999")) {//Response code from bank(999 for pending transactions 00 for success)
                        String requesttxn = data.getStringExtra("requesttxn ");//Type of transaction
                        String refstan = data.getStringExtra("refstan");// Mahagram Stan Numbe
                        String mid = data.getStringExtra("mid");//Mid
                        String tid = data.getStringExtra("tid");//Tid
                        String clientrefid = data.getStringExtra("clientrefid");//Your reference Id
                        String vendorid = data.getStringExtra("vendorid");//Your define value
                        String udf1 = data.getStringExtra("udf1");//Your define value
                        String udf2 = data.getStringExtra("udf2");//Your define value
                        String udf3 = data.getStringExtra("udf3");//Your define value
                        String udf4 = data.getStringExtra("udf4");//Your define value
                        String date = data.getStringExtra("date");//Date of transaction
                        String txnamount = data.getStringExtra("txnamount");//Transaction amount (0 in case of balanceinquiry and transaction amount in cash withdrawal)
                        model = new Mo(requesttxn, refstan, txnamount, mid, tid, clientrefid, vendorid, udf1, udf2, udf3, udf4, date, "", "", "", "", "", respCode, "");
                        if (MyUtil.isNN(requesttxn))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Txnid", requesttxn));
                        if (MyUtil.isNN(refstan))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Ref Stan", refstan));
                        if (MyUtil.isNN(mid))
                            Constants.INVOICE_DATA.add(new InvoiceModel("MID", mid));
                        if (MyUtil.isNN(tid))
                            Constants.INVOICE_DATA.add(new InvoiceModel("TID", tid));
                        if (MyUtil.isNN(clientrefid))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Clientrefid", clientrefid));
                        if (MyUtil.isNN(vendorid))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Vendorid", vendorid));
                        if (MyUtil.isNN(date))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Date", date));
                        if (MyUtil.isNN(txnamount))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(this, txnamount)));
                    } else {
                        String requesttxn = data.getStringExtra("requesttxn ");//Type of transaction
                        String bankremarks = data.getStringExtra("msg");//Bank message
                        msg = bankremarks;
                        String refstan = data.getStringExtra("refstan");// Mahagram Stan Number
                        String cardno = data.getStringExtra("cardno");//Atm card number
                        String date = data.getStringExtra("date");//Date of transaction
                        String amount = data.getStringExtra("amount");//Account Balance
                        String invoicenumber = data.getStringExtra("invoicenumber");//Invoice Number
                        String mid = data.getStringExtra("mid");//Mid
                        String tid = data.getStringExtra("tid");//Tid
                        String clientrefid = data.getStringExtra("clientrefid");//Your reference Id
                        String vendorid = data.getStringExtra("vendorid");//Your define value
                        String udf1 = data.getStringExtra("udf1");//Your define value
                        String udf2 = data.getStringExtra("udf2");//Your define value
                        String udf3 = data.getStringExtra("udf3");//Your define value
                        String udf4 = data.getStringExtra("udf4");//Your define value
                        String txnamount = data.getStringExtra("txnamount");//Transaction amount (0 in case of balanceinquiry and transaction amount in cash withdrawal)
                        String rrn = data.getStringExtra("rrn");//Bank RRN number
                        model = new Mo(requesttxn, refstan, txnamount, mid, tid, clientrefid, vendorid, udf1, udf2, udf3, udf4, date, bankremarks, cardno, amount, invoicenumber, rrn, respCode, "");
                        if (MyUtil.isNN(invoicenumber))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Invoice No", invoicenumber));
                        if (MyUtil.isNN(requesttxn))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Txnid", requesttxn));
                        if (MyUtil.isNN(rrn))
                            Constants.INVOICE_DATA.add(new InvoiceModel("RRN", rrn));
                        if (MyUtil.isNN(refstan))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Ref Stan", refstan));
                        if (MyUtil.isNN(cardno))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Card No", cardno));
                        if (MyUtil.isNN(date))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Date", date));
                        if (MyUtil.isNN(mid))
                            Constants.INVOICE_DATA.add(new InvoiceModel("MID", mid));
                        if (MyUtil.isNN(tid))
                            Constants.INVOICE_DATA.add(new InvoiceModel("TID", tid));
                        if (MyUtil.isNN(clientrefid))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Clientrefid", clientrefid));
                        if (MyUtil.isNN(vendorid))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Vendorid", vendorid));
                        if (MyUtil.isNN(txnamount))
                            Constants.INVOICE_DATA.add(new InvoiceModel("TXN Amount", txnamount));
                        if (MyUtil.isNN(amount))
                            Constants.INVOICE_DATA.add(new InvoiceModel("Amount", amount));
                    }
                    Constants.INVOICE_DATA.add(new InvoiceModel("Res Code", respCode));
                    if (respCode.equalsIgnoreCase("00")) {
                        Constants.INVOICE_DATA.add(new InvoiceModel("Status", "Success"));
                        status = "success";
                    } else if (respCode.equalsIgnoreCase("999")) {
                        Constants.INVOICE_DATA.add(new InvoiceModel("Status", "Pending"));
                        status = "pending";
                    } else {
                        Constants.INVOICE_DATA.add(new InvoiceModel("Status", "Failed"));
                        status = "failed";
                    }
                    Intent i = new Intent(this, MatmInvoice.class);
                    i.putExtra("status", status);
                    i.putExtra("remark", "" + msg);
                    i.putExtra("data", model);
                    startActivity(i);
                    finish();
                } else {
                    try {
                        data.getStringExtra("statuscode"); //to get status code
                        data.getStringExtra("message"); //to get response message
                        String msg = "Status " + data.getStringExtra("statuscode") + "\nMessage " + data.getStringExtra("message");
                        ExtensionFunction.showToast(this,msg);
                        if (data.getStringExtra("statuscode").equals("111")) {
                            sdk();
                        } else {
                            String msg1 = "Status " + data.getStringExtra("statuscode") + "\nMessage " + data.getStringExtra("message");
                            finishWithResult(msg, 5000);
                            sendBackSdkRes(data.getStringExtra("statuscode"), data.getStringExtra("message"));
                        }
                    } catch (Exception e) {
                        String msg = data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message");
                        ExtensionFunction.showToast(this,msg);
                        finishWithResult(msg, 5000);
                    }
                }
            } else{
                try {
                    String msg = data.getStringExtra("statuscode") + "\n" + data.getStringExtra("message");
                    Print.P(msg);
                    finishWithResult(msg, 5000);
                } catch (Exception e) {
                    String msg = data.getStringExtra("StatusCode") + "\n" + data.getStringExtra("Message");
                    Print.P(msg);
                    finishWithResult(msg, 5000);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    AlertDialog alert;
    private void sdk() {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Information")
                .setMessage("Micro Atm app sdk version is outdated do you want to update ?")
                .setPositiveButton("Yes", (dialog, whichButton) -> {
                    alert.dismiss();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=org.egram.microatm")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton(android.R.string.no, (dialog, whichButton) -> {
                    alert.dismiss();
                    finish();
                });

        alert = builder.create();
        alert.show();
    }

    private void aepsDialog() {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("KYC")
                .setMessage("Please continue to complete your KYC then you can access our aeps service")
                .setPositiveButton("Yes", (dialog, whichButton) -> {
                    alert.dismiss();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=org.egram.microatm")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton(android.R.string.no, (dialog, whichButton) -> {
                    alert.dismiss();
                    finish();
                });

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onSuccessRequest(String Response) {
        try {

            JSONObject data = new JSONObject(Response).getJSONObject("data");
            String saltKey = data.getString("saltKey");
            String secretKey = data.getString("secretKey");
            String BcId = data.getString("BcId");
            String UserId = data.getString("UserId");
            String bcEmailId = data.getString("bcEmailId");
            String Phone1 = data.getString("Phone1");
            if (type != null && type.equals("aeps")) {
                callAeps(saltKey, secretKey, BcId, UserId, bcEmailId, Phone1, appUserId);
            } else if (type != null && type.equals("matm")) {
                 clientrefid = data.getString("clientrefid");
                mCallMicroATMIntent(saltKey, secretKey, BcId, UserId, bcEmailId, Phone1, clientrefid);
            } else {
                String msg = "Type param is not correct";
                finishWithResult(msg, 5000);
            }
        } catch (JSONException e) {
            finishWithResult("Json response parsing error please check the response.: " + Response, 5000);
            e.printStackTrace();
        }
    }

    private void finishWithResult(String msg, int code) {
        flag = 2;
        Intent returnIntent = new Intent();
        returnIntent.putExtra("msg", msg);
        setResult(code, returnIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type.equals("aeps") && flag > 0 && flag != 1) {
            finishWithResult("Back pressed transaction cancelled", 5000);
        }
    }

    @Override
    public void onFailRequest(String msg) {
        //finishWithResult("Something went wrong with the network call please try again after some time", 5000);
        Print.P(msg);
        if (msg.equalsIgnoreCase("Aeps Registration Pending")) {
            ExtensionFunction.startActivity(this, OnBoardingActivity.class);
            finish();
        } else {
            Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendBackSdkRes(String statusCode, String msg) {
        new VolleyNetworkCall(new RequestResponseLis() {
            @Override
            public void onSuccessRequest(String JSonResponse) {
                Print.P("Res : " + JSonResponse);
            }

            @Override
            public void onFailRequest(String msg) {
                Print.P("Res : " + msg);
            }
        }, this, Constants.URL.MATM_DATA_UPDATE, 1, matmParam(msg, statusCode),
                false, false).netWorkCall();
    }

    private Map<String, String> matmParam(String msg, String code) {
        Map<String, String> map = new HashMap<>();
        model = new Mo("", "", "", "", "", clientrefid, "", "",
                "", "", "", "", "", "", "", "",
                "", code, msg);
        map.put("response", new GsonBuilder().create().toJson(model));
        map.put("res", new GsonBuilder().create().toJson(model));
        return map;
    }
}
