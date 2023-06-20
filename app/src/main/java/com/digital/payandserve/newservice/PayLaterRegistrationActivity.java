package com.digital.payandserve.newservice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.select_state_district.SearchWithListActivity;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PayLaterRegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, RequestResponseLis {

    EditText fNameEt, mNameEt, emailEt, mobileNumberEt, lNameEt;
    TextView dobTv, stateTv, districtTv, addressEt;
    Button nextBtn;
    Spinner genderSpinner;
    ImageView onBoardBackBtn;

    String loanId, token, userId;
    String[] selectGender = {"Please Select...", "Male", "Female"};
    String gender, stateId, stateName, districtId, mobileNumber;
    Calendar myCalendar;
    private SimpleDateFormat simpleDateFormat;

    private void init() {

        fNameEt = (EditText) findViewById(R.id.fNameEt);
        mNameEt = (EditText) findViewById(R.id.mNameEt);
        lNameEt = (EditText) findViewById(R.id.lNameEt);
        emailEt = (EditText) findViewById(R.id.emailEt);
        mobileNumberEt = (EditText) findViewById(R.id.mobileNumberEt);
        dobTv = (TextView) findViewById(R.id.dobTv);
        stateTv = (TextView) findViewById(R.id.stateTv);
        districtTv = (TextView) findViewById(R.id.districtTv);
        addressEt = (TextView) findViewById(R.id.addressEt);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        onBoardBackBtn = (ImageView) findViewById(R.id.onBoardBackBtn);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        init();

        token = SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN);
        userId = SharedPrefs.getValue(this, SharedPrefs.USER_ID);

        Log.d("onBoard ->", "token :" + token + "," + "userId:" + userId);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>((Context) this, android.R.layout.simple_spinner_item, selectGender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);
        int spinnerPosition = adapter.getPosition("Please Select...");
        genderSpinner.setSelection(spinnerPosition);

        stateTv.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Intent intent = new Intent((Context) this, SearchWithListActivity.class);
            bundle.putString("type", "state");
            intent.putExtras(bundle);
            startActivityForResult(intent, 100);
        });

        districtTv.setOnClickListener(v -> {
            if (stateTv.getText().toString().trim().isEmpty()) {
                ExtensionFunction.showToast(this, "Select state first..!");
            } else {
                Bundle bundle = new Bundle();
                Intent intent = new Intent((Context) this, SearchWithListActivity.class);
                bundle.putString("type", "district");
                bundle.putString("stateId", stateId);
                intent.putExtras(bundle);
                startActivityForResult(intent, 101);
            }
        });

        dobTv.setOnClickListener(v -> {
            new SpinnerDatePickerDialogBuilder()
                    .context((Context) PayLaterRegistrationActivity.this)
                    .callback(this)
                    .spinnerTheme(R.style.NumberPickerStyle)
                    .showTitle(true)

                    .showDaySpinner(true)
                    .defaultDate(2017, 0, 1)
                    .maxDate(2100, 0, 1)
                    .minDate(1800, 0, 1)
                    .build()
                    .show();
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppManager.isOnline(PayLaterRegistrationActivity.this)) {
                    if (isValid()) {
                        String url = Constants.URL.BASE_URL + "api/android/paylater/useronboard";
                        networkCallUsingVolleyApi(param(), url);
                    }
                } else {
                    Toast.makeText((Context) PayLaterRegistrationActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        onBoardBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("OnBoard --:" + JSonResponse);

        JSONObject jsonObject = null;
        try {
            String msg = "some error accor";
            jsonObject = new JSONObject(JSonResponse);
            String status = jsonObject.getString("statuscode");

            if (jsonObject.has("message"))
                msg = jsonObject.getString("message");

            if (status.equalsIgnoreCase("TXN")) {
                ExtensionFunction.showToast(this, msg);

                Intent intent = new Intent((Context) this, GetPlanActivity.class);
                intent.putExtra("mobileNumber", ""+mobileNumber);
                startActivity(intent);

                Log.d("OnBoard Param if :", "" + msg);

            } else {
                Log.d("OnBoard Param :", "" + msg);
                Toast.makeText((Context) this, "Error : " + msg, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Print.P("Json Parser Exception");
        }

    }

    @Override
    public void onFailRequest(String msg) {
        ExtensionFunction.showToast(this, msg);
    }

    public Map<String, String> param() {

        mobileNumber = mobileNumberEt.getText().toString();

        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("mobile", mobileNumber);
        map.put("firstName", fNameEt.getText().toString());
        map.put("middleName", mNameEt.getText().toString());
        map.put("lastName", lNameEt.getText().toString());
        map.put("Email", emailEt.getText().toString());
        map.put("state", stateTv.getText().toString());
        map.put("city", districtTv.getText().toString());
        map.put("line1", addressEt.getText().toString());
        map.put("apptoken", token);
        map.put("gender", gender);
        map.put("birthday", dobTv.getText().toString());
        Log.d("OnBoard Param :", "" + map.toString());

        return map;
    }

    private boolean isValid() {

        String email = emailEt.getText().toString().trim();

        if (fNameEt.getText().toString().trim().equals("")) {
            ExtensionFunction.checkValidation(fNameEt, "First Name Required");
            return false;

        } else if (gender.equals("") || gender.equals("Please Select...")) {
            ExtensionFunction.showToast(this, "Select Gender..!");
            return false;

        } else if (email.isEmpty() || (!email.contains("@") && !email.contains("."))) {
            ExtensionFunction.checkValidation(emailEt, "Email Required");
            return false;
        } else if (mobileNumberEt.getText().toString().trim().equals("")) {
            ExtensionFunction.checkValidation(mobileNumberEt, "Mobile Number Required");
            return false;

        } else if (dobTv.getText().toString().trim().equals("")) {
            ExtensionFunction.showToast(this, "Select DOB..!");
            return false;

        } else if (stateTv.getText().toString().trim().equals("")) {
            ExtensionFunction.showToast(this, "Select State..!");
            return false;

        } else if (districtTv.getText().toString().trim().equals("")) {
            ExtensionFunction.showToast(this, "Select District..!");
            return false;

        } else if (addressEt.getText().toString().trim().equals("")) {
            ExtensionFunction.showToast(this, "Address Required");
            return false;

        }else if (mobileNumberEt.getText().toString().length() != 10) {
            Toast.makeText((Context) this, "Invalid contact", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // on below line we are displaying toast message for selected item
        // Toast.makeText((Context) this, "" + selectGender[position] + " Selected..", Toast.LENGTH_SHORT).show();
        gender = selectGender[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 100) {
                stateName = data.getStringExtra("stateName");
                stateId = data.getStringExtra("stateId");
                stateTv.setText(stateName);
                ExtensionFunction.showToast(this, stateId);
            } else if (requestCode == 101) {
                String districtName = data.getStringExtra("districtname");
                districtId = data.getStringExtra("districtId");
                districtTv.setText(districtName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.e("dte", "" + year + "   " + monthOfYear + "   " + dayOfMonth);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        myCalendar = Calendar.getInstance();
        myCalendar.set(year, monthOfYear, dayOfMonth);
        dobTv.setText(simpleDateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}