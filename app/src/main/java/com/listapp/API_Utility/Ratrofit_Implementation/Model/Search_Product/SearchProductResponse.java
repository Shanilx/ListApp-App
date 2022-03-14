
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SearchProductResponse {

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

        @SerializedName("add_date")
        private String mAddDate;
        @SerializedName("company_name")
        private String mCompanyName;
        @SerializedName("drug_name")
        private String mDrugName;
        @SerializedName("form")
        private String mForm;
        @SerializedName("mrp")
        private String mMrp;
        @SerializedName("pack_size")
        private String mPackSize;
        @SerializedName("packing_type")
        private String mPackingType;
        @SerializedName("product_id")
        private String mProductId;
        @SerializedName("product_name")
        private String mProductName;
        @SerializedName("rate")
        private String mRate;
        @SerializedName("schedule")
        private String mSchedule;
        @SerializedName("status")
        private String mStatus;

        public String getAddDate() {
            return mAddDate;
        }

        public void setAddDate(String addDate) {
            mAddDate = addDate;
        }

        public String getCompanyName() {
            return mCompanyName;
        }

        public void setCompanyName(String companyName) {
            mCompanyName = companyName;
        }

        public String getDrugName() {
            return mDrugName;
        }

        public void setDrugName(String drugName) {
            mDrugName = drugName;
        }

        public String getForm() {
            return mForm;
        }

        public void setForm(String form) {
            mForm = form;
        }

        public String getMrp() {
            return mMrp;
        }

        public void setMrp(String mrp) {
            mMrp = mrp;
        }

        public String getPackSize() {
            return mPackSize;
        }

        public void setPackSize(String packSize) {
            mPackSize = packSize;
        }

        public String getPackingType() {
            return mPackingType;
        }

        public void setPackingType(String packingType) {
            mPackingType = packingType;
        }

        public String getProductId() {
            return mProductId;
        }

        public void setProductId(String productId) {
            mProductId = productId;
        }

        public String getProductName() {
            return mProductName;
        }

        public void setProductName(String productName) {
            mProductName = productName;
        }

        public String getRate() {
            return mRate;
        }

        public void setRate(String rate) {
            mRate = rate;
        }

        public String getSchedule() {
            return mSchedule;
        }

        public void setSchedule(String schedule) {
            mSchedule = schedule;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

    }


}
