package com.listapp.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.MobileNumberChange.NewOTPResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.MobileNumberChange.NewResendOTPResponse;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.MarshMallowPermission;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;
import com.listapp.Receiver.SmsListener;
import com.listapp.Receiver.SmsReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by syscraft on 7/7/2017.
 */

public class MobileNumberChangeOTPVerifyActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout authenticateOTPResendBtn, layoutHeader;
    private TextView authenticateResendText, otpErrorText;
    private TextView authenticationTimer;
    private LinearLayout forgotMobileNumberError;
    private String otp;
    private APIInterface apiInterface;
    private Call call;
    private String mobileNumber, deviceType, deviceToken;
    private ProgressDialog progressDialog;
    private String userId;
    private MarshMallowPermission marshMallowPermission;
    private boolean onPause = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        setUpView();
    }

    private void setUpView() {

        progressDialog = AppUtil.createProgressDialog(MobileNumberChangeOTPVerifyActivity.this);
        progressDialog.dismiss();
        layoutHeader = findViewById(R.id.layoutHeader);
        layoutHeader.setVisibility(View.GONE);
        findViewById(R.id.authenticateOTPSubmit).setOnClickListener(this);
        authenticateOTPResendBtn = findViewById(R.id.authenticateOTPResendBtn);
        authenticateResendText = findViewById(R.id.authenticateResendText);
        authenticationTimer = findViewById(R.id.authenticationTimer);
        forgotMobileNumberError = findViewById(R.id.forgotMobileNumberError);
        forgotMobileNumberError.setVisibility(View.INVISIBLE);

        try {
            mobileNumber = getIntent().getStringExtra("newMobileNumber");
        } catch (Exception e) {
            e.printStackTrace();
        }

        authenticateOTPResendBtn.setEnabled(false);
        authenticateOTPResendBtn.setClickable(false);
        otpErrorText = findViewById(R.id.otpErrorText);

        userId = PreferenceConnector.readString(MobileNumberChangeOTPVerifyActivity.this, PreferenceConnector.USER_ID, "");
        deviceType = PreferenceConnector.readString(MobileNumberChangeOTPVerifyActivity.this, PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(MobileNumberChangeOTPVerifyActivity.this, PreferenceConnector.DEVICE_TOKEN, "");

        authenticateOTPResendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTPRetro();
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);
        startTimer();

        marshMallowPermission = new MarshMallowPermission(MobileNumberChangeOTPVerifyActivity.this);
        if (!marshMallowPermission.checkPermissionForReceiveSMS())
            marshMallowPermission.requestPermissionForReceiveSMS();

        try {
            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    try {
                        //           Log.d("Text", messageText);
                        if (onPause) {
                            getAuthenticateOTP().setText(messageText);
                            otp = messageText;
                            if (CheckNetwork.isNetwordAvailable(MobileNumberChangeOTPVerifyActivity.this))
                                verifyOTp();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        onPause = true;
    }

    private EditText getAuthenticateOTP() {
        return (EditText) findViewById(R.id.authenticateOTP);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.authenticateOTPSubmit:
                if (checkData()) {
                    verifyOTp();
                }
                break;
        }
    }

    private void verifyOTp() {

        String old = PreferenceConnector.readString(this, PreferenceConnector.MOBILE_NUMBER, "");
        progressDialog.show();
        call = apiInterface.otpVerifyNew(old, mobileNumber, otp, deviceType, deviceToken, userId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                NewOTPResponse otpResponse = (NewOTPResponse) response.body();
                if (otpResponse != null) {
                    String error = otpResponse.getError();
                    String msg = otpResponse.getMessage();
                    if (error.equals("0")) {
                        AppUtil.sessionLogout("Your Mobile Number has been updated successfully. Please login with new Mobile Number", MobileNumberChangeOTPVerifyActivity.this);
                    } else {
                        if (msg.equalsIgnoreCase("Your session has been expired")) {
                            AppUtil.sessionLogout(msg, MobileNumberChangeOTPVerifyActivity.this);
                        } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                            AppUtil.sessionLogout(msg, MobileNumberChangeOTPVerifyActivity.this);
                        else
                            AppUtil.showAlert(msg, MobileNumberChangeOTPVerifyActivity.this);
                    }

                } else
                    AppUtil.showAlert(getString(R.string.serverError), MobileNumberChangeOTPVerifyActivity.this);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                AppUtil.showAlert(getString(R.string.serverError), MobileNumberChangeOTPVerifyActivity.this);
            }
        });

    }

    private void startTimer() {

        authenticateOTPResendBtn.setEnabled(false);
        authenticateResendText.setText("Resend OTP");
        authenticateOTPResendBtn.setClickable(false);
        CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                int i = (int) (millisUntilFinished / 1000);

                if (i > 9)
                    authenticationTimer.setText("00:" + i);
                else
                    authenticationTimer.setText("00:0" + i);
            }

            @Override
            public void onFinish() {
                authenticateResendText.setText(AppUtil.getHalfBoldString("Resend OTP"));
                authenticationTimer.setText("00:00");
                authenticateOTPResendBtn.setEnabled(true);
                authenticateOTPResendBtn.setClickable(true);
            }
        };
        countDownTimer.start();
    }

    private void resendOTPRetro() {

        progressDialog.show();
        call = apiInterface.sendOtpForNew(PreferenceConnector.readString(this, PreferenceConnector.MOBILE_NUMBER, ""), mobileNumber, deviceType, deviceToken, userId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                startTimer();
                NewResendOTPResponse resendOTPResponse = (NewResendOTPResponse) response.body();
                if (resendOTPResponse != null) {
                    String error = resendOTPResponse.getError();
                    String msg = resendOTPResponse.getMessage();
                    //       Log.e(">>", error + "____" + msg + "_____");
                    if (error.equals("0")) {
                        AppUtil.showAlert(msg, MobileNumberChangeOTPVerifyActivity.this);
                        //       Log.e(">>", error + "____" + msg + "_____" + resendOTPResponse.getData().getOTP());
                    } else {
                        if (msg.equalsIgnoreCase("Your session has been expired")) {
                            AppUtil.sessionLogout(msg, MobileNumberChangeOTPVerifyActivity.this);
                        } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                            AppUtil.sessionLogout(msg, MobileNumberChangeOTPVerifyActivity.this);
                        else
                            AppUtil.showAlert(msg, MobileNumberChangeOTPVerifyActivity.this);
                    }
                } else
                    AppUtil.showAlert(getString(R.string.serverError), MobileNumberChangeOTPVerifyActivity.this);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                AppUtil.showAlert(getString(R.string.serverError), MobileNumberChangeOTPVerifyActivity.this);
                startTimer();
            }
        });
    }

    private boolean checkData() {

        otp = getAuthenticateOTP().getText().toString().trim();
        if (otp != null && !otp.equals("")) {
            forgotMobileNumberError.setVisibility(View.INVISIBLE);
            return true;

        } else
            forgotMobileNumberError.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPause = false;
    }
}
