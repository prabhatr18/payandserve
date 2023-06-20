package com.payment.aeps.activity;

import static com.payment.aeps.app.ModuleConstants.AEPS_TYPE;
import static com.payment.aeps.app.ModuleConstants.PAYSPRINT_AEPS;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.aeps.ParentActivity;
import com.payment.aeps.R;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.moduleprinter.ModuleInvoice;
import com.payment.aeps.network.VolleyGetNetworkCall;
import com.payment.aeps.util.AadharNumberPattern;
import com.payment.aeps.util.DeviceDataModel;
import com.payment.aeps.util.ModuleSharedPrefs;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;
import com.payment.aeps.util.utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class CashWithdraw extends ParentActivity implements View.OnClickListener, VolleyGetNetworkCall.RequestResponseLis {
    final private int BANK_CODE = 100;
    String PidData = "<?xml version=\"1.0\"?> <PidOptions ver=\"1.0\"> <Opts fCount=\"1\" fType=\"2\" iCount=\"0\" pCount=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" posh=\"UNKNOWN\" env=\"P\" /> <CustOpts><Param name=\"mantrakey\" value=\"\" /></CustOpts> </PidOptions>";
    //    String PidData = "<?xml version=\"1.0\"?> <PidOptions ver=\"1.0\"> <Opts fCount=\"1\" fType=\"0\" iCount=\"0\" pCount=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" posh=\"UNKNOWN\" env=\"P\" /> <CustOpts><Param name=\"mantrakey\" value=\"\" /></CustOpts> </PidOptions>";
    String transactionType;
    private EditText etMobile, etAmount;
    private EditText etAadhaar;
    private ImageView imgPlus, imgMinus;
    private TextView tvAmount1, tvAmount2, tvAmount3, tvAmount4, tvAmount5, tvBank;
    private long amount = 0;
    private String bankId, bankName;
    private Button btnProceed;
    private String PID_DATA = "";
    private DeviceDataModel morphoDeviceData;

    public static boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = AadharNumberPattern.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    private void init() {
        etMobile = findViewById(R.id.etMobile);
        etAadhaar = findViewById(R.id.etAadhaar);
        tvAmount1 = findViewById(R.id.tvAmount1);
        tvAmount2 = findViewById(R.id.tvAmount2);
        tvAmount3 = findViewById(R.id.tvAmount3);
        tvAmount4 = findViewById(R.id.tvAmount4);
        tvAmount5 = findViewById(R.id.tvAmount5);
        imgPlus = findViewById(R.id.imgPlus);
        btnProceed = findViewById(R.id.btnProceed);
        etAmount = findViewById(R.id.etAmount);
        imgMinus = findViewById(R.id.imgMinus);
        tvBank = findViewById(R.id.tvBank);

        setClickLis();

        transactionType = getIntent().getStringExtra("option");
        switch (transactionType) {
            case "1":
                setupToolBar("Balance Enquiry");
                transactionType = "BE";
                hideAmountSec();
                break;
            case "2":
                setupToolBar("Aadhaar Pay");
                transactionType = "M";
                break;
            case "3":
                setupToolBar("Cash Withdraw");
                transactionType = "CW";
                break;
            case "4":
                setupToolBar("Mini Statement");
                transactionType = "MS";
                hideAmountSec();
                break;
        }

        String deviceValue = ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.SELECTED_DEVICE_INDEX);
        if (deviceValue == null || deviceValue.length() == 0) {
            Toast.makeText(this, "Scanner device selection is required", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void hideAmountSec() {
        etAmount.setText("0");
        findViewById(R.id.amountSec).setVisibility(View.GONE);
    }

    private void setupToolBar(String title) {
        TextView tvToolbar = findViewById(R.id.tvToolBarTitle);
        tvToolbar.setText(title);
        ImageView backImage = findViewById(R.id.imgBack);
        backImage.setOnClickListener(view -> finish());
    }

    private void setClickLis() {
        tvAmount1.setOnClickListener(this);
        tvAmount2.setOnClickListener(this);
        tvAmount3.setOnClickListener(this);
        tvAmount4.setOnClickListener(this);
        tvAmount5.setOnClickListener(this);
        etAmount.setOnClickListener(this);
        imgPlus.setOnClickListener(this);
        imgMinus.setOnClickListener(this);
        tvBank.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_withdraw);

        init();

        tvBank.setOnClickListener(view -> {
            Intent i = new Intent(CashWithdraw.this, BankList.class);
            startActivityForResult(i, BANK_CODE);
        });

        btnProceed.setOnClickListener(view -> {
            if (isValid()) {
                scanDevice();
            }
        });
    }

    private boolean isValid() {
        if (bankId == null || bankId.length() == 0) {
            Toast.makeText(this, "Bank selection is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAadhaar == null || etAadhaar.getText().toString().length() == 0 || !validateAadharNumber(etAadhaar.getText().toString())) {
            Toast.makeText(this, "Please provide valid aadhhar number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etMobile == null || etMobile.getText().toString().length() != 10) {
            Toast.makeText(this, "Please provide valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (transactionType.equals("CW") || transactionType.equals("M")) {
            if (etAmount == null || etAmount.getText().toString().length() == 0) {
                Toast.makeText(this, "Please input valid amount string", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Double.parseDouble(etAmount.getText().toString()) < 100) {
                Toast.makeText(this, "Minimum amount should be 100", Toast.LENGTH_SHORT).show();
                //return false;
            }
        }
        return true;
    }

    private void log(String log) {
        //Log.e("LOG: ", log);
    }

    @Override
    public void onClick(View view) {
        if (etAmount.getText().toString().length() > 0)
            amount = Long.parseLong(etAmount.getText().toString());
        if (view.getId() == R.id.tvAmount1) {
            amount = 100;
        } else if (view.getId() == R.id.tvAmount2) {
            amount = 500;
        } else if (view.getId() == R.id.tvAmount3) {
            amount = 1000;
        } else if (view.getId() == R.id.tvAmount4) {
            amount = 1500;
        } else if (view.getId() == R.id.tvAmount5) {
            amount = 2000;
        } else if (view.getId() == R.id.imgPlus) {
            amount++;
        } else if (view.getId() == R.id.imgMinus) {
            if (amount > 100)
                amount--;
        }
        setAmount();
    }

    private void setAmount() {
        etAmount.setText(String.valueOf(amount));
    }

    private void scanDevice() {
        int indexCount = 50;
        String deviceValue = ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.SELECTED_DEVICE_INDEX);
        if (deviceValue != null && deviceValue.length() > 0) {
            indexCount = Integer.parseInt(deviceValue);
        }

        switch (indexCount) {
            case 0:
                this.MantraFinger();
                break;
            case 1:
                this.MorphoDevice();
                break;
            case 2:
                this.TatvikFinger();
                break;
            case 3:
                this.StarTekFinger();
                break;
            case 4:
                this.SecuGenFinger();
                break;
            case 5:
                this.EvoluteFinger();
                break;
            default:
                showMsg("Something went wrong.Please try again later.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        DeviceDataModel dataModel;
        try {
            switch (requestCode) {
                case BANK_CODE:
                    bankId = data.getStringExtra("id");
                    bankName = data.getStringExtra("name");
                    tvBank.setText(bankName);
                    break;
                case 1:
                    this.morphoDeviceData = (new utils()).morphoDeviceData(this, data.getStringExtra("DEVICE_INFO"));
                    if (this.morphoDeviceData.getErrCode().equalsIgnoreCase("919")) {
                        this.MorphoFinger();
                    }
                    break;
                case 2:
                    dataModel = (new utils()).morphoFingerData(this, data.getStringExtra("PID_DATA"), this.morphoDeviceData);
                    Log.e("ERROR INFO", "=-----------------------------------");
                    //Log.e("ERROR INFO", "" + dataModel.getErrMsg());
                    if (dataModel.getErrCode().equalsIgnoreCase("0")) {
                        PID_DATA = data.getStringExtra("PID_DATA");
                        callApiWithPidData();
                    } else if (ModuleUtil.getPreferredPackage(this, "com.scl.rdservice").equalsIgnoreCase("")) {
                        showMsg(dataModel.getErrCode() + " : Morpho " + dataModel.getErrMsg() + ModuleUtil.getPreferredPackage(this, "com.scl.rdservice"));
                    }
                    break;
                case 3:
                    dataModel = (new utils()).mantraData(data.getStringExtra("PID_DATA"), this);
                    if (dataModel.getErrCode().equalsIgnoreCase("0")) {
                        PID_DATA = data.getStringExtra("PID_DATA");
                        callApiWithPidData();
                    } else if (ModuleUtil.getPreferredPackage(this, "com.mantra.rdservice").equalsIgnoreCase("")) {
                        showMsg(dataModel.getErrCode() + " : Mantra " + dataModel.getErrMsg() + ModuleUtil.getPreferredPackage(this, "com.mantra.rdservice"));
                    }
                    break;
                case 4:
                    dataModel = (new utils()).secugenData(this, data.getStringExtra("PID_DATA"));
                    if (dataModel.getErrCode().equalsIgnoreCase("0")) {
                        PID_DATA = data.getStringExtra("PID_DATA");
                        callApiWithPidData();
                    } else if (ModuleUtil.getPreferredPackage(this, "com.secugen.rdservice").equalsIgnoreCase("")) {
                        showMsg(dataModel.getErrCode() + " : Sucugen " + dataModel.getErrMsg() + ModuleUtil.getPreferredPackage(this, "com.secugen.rdservice"));
                    }
                    break;
                case 5:
                    dataModel = (new utils()).tatvikData(this, data.getStringExtra("PID_DATA"));
                    if (dataModel.getErrCode().equalsIgnoreCase("0")) {
                        PID_DATA = data.getStringExtra("PID_DATA");
                        callApiWithPidData();
                    } else if (ModuleUtil.getPreferredPackage(this, "com.tatvik.bio.tmf20").equalsIgnoreCase("")) {
                        showMsg(dataModel.getErrCode() + " : Tatvik " + dataModel.getErrMsg() + ModuleUtil.getPreferredPackage(this, "com.tatvik.bio.tmf20"));
                    }
                    break;
                case 6:
                    dataModel = (new utils()).starTekData(this, data.getStringExtra("PID_DATA"));
                    if (dataModel.getErrCode().equalsIgnoreCase("0")) {
                        PID_DATA = data.getStringExtra("PID_DATA");
                        callApiWithPidData();
                    } else if (ModuleUtil.getPreferredPackage(this, "com.acpl.registersdk").equalsIgnoreCase("")) {
                        showMsg(dataModel.getErrCode() + " : Startek " + dataModel.getErrMsg() + ModuleUtil.getPreferredPackage(this, "com.acpl.registersdk"));
                    }
                    break;
                case 7:
                    dataModel = (new utils()).EvoluteData(this, data.getStringExtra("PID_DATA"));
                    if (dataModel.getErrCode().equalsIgnoreCase("0")) {
                        PID_DATA = data.getStringExtra("PID_DATA");
                        callApiWithPidData();
                    } else if (ModuleUtil.getPreferredPackage(this, "com.evolute.rdservice").equalsIgnoreCase("")) {
                        showMsg(dataModel.getErrCode() + " : Evolute" + dataModel.getErrMsg() + ModuleUtil.getPreferredPackage(this, "com.evolute.rdservice"));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callApiWithPidData() {
        if (PID_DATA != null && PID_DATA.length() > 0) {
            String url = ModuleConstants.AEPS_API;
            if (AEPS_TYPE.equalsIgnoreCase(PAYSPRINT_AEPS)) {
                url = ModuleConstants.baseUrl + "paysprint/aeps";
            }
            networkCallUsingVolleyApi(url);
        } else {
            Toast.makeText(this, "PID DATA is not valid", Toast.LENGTH_SHORT).show();
        }
    }

    private void networkCallUsingVolleyApi(String url) {
        Print.P(url);
        if (ModuleUtil.isOnline(this)) {
            new VolleyGetNetworkCall(this, this, url, 1, param()).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID));
        map.put("apptoken", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN));
        map.put("transactionType", transactionType);  //"value": "BE/CW/MS/M"
        map.put("mobileNumber", etMobile.getText().toString());
        map.put("adhaarNumber", etAadhaar.getText().toString());
        map.put("bankName1", bankId);
        map.put("bankid", bankId);
        map.put("txtPidData", PID_DATA);

        if (transactionType.equals("M"))
            map.put("bankName2", bankId);

        if (transactionType.equals("CW") || transactionType.equals("M"))
            map.put("transactionAmount", etAmount.getText().toString());  //in case CW/M

        int indexCount = 50;
        String deviceValue = ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.SELECTED_DEVICE_INDEX);
        if (deviceValue != null && deviceValue.length() > 0) {
            indexCount = Integer.parseInt(deviceValue);
        }

        if (indexCount == 0) {
            map.put("device", "MANTRA_PROTOBUF");
        } else {
            map.put("device", "MORPHO_PROTOBUF");
        }

        Print.P("PARAM : \n " + new JSONObject(map));
        Print.appendLog("---------------------------- NEW REQUEST ----------------------");
        Print.appendLog(new JSONObject(map).toString());
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P(JSonResponse);
        try {
            Print.appendLog(JSonResponse);
            Print.P("API RES : \n" + JSonResponse);
            Intent i = new Intent(CashWithdraw.this, ModuleInvoice.class);
            i.putExtra("invoice", JSonResponse);
            i.putExtra("type", transactionType);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        showMsg(msg);
    }

    private void MorphoDevice() {
        PackageManager packageManager = this.getPackageManager();
        if (ModuleUtil.isPackageInstalled("com.scl.rdservice", packageManager)) {
            Intent intent = new Intent("in.gov.uidai.rdservice.fp.INFO");
            intent.setPackage("com.scl.rdservice");
            this.startActivityForResult(intent, 1);
        } else {
            serviceNotFoundDialog("Get Service", "Morpho RD Services Not Found.Click OK to Download Now.",
                    "https://play.google.com/store/apps/details?id=com.scl.rdservice");
        }
    }

    private void MorphoFinger() {
        Intent intent2 = new Intent();
        intent2.setComponent(new ComponentName("com.scl.rdservice", "com.scl.rdservice.FingerCaptureActivity"));
        intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
        intent2.putExtra("PID_OPTIONS", PidData);
        this.startActivityForResult(intent2, 2);
    }

    private void MantraFinger() {
        PackageManager packageManager = this.getPackageManager();
        if (ModuleUtil.isPackageInstalled("com.mantra.rdservice", packageManager)) {
            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName("com.mantra.rdservice", "com.mantra.rdservice.RDServiceActivity"));
            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
            intent2.putExtra("PID_OPTIONS", PidData);
            this.startActivityForResult(intent2, 3);
        } else {
            serviceNotFoundDialog("Get Service", "Mantra RD Services Not Found.Click OK to Download Now.",
                    "https://play.google.com/store/apps/details?id=com.mantra.rdservice");
        }

    }

    private void SecuGenFinger() {
        PackageManager packageManager = this.getPackageManager();
        if (ModuleUtil.isPackageInstalled("com.secugen.rdservice", packageManager)) {
            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName("com.secugen.rdservice", "com.secugen.rdservice.Capture"));
            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
            intent2.putExtra("PID_OPTIONS", PidData);
            this.startActivityForResult(intent2, 4);
        } else {
            serviceNotFoundDialog("Get Service",
                    "SecuGen RD Services Not Found.Click OK to Download Now.",
                    "https://play.google.com/store/apps/details?id=com.secugen.rdservice");
        }

    }

    private void TatvikFinger() {
        PackageManager packageManager = this.getPackageManager();
        if (ModuleUtil.isPackageInstalled("com.tatvik.bio.tmf20", packageManager)) {
            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName("com.tatvik.bio.tmf20", "com.tatvik.bio.tmf20.RDMainActivity"));
            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
            intent2.putExtra("PID_OPTIONS", PidData);
            this.startActivityForResult(intent2, 5);
        } else {
            serviceNotFoundDialog("Get Service",
                    "Tatvik RD Services Not Found.Click OK to Download Now.",
                    "https://play.google.com/store/apps/details?id=com.tatvik.bio.tmf20");
        }

    }

    private void StarTekFinger() {
        PackageManager packageManager = this.getPackageManager();
        if (ModuleUtil.isPackageInstalled("com.acpl.registersdk", packageManager)) {
            Intent intent = new Intent();
            intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
            intent.setComponent(new ComponentName("com.acpl.registersdk", "com.acpl.registersdk.MainActivity"));
            intent.putExtra("PID_OPTIONS", PidData);
            this.startActivityForResult(intent, 6);
        } else {
            serviceNotFoundDialog("Get Service",
                    "Startek RD Services Not Found.Click OK to Download Now.",
                    "https://play.google.com/store/apps/details?id=com.acpl.registersdk");
        }
    }

    private void EvoluteFinger() {
        PackageManager packageManager = this.getPackageManager();
        if (ModuleUtil.isPackageInstalled("com.evolute.rdservice", packageManager)) {
            Intent intent2 = new Intent();
            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
            intent2.setComponent(new ComponentName("com.evolute.rdservice", "com.evolute.rdservice.RDserviceActivity"));
            intent2.putExtra("PID_OPTIONS", PidData);
            this.startActivityForResult(intent2, 7);
        } else {
            serviceNotFoundDialog("Get Service",
                    "Evolute RD Services Not Found.Click OK to Download Now.",
                    "https://play.google.com/store/apps/details?id=com.evolute.rdservice");
        }
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void serviceNotFoundDialog(String title, String msg, String url) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", (dialog, which) -> {
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            } catch (Exception var4) {
                showMsg("Something went wrong.Please try again later.");
                var4.printStackTrace();
            }
        });
        alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}