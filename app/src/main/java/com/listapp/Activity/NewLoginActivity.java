package com.listapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Login_Response.LoginResponse;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nivesh on 6/20/2017.
 */

public class NewLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView signInShowPassword;
    private LinearLayout signInPasswordError;
    private TextView signInForgotPassword, passwordErrorText;
    private Intent intent;
    private String password = null;
    private APIInterface apiInterface;
    private Call loginCall;
    boolean showpass = false;
    private ProgressDialog progressDialog;
    private boolean useVisible = true;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        signInShowPassword = findViewById(R.id.signInShowPassword);
        signInShowPassword.setOnClickListener(this);
        signInPasswordError = findViewById(R.id.signInPasswordError);
        findViewById(R.id.signInSubmit).setOnClickListener(this);
        signInForgotPassword = findViewById(R.id.signInForgotPassword);
        passwordErrorText = findViewById(R.id.passwordErrorText);
        signInForgotPassword.setOnClickListener(this);
        hideErrorView();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        PreferenceConnector.writeString(this, PreferenceConnector.DEVICE_TOKEN, FirebaseInstanceId.getInstance().getToken());
        try {
            number = getIntent().getStringExtra("no");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EditText getSignInPassword() {
        return (EditText) findViewById(R.id.signInPassword);
    }

    private EditText getSignInVisiblePassword() {
        return (EditText) findViewById(R.id.signInVisiblePassword);
    }

    @Override
    public void onClick(View view) {

        AppUtil.hideKeyBoard(NewLoginActivity.this);
        switch (view.getId()) {

            case R.id.signInSubmit:
                if (CheckNetwork.isNetwordAvailable(this)) {
                    if (checkData()) {
                        doLogin();
                    }
                } else
                    AppUtil.showAlert(getString(R.string.networkError), NewLoginActivity.this);
                break;

            case R.id.signInForgotPassword:
                Intent i=new Intent(NewLoginActivity.this, ForgotPasswordActivity.class);
                i.putExtra("mob",number);
                startActivity(i);
                break;

            case R.id.signInShowPassword:
                if (showpass == false) {
                    signInShowPassword.setImageResource(R.drawable.ic_visibility_black_24dp);
                    getSignInVisiblePassword().setVisibility(View.VISIBLE);
                    getSignInVisiblePassword().setText(getSignInPassword().getText().toString());
                    getSignInPassword().setVisibility(View.GONE);
                    getSignInVisiblePassword().requestFocus();
           //         AppUtil.showKeyboard(NewLoginActivity.this);
                    password = getSignInVisiblePassword().getText().toString().trim();
                    showpass = true;
                    useVisible = false;
                } else if (showpass == true) {
                    signInShowPassword.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    getSignInVisiblePassword().setVisibility(View.GONE);
                    getSignInPassword().setVisibility(View.VISIBLE);
                    getSignInPassword().setText(getSignInVisiblePassword().getText().toString());
                    getSignInPassword().requestFocus();
        //            AppUtil.showKeyboard(NewLoginActivity.this);
                    password = getSignInPassword().getText().toString().trim();
                    showpass = false;
                    useVisible = true;
                }
                break;
        }
    }

    private void doLogin() {

        progressDialog = AppUtil.createProgressDialog(NewLoginActivity.this);
        if (!progressDialog.isShowing())
            progressDialog.show();

        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        if (deviceToken != null && !deviceToken.equals("")) {
            PreferenceConnector.writeString(this, PreferenceConnector.DEVICE_TOKEN, deviceToken);
        } else {
            deviceToken = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TOKEN, "");
        }

        loginCall = apiInterface.login(number, password, PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TYPE, "Android"),
                deviceToken);

        loginCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                LoginResponse loginResponse = (LoginResponse) response.body();
                if (loginResponse != null) {
                    if (loginResponse.getError().equals("0")) {
                        PreferenceConnector.writeString(NewLoginActivity.this, PreferenceConnector.MOBILE_NUMBER, number);
                        PreferenceConnector.writeString(NewLoginActivity.this, PreferenceConnector.FULL_NAME, loginResponse.getData().getFullName());
                        PreferenceConnector.writeString(NewLoginActivity.this, PreferenceConnector.USER_ID, loginResponse.getData().getUserID());
                        PreferenceConnector.writeString(NewLoginActivity.this, PreferenceConnector.OTP, loginResponse.getData().getOTP());
                        PreferenceConnector.writeString(NewLoginActivity.this, PreferenceConnector.EMAIl, loginResponse.getData().getEmail());
                        PreferenceConnector.writeString(NewLoginActivity.this, PreferenceConnector.AREA, loginResponse.getData().getArea());
                        PreferenceConnector.writeString(NewLoginActivity.this, PreferenceConnector.ADDRESS, loginResponse.getData().getAddress());
                        PreferenceConnector.writeString(NewLoginActivity.this, PreferenceConnector.CITY, loginResponse.getData().getCity());
                        PreferenceConnector.writeString(NewLoginActivity.this, PreferenceConnector.STATE, loginResponse.getData().getState());
                        PreferenceConnector.writeString(NewLoginActivity.this, PreferenceConnector.SHOP_NAME, loginResponse.getData().getShopName());
                        intent = new Intent(NewLoginActivity.this, MedicineSearchActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (loginResponse.getMessage().equals("OTP Unverified") || loginResponse.getMessage().equalsIgnoreCase("go to otp")) {
                        Log.e(">>","OTP" + loginResponse.getData().getOTP());
                   //    AppUtil.okayEventDialog("Please verify OTP first", NewLoginActivity.this, new OKayEvent() {
                    //       @Override
                     //      public void okayEvent(boolean b) {
                               Intent intent = new Intent(NewLoginActivity.this,OTPVerifyActivity.class);
                               intent.putExtra("mobileNumber", number);
                               startActivity(intent);
                      //     }
                      // });
                    } else
                        AppUtil.showAlert(String.valueOf(Html.fromHtml(loginResponse.getMessage())), NewLoginActivity.this);
                } else {
                    AppUtil.showAlert(getString(R.string.serverError), NewLoginActivity.this);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                AppUtil.showAlert(getString(R.string.serverError), NewLoginActivity.this);
            }
        });
    }

    private boolean checkData() {
        if (useVisible)
            password = getSignInPassword().getText().toString().trim();
        else
            password = getSignInVisiblePassword().getText().toString().trim();

        signInPasswordError.setVisibility(View.INVISIBLE);
        if (!password.equals("")) {
            if (password.length() > 5) {
                hideErrorView();
                return true;
            } else {
                passwordErrorText.setText("Password must be in between 6-15 characters");
                signInPasswordError.setVisibility(View.VISIBLE);
            }
        } else {
            passwordErrorText.setText("Enter Password");
            signInPasswordError.setVisibility(View.VISIBLE);
        }
        return false;
    }

    private void hideErrorView() {
        signInPasswordError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
