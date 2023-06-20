package com.digital.payandserve.views.invoice.model;

import java.util.List;

public class ReportPDFModel {
    private String[] keys;
    private List<List<InvoiceModel>> valueModels;

    public ReportPDFModel(String[] keys, List<List<InvoiceModel>> valueModels) {
        this.keys = keys;
        this.valueModels = valueModels;
    }

    public String[] getKeys() {
        return keys;
    }


    public List<List<InvoiceModel>> getValueModels() {
        return valueModels;
    }

}
