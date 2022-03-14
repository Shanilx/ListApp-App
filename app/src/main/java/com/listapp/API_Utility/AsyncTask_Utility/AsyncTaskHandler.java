package com.listapp.API_Utility.AsyncTask_Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.listapp.ListAppUtil.AppUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Syacraft on 15-Sep-16.
 */
public class AsyncTaskHandler extends AsyncTask<String, String, String> {

    private final static String TAG = AsyncTaskHandler.class.getSimpleName();
    private ProgressDialog dialog;
    private Context context;
    private String url;
    private Map<String, String> params = new HashMap<String, String>();
    private String reqMethod;
    ResponseHandler responseHandler;

    public AsyncTaskHandler(Context context, String url, Map<String, String> params, String reqMethod, ResponseHandler responseHandler) {
        this.context = context;
        this.url = url;
        this.params = params;
        this.reqMethod = reqMethod;
        this.responseHandler = responseHandler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (dialog == null) {
            dialog = AppUtil.createProgressDialog(context);
            dialog.show();
        } else {
            dialog.show();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = null;
        switch (reqMethod) {
            case "GET":
        //        Log.e(TAG + ">>GET>>", "URL>>" + url);
                try {
                    HttpUtility.sendGetRequest(url);
                    response = HttpUtility.readMultipleLinesRespone();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                HttpUtility.disconnect();
                break;
            case "POST":
         //       Log.e(TAG + ">>POST>>", "URL>>" + url);
         //       Log.e(TAG + ">>POST>>", "PARAMS>>" + params.toString());
                try {
                    HttpUtility.sendPostRequest(url, params);
                    response = HttpUtility.readMultipleLinesRespone();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                HttpUtility.disconnect();
                break;
            default:
        }
        return response;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dialog.dismiss();
        try {
     //       Log.e(TAG + ">>RESPONSE", ">>" + result);
            responseHandler.handleResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


