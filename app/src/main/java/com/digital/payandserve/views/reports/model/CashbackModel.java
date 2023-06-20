package com.digital.payandserve.views.reports.model;

public class CashbackModel {
    private String id;
    private String service;
    private String type;
    private String value;
    private String cashbacktype;
    private String cashbackvalue;
    private String validfor;
    private String wallet;
    private String created_at;
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCashbacktype() {
        return cashbacktype;
    }

    public void setCashbacktype(String cashbacktype) {
        this.cashbacktype = cashbacktype;
    }

    public String getCashbackvalue() {
        return cashbackvalue;
    }

    public void setCashbackvalue(String cashbackvalue) {
        this.cashbackvalue = cashbackvalue;
    }

    public String getValidfor() {
        return validfor;
    }

    public void setValidfor(String validfor) {
        this.validfor = validfor;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
