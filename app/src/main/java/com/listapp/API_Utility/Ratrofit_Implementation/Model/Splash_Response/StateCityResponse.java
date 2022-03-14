
package com.listapp.API_Utility.Ratrofit_Implementation.Model.Splash_Response;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StateCityResponse {

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

        @SerializedName("city")
        private List<City> mCity;
        @SerializedName("state_id")
        private String mStateId;
        @SerializedName("state_name")
        private String mStateName;

        public List<City> getCity() {
            return mCity;
        }

        public void setCity(List<City> city) {
            mCity = city;
        }

        public String getStateId() {
            return mStateId;
        }

        public void setStateId(String stateId) {
            mStateId = stateId;
        }

        public String getStateName() {
            return mStateName;
        }

        public void setStateName(String stateName) {
            mStateName = stateName;
        }

        public class City {

            @SerializedName("city_id")
            private String mCityId;
            @SerializedName("city_name")
            private String mCityName;

            public String getCityId() {
                return mCityId;
            }

            public void setCityId(String cityId) {
                mCityId = cityId;
            }

            public String getCityName() {
                return mCityName;
            }

            public void setCityName(String cityName) {
                mCityName = cityName;
            }

        }

    }


}
