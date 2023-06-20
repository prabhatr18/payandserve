package com.digital.payandserve.views.dmt;


import android.os.Parcel;
import android.os.Parcelable;

public class LocalInvoiceModel implements Parcelable {
    public static final Creator<LocalInvoiceModel> CREATOR = new Creator<LocalInvoiceModel>() {
        @Override
        public LocalInvoiceModel createFromParcel(Parcel in) {
            return new LocalInvoiceModel(in);
        }

        @Override
        public LocalInvoiceModel[] newArray(int size) {
            return new LocalInvoiceModel[size];
        }
    };
    private String rrn;
    private String message;
    private String status;
    private String amount;
    private String orderId;
    private String utr;

    public LocalInvoiceModel(String rrn, String message, String status, String amount) {
        this.rrn = rrn;
        this.message = message;
        this.status = status;
        this.amount = amount;
    }

    public LocalInvoiceModel(String message, String status, String amount, String orderId, String utr) {
        this.message = message;
        this.status = status;
        this.amount = amount;
        this.orderId = orderId;
        this.utr = utr;
    }

    protected LocalInvoiceModel(Parcel in) {
        rrn = in.readString();
        message = in.readString();
        status = in.readString();
        amount = in.readString();
        orderId = in.readString();
        utr = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rrn);
        dest.writeString(message);
        dest.writeString(status);
        dest.writeString(amount);
        dest.writeString(orderId);
        dest.writeString(utr);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRrn() {
        return rrn;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getAmount() {
        return amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUtr() {
        return utr;
    }
}