package com.listapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Splash_Response.AuthenticationResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Splash_Response.StateCityResponse;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.Local_Database.DataBaseHandler;
import com.listapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private Intent intent;
    private APIInterface apiInterface;
    private Call getStateCityList, checkRegistration;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressDialog = AppUtil.createProgressDialog(SplashActivity.this);
        if (!progressDialog.isShowing())
            progressDialog.show();

        String android_id = null;
        PreferenceConnector.writeString(this,PreferenceConnector.CHECK_UPDATE,"true");
        try {
            android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            PreferenceConnector.writeString(this, PreferenceConnector.MOBILE_ID, android_id);
            PreferenceConnector.writeString(this, PreferenceConnector.DEVICE_TYPE, "Android");
            if(PreferenceConnector.readString(this,PreferenceConnector.CITY,"").contains("0")){
                PreferenceConnector.writeString(this,PreferenceConnector.CITY,"Indore");
                PreferenceConnector.writeString(this,PreferenceConnector.CITY_ID,"2229");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        apiInterface = APIClient.getClient().create(APIInterface.class);

        if (CheckNetwork.isNetwordAvailable(this)) {
  //          boolean firstTime = PreferenceConnector.readBoolean(this, "firstTime", false);
  //          if (!firstTime) {
  //              getStateCityList = apiInterface.getStateCity();
  //              getStateCityList();
   //         } else
                checkRegistration();
        } else
            retryDialog();

    }

    private void checkRegistration() {
        String mobileNumber, mobileID, deviceToken, deviceType;
        mobileNumber = PreferenceConnector.readString(this, PreferenceConnector.MOBILE_NUMBER, "");
        mobileID = PreferenceConnector.readString(this, PreferenceConnector.MOBILE_ID, "");
        deviceToken = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TOKEN, "");

        deviceType = "Android";
     //   Log.e(">>", mobileNumber + "___" + mobileID + "__" + deviceToken + "__" + deviceType);
        checkRegistration = apiInterface.checkRegistration(mobileNumber, mobileID, deviceType, deviceToken);

        checkRegistration.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                stopProgress();
                AuthenticationResponse authenticationResponse = (AuthenticationResponse) response.body();
                if (authenticationResponse != null) {
         //           Log.e(">>", authenticationResponse.getError() + "___" + authenticationResponse.getMessage() + "___");

                    if (authenticationResponse.getError().equals("0")) {

          //            loadAd();
         //               Log.e("<<>>Splash Check Registration Response", authenticationResponse.getError());
                        intent = new Intent(SplashActivity.this, MedicineSearchActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        gotoSignIn();
                    }
                } else {
         //           Log.e("<<>>Splash Check Registration Response", authenticationResponse.getError());
                    showAlert(getString(R.string.serverError));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                stopProgress();
                showAlert(getString(R.string.serverError));
            }
        });
    }


    private void getStateCityList() {

        getStateCityList.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                final StateCityResponse stateCityResponse = (StateCityResponse) response.body();
                if (stateCityResponse != null) {
          //          Log.e("<<>>State City Response", stateCityResponse.getError());

                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {

                            DataBaseHandler dataBaseHandler = new DataBaseHandler(SplashActivity.this);
                            dataBaseHandler.insertStateCityData(stateCityResponse);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            PreferenceConnector.writeBoolean(SplashActivity.this, "firstTime", true);
                            checkRegistration();
                        }
                    }.execute();


                } else {
                    stopProgress();
                    showAlert(getString(R.string.serverError));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                stopProgress();
                showAlert(getString(R.string.serverError));
            }
        });
    }

    private void gotoSignIn() {
        intent = new Intent(SplashActivity.this, NewSignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        gotoSignIn();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void retryDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(SplashActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Alert");
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.networkError));
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CheckNetwork.isNetwordAvailable(SplashActivity.this)) {
                    boolean firstTime = PreferenceConnector.readBoolean(SplashActivity.this, "firstTime", false);
//                    if (!firstTime) {
//                        PreferenceConnector.writeString(SplashActivity.this, PreferenceConnector.DEVICE_TYPE, "Android");
//                        getStateCityList = apiInterface.getStateCity();
//                        getStateCityList();
//                    } else
                        checkRegistration();
                    intent = new Intent(SplashActivity.this, MedicineSearchActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    dialog.dismiss();
                  //  retryDialog();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity.this.finish();
                dialog.dismiss();
            }
        });
        builder.show();
    }
}