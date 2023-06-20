package com.payment.aeps.util;

public class DeviceDataModel {
    private String errCode;
    private String errMsg;
    private String fingerData;
    private String dc;
    private String dpId;
    private String mc;
    private String mi;
    private String rdsId;
    private String rdsVer;
    private String srno;
    private String sysid;
    private String ts;
    private String Hmac;
    private String ci;
    private String Skey;
    private String nmPoints;

    public DeviceDataModel() {
    }

    public void setNmPoints(String nmPoints) {
        this.nmPoints = nmPoints;
    }

    public String getNmPoints() {
        return this.nmPoints;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getFingerData() {
        return this.fingerData;
    }

    public void setFingerData(String fingerData) {
        this.fingerData = fingerData;
    }

    public String getDc() {
        return this.dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getDpId() {
        return this.dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getMc() {
        return this.mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getMi() {
        return this.mi;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }

    public String getRdsId() {
        return this.rdsId;
    }

    public void setRdsId(String rdsId) {
        this.rdsId = rdsId;
    }

    public String getRdsVer() {
        return this.rdsVer;
    }

    public void setRdsVer(String rdsVer) {
        this.rdsVer = rdsVer;
    }

    public String getSrno() {
        return this.srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getSysid() {
        return this.sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getTs() {
        return this.ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getHmac() {
        return this.Hmac;
    }

    public void setHmac(String hmac) {
        this.Hmac = hmac;
    }

    public String getCi() {
        return this.ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getSkey() {
        return this.Skey;
    }

    public void setSkey(String skey) {
        this.Skey = skey;
    }

    @Override
    public String toString() {
        return "DeviceDataModel{" +
                "errCode='" + errCode + '\'' +
                "\n, errMsg='" + errMsg + '\'' +
                "\n, fingerData='" + fingerData + '\'' +
                "\n, dc='" + dc + '\'' +
                "\n, dpId='" + dpId + '\'' +
                "\n, mc='" + mc + '\'' +
                "\n, mi='" + mi + '\'' +
                "\n, rdsId='" + rdsId + '\'' +
                "\n, rdsVer='" + rdsVer + '\'' +
                "\n, srno='" + srno + '\'' +
                "\n, sysid='" + sysid + '\'' +
                "\n, ts='" + ts + '\'' +
                "\n, Hmac='" + Hmac + '\'' +
                "\n, ci='" + ci + '\'' +
                "\n, Skey='" + Skey + '\'' +
                "\n, nmPoints='" + nmPoints + '\'' +
                '}';
    }
}
