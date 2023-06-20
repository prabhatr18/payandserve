package com.digital.payandserve.views.auth;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Signup extends AppCompatActivity implements RequestResponseLis {

    ProgressDialog dialog;
    String slug = "";
    String genderType = "Male";
    private Button btnSignup;
    private EditText etName;
    private EditText etMobile;
    private EditText etEmail;
    private EditText etShop;
    private EditText etPan;
    private EditText etAadhar;
    private EditText etState;
    private EditText etCity;
    private EditText etAddress;
    private EditText etPincode;
    private EditText etReferal;
    private EditText etDob;
    private Spinner spnrSlug;
    private RadioButton rbMale, rbFemale;
    private Context context;
    private AlertDialog loaderDialog;
    InstallReferrerClient referrerClient;

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    private void init() {
        context = Signup.this;
        referrerClient = InstallReferrerClient.newBuilder((Context) this).build();
        etName = findViewById(R.id.etName);
        etPincode = findViewById(R.id.etPincode);
        etReferal = findViewById(R.id.etReferal);
        etDob = findViewById(R.id.etDateOfBirth);
        spnrSlug = findViewById(R.id.spnrSlug);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etShop = findViewById(R.id.etShop);
        etPan = findViewById(R.id.etPan);
        etAadhar = findViewById(R.id.etAadhar);
        etState = findViewById(R.id.etState);
        etCity = findViewById(R.id.etCity);
        etAddress = findViewById(R.id.etAddress);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnSignup = findViewById(R.id.btnSubmit);

        rbMale.setOnClickListener(v -> genderType = "Male");
        rbFemale.setOnClickListener(v -> genderType = "Female");
        etDob.setOnClickListener(arg0 -> {
            final AlertDialog.Builder adb = new AlertDialog.Builder((Context) this);
            final View view = LayoutInflater.from((Context) this).inflate(R.layout.date_picker, null);
            adb.setView(view);
            final Dialog dialog;
            adb.setPositiveButton("OK",
                    (dialog1, arg1) -> {
                        DatePicker etDatePicker = view.findViewById(R.id.datePicker1);
                        etDatePicker.setMaxDate(System.currentTimeMillis());
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.set(etDatePicker.getYear(), etDatePicker.getMonth(), etDatePicker.getDayOfMonth());
                        Date dateSelected = cal.getTime();
                        Date current = new Date();
                        if (getDiffYears(dateSelected, current) >= 18) {
                            String approxDateString = Constants.SHOWING_DATE_FORMAT.format(dateSelected);
                            etDob.setText(approxDateString);
                        } else {
                            etDob.setText("");
                            Toast.makeText((Context) this, "Under 18 is not eligible", Toast.LENGTH_SHORT).show();
                        }

                    });
            dialog = adb.create();
            dialog.show();
        });

        refClient();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        init();

        btnSignup.setOnClickListener(v -> {
            if (AppManager.isOnline(Signup.this)) {
                if (isValid()) {
                    String url = Constants.URL.BASE_URL + "api/android/auth/user/register";
                    networkCallUsingVolleyApi(param(), url);
                }
            } else {
                Toast.makeText((Context) Signup.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayList<String> slugList = new ArrayList<>();
        slugList.add("Select Slug");
        slugList.add("retailer");
        slugList.add("md");
        slugList.add("distributor");
        slugList.add("whitelable");

        ArrayAdapter stateAdapter = new ArrayAdapter<String>((Context) this, android.R.layout.simple_spinner_item, slugList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnrSlug.setAdapter(stateAdapter);

        spnrSlug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    slug = slugList.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void networkCallUsingVolleyApi(Map<String, String> map, String url) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), true).netWorkCall();
        } else {
            Toast.makeText((Context) this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    public Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("name", etName.getText().toString());
        map.put("mobile", etMobile.getText().toString());
        map.put("email", etEmail.getText().toString());
        map.put("shopname", etShop.getText().toString());
        map.put("pancard", etPan.getText().toString());
        map.put("aadharcard", etAadhar.getText().toString());
        map.put("state", etState.getText().toString());
        map.put("city", etCity.getText().toString());
        map.put("address", etAddress.getText().toString());
        map.put("pincode", etPincode.getText().toString());
        map.put("slug", slug);
        if (MyUtil.isNN(etReferal.getText().toString()))
            map.put("referal", etReferal.getText().toString());
        map.put("gender", genderType);
        map.put("dob", etDob.getText().toString());
        return map;
    }

    private boolean isValid() {

        if (etName.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPincode.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "Pin is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etPincode.getText().toString().length() != 6) {
            Toast.makeText((Context) this, "Pin length should be 6", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etMobile.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "Mobile number is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etMobile.getText().toString().length() != 10) {
            Toast.makeText((Context) this, "Invalid contact", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etEmail.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etShop.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "Shop field is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPan.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "Pancard number is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAadhar.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "Aadhar number is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etAadhar.getText().toString().length() != 12) {
            Toast.makeText((Context) this, "Value should be 12 digits long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etState.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "State is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etCity.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "City is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAddress.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "Address is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etDob.getText().toString().length() == 0) {
            Toast.makeText((Context) this, "DOB is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (slug.length() == 0) {
            Toast.makeText((Context) this, "Slug value is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P(JSonResponse);
        //{"statuscode":"TXN","message":"Thank you for choosing, your request is successfully submitted for approval"}
        JSONObject jsonObject = null;
        try {
            String msg = "some error accour";
            jsonObject = new JSONObject(JSonResponse);
            String status;
            if (jsonObject.has("status")) {
                status = jsonObject.getString("status");
            } else {
                status = jsonObject.getString("statuscode");
            }
            if (jsonObject.has("message"))
                msg = jsonObject.getString("message");

            if (status.equalsIgnoreCase("TXN")) {
                confirmPopup(msg);
            } else {
                Toast.makeText(context, "Error : " + msg, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Print.P("Json Parser Exception");
        }
    }

    private void confirmPopup(String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onFailRequest(String msg) {
        ExtensionFunction.showToast(this, msg);
    }

    private void refClient() {
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            response = referrerClient.getInstallReferrer();
                            String referrerUrl = response.getInstallReferrer();
                            Print.P("Referral Url : " + referrerUrl);
                            parseValue(referrerUrl);
                        } catch (RemoteException e) {

                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
            }
        });
    }

    private void parseValue(String referralString) {
        //referralString = "utm_source=google&utm_medium=cpc&utm_campaign=IYDA983";
        Print.P("--> " + referralString);
        try {
            if (MyUtil.isNN(referralString) && referralString.contains("utm_campaign")) {
                String[] stringsArray = referralString.split("&");
                for (String refCode : stringsArray) {
                    if (refCode.contains("utm_campaign")) {
                        String[] refArray = refCode.split("=");
                        String regCode = refArray[1];
                        if (MyUtil.isNN(regCode)) {
                            etReferal.setText("" + regCode);
                            etReferal.setEnabled(false);
                        }else{
                            etReferal.setEnabled(true);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            etReferal.setVisibility(View.VISIBLE);
        }
    }
}
