package com.listapp.API_Utility.Ratrofit_Implementation.Model.TermsAndCondition;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TermsAndConditionResponse {

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

        @SerializedName("date_added")
        private String mDateAdded;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("terms_content")
        private String mTermsContent;
        @SerializedName("terms_id")
        private String mTermsId;

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

        public String getTermsContent() {
            return mTermsContent;
        }

        public void setTermsContent(String termsContent) {
            mTermsContent = termsContent;
        }

        public String getTermsId() {
            return mTermsId;
        }

        public void setTermsId(String termsId) {
            mTermsId = termsId;
        }

    }

}
