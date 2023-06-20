package com.digital.payandserve.views.reports.model;

import com.google.gson.annotations.SerializedName;

public class BuyProductModel {

	@SerializedName("sendername")
	private String sendername;

	@SerializedName("product")
	private String product;

	@SerializedName("created_at")
	private String createdAt;
	

	@SerializedName("providername")
	private String providername;

	@SerializedName("via")
	private String via;

	@SerializedName("number")
	private String number;

	@SerializedName("balance")
	private String balance;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("fundbank")
	private String fundbank;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("price")
	private String price;

	@SerializedName("id")
	private String id;

	@SerializedName("status")
	private String status;

	@SerializedName("username")
	private String username;

	public String getSendername(){
		return sendername;
	}

	public String getProduct(){
		return product;
	}

	public String getCreatedAt(){
		return createdAt;
	}


	public String getProvidername(){
		return providername;
	}

	public String getVia(){
		return via;
	}

	public String getNumber(){
		return number;
	}

	public String getBalance(){
		return balance;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getFundbank(){
		return fundbank;
	}

	public String getUserId(){
		return userId;
	}

	public String getPrice(){
		return price;
	}

	public String getId(){
		return id;
	}

	public String getStatus(){
		return status;
	}

	public String getUsername(){
		return username;
	}
}