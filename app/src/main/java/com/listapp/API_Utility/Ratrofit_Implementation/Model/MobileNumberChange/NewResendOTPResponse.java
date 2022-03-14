
package com.listapp.API_Utility.Ratrofit_Implementation.Model.MobileNumberChange;

import com.google.gson.annotations.SerializedName;

public class NewResendOTPResponse {

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

        @SerializedName("OTP")
        private Long mOTP;

        public Long getOTP() {
            return mOTP;
        }

        public void setOTP(Long OTP) {
            mOTP = OTP;
        }
    }
}
