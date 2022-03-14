
package com.listapp.API_Utility.Ratrofit_Implementation.Model.CompanySearch;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CompanyDetailResponse {

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

        @SerializedName("company_detail")
        private List<CompanyDetail> mCompanyDetail;
        @SerializedName("suppliers")
        private List<Supplier> mSuppliers;

        public List<CompanyDetail> getCompanyDetail() {
            return mCompanyDetail;
        }

        public void setCompanyDetail(List<CompanyDetail> companyDetail) {
            mCompanyDetail = companyDetail;
        }

        public List<Supplier> getSuppliers() {
            return mSuppliers;
        }

        public void setSuppliers(List<Supplier> suppliers) {
            mSuppliers = suppliers;
        }

        public class CompanyDetail {

            @SerializedName("company_id")
            private String mCompanyId;
            @SerializedName("company_name")
            private String mCompanyName;
            @SerializedName("date_added")
            private String mDateAdded;
            @SerializedName("status")
            private String mStatus;

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

            public String getDateAdded() {
                return mDateAdded;
            }

            public void setDateAdded(String dateAdded) {
                mDateAdded = dateAdded;
            }

            public String getStatus() {
                return mStatus;
            }

            public void setStatus(String status) {
                mStatus = status;
            }

        }


        public class Supplier {

            @SerializedName("authorised_status")
            private String mAuthorisedStatus;
            @SerializedName("city_name")
            private String mCityName;
            @SerializedName("shop_name")
            private String mShopName;
            @SerializedName("supplier_address")
            private String mSupplierAddress;
            @SerializedName("supplier_id")
            private String mSupplierId;
            @SerializedName("supplier_name")
            private String mSupplierName;

            public String getAuthorisedStatus() {
                return mAuthorisedStatus;
            }

            public void setAuthorisedStatus(String authorisedStatus) {
                mAuthorisedStatus = authorisedStatus;
            }

            public String getCityName() {
                return mCityName;
            }

            public void setCityName(String cityName) {
                mCityName = cityName;
            }

            public String getShopName() {
                return mShopName;
            }

            public void setShopName(String shopName) {
                mShopName = shopName;
            }

            public String getSupplierAddress() {
                return mSupplierAddress;
            }

            public void setSupplierAddress(String supplierAddress) {
                mSupplierAddress = supplierAddress;
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

        }
    }
}
