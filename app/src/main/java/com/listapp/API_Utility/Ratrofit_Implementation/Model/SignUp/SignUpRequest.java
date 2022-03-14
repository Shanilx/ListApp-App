package com.listapp.API_Utility.Ratrofit_Implementation.Model.SignUp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by syscraft on 7/6/2017.
 */

public class SignUpRequest {

//
// params.put("address", signUpRequest.getmAddress());
//        params.put("area", signUpRequest.getmArea());
//        params.put("city", signUpRequest.getmCity());
//        params.put("mobile_no", signUpRequest.getmContactNumber());
//        params.put("device_token", signUpRequest.getmDeviceToken());
//        params.put("user_type", signUpRequest.getmUserType());
//        params.put("state", signUpRequest.getmState());
//        params.put("shop_name", signUpRequest.getmShopName());
//        params.put("password", signUpRequest.getmPassword());
//        params.put("mobile_unique_id", signUpRequest.getmMobileUniqueID());
//        params.put("full_name", signUpRequest.getmFullName());
//        params.put("email", signUpRequest.getmEmail());
//        params.put("device_type", signUpRequest.getmDeviceType());

    @SerializedName("address")
    private String mAddress;
    @SerializedName("area")
    private String mArea;
    @SerializedName("city")
    private String mCity;
    @SerializedName("mobile_no")
    private String mContactNumber;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("full_name")
    private String mFullName;
    @SerializedName("user_type")
    private String mUserType;
    @SerializedName("device_type")
    private String mDeviceType;
    @SerializedName("device_token")
    private String mDeviceToken;
    @SerializedName("shop_name")
    private String mShopName;
    @SerializedName("state")
    private String mState;
    @SerializedName("mobile_unique_id")
    private String mMobileUniqueID;

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmArea() {
        return mArea;
    }

    public void setmArea(String mArea) {
        this.mArea = mArea;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmContactNumber() {
        return mContactNumber;
    }

    public void setmContactNumber(String mContactNumber) {
        this.mContactNumber = mContactNumber;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmFullName() {
        return mFullName;
    }

    public void setmFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public String getmUserType() {
        return mUserType;
    }

    public void setmUserType(String mUserType) {
        this.mUserType = mUserType;
    }

    public String getmDeviceType() {
        return mDeviceType;
    }

    public void setmDeviceType(String mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    public String getmDeviceToken() {
        return mDeviceToken;
    }

    public void setmDeviceToken(String mDeviceToken) {
        this.mDeviceToken = mDeviceToken;
    }

    public String getmShopName() {
        return mShopName;
    }

    public void setmShopName(String mShopName) {
        this.mShopName = mShopName;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmMobileUniqueID() {
        return mMobileUniqueID;
    }

    public void setmMobileUniqueID(String mMobileUniqueID) {
        this.mMobileUniqueID = mMobileUniqueID;
    }

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "mAddress='" + mAddress + '\'' +
                ", mArea='" + mArea + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mContactNumber='" + mContactNumber + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mFullName='" + mFullName + '\'' +
                ", mUserType='" + mUserType + '\'' +
                ", mDeviceType='" + mDeviceType + '\'' +
                ", mDeviceToken='" + mDeviceToken + '\'' +
                ", mShopName='" + mShopName + '\'' +
                ", mState='" + mState + '\'' +
                ", mMobileUniqueID='" + mMobileUniqueID + '\'' +
                '}';
    }
}
