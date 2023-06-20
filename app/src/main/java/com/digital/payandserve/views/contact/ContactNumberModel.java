package com.digital.payandserve.views.contact;

import java.util.Comparator;

public class ContactNumberModel {
    public static Comparator<ContactNumberModel> ContactNameComparator = (s1, s2) -> {
        String StudentName1 = s1.getUserName().toUpperCase();
        String StudentName2 = s2.getUserName().toUpperCase();
        return StudentName1.compareTo(StudentName2);
    };
    private String userName;
    private String contact;
    private int img;

    public ContactNumberModel(String userName, String contact, int img) {
        this.userName = userName;
        this.contact = contact;
        this.img = img;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

}
