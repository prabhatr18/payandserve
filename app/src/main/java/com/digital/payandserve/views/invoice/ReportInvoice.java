package com.digital.payandserve.views.invoice;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.digital.payandserve.R;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.posprinter.AEMPrinter;
import com.digital.payandserve.posprinter.AEMScrybeDevice;
import com.digital.payandserve.posprinter.IAemScrybe;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.invoice.model.PrintDataModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.digital.payandserve.posprinter.AEMPrinter.ESCAPE_SEQ;


public class ReportInvoice extends AppCompatActivity implements View.OnClickListener, IAemScrybe {
    final ArrayList<PrintDataModel> list2;
    final ArrayList<PrintDataModel> list = list2 = new ArrayList<>();
    String message;
    private NestedScrollView scrollView;
    private Context context;
    private ImageView imgShare, imgPosPrinter,imgPrint;
    private LottieAnimationView imgTxnStatus;
    private TextView tvTxnStatus, tvValue,infoTv;
    private RelativeLayout top;
    private LinearLayout invoiceCon;

    private AEMPrinter m_AemPrinter = null;
    private AEMScrybeDevice m_AemScrybeDevice;
    private ArrayList<String> printerList;
    private ImageView btnprint, btnprint1;
    private char[] batteryStatusCommand = new char[]{0x1B, 0x7E, 0x42, 0x50, 0x7C, 0x47, 0x45, 0x54, 0x7C, 0x42, 0x41, 0x54, 0x5F, 0x53, 0x54, 0x5E};
    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    int glbPrinterWidth = 32;
    int numChars = glbPrinterWidth;

    public void init() {
        printerList = new ArrayList<>();
        m_AemScrybeDevice = new AEMScrybeDevice(this);
        imgShare = findViewById(R.id.imgShare);
        invoiceCon = findViewById(R.id.invoiceCon);
        imgPosPrinter = findViewById(R.id.imgPosPrinter);
        top = findViewById(R.id.top);
        imgPrint = findViewById(R.id.imgPrint);
        scrollView = findViewById(R.id.scrollView);
        tvValue = findViewById(R.id.tvValue);
        imgTxnStatus = findViewById(R.id.imgTxnStatus);
        tvTxnStatus = findViewById(R.id.tvTxnStatus);
        infoTv = findViewById(R.id.infoTv);
        setLis();
        permissionCheck();
        setupToolBar();
    }

    private void setupToolBar() {
        ImageView backImage = findViewById(R.id.imgClose);
        backImage.setOnClickListener(view -> finish());
    }

    private void setLis() {
        imgShare.setOnClickListener(this);
        imgPrint.setOnClickListener(this);
        imgPosPrinter.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.transparentStatusBar(this);
        setContentView(R.layout.report_invoice);
        init();
        context = ReportInvoice.this;
        parseRes();

        String info = SharedPrefs.getValue(this,SharedPrefs.USER_NAME) + " ("+SharedPrefs.getValue(this,SharedPrefs.USER_ID) + ")"+
                "\n"+" "+SharedPrefs.getValue(this,SharedPrefs.LOGIN_ID)+ "\n"+SharedPrefs.getValue(this,SharedPrefs.SHOP_NAME);
        infoTv.setText(info);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void parseRes() {
        try {
            message = getIntent().getStringExtra("remark");
            if (message == null || message.length() == 0 || message.trim().equalsIgnoreCase("null")) {
                message = "Not Available";
            }
            tvValue.setText(message);
            String status = getIntent().getStringExtra("status");
            switch (status) {
                case "success":
                case "SUCCESS":
                case "txn":
                case "TXN":
                    tvTxnStatus.setText(R.string.tran_success);
                    top.setBackgroundColor(getResources().getColor(R.color.green));
                    imgTxnStatus.setAnimation(R.raw.transaction_scuccess);
                    break;
                case "pending":
                    tvTxnStatus.setText(R.string.transaction_pending);
                    tvTxnStatus.setTextColor(getResources().getColor(R.color.text_yellow));
                    top.setBackgroundColor(getResources().getColor(R.color.bg_yellow));
                    imgTxnStatus.setAnimation(R.raw.payment_pending_file);
                    break;
                case "approved":
                    tvTxnStatus.setText(R.string.transaction_approved);
                    tvTxnStatus.setTextColor(getResources().getColor(R.color.textGreen));
                    top.setBackgroundColor(getResources().getColor(R.color.green));
                    imgTxnStatus.setAnimation(R.raw.transaction_scuccess);
                    break;
                case "rejected":
                    tvTxnStatus.setText(R.string.transaction_rejected);
                    tvTxnStatus.setTextColor(getResources().getColor(R.color.textRed));
                    top.setBackgroundColor(getResources().getColor(R.color.red));
                    imgTxnStatus.setAnimation(R.raw.payment_failed);
                    break;
                case "failed":
                case "failure":
                case "fail":
                case "Failed":
                    tvTxnStatus.setText(R.string.tran_failed);
                    tvTxnStatus.setTextColor(getResources().getColor(R.color.textRed));
                    top.setBackgroundColor(getResources().getColor(R.color.red));
                    imgTxnStatus.setAnimation(R.raw.payment_failed);
                    break;
                default:
                    tvTxnStatus.setText(String.format("%s %s", getString(R.string.transaction), status));
                    tvTxnStatus.setTextColor(getResources().getColor(R.color.text_yellow));
                    top.setBackgroundColor(getResources().getColor(R.color.bg_yellow));
                    imgTxnStatus.setAnimation(R.raw.payment_pending_file);
            }
            initTransactionList();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void initTransactionList() {
        try {
            InvoiceListAdapter adapter = new InvoiceListAdapter(this, Constants.INVOICE_DATA);
            ListView listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(context, "Invoice Generation Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public final void l() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else
            new wd().NUL(this.invoiceCon, this.context);
    }

    public final void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

    public final void k() {
        for (InvoiceModel m : Constants.INVOICE_DATA) {
            list.add(new PrintDataModel(m.getKey(), m.getValue()));
        }
        final PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        final String string = "E Banker Document";
        String s = "EBanker";
        list2.add(new PrintDataModel("Remark", message));
        printManager.print(string, new ud(this, list2, s, "app_name"), null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgPosPrinter) {
            if (isBTEnable()) {
                if (bAdapter == null) {
                    Toast.makeText(ReportInvoice.this, "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
                } else {
                    onShowPairedPrinters();
                }
            } else {
                Toast.makeText(context, "Please enable bluetooth and pair vriddhi printer for this functionality", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.imgPrint) {
            k();
        } else if (view.getId() == R.id.imgShare) {
            l();
        }
    }

    public void print() {
        try {
            StringBuilder data = new StringBuilder();
            m_AemPrinter.setFontType(AEMPrinter.FONT_002);
            data.append("\n");
            data.append("Pay And Serve");
            data.append("\n");
            m_AemPrinter.setFontType(ESCAPE_SEQ);//esc
            data.append(Constants.COMMON_DATE_FORMAT.format(new Date()));
            data.append("\n\n");
            if (Constants.INVOICE_DATA != null && Constants.INVOICE_DATA.size() > 0) {
                for (InvoiceModel model : Constants.INVOICE_DATA) {
                    String d = model.getValue();
                    if (MyUtil.isNN(d))
                        d = model.getValue().replace(getString(R.string.rupee), "Rs").toUpperCase();
                    data.append("\n").append(model.getKey()).append("  : ").append(d);
                }
            }
            m_AemPrinter.print(data.toString());
            m_AemPrinter.setCarriageReturn();
            m_AemPrinter.setCarriageReturn();
            m_AemPrinter.setCarriageReturn();
            m_AemPrinter.setCarriageReturn();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDiscoveryComplete(ArrayList<String> aemPrinterList) {

    }

    AlertDialog dialog;
    ProgressDialog p;

    private class PrintTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(ReportInvoice.this);
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            print();
            return "";
        }

        @Override
        protected void onPostExecute(String bitmap) {
            super.onPostExecute(bitmap);
            p.dismiss();
        }
    }

    private boolean isBTEnable() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.isEnabled();
    }

    private void callBluetoothActivity() {
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intentOpenBluetoothSettings);
    }

    AlertDialog alert;

    private void confirmPopup() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("No Printer")
                .setMessage("Please connect a bluetooth printer from device settings")
                .setPositiveButton("Ok", (dialog, whichButton) -> callBluetoothActivity())
                .setNegativeButton("Cancel", (dialog, whichButton) -> alert.dismiss());
        alert = builder.create();
        alert.show();
    }


    public void onShowPairedPrinters() {
        printerList = m_AemScrybeDevice.getPairedPrinters();
        if (printerList.size() > 0)
            printerList();
        else
            confirmPopup();
    }

    @Override
    protected void onDestroy() {
        if (m_AemScrybeDevice != null) {
            try {
                m_AemScrybeDevice.disConnectPrinter();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    public void onPrintBill() throws IOException {
        Toast.makeText(this, "Printing " + numChars + " Character/Line Bill", Toast.LENGTH_SHORT).show();
        //onPrintBillBluetooth(numChars);
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        new PrintTask().execute();
    }

    AlertDialog printerListDialog;
    int isSuccess = 0;

    private void printerList() {
        String[] array = new String[printerList.size()];
        int i = 0;
        for (String s : printerList) array[i++] = s;
        AlertDialog.Builder alert =
                new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog);
        alert.setTitle("Please select printer");
        alert.setSingleChoiceItems(array, -1, (dialog, which) -> {
            String printerName = array[which];
            try {
                m_AemScrybeDevice.connectToPrinter(printerName);
                //m_cardReader = m_AemScrybeDevice.getCardReader(this);
                m_AemPrinter = m_AemScrybeDevice.getAemPrinter();
                Toast.makeText(this, "Connected with " + printerName, Toast.LENGTH_SHORT).show();
                String data = new String(batteryStatusCommand);
                m_AemPrinter.print(data);
                //  m_cardReader.readMSR();
                if (m_AemPrinter != null) {
                    isSuccess = 1;
                    printerListDialog.dismiss();
                    onPrintBill();
                }
            } catch (Exception e) {
                isSuccess = 2;
                e.printStackTrace();
                if (e.getMessage().contains("Service discovery failed")) {
                    Toast.makeText(this, "Not Connected\n" + printerName + " is unreachable or off otherwise it is connected with other device", Toast.LENGTH_SHORT).show();
                } else if (e.getMessage().contains("Device or resource busy")) {
                    Toast.makeText(this, "the device is already connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Unable to connect", Toast.LENGTH_SHORT).show();
                }
            } finally {
                if (isSuccess == 1) {
                    Toast.makeText(context, "Printed Successfully", Toast.LENGTH_SHORT).show();
                } else if (isSuccess == 0) {
                    Toast.makeText(context, "Unable to connect with this device", Toast.LENGTH_SHORT).show();
                }
            }

        });
        printerListDialog = alert.create();
        printerListDialog.show();
    }
}


