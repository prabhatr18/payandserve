package com.digital.payandserve.views.reports.model.ticketModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

	@SerializedName("agentcode")
	private String agentcode;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	protected User(Parcel in) {
		agentcode = in.readString();
		name = in.readString();
		mobile = in.readString();
		id = in.readString();
		email = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(agentcode);
		dest.writeString(name);
		dest.writeString(mobile);
		dest.writeString(id);
		dest.writeString(email);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	public String getAgentcode(){
		return agentcode;
	}

	public String getName(){
		return name;
	}

	public String getMobile(){
		return mobile;
	}

	public String getId(){
		return id;
	}

	public String getEmail(){
		return email;
	}
}