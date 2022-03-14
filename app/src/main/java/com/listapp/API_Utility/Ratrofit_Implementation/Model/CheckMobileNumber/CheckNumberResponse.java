
package com.listapp.API_Utility.Ratrofit_Implementation.Model.CheckMobileNumber;

import com.google.gson.annotations.SerializedName;

public class CheckNumberResponse {

    @SerializedName("error")
    private String mError;

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

}
