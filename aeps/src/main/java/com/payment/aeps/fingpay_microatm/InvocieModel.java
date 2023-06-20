package com.payment.aeps.fingpay_microatm;

import com.google.gson.annotations.SerializedName;

public class InvocieModel{

	@SerializedName("Value")
	private String value;

	@SerializedName("Key")
	private String key;

	@SerializedName("Label")
	private String label;

	public String getValue(){
		return value;
	}

	public String getKey(){
		return key;
	}

	public String getLabel(){
		return label;
	}

	public InvocieModel(String value, String key, String label) {
		this.value = value;
		this.key = key;
		this.label = label;
	}
}