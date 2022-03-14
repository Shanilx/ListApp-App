
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Edit_Profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditProfileResponse {

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
        @SerializedName("contact_number")
        private String mContactNumber;
        @SerializedName("contact_person")
        private List<ContactPerson> mContactPerson;
        @SerializedName("drug_lic_number")
        private String mDrugLicNumber;
        @SerializedName("email")
        private String mEmail;
        @SerializedName("estd_year")
        private String mEstdYear;
        @SerializedName("full_name")
        private String mFullName;
        @SerializedName("state")
        private String mState;
        @SerializedName("tin_number")
        private String mTinNumber;

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

        public String getDrugLicNumber() {
            return mDrugLicNumber;
        }

        public void setDrugLicNumber(String drugLicNumber) {
            mDrugLicNumber = drugLicNumber;
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

        public String getFullName() {
            return mFullName;
        }

        public void setFullName(String fullName) {
            mFullName = fullName;
        }

        public String getState() {
            return mState;
        }

        public void setState(String state) {
            mState = state;
        }

        public String getTinNumber() {
            return mTinNumber;
        }

        public void setTinNumber(String tinNumber) {
            mTinNumber = tinNumber;
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
