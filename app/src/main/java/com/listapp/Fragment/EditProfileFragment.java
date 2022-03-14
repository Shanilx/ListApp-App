package com.listapp.Fragment;

/**
 * Created by syscraft on 7/3/2017.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.iid.FirebaseInstanceId;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.City.Cities;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Edit_Profile.ChangeTinDlNumberResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Edit_Profile.EditProfileResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Get_Profile.GetProfileResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.MobileNumberChange.NewResendOTPResponse;
import com.listapp.Activity.MobileNumberChangeOTPVerifyActivity;
import com.listapp.Activity.ProfileActivity;
import com.listapp.Adapter.YearAdapter;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.ListAppUtil.YesNoInteface;
import com.listapp.Local_Database.DataBaseHandler;
import com.listapp.Models.YearModel;
import com.listapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private LinearLayout retailerShopNameError, retailerFullNameError;
    private LinearLayout retailerAreaError;
    private LinearLayout retailerAddressError;
    private AutoCompleteTextView retailerState;
    private LinearLayout retailerStateError;
    private AutoCompleteTextView retailerCity;
    private LinearLayout retailerCityError;
    private LinearLayout retailerNumberError;
    private TextView dlnoRequest;
    private TextView tinNuRequest;
    private LinearLayout addFirstView;
    private ImageView addFirst;
    private LinearLayout cancelSecondView;
    private ImageView cancelSecond;
    private LinearLayout contactSecondNumberView, contactNameSecondView;
    private LinearLayout contactThirdNameView;
    private LinearLayout cancelThirdView;
    private ImageView cancelThird;
    private LinearLayout contactThirdNumberView;
    private LinearLayout contactNameFourthView;
    private LinearLayout cancelFourthView;
    private ImageView cancelFourth;
    private LinearLayout contactNumberFourthView;
    private LinearLayout contactNameFiveView;
    private LinearLayout cancelFiveView;
    private ImageView cancelFive;
    private LinearLayout contactNumberFiveView, retailerEmailError;
    private DataBaseHandler dataBaseHandler;
    private List<String> stateList =new ArrayList<>();;
    private String stateName;
    private List<String> cityList =new ArrayList<>();;
    private String cityName;
    private boolean firstView, secondView = false, thirdView = false, fourthView = false, fifthView = false;
    private APIInterface apiInterface;
    private Call editprofileCall;
    private String strShopName;
    private String strArea;
    private String strAddress, retailerEmail;
    private String mobileNumber;
    private String dln;
    private String tin;
    private String estd, userId, fullName, email, deviceType, deviceToken;
    private String name1, name2, name3, name4, name5, no1, no2, no3, no4, no5;
    private LinearLayout nameFirstErrorView, nameSecondErrorView, nameThirdErrorView, nameFourthErrorView, nameFifthErrorView, numberFirstErrorView,
            numberSecondErrorView, numberThirdErrorView, numberFourthErrorView, numberFifthErrorView;
    private TextView number1ErrorText, number2ErrorText, number3ErrorText, number4ErrorText, number5ErrorText;
    private String cityID;
    private String stateID;
    private ProgressDialog progressDialog;
    private Call<GetProfileResponse> getProfileCall;
    private Call<ChangeTinDlNumberResponse> changeTinCall;
    private List<YearModel> years;
    private Context context;
    private ScrollView hideKeyboardView;
    private TextView mobileNumberTextError;
    private String strRetailerName;
    private ArrayList<String> contactNames, contactNumbers;
    private Call<NewResendOTPResponse> sendOtpCall;
    private TextView mobileNumberRequest, selectedESTDYear;
    private boolean isReset = false;
    private YearAdapter yearAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize the SDK

        return inflater.inflate(R.layout.fragment_edit_profile, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProfileActivity.heading.setText("EDIT PROFILE");

        nameFirstErrorView = view.findViewById(R.id.nameFirstError);
        nameSecondErrorView = view.findViewById(R.id.nameSecondError);
        nameThirdErrorView = view.findViewById(R.id.nameThirdError);
        nameFourthErrorView = view.findViewById(R.id.nameFourthError);
        nameFifthErrorView = view.findViewById(R.id.nameFifthError);
        numberFirstErrorView = view.findViewById(R.id.numberFirstError);
        numberSecondErrorView = view.findViewById(R.id.numberSecondError);
        numberThirdErrorView = view.findViewById(R.id.numberThirdError);
        numberFourthErrorView = view.findViewById(R.id.numberFourthError);
        numberFifthErrorView = view.findViewById(R.id.numberFifthError);

        number1ErrorText = view.findViewById(R.id.numberFirstErrorText);
        number2ErrorText = view.findViewById(R.id.numberSecondErrorText);
        number3ErrorText = view.findViewById(R.id.numberThirdErrorText);
        number4ErrorText = view.findViewById(R.id.numberFourthErrorText);
        number5ErrorText = view.findViewById(R.id.numberFifthErrorText);

        mobileNumberRequest = view.findViewById(R.id.mobileNumberRequest);
        mobileNumberRequest.setOnClickListener(this);

        selectedESTDYear = view.findViewById(R.id.selectedESTDYear);
        selectedESTDYear.setVisibility(View.VISIBLE);
        selectedESTDYear.setOnClickListener(this);

        retailerFullNameError = view.findViewById(R.id.retailerFullNameError);
        retailerShopNameError = view.findViewById(R.id.retailerShopNameError);
        retailerAreaError = view.findViewById(R.id.retailerAreaError);
        retailerAddressError = view.findViewById(R.id.retailerAddressError);
        retailerState = view.findViewById(R.id.retailerState);
        retailerState.setText("Madhya Pradesh");
        retailerStateError = view.findViewById(R.id.retailerStateError);
        retailerCity = view.findViewById(R.id.retailerCity);
        retailerCityError = view.findViewById(R.id.retailerCityError);
        retailerNumberError = view.findViewById(R.id.retailerNumberError);
        dlnoRequest = view.findViewById(R.id.dlnoRequest);
        tinNuRequest = view.findViewById(R.id.tinNuRequest);
        addFirstView = view.findViewById(R.id.addFirstView);
        addFirst = view.findViewById(R.id.addFirst);
        cancelSecondView = view.findViewById(R.id.cancelSecondView);
        cancelSecond = view.findViewById(R.id.cancelSecond);
        contactSecondNumberView = view.findViewById(R.id.contactSecondNumberView);
        contactNameSecondView = view.findViewById(R.id.contactNameSecondView);
        contactThirdNameView = view.findViewById(R.id.contactThirdNameView);
        cancelThirdView = view.findViewById(R.id.cancelThirdView);
        cancelThird = view.findViewById(R.id.cancelThird);
        contactThirdNumberView = view.findViewById(R.id.contactThirdNumberView);
        contactNameFourthView = view.findViewById(R.id.contactNameFourthView);
        cancelFourthView = view.findViewById(R.id.cancelFourthView);
        cancelFourth = view.findViewById(R.id.cancelFourth);
        contactNumberFourthView = view.findViewById(R.id.contactNumberFourthView);
        contactNameFiveView = view.findViewById(R.id.contactNameFiveView);
        cancelFiveView = view.findViewById(R.id.cancelFiveView);
        cancelFive = view.findViewById(R.id.cancelFive);
        contactNumberFiveView = view.findViewById(R.id.contactNumberFiveView);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        view.findViewById(R.id.retailerSubmit).setOnClickListener(this);
        dlnoRequest.setOnClickListener(this);
        tinNuRequest.setOnClickListener(this);
        hideKeyboardView = view.findViewById(R.id.hideKeyboardView);
        hideAllErrorViews();
        getData();
       // getRetailerArea().setOnClickListener(this);
        addFirst.setOnClickListener(this);
        addFirstView.setOnClickListener(this);
        cancelSecond.setOnClickListener(this);
        cancelFourth.setOnClickListener(this);
        cancelThird.setOnClickListener(this);
        cancelFive.setOnClickListener(this);
        cancelSecondView.setOnClickListener(this);
        cancelThirdView.setOnClickListener(this);
        cancelFourthView.setOnClickListener(this);
        cancelFiveView.setOnClickListener(this);
        retailerEmailError = view.findViewById(R.id.retailerEmailError);
        retailerEmailError.setVisibility(View.INVISIBLE);
        mobileNumberTextError = view.findViewById(R.id.mobileNumberTextError);

        years = new ArrayList<>();
        getEstdNumber().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    isReset = true;
                    estd = years.get(i).getYear();
                    selectedESTDYear.setText(estd);
                    selectedESTDYear.setVisibility(View.VISIBLE);
                    int length = years.size();
                    for (int ii = 0; ii < length; ii++) {
                        years.get(ii).setSleted(false);
                    }
                    years.get(i).setSleted(true);
                    yearAdapter.notifyDataSetChanged();
                } else if (isReset) {
                    estd = years.get(i).getYear();
                    int length = years.size();
                    for (int ii = 0; ii < length; ii++) {
                        years.get(ii).setSleted(false);
                    }
                    yearAdapter.notifyDataSetChanged();
                    selectedESTDYear.setText(estd);
                    selectedESTDYear.setVisibility(View.VISIBLE);
                } else {
                    for (YearModel s : years) {
                        if (s.getYear().equals(estd))
                            getEstdNumber().setSelection(years.indexOf(s));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                int i = adapterView.getFirstVisiblePosition();
                selectedESTDYear.setText(estd);
                selectedESTDYear.setVisibility(View.VISIBLE);
            }
        });

//        stateName = "Madhya Pradesh";
        Call<Cities> statesCall = apiInterface.getstates();
        statesCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body()!=null){
                    Cities states = (Cities) response.body();
                    stateList.addAll(states.getmData());
                    ArrayAdapter stateAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, stateList);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    retailerState.setAdapter(stateAdapter);
                    //  retailerState.setSelection(19);
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(context, "Failed to fetch states", Toast.LENGTH_SHORT).show();

            }
        });

        Call<Cities> citiesCall = apiInterface.getCityByState("Madhya Pradesh");
        citiesCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Cities cities = (Cities) response.body();
                if (cities.getmData() != null) {
                    cityList.clear();
                    cityList.addAll(cities.getmData());
                    ArrayAdapter cityAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, cityList);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    retailerCity.setAdapter(cityAdapter);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        try {
            retailerState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String state = retailerState.getText().toString();
                    Call<Cities> citiesCall = apiInterface.getCityByState(state);
                    citiesCall.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Cities cities = (Cities) response.body();
                            if (cities.getmData() != null) {
                                cityList.clear();
                                cityList.addAll(cities.getmData());
                                ArrayAdapter cityAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, cityList);
                                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                retailerCity.setAdapter(cityAdapter);

                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        } catch (Exception e) {
        }
      //  cityName = "Indore";
    }

    private EditText getRetailerShopName() {
        return (EditText) getView().findViewById(R.id.retailerShopName);
    }

    private TextView getRetailerArea() {
        return (TextView) getView().findViewById(R.id.retailerArea);
    }

    private EditText getRetailerAddress() {
        return (EditText) getView().findViewById(R.id.retailerAddress);
    }

    private EditText getRetailerFullName() {
        return (EditText) getView().findViewById(R.id.retailerFullName);
    }

    private EditText getRetailerEmail() {
        return (EditText) getView().findViewById(R.id.retailerEmail);
    }

    private EditText getRetailerNumber() {
        return (EditText) getView().findViewById(R.id.retailerNumber);
    }

    private EditText getRetailerDLN() {
        return (EditText) getView().findViewById(R.id.retailerDLN);
    }

    private EditText getTinNumber() {
        return (EditText) getView().findViewById(R.id.tinNumber);
    }

    private Spinner getEstdNumber() {
        return (Spinner) getView().findViewById(R.id.estdNumber);
    }

    private EditText getContactFirstName() {
        return (EditText) getView().findViewById(R.id.contactFirstName);
    }

    private EditText getContactFirstNumber() {
        return (EditText) getView().findViewById(R.id.contactFirstNumber);
    }

    private EditText getContactSecondName() {
        return (EditText) getView().findViewById(R.id.contactSecondName);
    }

    private EditText getContactSecondNumber() {
        return (EditText) getView().findViewById(R.id.contactSecondNumber);
    }

    private EditText getContactThirdName() {
        return (EditText) getView().findViewById(R.id.contactThirdName);
    }

    private EditText getContactThirdNumber() {
        return (EditText) getView().findViewById(R.id.contactThirdNumber);
    }

    private EditText getContactFourthName() {
        return (EditText) getView().findViewById(R.id.contactFourthName);
    }

    private EditText getContactFourthNumber() {
        return (EditText) getView().findViewById(R.id.contactFourthNumber);
    }

    private EditText getContactNameFive() {
        return (EditText) getView().findViewById(R.id.contactNameFive);
    }

    private EditText getContactFiveNumber() {
        return (EditText) getView().findViewById(R.id.contactFiveNumber);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                getFragmentManager().popBackStack();
                break;
            case R.id.retailerSubmit:
                if (CheckNetwork.isNetwordAvailable(getContext())) {
                    if (checkInputData()) {
                        uploadData();
                    }
                } else
                    AppUtil.showAlert(getContext().getString(R.string.networkError), getContext());
                break;
            case R.id.tinNuRequest:
                requestDialog(2);
                break;
            case R.id.dlnoRequest:
                requestDialog(1);
                break;

            case R.id.selectedESTDYear:
                selectedESTDYear.setVisibility(View.VISIBLE);
                getEstdNumber().setVisibility(View.INVISIBLE);
                getEstdNumber().performClick();
                break;

            case R.id.addFirst:
                addContactView();
                break;
            case R.id.addFirstView:
                addContactView();
                break;

            case R.id.mobileNumberRequest:
                String dummy = getRetailerNumber().getText().toString().trim();
                if (!dummy.equals("") && dummy.length() > 9) {
                    if (!dummy.equals(PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, ""))) {
                        resendOTPRetro(dummy);
                    } else
                        AppUtil.showAlert("New Mobile Number can not be same as old Mobile Number", context);
                } else
                    AppUtil.showAlert("Please enter valid Contact Number", context);
                break;

            case R.id.cancelSecond:
                if (!getContactSecondName().getText().toString().trim().equals("") || !getContactSecondNumber().getText().toString().trim().equals("")) {
                    AppUtil.yesNoDialog(context, "Do you really want to delete this Contact Details", new YesNoInteface() {
                        @Override
                        public void isNoYes(boolean b) {
                            if (b)
                                hideSecondView();
                        }
                    });
                } else
                    hideSecondView();
                break;
            case R.id.cancelSecondView:
                if (!getContactSecondName().getText().toString().trim().equals("") || !getContactSecondNumber().getText().toString().trim().equals("")) {
                    AppUtil.yesNoDialog(context, "Do you really want to delete this Contact Details", new YesNoInteface() {
                        @Override
                        public void isNoYes(boolean b) {
                            if (b)
                                hideSecondView();
                        }
                    });
                } else
                    hideSecondView();
                break;

            case R.id.cancelThird:
                if (!getContactThirdName().getText().toString().trim().equals("") || !getContactThirdNumber().getText().toString().trim().equals("")) {
                    AppUtil.yesNoDialog(context, "Do you really want to delete this Contact Details", new YesNoInteface() {
                        @Override
                        public void isNoYes(boolean b) {
                            if (b)
                                hideThirdView();
                        }
                    });
                } else
                    hideThirdView();
                break;
            case R.id.cancelThirdView:
                if (!getContactThirdName().getText().toString().trim().equals("") || !getContactThirdNumber().getText().toString().trim().equals("")) {
                    AppUtil.yesNoDialog(context, "Do you really want to delete this Contact Details", new YesNoInteface() {
                        @Override
                        public void isNoYes(boolean b) {
                            if (b)
                                hideThirdView();
                        }
                    });
                } else
                    hideThirdView();
                break;

            case R.id.cancelFourth:
                if (!getContactFourthName().getText().toString().trim().equals("") || !getContactFourthNumber().getText().toString().trim().equals("")) {
                    AppUtil.yesNoDialog(context, "Do you really want to delete this Contact Details", new YesNoInteface() {
                        @Override
                        public void isNoYes(boolean b) {
                            if (b)
                                hideFourthView();
                        }
                    });
                } else
                    hideFourthView();
                break;
            case R.id.cancelFourthView:
                if (!getContactFourthName().getText().toString().trim().equals("") && !getContactFourthNumber().getText().toString().trim().equals("")) {
                    AppUtil.yesNoDialog(context, "Do you really want to delete this Contact Details", new YesNoInteface() {
                        @Override
                        public void isNoYes(boolean b) {
                            if (b)
                                hideFourthView();
                        }
                    });
                } else
                    hideFourthView();
                break;

            case R.id.cancelFive:
                if (!getContactNameFive().getText().toString().trim().equals("") || !getContactFiveNumber().getText().toString().trim().equals("")) {
                    AppUtil.yesNoDialog(context, "Do you really want to delete this Contact Details", new YesNoInteface() {
                        @Override
                        public void isNoYes(boolean b) {
                            if (b)
                                hideFifthView();
                        }
                    });
                } else
                    hideFifthView();
                break;
            case R.id.cancelFiveView:
                if (!getContactNameFive().getText().toString().trim().equals("") || !getContactFiveNumber().getText().toString().trim().equals("")) {
                    AppUtil.yesNoDialog(context, "Do you really want to delete Contact Details", new YesNoInteface() {
                        @Override
                        public void isNoYes(boolean b) {
                            if (b)
                                hideFifthView();
                        }
                    });
                } else
                    hideFifthView();
                break;
        }
    }

    private void uploadData() {

        if (secondView == false && thirdView == false && fourthView == false && fifthView == false) {

            if (checkNameNumberOneViews()) {
                sendData(1);
            }
        } else if (thirdView == false && fourthView == false && fifthView == false) {

            if (checkNameNumberTwoViews()) {
                sendData(2);
            }
        } else if (thirdView == true && secondView == false && fourthView == false && fifthView == false) {
            if (checkNameNumberOneViews()) {
                if (checkNameNumberOnlyThirdView()) {
                    sendData(33);
                }
            }
        } else if (fourthView == true && secondView == false && thirdView == false && fifthView == false) {
            if (checkNameNumberOneViews()) {
                if (checkNameNumberOnlyFiveView()) {
                    sendData(44);
                }
            }
        } else if (fifthView == true && secondView == false && thirdView == false && fourthView == false) {
            if (checkNameNumberOneViews()) {
                if (checkNameNumberOnlyFourthView()) {
                    sendData(55);
                }
            }
        } else if (fifthView == true && secondView == true && thirdView == true && fourthView == false) {
            if (checkNameNumberThreeViews()) {
                if (checkNameNumberOnlyFiveView()) {
                    sendData(515);
                }
            }
        } else if (fourthView == false && fifthView == false) {

            if (checkNameNumberThreeViews()) {
                sendData(3);
            }
        } else if (secondView == true && thirdView == true && fourthView == false) {

            if (checkNameNumberThreeViews())
                sendData(3);

        } else if (secondView == true && thirdView == true && fourthView == true && fifthView == true) {

            if (checkNameNumberFiveViews()) {
                sendData(5);
            }
        } else if (fifthView == false) {

            if (checkNameNumberFourViews()) {
                sendData(4);
            }
        }
    }


    private void sendData(final int i) {

        if (CheckNetwork.isNetwordAvailable(context)) {

            if (progressDialog != null) {
                progressDialog = AppUtil.createProgressDialog(getContext());
            } else if (!progressDialog.isShowing())
                progressDialog.show();

            apiInterface = APIClient.getClient().create(APIInterface.class);
            deviceType = PreferenceConnector.readString(getContext(), PreferenceConnector.DEVICE_TYPE, "Android");
            dln = PreferenceConnector.readString(getContext(), PreferenceConnector.DL_NUMBER, "");
            tin = PreferenceConnector.readString(getContext(), PreferenceConnector.TIN_NUMBER, "");
            userId = PreferenceConnector.readString(getContext(), PreferenceConnector.USER_ID, "");
            final String deviceToken = FirebaseInstanceId.getInstance().getToken();
            if (estd.equals("Select ESTD Year"))
                estd = "";


            if(PreferenceConnector.readString(getContext(),PreferenceConnector.User_CITY_ID,"")== "" ||PreferenceConnector.readString(getContext(),PreferenceConnector.User_CITY_ID,"")== null)

            stateName = retailerState.getText().toString();
            cityName = retailerCity.getText().toString();
                apiInterface.getCityID(cityName).enqueue(
                        new Callback<Cities>() {
                            @Override
                            public void onResponse(Call<Cities> call, Response<Cities> response) {
                                Cities city = response.body();
                                cityID = city.getmData().toString();

                                apiInterface.getStateID(stateName).enqueue(
                                        new Callback<Cities>() {
                                            @Override
                                            public void onResponse(Call<Cities> call, Response<Cities> response) {
                                                Cities city = response.body();
                                                stateID = city.getmData().toString();

                                                if (i == 1) {
                                                    editprofileCall = apiInterface.editProfileDetail(mobileNumber, dln, tin, estd, strArea, cityID, stateID, deviceToken, strShopName, strAddress, userId, strRetailerName, retailerEmail, deviceType,
                                                            name1, name2, "", "", "", no1, no2, "", "", "");
                                                } else if (i == 2) {
                                                    editprofileCall = apiInterface.editProfileDetail(mobileNumber, dln, tin, estd, strArea, cityID, stateID, deviceToken, strShopName, strAddress, userId, strRetailerName, retailerEmail, deviceType,
                                                            name1, name2, "", "", "", no1, no2, "", "", "");
                                                } else if (i == 3) {
                                                    editprofileCall = apiInterface.editProfileDetail(mobileNumber, dln, tin, estd, strArea, cityID, stateID, deviceToken, strShopName, strAddress, userId, strRetailerName, retailerEmail, deviceType,
                                                            name1, name2, name3, "", "", no1, no2, no3, "", "");
                                                } else if (i == 4) {
                                                    editprofileCall = apiInterface.editProfileDetail(mobileNumber, dln, tin, estd, strArea, cityID, stateID, deviceToken, strShopName, strAddress, userId, strRetailerName, retailerEmail, deviceType,
                                                            name1, name2, name3, name4, "", no1, no2, no3, no4, "");
                                                } else if (i == 5) {
                                                    editprofileCall = apiInterface.editProfileDetail(mobileNumber, dln, tin, estd, strArea, cityID, stateID, deviceToken, strShopName, strAddress, userId, strRetailerName, retailerEmail, deviceType,
                                                            name1, name2, name3, name4, name5, no1, no2, no3, no4, no5);
                                                } else if (i == 33) {
                                                    editprofileCall = apiInterface.editProfileDetail(mobileNumber, dln, tin, estd, strArea, cityID, stateID, deviceToken, strShopName, strAddress, userId, strRetailerName, retailerEmail, deviceType,
                                                            name1, "", name3, "", "", no1, "", no3, "", "");
                                                } else if (i == 44) {
                                                    editprofileCall = apiInterface.editProfileDetail(mobileNumber, dln, tin, estd, strArea, cityID, stateID, deviceToken, strShopName, strAddress, userId, strRetailerName, retailerEmail, deviceType,
                                                            name1, "", "", name4, "", no1, "", "", no4, "");
                                                } else if (i == 55) {
                                                    editprofileCall = apiInterface.editProfileDetail(mobileNumber, dln, tin, estd, strArea, cityID, stateID, deviceToken, strShopName, strAddress, userId, strRetailerName, retailerEmail, deviceType,
                                                            name1, "", "", "", name5, no1, "", "", "", no5);
                                                } else if (i == 515) {
                                                    editprofileCall = apiInterface.editProfileDetail(mobileNumber, dln, tin, estd, strArea, cityID, stateID, deviceToken, strShopName, strAddress, userId, strRetailerName, retailerEmail, deviceType,
                                                            name1, name2, name3, "", name5, no1, no2, no3, "", no5);
                                                }

                                                editprofileCall.enqueue(new Callback() {
                                                    @Override
                                                    public void onResponse(Call call, Response response) {
                                                        if (progressDialog != null)
                                                            progressDialog.dismiss();
                                                        if (response != null) {
                                                            EditProfileResponse editProfileResponse = (EditProfileResponse) response.body();
                                                            if (editProfileResponse != null) {

                                                                String error, msg;
                                                                error = editProfileResponse.getError();
                                                                msg = editProfileResponse.getMessage();
                                                                if (error.equals("0")) {

                                                                    ProfileActivity.address.setText(editProfileResponse.getData().getAddress());
                                                                    showAlert("Profile has been updated successfully ");
                                                                    PreferenceConnector.writeBoolean(getContext(), "firstTimeGetProfile", true);
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.FULL_NAME, editProfileResponse.getData().getFullName());
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.EMAIl, editProfileResponse.getData().getEmail());
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.AREA, editProfileResponse.getData().getArea());
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.ADDRESS, editProfileResponse.getData().getAddress());
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.User_CITY, editProfileResponse.getData().getCity());
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.STATE, editProfileResponse.getData().getState());
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.SHOP_NAME, strShopName);
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.CONTACT_NUMBER, editProfileResponse.getData().getContactNumber());
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.TIN_NUMBER, editProfileResponse.getData().getTinNumber());
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.ESTD_YEAR, editProfileResponse.getData().getEstdYear());
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.DL_NUMBER, editProfileResponse.getData().getDrugLicNumber());
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.User_CITY_ID, cityID);
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.User_CITY, cityName);
                                                                    PreferenceConnector.writeString(getContext(), PreferenceConnector.STATE_ID, stateID);

                                                                    List<EditProfileResponse.Data.ContactPerson> list = editProfileResponse.getData().getContactPerson();

                                                                    contactNames = new ArrayList<String>();
                                                                    contactNumbers = new ArrayList<String>();
                                                                    if (list != null) {
                                                                        int length = list.size();
                                                                        for (int i = 0; i < length; i++) {
                                                                            contactNames.add(list.get(i).getContactName());
                                                                            contactNumbers.add(list.get(i).getContactNumber());
                                                                        }
                                                                    }
                                                                    PreferenceConnector.writeArraylist(context, PreferenceConnector.CONTACTNAMES, contactNames);
                                                                    PreferenceConnector.writeArraylist(context, PreferenceConnector.CONTACTNUMBERS, contactNumbers);

                                                                } else {
                                                                    if (msg.equalsIgnoreCase("Your session has been expired"))
                                                                        AppUtil.sessionLogout(msg, getContext());
                                                                    else if (msg.equalsIgnoreCase("OTP Unverified") || msg.equals("go to otp")) {
                                                                        AppUtil.sessionLogout("Please Login Again with New Mobile Number", getActivity());
                                                                    } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                                                        AppUtil.sessionLogout(msg, context);
                                                                    else
                                                                        AppUtil.showAlert(msg, getContext());
                                                                }
                                                            } else
                                                                AppUtil.showAlert(context.getString(R.string.networkError), getContext());
                                                        } else
                                                            AppUtil.showAlert(context.getString(R.string.networkError), getContext());
                                                    }

                                                    @Override
                                                    public void onFailure(Call call, Throwable t) {
                                                        if (progressDialog != null)
                                                            progressDialog.dismiss();
                                                        AppUtil.showAlert(getString(R.string.networkError), getContext());
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(Call<Cities> call, Throwable t) {

                                            }
                                        });


                            }

                            @Override
                            public void onFailure(Call<Cities> call, Throwable t) {

                            }
                        });
        }}






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    private void requestDialog(int i) {
        final Dialog dialog = new Dialog(getContext(), R.style.MyRequestDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_request_for_change);
        final LinearLayout dlnError, tinError;
        final EditText dlnumber, tinnumber;
        final CheckBox dlnCheck, tinCheck;
        Button submit = dialog.findViewById(R.id.submit);
        ImageView cross;
        cross = dialog.findViewById(R.id.cross);
        dlnumber = dialog.findViewById(R.id.retailerDLN);
        tinnumber = dialog.findViewById(R.id.tinNumber);
        dlnCheck = dialog.findViewById(R.id.dlnCheck);
        tinCheck = dialog.findViewById(R.id.tinCheck);
        dlnError = dialog.findViewById(R.id.errorDLNumber);
        tinError = dialog.findViewById(R.id.errorTINumber);

        if (i == 1) {
            dlnCheck.setChecked(true);
            tinCheck.setChecked(false);
            tinnumber.setEnabled(false);
            tinError.setVisibility(View.INVISIBLE);
            dlnError.setVisibility(View.INVISIBLE);
        } else {
            dlnCheck.setChecked(false);
            tinCheck.setChecked(true);
            dlnumber.setEnabled(false);
            dlnError.setVisibility(View.INVISIBLE);
            tinError.setVisibility(View.INVISIBLE);
        }
        dlnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dlnCheck.isChecked()) {

                    dlnumber.setEnabled(true);
                } else {
                    dlnumber.setEnabled(false);
                    dlnumber.setText("");
                }
            }
        });
        tinCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tinCheck.isChecked()) {
                    tinnumber.setEnabled(true);
                } else {
                    tinnumber.setEnabled(false);
                    tinnumber.setText("");
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tin, dln;
                tin = tinnumber.getText().toString().trim();
                dln = dlnumber.getText().toString().trim();
                if (tinCheck.isChecked() && dlnCheck.isChecked()) {
                    if (!dln.equals("")) {
                        dlnError.setVisibility(View.INVISIBLE);
                        if (!tin.equals("")) {
                            tinError.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                            changeTinDlNumber(dln, tin, 33);
                        } else
                            tinError.setVisibility(View.VISIBLE);
                    } else
                        dlnError.setVisibility(View.VISIBLE);
                } else if (tinCheck.isChecked() && !dlnCheck.isChecked()) {

                    dlnError.setVisibility(View.INVISIBLE);
                    if (!tin.equals("")) {
                        tinError.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                        changeTinDlNumber("", tin, 22);
                    } else
                        tinError.setVisibility(View.VISIBLE);
                } else if (dlnCheck.isChecked() && !tinCheck.isChecked()) {
                    tinError.setVisibility(View.INVISIBLE);
                    if (!dln.equals("")) {
                        dlnError.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                        changeTinDlNumber(dln, "", 11);
                    } else
                        dlnError.setVisibility(View.VISIBLE);
                } else
                    Toast.makeText(getContext(), "Please select checkbox", Toast.LENGTH_SHORT).show();
            }
        });

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

    private void changeTinDlNumber(String dln, String tin, final int i) {

        if (CheckNetwork.isNetwordAvailable(context)) {
            if (progressDialog != null)
                progressDialog.show();

            apiInterface = APIClient.getClient().create(APIInterface.class);
            if (i == 33) {
                changeTinCall = apiInterface.changeTinDlNumber(mobileNumber, userId, deviceType, deviceToken, tin, dln);
            } else if (i == 22) {
                dln = PreferenceConnector.readString(getContext(), PreferenceConnector.DL_NUMBER, "");
                changeTinCall = apiInterface.changeTinDlNumber(mobileNumber, userId, deviceType, deviceToken, tin, dln);
            } else if (i == 11) {
                tin = PreferenceConnector.readString(getContext(), PreferenceConnector.TIN_NUMBER, "");
                changeTinCall = apiInterface.changeTinDlNumber(mobileNumber, userId, deviceType, deviceToken, tin, dln);
            }

            changeTinCall.enqueue(new Callback<ChangeTinDlNumberResponse>() {
                @Override
                public void onResponse(Call<ChangeTinDlNumberResponse> call, Response<ChangeTinDlNumberResponse> response) {
                    if(progressDialog!=null)
                    progressDialog.dismiss();
                    ChangeTinDlNumberResponse changeTinDlNumberResponse = response.body();
                    if (changeTinDlNumberResponse != null) {
                        String error, msg;
                        error = changeTinDlNumberResponse.getError();
                        msg = changeTinDlNumberResponse.getMessage();
                        if (error.equals("0")) {

                            if (i == 11)
                                AppUtil.showAlert("Request for change of Drug Licence Number has been sent successfully", getContext());
                            if (i == 22)
                                AppUtil.showAlert("Request for change of GSTIN Number has been sent successfully", getContext());
                            if (i == 33)
                                AppUtil.showAlert("Request for change of Drug Licence Number and GSTIN Number has been sent successfully", getContext());
                        } else {
                            if (changeTinDlNumberResponse.getMessage().equalsIgnoreCase("Your session has been expired")) {
                                AppUtil.sessionLogout(changeTinDlNumberResponse.getMessage(), getContext());
                            } else if (changeTinDlNumberResponse.getMessage().equalsIgnoreCase("Your account has been deactivated by administrator"))
                                AppUtil.sessionLogout(changeTinDlNumberResponse.getMessage(), context);
                            else
                                AppUtil.showAlert(msg, getContext());
                        }
                    } else
                        AppUtil.showAlert(getString(R.string.serverError), getContext());
                }

                @Override
                public void onFailure(Call<ChangeTinDlNumberResponse> call, Throwable t) {
                    if(progressDialog!=null)
                    progressDialog.dismiss();
                        AppUtil.showAlert(getString(R.string.serverError), getContext());
                }
            });
        } else
            AppUtil.showAlert(getString(R.string.networkError), context);
    }


    private void getData() {
        mobileNumber = PreferenceConnector.readString(getContext(), PreferenceConnector.MOBILE_NUMBER, "");
        userId = PreferenceConnector.readString(getContext(), PreferenceConnector.USER_ID, "");
        deviceType = PreferenceConnector.readString(getContext(), PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(getContext(), PreferenceConnector.DEVICE_TOKEN, "");

        progressDialog = AppUtil.createProgressDialog(getContext());
        if (!progressDialog.isShowing())
            progressDialog.show();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        getProfileCall = apiInterface.getProfileDetail(mobileNumber, userId, deviceType, deviceToken);
        getProfileCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                GetProfileResponse getProfileResponse = (GetProfileResponse) response.body();
                if (getProfileResponse != null) {

                    if (getProfileResponse.getError().equals("0")) {

                        PreferenceConnector.writeString(getContext(), PreferenceConnector.FULL_NAME, getProfileResponse.getData().getRetailerName());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.USER_ID, getProfileResponse.getData().getUserId());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.EMAIl, getProfileResponse.getData().getEmail());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.AREA, getProfileResponse.getData().getArea());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.ADDRESS, getProfileResponse.getData().getAddress());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.User_CITY, getProfileResponse.getData().getCity());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.STATE, getProfileResponse.getData().getState());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.SHOP_NAME, getProfileResponse.getData().getShopName());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.CONTACT_NUMBER, getProfileResponse.getData().getContactNumber());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.TIN_NUMBER, getProfileResponse.getData().getTinNumber());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.ESTD_YEAR, getProfileResponse.getData().getEstdYear());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.DL_NUMBER, getProfileResponse.getData().getDrugLicNo());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.User_CITY_ID, getProfileResponse.getData().getCityId());
                        PreferenceConnector.writeString(getContext(), PreferenceConnector.STATE_ID, getProfileResponse.getData().getStateId());

                        ProfileActivity.address.setText(getProfileResponse.getData().getAddress());
                        ProfileActivity.userName.setText(getProfileResponse.getData().getShopName());
                        ProfileActivity.mobileNumber.setText(getProfileResponse.getData().getContactNumber());

                        List<GetProfileResponse.Data.ContactPerson> list = getProfileResponse.getData().getContactPerson();
                        contactNames = new ArrayList<String>();
                        contactNumbers = new ArrayList<String>();
                        if (list != null) {
                            int length = list.size();

                            for (int i = 0; i < length; i++) {

                                contactNames.add(list.get(i).getContactName());
                                contactNumbers.add(list.get(i).getContactNumber());

                                if (i == 0) {
                                    firstView = true;
                                    getContactFirstNumber().setText(list.get(i).getContactNumber());
                                    getContactFirstName().setText(list.get(i).getContactName());
                                } else if (i == 1) {
                                    secondView = true;
                                    visibleSecondView();
                                    getContactSecondNumber().setText(list.get(i).getContactNumber());
                                    getContactSecondName().setText(list.get(i).getContactName());
                                } else if (i == 2) {
                                    thirdView = true;
                                    visibleThirdView();
                                    getContactThirdNumber().setText(list.get(i).getContactNumber());
                                    getContactThirdName().setText(list.get(i).getContactName());
                                } else if (i == 3) {
                                    fourthView = true;
                                    visibleFourthView();
                                    getContactFourthNumber().setText(list.get(i).getContactNumber());
                                    getContactFourthName().setText(list.get(i).getContactName());
                                } else if (i == 4) {
                                    fifthView = true;
                                    visibleFifthView();
                                    getContactFiveNumber().setText(list.get(i).getContactNumber());
                                    getContactNameFive().setText(list.get(i).getContactName());
                                }
                            }
                        }
                        setPreviousData();
                        PreferenceConnector.writeArraylist(context, PreferenceConnector.CONTACTNAMES, contactNames);
                        PreferenceConnector.writeArraylist(context, PreferenceConnector.CONTACTNUMBERS, contactNumbers);

                    } else {
                        if (getProfileResponse.getMessage().equalsIgnoreCase("Your session has been expired")) {
                            AppUtil.sessionLogout(getProfileResponse.getMessage(), getContext());
                        } else if (getProfileResponse.getMessage().equalsIgnoreCase("Your account has been deactivated by administrator"))
                            AppUtil.sessionLogout(getProfileResponse.getMessage(), context);
                        else
                            AppUtil.showAlert(getProfileResponse.getMessage(), getContext());
                    }
                } else
                    AppUtil.showAlert(getString(R.string.serverError), getContext());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                    AppUtil.showAlert(getString(R.string.serverError), getContext());
                progressDialog.dismiss();
            }
        });
    }

    private void setPreviousData() {

        getRetailerShopName().setText(PreferenceConnector.readString(getContext(), PreferenceConnector.SHOP_NAME, "-"));
        getRetailerArea().setText(PreferenceConnector.readString(getContext(), PreferenceConnector.AREA, "-"));
        getRetailerAddress().setText(PreferenceConnector.readString(getContext(), PreferenceConnector.ADDRESS, "-"));
        getRetailerFullName().setText(PreferenceConnector.readString(getContext(), PreferenceConnector.FULL_NAME, ""));
        mobileNumber = PreferenceConnector.readString(getContext(), PreferenceConnector.MOBILE_NUMBER, "-");
        getRetailerNumber().setText(mobileNumber);
        getRetailerEmail().setText(PreferenceConnector.readString(getContext(), PreferenceConnector.EMAIl, "-"));
        dln = PreferenceConnector.readString(getContext(), PreferenceConnector.DL_NUMBER, "-");
        if (!dln.equals("") && !dln.equals("0"))
            getRetailerDLN().setText(dln);
        tin = PreferenceConnector.readString(getContext(), PreferenceConnector.TIN_NUMBER, "-");
        if (!tin.equals("") && !tin.equals("0"))
            getTinNumber().setText(tin);
        estd = PreferenceConnector.readString(getContext(), PreferenceConnector.ESTD_YEAR, "-");
        if (!estd.equals("") && !estd.equals("0000") && !estd.equals("estd year")) {
            setESTDYears();
            selectedESTDYear.setText(estd);
        } else {
            estd = "Select ESTD Year";
            selectedESTDYear.setText("Select ESTD Year");
            setESTDYears();
        }

        dataBaseHandler = new DataBaseHandler(getContext());
        stateList = new ArrayList<>();
        cityList = new ArrayList<>();


        ArrayAdapter stateAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, stateList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        retailerState.setAdapter(stateAdapter);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void hideAllErrorViews() {

        retailerAddressError.setVisibility(View.INVISIBLE);
        retailerAreaError.setVisibility(View.INVISIBLE);
        retailerStateError.setVisibility(View.INVISIBLE);
        retailerShopNameError.setVisibility(View.INVISIBLE);
        retailerNumberError.setVisibility(View.GONE);
        retailerCityError.setVisibility(View.INVISIBLE);
        retailerFullNameError.setVisibility(View.INVISIBLE);
        numberFirstErrorView.setVisibility(View.INVISIBLE);
        numberSecondErrorView.setVisibility(View.GONE);
        numberThirdErrorView.setVisibility(View.GONE);
        numberFourthErrorView.setVisibility(View.GONE);
        numberFifthErrorView.setVisibility(View.GONE);
        nameFirstErrorView.setVisibility(View.INVISIBLE);
        nameSecondErrorView.setVisibility(View.GONE);
        nameThirdErrorView.setVisibility(View.GONE);
        numberThirdErrorView.setVisibility(View.GONE);
        nameFifthErrorView.setVisibility(View.GONE);
    }

    private void visibleSecondView() {
        contactNameSecondView.setVisibility(View.VISIBLE);
        contactSecondNumberView.setVisibility(View.VISIBLE);
        nameSecondErrorView.setVisibility(View.INVISIBLE);
        numberSecondErrorView.setVisibility(View.INVISIBLE);
    }

    private void visibleThirdView() {
        contactThirdNameView.setVisibility(View.VISIBLE);
        contactThirdNumberView.setVisibility(View.VISIBLE);
        nameThirdErrorView.setVisibility(View.INVISIBLE);
        numberThirdErrorView.setVisibility(View.INVISIBLE);
    }

    private void visibleFourthView() {
        contactNameFourthView.setVisibility(View.VISIBLE);
        contactNumberFourthView.setVisibility(View.VISIBLE);
        nameFourthErrorView.setVisibility(View.INVISIBLE);
        numberFourthErrorView.setVisibility(View.INVISIBLE);
    }

    private void visibleFifthView() {
        contactNameFiveView.setVisibility(View.VISIBLE);
        contactNumberFiveView.setVisibility(View.VISIBLE);
        nameFifthErrorView.setVisibility(View.INVISIBLE);
        numberFifthErrorView.setVisibility(View.INVISIBLE);

    }


    private void hideSecondView() {
        secondView = false;
        getContactSecondName().setText("");
        getContactSecondNumber().setText("");
        contactNameSecondView.setVisibility(View.GONE);
        contactSecondNumberView.setVisibility(View.GONE);
        nameSecondErrorView.setVisibility(View.GONE);
        numberSecondErrorView.setVisibility(View.GONE);
    }

    private void hideThirdView() {
        thirdView = false;
        getContactThirdName().setText("");
        getContactThirdNumber().setText("");
        contactThirdNameView.setVisibility(View.GONE);
        contactThirdNumberView.setVisibility(View.GONE);
        nameThirdErrorView.setVisibility(View.GONE);
        numberThirdErrorView.setVisibility(View.GONE);
    }

    private void hideFourthView() {
        fourthView = false;
        getContactFourthName().setText("");
        getContactFourthNumber().setText("");
        contactNameFourthView.setVisibility(View.GONE);
        contactNumberFourthView.setVisibility(View.GONE);
        nameFourthErrorView.setVisibility(View.GONE);
        numberFourthErrorView.setVisibility(View.GONE);
    }

    private void hideFifthView() {
        fifthView = false;
        getContactFiveNumber().setText("");
        getContactNameFive().setText("");
        contactNameFiveView.setVisibility(View.GONE);
        contactNumberFiveView.setVisibility(View.GONE);
        nameFifthErrorView.setVisibility(View.GONE);
        numberFifthErrorView.setVisibility(View.GONE);
    }

    private void addContactView() {

        if (!secondView) {
            secondView = true;
            visibleSecondView();
            return;
        }
        if (!thirdView) {
            thirdView = true;
            visibleThirdView();
            return;
        }
        if (!fourthView) {
            fourthView = true;
            visibleFourthView();
            return;
        }
        if (!fifthView) {
            fifthView = true;
            visibleFifthView();
            return;
        }
    }

    private boolean checkInputData() {
        strShopName = getRetailerShopName().getText().toString().trim().toUpperCase();
        strArea = getRetailerArea().getText().toString().trim();
        strAddress = getRetailerAddress().getText().toString().trim();
        mobileNumber = getRetailerNumber().getText().toString().trim();
        retailerEmail = getRetailerEmail().getText().toString().trim();
        strRetailerName = getRetailerFullName().getText().toString().trim();
        stateName = retailerState.getText().toString();
        cityName = retailerCity.getText().toString();

        if (strShopName != null && !strShopName.equals("")) {
            retailerShopNameError.setVisibility(View.INVISIBLE);

            if (strAddress != null && !strAddress.equals("")) {
                retailerAddressError.setVisibility(View.INVISIBLE);
                if (stateName != null) {
                    retailerStateError.setVisibility(View.INVISIBLE);
                    if (cityName != null) {
                        retailerCityError.setVisibility(View.INVISIBLE);
                        if (strArea != null && !strArea.equals("")) {
                            retailerAreaError.setVisibility(View.INVISIBLE);
                            if (retailerEmail.equals("") || AppUtil.isEmailValid(retailerEmail)) {
                                retailerEmailError.setVisibility(View.INVISIBLE);
                                if (!strRetailerName.equals("")) {
                                    if (mobileNumber.length() > 9 && mobileNumber.equals(PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, ""))) {
                                        return true;
                                    } else {
                                        AppUtil.showAlert("Please verify your Mobile Number from update contact option", context);
                                    }
                                } else {
                                    retailerFullNameError.setVisibility(View.VISIBLE);
                                }
                            } else
                                retailerEmailError.setVisibility(View.VISIBLE);
                        } else {
                            retailerAreaError.setVisibility(View.VISIBLE);
                        }
                    } else
                        retailerCityError.setVisibility(View.VISIBLE);
                } else {
                    retailerStateError.setVisibility(View.VISIBLE);
                }
            } else {
                retailerAddressError.setVisibility(View.VISIBLE);
            }
        } else {
            retailerShopNameError.setVisibility(View.VISIBLE);
        }

        hideKeyboardView.scrollTo(0,0);

        return false;
    }

    private boolean checkNameNumberFiveViews() {

        no1 = getContactFirstNumber().getText().toString().trim();
        no2 = getContactSecondNumber().getText().toString().trim();
        no3 = getContactThirdNumber().getText().toString().trim();
        no4 = getContactFourthNumber().getText().toString().trim();
        no5 = getContactFiveNumber().getText().toString().trim();

        name1 = getContactFirstName().getText().toString().trim();
        name2 = getContactSecondName().getText().toString().trim();
        name3 = getContactThirdName().getText().toString().trim();
        name4 = getContactFourthName().getText().toString().trim();
        name5 = getContactNameFive().getText().toString().trim();


        if (!name1.equals("")) {
            nameFirstErrorView.setVisibility(View.INVISIBLE);
            if (!no1.equals("")) {
                if (no1.length() > 8) {
                    numberFirstErrorView.setVisibility(View.INVISIBLE);
                    if (!name2.equals("")) {
                        nameSecondErrorView.setVisibility(View.INVISIBLE);
                        if (!no2.equals("")) {
                            if (no2.length() > 8) {
                                numberSecondErrorView.setVisibility(View.INVISIBLE);
                                if (!name3.equals("")) {
                                    nameThirdErrorView.setVisibility(View.INVISIBLE);
                                    if (!no3.equals("")) {
                                        if (no3.length() > 8) {
                                            numberThirdErrorView.setVisibility(View.INVISIBLE);
                                            if (!name4.equals("")) {
                                                nameFourthErrorView.setVisibility(View.INVISIBLE);
                                                if (!no4.equals("")) {
                                                    if (no4.length() > 8) {
                                                        numberFourthErrorView.setVisibility(View.INVISIBLE);
                                                        if (!name5.equals("")) {
                                                            nameFifthErrorView.setVisibility(View.INVISIBLE);
                                                            if (!no5.equals("")) {
                                                                if (no5.length() > 8) {
                                                                    numberFifthErrorView.setVisibility(View.INVISIBLE);
                                                                    return true;
                                                                } else {
                                                                    number5ErrorText.setText("Please enter a value between 8 to 14 numbers");
                                                                    numberFifthErrorView.setVisibility(View.VISIBLE);
                                                                }
                                                            } else
                                                                numberFifthErrorView.setVisibility(View.VISIBLE);
                                                        } else
                                                            nameFifthErrorView.setVisibility(View.VISIBLE);
                                                    } else {
                                                        number4ErrorText.setText("Please enter a value between 8 to 14 numbers");
                                                        numberFourthErrorView.setVisibility(View.VISIBLE);
                                                    }
                                                } else
                                                    numberFourthErrorView.setVisibility(View.VISIBLE);
                                            } else
                                                nameFourthErrorView.setVisibility(View.VISIBLE);
                                        } else {
                                            number3ErrorText.setText("Please enter a value between 8 to 14 numbers");
                                            numberThirdErrorView.setVisibility(View.VISIBLE);
                                        }
                                    } else
                                        numberThirdErrorView.setVisibility(View.VISIBLE);
                                } else
                                    nameThirdErrorView.setVisibility(View.VISIBLE);
                            } else {
                                number2ErrorText.setText("Please enter a value between 8 to 14 numbers");
                                numberSecondErrorView.setVisibility(View.VISIBLE);
                            }
                        } else
                            numberSecondErrorView.setVisibility(View.VISIBLE);
                    } else
                        nameSecondErrorView.setVisibility(View.VISIBLE);
                } else {
                    number1ErrorText.setText("Please enter a value between 8 to 14 numbers");
                    numberFirstErrorView.setVisibility(View.VISIBLE);
                }
            } else
                numberFirstErrorView.setVisibility(View.VISIBLE);
        } else
            nameFirstErrorView.setVisibility(View.VISIBLE);

        return false;
    }


    private boolean checkNameNumberFourViews() {

        no1 = getContactFirstNumber().getText().toString().trim();
        no2 = getContactSecondNumber().getText().toString().trim();
        no3 = getContactThirdNumber().getText().toString().trim();
        no4 = getContactFourthNumber().getText().toString().trim();

        name1 = getContactFirstName().getText().toString().trim();
        name2 = getContactSecondName().getText().toString().trim();
        name3 = getContactThirdName().getText().toString().trim();
        name4 = getContactFourthName().getText().toString().trim();


        if (!name1.equals("")) {
            nameFirstErrorView.setVisibility(View.INVISIBLE);
            if (!no1.equals("")) {
                if (no1.length() > 8) {
                    numberFirstErrorView.setVisibility(View.INVISIBLE);
                    if (!name2.equals("")) {
                        nameSecondErrorView.setVisibility(View.INVISIBLE);
                        if (!no2.equals("")) {
                            if (no2.length() > 8) {
                                numberSecondErrorView.setVisibility(View.INVISIBLE);
                                if (!name3.equals("")) {
                                    nameThirdErrorView.setVisibility(View.INVISIBLE);
                                    if (!no3.equals("")) {
                                        if (no3.length() > 8) {
                                            numberThirdErrorView.setVisibility(View.INVISIBLE);
                                            if (!name4.equals("")) {
                                                nameFourthErrorView.setVisibility(View.INVISIBLE);
                                                if (!no4.equals("")) {
                                                    if (no4.length() > 8) {
                                                        numberFourthErrorView.setVisibility(View.INVISIBLE);
                                                        return true;
                                                    } else {
                                                        number4ErrorText.setText("Please enter a value between 8 to 14 numbers");
                                                        numberFourthErrorView.setVisibility(View.VISIBLE);
                                                    }
                                                } else
                                                    numberFourthErrorView.setVisibility(View.VISIBLE);
                                            } else
                                                nameFourthErrorView.setVisibility(View.VISIBLE);
                                        } else {
                                            number3ErrorText.setText("Please enter a value between 8 to 14 numbers");
                                            numberThirdErrorView.setVisibility(View.VISIBLE);
                                        }
                                    } else
                                        numberThirdErrorView.setVisibility(View.VISIBLE);
                                } else
                                    nameThirdErrorView.setVisibility(View.VISIBLE);
                            } else {
                                number2ErrorText.setText("Please enter a value between 8 to 14 numbers");
                                numberSecondErrorView.setVisibility(View.VISIBLE);
                            }
                        } else
                            numberSecondErrorView.setVisibility(View.VISIBLE);
                    } else
                        nameSecondErrorView.setVisibility(View.VISIBLE);
                } else {
                    number1ErrorText.setText("Please enter a value between 8 to 14 numbers");
                    numberFirstErrorView.setVisibility(View.VISIBLE);
                }
            } else
                numberFirstErrorView.setVisibility(View.VISIBLE);
        } else
            nameFirstErrorView.setVisibility(View.VISIBLE);

        return false;
    }

    private boolean checkNameNumberThreeViews() {

        no1 = getContactFirstNumber().getText().toString().trim();
        no2 = getContactSecondNumber().getText().toString().trim();
        no3 = getContactThirdNumber().getText().toString().trim();

        name1 = getContactFirstName().getText().toString().trim();
        name2 = getContactSecondName().getText().toString().trim();
        name3 = getContactThirdName().getText().toString().trim();


        if (!name1.equals("")) {
            nameFirstErrorView.setVisibility(View.INVISIBLE);
            if (!no1.equals("")) {
                if (no1.length() > 8) {
                    numberFirstErrorView.setVisibility(View.INVISIBLE);
                    if (!name2.equals("")) {
                        nameSecondErrorView.setVisibility(View.INVISIBLE);
                        if (!no2.equals("")) {
                            if (no2.length() > 8) {
                                numberSecondErrorView.setVisibility(View.INVISIBLE);
                                if (!name3.equals("")) {
                                    nameThirdErrorView.setVisibility(View.INVISIBLE);
                                    if (!no3.equals("")) {
                                        if (no3.length() > 8) {
                                            numberThirdErrorView.setVisibility(View.INVISIBLE);
                                            return true;
                                        } else {
                                            number3ErrorText.setText("Please enter a value between 8 to 14 numbers");
                                            numberThirdErrorView.setVisibility(View.VISIBLE);
                                        }
                                    } else
                                        numberThirdErrorView.setVisibility(View.VISIBLE);
                                } else
                                    nameThirdErrorView.setVisibility(View.VISIBLE);
                            } else {
                                number2ErrorText.setText("Please enter a value between 8 to 14 numbers");
                                numberSecondErrorView.setVisibility(View.VISIBLE);
                            }
                        } else
                            numberSecondErrorView.setVisibility(View.VISIBLE);
                    } else
                        nameSecondErrorView.setVisibility(View.VISIBLE);
                } else {
                    number1ErrorText.setText("Please enter a value between 8 to 14 numbers");
                    numberFirstErrorView.setVisibility(View.VISIBLE);
                }
            } else
                numberFirstErrorView.setVisibility(View.VISIBLE);
        } else
            nameFirstErrorView.setVisibility(View.VISIBLE);

        return false;
    }

    private boolean checkNameNumberTwoViews() {

        no1 = getContactFirstNumber().getText().toString().trim();
        no2 = getContactSecondNumber().getText().toString().trim();

        name1 = getContactFirstName().getText().toString().trim();
        name2 = getContactSecondName().getText().toString().trim();

        if (!name1.equals("")) {
            nameFirstErrorView.setVisibility(View.INVISIBLE);
            if (!no1.equals("")) {
                if (no1.length() > 8) {
                    numberFirstErrorView.setVisibility(View.INVISIBLE);
                    if (!name2.equals("")) {
                        nameSecondErrorView.setVisibility(View.INVISIBLE);
                        if (!no2.equals("")) {
                            if (no2.length() > 8) {
                                numberSecondErrorView.setVisibility(View.INVISIBLE);
                                return true;
                            } else {
                                number2ErrorText.setText("Please enter a value between 8 to 14 numbers");
                                numberSecondErrorView.setVisibility(View.VISIBLE);
                            }
                        } else
                            numberSecondErrorView.setVisibility(View.VISIBLE);
                    } else
                        nameSecondErrorView.setVisibility(View.VISIBLE);
                } else {
                    number1ErrorText.setText("Please enter a value between 8 to 14 numbers");
                    numberFirstErrorView.setVisibility(View.VISIBLE);
                }
            } else
                numberFirstErrorView.setVisibility(View.VISIBLE);
        } else
            nameFirstErrorView.setVisibility(View.VISIBLE);

        return false;
    }

    private boolean checkNameNumberOneViews() {

        no1 = getContactFirstNumber().getText().toString().trim();
        name1 = getContactFirstName().getText().toString().trim();

        if (name1.equals("") && no1.equals(""))
            return true;

        if (!name1.equals("")) {
            nameFirstErrorView.setVisibility(View.INVISIBLE);
            if (!no1.equals("")) {
                if (no1.length() > 8) {
                    numberFirstErrorView.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    number1ErrorText.setText("Please enter a value between 8 to 14 numbers");
                    numberFirstErrorView.setVisibility(View.VISIBLE);
                }
            } else
                numberFirstErrorView.setVisibility(View.VISIBLE);
        } else
            nameFirstErrorView.setVisibility(View.VISIBLE);

        return false;
    }

    private boolean checkNameNumberOnlyThirdView() {
        no3 = getContactThirdNumber().getText().toString().trim();
        name3 = getContactThirdName().getText().toString().trim();

        if (!name3.equals("")) {
            nameThirdErrorView.setVisibility(View.INVISIBLE);
            if (!no3.equals("")) {
                if (no3.length() > 8) {
                    numberThirdErrorView.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    number3ErrorText.setText("Please enter a value between 8 to 14 numbers");
                    numberThirdErrorView.setVisibility(View.VISIBLE);
                }
            } else
                numberThirdErrorView.setVisibility(View.VISIBLE);
        } else
            nameThirdErrorView.setVisibility(View.VISIBLE);

        return false;
    }

    private boolean checkNameNumberOnlyFourthView() {
        no4 = getContactFourthNumber().getText().toString().trim();
        name4 = getContactFourthNumber().getText().toString().trim();

        if (!name4.equals("")) {
            nameFourthErrorView.setVisibility(View.INVISIBLE);
            if (!no4.equals("")) {
                if (no4.length() > 8) {
                    numberFourthErrorView.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    number4ErrorText.setText("Please enter a value between 8 to 14 numbers");
                    numberFourthErrorView.setVisibility(View.VISIBLE);
                }
            } else
                numberFourthErrorView.setVisibility(View.VISIBLE);
        } else
            nameFourthErrorView.setVisibility(View.VISIBLE);

        return false;
    }

    private boolean checkNameNumberOnlyFiveView() {

        no5 = getContactFiveNumber().getText().toString().trim();
        name5 = getContactNameFive().getText().toString().trim();

        if (!name5.equals("")) {
            nameFifthErrorView.setVisibility(View.INVISIBLE);
            if (!no5.equals("")) {
                if (no5.length() > 8) {
                    numberFifthErrorView.setVisibility(View.INVISIBLE);

                    return true;
                } else {
                    number5ErrorText.setText("Please enter a value between 8 to 14 numbers");
                    numberFifthErrorView.setVisibility(View.VISIBLE);
                }
            } else
                numberFourthErrorView.setVisibility(View.VISIBLE);
        } else
            nameFifthErrorView.setVisibility(View.VISIBLE);

        return false;
    }

    private void showAlert(String msg) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        getFragmentManager().popBackStack();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setESTDYears() {
        years.add(new YearModel("Select ESTD Year", false));
        for (int i = 1951; i < 2018; i++) {
            if (estd.equals(String.valueOf(i)))
                years.add(new YearModel("" + i, true));
            else
                years.add(new YearModel("" + i, false));
        }

        try {
            yearAdapter = new YearAdapter(context, years);
            getEstdNumber().setAdapter(yearAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        AppUtil.hideKeyBoardWithAllView(context, event);
        return false;
    }

    private void resendOTPRetro(final String newMobile) {

        progressDialog.show();
        sendOtpCall = apiInterface.sendOtpForNew(PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, ""), newMobile, deviceType, deviceToken, userId);
        sendOtpCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                NewResendOTPResponse resendOTPResponse = (NewResendOTPResponse) response.body();
                if (resendOTPResponse != null) {
                    String error = resendOTPResponse.getError();
                    String msg = resendOTPResponse.getMessage();
                    if (error.equals("0")) {
                        Intent intent = new Intent(context, MobileNumberChangeOTPVerifyActivity.class);
                        intent.putExtra("newMobileNumber", newMobile);
                        context.startActivity(intent);
                    } else {
                        if (msg.equalsIgnoreCase("Your session has been expired")) {
                            AppUtil.sessionLogout(msg, context);
                        } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                            AppUtil.sessionLogout(msg, context);
                        else
                            AppUtil.showAlert(msg, context);
                    }
                } else
                    AppUtil.showAlert(getString(R.string.serverError), context);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                progressDialog.dismiss();
                AppUtil.showAlert(getString(R.string.serverError), context);
            }
        });
    }
}
