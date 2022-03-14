package com.listapp.API_Utility.Ratrofit_Implementation.Model.City;

import com.google.gson.annotations.SerializedName;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.TermsAndCondition.TermsAndConditionResponse;

import java.util.List;

public class Cities {
    @SerializedName("data")
    private List<String> mData;
    @SerializedName("error")
    private String mError;
    @SerializedName("message")
    private String mMessage;

    public List<String> getmData() {
        return mData;
    }

    public void setmData(List<String> mData) {
        this.mData = mData;
    }
}
