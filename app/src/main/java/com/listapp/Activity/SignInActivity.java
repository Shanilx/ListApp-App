package com.listapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Login_Response.LoginResponse;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.MarshMallowPermission;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nivesh on 6/20/2017.
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout signInMobileNumberError;
    private ImageView signInShowPassword;
    private LinearLayout signInPasswordError;
    private TextView signInForgotPassword, passwordErrorText, numberErrorText;
    private TextView signInNewUser;
    private Intent intent;
    private String password = null;
    private String number;
    private APIInterface apiInterface;
    private Call loginCall;
    boolean showpass = false;
    private ProgressDialog progressDialog;
    private boolean useVisible = true;
    private MarshMallowPermission marshMallowPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        signInMobileNumberError = findViewById(R.id.signInMobileNumberError);
        signInShowPassword = findViewById(R.id.signInShowPassword);
        signInShowPassword.setOnClickListener(this);
        signInPasswordError = findViewById(R.id.signInPasswordError);
        findViewById(R.id.signInSubmit).setOnClickListener(this);
        signInForgotPassword = findViewById(R.id.signInForgotPassword);
        signInNewUser = findViewById(R.id.signInNewUser);
        signInNewUser.setOnClickListener(this);
        passwordErrorText = findViewById(R.id.passwordErrorText);
        numberErrorText = findViewById(R.id.numberErrorText);
        signInForgotPassword.setOnClickListener(this);
        hideErrorView();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        PreferenceConnector.writeString(this, PreferenceConnector.DEVICE_TOKEN, FirebaseInstanceId.getInstance().getToken());
        marshMallowPermission = new MarshMallowPermission(SignInActivity.this);



//        getSignInMobileNumber().setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
    }

    private EditText getSignInMobileNumber() {
        return (EditText) findViewById(R.id.signInMobileNumber);
    }

    private EditText getSignInPassword() {
        return (EditText) findViewById(R.id.signInPassword);
    }

    private EditText getSignInVisiblePassword() {
        return (EditText) findViewById(R.id.signInVisiblePassword);
    }

    @Override
    public void onClick(View view) {

        AppUtil.hideKeyBoard(SignInActivity.this);
        switch (view.getId()) {

            case R.id.signInSubmit:
                if (CheckNetwork.isNetwordAvailable(this)) {
                    if (checkData()) {
                        doLogin();
                    }
                } else
                    AppUtil.showAlert(getString(R.string.networkError), SignInActivity.this);
                break;

            case R.id.signInForgotPassword:

                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
                break;

            case R.id.signInNewUser:
                startActivity(new Intent(SignInActivity.this, SignupActivity.class));
                break;

            case R.id.signInShowPassword:
                if (showpass == false) {
                    signInShowPassword.setImageResource(R.drawable.ic_visibility_black_24dp);
                    getSignInVisiblePassword().setVisibility(View.VISIBLE);
                    getSignInVisiblePassword().setText(getSignInPassword().getText().toString());
                    getSignInPassword().setVisibility(View.GONE);
                    getSignInVisiblePassword().requestFocus();
                    AppUtil.showKeyboard(SignInActivity.this);
                    password = getSignInVisiblePassword().getText().toString().trim();
                    showpass = true;
                    useVisible = false;
                } else if (showpass == true) {
                    signInShowPassword.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    getSignInVisiblePassword().setVisibility(View.GONE);
                    getSignInPassword().setVisibility(View.VISIBLE);
                    getSignInPassword().setText(getSignInVisiblePassword().getText().toString());
                    getSignInPassword().requestFocus();
                    AppUtil.showKeyboard(SignInActivity.this);
                    password = getSignInPassword().getText().toString().trim();
                    showpass = false;
                    useVisible = true;
                }
                break;
        }
    }

    private void doLogin() {

        progressDialog = AppUtil.createProgressDialog(SignInActivity.this);
        if (!progressDialog.isShowing())
            progressDialog.show();

        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        if (deviceToken != null && !deviceToken.equals("")) {
            PreferenceConnector.writeString(this, PreferenceConnector.DEVICE_TOKEN, deviceToken);
        } else {
            deviceToken = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TOKEN, "");
        }
        //    PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.MOBILE_NUMBER, number);

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
//                        Log.e(">>", loginResponse.getError() + "_____" + loginResponse.getMessage());
                        PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.MOBILE_NUMBER, number);
                        PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.FULL_NAME, loginResponse.getData().getFullName());
                        PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.USER_ID, loginResponse.getData().getUserID());
                        PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.OTP, loginResponse.getData().getOTP());
                        PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.EMAIl, loginResponse.getData().getEmail());
                        PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.AREA, loginResponse.getData().getArea());
                        PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.ADDRESS, loginResponse.getData().getAddress());
                        PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.CITY, loginResponse.getData().getCity());
                        PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.STATE, loginResponse.getData().getState());
                        PreferenceConnector.writeString(SignInActivity.this, PreferenceConnector.SHOP_NAME, loginResponse.getData().getShopName());
                        intent = new Intent(SignInActivity.this, MedicineSearchActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (loginResponse.getMessage().equals("OTP Unverified") || loginResponse.getMessage().equalsIgnoreCase("go to otp")) {
                        Log.e(">>","OTP" + loginResponse.getData().getOTP());
                        showAlert("Please verify OTP first", 1);
                    } else if (loginResponse.getMessage().equals("Mobile number not registered with us")) {
                        showAlert("This mobile number is not registered with us. Please register", 2);
                    } else
                        AppUtil.showAlert(loginResponse.getMessage(), SignInActivity.this);

                } else {
                    AppUtil.showAlert(getString(R.string.serverError), SignInActivity.this);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                    AppUtil.showAlert(getString(R.string.serverError), SignInActivity.this);
            }
        });
    }

    private boolean checkData() {
        number = getSignInMobileNumber().getText().toString().trim();
        if (useVisible)
            password = getSignInPassword().getText().toString().trim();
        else
            password = getSignInVisiblePassword().getText().toString().trim();

        if (!number.equals("")) {
            if (number.length() == 10) {
                signInMobileNumberError.setVisibility(View.INVISIBLE);
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
            } else {
                numberErrorText.setText("Mobile Number must be of 10 digits");
                signInMobileNumberError.setVisibility(View.VISIBLE);
            }
        } else {
            numberErrorText.setText("Enter Mobile Number");
            signInMobileNumberError.setVisibility(View.VISIBLE);
        }


        return false;
    }

    private void hideErrorView() {
        signInMobileNumberError.setVisibility(View.INVISIBLE);
        signInPasswordError.setVisibility(View.INVISIBLE);
    }

    private void showAlert(String msg, final int i) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this, R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (i == 1) {
                            Intent intent = new Intent(SignInActivity.this, OTPVerifyActivity.class);
                            intent.putExtra("mobileNumber", number);
                            startActivity(intent);
                        } else if (i == 2) {
                            Intent intent = new Intent(SignInActivity.this, SignupActivity.class);
                            intent.putExtra("mobileNumber", number);
                            startActivity(intent);
                        }

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*@Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.signInShowPassword) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    getSignInPassword().setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case MotionEvent.ACTION_UP:
                    getSignInPassword().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;
            }
            return true;
        }
        return false;
    }*/
}
