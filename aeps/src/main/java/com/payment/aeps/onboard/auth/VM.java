package com.payment.aeps.onboard.auth;

import android.content.Context;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.Toast;

import com.payment.aeps.util.AadharNumberPattern;
import com.payment.aeps.util.ModuleUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VM {
    // Generic validation constant;
    public static final String AADHAAR = "aadhaar";
    public static final String PANCARD = "pancard";
    public static final String EMAIL = "email";

    private EditText editText;
    private String fieldName; // Static Name of Field for toast
    private String fieldValue; // String variable value to validate
    private String specialField; // Some special validation String
    private int length = 0;

    public VM(EditText editText, String fieldName, int length) {
        this.editText = editText;
        this.length = length;
        this.fieldName = fieldName;
    }

    public VM(EditText editText, String fieldName, int length, String specialField) {
        this.editText = editText;
        this.length = length;
        this.fieldName = fieldName;
        this.specialField = specialField;
    }

    public VM(EditText editText, String fieldName) {
        this.editText = editText;
        this.fieldName = fieldName;
    }

    public VM(EditText editText, String fieldName, String specialField) {
        this.editText = editText;
        this.fieldName = fieldName;
        this.specialField = specialField;
    }

    public VM(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public static boolean isValidUtil(ArrayList<VM> validationList, Context context) {
        boolean flag = true;
        for (VM m : validationList) {
            EditText editText = m.getEditText();
            int size = m.getLength();
            if (editText.getText().toString().trim().length() == 0) {
                flag = false;
                editText.setError("Required Field");
                Toast.makeText(context, "" + m.getFieldName() + " is required", Toast.LENGTH_SHORT).show();
                break;
            }

            if (size > 0 && editText.getText().toString().trim().length() != size) {
                flag = false;
                editText.setError("Invalid Input");
                Toast.makeText(context, "Please input valid " + m.getFieldName(), Toast.LENGTH_SHORT).show();
                break;
            }

            if (ModuleUtil.isNotNull(m.getSpecialField())) {
                if (m.getSpecialField().equals(AADHAAR)) {
                    if (!validateAadharNumber(editText.getText().toString())) {
                        flag = false;
                        editText.setError("Invalid Aadhaar Number");
                        Toast.makeText(context, "Please input valid " + m.getFieldName(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else if (m.getSpecialField().equals(PANCARD)) {
                    if (!isValidPanCardNo(editText.getText().toString())) {
                        flag = false;
                        editText.setError("Invalid Pan Number");
                        Toast.makeText(context, "Please input valid " + m.getFieldName(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else if (m.getSpecialField().equals(EMAIL)) {
                    if (!m.isValidEmail(editText.getText().toString())) {
                        flag = false;
                        editText.setError("Invalid Email");
                        Toast.makeText(context, "Please input valid " + m.getFieldName(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
        return flag;
    }

    public static boolean isValidStringUtil(ArrayList<VM> validationList, Context context) {
        boolean flag = true;
        for (VM m : validationList) {
            if (m.getFieldValue() == null || m.getFieldValue().trim().length() == 0) {
                flag = false;
                Toast.makeText(context, "" + m.getFieldName() + " is required", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return flag;
    }

    public static boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = AadharNumberPattern.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    private static boolean isValidPanCardNo(String panCardNo) {
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        Pattern p = Pattern.compile(regex);
        if (panCardNo == null) {
            return false;
        }
        Matcher m = p.matcher(panCardNo);
        return m.matches();
    }

    public static void setInputLimit(EditText et, int limit) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(limit);
        et.setFilters(filterArray);
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public String getSpecialField() {
        return specialField;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.trim().matches(emailPattern);
    }
}
