package com.payment.aeps.objectmodel;

import com.google.gson.annotations.SerializedName;

public class BankModel{

	@SerializedName("iinno")
	private String iinno;

	@SerializedName("bankName")
	private String bankName;

	@SerializedName("details")
	private String details;

	@SerializedName("id")
	private String id;

	@SerializedName("remarks")
	private String remarks;

	@SerializedName("activeFlag")
	private String activeFlag;

	@SerializedName("timestamp")
	private String timestamp;

	public void setIinno(String iinno){
		this.iinno = iinno;
	}

	public String getIinno(){
		return iinno;
	}

	public void setBankName(String bankName){
		this.bankName = bankName;
	}

	public String getBankName(){
		return bankName;
	}

	public void setDetails(String details){
		this.details = details;
	}

	public String getDetails(){
		return details;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setRemarks(String remarks){
		this.remarks = remarks;
	}

	public String getRemarks(){
		return remarks;
	}

	public void setActiveFlag(String activeFlag){
		this.activeFlag = activeFlag;
	}

	public String getActiveFlag(){
		return activeFlag;
	}

	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}

	public String getTimestamp(){
		return timestamp;
	}
}