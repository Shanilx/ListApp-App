package com.listapp.API_Utility.AsyncTask_Utility;

/**
 * Created by Syacraft on 01-Oct-16.
 */
public interface UrlConfig {

     String BASE_URL = "http://listapp.in/dev/api/Login/";

     String signUp = BASE_URL+"signUp";
     String login = BASE_URL+"login";
     String checkDeviceReg = BASE_URL+"checkDeviceReg";
     String StateCity = BASE_URL+"StateCity";
     String getTermsCondition = BASE_URL+"getTermsCondition";

     String OTP_VERIFY = BASE_URL+"otpVerify";
     String RESEND_OTP = BASE_URL+"resendOTP";

}

