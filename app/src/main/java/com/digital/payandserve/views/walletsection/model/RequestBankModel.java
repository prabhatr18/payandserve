package com.digital.payandserve.views.walletsection.model;

import com.google.gson.annotations.SerializedName;

public class RequestBankModel {

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("ifsc")
	private String ifsc;

	@SerializedName("branch")
	private String branch;

	@SerializedName("account")
	private String account;

	@SerializedName("status")
	private String status;

	@SerializedName("username")
	private String username;

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getUserId(){
		return userId;
	}

	public String getName(){
		return name;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public String getIfsc(){
		return ifsc;
	}

	public String getBranch(){
		return branch;
	}

	public String getAccount(){
		return account;
	}

	public String getStatus(){
		return status;
	}

	public String getUsername(){
		return username;
	}
}