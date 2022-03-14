package com.listapp.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.OtpVerify.OTPResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Resend_OTP.ResendOTPResponse;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.MarshMallowPermission;
import com.listapp.ListAppUtil.OKayEvent;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;
import com.listapp.Receiver.MySMSBroadcastReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by syscraft on 7/7/2017.
 */

public class OTPVerifyActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MySMSBroadcastReceiver.OTPReceiveListener {


    private LinearLayout authenticateOTPResendBtn;
    private TextView authenticateResendText, otpErrorText;
    private TextView authenticationTimer;
    private LinearLayout forgotMobileNumberError;
    private String otp;
    private APIInterface apiInterface;
    private Call call;
    private String mobileNumber, deviceType, deviceToken;
    private ProgressDialog progressDialog;
    public static int forwardStatus = 0;
    private MarshMallowPermission marshMallowPermission;
    private boolean onPause = true;
    GoogleApiClient mCredentialsApiClient = null;
    MySMSBroadcastReceiver smsBroadcast = new MySMSBroadcastReceiver();

    private final int SMS_CONSENT_REQUEST = 2021;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        setUpView();
        mCredentialsApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).enableAutoManage(this, this).addApi(Auth.CREDENTIALS_API).build();
        startSMSListener();


/*
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
// SMS message.
          SmsRetrieverClient client = SmsRetriever.getClient(this );

// Starts SmsRetriever, which waits for ONE matching SMS message until timeout
// (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
// action SmsRetriever#SMS_RETRIEVED_ACTION.
        final Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task. If in a background thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
                Log.e(">>", "task" + task.getResult());

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                Log.e(">>", "taskFail" + task.getResult());

            }
        });*/
    }

    private void startSMSListener() {

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                otpErrorText.setText("Waiting for the OTP");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                otpErrorText.setText("Cannot Start SMS Retriever");
            }
        });
    }

    private void setUpView() {

        progressDialog = AppUtil.createProgressDialog(OTPVerifyActivity.this);
        progressDialog.dismiss();
        findViewById(R.id.authenticateOTPSubmit).setOnClickListener(this);
        authenticateOTPResendBtn = findViewById(R.id.authenticateOTPResendBtn);
        authenticateResendText = findViewById(R.id.authenticateResendText);
        authenticationTimer = findViewById(R.id.authenticationTimer);
        forgotMobileNumberError = findViewById(R.id.forgotMobileNumberError);
        forgotMobileNumberError.setVisibility(View.INVISIBLE);

        try {
            mobileNumber = getIntent().getStringExtra("mobileNumber");
        } catch (Exception e) {
            e.printStackTrace();
        }

        deviceType = PreferenceConnector.readString(OTPVerifyActivity.this, PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(OTPVerifyActivity.this, PreferenceConnector.DEVICE_TOKEN, "");
        authenticateOTPResendBtn.setEnabled(false);
        authenticateOTPResendBtn.setClickable(false);
        otpErrorText = findViewById(R.id.otpErrorText);
        authenticateOTPResendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTPRetro();
            }
        });
        apiInterface = APIClient.getClient().create(APIInterface.class);
        startTimer();

     /*   marshMallowPermission = new MarshMallowPermission(OTPVerifyActivity.this);
        if (!marshMallowPermission.checkPermissionForReceiveSMS())
            marshMallowPermission.requestPermissionForReceiveSMS();
*/
       /* try {
            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    try {
                        Log.d(">>OTP", messageText);
                        if(onPause) {
                            getAuthenticateOTP().setText(messageText);
                            otp = messageText;
                            if (CheckNetwork.isNetwordAvailable(OTPVerifyActivity.this))
                                verifyOTp();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        onPause = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsBroadcast.initOTPListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcast, intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcast);
    }

    private EditText getAuthenticateOTP() {
        return (EditText) findViewById(R.id.authenticateOTP);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.authenticateOTPSubmit:
                if (checkData()) {
                    AppUtil.hideKeyBoard(OTPVerifyActivity.this);
                    verifyOTp();
                }
                break;
        }
    }

    private void verifyOTp() {
        if (progressDialog != null && !isFinishing())
            progressDialog.show();

        call = apiInterface.otpVerify(mobileNumber, otp, deviceType, deviceToken);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                OTPResponse otpResponse = (OTPResponse) response.body();
                if (otpResponse != null) {
                    String error = otpResponse.getError();
                    String msg = otpResponse.getMessage();
                    if (otpResponse.getError().equals("0")) {
                        PreferenceConnector.writeString(OTPVerifyActivity.this, PreferenceConnector.USER_ID, otpResponse.getData().getUserId());
                        if (forwardStatus == 1) {
                            Intent intent = new Intent(OTPVerifyActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("mobileNumber", mobileNumber);
                            startActivity(intent);
                            finish();
                        } else if (forwardStatus == 2) {
                            PreferenceConnector.writeString(OTPVerifyActivity.this, PreferenceConnector.MOBILE_NUMBER, mobileNumber);
                            AppUtil.okayEventDialog("OTP has been verified, Complete your profile using profile option", OTPVerifyActivity.this, new OKayEvent() {
                                @Override
                                public void okayEvent(boolean b) {
                                    Intent intent = new Intent(OTPVerifyActivity.this, MedicineSearchActivity.class);
                                    startActivity(intent);
                                    MedicineSearchActivity.once = 1;
                                    finish();
                                    finishAffinity();
                                }
                            });
                        } else {
                            PreferenceConnector.writeString(OTPVerifyActivity.this, PreferenceConnector.MOBILE_NUMBER, mobileNumber);
                            Intent intent = new Intent(OTPVerifyActivity.this, MedicineSearchActivity.class);
                            startActivity(intent);
                            finish();
                            finishAffinity();
                        }
                    } else
                        AppUtil.showAlert("Please enter valid OTP", OTPVerifyActivity.this);

                } else
                    AppUtil.showAlert(getString(R.string.serverError), OTPVerifyActivity.this);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (progressDialog != null && !isFinishing())
                    progressDialog.dismiss();
                AppUtil.showAlert(getString(R.string.serverError), OTPVerifyActivity.this);
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
                if (i > 9) {
                    authenticationTimer.setText("00:" + i);
                    //         Log.e(">>", "" + i);
                } else {
                    authenticationTimer.setText("00:0" + i);
                    //          Log.e(">>", "" + i);
                }
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
        if (progressDialog != null && !isFinishing())
            progressDialog.show();
        call = apiInterface.resendOTp(mobileNumber, deviceType, deviceToken);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                startTimer();
                ResendOTPResponse resendOTPResponse = (ResendOTPResponse) response.body();
                if (resendOTPResponse != null) {
                    String error = resendOTPResponse.getError();
                    String msg = resendOTPResponse.getMessage();

                    if (error.equals("0")) {
                        Log.e(">>", "OTP" + resendOTPResponse.getData().getOtp());
                        AppUtil.showAlert(msg, OTPVerifyActivity.this);
                    } else {
                        AppUtil.showAlert(msg, OTPVerifyActivity.this);
                    }
                } else
                    AppUtil.showAlert(getString(R.string.serverError), OTPVerifyActivity.this);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                AppUtil.showAlert(getString(R.string.serverError), OTPVerifyActivity.this);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onOTPReceived(Intent consentIntent) {


        try {
            // Start activity to show consent dialog to user, activity must be started in
            // 5 minutes, otherwise you'll receive another TIMEOUT intent
            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
        } catch (ActivityNotFoundException e) {
            // Handle the exception ...
        }
    }

    @Override
    public void onOTPTimeOut() {
        Toast.makeText(OTPVerifyActivity.this, "Please enter otp manually or try again", Toast.LENGTH_LONG).show();
    }

    private int RC_HINT = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_HINT && resultCode == Activity.RESULT_OK) {

            /*You will receive user selected phone number here if selected and send it to the server for request the otp*/
            //  assert data != null;
            //   Credential credential= data.getParcelableExtra(Credential.EXTRA_KEY);
            //  Toast.makeText(this, credential.getId(), Toast.LENGTH_LONG).show();
        }

        // ...
        if (requestCode == SMS_CONSENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get SMS message content
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                // Extract one-time code from the message and complete verification
                // `sms` contains the entire text of the SMS message, so you will need
                // to parse the string.
                //String oneTimeCode = parseOneTimeCode(message); // define this function
                if (message != null) {
                    String numberOnly = message.replaceAll("[^0-9]", "");
                    Log.d("ssv", numberOnly);

                    getAuthenticateOTP().setText(numberOnly);
                    this.otp = numberOnly;
                    if (CheckNetwork.isNetwordAvailable(OTPVerifyActivity.this))
                        verifyOTp();
                }

                // send one time code to the server
            } else {
                // Consent canceled, handle the error ...
            }
        }
    }

}
