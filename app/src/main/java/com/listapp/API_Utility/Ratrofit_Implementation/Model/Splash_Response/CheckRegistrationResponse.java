
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Splash_Response;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CheckRegistrationResponse {

    @SerializedName("data")
    private List<Object> mData;
    @SerializedName("error")
    private String mError;
    @SerializedName("message")
    private String mMessage;

    public List<Object> getData() {
        return mData;
    }

    public void setData(List<Object> data) {
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
