package com.digital.payandserve.views.tutorial.model;

public class TutorialModel {
    private String id;
    private String title;
    private String description;
    private String value;

    public TutorialModel(String id, String title, String description, String value) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.value = value;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
