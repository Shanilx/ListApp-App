package com.listapp.Fragment;

/**
 * Created by Nivesh on 6/23/2017.
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.City.Cities;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.ProductDetails;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SupplierDetails.SupplierProfile;
import com.listapp.Activity.CitySearchActivity;
import com.listapp.Activity.CompanySearchActivity;
import com.listapp.Adapter.CityListBaseAdapter;
import com.listapp.Interface.FragmentBack;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.MarshMallowPermission;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;
import com.listapp.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitySearchFragment extends Fragment implements View.OnClickListener, FragmentBack, TextView.OnEditorActionListener, XListView.IXListViewListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    public static int afterSubmitNotForm = 0;
    private static FrameLayout productDetailView;
    private static ScrollView parentNoItemsView;
    public static boolean serchResult = false;
    private TextView shopName;
    private TextView address;
    private TextView mobileNumber;
    private Spinner searchErrorSpinner;
    private LinearLayout spinnerError, descriptionError, fileNameView;
    private static LinearLayout parentItemListView;
    private static TextView searchResultCount;
    private XListView searchResultList;
    private ProgressBar searchProgress;
    private Call searchProductCall, supplierProfileCall;
    private List<String> productList;
    private String text;
    private int offset = 1;
    private LinearLayout upload2;

    private ImageView medicineType;
    private TextView medicineName;
    private ImageView medicineFavourite;
    private ImageView expandCollapseView;
    private LinearLayout expandCollapseViewLayout;
    private TextView manufacturedBy;
    private TextView composition, price, typeOfProduct;
    private TextView usedForTreatment;
    private TextView packingDetail;
    private TextView cityName;
    private ImageView expandCollapseViewSupplierList;
    private XListView suppliersListView;
    private ScrollView parentDetailView;
    private boolean detailViewStatus = false, listViewStatus = false;
    private APIInterface apiInterface;
    private Call<ProductDetails> productDetailCall;
    private ProgressDialog progressDialog;
    private String mobileNumber1;
    private String userId;
    private String deviceType, deviceToken, cityID;
    private String productID = null;
    private List<ProductDetails.Data.Supplier> supplierList = null;
    private ProductDetails.Data.Product product = null;
    private String type;
    private boolean noItemBoolean = false;
    private boolean productDetailBoolean = false;
    private boolean supplierDetailBoolean = false;
    private Context context;

    private FrameLayout suppliersProfile;
    private TextView supplierProfileName;
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
    private ListView supplierProfileauthorisedDealerOfList;
    private boolean dealersListHide = false;
    private MarshMallowPermission marshMallowPermission;
    private TextView fileNameText;
    private int supplierListOffset = 1;
    private RelativeLayout noSupplier;
    private ScrollView hideKeyboardView1;
    private Uri fileUri;
    private TextView noSuppliers;
    private List<SupplierProfile.Data.Supplier.ContactPerson> supplierContactPersonsList;
    private String nameOfSearchedMedicine;
    private View view;
    private LinearLayout viewContact5, viewContact4, viewContact3, viewContact2, viewContact1;
    private Button callNotFoundPage;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__home_search, null);
        afterSubmitNotForm = 0;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productDetailView = view.findViewById(R.id.productDetailView);
        parentNoItemsView = view.findViewById(R.id.parentNoItemsView);
        shopName = view.findViewById(R.id.shopName);
        address = view.findViewById(R.id.address);
        mobileNumber = view.findViewById(R.id.mobileNumber);
        searchErrorSpinner = view.findViewById(R.id.searchErrorSpinner);
        spinnerError = view.findViewById(R.id.spinnerError);
        view.findViewById(R.id.upload).setOnClickListener(this);
        view.findViewById(R.id.upload2).setOnClickListener(this);
        view.findViewById(R.id.submit).setOnClickListener(this);
        parentItemListView = view.findViewById(R.id.parentItemListView);
        searchResultCount = view.findViewById(R.id.searchResultCount);
        searchResultList = view.findViewById(R.id.searchResultList);
        searchProgress = view.findViewById(R.id.searchProgress);
        searchProgress.setVisibility(View.GONE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        descriptionError = view.findViewById(R.id.descriptionError);
        fileNameView = view.findViewById(R.id.fileNameView);
        fileNameText = view.findViewById(R.id.fileNameText);

        mobileNumber1 = PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, "");
        userId = PreferenceConnector.readString(context, PreferenceConnector.USER_ID, "");
        deviceType = PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TOKEN, "");
        cityID = PreferenceConnector.readString(context, PreferenceConnector.CITY_ID, "");
        searchResultList.setXListViewListener(this);
     //   searchResultList.setPullLoadEnable(false);



        parentItemListView.setVisibility(View.GONE);
        productDetailView.setVisibility(View.GONE);
        parentNoItemsView.setVisibility(View.GONE);

        hideKeyboardView1 = view.findViewById(R.id.hideKeyboardView1);
        hideKeyboardView1.setOnTouchListener(this);

        showKeyboard(context);
        searchResultList.setOnItemClickListener(this);
        CitySearchActivity.searchView.setVisibility(View.VISIBLE);
        CitySearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
        CitySearchActivity.backButton.setVisibility(View.VISIBLE);
        CitySearchActivity.heading.setVisibility(View.GONE);
        CitySearchActivity.searchView.requestFocus();
        CitySearchActivity.searchiconView.setOnClickListener(this);
        CitySearchActivity.search.setOnEditorActionListener(this);
        CitySearchActivity.backButton.setOnClickListener(this);
        CitySearchActivity.toolbarCloseIconView.setOnClickListener(this);

        parentDetailView = view.findViewById(R.id.parentDetailView);
        medicineType = view.findViewById(R.id.medicineType);
        medicineName = view.findViewById(R.id.medicineName);
        medicineFavourite = view.findViewById(R.id.medicine_favourite);
        expandCollapseView = view.findViewById(R.id.expandCollapseView);
        expandCollapseViewLayout = view.findViewById(R.id.expandCollapseViewLayout);
        manufacturedBy = view.findViewById(R.id.manufacturedBy);
        composition = view.findViewById(R.id.composition);
        usedForTreatment = view.findViewById(R.id.usedForTreatment);
        packingDetail = view.findViewById(R.id.packingDetail);
        price = view.findViewById(R.id.price);
        typeOfProduct = view.findViewById(R.id.typeOfProduct);
        cityName = view.findViewById(R.id.cityName);
        expandCollapseViewSupplierList = view.findViewById(R.id.expandCollapseViewSupplierList);
        suppliersListView = view.findViewById(R.id.suppliersList);
        suppliersListView.setOnTouchListener(this);
        noSupplier = view.findViewById(R.id.noSupplier);
        expandCollapseViewLayout.setOnClickListener(this);
        medicineName.setOnClickListener(this);
        expandCollapseViewSupplierList.setOnClickListener(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mobileNumber1 = PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, "");
        userId = PreferenceConnector.readString(context, PreferenceConnector.USER_ID, "");
        deviceType = PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TYPE, "Android");
        deviceToken = PreferenceConnector.readString(context, PreferenceConnector.DEVICE_TOKEN, "");
        cityID = PreferenceConnector.readString(context, PreferenceConnector.CITY_ID, "");
        CitySearchActivity.backButton.setOnClickListener(this);
        noSuppliers = view.findViewById(R.id.noSuppliers);
        noSuppliers.setVisibility(View.GONE);


        shopName.setText("I am ");
        shopName.append(AppUtil.getHalfBoldString(PreferenceConnector.readString(context, PreferenceConnector.SHOP_NAME, "")));
        address.setText(AppUtil.getHalfBoldString(PreferenceConnector.readString(context, PreferenceConnector.ADDRESS, "")));
        mobileNumber.setText("RMN ");
        mobileNumber.append(AppUtil.getHalfBoldString(PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, "")));



        searchResultList.setOnTouchListener(this);

        suppliersProfile = view.findViewById(R.id.suppliersProfile);
        suppliersProfile.setVisibility(View.GONE);
        supplierProfileName = view.findViewById(R.id.supplierProfileName);
        supplierProfileauthorize = view.findViewById(R.id.supplierProfileauthorize);
        supplierProfilefavourite = view.findViewById(R.id.supplierProfilefavourite);
        share = view.findViewById(R.id.share);
        share.setOnClickListener(this);

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
        supplierProfileauthorisedDealerOfList = view.findViewById(R.id.supplierProfileauthorisedDealerOfList);
        supplierProfileauthorisedDealerOfList.setOnTouchListener(this);
        supplierProfileexpandCollapseView.setOnClickListener(this);

        callNotFoundPage = view.findViewById(R.id.callNotFoundPage);
        callNotFoundPage.setVisibility(View.INVISIBLE);
        callNotFoundPage.setOnClickListener(this);

        fetchProductList(" ","");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
                    text = s.toString().trim();
                    int length = s.length();
                    if (length > 0) {
                        CitySearchActivity.searchiconView.setVisibility(View.GONE);
                        CitySearchActivity.toolbarCloseIconView.setVisibility(View.VISIBLE);
                    } else {
                        CitySearchActivity.searchiconView.setVisibility(View.VISIBLE);
                        CitySearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
                    }

                    if (text.length() > 0) {
                        searchProgress.setVisibility(View.GONE);
                        if (!isRunning) {
                            if (searchProductCall != null)
                                if (!searchProductCall.isExecuted())
                                    searchProductCall.cancel();
                            countDownTimer.start();
                        } else {
                            countDownTimer.cancel();
                            countDownTimer.start();
                            if (searchProductCall != null)
                                if (!searchProductCall.isExecuted())
                                    searchProductCall.cancel();
                        }
                    } else {
                        searchProgress.setVisibility(View.GONE);
                        countDownTimer.cancel();
                        if (searchProductCall != null)
                            if (!searchProductCall.isExecuted())
                                searchProductCall.cancel();
                    }
                } else
                    AppUtil.showAlert(context.getString(R.string.networkError), context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        CitySearchActivity.search.addTextChangedListener(textWatcher);
        if (afterSubmitNotForm == 1) {
            AppUtil.hideKeyBoard(context);
            CityHomeFragment.hideFragmentView();
            getFragmentManager().popBackStack();
            parentItemListView.setVisibility(View.GONE);
            suppliersProfile.setVisibility(View.GONE);
            CitySearchActivity.search.removeTextChangedListener(textWatcher);
        }
    }

    private EditText getDescription() {
        return (EditText) view.findViewById(R.id.description);
    }

    @Override
    public void onClick(View view) {

        if (isRunning)
            countDownTimer.cancel();

        switch (view.getId()) {

            case R.id.toolbarCloseIconView:
                CitySearchActivity.search.setText("");
                text = "";
                CitySearchActivity.searchiconView.setVisibility(View.VISIBLE);
                break;



            case R.id.toolbarBackButton:
                onBackPressed();
                break;

            case R.id.toolbarSerachIconView:
                didSearch();
                break;
        }
    }

    private void didSearch() {

        AppUtil.hideKeyBoard(context);
        if (text != null) {
            if (text.trim().length() > 0) {
                searchProgress.setVisibility(View.GONE);
                if (!isRunning) {
                    if (searchProductCall != null)
                        if (!searchProductCall.isExecuted())
                            searchProductCall.cancel();
                    countDownTimer.start();

                } else {
                    if (searchProductCall != null)
                        if (!searchProductCall.isExecuted())
                            searchProductCall.cancel();
                    countDownTimer.cancel();
                    countDownTimer.start();

                }
            } else {
                searchProgress.setVisibility(View.GONE);
                AppUtil.showAlert("Please Search with at least 1 character", context);
                countDownTimer.cancel();
                if (searchProductCall != null)
                    if (!searchProductCall.isExecuted())
                        searchProductCall.cancel();
            }
        } else {
            searchProgress.setVisibility(View.GONE);
            AppUtil.showAlert("Please Search with at least 1 characters", context);
            if (isRunning)
                countDownTimer.cancel();
            if (searchProductCall != null)
                if (!searchProductCall.isExecuted())
                    searchProductCall.cancel();
        }
    }


    @Override
    public boolean onBackPressed() {
        if (isRunning) {
            countDownTimer.cancel();
            if (searchProductCall != null)
                if (!searchProductCall.isExecuted())
                    searchProductCall.cancel();
        }
        if (supplierDetailBoolean) {
        } else if (productDetailBoolean) {
            serchResult = true;
            productDetailBoolean = false;
            parentItemListView.setVisibility(View.VISIBLE);
            productDetailView.setVisibility(View.GONE);
        } else if (noItemBoolean) {

            AppUtil.hideKeyBoard(context);
            parentItemListView.setVisibility(View.GONE);
            parentNoItemsView.setVisibility(View.GONE);
            suppliersProfile.setVisibility(View.GONE);
        } else if (serchResult) {

            AppUtil.hideKeyBoard(context);
            parentItemListView.setVisibility(View.GONE);
            parentNoItemsView.setVisibility(View.GONE);
            suppliersProfile.setVisibility(View.GONE);
        } else {
            AppUtil.hideKeyBoard(context);
            parentItemListView.setVisibility(View.GONE);
            parentNoItemsView.setVisibility(View.GONE);
            suppliersProfile.setVisibility(View.GONE);
            CitySearchActivity.search.removeTextChangedListener(textWatcher);
        }
        return true;
    }

    @Override
    public int getBackPriority() {
        return HIGH_BACK_PRIORITY;
    }

    public static void hideFragmentView() {

        productDetailView.setVisibility(View.GONE);
        if (serchResult) {
            parentItemListView.setVisibility(View.VISIBLE);
            parentNoItemsView.setVisibility(View.GONE);
        } else {
            parentItemListView.setVisibility(View.GONE);
            parentNoItemsView.setVisibility(View.VISIBLE);
        }
    }

    private void fetchProductList(final String text, final String offset1) {

        if (CheckNetwork.isNetwordAvailable(context)) {

            if (offset1.equals("0")) {
                searchProgress.setVisibility(View.VISIBLE);
                parentItemListView.setVisibility(View.GONE);
                parentNoItemsView.setVisibility(View.GONE);
                suppliersProfile.setVisibility(View.GONE);
                productDetailView.setVisibility(View.GONE);
                serchResult = false;
                productDetailBoolean = false;
                supplierDetailBoolean = false;
                noItemBoolean = false;
            }
            if(productList!=null)
                populateList(text);
            else{
            searchProductCall = apiInterface.getSupplierCity();
            searchProductCall.enqueue(new Callback() {
                private List<String> tmplist;

                @Override
                public void onResponse(Call call, Response response) {

                    if (offset1.equals("0")) {
                        searchProgress.setVisibility(View.GONE);
                    } else
                        onLoad();

                    if (response != null) {
                        Cities searchProductResponse = (Cities) response.body();
                        if (searchProductResponse != null) {
                            parentItemListView.setVisibility(View.VISIBLE);
                            parentNoItemsView.setVisibility(View.GONE);
                            productDetailView.setVisibility(View.GONE);
                            productList = searchProductResponse.getmData();
                            int length = productList.size();
                            if (productList != null) {
                                if(text!=null && text!=""){

                                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, text);
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "City");
                                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                                    int j=0;
                                    tmplist= new ArrayList<>();
                                    for(int i=0;i<length;i++){
                                        if(productList.get(i).toLowerCase().contains(text.toLowerCase())){
                                        tmplist.add(productList.get(i));
                                        }
                                    }
                                }
                                if (length > 0) {
//                                    if (length > 25) {
//                                        searchResultList.setPullLoadEnable(true);
//                                    } else
//                                        searchResultList.setPullLoadEnable(false);

                                    try {
                                        if(tmplist.isEmpty()){tmplist=productList;}
                                        CityListBaseAdapter productListAdapter = new CityListBaseAdapter(context, tmplist);
                                            searchResultList.setAdapter(productListAdapter);
                                           } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    serchResult = true;
                                }

                            } else {
                            }
                        }
                    } else
                        AppUtil.showAlert(context.getString(R.string.serverError), context);

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    serchResult = false;
                    if (offset1.equals("0"))
                        searchProgress.setVisibility(View.GONE);
                    else
                        onLoad();

                    AppUtil.showAlert(context.getString(R.string.serverError), context);
                }
            });
        }} else {
            try {
                onLoad();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AppUtil.showAlert(context.getString(R.string.networkError), context);
        }
    }

    private void populateList(String text) {
        int length = productList.size();
        if (productList != null) {
            List<String> tmplist = null;

            if(text!=null && text!=""){
                tmplist= new ArrayList<>();
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, text);
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "City");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                int j=0;
                for(int i=0;i<length;i++){
                    if(productList.get(i).toLowerCase().contains(text.toLowerCase())){
                        tmplist.add(productList.get(i));
                    }
                }
            }
            if (length > 0) {
//                                    if (length > 25) {
//                                        searchResultList.setPullLoadEnable(true);
//                                    } else
//                                        searchResultList.setPullLoadEnable(false);

                try {
                    if(tmplist.isEmpty()){
                        AppUtil.showAlert("The city you searched for is not available yet.", context);
                        tmplist=productList;}
                    CityListBaseAdapter productListAdapter = new CityListBaseAdapter(context, tmplist);
                    searchResultList.setAdapter(productListAdapter);
                    searchProgress.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                serchResult = true;

            }

        } else {
        }


    }

    public static void showKeyboard(Context context) {
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            didSearch();
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {

    }

    private void onLoad() {
        searchResultList.stopRefresh();
        searchResultList.stopLoadMore();
        searchResultList.setRefreshTime("Load More Products");
    }

    @Override
    public void onLoadMore() {
        if (offset == 0)
            offset += 1;
        fetchProductList(text, String.valueOf(offset));
        offset += 1;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView city = view.findViewById(R.id.storeName);
            String cityname = city.getText().toString();
            PreferenceConnector.writeString(context,PreferenceConnector.CITY,cityname);
            Call<Cities> getcityid = apiInterface.getCityID(cityname);
            getcityid.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response != null) {

                    Cities data = (Cities)response.body();
                    String cityid = data.getmData().get(0);
                    PreferenceConnector.writeString(context, PreferenceConnector.CITY_ID, cityid);
                    Intent i=getActivity().getIntent();
                    Bundle b=i.getExtras();
                    if(b!=null)
                    {
                            getActivity().startActivity(new Intent(getContext(), CompanySearchActivity.class));

                    }
                    getActivity().finish();
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {

                AppUtil.showAlert(context.getString(R.string.serverError), context);
            }
        });


    }




    private boolean isRunning = false;

    CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long l) {
            isRunning = true;
        }

        @Override
        public void onFinish() {

            isRunning = false;
            if (searchProductCall != null) {
                if (!searchProductCall.isExecuted()) {
                    searchProductCall.cancel();
                    fetchProductList(text.trim(), "0");
                } else
                    fetchProductList(text.trim(), "0");
            } else
                fetchProductList(text.trim(), "0");
            offset = 0;
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        AppUtil.hideKeyBoardWithAllView(context, event);
        return false;
    }



}
