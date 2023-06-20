package com.digital.payandserve.views.member;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMember extends AppCompatActivity implements RequestResponseLis {
    private EditText etName, etEmail, etMobile, etAddress, etShopName, etCity,
            etState, etPincode, etPancard, etAadhaar, etRole;
    private Button btnValidate;
    private String roleId = "";

    private void init() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etAddress = findViewById(R.id.etAddress);
        etShopName = findViewById(R.id.etShopName);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etPincode = findViewById(R.id.etPincode);
        etPancard = findViewById(R.id.etPancard);
        etAadhaar = findViewById(R.id.etAadhaar);
        btnValidate = findViewById(R.id.btnProceed);
        etRole = findViewById(R.id.etRole);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);
        init();
        btnValidate.setOnClickListener(v -> {
            if (isValid()) networkCallUsingVolleyApi(Constants.URL.CREATE_MEMBER, true);
        });

        etRole.setOnClickListener(v -> rolePopup());
    }

    private boolean isEN(EditText et) {
        return et.getText().toString().length() == 0;
    }

    private boolean isValid() {
        if (isEN(etName)) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEN(etEmail) && !MyUtil.isValidEmail(etEmail.getText().toString())) {
            Toast.makeText(this, "Valid Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEN(etMobile) && etMobile.getText().toString().length() != 10) {
            Toast.makeText(this, "Valid Mobile is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEN(etAddress)) {
            Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEN(etShopName)) {
            Toast.makeText(this, "Shop Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEN(etCity)) {
            Toast.makeText(this, "City is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEN(etState)) {
            Toast.makeText(this, "State is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEN(etPincode)) {
            Toast.makeText(this, "Pincode is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEN(etPancard)) {
            Toast.makeText(this, "Pancard is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEN(etAadhaar)) {
            Toast.makeText(this, "Aadhaar is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (roleId.length() == 0) {
            Toast.makeText(this, "Role Selection is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void rolePopup() {
        String slug = SharedPrefs.getValue(this, SharedPrefs.ROLE_SLUG);
        if (!MyUtil.isNN(slug)) return;
        List<CharSequence> roleList = new ArrayList<>();
        roleList.add("Retailer");
        if (slug.equalsIgnoreCase("MD"))
            roleList.add("Distributor");
        CharSequence[] choice = roleList.toArray(new CharSequence[roleList.size()]);
        AlertDialog.Builder alert = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog);
        alert.setTitle("Select Role");
        alert.setSingleChoiceItems(choice, -1, (dialog, which) -> {
            if (choice[which].equals("Retailer")) {
                roleId = "4";
                etRole.setText(choice[which]);
            } else {
                roleId = "3";
                etRole.setText(choice[which]);
            }
        });
        alert.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());
        alert.show();
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
        map.put("name", etName.getText().toString());
        map.put("email", etEmail.getText().toString());
        map.put("mobile", etMobile.getText().toString());
        map.put("role_id", roleId);
        map.put("address", etAddress.getText().toString());
        map.put("shopname", etShopName.getText().toString());
        map.put("city", etCity.getText().toString());
        map.put("state", etState.getText().toString());
        map.put("pincode", etPincode.getText().toString());
        map.put("pancard", etPancard.getText().toString());
        map.put("aadharcard", etAadhaar.getText().toString());
        Print.P("value : " + new JSONObject(map));
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        String message = AppHandler.getMessage(JSonResponse);
        popUp(message);
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }

    private void popUp(String msg) {
        AlertDialog alert;
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this,
                R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Response")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    dialog.dismiss();
                    Constants.IS_RELOAD_REQUEST = true;
                    finish();
                });
        alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }
}
