package com.digital.payandserve.views.operator.model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class OperatorModel {

    @SerializedName("name")
    private String name;

    @SerializedName("logo")
    private String logo;

    @SerializedName("id")
    private String id;

    @SerializedName("ifsc")
    private String masterifsc;

    @SerializedName("url")
    private String url;

    private String bankid;
    private String bankcode;
   // private String masterifsc;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getId() {
        return id;
    }

    public static Comparator<OperatorModel> comparator = (s1, s2) -> {
        String name1 = s1.getName().toUpperCase();
        String name2 = s2.getName().toUpperCase();
        return name1.compareTo(name2);
    };

    public OperatorModel(String name, String logo, String id, String bankid, String bankcode, String masterifsc) {
        this.name = name;
        this.logo = logo;
        this.id = id;
        this.bankid = bankid;
        this.bankcode = bankcode;
        this.masterifsc = masterifsc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getMasterifsc() {
        return masterifsc;
    }

    public void setMasterifsc(String masterifsc) {
        this.masterifsc = masterifsc;
    }

    public static Comparator<OperatorModel> getComparator() {
        return comparator;
    }

    public static void setComparator(Comparator<OperatorModel> comparator) {
        OperatorModel.comparator = comparator;
    }
}