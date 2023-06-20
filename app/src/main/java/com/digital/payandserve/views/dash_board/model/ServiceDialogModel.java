package com.digital.payandserve.views.dash_board.model;

public class ServiceDialogModel {

    private String title;
    private int imgIc;
    private Boolean selected = false;

    public ServiceDialogModel(String title, int imgIc, Boolean selected) {
        this.title = title;
        this.imgIc = imgIc;
        this.selected = selected;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgIc() {
        return imgIc;
    }

    public void setImgIc(int imgIc) {
        this.imgIc = imgIc;
    }
}
