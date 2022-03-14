
package com.listapp.API_Utility.Ratrofit_Implementation.Model.SupplierDetails;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupplierProfile {

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

        @SerializedName("supplier")
        private List<Supplier> mSupplier;

        public List<Supplier> getSupplier() {
            return mSupplier;
        }

        public void setSupplier(List<Supplier> supplier) {
            mSupplier = supplier;
        }

        public class Supplier {

            @SerializedName("address")
            private String mAddress;
            @SerializedName("area")
            private String mArea;
            @SerializedName("authorised")
            private String mAuthorised;
            @SerializedName("city")
            private String mCity;
            @SerializedName("company_dealership")
            private List<CompanyDealership> mCompanyDealership;
            @SerializedName("contact_number")
            private String mContactNumber;
            @SerializedName("contact_person")
            private List<ContactPerson> mContactPerson;
            @SerializedName("dln_no")
            private String mDlnNo;
            @SerializedName("email")
            private String mEmail;
            @SerializedName("estd_no")
            private String mEstdNo;
            @SerializedName("favourite_status")
            private String mFavouriteStatus;
            @SerializedName("shop_name")
            private String mShopName;
            @SerializedName("state")
            private String mState;
            @SerializedName("status")
            private String mStatus;
            @SerializedName("supplier_id")
            private String mSupplierId;
            @SerializedName("supplier_name")
            private String mSupplierName;
            @SerializedName("tln_no")
            private String mTlnNo;

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

            public String getAuthorised() {
                return mAuthorised;
            }

            public void setAuthorised(String authorised) {
                mAuthorised = authorised;
            }

            public String getCity() {
                return mCity;
            }

            public void setCity(String city) {
                mCity = city;
            }

            public List<CompanyDealership> getCompanyDealership() {
                return mCompanyDealership;
            }

            public void setCompanyDealership(List<CompanyDealership> companyDealership) {
                mCompanyDealership = companyDealership;
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

            public String getDlnNo() {
                return mDlnNo;
            }

            public void setDlnNo(String dlnNo) {
                mDlnNo = dlnNo;
            }

            public String getEmail() {
                return mEmail;
            }

            public void setEmail(String email) {
                mEmail = email;
            }

            public String getEstdNo() {
                return mEstdNo;
            }

            public void setEstdNo(String estdNo) {
                mEstdNo = estdNo;
            }

            public String getFavouriteStatus() {
                return mFavouriteStatus;
            }

            public void setFavouriteStatus(String favouriteStatus) {
                mFavouriteStatus = favouriteStatus;
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

            public String getStatus() {
                return mStatus;
            }

            public void setStatus(String status) {
                mStatus = status;
            }

            public String getSupplierId() {
                return mSupplierId;
            }

            public void setSupplierId(String supplierId) {
                mSupplierId = supplierId;
            }

            public String getSupplierName() {
                return mSupplierName;
            }

            public void setSupplierName(String supplierName) {
                mSupplierName = supplierName;
            }

            public String getTlnNo() {
                return mTlnNo;
            }

            public void setTlnNo(String tlnNo) {
                mTlnNo = tlnNo;
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

            public class CompanyDealership {

                @SerializedName("company_id")
                private String mCompanyId;
                @SerializedName("company_name")
                private String mCompanyName;

                public String getCompanyId() {
                    return mCompanyId;
                }

                public void setCompanyId(String companyId) {
                    mCompanyId = companyId;
                }

                public String getCompanyName() {
                    return mCompanyName;
                }

                public void setCompanyName(String companyName) {
                    mCompanyName = companyName;
                }

            }
        }
    }
}
