package com.listapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.CompanySearch.CompanyDetailResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Get_Profile.GetProfileResponse;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.SupplierDetails.SupplierProfile;
import com.listapp.Adapter.NavigationListAdapter;
import com.listapp.Interface.BackFragmentHelper;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoActivity extends AppCompatActivity implements  View.OnClickListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

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
    private ImageView contactOneImage;
    private LinearLayout viewContact1;
    private LinearLayout viewContact2;
    private LinearLayout viewContact3;
    private LinearLayout viewContact4;
    private LinearLayout viewContact5;
    private ProgressDialog progressDialog;

    public static DrawerLayout drawer;
    private Toolbar toolbar;
    private TextView  userName, mobileNumber, address;
    public static TextView heading;
    public static LinearLayout searchView, backButton, toolbarCloseIconView;
    public static EditText search;
    public static ImageView searchIcon;
    private LinearLayout drawerIcon;
    public static LinearLayout searchiconView;
    public static ExpandableListView navigationList;
    private ArrayList<String> listDataHeader;
    private HashMap listDataChild;
    private NavigationListAdapter navigationListAdapter;
    public static int whichScreen = 0;
    public static int once = 0;
    //    private MarshMallowPermission marshMallowPermission;
    private boolean isRestart = false;
    private boolean doubleBackToExitPressedOnce = false;

    private retrofit2.Call searchSupplierCall, supplierProfileCall;
    private List<CompanyDetailResponse.Data.Supplier> companyDataList;
    private APIInterface apiInterface;
    private List<SupplierProfile.Data.Supplier.ContactPerson> supplierContactPersonsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplierdetail);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        setUpView();
        whichScreen = 1;
        heading.setVisibility(View.VISIBLE);
        heading.setText("Supplier Details");

        suppliersProfile = findViewById(R.id.suppliersProfile);
        supplierProfileName =  findViewById(R.id.supplierProfileName);
    //    supplierProfilecityName =  findViewById(R.id.supplierProfilecityName);
        supplierProfileauthorize =  findViewById(R.id.supplierProfileauthorize);
        supplierProfilefavourite =  findViewById(R.id.supplierProfilefavourite);
        contactOneImage =  findViewById(R.id.contactOneImage);
        supplierProfilecontactPerson1 =  findViewById(R.id.supplierProfilecontactPerson1);
        supplierProfilecontactPerson5 =  findViewById(R.id.supplierProfilecontactPerson5);
        supplierProfilecontactPerson4 =  findViewById(R.id.supplierProfilecontactPerson4);
        supplierProfilecontactPerson3 =  findViewById(R.id.supplierProfilecontactPerson3);
        supplierProfilecontactPerson2 =  findViewById(R.id.supplierProfilecontactPerson2);

        supplierProfilecontactPerson1.setOnClickListener(this);
        supplierProfilecontactPerson2.setOnClickListener(this);
        supplierProfilecontactPerson3.setOnClickListener(this);
        supplierProfilecontactPerson4.setOnClickListener(this);
        supplierProfilecontactPerson5.setOnClickListener(this);

        viewContact5 =  findViewById(R.id.viewContact5);
        viewContact4 =  findViewById(R.id.viewContact4);
        viewContact3 =  findViewById(R.id.viewContact3);
        viewContact2 =  findViewById(R.id.viewContact2);
        viewContact1 =  findViewById(R.id.viewContact1);


        supplierProfilearea =  findViewById(R.id.supplierProfilearea);
        supplierProfileaddress =  findViewById(R.id.supplierProfileaddress);
        supplierProfilecontctNumber =  findViewById(R.id.supplierProfilecontctNumber);
        supplierProfilecontctNumber.setOnClickListener(this);
        supplierProfileemail =  findViewById(R.id.supplierProfileemail);
        supplierProfiledrugLicenceNumber =  findViewById(R.id.supplierProfiledrugLicenceNumber);
        supplierProfiletinNumber =  findViewById(R.id.supplierProfiletinNumber);
        supplierProfileestdYear =  findViewById(R.id.supplierProfileestdYear);
        supplierProfileexpandCollapseView =  findViewById(R.id.supplierProfileexpandCollapseView);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.d("lauri",data.toString().substring(23));
        initSupplierProfileDetail(data.toString().substring(23));
    }
    private void initSupplierProfileDetail(final String supplierID) {

        if (progressDialog == null) {
            progressDialog = AppUtil.createProgressDialog(this);
            if (!progressDialog.isShowing())
                progressDialog.show();
        } else {
            if (!progressDialog.isShowing())
                progressDialog.show();
        }
        String mobileNumber = PreferenceConnector.readString(this, PreferenceConnector.MOBILE_NUMBER, "");
        String userId = PreferenceConnector.readString(this, PreferenceConnector.USER_ID, "");
        String deviceType = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TYPE, "Android");
        String deviceToken = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TOKEN, "");
        apiInterface = APIClient.getClient().create(APIInterface.class);

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
                      //      showProfileDetail();
                            setSupplierProfileDetail(supplierProfile.getData());
                        } else {
                            if (msg.equalsIgnoreCase("Your session has been expired")) {
                                AppUtil.sessionLogout(msg, PromoActivity.this);
                            } else if (msg.equalsIgnoreCase("Your account has been deactivated by administrator"))
                                AppUtil.sessionLogout(msg, PromoActivity.this);
                            else {
                                //showListView();
                              //  AppUtil.showAlert(msg, context);
                            }
                        }
                    } else {
                        //showListView();
                   //     AppUtil.showAlert(context.getString(R.string.serverError), context);
                    }
                } else {
                  //  showListView();
                  //  AppUtil.showAlert(context.getString(R.string.serverError), context);
                }
            }

            @Override
            public void onFailure(retrofit2.Call call, Throwable t) {
                if (progressDialog != null)
                    progressDialog.dismiss();
               // showListView();
               // AppUtil.showAlert(context.getString(R.string.serverError), context);
            }
        });
    }


    private void setSupplierProfileDetail(SupplierProfile.Data data) {

        if (data.getSupplier() != null && data.getSupplier().size() != 0) {
           // hideKeyboardView1.scrollTo(0, 0);
           // isDetailOpened = true;
            supplierProfilearea.setText("");
     //       supplierProfilecityName.setText("");
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
   //         supplierProfilecityName.setText(supplier.getCity().toUpperCase());

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
                    contactOneImage.setVisibility( View.VISIBLE);
                    if (length == 1) {
                        supplierProfilecontactPerson1.setText(supplierContactPersonsList.get(0).getContactName() + " : " +
                                supplierContactPersonsList.get(0).getContactNumber());
                        viewContact1.setVisibility( View.VISIBLE);
                        viewContact2.setVisibility( View.GONE);
                        viewContact3.setVisibility( View.GONE);
                        viewContact4.setVisibility( View.GONE);
                        viewContact5.setVisibility( View.GONE);
                    } else if (length == 2) {
                        supplierProfilecontactPerson1.setText(supplierContactPersonsList.get(0).getContactName() + " : " +
                                supplierContactPersonsList.get(0).getContactNumber());
                        supplierProfilecontactPerson2.setText(supplierContactPersonsList.get(1).getContactName() + " : " +
                                supplierContactPersonsList.get(1).getContactNumber());

                        viewContact1.setVisibility( View.VISIBLE);
                        viewContact2.setVisibility( View.VISIBLE);
                        viewContact3.setVisibility( View.GONE);
                        viewContact4.setVisibility( View.GONE);
                        viewContact5.setVisibility( View.GONE);
                    } else if (length == 3) {
                        supplierProfilecontactPerson1.setText(supplierContactPersonsList.get(0).getContactName() + " : " +
                                supplierContactPersonsList.get(0).getContactNumber());
                        supplierProfilecontactPerson2.setText(supplierContactPersonsList.get(1).getContactName() + " : " +
                                supplierContactPersonsList.get(1).getContactNumber());
                        supplierProfilecontactPerson3.setText(supplierContactPersonsList.get(2).getContactName() + " : " +
                                supplierContactPersonsList.get(2).getContactNumber());
                        viewContact1.setVisibility( View.VISIBLE);
                        viewContact2.setVisibility( View.VISIBLE);
                        viewContact3.setVisibility( View.VISIBLE);
                        viewContact4.setVisibility( View.GONE);
                        viewContact5.setVisibility( View.GONE);
                    } else if (length == 4) {
                        supplierProfilecontactPerson1.setText(supplierContactPersonsList.get(0).getContactName() + " : " +
                                supplierContactPersonsList.get(0).getContactNumber());
                        supplierProfilecontactPerson2.setText(supplierContactPersonsList.get(1).getContactName() + " : " +
                                supplierContactPersonsList.get(1).getContactNumber());
                        supplierProfilecontactPerson3.setText(supplierContactPersonsList.get(2).getContactName() + " : " +
                                supplierContactPersonsList.get(2).getContactNumber());
                        supplierProfilecontactPerson4.setText(supplierContactPersonsList.get(3).getContactName() + " : " +
                                supplierContactPersonsList.get(3).getContactNumber());
                        viewContact1.setVisibility( View.VISIBLE);
                        viewContact2.setVisibility( View.VISIBLE);
                        viewContact3.setVisibility( View.VISIBLE);
                        viewContact4.setVisibility( View.VISIBLE);
                        viewContact5.setVisibility( View.GONE);
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

                        viewContact1.setVisibility( View.VISIBLE);
                        viewContact2.setVisibility( View.VISIBLE);
                        viewContact3.setVisibility( View.VISIBLE);
                        viewContact4.setVisibility( View.VISIBLE);
                        viewContact5.setVisibility( View.VISIBLE);
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

                        viewContact1.setVisibility( View.VISIBLE);
                        viewContact2.setVisibility( View.VISIBLE);
                        viewContact3.setVisibility( View.VISIBLE);
                        viewContact4.setVisibility( View.VISIBLE);
                        viewContact5.setVisibility( View.VISIBLE);
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

          
           // dealersList = supplier.getCompanyDealership();
//            if (dealersList != null) {
//                if (dealersList.size() > 0) {
//                    Collections.sort(dealersList, new Comparator<SupplierProfile.Data.Supplier.CompanyDealership>() {
//                        @Override
//                        public int compare(SupplierProfile.Data.Supplier.CompanyDealership o1, SupplierProfile.Data.Supplier.CompanyDealership o2) {
//                            return o1.getCompanyName().compareToIgnoreCase(o2.getCompanyName());
//                        }
//                    });
//                    AuthorisedDealersAdapter authorisedDealersAdapter = new AuthorisedDealersAdapter(context, dealersList);
//                    supplierProfileauthorisedDealerOfList.setAdapter(authorisedDealersAdapter);
//                    noSuppliers.setVisibility( View.GONE);
//                    supplierProfileexpandCollapse setVisibility( View.VISIBLE);
//                } else {
//                    noSuppliers.setVisibility( View.VISIBLE);
//                    supplierProfileexpandCollapse setVisibility( View.GONE);
//                }
            } else {
             //   noSuppliers.setVisibility( View.VISIBLE);
             //   supplierProfileexpandCollapse setVisibility( View.GONE);
            }
        }
    private void setUpView() {

        heading = getToolbarHeading();
        drawerIcon = getToolBarDrawerIcon();
        toolbarCloseIconView = findViewById(R.id.toolbarCloseIconView);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.hideKeyBoard(PromoActivity.this);
                if (drawer.isDrawerOpen(GravityCompat.END))
                    drawer.closeDrawer(GravityCompat.END);
                else
                    drawer.openDrawer(GravityCompat.END);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView shareApp =headerView.findViewById(R.id.shareapp);
        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n" +
                        "Use ListApp for finding right Suppliers\n" +
                        "Download - https://bit.ly/listapp1");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);

                startActivity(shareIntent);
            }
        });
        //  View headerView = navigationView.inflateHeaderView(R.layout.navigation_parentview);

        userName = headerView.findViewById(R.id.shopName);
        mobileNumber = headerView.findViewById(R.id.number);
        address = headerView.findViewById(R.id.address);

        navigationList = findViewById(R.id.expandableList);
        prepareDataNavigationList();
        navigationListAdapter = new NavigationListAdapter(PromoActivity.this, listDataHeader, listDataChild);
        navigationList.setAdapter(navigationListAdapter);
        navigationList.expandGroup(0, true);

        navigationList.setOnChildClickListener(this);
        navigationList.setOnGroupClickListener(this);

        if (!PreferenceConnector.readBoolean(this, "firstTimeGetProfile", false)) {
            getProfileDetail();
        } else
            setData();
    }

    private void setData() {
        userName.setText(PreferenceConnector.readString(PromoActivity.this, PreferenceConnector.SHOP_NAME, ""));
        String add = PreferenceConnector.readString(PromoActivity.this, PreferenceConnector.ADDRESS, "");
        address.setText(add);
        PromoActivity.this.mobileNumber.setText(PreferenceConnector.readString(PromoActivity.this, PreferenceConnector.MOBILE_NUMBER, ""));

    }

    private void getProfileDetail() {
        String mobileNumber = PreferenceConnector.readString(this, PreferenceConnector.MOBILE_NUMBER, "");
        String userId = PreferenceConnector.readString(this, PreferenceConnector.USER_ID, "");
        String deviceType = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TYPE, "Android");
        String deviceToken = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TOKEN, "");

        final ProgressDialog progressDialog = AppUtil.createProgressDialog(PromoActivity.this);
        if (!progressDialog.isShowing())
            progressDialog.show();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<GetProfileResponse> getProfileCall = apiInterface.getProfileDetail(mobileNumber, userId, deviceType, deviceToken);
        getProfileCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                GetProfileResponse getProfileResponse = (GetProfileResponse) response.body();
                if (getProfileResponse != null) {

                    if (getProfileResponse.getError().equals("0")) {
                        PreferenceConnector.writeBoolean(PromoActivity.this, "firstTimeGetProfile", true);
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.FULL_NAME, getProfileResponse.getData().getRetailerName());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.USER_ID, getProfileResponse.getData().getUserId());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.EMAIl, getProfileResponse.getData().getEmail());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.AREA, getProfileResponse.getData().getArea());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.ADDRESS, getProfileResponse.getData().getAddress());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.CITY, getProfileResponse.getData().getCity());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.STATE, getProfileResponse.getData().getState());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.SHOP_NAME, getProfileResponse.getData().getShopName());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.CONTACT_NUMBER, getProfileResponse.getData().getContactNumber());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.TIN_NUMBER, getProfileResponse.getData().getTinNumber());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.ESTD_YEAR, getProfileResponse.getData().getEstdYear());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.DL_NUMBER, getProfileResponse.getData().getDrugLicNo());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.CITY_ID, getProfileResponse.getData().getCityId());
                        PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.STATE_ID, getProfileResponse.getData().getStateId());
                        userName.setText(PreferenceConnector.readString(PromoActivity.this, PreferenceConnector.SHOP_NAME, ""));
                        String add = PreferenceConnector.readString(PromoActivity.this, PreferenceConnector.ADDRESS, "");
                        address.setText(add);
                        PromoActivity.this.mobileNumber.setText(PreferenceConnector.readString(PromoActivity.this, PreferenceConnector.MOBILE_NUMBER, ""));

                        List<GetProfileResponse.Data.ContactPerson> list = getProfileResponse.getData().getContactPerson();
                        StringBuilder strContactPerson = new StringBuilder();
                        if (list != null) {
                            int length = list.size();
                            for (int i = 0; i < length; i++) {
                                strContactPerson.append("- " + list.get(i).getContactName() + " : " + list.get(i).getContactNumber() + "\n");
                            }
                            PreferenceConnector.writeString(PromoActivity.this, PreferenceConnector.CONTACT_PERSON, strContactPerson.toString());
                        }
                        setData();
                    } else {
                        if (getProfileResponse.getMessage().equalsIgnoreCase("Your session has been expired")) {
                            AppUtil.sessionLogout(getProfileResponse.getMessage(), PromoActivity.this);
                        } else if (getProfileResponse.getMessage().equalsIgnoreCase("Your account has been deactivated by administrator"))
                            AppUtil.sessionLogout(getProfileResponse.getMessage(), PromoActivity.this);
                        else
                            AppUtil.showAlert(getProfileResponse.getMessage(), PromoActivity.this);
                    }
                } else
                    AppUtil.showAlert(getString(R.string.serverError), PromoActivity.this);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                AppUtil.showAlert(getString(R.string.serverError), PromoActivity.this);
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });

    }

    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolBar);
    }

    public EditText getToolbarSearch() {
        return (EditText) findViewById(R.id.toolbarSearch);
    }

    public TextView getToolbarHeading() {
        return (TextView) findViewById(R.id.heading);
    }

    public ImageView getToolbarSearchIcon() {
        return (ImageView) findViewById(R.id.toolbarSerachIcon);
    }

    public LinearLayout getToolbarSearchIconView() {
        return (LinearLayout) findViewById(R.id.toolbarSerachIconView);
    }

    public LinearLayout getToolbarSearchView() {
        return (LinearLayout) findViewById(R.id.toolbarSearchView);
    }

    public LinearLayout getToolbarBackButton() {
        return (LinearLayout) findViewById(R.id.toolbarBackButton);
    }
    private LinearLayout getToolBarDrawerIcon() {
        return (LinearLayout) findViewById(R.id.drawerIcon);
    }

    public static void togglDrawer() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else if (!BackFragmentHelper.fireOnBackPressedEvent(this)) {
                int i = getSupportFragmentManager().getBackStackEntryCount();

                if (doubleBackToExitPressedOnce) {
                    if (i <= 2) {
                        finishAffinity();
                    } else {
                        getSupportFragmentManager().popBackStack();
                    }
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Tap Again to Exit " + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareDataNavigationList() {

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Search");
        if(PreferenceConnector.readString(this,PreferenceConnector.CITY,"").length()<4)
            PreferenceConnector.writeString(this,PreferenceConnector.CITY,"Indore");
        listDataHeader.add(PreferenceConnector.readString(this,PreferenceConnector.CITY,""));
        listDataHeader.add("Profile");
        listDataHeader.add("Margin Calculator");
        listDataHeader.add("Registration");
        listDataHeader.add("About Us");
        listDataHeader.add("Notifications");
        listDataHeader.add("Contact Us");
        listDataHeader.add("Logout");

        List<String> childList = new ArrayList<>();

        childList.add("- Medicines/Products");
        childList.add("- Supplier");
        childList.add("- Company");


        listDataChild.put(listDataHeader.get(0), childList);


    }

    private void clearFragmentStack() {
        for (int i = 1; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
        // MedicineHomeFragment.hideFragmentView();
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        switch (groupPosition) {
            case 1:
                //    clearFragmentStack();
                togglDrawer();
                isRestart = true;
                AppUtil.hideKeyBoard(this);
                startActivity(new Intent(this, CitySearchActivity.class));
                break;


            case 2:

                clearFragmentStack();
                togglDrawer();
                isRestart = true;
                AppUtil.hideKeyBoard(this);
                startActivity(new Intent(this, ProfileActivity.class));
                break;

            case 3:
                clearFragmentStack();
                togglDrawer();
                AppUtil.hideKeyBoard(this);
                isRestart = true;
                startActivity(new Intent(this, CalculatorActivity.class));
                break;

            case 4:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/lisupreg/registrations"));
                startActivity(browserIntent);
                break;
            case 5:
                clearFragmentStack();
                togglDrawer();
                AppUtil.hideKeyBoard(this);
                isRestart = true;
                startActivity(new Intent(this, AboutUSActivity.class));
                break;


            case 6:
                clearFragmentStack();
                togglDrawer();
                isRestart = true;
                AppUtil.hideKeyBoard(this);
                startActivity(new Intent(this, NotificationActivity.class));
                break;


            case 7:
                clearFragmentStack();
                togglDrawer();
                AppUtil.hideKeyBoard(this);
                isRestart = true;
                startActivity(new Intent(this, ContactUSActivity.class));
                break;

            case 8:
                togglDrawer();
                AppUtil.hideKeyBoard(this);
                clearFragmentStack();
                AppUtil.userLogout(PromoActivity.this);
                break;
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if(groupPosition == 0) {
            switch (childPosition) {

                case 0:
                    togglDrawer();
                    AppUtil.hideKeyBoard(this);
                    clearFragmentStack();
                    isRestart = true;
                    startActivity(new Intent(this, MedicineSearchActivity.class));
                    break;

                case 1:
                    togglDrawer();
                    AppUtil.hideKeyBoard(this);
                    clearFragmentStack();
                    isRestart = true;
                    startActivity(new Intent(this, SupplierSearchActivity.class));
                    break;

                case 2:
                    togglDrawer();
                    AppUtil.hideKeyBoard(this);
                    clearFragmentStack();
                    startActivity(new Intent(this, CompanySearchActivity.class));
                    isRestart = true;
                    break;
            }}
        if(groupPosition == 1){


        }

        return false;

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        EditText e;
        try{
            e= (EditText) v;
            e.selectAll();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (id) {
            case R.id.toolbarBackButton:
                onBackPressed();
                break;
        }
    }

}
