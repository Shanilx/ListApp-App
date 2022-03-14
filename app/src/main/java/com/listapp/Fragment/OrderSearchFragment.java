package com.listapp.Fragment;

/**
 * Created by Nivesh on 6/23/2017.
 */

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.listapp.API_Utility.AsyncTask_Utility.CheckNetwork;
import com.listapp.API_Utility.AsyncTask_Utility.MultipartUtility;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.ProductDetails;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Search_Product.SearchProductResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SupplierDetails.SupplierProfile;
import com.listapp.Activity.CitySearchActivity;
import com.listapp.Activity.MedicineSearchActivity;
import com.listapp.Activity.OrderRegisterActivity;
import com.listapp.Activity.SuppliersNotFoundActivity;
import com.listapp.Adapter.AuthorisedDealersAdapter;
import com.listapp.Adapter.OrderListBaseAdapter;
import com.listapp.Adapter.OrderSupplierListAdapter;
import com.listapp.Adapter.SupplierListAdapter;
import com.listapp.Interface.FragmentBack;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.MarshMallowPermission;
import com.listapp.ListAppUtil.OKayEvent;
import com.listapp.ListAppUtil.Order;
import com.listapp.ListAppUtil.OrderDB;
import com.listapp.ListAppUtil.OrderDao;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.ListAppUtil.Product;
import com.listapp.R;
import com.listapp.xlistview.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class OrderSearchFragment extends Fragment implements View.OnClickListener, FragmentBack, TextView.OnEditorActionListener, XListView.IXListViewListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    public static int afterSubmitNotForm = 0;
    private static final int GALLERY_REQUEST_CODE = 10;
    private static final int FILE_REQUEST_CODE = 11;
    private static final int CAMERA_REQUEST_CODE = 12;
    private static FrameLayout productDetailView;
    private static ScrollView parentNoItemsView;
    public static boolean serchResult = false;
    public String qty;
    public String sch;
    public boolean newproduct;
    public String supplierName;
    public boolean editing;
    public boolean newproducttapped;
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
    private List<SearchProductResponse.Datum> productList;
    private String text;
    private int offset = 1;
    private LinearLayout upload2;
    List<Order>orderlist;

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
    public String productID = null;
    private List<ProductDetails.Data.Supplier> supplierList = null;
    private ProductDetails.Data.Product product = null;
    private String type;
    private boolean noItemBoolean = false;
    private boolean productDetailBoolean = false;
    private boolean supplierDetailBoolean = false;
    private Context context;

    private FrameLayout suppliersProfile;
    private TextView supplierProfileName;
  //  private TextView supplierProfilecityName;
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
    private boolean isRestart;
    private OrderSearchFragment osf;
    public String productName;
    public String publicsupplierName;
    public String companyName;
    private EditText suppliersearch;
     private ArrayList<String> autolist;
    private List<Order> ordersupplierlist;
    ArrayAdapter<String> adapter;
    private List<ProductDetails.Data.Supplier> realsupplierList;
    private List<Order> realordersupplierList;
    public boolean addingProduct;
    private EditText searchsuppiers;
    private TextView t19;
  //  private TextView t18;
    private ImageView info;
    private List<Order> realorderlist;

    public  void showProductDetails() {



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__order_search, null);

        afterSubmitNotForm = 0;
        addingProduct=false;
        osf=this;
        newproduct=false;
        newproducttapped=false;

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                OrderDB.class, "Order").allowMainThreadQueries().build();
        OrderDao od= db.orderDao();
        orderlist= od.getNewproducts();
        realordersupplierList=new ArrayList<>();
        realorderlist=new ArrayList<>();
        realsupplierList=new ArrayList<>();
        ordersupplierlist=od.getSupplierlist();

            for (int i = 0; i < ordersupplierlist.size(); i++)
                if (!ordersupplierlist.get(i).getSupplier().isEmpty() && ordersupplierlist.get(i).getSupplier().indexOf("Select") < 0) {
                    realordersupplierList.add(ordersupplierlist.get(i));
                Log.d("realorderlist items : ",ordersupplierlist.get(i).getSupplier());
                }
        autolist= (ArrayList<String>) od.getProductlist();
        adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, autolist);
        if(context.getClass().getSimpleName().equals("MedicineSearchActivity")){
            MedicineSearchActivity.search.setDropDownBackgroundResource(R.color.white);
            MedicineSearchActivity.search.setAdapter(adapter);
        }else{ 
            OrderRegisterActivity.search.setDropDownBackgroundResource(R.color.white);
            OrderRegisterActivity.search.setAdapter(adapter);
        }
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
        searchResultList.setPullLoadEnable(true);
        LinearLayout v1 = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.medicine_list_footer, null);
        Button b = v1.findViewById(R.id.callNoItem);
        b.setOnClickListener(this);
        searchResultList.addFooterView(v1);

        parentItemListView.setVisibility(View.GONE);
        productDetailView.setVisibility(View.GONE);
        parentNoItemsView.setVisibility(View.GONE);

        hideKeyboardView1 = view.findViewById(R.id.hideKeyboardView1);
        hideKeyboardView1.setOnTouchListener(this);

        showKeyboard(context);
        searchResultList.setOnItemClickListener(this);
        if(context.getClass().getSimpleName().equals("MedicineSearchActivity")) {
            MedicineSearchActivity.searchView.setVisibility(View.VISIBLE);
            MedicineSearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
            MedicineSearchActivity.backButton.setVisibility(View.VISIBLE);
            MedicineSearchActivity.heading.setVisibility(View.GONE);
            MedicineSearchActivity.searchiconView.setOnClickListener(this);
            MedicineSearchActivity.search.setOnEditorActionListener(this);
            if (MedicineSearchActivity.search.getText().length() > 0) {
                didSearch();
            }

            MedicineSearchActivity.backButton.setOnClickListener(this);
            MedicineSearchActivity.toolbarCloseIconView.setOnClickListener(this);
        }else {
            OrderRegisterActivity.searchView.setVisibility(View.VISIBLE);
            OrderRegisterActivity.toolbarCloseIconView.setVisibility(View.GONE);
            OrderRegisterActivity.backButton.setVisibility(View.VISIBLE);
            OrderRegisterActivity.heading.setVisibility(View.GONE);
            OrderRegisterActivity.searchiconView.setOnClickListener(this);
            OrderRegisterActivity.search.setOnEditorActionListener(this);
            if (OrderRegisterActivity.search.getText().length() > 0) {
                didSearch();
            }

            OrderRegisterActivity.backButton.setOnClickListener(this);
            OrderRegisterActivity.toolbarCloseIconView.setOnClickListener(this);
        }
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
        cityName.setOnClickListener(this);
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
        if(context.getClass().getSimpleName().equals("MedicineSearchActivity")) 
            MedicineSearchActivity.backButton.setOnClickListener(this);
        else
            OrderRegisterActivity.backButton.setOnClickListener(this);
        noSuppliers = view.findViewById(R.id.noSuppliers);
        noSuppliers.setVisibility(View.GONE);

        view.findViewById(R.id.callNoItem).setOnClickListener(this);

        shopName.setText("I am ");
        shopName.append(AppUtil.getHalfBoldString(PreferenceConnector.readString(context, PreferenceConnector.SHOP_NAME, "")));
        address.setText(AppUtil.getHalfBoldString(PreferenceConnector.readString(context, PreferenceConnector.ADDRESS, "")));
        mobileNumber.setText("RMN ");
        mobileNumber.append(AppUtil.getHalfBoldString(PreferenceConnector.readString(context, PreferenceConnector.MOBILE_NUMBER, "")));

        suppliersListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                getProductDetails(String.valueOf(supplierListOffset));
                supplierListOffset += 1;
            }
        });
        suppliersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isRunning)
                    countDownTimer.cancel();

                if (CheckNetwork.isNetwordAvailable(context)) {
                    position -= 1;
                    if (context.getClass().getSimpleName().equals("MedicineSearchActivity") && !addingProduct) {
                           if(!supplierList.isEmpty()) {
                               initSupplierProfileDetail(supplierList.get(position).getSupplierId());
                           }
                    } else {
                        try {
                            if (position == 0)
                                AppUtil.addnewsupplier(context, productName, companyName, type, osf);
                            else {
                                supplierName = supplierList.get(position - 1).getSupplierName();
                                hideProductDetail();
                                String supplierId = supplierList.get(position - 1).getSupplierId();
                                final String[] contact = new String[1];
                                supplierProfileCall = apiInterface.getSupplierProfile(mobileNumber1, userId, deviceType, deviceToken, supplierId);
                                supplierProfileCall.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                       SupplierProfile detail= (SupplierProfile) response.body();

                                       contact[0] =detail.getData().getSupplier().get(0).getContactNumber();
                                        if(newproduct){
                                            publicsupplierName=supplierName;
                                            AppUtil.addnewproduct(context,osf,productName);
                                        }
                                        else AppUtil.addproduct(context, productName, supplierName, companyName, "", type, osf, "false", contact[0]);
                                    }

                                    @Override
                                    public void onFailure(Call call, Throwable t) {
                                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                                    }
                                });


                            }
                        } catch (Exception e) {
                            AppUtil.showAlert(e.getMessage(), context);
                        }

                    }
                }}
        });


        searchResultList.setOnTouchListener(this);

        suppliersProfile = view.findViewById(R.id.suppliersProfile);
        suppliersProfile.setVisibility(View.GONE);
        supplierProfileName = view.findViewById(R.id.supplierProfileName);
       // supplierProfilecityName = view.findViewById(R.id.supplierProfilecityName);
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
        callNotFoundPage.setOnClickListener(this);

        suppliersearch=view.findViewById(R.id.searchsupplier);
        suppliersearch.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                final List<ProductDetails.Data.Supplier>[] tmplist = new List[]{new ArrayList<>()};
                final List<Order> ordertmplist = new ArrayList<>();

                Call listsupplier = apiInterface.listSuppliers(mobileNumber1, s.toString(),"0",cityID);
                listsupplier.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        showSuppliersList();
                        List<ProductDetails.Data.Supplier> list= (List<ProductDetails.Data.Supplier>) response.body();
                        supplierList=list;
                        tmplist[0].addAll(list);
                        for(int i=0;i<ordersupplierlist.size();i++){
                            if(ordersupplierlist.get(i).getSupplier().toLowerCase().indexOf(s.toString().toLowerCase().trim())>=0
                            &&ordersupplierlist.get(i).getSupplier().toLowerCase().indexOf("select")<0 ){

                            }
                        }

                        realsupplierList= tmplist[0];
                     //     realordersupplierList.clear();
                       // realordersupplierList.addAll(tmplist[0]);
                        OrderSupplierListAdapter orderSupplierListAdapter=new OrderSupplierListAdapter(context, supplierList,osf);
                        suppliersListView.setAdapter(orderSupplierListAdapter);

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
        List<Order> orders= od.getOrderlist();
        for (int i=0;i<orders.size();i++){
            Order order=orders.get(i);
            Call col=apiInterface.createOrderList(order.getProduct(), order.getSupplier(),order.getQuantity(),
                    order.getScheme(), order.getUser(), order.getSupplierid(), order.getIsordered());
        col.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        }

        searchsuppiers=view.findViewById(R.id.searchsupplier);
//        t18=view.findViewById(R.id.textView18);
        t19=view.findViewById(R.id.textView19);
        t19.setVisibility(View.GONE);
        publicsupplierName="";
        info=view.findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t19.getVisibility()==View.VISIBLE){
                    t19.setVisibility(View.GONE);
                }else
                    t19.setVisibility(View.VISIBLE);

            }
        });
        if(editing) {
            searchsuppiers.setVisibility(View.VISIBLE);
         //   t18.setVisibility(View.VISIBLE);
            info.setVisibility(View.VISIBLE);
            addingProduct=true;
            searchsuppiers.requestFocus();
            showInAppProductDetail();
        }else{
            editing=false;
            if(context.getClass().getSimpleName().equals("MedicineSearchActivity"))
                MedicineSearchActivity.searchView.requestFocus();
                else
                OrderRegisterActivity.searchView.requestFocus();

        }

    }

    private void initcacheSupplierProfileDetail(Order order) {

        hideKeyboardView1.scrollTo(0, 0);
        supplierProfilecontctNumber.setText(order.getSupplierid());
        supplierProfileName.setText(order.getSupplier());
        productDetailBoolean = false;
        supplierDetailBoolean = true;
        suppliersProfile.setVisibility(View.VISIBLE);
        supplierProfileName.setText(order.getSupplier());


    }



    public void hideProductDetail() {
        serchResult = true;
        productDetailBoolean = false;
        resetDetailView();
        parentItemListView.setVisibility(View.VISIBLE);
        searchResultList.setVisibility(View.VISIBLE);
        productDetailView.setVisibility(View.GONE);
        parentNoItemsView.setVisibility(View.GONE);
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
                        if(context.getClass().getSimpleName().equals("MedicineSearchActivity")){
                            MedicineSearchActivity.searchiconView.setVisibility(View.GONE);
                            MedicineSearchActivity.toolbarCloseIconView.setVisibility(View.VISIBLE);}
                        else{
                            OrderRegisterActivity.searchiconView.setVisibility(View.GONE);
                            OrderRegisterActivity.toolbarCloseIconView.setVisibility(View.VISIBLE);
                        }
                    } else {
                    if(context.getClass().getSimpleName().equals("MedicineSearchActivity")) {
                        MedicineSearchActivity.searchiconView.setVisibility(View.VISIBLE);
                        MedicineSearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
                    }else{
                        OrderRegisterActivity.searchiconView.setVisibility(View.VISIBLE);
                        OrderRegisterActivity.toolbarCloseIconView.setVisibility(View.GONE);
                    }
                       }

                    if (text.length() > 0) {
                        searchProgress.setVisibility(View.VISIBLE);
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
        if (isRestart) {
            if(context.getClass().getSimpleName().equals("MedicineSearchActivity"))
                startActivity(new Intent(context, MedicineSearchActivity.class));
                else
                startActivity(new Intent(context, OrderRegisterActivity.class));
            getActivity().finish();
            
            isRestart = false;
        }
        if(context.getClass().getSimpleName().equals("MedicineSearchActivity"))
            MedicineSearchActivity.search.addTextChangedListener(textWatcher);
        else
        OrderRegisterActivity.search.addTextChangedListener(textWatcher);
        if (afterSubmitNotForm == 1) {
            AppUtil.hideKeyBoard(context);
         //   MedicineHomeFragment.hideFragmentView();
            getFragmentManager().popBackStack();
            parentItemListView.setVisibility(View.VISIBLE);
            parentNoItemsView.setVisibility(View.VISIBLE);
            suppliersProfile.setVisibility(View.VISIBLE);
            if(context.getClass().getSimpleName().equals("MedicineSearchActivity"))
                MedicineSearchActivity.search.removeTextChangedListener(textWatcher);
            else
                OrderRegisterActivity.search.removeTextChangedListener(textWatcher);
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
            case R.id.upload:
                uploadEvent();
                break;

            case R.id.callNoItem:
                callNotFindProduct();
                break;

            case R.id.upload1:
                uploadEvent();
                break;

            case R.id.share:

                StringBuilder shareText = new StringBuilder("Supplier details for " + medicineName.getText().toString() + "\nSupplier Name: " + supplierProfileName.getText().toString().trim());
                if (supplierContactPersonsList != null) {
                    int length = supplierContactPersonsList.size();
                    if (length != 0) {
                        for (int i = 0; i < length; i++) {
                            shareText.append("\n" + supplierContactPersonsList.get(i).getContactName()
                                    + ": " + supplierContactPersonsList.get(i).getContactNumber());
                        }
                    }
                }
                AppUtil.shareOnWhatsApp(shareText.toString(), context);
                break;

            case R.id.upload2:
                uploadEvent();
                break;

            case R.id.submit:
                Log.d("TAG", "The interstitial wasn't loaded yet.");
                AppUtil.hideKeyBoard(context);
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

            case R.id.callNotFoundPage:

                    Log.d("TAG", "The interstitial wasn't loaded yet.");

                Intent intent = new Intent(context, SuppliersNotFoundActivity.class);
                intent.putExtra("text", medicineName.getText().toString().trim());
                intent.putExtra("keyWord", "Medicine");
                startActivity(intent);
                break;

            case R.id.toolbarCloseIconView:
                if(context.getClass().getSimpleName().equals("MedicineSearchActivity"))
                { MedicineSearchActivity.search.setText("");
                MedicineSearchActivity.searchiconView.setVisibility(View.VISIBLE);
                }else{
                    OrderRegisterActivity.search.setText("");
                    OrderRegisterActivity.searchiconView.setVisibility(View.VISIBLE);
                }
                text = "";
                break;

            case R.id.medicineName:
                if (!detailViewStatus)
                    showDetailView();
                else
                    hideDetailView();
                break;

            case R.id.supplierProfileexpandCollapseView:
                if (dealersListHide) {
                    dealersListHide = false;
                    supplierProfileexpandCollapseView.setImageResource(R.drawable.expand);
                    noSuppliers.setVisibility(View.GONE);
                } else {
                    supplierProfileexpandCollapseView.setImageResource(R.drawable.collapse);
                    dealersListHide = true;
                }
                break;

            case R.id.toolbarBackButton:
                onBackPressed();
                break;

            case R.id.toolbarSerachIconView:
                didSearch();
                break;

            case R.id.expandCollapseViewLayout:
                if (!detailViewStatus)
                    showDetailView();
                else
                    hideDetailView();
                break;

            case R.id.expandCollapseViewSupplierList:

                if (!listViewStatus)
                    showSuppliersList();
                else
                    hideSuppliersList();
                break;

            case R.id.cityName:
                AppUtil.hideKeyBoard(context);
                isRestart=true;
                startActivity(new Intent(context, CitySearchActivity.class));
                break;
        }
    }

    private void didSearch() {

        AppUtil.hideKeyBoard(context);

        OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                OrderDB.class, "Order").allowMainThreadQueries().build();
        OrderDao od = db.orderDao();
        autolist.addAll(od.getProductlist());
        adapter.notifyDataSetChanged();
        if (context.getClass().getSimpleName().equals("MedicineSearchActivity")) {
            text = MedicineSearchActivity.search.getText().toString();
            MedicineSearchActivity.search.clearFocus();
        } else {
            text = OrderRegisterActivity.search.getText().toString();
            OrderRegisterActivity.search.clearFocus();
        }
        if (text != null) {
            if (text.trim().length() > 2) {
                searchProgress.setVisibility(View.VISIBLE);
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
                AppUtil.showAlert("Please Search with at least 3 characters", context);
                countDownTimer.cancel();
                if (searchProductCall != null)
                    if (!searchProductCall.isExecuted())
                        searchProductCall.cancel();
            }
        } else {
            searchProgress.setVisibility(View.GONE);
            AppUtil.showAlert("Please Search with at least 3 characters", context);
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
            hideSupplierProfileDetail();
        } else if (productDetailBoolean) {
            serchResult = true;
            if(context.getClass().getSimpleName().equals("MedicineSearchActivity"));
            else
                OrderRegisterActivity.backButton.setVisibility(View.GONE);
            productDetailBoolean = false;
            resetDetailView();
            parentItemListView.setVisibility(View.VISIBLE);
            searchResultList.setVisibility(View.VISIBLE);
            productDetailView.setVisibility(View.GONE);
            parentNoItemsView.setVisibility(View.GONE);
        } else if (noItemBoolean) {

            AppUtil.hideKeyBoard(context);
            Intent intent;
            if(context.getClass().getSimpleName().equals("MedicineSearchActivity"))
                intent = new Intent(getContext(), MedicineSearchActivity.class);
            else
                intent = new Intent(getContext(), OrderRegisterActivity.class);
            startActivity(intent);
            parentItemListView.setVisibility(View.GONE);
            parentNoItemsView.setVisibility(View.GONE);
            suppliersProfile.setVisibility(View.GONE);
        } else if (serchResult) {

            AppUtil.hideKeyBoard(context);
           // MedicineHomeFragment.hideFragmentView();
            Intent intent;
            if(context.getClass().getSimpleName().equals("MedicineSearchActivity"))
                intent = new Intent(getContext(), MedicineSearchActivity.class);
            else
                intent = new Intent(getContext(), OrderRegisterActivity.class);
            startActivity(intent);
            parentItemListView.setVisibility(View.GONE);
            parentNoItemsView.setVisibility(View.GONE);
            suppliersProfile.setVisibility(View.GONE);
        } else {
            AppUtil.hideKeyBoard(context);
         //   MedicineHomeFragment.hideFragmentView();
            Intent intent;
            if(context.getClass().getSimpleName().equals("MedicineSearchActivity"))
                intent = new Intent(getContext(), MedicineSearchActivity.class);
            else
                intent = new Intent(getContext(), OrderRegisterActivity.class);
            startActivity(intent);
//            getFragmentManager().popBackStack();
//            parentItemListView.setVisibility(View.GONE);
//            parentNoItemsView.setVisibility(View.GONE);
//            suppliersProfile.setVisibility(View.GONE);
//            OrderRegisterActivity.search.removeTextChangedListener(textWatcher);
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

            searchProductCall = apiInterface.searchProduct(mobileNumber1, userId, deviceType, deviceToken, text, offset1, "");
            searchProductCall.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    if (offset1.equals("0")) {
                        searchProgress.setVisibility(View.GONE);
                     } else
                        onLoad();

                    if (response != null) {

                        SearchProductResponse searchProductResponse = (SearchProductResponse) response.body();
                        if (searchProductResponse != null) {

                            String error = searchProductResponse.getError();
                            String msg = searchProductResponse.getMessage();

                            if (error.equals("0")) {
                                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                                Bundle bundle = new Bundle();
                                String firetxt=PreferenceConnector.readString(context,PreferenceConnector.MOBILE_NUMBER,"")+" : "+text;
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,firetxt);
                                // Toast.makeText(context,firetxt,Toast.LENGTH_LONG).show();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"Medicine");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);
                                parentItemListView.setVisibility(View.VISIBLE);
                                parentNoItemsView.setVisibility(View.GONE);
                                productDetailView.setVisibility(View.GONE);
                                if (offset1.equals("0")) {
                                    productList = searchProductResponse.getData();
                                    if (productList != null) {
                                        int length = productList.size();
                                        if (length > 0) {
                                            searchResultList.setVisibility(View.VISIBLE);
                                            if (length > 25) {
                                                searchResultList.setPullLoadEnable(true);
                                            } else
                                                searchResultList.setPullLoadEnable(false);

                                            searchResultCount.setText(length + " Results for " + text);
                                            try {
                                                OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                                                        OrderDB.class, "Order").allowMainThreadQueries().build();
                                                OrderDao od= db.orderDao();
                                                orderlist= od.getNewproducts();
                                                List<Order> tmporderlist = new ArrayList<>();
                                                for(int i=0; i<orderlist.size();i++){
                                                    if(orderlist.get(i).getProduct().toLowerCase().indexOf(text.toLowerCase())>=0) {
                                                        tmporderlist.add(orderlist.get(i));
                                                    }
                                                }
                                                realorderlist.clear();
                                                realorderlist.addAll(tmporderlist);
                                                OrderListBaseAdapter productListAdapter = new OrderListBaseAdapter(context,realorderlist,productList,osf);
                                                tmporderlist.clear();
                                                searchResultList.setAdapter(productListAdapter);
                                                for (int i=0;i<productList.size();i++) {
                                                    Product p = new Product();
                                                    p.setAddDate(productList.get(i).getAddDate());
                                                    p.setCompanyName(productList.get(i).getCompanyName());
                                                    p.setDrugName(productList.get(i).getDrugName());
                                                    p.setForm(productList.get(i).getForm());
                                                    p.setMrp(productList.get(i).getMrp());
                                                    p.setPackingType(productList.get(i).getPackingType());
                                                    p.setPackSize(productList.get(i).getPackSize());
                                                    p.setProductId(productList.get(i).getProductId());
                                                    p.setProductName(productList.get(i).getProductName());
                                                    p.setCompanyName(productList.get(i).getCompanyName());
                                                    p.setRate(productList.get(i).getRate());
                                                    p.setSchedule(productList.get(i).getSchedule());
                                                    od.addProduct(p);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            serchResult = true;
                                        } else {
                                            callNotFindProduct();
                                        }
                                    } else
                                        callNotFindProduct();
                                } else {
                                    int length = productList.size();
                                    productList.addAll(searchProductResponse.getData());
                                    searchResultCount.setText(length + " Results for " + text);

                                    try {
                                        List<Order> tmporderlist = new ArrayList<>();
                                        for(int i=0;i<orderlist.size();i++){
                                            if(orderlist.get(i).getProduct().toLowerCase().indexOf(text.toLowerCase())>=0)
                                                tmporderlist.add(orderlist.get(i));
                                        }
                                        realorderlist.clear();
                                        realorderlist.addAll(tmporderlist);
                                        tmporderlist.clear();
                                        OrderListBaseAdapter productListAdapter = new OrderListBaseAdapter(context, realorderlist, productList,osf);
                                        searchResultList.setAdapter(productListAdapter);
                                       searchResultList.setVisibility(View.VISIBLE);

                                        //  searchResultList.setSelection(length - 1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                if (msg.equalsIgnoreCase("Your session has been expired")) {
                                    AppUtil.sessionLogout(msg, getContext());
                                } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                    AppUtil.sessionLogout(msg, getContext());
                                else if (!offset1.equals("0")) {
                                    AppUtil.showAlert("No More Match Found", context);
                                } else {
                                    callNotFindProduct();
                                }
                            }
                        } else
                            AppUtil.showAlert(context.getString(R.string.serverError), context);
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
        } else {
            try {
                onLoad();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AppUtil.showAlert(context.getString(R.string.networkError), context);
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

      //  suppliersearch.setText("");

        addingProduct= !context.getClass().getSimpleName().equals("MedicineSearchActivity");

        if (isRunning) {
            if (searchProductCall != null)
                if (!searchProductCall.isExecuted())
                    searchProductCall.cancel();
            countDownTimer.cancel();
        }

        if (CheckNetwork.isNetwordAvailable(context)) {
            position -= 1;

            if(position ==0){

                AppUtil.addnewproduct(context,osf,text);
            }
            else if(position <= realorderlist.size()){
                serchResult = true;
                productDetailBoolean = true;
                parentItemListView.setVisibility(View.GONE);
                parentNoItemsView.setVisibility(View.GONE);
                productDetailView.setVisibility(View.VISIBLE);
                suppliersProfile.setVisibility(View.GONE);
                productName=realorderlist.get(position-1).getProduct();
                companyName=realorderlist.get(position-1).getCompany();
                // AppUtil.addproduct(context,productName,"",companyName, "", this);
                showInAppProductDetail();
            }
            else if (!productList.isEmpty()) {
                productID = productList.get(position-realorderlist.size()-1).getProductId();
                type = productList.get(position-realorderlist.size()-1).getForm();
                if (productID != null && !productID.equals("")) {
                    serchResult = true;
                    productDetailBoolean = true;
                    parentItemListView.setVisibility(View.GONE);
                    parentNoItemsView.setVisibility(View.GONE);
                    productDetailView.setVisibility(View.VISIBLE);
                    suppliersProfile.setVisibility(View.GONE);
                    productName=productList.get(position-realorderlist.size()-1).getProductName();
                    companyName=productList.get(position-realorderlist.size()-1).getCompanyName();
                   // AppUtil.addproduct(context,productName,"",companyName, "", this);
                    showProductDetail();
                } else
                    AppUtil.showAlert("Product ID Not Available for Search Product Detail", context);
            }
        } else
            AppUtil.showAlert(context.getString(R.string.networkError), context);
    }

    public void showInAppProductDetail() {
        serchResult = true;
        productDetailBoolean = true;
        newproduct=false;
        newproducttapped=false;
        parentItemListView.setVisibility(View.GONE);
        parentNoItemsView.setVisibility(View.GONE);
        productDetailView.setVisibility(View.VISIBLE);
        suppliersProfile.setVisibility(View.GONE);
            medicineName.setText(productName);
            manufacturedBy.setText(companyName);
            cityName.setText(PreferenceConnector.readString(context,PreferenceConnector.CITY,"").toUpperCase());
        if(addingProduct){
            searchsuppiers.setVisibility(View.VISIBLE);
        //    t18.setVisibility(View.VISIBLE);
            info.setVisibility(View.VISIBLE);
        }else{
            searchsuppiers.setVisibility(View.GONE);
      //      t18.setVisibility(View.GONE);
            info.setVisibility(View.GONE);
        }

        parentDetailView.setVisibility(View.GONE);
        if (CheckNetwork.isNetwordAvailable(context)) {
            try {
                getInAppProductDetails();
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AppUtil.showExitAlert(context.getString(R.string.networkError), context);
            progressDialog.dismiss();
        }

    }

    private void getInAppProductDetails() {
        productDetailBoolean = true;
        serchResult = false;

        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        else {
            progressDialog = AppUtil.createProgressDialog(context);
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
        }

        parentDetailView.setVisibility(View.GONE);
        noSupplier.setVisibility(View.GONE);
        suppliersListView.setVisibility(View.GONE);
        cityID = PreferenceConnector.readString(context, PreferenceConnector.CITY_ID, "");
        List<ProductDetails.Data.Supplier> sl=new ArrayList<ProductDetails.Data.Supplier>();
        OrderSupplierListAdapter supplierListAdapter = new OrderSupplierListAdapter(context, sl,osf);
        suppliersListView.setAdapter(supplierListAdapter);
        suppliersListView.setVisibility(View.VISIBLE);



    }

    public void showProductDetail() {

        OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                OrderDB.class, "Order").allowMainThreadQueries().build();
        OrderDao od = db.orderDao();
        Order o= od.getOrderbyproduct(productName);
//       try {
//           if (o.getNewproduct()=="true")
//           { showInAppProductDetail();
//           return;
//           }
//       }catch (Exception e){
//
//       }
        medicineName.setText(productName);
        manufacturedBy.setText(companyName);
        serchResult = true;
        productDetailBoolean = true;
    //    parentItemListView.setVisibility(View.GONE);
        parentNoItemsView.setVisibility(View.GONE);
        productDetailView.setVisibility(View.VISIBLE);
        suppliersProfile.setVisibility(View.GONE);
        if(addingProduct){
            searchsuppiers.setVisibility(View.VISIBLE);
       //     t18.setVisibility(View.VISIBLE);
            info.setVisibility(View.VISIBLE);
        }else{
            searchsuppiers.setVisibility(View.GONE);
  //          t18.setVisibility(View.GONE);
            info.setVisibility(View.GONE);
        }
        parentDetailView.setVisibility(View.GONE);
        if (CheckNetwork.isNetwordAvailable(context)) {
            try {
                getProductDetails("0");
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            AppUtil.showExitAlert(context.getString(R.string.networkError), context);
            progressDialog.dismiss();
        }
    }

    private void resetDetailView() {

        cityName.setText("");
        price.setText("");
        medicineName.setText("");
        manufacturedBy.setText("");
        composition.setText("");
        usedForTreatment.setText("");
        packingDetail.setText("");
        typeOfProduct.setText("");
        expandCollapseView.setImageResource(R.drawable.expand);
        expandCollapseViewSupplierList.setImageResource(R.drawable.collapse);
    }

    private void onSupplierListLoad() {
        suppliersListView.stopRefresh();
        suppliersListView.stopLoadMore();
        suppliersListView.setRefreshTime("Load More Products");
    }

    public void getProductDetails(final String s) {
        if (CheckNetwork.isNetwordAvailable(context)) {
            productDetailBoolean = true;
            serchResult = false;



            parentDetailView.setVisibility(View.GONE);
            noSupplier.setVisibility(View.GONE);
            suppliersListView.setVisibility(View.GONE);
            cityID = PreferenceConnector.readString(context, PreferenceConnector.CITY_ID, "");
            productDetailCall = apiInterface.getProductDetail(mobileNumber1, userId, deviceType, deviceToken, productID, s, cityID);
            productDetailCall.enqueue(new Callback<ProductDetails>() {
                @Override
                public void onResponse(Call<ProductDetails> call, Response<ProductDetails> response) {
                    onSupplierListLoad();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    if (response != null) {
                        ProductDetails productDetails = response.body();
                        if (productDetails != null) {

                            String error = productDetails.getError();
                            String msg = productDetails.getMessage();
                            if (error.equals("0")) {
                                if (productDetails.getData() != null) {
                                    if (s.equals("0")) {
                                        if(supplierList!=null) {
                                            supplierList.clear();
                                            supplierList.addAll(productDetails.getData().getSuppliers());
                                        }else supplierList=productDetails.getData().getSuppliers();
                                        product = productDetails.getData().getProduct();
                                        productName=product.getProductName();
                                        companyName=product.getMenufectureBy();
                                        if (!supplierList.isEmpty()) {
                                            int length = supplierList.size();
                                            if (length >= 0) {
                                                if (length > 25) {
                                                    suppliersListView.setPullLoadEnable(true);
                                                } else
                                                    suppliersListView.setPullLoadEnable(false);

                                                expandCollapseViewSupplierList.setVisibility(View.VISIBLE);
                                                if(!realsupplierList.isEmpty()){
                                                realsupplierList.clear();
                                                realordersupplierList.clear();
                                                realsupplierList.addAll(supplierList);}
                                                for (int i = 0; i < ordersupplierlist.size(); i++)
                                                    if (!ordersupplierlist.get(i).getSupplier().isEmpty() && ordersupplierlist.get(i).getSupplier().indexOf("Select") < 0) {
                                                        realordersupplierList.add(ordersupplierlist.get(i));
                                                    }
                                                SupplierListAdapter supplierListAdapter;
                                                if(context.getClass().getSimpleName().equals("MedicineSearchActivity") && !addingProduct) {
                                                    supplierListAdapter = new SupplierListAdapter(context,supplierList);
                                                    suppliersListView.setAdapter(supplierListAdapter);
                                                    suppliersListView.stopLoadMore();
                                                    suppliersListView.setVisibility(View.VISIBLE);
                                                }
                                                else {

                                                   OrderSupplierListAdapter supplierListAdapter2 = new OrderSupplierListAdapter(context, supplierList, osf);
                                                    suppliersListView.setAdapter(supplierListAdapter2);
                                                }

                                                productDetailBoolean = true;
                                                suppliersListView.setVisibility(View.VISIBLE);
                                            } else {
                                                expandCollapseViewSupplierList.setVisibility(View.GONE);
                                                suppliersListView.setVisibility(View.GONE);
                                                noSupplier.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            expandCollapseViewSupplierList.setVisibility(View.GONE);
                                            noSupplier.setVisibility(View.VISIBLE);
                                            suppliersListView.setVisibility(View.GONE);
                                        }
                                        setProductDetails();
                                    } else {
                                        expandCollapseViewSupplierList.setVisibility(View.VISIBLE);
                                        supplierList.addAll(productDetails.getData().getSuppliers());
                                        OrderSupplierListAdapter supplierListAdapter = new OrderSupplierListAdapter(context, supplierList,osf);
                                        suppliersListView.setAdapter(supplierListAdapter);
                                        realordersupplierList.clear();
                                        for (int i = 0; i < ordersupplierlist.size(); i++)
                                            if (!ordersupplierlist.get(i).getSupplier().isEmpty() && ordersupplierlist.get(i).getSupplier().indexOf("Select") < 0) {
                                                realordersupplierList.add(ordersupplierlist.get(i));
                                                Log.d("realorderlist items : ",ordersupplierlist.get(i).getSupplier());
                                            }
                                        realsupplierList=supplierList;
                                        suppliersListView.setVisibility(View.VISIBLE);
                                    }
                                } else
                                    AppUtil.okayEventDialog("This Medicine/Product has been deactivated by administrator", context, new OKayEvent() {
                                        @Override
                                        public void okayEvent(boolean b) {
                                            parentItemListView.setVisibility(View.VISIBLE);
                                            productDetailView.setVisibility(View.GONE);
                                            parentNoItemsView.setVisibility(View.GONE);
                                            productDetailBoolean = false;
                                            serchResult = true;
                                        }
                                    });
                            } else {
                                if (msg.equalsIgnoreCase("Your session has been expired")) {
                                    AppUtil.sessionLogout(msg, getContext());
                                } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                    AppUtil.sessionLogout(msg, getContext());
                                else if (s.equals("0")) {
                                    expandCollapseViewSupplierList.setVisibility(View.VISIBLE);
                                    OrderDB db = Room.databaseBuilder(context.getApplicationContext(),
                                            OrderDB.class, "Order").allowMainThreadQueries().build();
                                    OrderDao od= db.orderDao();
                                    ordersupplierlist=od.getSupplierlist();
                                    supplierList= new List<ProductDetails.Data.Supplier>() {
                                        @Override
                                        public int size() {
                                            return 0;
                                        }

                                        @Override
                                        public boolean isEmpty() {
                                            return false;
                                        }

                                        @Override
                                        public boolean contains(@Nullable Object o) {
                                            return false;
                                        }

                                        @NonNull
                                        @Override
                                        public Iterator<ProductDetails.Data.Supplier> iterator() {
                                            return null;
                                        }

                                        @NonNull
                                        @Override
                                        public Object[] toArray() {
                                            return new Object[0];
                                        }

                                        @NonNull
                                        @Override
                                        public <T> T[] toArray(@NonNull T[] a) {
                                            return null;
                                        }

                                        @Override
                                        public boolean add(ProductDetails.Data.Supplier supplier) {
                                            return false;
                                        }

                                        @Override
                                        public boolean remove(@Nullable Object o) {
                                            return false;
                                        }

                                        @Override
                                        public boolean containsAll(@NonNull Collection<?> c) {
                                            return false;
                                        }

                                        @Override
                                        public boolean addAll(@NonNull Collection<? extends ProductDetails.Data.Supplier> c) {
                                            return false;
                                        }

                                        @Override
                                        public boolean addAll(int index, @NonNull Collection<? extends ProductDetails.Data.Supplier> c) {
                                            return false;
                                        }

                                        @Override
                                        public boolean removeAll(@NonNull Collection<?> c) {
                                            return false;
                                        }

                                        @Override
                                        public boolean retainAll(@NonNull Collection<?> c) {
                                            return false;
                                        }

                                        @Override
                                        public void clear() {

                                        }

                                        @Override
                                        public boolean equals(@Nullable Object o) {
                                            return false;
                                        }

                                        @Override
                                        public int hashCode() {
                                            return 0;
                                        }

                                        @Override
                                        public ProductDetails.Data.Supplier get(int index) {
                                            return null;
                                        }

                                        @Override
                                        public ProductDetails.Data.Supplier set(int index, ProductDetails.Data.Supplier element) {
                                            return null;
                                        }

                                        @Override
                                        public void add(int index, ProductDetails.Data.Supplier element) {

                                        }

                                        @Override
                                        public ProductDetails.Data.Supplier remove(int index) {
                                            return null;
                                        }

                                        @Override
                                        public int indexOf(@Nullable Object o) {
                                            return 0;
                                        }

                                        @Override
                                        public int lastIndexOf(@Nullable Object o) {
                                            return 0;
                                        }

                                        @NonNull
                                        @Override
                                        public ListIterator<ProductDetails.Data.Supplier> listIterator() {
                                            return null;
                                        }

                                        @NonNull
                                        @Override
                                        public ListIterator<ProductDetails.Data.Supplier> listIterator(int index) {
                                            return null;
                                        }

                                        @NonNull
                                        @Override
                                        public List<ProductDetails.Data.Supplier> subList(int fromIndex, int toIndex) {
                                            return null;
                                        }
                                    };
                                    OrderSupplierListAdapter supplierListAdapter = new OrderSupplierListAdapter(context, supplierList,osf);
                                    suppliersListView.setAdapter(supplierListAdapter);
                                    realsupplierList=supplierList;
                                    realordersupplierList.clear();
                                    for (int i = 0; i < ordersupplierlist.size(); i++)
                                        if (!ordersupplierlist.get(i).getSupplier().isEmpty() && ordersupplierlist.get(i).getSupplier().indexOf("Select") < 0) {
                                            realordersupplierList.add(ordersupplierlist.get(i));
                                            Log.d("realorderlist items : ",ordersupplierlist.get(i).getSupplier());
                                        }
                                    productDetailBoolean = true;
                                    suppliersListView.setVisibility(View.VISIBLE);
//                                    productDetailBoolean = false;
//                                    parentItemListView.setVisibility(View.VISIBLE);
//                                    productDetailView.setVisibility(View.GONE);
//                                    parentNoItemsView.setVisibility(View.GONE);
                                    serchResult = true;
                                } else {
                                    AppUtil.showAlert(msg, context);
                                }
                            }
                        } else {
                            AppUtil.showAlert(context.getString(R.string.serverError), context);
                            parentItemListView.setVisibility(View.VISIBLE);
                            productDetailView.setVisibility(View.GONE);
                            parentNoItemsView.setVisibility(View.GONE);
                            productDetailBoolean = false;
                        }
                    } else {
                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                        parentItemListView.setVisibility(View.VISIBLE);
                        productDetailView.setVisibility(View.GONE);
                        parentNoItemsView.setVisibility(View.GONE);
                        productDetailBoolean = false;
                    }
                }

                @Override
                public void onFailure(Call<ProductDetails> call, Throwable t) {
                    onSupplierListLoad();
                    AppUtil.showAlert(context.getString(R.string.serverError), context);
                    parentItemListView.setVisibility(View.VISIBLE);
                    productDetailView.setVisibility(View.GONE);
                    parentNoItemsView.setVisibility(View.GONE);
                    productDetailBoolean = false;
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }
            });
        } else {
            AppUtil.showAlert(context.getString(R.string.networkError), context);
            onSupplierListLoad();
        }
    }

    private void setProductDetails() {

        if (product != null) {
            productDetailBoolean = true;
            if(product.getProductName().isEmpty())
                medicineName.setText(productName);
            else
            medicineName.setText(product.getProductName());
            if(product.getMenufectureBy().isEmpty())
                manufacturedBy.setText(companyName);
            else
            manufacturedBy.setText(product.getMenufectureBy());
            composition.setText(product.getComposition());
            usedForTreatment.setText(product.getUsedForTreatment());
            packingDetail.setText(product.getPackingDetail());
            if(product.getCityName().isEmpty())
                cityName.setText(PreferenceConnector.readString(context,PreferenceConnector.CITY,"").toUpperCase());
            else
            cityName.setText(product.getCityName().toUpperCase());
            price.setText(context.getString(R.string.rs) + " " + product.getPrice());
            typeOfProduct.setText("/" + type);
            if(type!=null) {
                if (type.equals("STRIP") || type.equals("TABLET")) {
                    medicineType.setImageResource(R.drawable.tablet);
                } else if (type.equals("BOTTLE") || type.equals("SYRUP") || type.equals("SUSPENSION")) {
                    medicineType.setImageResource(R.drawable.bottle);
                } else if (type.equals("DROPS")) {
                    medicineType.setImageResource(R.drawable.drop);
                } else if (type.equals("GEL")) {
                    medicineType.setImageResource(R.drawable.gel);
                } else if (type.equals("CREAM") || type.equals("TUBE") || type.equals("OINTMENT")) {
                    medicineType.setImageResource(R.drawable.tube);
                } else if (type.equals("LOTION")) {
                    medicineType.setImageResource(R.drawable.lotion);
                } else if (type.equals("CAPSULE")) {
                    medicineType.setImageResource(R.drawable.capsuls);
                } else if (type.equals("SOAP")) {
                    medicineType.setImageResource(R.drawable.soap);
                } else if (type.equals("INJECTION")) {
                    medicineType.setImageResource(R.drawable.injection);
                } else if (type.equals("SPRAY")) {
                    medicineType.setImageResource(R.drawable.spray);
                } else {
                    medicineType.setImageResource(R.drawable.unnown);
                }
            }
            if (product.getFavouriteStatus().equals("0"))
                medicineFavourite.setImageResource(R.drawable.favourite_icon);
            else
                medicineFavourite.setImageResource(R.drawable.black_unfavourite);
        } else
            AppUtil.okayEventDialog("This Medicine/Product has been deactivated by administrator", context, new OKayEvent() {
                @Override
                public void okayEvent(boolean b) {

                    serchResult = true;
                    parentItemListView.setVisibility(View.VISIBLE);
                    productDetailView.setVisibility(View.GONE);
                    parentNoItemsView.setVisibility(View.GONE);
                    productDetailBoolean = false;
                }
            });
    }

    private void hideDetailView() {
        expandCollapseView.setImageResource(R.drawable.expand);
        parentDetailView.setVisibility(View.GONE);
        detailViewStatus = false;
    }

    private void showDetailView() {

        expandCollapseView.setImageResource(R.drawable.collapse);
        parentDetailView.setVisibility(View.VISIBLE);

        detailViewStatus = true;
    }

    private void hideSuppliersList() {
        expandCollapseViewSupplierList.setImageResource(R.drawable.expand);
        listViewStatus = false;
        suppliersListView.setVisibility(View.GONE);
    }

    private void showSuppliersList() {
        expandCollapseViewSupplierList.setImageResource(R.drawable.collapse);
        listViewStatus = true;
        suppliersListView.setVisibility(View.VISIBLE);
    }

    private View getWhiteViewAfterDetails() {
        return getView().findViewById(R.id.whiteViewAfterDetails);
    }

    private void initSupplierProfileDetail(String supplierID) {
        if (CheckNetwork.isNetwordAvailable(context)) {
            if (progressDialog == null) {
                progressDialog = AppUtil.createProgressDialog(context);
                if (!progressDialog.isShowing())
                    progressDialog.show();
            } else {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            suppliersProfile.setVisibility(View.GONE);
            supplierDetailBoolean = true;
            productDetailView.setVisibility(View.GONE);
            parentItemListView.setVisibility(View.GONE);
            searchResultList.setVisibility(View.GONE);
            supplierProfileCall = apiInterface.getSupplierProfile(mobileNumber1, userId, deviceType, deviceToken, supplierID);
            supplierProfileCall.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    if (response != null) {
                        SupplierProfile supplierProfile = (SupplierProfile) response.body();
                        if (supplierProfile != null) {

                            String error = supplierProfile.getError();
                            String msg = supplierProfile.getMessage();
                            if (error.equals("0")) {
                                suppliersProfile.setVisibility(View.VISIBLE);
                                setSupplierProfileDetail(supplierProfile.getData());

                            } else {
                                if (msg.equalsIgnoreCase("Your session has been expired")) {
                                    AppUtil.sessionLogout(msg, getContext());
                                } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                    AppUtil.sessionLogout(msg, getContext());
                                else {
                                    hideSupplierProfileDetail();
                                    AppUtil.showAlert(msg, context);
                                }
                            }
                        } else {
                            hideSupplierProfileDetail();
                            AppUtil.showAlert(context.getString(R.string.serverError), context);
                        }
                    } else {
                        hideSupplierProfileDetail();
                        AppUtil.showAlert(context.getString(R.string.serverError), context);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    hideSupplierProfileDetail();
                    AppUtil.showAlert(context.getString(R.string.serverError), context);
                }
            });
        } else
            AppUtil.showAlert(context.getString(R.string.networkError), context);
    }

    private void setSupplierProfileDetail(SupplierProfile.Data data) {

        if (data.getSupplier() != null && data.getSupplier().size() != 0) {

            hideKeyboardView1.scrollTo(0, 0);

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

            productDetailBoolean = false;
            supplierDetailBoolean = true;
            SupplierProfile.Data.Supplier supplier = data.getSupplier().get(0);
            supplierProfilearea.setText(supplier.getArea());
         //   supplierProfilecityName.setText(supplier.getCity().toUpperCase());
         //   supplierProfilecityName.setOnClickListener(this);
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
                        viewContact1.setVisibility(View.VISIBLE);
                        viewContact2.setVisibility(View.GONE);
                        viewContact3.setVisibility(View.GONE);
                        viewContact4.setVisibility(View.GONE);
                        viewContact5.setVisibility(View.GONE);
                        supplierContactPersonsList.clear();
                    }
                } else {
                    supplierProfilecontactPerson1.setText("-");
                    viewContact1.setVisibility(View.VISIBLE);
                    viewContact2.setVisibility(View.GONE);
                    viewContact3.setVisibility(View.GONE);
                    viewContact4.setVisibility(View.GONE);
                    viewContact5.setVisibility(View.GONE);
                    supplierContactPersonsList.clear();
                }
            } else {
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
         //       supplierProfileauthorize.setImageResource(R.drawable.yellow_bg);
            if (supplier.getFavouriteStatus().equals("Yes"))
                supplierProfilefavourite.setImageResource(R.drawable.favourite_icon);
            else
                supplierProfilefavourite.setImageResource(R.drawable.black_unfavourite);
            List<SupplierProfile.Data.Supplier.CompanyDealership> authDealers = supplier.getCompanyDealership();
            if (authDealers != null) {
                if (authDealers.size() != 0) {
                    Collections.sort(authDealers, new Comparator<SupplierProfile.Data.Supplier.CompanyDealership>() {
                        @Override
                        public int compare(SupplierProfile.Data.Supplier.CompanyDealership o1, SupplierProfile.Data.Supplier.CompanyDealership o2) {
                            return o1.getCompanyName().compareToIgnoreCase(o2.getCompanyName());
                        }
                    });
                    noSuppliers.setVisibility(View.GONE);
                    AuthorisedDealersAdapter authorisedDealersAdapter = new AuthorisedDealersAdapter(context, authDealers);
                    supplierProfileauthorisedDealerOfList.setAdapter(authorisedDealersAdapter);
                } else {
                    noSuppliers.setVisibility(View.VISIBLE);
                }
            } else {
                noSuppliers.setVisibility(View.VISIBLE);
            }
        }
    }

    private void hideSupplierProfileDetail() {
        productDetailBoolean = true;
        suppliersProfile.setVisibility(View.GONE);
        supplierDetailBoolean = false;
        productDetailView.setVisibility(View.VISIBLE);
    }

    private void noItemView() {
        try {
            noItemBoolean = true;
            ArrayList<String> spinnerErrorList = new ArrayList<>();
            spinnerErrorList.add("Not Finding Product");
            ArrayAdapter cityAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerErrorList);
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            searchErrorSpinner.setAdapter(cityAdapter);
            spinnerError.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitEvent() {

        final String query = "Not Finding Product";
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
                                        showAlert("Your Medicine/Product request has been sent successfully");
                                    else
                                        showAlert("Your Medicine/Product request has been sent successfully");
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
            MultipartUtility multipart = new MultipartUtility(APIInterface.PRODUCT_NOT_FOUND, "UTF-8");
            multipart.addFormField("user_id", userId);
            multipart.addFormField("mobile_no", mobileNumber1);
            multipart.addFormField("device_type", deviceType);
            multipart.addFormField("device_token", deviceToken);
            multipart.addFormField("issue", query);
            multipart.addFormField("product_name", nameOfSearchedMedicine);
            multipart.addFormField("description", description);
            multipart.addFormField("city", PreferenceConnector.readString(context,PreferenceConnector.CITY,""));
            multipart.addFormField("request_type", "1");
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
            msg = e.toString();
            e.printStackTrace();
        }
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

    private String picturePath, filePath;
    private boolean isImage;

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
                        picturePath = newfile.getAbsolutePath();
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
                    isImage = true;
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

    private void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppUtil.hideKeyBoard(context);
                        parentItemListView.setVisibility(View.VISIBLE);
                        parentNoItemsView.setVisibility(View.GONE);
                        suppliersProfile.setVisibility(View.GONE);
                        if(context.getClass().getSimpleName().equals("MedicineSearchActivity"))
                            MedicineSearchActivity.search.removeTextChangedListener(textWatcher);
                        else
                            OrderRegisterActivity.search.removeTextChangedListener(textWatcher);
                    }
                });
        AlertDialog dialog = builder.create();
        if (!((Activity) context).isFinishing())
            dialog.show();
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


    private void callNotFindProduct() {
        nameOfSearchedMedicine = text;
        getDescription().setText(text);
        serchResult = false;
        parentItemListView.setVisibility(View.GONE);
        parentNoItemsView.setVisibility(View.VISIBLE);
        noItemBoolean = true;
        noItemView();
        suppliersProfile.setVisibility(View.GONE);
        productDetailView.setVisibility(View.GONE);
    }



}
