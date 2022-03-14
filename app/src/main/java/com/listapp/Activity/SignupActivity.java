package com.listapp.Activity;

/**
 * Created by Nivesh on 6/21/2017.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.City.Cities;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SignUp.SignUpResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.TermsAndCondition.TermsAndConditionResponse;
import com.listapp.Interface.BackFragmentHelper;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView textView, signupAlreadyRegister, passwordErrorText, numberErrorText;
    private LinearLayout retailerShopNameError;
    private LinearLayout signupMobileNumberError;
    private ImageView signInShowPassword;
    private LinearLayout signupPasswordError;
    private LinearLayout signupFarmacyError;
    private TextView signupTermsCondition;
    private static RelativeLayout signupParentView;
    private static FrameLayout signupFragmentView;
    private APIInterface apiInterface;
    private Call call, signUpCall;
    boolean showpass;
    private String termsContent = null;
    private List<String> userTypeList;
    private ProgressDialog progressDialog;
    private String userType = null;
    private String strShopName, strMobileNumber, strPassword = null;
    private int intUserType;
    private boolean useVisible = true;
    private EditText getSignupShopName;
    private List<String> stateList;
    private String stateName;
    private List<String> cityList;
    private String cityName;
    private AutoCompleteTextView retailerState;
    private LinearLayout retailerStateError;
    private AutoCompleteTextView retailerCity;
    private LinearLayout retailerCityError;
    private String city_id;


    void editTextEventHandler() {
        retailerCity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String state = retailerState.getText().toString();
                Log.d("ssv", state);
                if (state.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please select state first", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Call<Cities> citiesCall = apiInterface.getCityByState(state);
                citiesCall.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.d("ssv", response.body() + "");
                        retailerCity.setOnItemSelectedListener(SignupActivity.this);
                        Cities cities = (Cities) response.body();
                        if (cities.getmData() != null) {
                            cityList.clear();
                            cityList.addAll(cities.getmData());
                            ArrayAdapter cityAdapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_spinner_item, cityList);
                            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            retailerCity.setAdapter(cityAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        cityList = new ArrayList<>();
        stateList = new ArrayList<>();
        setUpView();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        if (CheckNetwork.isNetwordAvailable(SignupActivity.this))
            getTermsAndCondition();
        else
            AppUtil.showAlert(getString(R.string.networkError), SignupActivity.this);



        Call<Cities> statesCall = apiInterface.getstates();
        statesCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Cities states = (Cities) response.body();
                stateList.addAll(states.getmData());
                ArrayAdapter stateAdapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_spinner_item, stateList);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                retailerState.setAdapter(stateAdapter);
                //     retailerState.setSelection(19);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getBaseContext(), "Failed to fetch states", Toast.LENGTH_SHORT).show();

            }
        });
        editTextEventHandler();
      /*  try {
            retailerCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = retailerState.getText().toString();
                    Log.d("ssv", state);

                    Call<Cities> citiesCall = apiInterface.getCityByState(state);
                    citiesCall.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Log.d("ssv", response.body() + "");
                            retailerCity.setOnItemSelectedListener(SignupActivity.this);
                            Cities cities = (Cities) response.body();
                            if (cities.getmData() != null) {
                                cityList.clear();
                                cityList.addAll(cities.getmData());
                                ArrayAdapter cityAdapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_spinner_item, cityList);
                                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                retailerCity.setAdapter(cityAdapter);
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            });
        } catch (Exception e) {
        }*/
    }

    private void setUpView() {

        retailerCity = findViewById(R.id.city_id);
        retailerState = findViewById(R.id.retailerState);
        textView = findViewById(R.id.textView);
        passwordErrorText = findViewById(R.id.passwordErrorText);
        retailerShopNameError = findViewById(R.id.retailerShopNameError);
        signupMobileNumberError = findViewById(R.id.signupMobileNumberError);
        signInShowPassword = findViewById(R.id.signInShowPassword);
        signInShowPassword.setOnClickListener(this);
        signupPasswordError = findViewById(R.id.signupPasswordError);
        signupFarmacyError = findViewById(R.id.signupFarmacyError);
        signupTermsCondition = findViewById(R.id.signupTermsCondition);
        signupFragmentView = findViewById(R.id.signupFragmentView);
        signupParentView = findViewById(R.id.signupParentView);
        signupAlreadyRegister = findViewById(R.id.signupAlreadyRegister);
        numberErrorText = findViewById(R.id.numberErrorText);
        signupAlreadyRegister.setOnClickListener(this);
        signupTermsCondition.setOnClickListener(this);
        findViewById(R.id.signupNext).setOnClickListener(this);
        hideAllErrorMsg();
        userTypeList = new ArrayList<>();
        userTypeList.add("Select User type");
        userTypeList.add("Pharmacy Retailer");
        userTypeList.add("Supplier");
        userTypeList.add("Company");
        userTypeList.add("Other");

        getSignupShopName = findViewById(R.id.retailerShopName);

        ArrayAdapter userTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypeList);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSignupFarmacy().setAdapter(userTypeAdapter);
        getSignupFarmacy().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    userType = null;
                } else {
                    userType = userTypeList.get(position);
                    intUserType = position;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            getSignupMobileNumber().setText(getIntent().getStringExtra("mobileNumber"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EditText getSignupMobileNumber() {
        return (EditText) findViewById(R.id.signupMobileNumber);
    }

    private EditText getSignupPassword() {
        return (EditText) findViewById(R.id.signupPassword);
    }

    private EditText getSignupVisiblePassword() {
        return (EditText) findViewById(R.id.signupVisiblePassword);
    }

    private Spinner getSignupFarmacy() {
        return (Spinner) findViewById(R.id.signupFarmacy);
    }

    private CheckBox getSignupCheckBox() {
        return (CheckBox) findViewById(R.id.signupCheckBox);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signupNext:
                if (CheckNetwork.isNetwordAvailable(SignupActivity.this)) {
                    if (checkInputData()) {
                        if (getSignupCheckBox().isChecked()) {
                            //                 hideSignupParentView();
                            hideAllErrorMsg();
                            //   getSupportFragmentManager().beginTransaction().replace(R.id.signupFragmentView, new RetailerSignupFragment(), "").addToBackStack("").commit();
                            doSignUp();
                        } else {
                            AppUtil.showAlert("Please accept Terms & Conditions", SignupActivity.this);
                        }
                    }
                } else
                    AppUtil.showAlert(getString(R.string.networkError), SignupActivity.this);
                break;

            case R.id.signupTermsCondition:
                termsAndConditionsDialog();
                break;

            case R.id.signupAlreadyRegister:
                Intent intent = new Intent(this, NewSignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.signInShowPassword:
                if (showpass == false) {
                    useVisible = false;
                    signInShowPassword.setImageResource(R.drawable.ic_visibility_black_24dp);
                    getSignupVisiblePassword().setVisibility(View.VISIBLE);
                    getSignupVisiblePassword().setText(getSignupPassword().getText().toString());
                    getSignupPassword().setVisibility(View.GONE);
                    getSignupVisiblePassword().requestFocus();
                    //       AppUtil.showKeyboard(SignupActivity.this);
                    strPassword = getSignupVisiblePassword().getText().toString().trim();
                    showpass = true;
                } else if (showpass == true) {
                    useVisible = true;
                    signInShowPassword.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    getSignupVisiblePassword().setVisibility(View.GONE);
                    getSignupPassword().setVisibility(View.VISIBLE);
                    getSignupPassword().setText(getSignupVisiblePassword().getText().toString());
                    getSignupPassword().requestFocus();
                    //        AppUtil.showKeyboard(SignupActivity.this);
                    strPassword = getSignupPassword().getText().toString().trim();
                    showpass = false;
                }
                break;
        }
    }

    private void doSignUp() {

        String android_id = null;
        try {
            android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            PreferenceConnector.writeString(this, PreferenceConnector.MOBILE_ID, android_id);
            PreferenceConnector.writeString(this, PreferenceConnector.DEVICE_TYPE, "Android");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        userTypeList.add("Select User type");
//        userTypeList.add("Pharmacy Retailer");
//        userTypeList.add("Supplier");
//        userTypeList.add("Company");
//        userTypeList.add("Other");

        if (userType.equals("Pharmacy Retailer"))
            intUserType = 3;
        else if (userType.equals("Supplier"))
            intUserType = 2;
        else if (userType.equals("Company"))
            intUserType = 5;
        else if (userType.equals("Other"))
            intUserType = 4;
        else {
            signupFarmacyError.setVisibility(View.VISIBLE);
            return;
        }

        if (progressDialog != null)
            if (!progressDialog.isShowing())
                progressDialog.show();

        final String deviceToken = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.DEVICE_TOKEN, "");

        if (city_id == null || city_id == "") {
            city_id = "2229";
        }

        String city = retailerCity.getText().toString();
        PreferenceConnector.writeString(getBaseContext(), PreferenceConnector.CITY, city);
        Call<Cities> getcityid = apiInterface.getCityID(city);
        final String finalAndroid_id = android_id;
        getcityid.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response != null) {

                    Cities data = (Cities) response.body();
                    String cityid = data.getmData().get(0);
                    PreferenceConnector.writeString(getBaseContext(), PreferenceConnector.CITY_ID, cityid);
                    city_id = cityid;

                    signUpCall = apiInterface.sinUp(strMobileNumber, String.valueOf(intUserType), strShopName.toUpperCase(), strPassword, finalAndroid_id, "Android", deviceToken, city_id);
                    signUpCall.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            if (response != null) {
                                SignUpResponse signUpResponse = (SignUpResponse) response.body();
                                if (signUpResponse != null) {
                                    String error = signUpResponse.getError();
                                    String msg = signUpResponse.getMessage();
                                    if (error.equals("0")) {
                                        Log.e(">>OTP", signUpResponse.getData().getOTP());
                                        OTPVerifyActivity.forwardStatus = 2;
                                        Intent intent = new Intent(SignupActivity.this, OTPVerifyActivity.class);
                                        intent.putExtra("mobileNumber", strMobileNumber);
                                        startActivity(intent);
                                    } else
                                        AppUtil.showAlert(msg, SignupActivity.this);
                                } else
                                    AppUtil.showAlert(getString(R.string.serverError), SignupActivity.this);
                            } else
                                AppUtil.showAlert(getString(R.string.serverError), SignupActivity.this);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            //           Log.e(">>Error", t.getMessage()+"");
                            AppUtil.showAlert(getString(R.string.serverError), SignupActivity.this);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                // AppUtil.showAlert(context.getString(R.string.serverError), context);
            }
        });


    }

    public static void hideSignupFragmentView() {
        signupParentView.setVisibility(View.VISIBLE);
        signupFragmentView.setVisibility(View.GONE);
    }

    private void hideSignupParentView() {
        signupParentView.setVisibility(View.GONE);
        signupFragmentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (!BackFragmentHelper.fireOnBackPressedEvent(this)) {
            if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
                finish();
            }
        }
    }


    private void getTermsAndCondition() {

        progressDialog = AppUtil.createProgressDialog(SignupActivity.this);
        if (!progressDialog.isShowing())
            progressDialog.show();

        call = apiInterface.getTerms();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (progressDialog != null)
                        progressDialog.dismiss();

                    TermsAndConditionResponse termsAndConditionResponse = (TermsAndConditionResponse) response.body();
                    if (termsAndConditionResponse != null) {
                        termsContent = termsAndConditionResponse.getData().get(0).getTermsContent();
                    } else
                        AppUtil.showAlert(getString(R.string.serverError), SignupActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                AppUtil.showAlert(getString(R.string.serverError), SignupActivity.this);
            }
        });
    }

    private void termsAndConditionsDialog() {

        final Dialog dialog = new Dialog(this, R.style.MyRequestDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.terms_and_condition);
        TextView terms;
        ImageView cross;
        terms = dialog.findViewById(R.id.terms);
        if (termsContent != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                terms.setText(Html.fromHtml(termsContent));
            else
                terms.setText((Html.fromHtml(termsContent, Html.FROM_HTML_MODE_COMPACT)));
        } else
            terms.setText("Terms and Condition Not Available");

        cross = dialog.findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String city = cityList.get(position);
        PreferenceConnector.writeString(getBaseContext(), PreferenceConnector.CITY, city);
        Call<Cities> getcityid = apiInterface.getCityID(city);
        getcityid.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response != null) {

                    Cities data = (Cities) response.body();
                    String cityid = data.getmData().get(0);
                    PreferenceConnector.writeString(getBaseContext(), PreferenceConnector.CITY_ID, cityid);
                    city_id = cityid;
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                // AppUtil.showAlert(context.getString(R.string.serverError), context);
            }
        });


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private boolean checkInputData() {
        strShopName = getSignupShopName.getText().toString().trim();
        strMobileNumber = getSignupMobileNumber().getText().toString().trim();
        if (useVisible)
            strPassword = getSignupPassword().getText().toString().trim();
        else
            strPassword = getSignupVisiblePassword().getText().toString().trim();

        if (strShopName != null && !strShopName.equals("")) {
            retailerShopNameError.setVisibility(View.INVISIBLE);

            if (strMobileNumber != null && !strMobileNumber.equals("")) {
                if (strMobileNumber.length() > 9) {
                    signupMobileNumberError.setVisibility(View.INVISIBLE);

                    if (strPassword != null && !strPassword.equals("")) {
                        signupPasswordError.setVisibility(View.INVISIBLE);

                        if (strPassword.length() > 5) {
                            signupPasswordError.setVisibility(View.INVISIBLE);

                            if (userType != null) {
                                signupFarmacyError.setVisibility(View.INVISIBLE);
                                return true;
                            } else {
                                signupFarmacyError.setVisibility(View.VISIBLE);
                            }
                        } else {
                            signupPasswordError.setVisibility(View.VISIBLE);
                            passwordErrorText.setText("Password length must be 6-15 characters");
                        }
                    } else {
                        passwordErrorText.setText("Enter Password");
                        signupPasswordError.setVisibility(View.VISIBLE);
                    }
                } else {
                    numberErrorText.setText("Mobile Number must be of 10 digits");
                    signupMobileNumberError.setVisibility(View.VISIBLE);
                }
            } else {
                numberErrorText.setText("Enter Mobile Number");
                signupMobileNumberError.setVisibility(View.VISIBLE);
            }
        } else {
            retailerShopNameError.setVisibility(View.VISIBLE);
        }
        return false;
    }

    private void hideAllErrorMsg() {
        signupFarmacyError.setVisibility(View.INVISIBLE);
        signupMobileNumberError.setVisibility(View.INVISIBLE);
        retailerShopNameError.setVisibility(View.INVISIBLE);
        signupPasswordError.setVisibility(View.INVISIBLE);
    }
}
