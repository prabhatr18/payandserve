package com.payment.aeps.onboard.auth;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.payment.aeps.R;
import com.payment.aeps.activity.StateList;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.network.ImageUploadHelper;
import com.payment.aeps.network.MRequestResponseLis;
import com.payment.aeps.permission.AppPermissions;
import com.payment.aeps.permission.PermissionHandler;
import com.payment.aeps.util.DeviceDataModel;
import com.payment.aeps.util.ModuleSharedPrefs;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class Onboard extends AppCompatActivity {

    private static final int BANK_CODE = 1234;
    int PIC_TYPE = 0;
    private Button btnSignup;
    private EditText etName;
    private EditText etPhone;
    private EditText etFather;
    private EditText etAadhaar;
    private EditText etPan;
    private EditText etPincode;
    private EditText etState;
    private EditText etCity;
    private EditText etThana;
    private EditText etAddress;
    private EditText etPhoneAlternate;
    private EditText etDob;
    private Context context;

    private File aadharPics, pancardPics, passports, shoppics;
    private String aadharPicsString, pancardPicsString, passportsString, shoppicsString;
    private View viewAadharPics, viewPancardPics, viewPassports, viewShoppics;
    private ImageView imgAttachAadharPics, imgAttachPancardPics, imgAttachPassports, imgAttachShoppics;
    private ImageView imgPreviewAadharPics, imgPreviewPancardPics, imgPreviewPassports, imgPreviewShoppics;
    private TextView tvLblAadharPics, tvLblPancardPics, tvLblPassports, tvLblShoppics;

    public static void alertDialog(final Activity context, String str, String status) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Status");
        alertDialogBuilder.setMessage(str);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            dialog.cancel();
                            if (status.equalsIgnoreCase("TXN")) {
                                context.finish();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void init() {
        context = Onboard.this;
        btnSignup = findViewById(R.id.btnSubmit);
        etPhone = findViewById(R.id.etPhone);
        etName = findViewById(R.id.etName);
        etState = findViewById(R.id.etState);
        etCity = findViewById(R.id.etCity);
        etAddress = findViewById(R.id.etAddress);
        etPincode = findViewById(R.id.etPincode);
        etPan = findViewById(R.id.etPan);
        VM.setInputLimit(etPan, 10);
        etAadhaar = findViewById(R.id.etAadhar);
        etFather = findViewById(R.id.etFather);
        etThana = findViewById(R.id.etThana);
        etDob = findViewById(R.id.etDob);
        etPhoneAlternate = findViewById(R.id.etPhoneAlternate);

        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> finish());

        viewAadharPics = findViewById(R.id.aadharPics);
        viewPancardPics = findViewById(R.id.pancardPics);
        viewPassports = findViewById(R.id.passports);
        viewShoppics = findViewById(R.id.shoppics);

        imgAttachAadharPics = viewAadharPics.findViewById(R.id.imgAttach);
        imgAttachPancardPics = viewPancardPics.findViewById(R.id.imgAttach);
        imgAttachPassports = viewPassports.findViewById(R.id.imgAttach);
        imgAttachShoppics = viewShoppics.findViewById(R.id.imgAttach);

        imgAttachAadharPics.setOnClickListener(view -> {
            PIC_TYPE = 0;
            showBottomSheetDialog();
        });
        imgAttachPancardPics.setOnClickListener(view -> {
            PIC_TYPE = 1;
            showBottomSheetDialog();
        });
        imgAttachPassports.setOnClickListener(view -> {
            PIC_TYPE = 2;
            showBottomSheetDialog();
        });
        imgAttachShoppics.setOnClickListener(view -> {
            PIC_TYPE = 3;
            showBottomSheetDialog();
        });

        etDob.setOnClickListener(view -> {
            datePopUp();
        });

        imgPreviewAadharPics = viewAadharPics.findViewById(R.id.imgPreview);
        imgPreviewPancardPics = viewPancardPics.findViewById(R.id.imgPreview);
        imgPreviewPassports = viewPassports.findViewById(R.id.imgPreview);
        imgPreviewShoppics = viewShoppics.findViewById(R.id.imgPreview);

        tvLblAadharPics = viewAadharPics.findViewById(R.id.tvLabel);
        tvLblPancardPics = viewPancardPics.findViewById(R.id.tvLabel);
        tvLblPassports = viewPassports.findViewById(R.id.tvLabel);
        tvLblShoppics = viewShoppics.findViewById(R.id.tvLabel);

        tvLblAadharPics.setText("Aadhaar Front Pic");
        tvLblPancardPics.setText("Aadhaar Back Image");
        tvLblPassports.setText("Passport Image");
        tvLblShoppics.setText("Pancard Image");
    }

    private void datePopUp() {
        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.m_date_picker, null);
        adb.setView(view);
        final Dialog dialog;
        adb.setPositiveButton("Add",
                (dialog1, arg1) -> {
                    DatePicker etDatePicker = view.findViewById(R.id.datePicker1);
                    Calendar cal = GregorianCalendar.getInstance();
                    cal.set(etDatePicker.getYear(), etDatePicker.getMonth(), etDatePicker.getDayOfMonth());
                    Date date = cal.getTime();
                    String approxDateString = ModuleConstants.SHOWING_DATE_FORMAT.format(date);
                    etDob.setText(approxDateString);
                });
        dialog = adb.create();
        dialog.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moduke_activity_onboard);
        init();

        btnSignup.setOnClickListener(v -> {
            if (ModuleUtil.isOnline(Onboard.this)) {
                if (isValid()) {
                    upload();
                }
            } else {
                Toast.makeText(Onboard.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE};
        AppPermissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                //ModuleUtil.checkExtraPermission(Onboard.this, getPackageName());
            }
        });

        etState.setOnClickListener(view -> {
            Intent i = new Intent(Onboard.this, StateList.class);
            startActivityForResult(i, BANK_CODE);
        });
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID));
        map.put("apptoken", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN));
        Print.P("BANK PARAM" + new JSONObject(map).toString());
        return map;
    }

    private Intent getFileChooserIntent() {
        //String[] mimeTypes = {"image/*", "application/pdf"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        return intent;
    }

    private boolean isValid() {
        ArrayList<VM> vList = new ArrayList<>();
        vList.add(new VM(etPhone, "Mobile Number", 10));
        vList.add(new VM(etPhoneAlternate, "Alternate Mobile Number", 10));
        vList.add(new VM(etName, "Name"));
        vList.add(new VM(etFather, "Father Name"));
        vList.add(new VM(etThana, "Thana"));
        vList.add(new VM(etDob, "DOB"));
        vList.add(new VM(etState, "State"));
        vList.add(new VM(etCity, "City"));
        vList.add(new VM(etAddress, "Address"));
        vList.add(new VM(etPincode, "Pincode", 6));
        vList.add(new VM(etPan, "Pan Number", VM.PANCARD));
        vList.add(new VM(etAadhaar, "Aadhaar Number", 12, VM.AADHAAR));
        boolean flag = VM.isValidUtil(vList, this);
        if (flag) {
            ArrayList<VM> vList2 = new ArrayList<>();
            vList2.add(new VM("Aadhaar Pic", aadharPicsString));
            vList2.add(new VM("Pancard Pic", pancardPicsString));
            vList2.add(new VM("Passport Pic", passportsString));
            vList2.add(new VM("Shop Pic", shoppicsString));
            flag = VM.isValidStringUtil(vList2, this);
        }

        return flag;
    }

    private void confirmPopup(String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "OK",
                (dialog, id) -> {
                    dialog.cancel();
                    finish();
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        DeviceDataModel dataModel;
        try {
            switch (requestCode) {
                case BANK_CODE:
                    etState.setText(data.getStringExtra("name"));
                    break;
                case 2021:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri uri = data.getData();
                        File fileImage = new File(uri.getPath());
                        Bitmap mImageBitmap = BitmapFactory.decodeFile(fileImage.getAbsolutePath());
                        switch (PIC_TYPE) {
                            case 0:
                                aadharPics = fileImage ;
                                aadharPicsString = fileImage.getName();
                                imgPreviewAadharPics.setImageBitmap(mImageBitmap);
                                break;
                            case 1:
                                pancardPics = fileImage ;
                                pancardPicsString = fileImage.getName();
                                imgPreviewPancardPics.setImageBitmap(mImageBitmap);
                                break;
                            case 2:
                                passports = fileImage ;
                                passportsString = fileImage.getName();
                                imgPreviewPassports.setImageBitmap(mImageBitmap);
                                break;
                            case 3:
                                shoppics = fileImage ;
                                shoppicsString = fileImage.getName();
                                imgPreviewShoppics.setImageBitmap(mImageBitmap);
                                break;
                        }
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(this, "" + ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upload() {
        HashMap<String, String> map = new HashMap<>();
        map.put("merchantPhoneNumber", etPhone.getText().toString());
        map.put("merchantalernativeNumber", etPhoneAlternate.getText().toString());
        map.put("merchantName", etName.getText().toString());
        map.put("father", etFather.getText().toString());
        map.put("thana", etThana.getText().toString());
        map.put("dob", etDob.getText().toString());
        map.put("user_id", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID));
        map.put("token", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN));
        map.put("merchantState", etState.getText().toString());
        map.put("merchantCityName", etCity.getText().toString());
        map.put("merchantAddress", etAddress.getText().toString());
        map.put("merchantPinCode", etPincode.getText().toString());
        map.put("userPan", etPan.getText().toString());
        map.put("transactionType", "useronboard");
        map.put("merchantAadhar", etAadhaar.getText().toString());
        Print.P("Params : " + new JSONObject(map));
        new ImageUploadHelper(aadharPics, pancardPics, passports, shoppics, map,
                this, "iaeps", new MRequestResponseLis() {
            @Override
            public void onSuccessRequest(String result) {
                Print.P("onSuccessRequest : " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = "Getting some error in uploading process please try after some time";
                    String status = "";
                    if (jsonObject.has("status")) {
                        status = jsonObject.getString("status");
                    } else {
                        status = jsonObject.getString("statuscode");
                    }
                    if (jsonObject.has("message"))
                        message = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("success") || status.equalsIgnoreCase("TXN"))
                        alertDialog(Onboard.this, message, status);
                    else
                        alertDialog(Onboard.this, message, status);
                } catch (Exception e) {
                    Toast.makeText(context, "Expected Unhandled response in the uploading process", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailRequest(String msg) {
                Print.P("onFailRequest : " + msg);
            }
        });
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.custom_dialog_image_selection);
        Button btnSubmit = bottomSheetDialog.findViewById(R.id.btnClose);
        ImageView btnGallery = bottomSheetDialog.findViewById(R.id.btnGallery);
        ImageView btnCamera = bottomSheetDialog.findViewById(R.id.btnCamera);
        btnSubmit.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        btnCamera.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            ImagePicker.with(this).crop().cameraOnly().maxResultSize(720, 1080).start(2021);
        });

        btnGallery.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            ImagePicker.with(this).crop().galleryOnly().maxResultSize(720, 1080).start(2021);
        });
        bottomSheetDialog.show();
    }

}
