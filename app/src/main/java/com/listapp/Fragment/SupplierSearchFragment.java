package com.listapp.Fragment;

/**
 * Created by syscraft on 7/17/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.CompanySearch.CompanyDetailResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SearchSupplier.SearchSupplierResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SupplierDetails.SupplierProfile;
import com.listapp.Activity.SupplierSearchActivity;
import com.listapp.Activity.SuppliersNotFoundActivity;
import com.listapp.Adapter.AuthorisedDealersAdapter;
import com.listapp.Adapter.CompanyDetailListAdapter;
import com.listapp.Adapter.SearchSupplierListAdapter;
import com.listapp.Interface.FragmentBack;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;
import com.listapp.xlistview.XListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierSearchFragment extends Fragment implements FragmentBack, TextWatcher, TextView.OnEditorActionListener, View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener, View.OnTouchListener {

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
    private LinearLayout parentItemListView;
    private TextView searchResultCount;
    private XListView supplierList;
    private Context context;
    private boolean isDetailOpened = false;
    private String searchedText;
    private retrofit2.Call searchSupplierCall, supplierProfileCall;
    private boolean isRunning = false;
    private ProgressDialog progressDialog;
    private APIInterface apiInterface;
    private String mobileNumber, deviceType, deviceToken, userId;
    private String cityID;
    private List<SearchSupplierResponse.Datum> suppliersList;
    private boolean authListVisible = false;
    private int supplierListOffset = 0;
    private LinearLayout companyDetail;
    private TextView companyName;
    private XListView companyDetailList;
    private String companyID;
    private int companyListOffset = 1;
    private boolean isCompanyDetailOpened = false;
    private Call<CompanyDetailResponse> companyDetailCall;
    private List<CompanyDetailResponse.Data.Supplier> companyDataList;
    private List<SupplierProfile.Data.Supplier.CompanyDealership> dealersList;
    private ScrollView hideKeyboardView1;
    private List<SupplierProfile.Data.Supplier.ContactPerson> supplierContactPersonsList;
    private LinearLayout viewContact5, viewContact4, viewContact3, viewContact2, viewContact1;

    public static int afterSubmitNotForm = 0;
    private ImageView contactOneImage;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_supplier_search, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchProgress = view.findViewById(R.id.searchProgress);
        suppliersProfile = view.findViewById(R.id.suppliersProfile);
        supplierProfileName = view.findViewById(R.id.supplierProfileName);
       // supplierProfilecityName = view.findViewById(R.id.supplierProfilecityName);
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
        supplierList = view.findViewById(R.id.supplierList);
        supplierList.setXListViewListener(this);
        hideKeyboardView1 = view.findViewById(R.id.hideKeyboardView1);
        hideKeyboardView1.setOnTouchListener(this);

        SupplierSearchActivity.searchView.setVisibility(View.VISIBLE);
        SupplierSearchActivity.heading.setVisibility(View.GONE);
       // SupplierSearchActivity.search.setText("");
        SupplierSearchActivity.search.addTextChangedListener(textWatcher);
        SupplierSearchActivity.search.requestFocus();
        SupplierSearchActivity.search.setOnEditorActionListener(this);
        SupplierSearchActivity.backButton.setOnClickListener(this);
        SupplierSearchActivity.backButton.setVisibility(View.VISIBLE);
        SupplierSearchActivity.toolbarSerachIconView.setOnClickListener(this);
        SupplierSearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
        SupplierSearchActivity.toolbarCloseIconView.setOnClickListener(this);
      //  supplierProfilecityName.setText(PreferenceConnector.readString(context,PreferenceConnector.CITY,""));

        AppUtil.showKeyboard(context);
        supplierList.setOnTouchListener(this);
        companyDetailList = view.findViewById(R.id.companyDetailList);
        companyDetailList.setOnTouchListener(this);
        companyName = view.findViewById(R.id.companyName);
        companyDetail = view.findViewById(R.id.companyDetail);
        companyDataList = new ArrayList<>();
        companyDetailList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

                getCompanyDetail(companyID, String.valueOf(companyListOffset));
                companyListOffset += 1;
            }
        });

        supplierProfileauthorisedDealerOfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position >= 0) {
                    if (dealersList != null && dealersList.size() > 0) {
                        companyID = dealersList.get(position).getCompanyId();
                        getCompanyDetail(companyID, "0");
                    }
                } else
                    AppUtil.showAlert("Something goes wrong", context);
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);
        suppliersList = new ArrayList<>();
        supplierList.setOnItemClickListener(this);

        view.findViewById(R.id.nosupplierfound).setOnClickListener(this);

        mobileNumber = PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, "");
        userId = PreferenceConnector.readString(context, PreferenceConnector.USER_ID, "");
        deviceType = PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TOKEN, "");
        cityID = PreferenceConnector.readString(context, PreferenceConnector.CITY_ID, "");
        hideViews();


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
                        SupplierSearchActivity.toolbarSerachIconView.setVisibility(View.GONE);
                        SupplierSearchActivity.toolbarCloseIconView.setVisibility(View.VISIBLE);
                    } else {
                        SupplierSearchActivity.toolbarSerachIconView.setVisibility(View.VISIBLE);
                        SupplierSearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
                    }

                    if (length > 0) {
                        searchProgress.setVisibility(View.VISIBLE);
                        if (!isRunning) {
                            countDownTimer.start();
                            if (searchSupplierCall != null)
                                if (!searchSupplierCall.isExecuted())
                                    searchSupplierCall.cancel();
                        } else {
                            countDownTimer.cancel();
                            countDownTimer.start();
                            if (searchSupplierCall != null)
                                if (!searchSupplierCall.isExecuted())
                                    searchSupplierCall.cancel();
                        }
                    } else {
                        searchProgress.setVisibility(View.GONE);
                        countDownTimer.cancel();
                        if (searchSupplierCall != null)
                            if (!searchSupplierCall.isExecuted())
                                searchSupplierCall.cancel();
                    }
                } else
                    AppUtil.showAlert(context.getString(R.string.networkError), context);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void didSearch() {

        AppUtil.hideKeyBoard(context);

        if (searchedText != null) {
            if (searchedText.trim().length() > 0) {
                searchProgress.setVisibility(View.VISIBLE);
                if (!isRunning) {
                    countDownTimer.start();
                } else {
                    if (searchSupplierCall != null)
                        if (!searchSupplierCall.isExecuted())
                            searchSupplierCall.cancel();
                    countDownTimer.cancel();
                    countDownTimer.start();
                }
            } else {
                searchProgress.setVisibility(View.GONE);
                AppUtil.showAlert("Please Search with at least 2 characters", context);
                countDownTimer.cancel();
                if (searchSupplierCall != null)
                    if (!searchSupplierCall.isExecuted())
                        searchSupplierCall.cancel();
            }
        } else {
            searchProgress.setVisibility(View.GONE);
            AppUtil.showAlert("Please Search with at least 2 characters", context);
            if (isRunning)
                countDownTimer.cancel();
            if (searchSupplierCall != null)
                if (!searchSupplierCall.isExecuted())
                    searchSupplierCall.cancel();
        }
    }

    private void hideViews() {
        isCompanyDetailOpened = false;
        isDetailOpened = false;
        parentItemListView.setVisibility(View.GONE);
        suppliersProfile.setVisibility(View.GONE);
        companyDetail.setVisibility(View.GONE);
    }

    private void showListView() {
        isCompanyDetailOpened = false;
        isDetailOpened = false;
        parentItemListView.setVisibility(View.VISIBLE);
        suppliersProfile.setVisibility(View.GONE);
        companyDetail.setVisibility(View.GONE);
    }

    private void showCompanyDetail() {

        isCompanyDetailOpened = true;
        isDetailOpened = true;
        parentItemListView.setVisibility(View.GONE);
        suppliersProfile.setVisibility(View.GONE);
        companyDetail.setVisibility(View.VISIBLE);
    }

    private void showProfileDetail() {
        isCompanyDetailOpened = false;
        isDetailOpened = true;
        parentItemListView.setVisibility(View.GONE);
        suppliersProfile.setVisibility(View.VISIBLE);
        companyDetail.setVisibility(View.GONE);
    }

    @Override
    public boolean onBackPressed() {

        if (isCompanyDetailOpened) {
            showProfileDetail();
        } else if (isDetailOpened) {
            showListView();
        } else {
            SupplierSearchActivity.search.removeTextChangedListener(textWatcher);
            AppUtil.hideKeyBoard(context);
            Intent intent = new Intent(getContext(), SupplierSearchActivity.class);
            startActivity(intent);
            hideViews();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (afterSubmitNotForm == 1) {
            AppUtil.hideKeyBoard(context);
        }
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
                    SupplierSearchActivity.toolbarSerachIconView.setVisibility(View.GONE);
                    SupplierSearchActivity.toolbarCloseIconView.setVisibility(View.VISIBLE);
                } else {
                    SupplierSearchActivity.toolbarSerachIconView.setVisibility(View.VISIBLE);
                    SupplierSearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
                }

                if (length > 0) {
                    searchProgress.setVisibility(View.VISIBLE);
                    if (!isRunning) {
                        countDownTimer.start();
                        if (searchSupplierCall != null)
                            if (!searchSupplierCall.isExecuted())
                                searchSupplierCall.cancel();
                    } else {
                        countDownTimer.cancel();
                        countDownTimer.start();
                        if (searchSupplierCall != null)
                            if (!searchSupplierCall.isExecuted())
                                searchSupplierCall.cancel();
                    }
                } else {
                    searchProgress.setVisibility(View.GONE);
                    countDownTimer.cancel();
                    if (searchSupplierCall != null)
                        if (!searchSupplierCall.isExecuted())
                            searchSupplierCall.cancel();
                }
            } else
                AppUtil.showAlert(context.getString(R.string.networkError), context);
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
    public void onClick(View v) {

        if (isRunning)
            countDownTimer.cancel();

        switch (v.getId()) {

            case R.id.callNoItem:
                Intent intent=new Intent(context, SuppliersNotFoundActivity.class);
                intent.putExtra("text",searchedText);
                intent.putExtra("keyword","");
                startActivity(intent);
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

            case R.id.toolbarBackButton:
                onBackPressed();
                break;

            case R.id.toolbarSerachIconView:
                didSearch();
                break;

            case R.id.share:

                if (supplierContactPersonsList != null) {
                    StringBuilder shareText = new StringBuilder("Contact details of " + supplierProfileName.getText().toString().trim());
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

            case R.id.toolbarCloseIconView:
                SupplierSearchActivity.search.setText("");
                searchedText = "";
                SupplierSearchActivity.toolbarSerachIconView.setVisibility(View.VISIBLE);
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
            case R.id.nosupplierfound:
                Intent intents = new Intent(context, SuppliersNotFoundActivity.class);
                intents.putExtra("text", searchedText);
                intents.putExtra("keyWord", "Supplier");
                startActivity(intents);
                break;
        }
    }

    private void initSupplierProfileDetail(String supplierID) {

        if (progressDialog == null) {
            progressDialog = AppUtil.createProgressDialog(context);
            if (!progressDialog.isShowing())
                progressDialog.show();
        } else {
            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        supplierProfileCall = apiInterface.getSupplierProfile(mobileNumber, userId, deviceType, deviceToken, supplierID);
        supplierProfileCall.enqueue(new Callback() {
            @Override
            public void onResponse(retrofit2.Call call, Response response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                if (response != null) {
                    SupplierProfile supplierProfile = (SupplierProfile) response.body();
                    if (supplierProfile != null) {
                        String error = supplierProfile.getError();
                        String msg = supplierProfile.getMessage();
                        if (error.equals("0")) {
                            showProfileDetail();
                            setSupplierProfileDetail(supplierProfile.getData());
                        } else {
                            if (msg.equalsIgnoreCase("Your session has been expired")) {
                                AppUtil.sessionLogout(msg, getContext());
                            } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                AppUtil.sessionLogout(msg, getContext());
                            else {
                                showListView();
                                AppUtil.showAlert(msg, context);
                            }
                        }
                    } else {
                        showListView();
                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                    }
                } else {
                    showListView();
                    AppUtil.showAlert(context.getString(R.string.serverError), context);
                }
            }

            @Override
            public void onFailure(retrofit2.Call call, Throwable t) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                showListView();
                AppUtil.showAlert(context.getString(R.string.serverError), context);
            }
        });
    }


    private void setSupplierProfileDetail(SupplierProfile.Data data) {

        if (data.getSupplier() != null && data.getSupplier().size() != 0) {
            hideKeyboardView1.scrollTo(0, 0);
            isDetailOpened = true;
            supplierProfilearea.setText("");
           // supplierProfilecityName.setText("");
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
           // supplierProfilecityName.setText(supplier.getCity().toUpperCase());

            supplierProfileaddress.setText(supplier.getAddress());
            supplierProfilecontctNumber.setText(supplier.getContactNumber());
            supplierProfiledrugLicenceNumber.setText(supplier.getDlnNo());
            supplierProfiletinNumber.setText(supplier.getTlnNo());
            supplierProfileName.setText(supplier.getShopName());
            supplierProfileestdYear.setText(supplier.getEstdNo());
            supplierProfileemail.setText(supplier.getEmail());
            supplierContactPersonsList = supplier.getContactPerson();
            if (supplierContactPersonsList != null) {
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
                supplierProfilecontactPerson1.setText("-");
                contactOneImage.setVisibility(View.GONE);
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
                supplierProfileauthorize.setColorFilter(R.color.primaryYellow);
            if (supplier.getFavouriteStatus().equals("Yes"))
                supplierProfilefavourite.setImageResource(R.drawable.favourite_icon);
            else
                supplierProfilefavourite.setImageResource(R.drawable.black_unfavourite);

            dealersList = supplier.getCompanyDealership();
            if (dealersList != null) {
                if (dealersList.size() > 0) {
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
            if (searchSupplierCall != null) {
                if (!searchSupplierCall.isExecuted()) {
                    searchSupplierCall.cancel();
                    fetchSupplierList(searchedText, "0");
                } else
                    fetchSupplierList(searchedText, "0");
            } else
                fetchSupplierList(searchedText, "0");
            supplierListOffset = 0;
        }
    };

    private void fetchSupplierList(final String text, final String s) {

        if (CheckNetwork.isNetwordAvailable(context)) {

            if (s.equals("0")) {
                searchProgress.setVisibility(View.VISIBLE);
                hideViews();
            }

            searchSupplierCall = apiInterface.searchSupplier(mobileNumber, userId, deviceType, deviceToken, text, s, cityID);
        searchSupplierCall.enqueue(new Callback() {
            private FirebaseAnalytics mFirebaseAnalytics;

            @Override
            public void onResponse(Call call, Response response) {

                if (s.equals("0"))
                    searchProgress.setVisibility(View.GONE);

                if (response != null) {
                    SearchSupplierResponse searchSupplierResponse = (SearchSupplierResponse) response.body();
                    if (searchSupplierResponse != null) {
                        String error, msg;
                        error = searchSupplierResponse.getError();
                        msg = searchSupplierResponse.getMessage();
                        if (error.equals("0")) {
                            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                            Bundle bundle = new Bundle();
                            String firetxt=PreferenceConnector.readString(context,PreferenceConnector.MOBILE_NUMBER,"")+" : "+text;
                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,firetxt);
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"Supplier");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);

                            showListView();
                            if (s.equals("0")) {
                                if (supplierList != null)
                                    suppliersList.clear();
                                suppliersList = searchSupplierResponse.getData();
                                if (supplierList != null) {
                                    int length = suppliersList.size();
                                    if (length > 25)
                                        supplierList.setPullLoadEnable(true);
                                    else
                                        supplierList.setPullLoadEnable(false);

                                    searchResultCount.setText(length + " Results for " + text);
                                    SearchSupplierListAdapter searchSupplierListAdapter = new SearchSupplierListAdapter(context, suppliersList);
                                    supplierList.setAdapter(searchSupplierListAdapter);
                                }
                            } else {
                                if (suppliersList != null) {
                                    onLoad();
                                    int length = suppliersList.size();
                                    suppliersList.addAll(searchSupplierResponse.getData());
                                    searchResultCount.setText(length + " Results for " + text);
                                    SearchSupplierListAdapter searchSupplierListAdapter = new SearchSupplierListAdapter(context, suppliersList);
                                    supplierList.setAdapter(searchSupplierListAdapter);
                                    supplierList.setSelection(length-1);
                                }
                            }
                        } else {
                            if (msg.equalsIgnoreCase("Your session has been expired")) {
                                AppUtil.sessionLogout(msg, getContext());
                            } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                AppUtil.sessionLogout(msg, getContext());
                            else {
                                onLoad();
                                AppUtil.showAlert(msg, context);
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
        if (supplierListOffset == 0)
            supplierListOffset += 1;

        fetchSupplierList(searchedText, String.valueOf(supplierListOffset));
        supplierListOffset += 1;
    }

    private void onLoad() {
        supplierList.stopRefresh();
        supplierList.stopLoadMore();
        supplierList.setRefreshTime("Load More Suppliers");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (CheckNetwork.isNetwordAvailable(context)) {
            position -= 1;
            if (position >= 0) {
                if (suppliersList != null) {
                    String supplierID = suppliersList.get(position).getSupplierId();
                    initSupplierProfileDetail(supplierID);
                    AppUtil.hideKeyBoard(context);
                }
            }
        } else
            AppUtil.showAlert(context.getString(R.string.networkError), context);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        AppUtil.hideKeyBoardWithAllView(context, event);
        return false;
    }

    private void getCompanyDetail(String companyID, final String offset) {
        if (CheckNetwork.isNetwordAvailable(context)) {

            if (offset.equals("0")) {
                if (progressDialog == null) {
                    progressDialog = AppUtil.createProgressDialog(context);
                    if (!progressDialog.isShowing())
                        progressDialog.show();
                } else {
                    if (!progressDialog.isShowing())
                        progressDialog.show();
                }
            }

            companyDetailCall = apiInterface.companyDetail(mobileNumber, userId, deviceType, deviceToken, companyID, offset,cityID);
            companyDetailCall.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (offset.equals("0")) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                    onLoadCompanyDetail();
                    if (response != null) {
                        CompanyDetailResponse companyDetailResponse = (CompanyDetailResponse) response.body();
                        if (companyDetailResponse != null) {
                            String error = companyDetailResponse.getError();
                            String msg = companyDetailResponse.getMessage();
                            if (error.equals("0")) {
                                companyName.setText(companyDetailResponse.getData().getCompanyDetail().get(0).getCompanyName());
                                if (offset.equals("0")) {
                                    showCompanyDetail();
                                    companyDataList.clear();
                                    companyDataList = companyDetailResponse.getData().getSuppliers();
                                    if (companyDataList != null) {
                                        int length = companyDataList.size();
                                        if (length > 0) {

                                            if (length > 25)
                                                companyDetailList.setPullLoadEnable(true);
                                            else
                                                companyDetailList.setPullLoadEnable(false);

                                            CompanyDetailListAdapter companyDetailListAdapter = new CompanyDetailListAdapter(context, companyDataList);
                                            companyDetailList.setAdapter(companyDetailListAdapter);
                                        } else {
                                            showProfileDetail();
                                            AppUtil.showAlert("Company Detail not available", context);
                                        }
                                    } else {
                                        showProfileDetail();
                                        AppUtil.showAlert("Company Detail not available", context);
                                    }
                                } else {
                                    companyDataList.addAll(companyDetailResponse.getData().getSuppliers());
                                    int length = companyDataList.size();
                                    if (length > 0) {
                                        CompanyDetailListAdapter companyDetailListAdapter = new CompanyDetailListAdapter(context, companyDataList);
                                        companyDetailList.setAdapter(companyDetailListAdapter);
                                    } else {
                                        showProfileDetail();
                                        AppUtil.showAlert("Company Detail not available", context);
                                    }
                                }
                            } else {
                                if (msg.equalsIgnoreCase("Your session has been expired")) {
                                    AppUtil.sessionLogout(msg, getContext());
                                } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                    AppUtil.sessionLogout(msg, getContext());
                                else if (!offset.equals("0")) {
                                    AppUtil.showAlert(msg, context);
                                    onLoadCompanyDetail();
                                } else {
                                    showProfileDetail();
                                    AppUtil.showAlert(msg, context);
                                }
                            }
                        }
                    } else {
                        showProfileDetail();
                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    if (offset.equals("0")) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                    onLoadCompanyDetail();
                    showProfileDetail();
                    AppUtil.showAlert(context.getString(R.string.serverError), context);
                }
            });
        } else {
            onLoadCompanyDetail();
            showProfileDetail();
            AppUtil.showAlert(context.getString(R.string.networkError), context);
        }
    }

    private void onLoadCompanyDetail() {
        companyDetailList.stopRefresh();
        companyDetailList.stopLoadMore();
        companyDetailList.setRefreshTime("Load More Suppliers");
    }


}
