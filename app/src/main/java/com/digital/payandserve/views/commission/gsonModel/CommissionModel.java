package com.digital.payandserve.views.commission.gsonModel;

import com.google.gson.annotations.SerializedName;

public class CommissionModel {
    @SerializedName("provider")
    private Provider provider;
    private String  keys;

    public Provider getProvider() {
        return provider;
    }

    @SerializedName("retailer")
    private String retailer;

    @SerializedName("md")
    private String md;

    @SerializedName("whitelable")
    private String whitelable;

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("distributor")
    private String distributor;

    @SerializedName("slab")
    private String slab;

    @SerializedName("scheme_id")
    private String schemeId;

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getRetailer(){
        return retailer;
    }

    public String getMd(){
        return md;
    }

    public String getWhitelable(){
        return whitelable;
    }

    public String getId(){
        return id;
    }

    public String getType(){
        return type;
    }

    public String getDistributor(){
        return distributor;
    }

    public String getSlab(){
        return slab;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }
}