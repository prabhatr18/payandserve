package com.payment.aeps.activity;

import static com.payment.aeps.app.ModuleConstants.AEPS_TYPE;
import static com.payment.aeps.app.ModuleConstants.PAYSPRINT_AEPS;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.aeps.ParentActivity;
import com.payment.aeps.R;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.network.VolleyGetNetworkCall;
import com.payment.aeps.util.DeviceDataModel;
import com.payment.aeps.util.ModuleSharedPrefs;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;
import com.payment.aeps.util.utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EKYCRegister extends ParentActivity implements VolleyGetNetworkCall.RequestResponseLis {
    private Button btnProceed;
    private String PID_DATA = "";
    private DeviceDataModel morphoDeviceData;
    String PidData = "<?xml version=\"1.0\"?> <PidOptions ver=\"1.0\"><Opts env=\"P\" fCount=\"1\" fType=\"2\" iCount=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"15000\" wadh=\"E0jzJ/P8UopUHAieZn8CKqS4WPMi5ZSYXgfnlfkWjrc=\" posh=\"UNKNOWN\" /></PidOptions>";

    private void init() {
        btnProceed = findViewById(R.id.btnProceed);
        String deviceValue = ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.SELECTED_DEVICE_INDEX);
        if (deviceValue == null || deviceValue.length() == 0) {
            Toast.makeText(this, "Scanner device selection is required", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupToolBar("EKYC");
    }

    private void setupToolBar(String title) {
        TextView tvToolbar = findViewById(R.id.tvToolBarTitle);
        tvToolbar.setText(title);
        ImageView backImage = findViewById(R.id.imgBack);
        backImage.setOnClickListener(view -> finish());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ekyc_register);
        init();
        btnProceed.setOnClickListener(view -> {
            scanDevice();
        });
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
                case 1:
                    this.morphoDeviceData = (new utils()).morphoDeviceData(this, data.getStringExtra("DEVICE_INFO"));
                    if (this.morphoDeviceData.getErrCode().equalsIgnoreCase("919")) {
                        this.MorphoFinger();
                    }
                    break;
                case 2:
                    dataModel = (new utils()).morphoFingerData(this, data.getStringExtra("PID_DATA"), this.morphoDeviceData);
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
        //param();
        if (PID_DATA != null && PID_DATA.length() > 0) {
            String url = ModuleConstants.EKYC_URL;
          /*  if (AEPS_TYPE.equalsIgnoreCase(PAYSPRINT_AEPS)) {
                url = ModuleConstants.baseUrl + "raeps/transaction";
            }*/
            networkCallUsingVolleyApi(url);
        } else {
            Toast.makeText(this, "PID DATA is not valid", Toast.LENGTH_SHORT).show();
        }
    }

    private void networkCallUsingVolleyApi(String url) {
        if (ModuleUtil.isOnline(this)) {
            new VolleyGetNetworkCall(this, this, url, 1, param()).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID));
        map.put("token", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN));
        map.put("transactionType", "useronboardekyc");
        map.put("primaryKeyId", getIntent().getStringExtra("primaryKeyId"));
        map.put("encodeFPTxnId", getIntent().getStringExtra("encodeFPTxnId"));
        map.put("biodata", PID_DATA);
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

        Print.P("PARAM : \n " + new JSONObject(map).toString());
        Print.appendLog("---------------------------- NEW REQUEST ----------------------");
        Print.appendLog(new JSONObject(map).toString());
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P(JSonResponse);
        try {
            JSONObject obj = new JSONObject(JSonResponse);
            String message = obj.getString("message");
            resPopup(message);
        } catch (Exception e) {
            Toast.makeText(this, "Parsing Exception", Toast.LENGTH_SHORT).show();
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
        intent2.putExtra("PID_OPTIONS", this.PidData);
        this.startActivityForResult(intent2, 2);
    }

    private void MantraFinger() {
        PackageManager packageManager = this.getPackageManager();
        if (ModuleUtil.isPackageInstalled("com.mantra.rdservice", packageManager)) {
            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName("com.mantra.rdservice", "com.mantra.rdservice.RDServiceActivity"));
            intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
            intent2.putExtra("PID_OPTIONS", this.PidData);
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
            intent2.putExtra("PID_OPTIONS", this.PidData);
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
            intent2.putExtra("PID_OPTIONS", this.PidData);
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
            intent.putExtra("PID_OPTIONS", this.PidData);
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
            intent2.putExtra("PID_OPTIONS", this.PidData);
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

    AlertDialog alert;

    private void resPopup(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Response")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    dialog.dismiss();
                    finish();
                });
        alert = builder.create();
        alert.show();
    }
}