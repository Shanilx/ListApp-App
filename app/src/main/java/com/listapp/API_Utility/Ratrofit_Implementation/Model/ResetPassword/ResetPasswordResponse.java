
package com.listapp.API_Utility.Ratrofit_Implementation.Model.ResetPassword;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordResponse {

    @SerializedName("error")
    private String mError;
    @SerializedName("message")
    private String mMessage;

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

}
