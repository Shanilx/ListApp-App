
package com.listapp.API_Utility.Ratrofit_Implementation.Model.CompanySearch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanySearchResponse {

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
}
