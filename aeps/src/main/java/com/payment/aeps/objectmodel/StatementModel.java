package com.payment.aeps.objectmodel;

import com.google.gson.annotations.SerializedName;

public class StatementModel{

	@SerializedName("date")
	private String date;

	@SerializedName("amount")
	private String amount;

	@SerializedName("narration")
	private String narration;

	@SerializedName("txnType")
	private String txnType;

	public String getDate(){
		return date;
	}

	public String getAmount(){
		return amount;
	}

	public String getNarration(){
		return narration;
	}

	public String getTxnType(){
		return txnType;
	}

	public StatementModel(String date, String amount, String narration, String txnType) {
		this.date = date;
		this.amount = amount;
		this.narration = narration;
		this.txnType = txnType;
	}
}