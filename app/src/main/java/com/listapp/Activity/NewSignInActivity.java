package com.listapp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.CheckMobileNumber.CheckNumberResponse;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nivesh on 6/20/2017.
 */

public class NewSignInActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout signInMobileNumberError;
    private TextView numberErrorText;
    private String number;
    private APIInterface apiInterface;
    private Call checkNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sign);

        signInMobileNumberError = findViewById(R.id.signInMobileNumberError);
        findViewById(R.id.signInSubmit).setOnClickListener(this);
        numberErrorText = findViewById(R.id.numberErrorText);
        hideErrorView();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        PreferenceConnector.writeString(this, PreferenceConnector.DEVICE_TOKEN, FirebaseInstanceId.getInstance().getToken());
    }

    private EditText getSignInMobileNumber() {
        return (EditText) findViewById(R.id.signInMobileNumber);
    }

    @Override
    public void onClick(View view) {

        AppUtil.hideKeyBoard(NewSignInActivity.this);
        switch (view.getId()) {

            case R.id.signInSubmit:
                if (CheckNetwork.isNetwordAvailable(this)) {
                    if (checkData()) {
                        doLogin();
                    }
                } else
                    AppUtil.showAlert(getString(R.string.networkError), NewSignInActivity.this);
                break;
        }
    }

    private void doLogin() {

        progressDialog = AppUtil.createProgressDialog(NewSignInActivity.this);
        if (!progressDialog.isShowing())
            progressDialog.show();

        checkNumber = apiInterface.verifyMobile(number);
        checkNumber.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                progressDialog.dismiss();

                if (response != null) {
                    CheckNumberResponse checkNumberResponse = (CheckNumberResponse) response.body();
                    if (checkNumberResponse != null) {
                        if (checkNumberResponse.getError().equals("0")) {
                            //  showAlert("",1);
                            Intent intent = new Intent(NewSignInActivity.this, NewLoginActivity.class);
                            intent.putExtra("no", number);
                            startActivity(intent);
                        } else {
                            showAlert("This mobile number is not registered with us. Please register", 2);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                AppUtil.showAlert(getString(R.string.serverError), NewSignInActivity.this);
            }
        });
    }

    private boolean checkData() {
        number = getSignInMobileNumber().getText().toString().trim();
        if (!number.equals("")) {
            if (number.length() == 10) {
                signInMobileNumberError.setVisibility(View.INVISIBLE);
                return true;
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
    }

    private void showAlert(String msg, final int i) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(NewSignInActivity.this, R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (i == 1) {
                            Intent intent = new Intent(NewSignInActivity.this, OTPVerifyActivity.class);
                            intent.putExtra("mobileNumber", number);
                            startActivity(intent);
                        } else if (i == 2) {
                            Intent intent = new Intent(NewSignInActivity.this, SignupActivity.class);
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
}
