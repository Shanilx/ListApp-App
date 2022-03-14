
package com.listapp.API_Utility.Ratrofit_Implementation.Model.SearchSupplier;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SearchSupplierResponse {

    @SerializedName("data")
    private List<Datum> mData;
    @SerializedName("error")
    private String mError;
    @SerializedName("message")
    private String mMessage;

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
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

    public class Datum {

        @SerializedName("authorised_status")
        private String mAuthorisedStatus;
        @SerializedName("city_name")
        private String mCityName;
        @SerializedName("supplier_address")
        private String mSupplierAddress;
        @SerializedName("supplier_id")
        private String mSupplierId;
        @SerializedName("supplier_name")
        private String mSupplierName;
        @SerializedName("shop_name")
        private String mShopName;

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

        public String getShopName() {
            return mShopName;
        }

        public void setShopName(String shopName) {
            mShopName = shopName;
        }

    }
}
