package com.digital.payandserve.views.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdharPayStatmentModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("aadhar")
    @Expose
    private String aadhar;
    @SerializedName("txnid")
    @Expose
    private String txnid;
    @SerializedName("api_id")
    @Expose
    private String apiId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("charge")
    @Expose
    private String charge;
    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("payid")
    @Expose
    private String payid;
    @SerializedName("refno")
    @Expose
    private String refno;
    @SerializedName("mytxnid")
    @Expose
    private String mytxnid;
    @SerializedName("terminalid")
    @Expose
    private String terminalid;
    @SerializedName("authcode")
    @Expose
    private String authcode;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("rtype")
    @Expose
    private String rtype;
    @SerializedName("transtype")
    @Expose
    private String transtype;
    @SerializedName("aepstype")
    @Expose
    private String aepstype;
    @SerializedName("TxnMedium")
    @Expose
    private String txnMedium;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("credited_by")
    @Expose
    private String creditedBy;
    @SerializedName("via")
    @Expose
    private String via;
    @SerializedName("apitxnid")
    @Expose
    private String apitxnid;
    @SerializedName("provider_id")
    @Expose
    private String providerId;
    @SerializedName("wid")
    @Expose
    private String wid;
    @SerializedName("wprofit")
    @Expose
    private String wprofit;
    @SerializedName("spid")
    @Expose
    private String spid;
    @SerializedName("sprofit")
    @Expose
    private String sprofit;
    @SerializedName("mdid")
    @Expose
    private String mdid;
    @SerializedName("mdprofit")
    @Expose
    private String mdprofit;
    @SerializedName("disid")
    @Expose
    private String disid;
    @SerializedName("disprofit")
    @Expose
    private String disprofit;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("apicode")
    @Expose
    private String apicode;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("providername")
    @Expose
    private String providername;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getMytxnid() {
        return mytxnid;
    }

    public void setMytxnid(String mytxnid) {
        this.mytxnid = mytxnid;
    }

    public String getTerminalid() {
        return terminalid;
    }

    public void setTerminalid(String terminalid) {
        this.terminalid = terminalid;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    public String getTranstype() {
        return transtype;
    }

    public void setTranstype(String transtype) {
        this.transtype = transtype;
    }

    public String getAepstype() {
        return aepstype;
    }

    public void setAepstype(String aepstype) {
        this.aepstype = aepstype;
    }

    public String getTxnMedium() {
        return txnMedium;
    }

    public void setTxnMedium(String txnMedium) {
        this.txnMedium = txnMedium;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreditedBy() {
        return creditedBy;
    }

    public void setCreditedBy(String creditedBy) {
        this.creditedBy = creditedBy;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getApitxnid() {
        return apitxnid;
    }

    public void setApitxnid(String apitxnid) {
        this.apitxnid = apitxnid;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getWprofit() {
        return wprofit;
    }

    public void setWprofit(String wprofit) {
        this.wprofit = wprofit;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getSprofit() {
        return sprofit;
    }

    public void setSprofit(String sprofit) {
        this.sprofit = sprofit;
    }

    public String getMdid() {
        return mdid;
    }

    public void setMdid(String mdid) {
        this.mdid = mdid;
    }

    public String getMdprofit() {
        return mdprofit;
    }

    public void setMdprofit(String mdprofit) {
        this.mdprofit = mdprofit;
    }

    public String getDisid() {
        return disid;
    }

    public void setDisid(String disid) {
        this.disid = disid;
    }

    public String getDisprofit() {
        return disprofit;
    }

    public void setDisprofit(String disprofit) {
        this.disprofit = disprofit;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getApicode() {
        return apicode;
    }

    public void setApicode(String apicode) {
        this.apicode = apicode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProvidername() {
        return providername;
    }

    public void setProvidername(String providername) {
        this.providername = providername;
    }
}
