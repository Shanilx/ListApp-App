package com.listapp.Fragment;

/**
 * Created by Nivesh on 7/17/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.AsyncTask_Utility.MultipartUtility;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.CompanySearch.CompanyDetailResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.CompanySearch.CompanySearchResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SupplierDetails.SupplierProfile;
import com.listapp.Activity.CompanySearchActivity;
import com.listapp.Activity.SuppliersNotFoundActivity;
import com.listapp.Adapter.AuthorisedDealersAdapter;
import com.listapp.Adapter.CompanyDetailListAdapter;
import com.listapp.Adapter.SearchCompanyListAdapter;
import com.listapp.Interface.FragmentBack;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.MarshMallowPermission;
import com.listapp.ListAppUtil.OKayEvent;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;
import com.listapp.xlistview.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class CompanySearchFragment extends Fragment implements FragmentBack, TextWatcher, TextView.OnEditorActionListener, View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    private static final int CAMERA_REQUEST_CODE = 10;
    private static final int FILE_REQUEST_CODE = 11;
    private static final int GALLERY_REQUEST_CODE = 12;
    private ProgressBar searchProgress;
    private FrameLayout suppliersProfile;
    private TextView supplierProfileName;
   // private TextView supplierProfilecityName;
    private ImageView supplierProfileauthorize;
    private ImageView supplierProfilefavourite;
    private ImageView share;
    private TextView supplierProfilecontactPerson1, supplierProfilecontactPerson2, supplierProfilecontactPerson3,
            supplierProfilecontactPerson4, supplierProfilecontactPerson5;
    private TextView supplierProfilearea;
    private TextView supplierProfileaddress;
    private TextView supplierProfilecontctNumber;
    private TextView supplierProfileemail;
    private TextView supplierProfiledrugLicenceNumber;
    private TextView supplierProfiletinNumber;
    private TextView supplierProfileestdYear;
    private ImageView supplierProfileexpandCollapseView;
    private TextView noSuppliers;
    private ListView supplierProfileauthorisedDealerOfList;
    private LinearLayout parentItemListView, companyDetail;
    private XListView suppliersList;
    private ScrollView hideKeyboardView;
    private TextView companyName;
    private TextView searchResultCount;
    private XListView companyList;
    private Context context;
    private boolean isDetailOpened = false, isCompanyDetailOpened = false;
    private String searchedText;
    private Call searchCompanyCall, supplierProfileCall, companyDetailCall;
    private boolean isRunning = false;
    private ProgressDialog progressDialog;
    private APIInterface apiInterface;
    private String mobileNumber, deviceType, deviceToken, userId;
    private String cityID;
    private List<CompanySearchResponse.Datum> companyDataList;
    private boolean authListVisible = false;
    private List<CompanyDetailResponse.Data.Supplier> suppliersDataList;
    private int supplierListOffset = 1;
    private String companyID;
    private int companyListOffset = 1;
    private List<SupplierProfile.Data.Supplier.CompanyDealership> dealersList;
    private LinearLayout viewContact5, viewContact4, viewContact3, viewContact2, viewContact1;

    private RelativeLayout noSupplier;
    private List<SupplierProfile.Data.Supplier.ContactPerson> supplierContactPersonsList;
    public static int afterSubmitNotForm = 0;
    private ImageView contactOneImage;


    private ScrollView parentNoItemsView;
    private TextView shopName;
    private TextView address;
    private TextView mobileNumberText;
    private Spinner searchErrorSpinner;
    private LinearLayout spinnerError;
    private LinearLayout descriptionError;
    private LinearLayout upload2;
    private ImageView upload1;
    private LinearLayout fileNameView;
    private TextView fileNameText;
    private String nameOfSearchedMedicine;
    private MarshMallowPermission marshMallowPermission;
    private Uri fileUri;
    private Button callnoitem;
    private boolean isRestart;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        afterSubmitNotForm = 0;
        return inflater.inflate(R.layout.fragment_company_search, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchProgress = view.findViewById(R.id.searchProgress);
        suppliersProfile = view.findViewById(R.id.suppliersProfile);
        supplierProfileName = view.findViewById(R.id.supplierProfileName);
       // supplierProfilecityName = view.findViewById(R.id.supplierProfilecityName);
        //supplierProfilecityName.setText(PreferenceConnector.readString(context,PreferenceConnector.CITY,""));
        supplierProfileauthorize = view.findViewById(R.id.supplierProfileauthorize);
        supplierProfilefavourite = view.findViewById(R.id.supplierProfilefavourite);
        share = view.findViewById(R.id.share);
        share.setOnClickListener(this);
        share.setVisibility(View.INVISIBLE);
        contactOneImage = view.findViewById(R.id.contactOneImage);
        supplierProfilecontactPerson1 = view.findViewById(R.id.supplierProfilecontactPerson1);
        supplierProfilecontactPerson5 = view.findViewById(R.id.supplierProfilecontactPerson5);
        supplierProfilecontactPerson4 = view.findViewById(R.id.supplierProfilecontactPerson4);
        supplierProfilecontactPerson3 = view.findViewById(R.id.supplierProfilecontactPerson3);
        supplierProfilecontactPerson2 = view.findViewById(R.id.supplierProfilecontactPerson2);

        supplierProfilecontactPerson1.setOnClickListener(this);
        supplierProfilecontactPerson2.setOnClickListener(this);
        supplierProfilecontactPerson3.setOnClickListener(this);
        supplierProfilecontactPerson4.setOnClickListener(this);
        supplierProfilecontactPerson5.setOnClickListener(this);

        viewContact5 = view.findViewById(R.id.viewContact5);
        viewContact4 = view.findViewById(R.id.viewContact4);
        viewContact3 = view.findViewById(R.id.viewContact3);
        viewContact2 = view.findViewById(R.id.viewContact2);
        viewContact1 = view.findViewById(R.id.viewContact1);

        supplierProfilearea = view.findViewById(R.id.supplierProfilearea);
        supplierProfileaddress = view.findViewById(R.id.supplierProfileaddress);
        supplierProfilecontctNumber = view.findViewById(R.id.supplierProfilecontctNumber);
        supplierProfilecontctNumber.setOnClickListener(this);
        supplierProfileemail = view.findViewById(R.id.supplierProfileemail);
        supplierProfiledrugLicenceNumber = view.findViewById(R.id.supplierProfiledrugLicenceNumber);
        supplierProfiletinNumber = view.findViewById(R.id.supplierProfiletinNumber);
        supplierProfileestdYear = view.findViewById(R.id.supplierProfileestdYear);
        supplierProfileexpandCollapseView = view.findViewById(R.id.supplierProfileexpandCollapseView);
        supplierProfileexpandCollapseView.setOnClickListener(this);
        noSuppliers = view.findViewById(R.id.noSuppliers);
        supplierProfileauthorisedDealerOfList = view.findViewById(R.id.supplierProfileauthorisedDealerOfList);
        supplierProfileauthorisedDealerOfList.setOnTouchListener(this);
        parentItemListView = view.findViewById(R.id.parentItemListView);
        searchResultCount = view.findViewById(R.id.searchResultCount);
        companyList = view.findViewById(R.id.comapanyList);
        companyList.setOnTouchListener(this);
        companyName = view.findViewById(R.id.companyName);
        suppliersList = view.findViewById(R.id.suppliersList);
        suppliersList.setOnTouchListener(this);
        companyDetail = view.findViewById(R.id.companyDetail);
        companyList.setXListViewListener(this);
        suppliersDataList = new ArrayList<>();
        hideKeyboardView = view.findViewById(R.id.hideKeyboardView);
        hideKeyboardView.setOnTouchListener(this);
        noSupplier = view.findViewById(R.id.noSupplier);
        view.findViewById(R.id.callNotFoundPage).setOnClickListener(this);
        view.findViewById(R.id.nocompanyfound).setOnClickListener(this);

      //  callnoitem = (Button) view.findViewById(R.id.callnocompany);
      //  callnoitem.setOnClickListener(this);
        parentItemListView = view.findViewById(R.id.parentItemListView);
        searchResultCount = view.findViewById(R.id.searchResultCount);
        parentNoItemsView = view.findViewById(R.id.parentNoItemsView);
        shopName = view.findViewById(R.id.shopName);
        address = view.findViewById(R.id.address);
        mobileNumberText = view.findViewById(R.id.mobileNumber);
        searchErrorSpinner = view.findViewById(R.id.searchErrorSpinner);
        spinnerError = view.findViewById(R.id.spinnerError);
        descriptionError = view.findViewById(R.id.descriptionError);
        view.findViewById(R.id.upload).setOnClickListener(this);
        upload2 = view.findViewById(R.id.upload2);
        upload1 = view.findViewById(R.id.upload1);
        fileNameView = view.findViewById(R.id.fileNameView);
        fileNameText = view.findViewById(R.id.fileNameText);
        view.findViewById(R.id.submit).setOnClickListener(this);
        upload1.setOnClickListener(this);
        upload2.setOnClickListener(this);

        shopName.setText("I am ");
        shopName.append(AppUtil.getHalfBoldString(PreferenceConnector.readString(context, PreferenceConnector.SHOP_NAME, "")));
        address.setText(AppUtil.getHalfBoldString(PreferenceConnector.readString(context, PreferenceConnector.ADDRESS, "")));
        mobileNumberText.setText("RMN ");
        mobileNumberText.append(AppUtil.getHalfBoldString(PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, "")));


        CompanySearchActivity.searchView.setVisibility(View.VISIBLE);
        CompanySearchActivity.heading.setVisibility(View.GONE);
       // CompanySearchActivity.search.setText("");
        CompanySearchActivity.search.addTextChangedListener(textWatcher);
        CompanySearchActivity.search.requestFocus();
        CompanySearchActivity.search.setOnEditorActionListener(this);
        CompanySearchActivity.backButton.setOnClickListener(this);
        CompanySearchActivity.backButton.setVisibility(View.VISIBLE);
        CompanySearchActivity.toolbarSerachIconView.setOnClickListener(this);
        CompanySearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
        CompanySearchActivity.toolbarCloseIconView.setOnClickListener(this);

        AppUtil.showKeyboard(context);
        companyList.setOnTouchListener(this);
        suppliersList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

                suppliersList.stopRefresh();
                suppliersList.stopLoadMore();
                suppliersList.setRefreshTime("Load More Companies");
            }

            @Override
            public void onLoadMore() {

                getCompanyDetail(companyID, String.valueOf(supplierListOffset));
                supplierListOffset += 1;
            }
        });
        suppliersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CheckNetwork.isNetwordAvailable(context)) {
                    position -= 1;
                    if (position >= 0) {
                        String supplierID = suppliersDataList.get(position).getSupplierId();
                        initSupplierProfileDetail(supplierID);
                    } else
                        AppUtil.showAlert("Something goes wrong", context);
                } else
                    AppUtil.showAlert(context.getString(R.string.networkError), context);
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);
        companyDataList = new ArrayList<>();
        companyList.setOnItemClickListener(this);

        mobileNumber = PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, "");
        userId = PreferenceConnector.readString(context, PreferenceConnector.USER_ID, "");
        deviceType = PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TOKEN, "");
        cityID = PreferenceConnector.readString(context, PreferenceConnector.CITY_ID, "");

        hideViews();
    }

    private EditText getDescription() {
        return (EditText) getView().findViewById(R.id.description);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            try {
                if (CheckNetwork.isNetwordAvailable(context)) {
                    searchedText = s.toString().trim();
                    int length = searchedText.length();
                    if (length > 0) {
                        CompanySearchActivity.toolbarSerachIconView.setVisibility(View.GONE);
                        CompanySearchActivity.toolbarCloseIconView.setVisibility(View.VISIBLE);
                    } else {
                        CompanySearchActivity.toolbarSerachIconView.setVisibility(View.VISIBLE);
                        CompanySearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
                    }

                    if (length > 0) {
                        searchProgress.setVisibility(View.VISIBLE);
                        if (!isRunning) {
                            countDownTimer.start();
                            if (searchCompanyCall != null) {
                                if (!searchCompanyCall.isExecuted()) {
                                    searchCompanyCall.cancel();
                                }
                            }
                        } else {
                            if (searchCompanyCall != null) {
                                if (!searchCompanyCall.isExecuted()) {
                                    searchCompanyCall.cancel();
                                }
                            }
                            countDownTimer.cancel();
                            countDownTimer.start();
                        }
                    } else {
                        if (searchCompanyCall != null) {
                            if (!searchCompanyCall.isExecuted()) {
                                searchCompanyCall.cancel();
                            }
                        }
                        searchProgress.setVisibility(View.GONE);
                        countDownTimer.cancel();
                    }
                } else {
                    AppUtil.showAlert(context.getString(R.string.networkError), context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void didSearch() {

        AppUtil.hideKeyBoard(context);
        if (CheckNetwork.isNetwordAvailable(context)) {
            if (searchedText != null) {
                if (searchedText.trim().length() > 0) {
                    searchProgress.setVisibility(View.VISIBLE);
                    if (!isRunning) {
                        countDownTimer.start();
                    } else {
                        if (searchCompanyCall != null) {
                            if (!searchCompanyCall.isExecuted()) {
                                searchCompanyCall.cancel();
                            }
                        }
                        countDownTimer.cancel();
                        countDownTimer.start();
                    }
                } else {
                    searchProgress.setVisibility(View.GONE);
                    AppUtil.showAlert(" Please Search with at least 2 characters", context);
                    countDownTimer.cancel();
                    if (searchCompanyCall != null) {
                        if (!searchCompanyCall.isExecuted()) {
                            searchCompanyCall.cancel();
                        }
                    }
                }
            } else {
                searchProgress.setVisibility(View.GONE);
                AppUtil.showAlert(" Please Search with at least 2 characters", context);
                if (isRunning) {
                    countDownTimer.cancel();
                    if (searchCompanyCall != null) {
                        if (!searchCompanyCall.isExecuted()) {
                            searchCompanyCall.cancel();
                        }
                    }
                }
            }
        } else
            AppUtil.showAlert(context.getString(R.string.networkError), context);
    }

    private void hideViews() {
        try {
            parentItemListView.setVisibility(View.GONE);
            suppliersProfile.setVisibility(View.GONE);
            companyDetail.setVisibility(View.GONE);
            isCompanyDetailOpened = false;
            isDetailOpened = false;
            noSupplier.setVisibility(View.GONE);
            parentNoItemsView.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showListView() {
        parentItemListView.setVisibility(View.VISIBLE);
        suppliersProfile.setVisibility(View.GONE);
        companyDetail.setVisibility(View.GONE);
        parentNoItemsView.setVisibility(View.GONE);
       // callnoitem.setVisibility(View.VISIBLE);
        isCompanyDetailOpened = false;
        isDetailOpened = false;
    }

    private void showCompanyDetail() {
        isCompanyDetailOpened = true;
        isDetailOpened = false;
        parentItemListView.setVisibility(View.GONE);
        suppliersProfile.setVisibility(View.GONE);
        parentNoItemsView.setVisibility(View.GONE);
        companyDetail.setVisibility(View.VISIBLE);
    }

    private void showSupplierDetail() {
        isCompanyDetailOpened = true;
        isDetailOpened = true;
        parentItemListView.setVisibility(View.GONE);
        suppliersProfile.setVisibility(View.VISIBLE);
        parentNoItemsView.setVisibility(View.GONE);
        companyDetail.setVisibility(View.GONE);
    }

    private void showNoItem() {
        isCompanyDetailOpened = false;
        isDetailOpened = false;
        parentNoItemsView.setVisibility(View.VISIBLE);
        parentItemListView.setVisibility(View.GONE);
        suppliersProfile.setVisibility(View.GONE);
        companyDetail.setVisibility(View.GONE);
        noItemView();
    }

    private void noItemView() {
        try {
            nameOfSearchedMedicine = searchedText;
            getDescription().setText(searchedText);

            ArrayList<String> spinnerErrorList = new ArrayList<>();
            spinnerErrorList.add("Not Finding Company");
            ArrayAdapter cityAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerErrorList);
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            searchErrorSpinner.setAdapter(cityAdapter);
            spinnerError.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPressed() {

        if (isDetailOpened) {
            showCompanyDetail();
        } else if (isCompanyDetailOpened) {
            showListView();
        } else {
           // CompanySearchActivity.search.removeTextChangedListener(textWatcher);
            AppUtil.hideKeyBoard(context);
            Intent i= getActivity().getIntent();
            getActivity().finish();
            context.startActivity(i);


        }
        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            if (CheckNetwork.isNetwordAvailable(context)) {
                searchedText = s.toString().trim();
                int length = searchedText.length();
                if (length > 0) {
                    CompanySearchActivity.toolbarSerachIconView.setVisibility(View.GONE);
                    CompanySearchActivity.toolbarCloseIconView.setVisibility(View.VISIBLE);
                } else {
                    CompanySearchActivity.toolbarSerachIconView.setVisibility(View.VISIBLE);
                    CompanySearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
                }

                if (length > 0) {
                    searchProgress.setVisibility(View.VISIBLE);
                    if (!isRunning) {
                        countDownTimer.start();
                        if (searchCompanyCall != null) {
                            if (!searchCompanyCall.isExecuted()) {
                                searchCompanyCall.cancel();
                            }
                        }
                    } else {
                        if (searchCompanyCall != null) {
                            if (!searchCompanyCall.isExecuted()) {
                                searchCompanyCall.cancel();
                            }
                        }
                        countDownTimer.cancel();
                        countDownTimer.start();
                    }
                } else {
                    if (searchCompanyCall != null) {
                        if (!searchCompanyCall.isExecuted()) {
                            searchCompanyCall.cancel();
                        }
                    }
                    searchProgress.setVisibility(View.GONE);
                    countDownTimer.cancel();
                }
            } else {
                AppUtil.showAlert(context.getString(R.string.networkError), context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            didSearch();
            return true;
        } else
            return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isRestart)
        {

            startActivity(new Intent(context,CompanySearchActivity.class));
            getActivity().finish();
            isRestart = false;
        }
        if (afterSubmitNotForm == 1) {
            AppUtil.hideKeyBoard(context);
            getFragmentManager().popBackStack();
            CompanyHomeFragment.hideFragment();
        }
    }

    @Override
    public void onClick(View v) {

        if (isRunning)
            countDownTimer.cancel();

        switch (v.getId()) {

            case R.id.upload:
                uploadEvent();
                break;
            case R.id.upload1:
                uploadEvent();
                break;
            case R.id.upload2:
                uploadEvent();
                break;
            case R.id.submit:
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                submitEvent();
                break;

            case R.id.supplierProfilecontctNumber:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + supplierProfilecontctNumber.getText().toString().trim())));
                break;

            case R.id.supplierProfilecontactPerson1:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + supplierContactPersonsList.get(0).getContactNumber().trim())));
                break;

            case R.id.supplierProfilecontactPerson2:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + supplierContactPersonsList.get(1).getContactNumber().trim())));
                break;

            case R.id.supplierProfilecontactPerson3:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + supplierContactPersonsList.get(2).getContactNumber().trim())));
                break;

            case R.id.supplierProfilecontactPerson4:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + supplierContactPersonsList.get(3).getContactNumber().trim())));
                break;

            case R.id.supplierProfilecontactPerson5:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + supplierContactPersonsList.get(4).getContactNumber().trim())));
                break;

            case R.id.share:

                if(supplierContactPersonsList!=null) {
                    StringBuilder shareText = new StringBuilder("Supplier details for " + companyName.getText().toString() + "\nSupplier Name: " + supplierProfileName.getText().toString().trim());
                    int length = supplierContactPersonsList.size();
                    if (length != 0) {
                        for (int i = 0; i < length; i++) {
                            shareText.append("\n" + supplierContactPersonsList.get(i).getContactName()
                                    + ": " + supplierContactPersonsList.get(i).getContactNumber());
                        }
                    }
                    AppUtil.shareOnWhatsApp(shareText.toString(), context);
                }
                break;

            case R.id.callNotFoundPage:

                Intent intent = new Intent(context, SuppliersNotFoundActivity.class);
                intent.putExtra("text", companyName.getText().toString().trim());
                intent.putExtra("keyWord", "Company");
                startActivity(intent);
                break;

            case R.id.toolbarBackButton:
                onBackPressed();
                break;

            case R.id.toolbarSerachIconView:
                didSearch();
                break;

            case R.id.toolbarCloseIconView:
                CompanySearchActivity.search.setText("");
                searchedText = "";
                CompanySearchActivity.toolbarSerachIconView.setVisibility(View.VISIBLE);
                break;

            case R.id.supplierProfileexpandCollapseView:
                if (authListVisible) {
                    authListVisible = false;
                    supplierProfileexpandCollapseView.setImageResource(R.drawable.collapse);
                } else {
                    authListVisible = true;
                    supplierProfileexpandCollapseView.setImageResource(R.drawable.expand);
                }
                break;
//            case R.id.supplierProfilecityName:
//                Intent city=new Intent(context, CitySearchActivity.class);
//                city.putExtra("company","true");
//                isRestart=true;
//                AppUtil.hideKeyBoard(context);
//                startActivity(city);
//                fetchCompanyList(searchedText, String.valueOf(companyListOffset));
//                break;
            case R.id.nocompanyfound:
                showNoItem();
                break;

        }
    }

    private void initSupplierProfileDetail(String supplierId) {

        if (progressDialog == null) {
            progressDialog = AppUtil.createProgressDialog(context);
            if (!progressDialog.isShowing())
                progressDialog.show();
        } else {
            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        supplierProfileCall = apiInterface.getSupplierProfile(mobileNumber, userId, deviceType, deviceToken, supplierId);
        supplierProfileCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                if (response != null) {
                    SupplierProfile supplierProfile = (SupplierProfile) response.body();
                    if (supplierProfile != null) {
                        String error = supplierProfile.getError();
                        String msg = supplierProfile.getMessage();
                        if (error.equals("0")) {

                            showSupplierDetail();
                            isDetailOpened = true;
                            showSupplierDetail();
                            setSupplierProfileDetail(supplierProfile.getData());
                        } else {
                            if (msg.equalsIgnoreCase("Your session has been expired")) {
                                AppUtil.sessionLogout(msg, getContext());
                            } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                AppUtil.sessionLogout(msg, getContext());
                            else
                                showCompanyDetail();
                            AppUtil.showAlert(msg, context);
                        }
                    } else {
                        showCompanyDetail();
                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                    }
                } else {
                    showCompanyDetail();
                    AppUtil.showAlert(context.getString(R.string.serverError), context);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(progressDialog!=null)
                progressDialog.dismiss();
                showCompanyDetail();
                AppUtil.showAlert(context.getString(R.string.serverError), context);
            }
        });
    }


    private void setSupplierProfileDetail(SupplierProfile.Data data) {

        if (data.getSupplier()!=null && data.getSupplier().size() != 0) {
            hideKeyboardView.scrollTo(0, 0);
            isDetailOpened = true;
            supplierProfilearea.setText("");
            //supplierProfilecityName.setText("");
            supplierProfileaddress.setText("");
            supplierProfilecontctNumber.setText("");
            supplierProfiledrugLicenceNumber.setText("");
            supplierProfiletinNumber.setText("");
            supplierProfileName.setText("");
            supplierProfileestdYear.setText("");
            supplierProfileemail.setText("");
            supplierProfilecontactPerson1.setText("");

            SupplierProfile.Data.Supplier supplier = data.getSupplier().get(0);
            supplierProfilearea.setText(supplier.getArea());
            //supplierProfilecityName.setText(supplier.getCity().toUpperCase());
            //supplierProfilecityName.setOnClickListener(this);
            supplierProfileaddress.setText(supplier.getAddress());
            supplierProfilecontctNumber.setText(supplier.getContactNumber());
            supplierProfiledrugLicenceNumber.setText(supplier.getDlnNo());
            supplierProfiletinNumber.setText(supplier.getTlnNo());
            supplierProfileName.setText(supplier.getShopName());
            supplierProfileestdYear.setText(supplier.getEstdNo());
            supplierProfileemail.setText(supplier.getEmail());
            supplierContactPersonsList = supplier.getContactPerson();
            if(supplierContactPersonsList!=null) {
                int length = supplierContactPersonsList.size();
                if (length > 0) {
                    contactOneImage.setVisibility(View.VISIBLE);
                    if (length == 1) {
                        supplierProfilecontactPerson1.setText(supplierContactPersonsList.get(0).getContactName() + " : " +
                                supplierContactPersonsList.get(0).getContactNumber());
                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.GONE);
                        viewContact3.setVisibility(View.GONE);
                        viewContact4.setVisibility(View.GONE);
                        viewContact5.setVisibility(View.GONE);
                    } else if (length == 2) {
                        supplierProfilecontactPerson1.setText(supplierContactPersonsList.get(0).getContactName() + " : " +
                                supplierContactPersonsList.get(0).getContactNumber());
                        supplierProfilecontactPerson2.setText(supplierContactPersonsList.get(1).getContactName() + " : " +
                                supplierContactPersonsList.get(1).getContactNumber());

                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.VISIBLE);
                        viewContact3.setVisibility(View.GONE);
                        viewContact4.setVisibility(View.GONE);
                        viewContact5.setVisibility(View.GONE);
                    } else if (length == 3) {
                        supplierProfilecontactPerson1.setText(supplierContactPersonsList.get(0).getContactName() + " : " +
                                supplierContactPersonsList.get(0).getContactNumber());
                        supplierProfilecontactPerson2.setText(supplierContactPersonsList.get(1).getContactName() + " : " +
                                supplierContactPersonsList.get(1).getContactNumber());
                        supplierProfilecontactPerson3.setText(supplierContactPersonsList.get(2).getContactName() + " : " +
                                supplierContactPersonsList.get(2).getContactNumber());
                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.VISIBLE);
                        viewContact3.setVisibility(View.VISIBLE);
                        viewContact4.setVisibility(View.GONE);
                        viewContact5.setVisibility(View.GONE);
                    } else if (length == 4) {
                        supplierProfilecontactPerson1.setText(supplierContactPersonsList.get(0).getContactName() + " : " +
                                supplierContactPersonsList.get(0).getContactNumber());
                        supplierProfilecontactPerson2.setText(supplierContactPersonsList.get(1).getContactName() + " : " +
                                supplierContactPersonsList.get(1).getContactNumber());
                        supplierProfilecontactPerson3.setText(supplierContactPersonsList.get(2).getContactName() + " : " +
                                supplierContactPersonsList.get(2).getContactNumber());
                        supplierProfilecontactPerson4.setText(supplierContactPersonsList.get(3).getContactName() + " : " +
                                supplierContactPersonsList.get(3).getContactNumber());
                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.VISIBLE);
                        viewContact3.setVisibility(View.VISIBLE);
                        viewContact4.setVisibility(View.VISIBLE);
                        viewContact5.setVisibility(View.GONE);
                    } else if (length == 5) {
                        supplierProfilecontactPerson1.setText(supplierContactPersonsList.get(0).getContactName() + " : " +
                                supplierContactPersonsList.get(0).getContactNumber());
                        supplierProfilecontactPerson2.setText(supplierContactPersonsList.get(1).getContactName() + " : " +
                                supplierContactPersonsList.get(1).getContactNumber());
                        supplierProfilecontactPerson3.setText(supplierContactPersonsList.get(1).getContactName() + " : " +
                                supplierContactPersonsList.get(1).getContactNumber());
                        supplierProfilecontactPerson4.setText(supplierContactPersonsList.get(3).getContactName() + " : " +
                                supplierContactPersonsList.get(3).getContactNumber());
                        supplierProfilecontactPerson5.setText(supplierContactPersonsList.get(4).getContactName() + " : " +
                                supplierContactPersonsList.get(4).getContactNumber());

                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.VISIBLE);
                        viewContact3.setVisibility(View.VISIBLE);
                        viewContact4.setVisibility(View.VISIBLE);
                        viewContact5.setVisibility(View.VISIBLE);
                    } else if (length > 5) {
                        supplierProfilecontactPerson1.setText(supplierContactPersonsList.get(0).getContactName() + " : " +
                                supplierContactPersonsList.get(0).getContactNumber());
                        supplierProfilecontactPerson2.setText(supplierContactPersonsList.get(1).getContactName() + " : " +
                                supplierContactPersonsList.get(1).getContactNumber());
                        supplierProfilecontactPerson3.setText(supplierContactPersonsList.get(1).getContactName() + " : " +
                                supplierContactPersonsList.get(1).getContactNumber());
                        supplierProfilecontactPerson4.setText(supplierContactPersonsList.get(3).getContactName() + " : " +
                                supplierContactPersonsList.get(3).getContactNumber());
                        supplierProfilecontactPerson5.setText(supplierContactPersonsList.get(4).getContactName() + " : " +
                                supplierContactPersonsList.get(4).getContactNumber());

                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.VISIBLE);
                        viewContact3.setVisibility(View.VISIBLE);
                        viewContact4.setVisibility(View.VISIBLE);
                        viewContact5.setVisibility(View.VISIBLE);
                    } else {
                        supplierProfilecontactPerson1.setText("-");
                        contactOneImage.setVisibility(View.GONE);
                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.GONE);
                        viewContact3.setVisibility(View.GONE);
                        viewContact4.setVisibility(View.GONE);
                        viewContact5.setVisibility(View.GONE);
                        supplierContactPersonsList.clear();
                    }
                } else {
                    contactOneImage.setVisibility(View.GONE);
                    supplierProfilecontactPerson1.setText("-");
                    viewContact1.setVisibility(View.VISIBLE);
                    viewContact2.setVisibility(View.GONE);
                    viewContact3.setVisibility(View.GONE);
                    viewContact4.setVisibility(View.GONE);
                    viewContact5.setVisibility(View.GONE);
                    supplierContactPersonsList.clear();
                }
            } else {
                contactOneImage.setVisibility(View.GONE);
                supplierProfilecontactPerson1.setText("-");
                viewContact1.setVisibility(View.VISIBLE);
                viewContact2.setVisibility(View.GONE);
                viewContact3.setVisibility(View.GONE);
                viewContact4.setVisibility(View.GONE);
                viewContact5.setVisibility(View.GONE);
                supplierContactPersonsList.clear();
            }

            if (supplier.getAuthorised().equals("Yes"))
                supplierProfileauthorize.setImageResource(R.drawable.black_authroze);
            else
                supplierProfileauthorize.setImageResource(R.drawable.yellow_bg);
            if (supplier.getFavouriteStatus().equals("Yes"))
                supplierProfilefavourite.setImageResource(R.drawable.favourite_icon);
            else
                supplierProfilefavourite.setImageResource(R.drawable.black_unfavourite);

            dealersList = supplier.getCompanyDealership();
            if (dealersList!=null && dealersList.size() > 0) {
                Collections.sort(dealersList, new Comparator<SupplierProfile.Data.Supplier.CompanyDealership>() {
                    @Override
                    public int compare(SupplierProfile.Data.Supplier.CompanyDealership o1, SupplierProfile.Data.Supplier.CompanyDealership o2) {
                        return o1.getCompanyName().compareToIgnoreCase(o2.getCompanyName());
                    }
                });
                AuthorisedDealersAdapter authorisedDealersAdapter = new AuthorisedDealersAdapter(context, dealersList);
                supplierProfileauthorisedDealerOfList.setAdapter(authorisedDealersAdapter);
                noSuppliers.setVisibility(View.GONE);
                supplierProfileexpandCollapseView.setVisibility(View.VISIBLE);
            } else {
                noSuppliers.setVisibility(View.VISIBLE);
                supplierProfileexpandCollapseView.setVisibility(View.GONE);
            }
        }
    }

    CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long l) {
            isRunning = true;
        }

        @Override
        public void onFinish() {

            isRunning = false;
            if (searchCompanyCall != null) {
                if (!searchCompanyCall.isExecuted()) {
                    searchCompanyCall.cancel();
                    fetchCompanyList(searchedText, "0");
                } else
                    fetchCompanyList(searchedText, "0");
            } else
                fetchCompanyList(searchedText, "0");
            companyListOffset = 0;
        }
    };

    private void fetchCompanyList(final String text, final String s) {

        if (CheckNetwork.isNetwordAvailable(context)) {

            if (s.equals("0")) {
                searchProgress.setVisibility(View.VISIBLE);
                hideViews();
            }

            searchCompanyCall = apiInterface.searchCompany(mobileNumber, userId, deviceType, deviceToken, text, s, cityID);
            searchCompanyCall.enqueue(new Callback() {
                private FirebaseAnalytics mFirebaseAnalytics;

                @Override
                public void onResponse(Call call, Response response) {

                    if (s.equals("0"))
                        searchProgress.setVisibility(View.GONE);

                    if (response != null) {
                        CompanySearchResponse companySearchResponse = (CompanySearchResponse) response.body();
                        if (companySearchResponse != null) {
                            String error, msg;
                            error = companySearchResponse.getError();
                            msg = companySearchResponse.getMessage();
                            if (error.equals("0")) {
                                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                                Bundle bundle = new Bundle();
                                String firetxt=PreferenceConnector.readString(context,PreferenceConnector.MOBILE_NUMBER,"")+" : "+text;
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,firetxt);
                                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"Company");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);
                                showListView();
                                if (s.equals("0")) {
                                    companyDataList.clear();
                                    companyDataList = companySearchResponse.getData();

                                    int length = companyDataList.size();
                                    if (length > 25)
                                    {
                                        companyList.setPullLoadEnable(true);


                                    }
                                    else {

                                        companyList.setPullLoadEnable(false);
                                    }

                                    searchResultCount.setText(length + " Results for " + text);
                                    SearchCompanyListAdapter searchComapnyListAdapter = new SearchCompanyListAdapter(context, companyDataList);
                                    companyList.setAdapter(searchComapnyListAdapter);
                                } else if (companyDataList != null) {
                                    onLoad();
                                    int length = companyDataList.size();
                                    companyDataList.addAll(companySearchResponse.getData());
                                    searchResultCount.setText(length + " Results for " + text);
                                    SearchCompanyListAdapter searchSupplierListAdapter = new SearchCompanyListAdapter(context, companyDataList);
                                    companyList.setAdapter(searchSupplierListAdapter);
                                    companyList.setSelection(length - 1);
                                }
                            } else {
                                if (msg.equalsIgnoreCase("Your session has been expired")) {
                                    AppUtil.sessionLogout(msg, getContext());
                                } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                    AppUtil.sessionLogout(msg, getContext());
                                else if (!s.equals("0")) {
                                    AppUtil.showAlert(msg, context);
                                    onLoad();
                                } else {
                                    onLoad();
                                    showNoItem();
                                }
                            }
                        } else {
                            onLoad();
                            AppUtil.showAlert(context.getString(R.string.serverError), context);
                        }
                    } else {
                        onLoad();
                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    onLoad();
                    if (s.equals("0"))
                        searchProgress.setVisibility(View.GONE);
                    AppUtil.showAlert(context.getString(R.string.serverError), context);
                }
            });
        } else {
            try {
                onLoad();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AppUtil.showAlert(context.getString(R.string.networkError), context);
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

        if (companyListOffset == 0)
            companyListOffset += 1;

        fetchCompanyList(searchedText, String.valueOf(companyListOffset));
        companyListOffset += 1;
    }

    private void onLoad() {

        try {
            companyList.stopRefresh();
            companyList.stopLoadMore();
            companyList.setRefreshTime("Load More Companies");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLoadSupplierList() {

        suppliersList.stopRefresh();
        suppliersList.stopLoadMore();
        suppliersList.setRefreshTime("Load More Suppliers");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        position -= 1;
        if (position >= 0) {
            if (companyDataList != null) {
                companyID = companyDataList.get(position).getCompanyId();
                getCompanyDetail(companyID, "0");
                AppUtil.hideKeyBoard(context);
                supplierListOffset = 0;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        AppUtil.hideKeyBoardWithAllView(context, event);
        return false;
    }

    private void getCompanyDetail(String companyID, final String offset) {
        if (CheckNetwork.isNetwordAvailable(context)) {

            if (progressDialog == null) {
                progressDialog = AppUtil.createProgressDialog(context);
                if (!progressDialog.isShowing())
                    progressDialog.show();
            } else {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }

            companyDetailCall = apiInterface.companyDetail(mobileNumber, userId, deviceType, deviceToken, companyID, offset, cityID);
            companyDetailCall.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if(progressDialog!=null)
                    progressDialog.dismiss();
                    onLoadSupplierList();
                    if (response != null) {
                        CompanyDetailResponse companyDetailResponse = (CompanyDetailResponse) response.body();
                        if (companyDetailResponse != null) {
                            String error = companyDetailResponse.getError();
                            String msg = companyDetailResponse.getMessage();
                            if (error.equals("0")) {
                                if (companyDetailResponse.getData().getCompanyDetail().size() != 0) {
                                    companyName.setText(companyDetailResponse.getData().getCompanyDetail().get(0).getCompanyName());
                                    if (offset.equals("0")) {
                                        showCompanyDetail();
                                        suppliersDataList.clear();
                                        suppliersDataList = companyDetailResponse.getData().getSuppliers();
                                        int length = suppliersDataList.size();
                                        if (length > 0) {

                                            if (length > 25 ) {
                                                suppliersList.setPullLoadEnable(true);

                                            }
                                            else {
                                                suppliersList.setPullLoadEnable(false);

                                            }
                                            noSupplier.setVisibility(View.GONE);
                                            suppliersList.setVisibility(View.VISIBLE);
                                            CompanyDetailListAdapter companyDetailListAdapter = new CompanyDetailListAdapter(context, suppliersDataList);
                                            suppliersList.setAdapter(companyDetailListAdapter);
                                        } else {
                                            noSupplier.setVisibility(View.VISIBLE);
                                            suppliersList.setVisibility(View.GONE);
                                        }
                                    } else {
                                        suppliersDataList.addAll(companyDetailResponse.getData().getSuppliers());
                                        int length = suppliersDataList.size();
                                        if (length > 0) {
                                            noSupplier.setVisibility(View.GONE);
                                            suppliersList.setVisibility(View.VISIBLE);
                                            CompanyDetailListAdapter companyDetailListAdapter = new CompanyDetailListAdapter(context, suppliersDataList);
                                            suppliersList.setAdapter(companyDetailListAdapter);
                                        } else {
                                            noSupplier.setVisibility(View.VISIBLE);
                                            suppliersList.setVisibility(View.GONE);
                                        }
                                    }
                                } else
                                    AppUtil.okayEventDialog("This Company has been deactivated by administrator", context, new OKayEvent() {
                                        @Override
                                        public void okayEvent(boolean b) {
                                            showListView();
                                        }
                                    });
                            } else {
                                if (msg.equalsIgnoreCase("Your session has been expired")) {
                                    AppUtil.sessionLogout(msg, getContext());
                                } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                    AppUtil.sessionLogout(msg, getContext());
                                else if (!offset.equals("0")) {
                                    AppUtil.showAlert(msg, context);
                                    onLoadSupplierList();
                                } else {
                                    showListView();
                                    AppUtil.showAlert(msg, context);
                                }
                            }
                        }
                    } else {
                        showListView();
                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    if(progressDialog!=null)
                    progressDialog.dismiss();
                    onLoadSupplierList();
                    showListView();
                    AppUtil.showAlert(context.getString(R.string.serverError), context);
                }
            });
        } else {
            onLoadSupplierList();
            AppUtil.showAlert(context.getString(R.string.networkError), context);
        }
    }

    private void submitEvent() {

        final String query = "Not Finding Company";
        final String description = getDescription().getText().toString().trim();
        if (!query.equals("")) {
            spinnerError.setVisibility(View.INVISIBLE);
            if (!description.equals("")) {
                descriptionError.setVisibility(View.INVISIBLE);

                if (CheckNetwork.isNetwordAvailable(context)) {
                    if (progressDialog == null) {
                        progressDialog = AppUtil.createProgressDialog(context);
                        if (!progressDialog.isShowing())
                            progressDialog.show();
                    } else {
                        if (!progressDialog.isShowing())
                            progressDialog.show();
                    }
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            demoUpload(query, description);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            progressDialog.dismiss();
                            try {
                                if (error.equals("0")) {

                                    if (picturePath != null)
                                        showAlert("Your Company request has been sent successfully");
                                    else
                                        showAlert("Your Company request has been sent successfully");
                                } else {
                                    if (msg.equalsIgnoreCase("Your session has been expired")) {
                                        AppUtil.sessionLogout(msg, getContext());
                                    } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                        AppUtil.sessionLogout(msg, getContext());
                                    else
                                        AppUtil.showAlert(msg, context);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute();
                } else
                    AppUtil.showAlert(context.getString(R.string.networkError), context);
            } else
                descriptionError.setVisibility(View.VISIBLE);
        } else
            spinnerError.setVisibility(View.VISIBLE);
    }

    String error, msg;

    private void demoUpload(String query, String description) {
        File file = null;
        if (picturePath != null)
            file = new File(picturePath);
        String response = null;
        try {
            MultipartUtility multipart = new MultipartUtility(APIInterface.COMPANY_NOT_FOUND, "UTF-8");
            multipart.addFormField("user_id", userId);
            multipart.addFormField("mobile_no", mobileNumber);
            multipart.addFormField("device_type", deviceType);
            multipart.addFormField("device_token", deviceToken);
            multipart.addFormField("issue", query);
            multipart.addFormField("product_name", nameOfSearchedMedicine);
            multipart.addFormField("city", PreferenceConnector.readString(context,PreferenceConnector.CITY,""));
            multipart.addFormField("description", description);
            multipart.addFormField("request_type", "2");
            if (file != null && file.isFile() && file.exists())
                multipart.addFilePart("file_name", file);
            response = multipart.finish();
            try {
                JSONObject jsonObject = new JSONObject(response);
                error = jsonObject.optString("error");
                msg = jsonObject.optString("message");
            } catch (JSONException e) {
                e.printStackTrace();
                AppUtil.showAlert(context.getString(R.string.serverError), context);
            }
        } catch (IOException e) {
            progressDialog.dismiss();
            error = "1";
            msg = "Something went wrong";
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppUtil.hideKeyBoard(context);
                        getFragmentManager().popBackStack();
                        CompanyHomeFragment.hideFragment();
                        CompanySearchActivity.search.removeTextChangedListener(textWatcher);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void uploadEvent() {
        marshMallowPermission = new MarshMallowPermission(getActivity());
        if (marshMallowPermission.checkPermissionForStorage()) {
            final CharSequence[] options = {"Take Photo", "Upload Photo", "Upload File",
                    "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Upload Photo")) {
                        callGallery();
                    } else if (options[item].equals("Take Photo")) {
                        callCamera();
                    } else if (options[item].equals("Upload File")) {
                        callSdCard();
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else
            marshMallowPermission.requestPermissionForStorage();
    }

    public void callCamera() {
        try {
            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = AppUtil.getOutputMediaFile(1, context);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent1, CAMERA_REQUEST_CODE);
        } catch (Exception e) {
            AppUtil.showAlert("Something went wrong", context);
        }
    }

    private void callSdCard() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        try {
            startActivityForResult(intent, FILE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callGallery() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String picturePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case CAMERA_REQUEST_CODE:

                    File newfile = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name) + " Images/" + AppUtil.fileName);
                    if (newfile.exists()) {
                        picturePath = newfile.getAbsolutePath();
                        fileNameView.setVisibility(View.VISIBLE);
                        fileNameText.setText(newfile.getName());
                        fileNameView.setVisibility(View.VISIBLE);
                        fileNameText.setText(newfile.getName());
                    }
                    break;

                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = context.getContentResolver().query(
                            selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    fileNameView.setVisibility(View.VISIBLE);
                    fileNameText.setText(new File(picturePath).getName());
                    c.close();
                    break;

                case FILE_REQUEST_CODE:
                    picturePath = data.getData().getPath();
                    if (picturePath.endsWith(".txt") || picturePath.endsWith(".doc") || picturePath.endsWith(".docx") || picturePath.endsWith(".xls") || picturePath.endsWith(".xlsx") || picturePath.endsWith(".pdf")) {
                        fileNameView.setVisibility(View.VISIBLE);
                        fileNameText.setText(new File(picturePath).getName());
                    } else {
                        AppUtil.showAlert("Invalid file format. Please upload valid format like .txt, .doc, .docx, .xls, .xlsx, .pdf", context);
                        fileNameView.setVisibility(View.INVISIBLE);
                        fileNameText.setText("");
                        picturePath = null;
                    }
                    break;
            }
        } else {
            fileNameView.setVisibility(View.INVISIBLE);
            fileNameText.setText("");
            picturePath = null;
        }
    }

}
