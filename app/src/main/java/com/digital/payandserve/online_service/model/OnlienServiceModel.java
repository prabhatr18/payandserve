package com.digital.payandserve.online_service.model;

import android.graphics.drawable.Drawable;

public class OnlienServiceModel {

    private Drawable icon;
    private String title;
    private String msg;
    private String type;
    private Boolean openInExternalBrowser;

    public OnlienServiceModel(Drawable icon, String title, String msg, String type, Boolean openInExternalBrowser) {
        this.icon = icon;
        this.title = title;
        this.msg = msg;
        this.type = type;
        this.openInExternalBrowser = openInExternalBrowser;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getOpenInExternalBrowser() {
        return openInExternalBrowser;
    }

    public void setOpenInExternalBrowser(Boolean openInExternalBrowser) {
        this.openInExternalBrowser = openInExternalBrowser;
    }
}
