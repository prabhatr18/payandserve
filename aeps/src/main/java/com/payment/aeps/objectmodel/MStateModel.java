package com.payment.aeps.objectmodel;

import com.google.gson.annotations.SerializedName;

public class MStateModel {

	@SerializedName("stateid")
	private String stateid;

	@SerializedName("id")
	private String id;

	@SerializedName("statename")
	private String statename;

	public String getStateid(){
		return stateid;
	}

	public String getId(){
		return id;
	}

	public String getStatename(){
		return statename;
	}
}