package com.digital.payandserve.views.walletsection.model;

import com.google.gson.annotations.SerializedName;

public class ModeModel{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("status")
	private String status;

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}

	public String getStatus(){
		return status;
	}
}