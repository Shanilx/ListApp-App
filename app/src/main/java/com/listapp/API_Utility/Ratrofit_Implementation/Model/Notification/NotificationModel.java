
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Notification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {

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

        @SerializedName("date")
        private String mDate;
        @SerializedName("meassage")
        private String mMeassage;
        @SerializedName("current_date")
        private String currentDate;
        @SerializedName("notification_id")
        private String mNotificationId;
        @SerializedName("time")
        private String mTime;
        @SerializedName("title")
        private String mTitle;
        @SerializedName("type")
        private String mType;
        @SerializedName("read")
        private String mIsRead;

        public String getCurrentDate() {
            return currentDate;
        }

        public void setCurrentDate(String currentDate) {
            this.currentDate = currentDate;
        }

        public String getIsRead() {
            return mIsRead;
        }

        public void setIsRead(String mIsRead) {
            this.mIsRead = mIsRead;
        }

        public String getDate() {
            return mDate;
        }

        public void setDate(String date) {
            mDate = date;
        }

        public String getMeassage() {
            return mMeassage;
        }

        public void setMeassage(String meassage) {
            mMeassage = meassage;
        }

        public String getNotificationId() {
            return mNotificationId;
        }

        public void setNotificationId(String notificationId) {
            mNotificationId = notificationId;
        }

        public String getTime() {
            return mTime;
        }

        public void setTime(String time) {
            mTime = time;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            mType = type;
        }

        @Override
        public String toString() {
            return "Datum{" +
                    "mDate='" + mDate + '\'' +
                    ", mMeassage='" + mMeassage + '\'' +
                    ", mNotificationId='" + mNotificationId + '\'' +
                    ", mTime='" + mTime + '\'' +
                    ", mTitle='" + mTitle + '\'' +
                    ", mType='" + mType + '\'' +
                    ", mIsRead='" + mIsRead + '\'' +
                    '}';
        }
    }

}
