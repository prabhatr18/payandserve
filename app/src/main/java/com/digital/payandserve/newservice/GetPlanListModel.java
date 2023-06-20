package com.digital.payandserve.newservice;

import java.io.Serializable;

public class GetPlanListModel implements Serializable {

    String id;
    String name;
    String minAmt;
    String maxAmt;
    String interest;
    String emiAmt;
    String emiTotal;
    String processingFee;

    public GetPlanListModel(String id, String name, String minAmt, String maxAmt, String interest, String emiAmt, String emiTotal, String processingFee, String description) {

        this.id = id;
        this.name = name;
        this.minAmt = minAmt;
        this.maxAmt = maxAmt;
        this.interest = interest;
        this.emiAmt = emiAmt;
        this.emiTotal = emiTotal;
        this.processingFee = processingFee;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(String minAmt) {
        this.minAmt = minAmt;
    }

    public String getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(String maxAmt) {
        this.maxAmt = maxAmt;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getEmiAmt() {
        return emiAmt;
    }

    public void setEmiAmt(String emiAmt) {
        this.emiAmt = emiAmt;
    }

    public String getEmiTotal() {
        return emiTotal;
    }

    public void setEmiTotal(String emiTotal) {
        this.emiTotal = emiTotal;
    }

    public String getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(String processingFee) {
        this.processingFee = processingFee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;


}
