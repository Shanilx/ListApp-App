package com.listapp.ListAppUtil;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreferenceConnector {

    public static final String PREF_NAME = "ListApp";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String IS_LOGIN = "isLogin";
    public static final String MOBILE_NUMBER = "mobilenumber";
    public static final String MOBILE_ID = "mobileID";
    public static final String DEVICE_TOKEN = "deviceToken";
    public static final String DEVICE_TYPE = "deviceType";
    public static final String FULL_NAME = "fullName";
    public static final String PASSWORD = "password";
    public static final String USER_TYPE = "userType";
    public static final String OTP = "otp";
    public static final String AREA = "AREA";
    public static final String ADDRESS = "ADDRESS";
    public static final String CITY = "CITY";
    public static final String STATE = "STATE";
    public static final String SHOP_NAME = "shopName";
    public static final String CONTACT_NUMBER = "contactNumber";
    public static final String TIN_NUMBER = "tinNumber";
    public static final String ESTD_YEAR = "estdNumber";
    public static final String DL_NUMBER = "dlNumber";
    public static final String ABOUT_US_CONTENT = "aboutContent";
    public static final String USER_ID = "userId";
    public static final String EMAIl = "EMAIL";
    public static final String CITY_ID = "cityID";
    public static final String STATE_ID = "stateID";
    public static final String CONTACT_PERSON = "contactPerson";
    public static final String CONTACTNAMES = "contactNames";
    public static final String CONTACTNUMBERS = "contactNumbers";
    public static final String SUPPLIERCITY ="suppliercity";
    public static final String CHECK_UPDATE = "checkupdate";
    public static String User_CITY = "usercity";
    public static String User_CITY_ID="usercityid";


    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void writeArraylist(Context context, String key,
                                      List<String> arryid) {
        Set<String> set = new HashSet<String>(arryid);
        getEditor(context).putStringSet(key, set).commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static List<String> readArraylist(Context context, String key) {
        Set<String> stock_Set = getPreferences(context).getStringSet(key,
                new HashSet<String>());
        List<String> demo = new ArrayList<String>(stock_Set);
        return demo;
    }

    public static void remove(Context context, String key) {
        getEditor(context).remove(key).commit();
    }

    public static void clear(Context context) {
        getEditor(context).clear().commit();
    }

}
