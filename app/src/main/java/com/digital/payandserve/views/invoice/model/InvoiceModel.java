package com.digital.payandserve.views.invoice.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

public class InvoiceModel implements Parcelable {
    private String key;
    private String value;

    public InvoiceModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    protected InvoiceModel(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InvoiceModel> CREATOR = new Creator<InvoiceModel>() {
        @Override
        public InvoiceModel createFromParcel(Parcel in) {
            return new InvoiceModel(in);
        }

        @Override
        public InvoiceModel[] newArray(int size) {
            return new InvoiceModel[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}