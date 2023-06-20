package com.digital.payandserve.views.dmt;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BenedatumPgPayout implements android.os.Parcelable {

    @SerializedName("beneid")
    @Expose
    private String beneId;
    @SerializedName("benebankid")
    @Expose
    private String bankid;
    @SerializedName("benebank")
    @Expose
    private String bankname;
    @SerializedName("benename")
    @Expose
    private String name;
    @SerializedName("beneaccount")
    @Expose
    private String accno;
    @SerializedName("beneifsc")
    @Expose
    private String ifsc;
    @SerializedName("verified")
    @Expose
    private String verified;
//    @SerializedName("banktype")
//    @Expose
//    private String banktype;
//    @SerializedName("paytm")
//    @Expose
//    private Boolean paytm;

    protected BenedatumPgPayout(Parcel in) {
        beneId = in.readString();
        bankid = in.readString();
        bankname = in.readString();
        name = in.readString();
        accno = in.readString();
        ifsc = in.readString();
        verified = in.readString();
//        banktype = in.readString();
//        byte tmpPaytm = in.readByte();
//        paytm = tmpPaytm == 0 ? null : tmpPaytm == 1;
    }

    public static final Creator<BenedatumPgPayout> CREATOR = new Creator<BenedatumPgPayout>() {
        @Override
        public BenedatumPgPayout createFromParcel(Parcel in) {
            return new BenedatumPgPayout(in);
        }

        @Override
        public BenedatumPgPayout[] newArray(int size) {
            return new BenedatumPgPayout[size];
        }
    };

    public String getBeneId() {
        return beneId;
    }

    public void setBeneId(String beneId) {
        this.beneId = beneId;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

//    public String getBanktype() {
//        return banktype;
//    }
//
//    public void setBanktype(String banktype) {
//        this.banktype = banktype;
//    }
//
//    public Boolean getPaytm() {
//        return paytm;
//    }
//
//    public void setPaytm(Boolean paytm) {
//        this.paytm = paytm;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//            dest.writeString(benemobile);
        dest.writeString(bankid);
        dest.writeString(bankname);
        dest.writeString(name);
        dest.writeString(accno);
        dest.writeString(ifsc);
        dest.writeString(verified);
//        dest.writeString(banktype);
    }
}
