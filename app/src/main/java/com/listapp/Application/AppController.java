package com.listapp.Application;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.ListAppUtil.AppSignatureHelper;

/**
 * Created by Nivesh on 6/20/2017.
 */

public class AppController extends MultiDexApplication  {

    public static final String TAG = AppController.class
            .getSimpleName();

    private static AppController mInstance;
    private static APIInterface apiInterface;


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mInstance = this;
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();

   }



    public static synchronized AppController getInstance() {
        return mInstance;
    }

}
