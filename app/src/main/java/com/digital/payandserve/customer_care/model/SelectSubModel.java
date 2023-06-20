package com.digital.payandserve.customer_care.model;

import com.digital.payandserve.views.select_state_district.state_model.StateModel;

import java.util.Comparator;

public class SelectSubModel {
    private String subject;

    public static Comparator<SelectSubModel> comparator = (s1, s2) -> {
        String name1 = s1.getSubject().toUpperCase();
        String name2 = s2.getSubject().toUpperCase();
        return name1.compareTo(name2);
    };

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static Comparator<SelectSubModel> getComparator() {
        return comparator;
    }

    public static void setComparator(Comparator<StateModel> comparator) {
        StateModel.comparator = comparator;
    }
}
