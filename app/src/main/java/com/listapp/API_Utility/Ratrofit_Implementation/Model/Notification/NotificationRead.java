
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Notification;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class NotificationRead {

    @SerializedName("error")
    private String mError;
    @SerializedName("is_read")
    private String mIsRead;
    @SerializedName("message")
    private String mMessage;

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

    public String getIsRead() {
        return mIsRead;
    }

    public void setIsRead(String isRead) {
        mIsRead = isRead;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

}
