package com.payment.aeps.fingpay_microatm;

import com.google.gson.annotations.SerializedName;

public class FingPayInitiateModel {

	@SerializedName("merchantPassword")
	private String merchantPassword;

	@SerializedName("merchantId")
	private String merchantId;

	@SerializedName("lon")
	private String lon;

	@SerializedName("superMerchentId")
	private String superMerchentId;

	@SerializedName("lat")
	private String lat;

	@SerializedName("txnid")
	private String txnid;

	public String getMerchantPassword(){
		return merchantPassword;
	}

	public String getMerchantId(){
		return merchantId;
	}

	public String getLon(){
		return lon;
	}

	public String getSuperMerchentId(){
		return superMerchentId;
	}

	public String getLat(){
		return lat;
	}

	public String getTxnid(){
		return txnid;
	}
}