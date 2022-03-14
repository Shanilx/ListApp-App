
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Splash_Response;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;

@SuppressWarnings("unused")
public class AuthenticationResponse {

    @SerializedName("data")
    private String mData;
    @SerializedName("error")
    private String mError;
    @SerializedName("message")
    private String mMessage;

    public String getData() {
        return mData;
    }

    public void setData(String data) {
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

}
