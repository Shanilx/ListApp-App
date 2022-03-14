package com.listapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Login_Response.LoginResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.ResetPassword.ResetPasswordResponse;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.OKayEvent;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nivesh on 6/20/2017.
 */

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private RelativeLayout parentView;
    private ImageView resetPasswordShowPassword;
    private LinearLayout resetPasswordPasswordError;
    //   private LinearLayout resetPasswordNewUser;
    private TextView passwordErrorText;
    private String password = null;
    private APIInterface apiInterface;
    private Call call;
    private ProgressDialog progressDialog;
    boolean showpass = false;
    boolean useVisible = true;
    String deviceType, deviceToken, mobileNumber = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reset);

        parentView = findViewById(R.id.parentView);
        resetPasswordShowPassword = findViewById(R.id.resetPasswordShowPassword);
        resetPasswordShowPassword.setOnClickListener(this);
        resetPasswordPasswordError = findViewById(R.id.resetPasswordPasswordError);
        findViewById(R.id.resetPasswordSubmit).setOnClickListener(this);
        passwordErrorText = findViewById(R.id.passwordErrorText);
        hideErrorView();
    }

    private EditText getResetPassword() {
        return (EditText) findViewById(R.id.resetPassword);
    }

    private EditText getSignInVisiblePassword() {
        return (EditText) findViewById(R.id.signInVisiblePassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resetPasswordSubmit:
                if (CheckNetwork.isNetwordAvailable(this)) {
                    if (checkData()) {
                        AppUtil.hideKeyBoard(ResetPasswordActivity.this);
                        resetPassword();
                    }
                } else
                    AppUtil.showAlert(getString(R.string.networkError), this);
                break;

            case R.id.resetPasswordShowPassword:
                if (showpass == false) {
                    resetPasswordShowPassword.setImageResource(R.drawable.ic_visibility_black_24dp);
                    getSignInVisiblePassword().setVisibility(View.VISIBLE);
                    getSignInVisiblePassword().setText(getResetPassword().getText().toString().trim());
                    getResetPassword().setVisibility(View.GONE);
                    getSignInVisiblePassword().requestFocus();
         //           AppUtil.showKeyboard(ResetPasswordActivity.this);
                    password = getSignInVisiblePassword().getText().toString().trim();
                    showpass = true;
                    useVisible = false;
                } else if (showpass == true) {
                    resetPasswordShowPassword.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    getSignInVisiblePassword().setVisibility(View.GONE);
                    getResetPassword().setVisibility(View.VISIBLE);
                    getResetPassword().setText(getSignInVisiblePassword().getText().toString());
                    getResetPassword().requestFocus();
         //           AppUtil.showKeyboard(ResetPasswordActivity.this);
                    password = getResetPassword().getText().toString().trim();
                    showpass = false;
                    useVisible = true;
                }
                break;
        }
    }

    private void resetPassword() {

        progressDialog = AppUtil.createProgressDialog(ResetPasswordActivity.this);
        if (!progressDialog.isShowing())
            progressDialog.show();

        deviceType = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TOKEN, "");
        try {
            mobileNumber = getIntent().getStringExtra("mobileNumber");
        } catch (Exception e) {
            e.printStackTrace();
        }

     //   Log.e(">>", password + "___" + mobileNumber + "___" + deviceToken);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        call = apiInterface.resetPassword(mobileNumber, password, deviceType, deviceToken);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                ResetPasswordResponse resetPasswordResponse = (ResetPasswordResponse) response.body();
                if (resetPasswordResponse != null) {
                    String error = resetPasswordResponse.getError();
                    String msg = resetPasswordResponse.getMessage();

                    if (error.equals("0")) {
                        showAlert("Your Password has been Reset Successfully");
                    } else {
                        if (msg.equalsIgnoreCase("Your session has been expired")) {
                            AppUtil.sessionLogout(msg, ResetPasswordActivity.this);
                        } else
                            AppUtil.showAlert(msg, ResetPasswordActivity.this);
                    }
                } else
                    AppUtil.showAlert(getString(R.string.serverError), ResetPasswordActivity.this);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                AppUtil.showAlert(getString(R.string.serverError), ResetPasswordActivity.this);
            }
        });
    }

    private boolean checkData() {
        if (useVisible)
            password = getResetPassword().getText().toString().trim();
        else
            password = getSignInVisiblePassword().getText().toString().trim();

        if (!password.equals("")) {
            if (password.length() > 5) {
                hideErrorView();
                return true;
            } else {
                passwordErrorText.setText("Password Must be in between 6-15 Characters");
                resetPasswordPasswordError.setVisibility(View.VISIBLE);
            }
        } else {
            passwordErrorText.setText("Enter Password");
            resetPasswordPasswordError.setVisibility(View.VISIBLE);
        }

        return false;
    }

    private void hideErrorView() {
        resetPasswordPasswordError.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.resetPasswordShowPassword) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    getResetPassword().setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case MotionEvent.ACTION_UP:
                    getResetPassword().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;
            }
            return true;
        }
        return false;
    }


    private void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this, R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            progressDialog = AppUtil.createProgressDialog(ResetPasswordActivity.this);
                            if (!progressDialog.isShowing())
                                progressDialog.show();

                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            if (deviceToken != null && !deviceToken.equals("")) {
                                PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.DEVICE_TOKEN, deviceToken);
                            } else {
                                deviceToken = PreferenceConnector.readString(ResetPasswordActivity.this, PreferenceConnector.DEVICE_TOKEN, "");
                            }

                        Call<LoginResponse> loginCall = apiInterface.login(mobileNumber, password, deviceType, deviceToken);

                            loginCall.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    if(progressDialog!=null)
                                        progressDialog.dismiss();
                                    LoginResponse loginResponse = (LoginResponse) response.body();
                                    if (loginResponse != null) {
                                        if (loginResponse.getError().equals("0")) {
                                            PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.MOBILE_NUMBER, mobileNumber);
                                            PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.FULL_NAME, loginResponse.getData().getFullName());
                                            PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.USER_ID, loginResponse.getData().getUserID());
                                            PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.OTP, loginResponse.getData().getOTP());
                                            PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.EMAIl, loginResponse.getData().getEmail());
                                            PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.AREA, loginResponse.getData().getArea());
                                            PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.ADDRESS, loginResponse.getData().getAddress());
                                            PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.CITY, loginResponse.getData().getCity());
                                            PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.STATE, loginResponse.getData().getState());
                                            PreferenceConnector.writeString(ResetPasswordActivity.this, PreferenceConnector.SHOP_NAME, loginResponse.getData().getShopName());
                                            Intent intent = new Intent(ResetPasswordActivity.this, MedicineSearchActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (loginResponse.getMessage().equals("OTP Unverified") || loginResponse.getMessage().equalsIgnoreCase("go to otp")) {
                                            Log.e(">>","OTP" + loginResponse.getData().getOTP());
                                            AppUtil.okayEventDialog("Please verify OTP first", ResetPasswordActivity.this, new OKayEvent() {
                                                @Override
                                                public void okayEvent(boolean b) {
                                                    Intent intent = new Intent(ResetPasswordActivity.this,OTPVerifyActivity.class);
                                                    intent.putExtra("mobileNumber", mobileNumber);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else
                                            AppUtil.showAlert(loginResponse.getMessage(), ResetPasswordActivity.this);
                                    } else {
                                        AppUtil.showAlert(getString(R.string.serverError), ResetPasswordActivity.this);
                                    }
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    if(progressDialog!=null)
                                        progressDialog.dismiss();
                                    AppUtil.showAlert(getString(R.string.serverError), ResetPasswordActivity.this);
                                }
                            });
//                        Intent intent = new Intent(ResetPasswordActivity.this, NewSignInActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
//                        finishAffinity();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
