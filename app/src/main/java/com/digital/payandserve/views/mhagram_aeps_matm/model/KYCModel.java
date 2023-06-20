package com.digital.payandserve.views.mhagram_aeps_matm.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class KYCModel implements Parcelable {
    String FirstName, LastName, Email, PhoneOne, PhoneTwo, DOB, State,
            District, Address, City, Block, Landmark, Mohhalla, Location, Pincode,
            Pan, ShopName, ShopType, Qualification, Population, LocationType;

    protected KYCModel(Parcel in) {
        FirstName = in.readString();
        LastName = in.readString();
        Email = in.readString();
        PhoneOne = in.readString();
        PhoneTwo = in.readString();
        DOB = in.readString();
        State = in.readString();
        District = in.readString();
        Address = in.readString();
        City = in.readString();
        Block = in.readString();
        Landmark = in.readString();
        Mohhalla = in.readString();
        Location = in.readString();
        Pincode = in.readString();
        Pan = in.readString();
        ShopName = in.readString();
        ShopType = in.readString();
        Qualification = in.readString();
        Population = in.readString();
        LocationType = in.readString();
    }

    public Map<String, String> getParam(){
        Map<String, String> map = new HashMap<>();
        map.put("bc_f_name", getFirstName());
        map.put("bc_l_name", getLastName());
        map.put("emailid", getEmail());
        map.put("phone1", getPhoneOne());
        map.put("phone2", getPhoneTwo());
        map.put("bc_dob", getDOB());
        map.put("bc_state", getState());
        map.put("bc_district", getDistrict());
        map.put("bc_address", getAddress());
        map.put("bc_block", getBlock());
        map.put("bc_city", getCity());
        map.put("bc_landmark", getLandmark());
        map.put("bc_mohhalla", getMohhalla());
        map.put("bc_loc", getLocation());
        map.put("bc_pincode", getPincode());
        map.put("bc_pan", getPan());
        map.put("shopname", getShopName());
        map.put("shopType", getShopType());
        map.put("qualification", getQualification());
        map.put("population", getPopulation());
        map.put("locationType", getLocationType());
        return map;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(Email);
        dest.writeString(PhoneOne);
        dest.writeString(PhoneTwo);
        dest.writeString(DOB);
        dest.writeString(State);
        dest.writeString(District);
        dest.writeString(Address);
        dest.writeString(City);
        dest.writeString(Block);
        dest.writeString(Landmark);
        dest.writeString(Mohhalla);
        dest.writeString(Location);
        dest.writeString(Pincode);
        dest.writeString(Pan);
        dest.writeString(ShopName);
        dest.writeString(ShopType);
        dest.writeString(Qualification);
        dest.writeString(Population);
        dest.writeString(LocationType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KYCModel> CREATOR = new Creator<KYCModel>() {
        @Override
        public KYCModel createFromParcel(Parcel in) {
            return new KYCModel(in);
        }

        @Override
        public KYCModel[] newArray(int size) {
            return new KYCModel[size];
        }
    };

    public KYCModel() { }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneOne() {
        return PhoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        PhoneOne = phoneOne;
    }

    public String getPhoneTwo() {
        return PhoneTwo;
    }

    public void setPhoneTwo(String phoneTwo) {
        PhoneTwo = phoneTwo;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getBlock() {
        return Block;
    }

    public void setBlock(String block) {
        Block = block;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getMohhalla() {
        return Mohhalla;
    }

    public void setMohhalla(String mohhalla) {
        Mohhalla = mohhalla;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getPan() {
        return Pan;
    }

    public void setPan(String pan) {
        Pan = pan;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShopType() {
        return ShopType;
    }

    public void setShopType(String shopType) {
        ShopType = shopType;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public String getPopulation() {
        return Population;
    }

    public void setPopulation(String population) {
        Population = population;
    }

    public String getLocationType() {
        return LocationType;
    }

    public void setLocationType(String locationType) {
        LocationType = locationType;
    }
}
