package com.digital.payandserve.views.mhagram_aeps_matm.kyc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.mhagram_aeps_matm.model.KYCModel;

import java.util.Map;

public class KYCActivityFormTwo extends AppCompatActivity implements RequestResponseLis {
    private EditText etLandmark, etMohhalla, etLocation, etPincode, etPan, etShopName, etShopType,
            etQualification, etPopulation, etLocationType;
    private KYCModel model;
    private Button btnNext, btnBack;

    private void init() {
        model = getIntent().getParcelableExtra("data");
        etLandmark = findViewById(R.id.etLandmark);
        etMohhalla = findViewById(R.id.etMohhalla);
        etLocation = findViewById(R.id.etLocation);
        etPincode = findViewById(R.id.etPincode);
        etPan = findViewById(R.id.etPan);
        etShopName = findViewById(R.id.etShopName);
        etShopType = findViewById(R.id.etShopType);
        etQualification = findViewById(R.id.etQualification);
        etPopulation = findViewById(R.id.etPopulation);
        etLocationType = findViewById(R.id.etLocationType);
        btnNext = findViewById(R.id.btnProceed);
        btnBack = findViewById(R.id.btnBack);
        setInit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps_kyc_page_2);
        init();

        btnNext.setOnClickListener(v -> {
            setFormData();
            if (isValid()) {
                networkCallUsingVolleyApi(Constants.URL.AEPS_REGISTER, true);
            }
        });

        etQualification.setFocusable(false);
        etPopulation.setFocusable(false);
        etLocationType.setFocusable(false);
        etQualification.setOnClickListener(v -> modePopup(1));
        etPopulation.setOnClickListener(v -> modePopup(2));
        etLocationType.setOnClickListener(v -> modePopup(3));

        btnBack.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("data", model);
            setResult(5000, returnIntent);
            finish();
        });
    }

    private void setInit() {
        if (MyUtil.isNN(model.getLandmark())) etLandmark.setText(model.getLastName());
        if (MyUtil.isNN(model.getMohhalla())) etMohhalla.setText(model.getMohhalla());
        if (MyUtil.isNN(model.getLocation())) etLocation.setText(model.getLocation());
        if (MyUtil.isNN(model.getPincode())) etPincode.setText(model.getPincode());
        if (MyUtil.isNN(model.getPan())) etPan.setText(model.getPan());
        if (MyUtil.isNN(model.getShopName())) etShopName.setText(model.getShopName());
        if (MyUtil.isNN(model.getShopType())) etShopType.setText(model.getShopType());
        if (MyUtil.isNN(model.getQualification()))
            etQualification.setText(model.getQualification());
        if (MyUtil.isNN(model.getPopulation())) etPopulation.setText(model.getPopulation());
        if (MyUtil.isNN(model.getLocationType())) etLocationType.setText(model.getLocationType());
    }

    private void setFormData() {
        if (MyUtil.isNN_ET(etLandmark)) model.setLandmark(etLandmark.getText().toString());
        if (MyUtil.isNN_ET(etMohhalla)) model.setMohhalla(etMohhalla.getText().toString());
        if (MyUtil.isNN_ET(etLocation)) model.setLocation(etLocation.getText().toString());
        if (MyUtil.isNN_ET(etPincode)) model.setPincode(etPincode.getText().toString());
        if (MyUtil.isNN_ET(etPan)) model.setPan(etPan.getText().toString());
        if (MyUtil.isNN_ET(etShopName)) model.setShopName(etShopName.getText().toString());
        if (MyUtil.isNN_ET(etShopType)) model.setShopType(etShopType.getText().toString());
        if (MyUtil.isNN_ET(etQualification))
            model.setQualification(etQualification.getText().toString());
        if (MyUtil.isNN_ET(etPopulation)) model.setPopulation(etPopulation.getText().toString());
        if (MyUtil.isNN_ET(etLocationType))
            model.setLocationType(etLocationType.getText().toString());
    }

    private boolean isValid() {
        if (!MyUtil.isNN(model.getFirstName())) {
            Toast.makeText(this, "First Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getLastName())) {
            Toast.makeText(this, "Last Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getEmail())) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getPhoneOne())) {
            Toast.makeText(this, "Phone number 1 is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getPhoneTwo())) {
            Toast.makeText(this, "Phone number 2 is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getDOB())) {
            Toast.makeText(this, "date of birth is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getState())) {
            Toast.makeText(this, "State is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getDistrict())) {
            Toast.makeText(this, "District is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getAddress())) {
            Toast.makeText(this, "address is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getCity())) {
            Toast.makeText(this, "City Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getBlock())) {
            Toast.makeText(this, "Block Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!MyUtil.isNN(model.getLandmark())) {
            Toast.makeText(this, "Land mark is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getMohhalla())) {
            Toast.makeText(this, "Mohhalla is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getLocation())) {
            Toast.makeText(this, "Location is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getPincode())) {
            Toast.makeText(this, "Pin code is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getPan())) {
            Toast.makeText(this, "pan number is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getShopName())) {
            Toast.makeText(this, "shop name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getShopType())) {
            Toast.makeText(this, "shop type is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!MyUtil.isNN(model.getQualification())) {
            Toast.makeText(this, "qualification field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getPopulation())) {
            Toast.makeText(this, "population is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(model.getLocationType())) {
            Toast.makeText(this, "location type is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void modePopup(int popupType) {
        String[] qualificationData = {"SSC", "HSC", "Graduate", "Post Graduate", "Diploma"};
        String[] populationData = {"0 to 2000", "2000 to 5000", "5000 to 10000", "10000 to 50000", "50000 to 100000"};
        String[] locationData = {"Rural", "Urban", "Metro Semi Urban"};
        String title = "";
        String[] array = null;
        switch (popupType) {
            case 1:
                title = "Select Qualification";
                array = qualificationData;
                break;
            case 2:
                title = "Select Population";
                array = populationData;
                break;
            case 3:
                title = "Select Location Type";
                array = locationData;
                break;
        }

        AlertDialog.Builder alert = new MaterialAlertDialogBuilder(this,
                R.style.ThemeOverlay_App_MaterialAlertDialog);
        alert.setTitle(title);
        alert.setSingleChoiceItems(array, -1, (dialog, which) -> {
            switch (popupType) {
                case 1:
                    etQualification.setText(qualificationData[which]);
                    break;
                case 2:
                    etPopulation.setText(populationData[which]);
                    break;
                case 3:
                    etLocationType.setText(locationData[which]);
                    break;
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
        return model.getParam();
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }
}
