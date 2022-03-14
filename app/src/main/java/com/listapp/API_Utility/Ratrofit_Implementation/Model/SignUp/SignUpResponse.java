
package com.listapp.API_Utility.Ratrofit_Implementation.Model.SignUp;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SignUpResponse {

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

        @SerializedName("Contact_Number")
        private String mContactNumber;
        @SerializedName("OTP")
        private String mOTP;
        @SerializedName("Role_id")
        private String mRoleId;
        @SerializedName("Role_Name")
        private String mRoleName;
        @SerializedName("Shop_Name")
        private String mShopName;
        @SerializedName("User_ID")
        private String mUserID;

        public String getContactNumber() {
            return mContactNumber;
        }

        public void setContactNumber(String ContactNumber) {
            mContactNumber = ContactNumber;
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

        public String getUserID() {
            return mUserID;
        }

        public void setUserID(String UserID) {
            mUserID = UserID;
        }

    }


}
