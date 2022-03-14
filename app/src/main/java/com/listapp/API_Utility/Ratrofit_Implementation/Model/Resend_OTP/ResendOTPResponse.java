
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Resend_OTP;

import com.google.gson.annotations.SerializedName;

public class ResendOTPResponse {

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

        @SerializedName("otp")
        private Long mOtp;

        public Long getOtp() {
            return mOtp;
        }

        public void setOtp(Long otp) {
            mOtp = otp;
        }
    }
}
