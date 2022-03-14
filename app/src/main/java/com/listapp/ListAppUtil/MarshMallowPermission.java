package com.listapp.ListAppUtil;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by Nivesh on 18 july 2017.
 */

public class MarshMallowPermission {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 4;
    public static final int READ_EXTERNAL_STORAGE = 5;
    public static final int READ_SMS = 6;
    Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermissionForStorage() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionForStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            try {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
            } catch (Exception e) {
           //     AppUtil.showAlert("Please allow permission for uploading file", activity);
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
        }
    }

    public boolean checkPermissionForReceiveSMS() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionForReceiveSMS() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECEIVE_SMS)) {
            try {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECEIVE_SMS}, READ_SMS);
            } catch (Exception e) {
                e.printStackTrace();
                AppUtil.showAlert("Please allow permission for auto verify OTP", activity);
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECEIVE_SMS}, READ_SMS);
        }

    }
}