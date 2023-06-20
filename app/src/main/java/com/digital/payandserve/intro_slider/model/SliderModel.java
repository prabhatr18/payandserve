package com.digital.payandserve.intro_slider.model;

public class SliderModel {
    private String id;
    private String name;
    private String code;
    private String value;
    private String company_id;

    public SliderModel(String id, String name, String code, String value, String company_id) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.value = value;
        this.company_id = company_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getCompany_id() {
        return company_id;
    }
}
