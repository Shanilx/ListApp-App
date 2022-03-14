
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Get_Profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProfileResponse {

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

        @SerializedName("address")
        private String mAddress;
        @SerializedName("area")
        private String mArea;
        @SerializedName("city")
        private String mCity;
        @SerializedName("city_id")
        private String mCityId;
        @SerializedName("contact_number")
        private String mContactNumber;
        @SerializedName("contact_person")
        private List<ContactPerson> mContactPerson;
        @SerializedName("drug_lic_no")
        private String mDrugLicNo;
        @SerializedName("email")
        private String mEmail;
        @SerializedName("estd_year")
        private String mEstdYear;
        @SerializedName("retailer_name")
        private String mRetailerName;
        @SerializedName("shop_name")
        private String mShopName;
        @SerializedName("state")
        private String mState;
        @SerializedName("state_id")
        private String mStateId;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("tin_number")
        private String mTinNumber;
        @SerializedName("user_id")
        private String mUserId;

        public String getAddress() {
            return mAddress;
        }

        public void setAddress(String address) {
            mAddress = address;
        }

        public String getArea() {
            return mArea;
        }

        public void setArea(String area) {
            mArea = area;
        }

        public String getCity() {
            return mCity;
        }

        public void setCity(String city) {
            mCity = city;
        }

        public String getCityId() {
            return mCityId;
        }

        public void setCityId(String cityId) {
            mCityId = cityId;
        }

        public String getContactNumber() {
            return mContactNumber;
        }

        public void setContactNumber(String contactNumber) {
            mContactNumber = contactNumber;
        }

        public List<ContactPerson> getContactPerson() {
            return mContactPerson;
        }

        public void setContactPerson(List<ContactPerson> contactPerson) {
            mContactPerson = contactPerson;
        }

        public String getDrugLicNo() {
            return mDrugLicNo;
        }

        public void setDrugLicNo(String drugLicNo) {
            mDrugLicNo = drugLicNo;
        }

        public String getEmail() {
            return mEmail;
        }

        public void setEmail(String email) {
            mEmail = email;
        }

        public String getEstdYear() {
            return mEstdYear;
        }

        public void setEstdYear(String estdYear) {
            mEstdYear = estdYear;
        }

        public String getRetailerName() {
            return mRetailerName;
        }

        public void setRetailerName(String retailerName) {
            mRetailerName = retailerName;
        }

        public String getShopName() {
            return mShopName;
        }

        public void setShopName(String shopName) {
            mShopName = shopName;
        }

        public String getState() {
            return mState;
        }

        public void setState(String state) {
            mState = state;
        }

        public String getStateId() {
            return mStateId;
        }

        public void setStateId(String stateId) {
            mStateId = stateId;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        public String getTinNumber() {
            return mTinNumber;
        }

        public void setTinNumber(String tinNumber) {
            mTinNumber = tinNumber;
        }

        public String getUserId() {
            return mUserId;
        }

        public void setUserId(String userId) {
            mUserId = userId;
        }


        public class ContactPerson {

            @SerializedName("contact_name")
            private String mContactName;
            @SerializedName("contact_number")
            private String mContactNumber;

            public String getContactName() {
                return mContactName;
            }

            public void setContactName(String contactName) {
                mContactName = contactName;
            }

            public String getContactNumber() {
                return mContactNumber;
            }

            public void setContactNumber(String contactNumber) {
                mContactNumber = contactNumber;
            }

        }


    }

}
