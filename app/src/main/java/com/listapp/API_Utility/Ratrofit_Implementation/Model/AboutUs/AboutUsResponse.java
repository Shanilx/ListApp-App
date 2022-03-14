
package com.listapp.API_Utility.Ratrofit_Implementation.Model.AboutUs;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AboutUsResponse {

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

        @SerializedName("about_content")
        private String mAboutContent;
        @SerializedName("about_us_id")
        private String mAboutUsId;
        @SerializedName("date_added")
        private String mDateAdded;
        @SerializedName("status")
        private String mStatus;

        public String getAboutContent() {
            return mAboutContent;
        }

        public void setAboutContent(String aboutContent) {
            mAboutContent = aboutContent;
        }

        public String getAboutUsId() {
            return mAboutUsId;
        }

        public void setAboutUsId(String aboutUsId) {
            mAboutUsId = aboutUsId;
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
