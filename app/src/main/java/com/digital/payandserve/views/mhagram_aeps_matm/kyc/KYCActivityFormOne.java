package com.digital.payandserve.views.mhagram_aeps_matm.kyc;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.views.mhagram_aeps_matm.model.KYCModel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class KYCActivityFormOne extends AppCompatActivity {
    private EditText etFirstName, etLastName, etEmail, etPhoneOne, etPhoneTwo, etDOB, etState,
            etDistrict, etAddress, etCity, etBlock;
    private KYCModel model;
    private Button btnNext;

    private void init() {
        model = new KYCModel();
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneOne = findViewById(R.id.etPhoneOne);
        etPhoneTwo = findViewById(R.id.etPhoneTwo);
        etDOB = findViewById(R.id.etDOB);
        etState = findViewById(R.id.etState);
        etDistrict = findViewById(R.id.etDistrict);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etBlock = findViewById(R.id.etBlock);
        btnNext = findViewById(R.id.btnProceed);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps_kyc);
        init();

        etDOB.setFocusable(false);
        etDOB.setOnClickListener(arg0 -> {
            final AlertDialog.Builder adb = new AlertDialog.Builder(this);
            final View view = LayoutInflater.from(this).inflate(R.layout.date_picker, null);
            adb.setView(view);
            final Dialog dialog;
            adb.setPositiveButton("OK",
                    (dialog1, arg1) -> {
                        DatePicker etDatePicker = view.findViewById(R.id.datePicker1);
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.set(etDatePicker.getYear(), etDatePicker.getMonth(), etDatePicker.getDayOfMonth());
                        Date date = null;
                        date = cal.getTime();
                        String approxDateString = Constants.COMMON_DATE_FORMAT.format(date);
                        etDOB.setText(approxDateString);
                    });
            dialog = adb.create();
            dialog.show();
        });

        btnNext.setOnClickListener(v -> {
            setFormData();
            Intent i = new Intent(KYCActivityFormOne.this, KYCActivityFormTwo.class);
            i.putExtra("data", model);
            startActivityForResult(i, 5000);
        });
    }

    private void setInit() {
        if (MyUtil.isNN(model.getFirstName())) etFirstName.setText(model.getFirstName());
        if (MyUtil.isNN(model.getLastName())) etLastName.setText(model.getLastName());
        if (MyUtil.isNN(model.getEmail())) etEmail.setText(model.getEmail());
        if (MyUtil.isNN(model.getPhoneOne())) etPhoneOne.setText(model.getPhoneOne());
        if (MyUtil.isNN(model.getPhoneTwo())) etPhoneTwo.setText(model.getPhoneTwo());
        if (MyUtil.isNN(model.getDOB())) etDOB.setText(model.getDOB());
        if (MyUtil.isNN(model.getState())) etState.setText(model.getState());
        if (MyUtil.isNN(model.getDistrict())) etDistrict.setText(model.getDistrict());
        if (MyUtil.isNN(model.getAddress())) etAddress.setText(model.getAddress());
        if (MyUtil.isNN(model.getCity())) etCity.setText(model.getCity());
        if (MyUtil.isNN(model.getBlock())) etBlock.setText(model.getBlock());
    }

    private void setFormData() {
        if (MyUtil.isNN_ET(etFirstName)) model.setFirstName(etFirstName.getText().toString());
        if (MyUtil.isNN_ET(etLastName)) model.setLastName(etLastName.getText().toString());
        if (MyUtil.isNN_ET(etEmail)) model.setEmail(etEmail.getText().toString());
        if (MyUtil.isNN_ET(etPhoneOne)) model.setPhoneOne(etPhoneOne.getText().toString());
        if (MyUtil.isNN_ET(etPhoneTwo)) model.setPhoneTwo(etPhoneTwo.getText().toString());
        if (MyUtil.isNN_ET(etDOB)) model.setDOB(etDOB.getText().toString());
        if (MyUtil.isNN_ET(etState)) model.setState(etState.getText().toString());
        if (MyUtil.isNN_ET(etDistrict)) model.setDistrict(etDistrict.getText().toString());
        if (MyUtil.isNN_ET(etAddress)) model.setAddress(etAddress.getText().toString());
        if (MyUtil.isNN_ET(etCity)) model.setCity(etCity.getText().toString());
        if (MyUtil.isNN_ET(etBlock)) model.setBlock(etBlock.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            setInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 5000:
                    model  = data.getParcelableExtra("data");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
