package com.listapp.API_Utility.AsyncTask_Utility;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckNetwork {

    public CheckNetwork() {
        // TODO Auto-generated constructor stub
        super();
    }

    public static boolean isNetwordAvailable(Context context) {
        // TODO Auto-generated method stub
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr.getActiveNetworkInfo() != null
                && connMgr.getActiveNetworkInfo().isAvailable()
                && connMgr.getActiveNetworkInfo().isConnected();
    }
}
