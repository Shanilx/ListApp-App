package com.listapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Resend_OTP.ResendOTPResponse;
import com.listapp.Interface.BackFragmentHelper;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nivesh on 6/20/2017.
 */

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout forgotMobileNumberError;
    private TextView forgotNewUser;
    private static RelativeLayout parentView;
    private TextView passwordErrorText;
    private String number;
    private ProgressDialog progressDialog;
    private Call call;
    private APIInterface apiInterface;
    private EditText mobnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mobnum= findViewById(R.id.forgotMobileNumber);

        forgotMobileNumberError = findViewById(R.id.forgotMobileNumberError);
        findViewById(R.id.forgotSendOTP).setOnClickListener(this);
        forgotNewUser = findViewById(R.id.forgotNewUser);
        forgotNewUser.setOnClickListener(this);
        parentView = findViewById(R.id.parentView);
        passwordErrorText = findViewById(R.id.passwordErrorText);
        forgotMobileNumberError.setVisibility(View.INVISIBLE);

        try {
            Bundle b = getIntent().getExtras();

            number =b.getString("mob");
            mobnum.setText(number);


        }catch(Exception e){}
    }

    private EditText getForgotMobileNumber() {
        return (EditText) findViewById(R.id.forgotMobileNumber);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgotSendOTP:
                if (CheckNetwork.isNetwordAvailable(this)) {
                    if (checkData()) {
                        AppUtil.hideKeyBoard(ForgotPasswordActivity.this);
                        sendOtp();
                    }
                } else
                    AppUtil.showAlert(getString(R.string.networkError), this);
                break;

            case R.id.forgotNewUser:
                startActivity(new Intent(ForgotPasswordActivity.this, SignupActivity.class));
                finish();
                break;
        }
    }

    private void sendOtp() {

        progressDialog = AppUtil.createProgressDialog(this);
        if (!progressDialog.isShowing())
            progressDialog.show();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        String deviceType, deviceToken;
        deviceType = PreferenceConnector.readString(this,PreferenceConnector.DEVICE_TYPE,"Android");
        deviceToken = PreferenceConnector.readString(this,PreferenceConnector.DEVICE_TOKEN,"");
   //     Log.e(">>",number+"__"+deviceToken);
        call = apiInterface.resendOTp(number, deviceType, deviceToken);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                ResendOTPResponse resendOTPResponse = (ResendOTPResponse) response.body();
                if (resendOTPResponse != null) {
                    String error = resendOTPResponse.getError();
                    String msg = resendOTPResponse.getMessage();
      //              Log.e(">>", error + "____" + msg);
                    if (error.equals("0")) {
       //                 Log.e(">>", error + "____" + msg + "_____" + resendOTPResponse.getData().getOtp());
                        OTPVerifyActivity.forwardStatus = 1;
                        Intent intent = new Intent(ForgotPasswordActivity.this, OTPVerifyActivity.class);
                        intent.putExtra("mobileNumber",number);
                        startActivity(intent);
                    }
                    else
                        AppUtil.showAlert(msg,ForgotPasswordActivity.this);
                } else
                    AppUtil.showAlert(getString(R.string.serverError), ForgotPasswordActivity.this);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                AppUtil.showAlert(getString(R.string.serverError), ForgotPasswordActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!BackFragmentHelper.fireOnBackPressedEvent(this)) {
            if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
                finish();
            }
        }
    }

    private boolean checkData() {
        number = getForgotMobileNumber().getText().toString().trim();

        if (!number.equals("")) {
            if (number.length() == 10) {
                forgotMobileNumberError.setVisibility(View.INVISIBLE);
             //   PreferenceConnector.writeString(this, PreferenceConnector.MOBILE_NUMBER,number);
                return true;
            } else {
                passwordErrorText.setText("Mobile number must be of 10 Digits");
                forgotMobileNumberError.setVisibility(View.VISIBLE);
            }
        } else {
            passwordErrorText.setText("Invalid Mobile Number");
            forgotMobileNumberError.setVisibility(View.VISIBLE);
        }
        return false;
    }
}
