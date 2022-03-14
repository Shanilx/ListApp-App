package com.listapp.Fragment;

/**
 * Created by Nivesh on 6/20/2017.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.OtpVerify.OTPResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Resend_OTP.ResendOTPResponse;
import com.listapp.Activity.NewSignInActivity;
import com.listapp.Interface.FragmentBack;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.MarshMallowPermission;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;
import com.listapp.Receiver.SmsListener;
import com.listapp.Receiver.SmsReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticateOtpFragment extends Fragment implements View.OnClickListener, FragmentBack {

    private ScrollView parentView;
    private ImageView imageView;
    private ImageView signInShowPassword;
    private LinearLayout authenticateOTPResendBtn;
    private TextView authenticateResendText;
    private TextView authenticationTimer;
    private LinearLayout forgotMobileNumberError;
    private String otp;
    private APIInterface apiInterface;
    private Call call;
    private String deviceType, deviceToken;
    public static String mobileNumber;
    private ProgressDialog progressDialog;
    private MarshMallowPermission marshMallowPermission;
    private boolean onPause = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp_authenticate, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = AppUtil.createProgressDialog(getContext());
        progressDialog.dismiss();

        parentView = view.findViewById(R.id.parentView);
        imageView = view.findViewById(R.id.imageView);
        view.findViewById(R.id.authenticateOTPSubmit).setOnClickListener(this);
        authenticateOTPResendBtn = view.findViewById(R.id.authenticateOTPResendBtn);
        authenticateResendText = view.findViewById(R.id.authenticateResendText);
        authenticationTimer = view.findViewById(R.id.authenticationTimer);
        forgotMobileNumberError = view.findViewById(R.id.forgotMobileNumberError);
        forgotMobileNumberError.setVisibility(View.INVISIBLE);

        deviceType = PreferenceConnector.readString(getContext(), PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(getContext(), PreferenceConnector.DEVICE_TOKEN, "");
        authenticateOTPResendBtn.setEnabled(false);
        authenticateOTPResendBtn.setClickable(false);

        authenticateOTPResendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTPRetro();
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);
        startTimer();

        marshMallowPermission = new MarshMallowPermission(getActivity());
        if (!marshMallowPermission.checkPermissionForReceiveSMS())
            marshMallowPermission.requestPermissionForReceiveSMS();

        try {
            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    try {
                        if (onPause) {
                            getAuthenticateOTP().setText(messageText);
                            otp = messageText;
                            if (CheckNetwork.isNetwordAvailable(getContext()))
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
        return (EditText) getView().findViewById(R.id.authenticateOTP);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.authenticateOTPSubmit:
                if (CheckNetwork.isNetwordAvailable(getContext())) {
                    if (checkData()) {
                        AppUtil.hideKeyBoard(getContext());
                        verifyOTp();
                    }
                } else
                    AppUtil.showAlert(getContext().getString(R.string.networkError), getContext());
                break;
        }
    }

    private void verifyOTp() {

        progressDialog.show();
        call = apiInterface.otpVerify(mobileNumber, otp, deviceType, deviceToken);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                OTPResponse otpResponse = (OTPResponse) response.body();
                if (otpResponse != null) {
                    String error = otpResponse.getError();
                    String msg = otpResponse.getMessage();
                    if (otpResponse.getError().equals("0")) {
                        showAlert("Your account created successfully");
                    } else
                        AppUtil.showAlert("Please enter valid OTP", getContext());

                } else
                    AppUtil.showAlert(getContext().getString(R.string.serverError), getContext());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                AppUtil.showAlert(getContext().getString(R.string.serverError), getContext());
            }
        });

    }

    private void startTimer() {

        authenticateOTPResendBtn.setEnabled(false);
        authenticateResendText.setText("Resend OTP");
        authenticateOTPResendBtn.setClickable(false);
        CountDownTimer countDownTimer = new CountDownTimer(60*1000, 1000) {
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
        call = apiInterface.resendOTp(mobileNumber, deviceType, deviceToken);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                startTimer();
                ResendOTPResponse resendOTPResponse = (ResendOTPResponse) response.body();
                if (resendOTPResponse != null) {
                    String error = resendOTPResponse.getError();
                    String msg = resendOTPResponse.getMessage();

                    if (error.equals("0")) {
                        getAuthenticateOTP().setText("");
                        AppUtil.showAlert(msg, getContext());
                    }
                } else
                    AppUtil.showAlert(getContext().getString(R.string.serverError), getContext());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                AppUtil.showAlert(getContext().getString(R.string.serverError), getContext());
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
    public boolean onBackPressed() {
        AppUtil.showAlert("You Can't Go Back in This Stage", getContext());
        return true;
    }

    @Override
    public int getBackPriority() {
        return HIGH_BACK_PRIORITY;
    }


    @Override
    public void onPause() {
        super.onPause();
        onPause = false;
    }


    private void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContext(), NewSignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                        getActivity().finishAffinity();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
