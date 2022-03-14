
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Login_Response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("data")
    private Data mData;
    @SerializedName("error")
    private String mError;
    @SerializedName("message")
    private String mMessage;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }


    public class Data {

        @SerializedName("Address")
        private String mAddress;
        @SerializedName("Area")
        private String mArea;
        @SerializedName("City")
        private String mCity;
        @SerializedName("CityId")
        private String mCityId;
        @SerializedName("Contact_Number")
        private String mContactNumber;
        @SerializedName("Email")
        private String mEmail;
        @SerializedName("Full_Name")
        private String mFullName;
        @SerializedName("OTP")
        private String mOTP;
        @SerializedName("Role_id")
        private String mRoleId;
        @SerializedName("Role_Name")
        private String mRoleName;
        @SerializedName("Shop_Name")
        private String mShopName;
        @SerializedName("State")
        private String mState;
        @SerializedName("Stateid")
        private String mStateid;
        @SerializedName("User_ID")
        private String mUserID;

        public String getAddress() {
            return mAddress;
        }

        public void setAddress(String Address) {
            mAddress = Address;
        }

        public String getArea() {
            return mArea;
        }

        public void setArea(String Area) {
            mArea = Area;
        }

        public String getCity() {
            return mCity;
        }

        public void setCity(String City) {
            mCity = City;
        }

        public String getCityId() {
            return mCityId;
        }

        public void setCityId(String CityId) {
            mCityId = CityId;
        }

        public String getContactNumber() {
            return mContactNumber;
        }

        public void setContactNumber(String ContactNumber) {
            mContactNumber = ContactNumber;
        }

        public String getEmail() {
            return mEmail;
        }

        public void setEmail(String Email) {
            mEmail = Email;
        }

        public String getFullName() {
            return mFullName;
        }

        public void setFullName(String FullName) {
            mFullName = FullName;
        }

        public String getOTP() {
            return mOTP;
        }

        public void setOTP(String OTP) {
            mOTP = OTP;
        }

        public String getRoleId() {
            return mRoleId;
        }

        public void setRoleId(String RoleId) {
            mRoleId = RoleId;
        }

        public String getRoleName() {
            return mRoleName;
        }

        public void setRoleName(String RoleName) {
            mRoleName = RoleName;
        }

        public String getShopName() {
            return mShopName;
        }

        public void setShopName(String ShopName) {
            mShopName = ShopName;
        }

        public String getState() {
            return mState;
        }

        public void setState(String State) {
            mState = State;
        }

        public String getStateid() {
            return mStateid;
        }

        public void setStateid(String Stateid) {
            mStateid = Stateid;
        }

        public String getUserID() {
            return mUserID;
        }

        public void setUserID(String UserID) {
            mUserID = UserID;
        }

    }

}
