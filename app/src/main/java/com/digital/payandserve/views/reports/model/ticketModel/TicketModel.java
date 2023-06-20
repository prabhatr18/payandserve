package com.digital.payandserve.views.reports.model.ticketModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TicketModel implements Parcelable {

	@SerializedName("transaction_id")
	private String transactionId;

	@SerializedName("product")
	private String product;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("subject")
	private String subject;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("user")
	private User user;

	@SerializedName("status")
	private String status;

	protected TicketModel(Parcel in) {
		transactionId = in.readString();
		product = in.readString();
		userId = in.readString();
		subject = in.readString();
		description = in.readString();
		createdAt = in.readString();
		id = in.readString();
		user = in.readParcelable(User.class.getClassLoader());
		status = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(transactionId);
		dest.writeString(product);
		dest.writeString(userId);
		dest.writeString(subject);
		dest.writeString(description);
		dest.writeString(createdAt);
		dest.writeString(id);
		dest.writeParcelable(user, flags);
		dest.writeString(status);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<TicketModel> CREATOR = new Creator<TicketModel>() {
		@Override
		public TicketModel createFromParcel(Parcel in) {
			return new TicketModel(in);
		}

		@Override
		public TicketModel[] newArray(int size) {
			return new TicketModel[size];
		}
	};

	public String getTransactionId(){
		return transactionId;
	}

	public String getProduct(){
		return product;
	}

	public String getUserId(){
		return userId;
	}

	public String getSubject(){
		return subject;
	}

	public String getDescription(){
		return description;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public User getUser(){
		return user;
	}

	public String getStatus(){
		return status;
	}
}