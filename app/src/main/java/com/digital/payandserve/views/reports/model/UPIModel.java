package com.digital.payandserve.views.reports.model;

import com.google.gson.annotations.SerializedName;

public class UPIModel{

	@SerializedName("api_id")
	private String apiId;

	@SerializedName("amount")
	private String amount;

	@SerializedName("product")
	private String product;

	@SerializedName("charge")
	private String charge;

	@SerializedName("wprofit")
	private String wprofit;

	@SerializedName("transtype")
	private String transtype;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("remark")
	private Object remark;

	@SerializedName("type")
	private String type;

	@SerializedName("disprofit")
	private String disprofit;

	@SerializedName("via")
	private String via;

	@SerializedName("aepstype")
	private String aepstype;

	@SerializedName("rtype")
	private String rtype;

	@SerializedName("mdprofit")
	private String mdprofit;

	@SerializedName("balance")
	private String balance;

	@SerializedName("provider_id")
	private String providerId;

	@SerializedName("id")
	private String id;

	@SerializedName("txnid")
	private String txnid;

	@SerializedName("status")
	private String status;

	public String getApiId(){
		return apiId;
	}

	public String getAmount(){
		return amount;
	}

	public String getProduct(){
		return product;
	}

	public String getCharge(){
		return charge;
	}

	public String getWprofit(){
		return wprofit;
	}

	public String getTranstype(){
		return transtype;
	}

	public String getMobile(){
		return mobile;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public Object getRemark(){
		return remark;
	}

	public String getType(){
		return type;
	}

	public String getDisprofit(){
		return disprofit;
	}

	public String getVia(){
		return via;
	}

	public String getAepstype(){
		return aepstype;
	}

	public String getRtype(){
		return rtype;
	}

	public String getMdprofit(){
		return mdprofit;
	}

	public String getBalance(){
		return balance;
	}

	public String getProviderId(){
		return providerId;
	}

	public String getId(){
		return id;
	}

	public String getTxnid(){
		return txnid;
	}

	public String getStatus(){
		return status;
	}
}