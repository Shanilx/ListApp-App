package com.listapp.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Get_Profile.GetProfileResponse;
import com.listapp.Adapter.NavigationListAdapter;
import com.listapp.Fragment.MedicineHomeFragment;
import com.listapp.Interface.BackFragmentHelper;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MedicineSearchActivity extends AppCompatActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener, View.OnClickListener {

    public static DrawerLayout drawer;
    private Toolbar toolbar;
    private TextView userName, mobileNumber, address;
    public static TextView heading;
    public static LinearLayout searchView, backButton, toolbarCloseIconView;
    public static AutoCompleteTextView search;
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
    private FirebaseAnalytics mFirebaseAnalytics;
    String currentVersion, latestVersion;
    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //        setContentView(R.layout.activity_home_search);
//        FullScreenSupporterSearch.assistActivity(MedicineSearchActivity.this);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        setUpView();
        whichScreen = 1;
        getSupportFragmentManager().beginTransaction().replace(R.id.homeActivityFragment, new MedicineHomeFragment(), "").addToBackStack("").commit();
        heading.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        if (PreferenceConnector.readString(this, PreferenceConnector.CHECK_UPDATE, "true") == "true") {
            getCurrentVersion();
            PreferenceConnector.writeString(this, PreferenceConnector.CHECK_UPDATE, "false");
        }


        AppUtil.hideKeyBoard(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        heading.setVisibility(View.VISIBLE);
//        searchView.setVisibility(View.GONE);
//        backButton.setVisibility(View.GONE);
        userName.setText(PreferenceConnector.readString(this, PreferenceConnector.SHOP_NAME, ""));
        String add = PreferenceConnector.readString(this, PreferenceConnector.ADDRESS, "");
        address.setText(add);
        mobileNumber.setText(PreferenceConnector.readString(this, PreferenceConnector.MOBILE_NUMBER, ""));

        if (isRestart) {
            startActivity(new Intent(this, MedicineSearchActivity.class));
            finish();
            isRestart = false;
        }
        if (!PreferenceConnector.readString(MedicineSearchActivity.this, "gotoNotification", "").equals("")) {

        }
        //    Log.e(">>", "Resume" + isRestart);
    }

    private void setUpView() {

        searchiconView = getToolbarSearchIconView();
        heading = getToolbarHeading();
        searchView = getToolbarSearchView();
        search = getToolbarSearch();
        search.setHint("Search Medicines");
        search.setHintTextColor(ContextCompat.getColor(MedicineSearchActivity.this, R.color.dummyTextLightBlack));
        searchIcon = getToolbarSearchIcon();
        backButton = getToolbarBackButton();
        drawerIcon = getToolBarDrawerIcon();
        backButton.setOnClickListener(this);
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
                AppUtil.hideKeyBoard(MedicineSearchActivity.this);
                if (drawer.isDrawerOpen(GravityCompat.END))
                    drawer.closeDrawer(GravityCompat.END);
                else
                    drawer.openDrawer(GravityCompat.END);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        //  View headerView = navigationView.inflateHeaderView(R.layout.navigation_parentview);

        userName = headerView.findViewById(R.id.shopName);
        mobileNumber = headerView.findViewById(R.id.number);
        address = headerView.findViewById(R.id.address);

        navigationList = findViewById(R.id.expandableList);
        prepareDataNavigationList();
        navigationListAdapter = new NavigationListAdapter(MedicineSearchActivity.this, listDataHeader, listDataChild);
        navigationList.setAdapter(navigationListAdapter);
        navigationList.expandGroup(0, true);

        navigationList.setOnChildClickListener(this);
        navigationList.setOnGroupClickListener(this);

        if (!PreferenceConnector.readBoolean(this, "firstTimeGetProfile", false)) {
            getProfileDetail();
        } else
            setData();

        ImageView shareApp = headerView.findViewById(R.id.shareapp);
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
    }

    private LinearLayout getToolBarDrawerIcon() {
        return (LinearLayout) findViewById(R.id.drawerIcon);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setData() {

    }

    private void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        new GetLatestVersion().execute();

    }

    public void changeLocation(View view) {
        togglDrawer();
        isRestart = true;
        AppUtil.hideKeyBoard(this);
        startActivity(new Intent(this, CitySearchActivity.class));
    }


    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
//It retrieves the latest version by scraping the content of current version from play store at runtime
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.listapp.in&hl=en_US").get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();

            } catch (Exception e) {
                e.printStackTrace();

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) { //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                        showUpdateDialog();
                    }
                }
            } else
                ;
            //background.start();
            super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.listapp.in")));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //background.start();
            }
        });

        builder.setCancelable(false);
        dialog = builder.show();
    }

    private void getProfileDetail() {
        String mobileNumber = PreferenceConnector.readString(this, PreferenceConnector.MOBILE_NUMBER, "");
        String userId = PreferenceConnector.readString(this, PreferenceConnector.USER_ID, "");
        String deviceType = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TYPE, "Android");
        String deviceToken = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TOKEN, "");
        final ProgressDialog progressDialog = AppUtil.createProgressDialog(MedicineSearchActivity.this);
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
                        PreferenceConnector.writeBoolean(MedicineSearchActivity.this, "firstTimeGetProfile", true);
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.FULL_NAME, getProfileResponse.getData().getRetailerName());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.USER_ID, getProfileResponse.getData().getUserId());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.EMAIl, getProfileResponse.getData().getEmail());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.AREA, getProfileResponse.getData().getArea());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.ADDRESS, getProfileResponse.getData().getAddress());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.CITY, getProfileResponse.getData().getCity());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.STATE, getProfileResponse.getData().getState());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.SHOP_NAME, getProfileResponse.getData().getShopName());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.CONTACT_NUMBER, getProfileResponse.getData().getContactNumber());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.TIN_NUMBER, getProfileResponse.getData().getTinNumber());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.ESTD_YEAR, getProfileResponse.getData().getEstdYear());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.DL_NUMBER, getProfileResponse.getData().getDrugLicNo());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.CITY_ID, getProfileResponse.getData().getCityId());
                        PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.STATE_ID, getProfileResponse.getData().getStateId());
                        userName.setText(PreferenceConnector.readString(MedicineSearchActivity.this, PreferenceConnector.SHOP_NAME, ""));
                        String add = PreferenceConnector.readString(MedicineSearchActivity.this, PreferenceConnector.ADDRESS, "");
                        address.setText(add);
                        MedicineSearchActivity.this.mobileNumber.setText(PreferenceConnector.readString(MedicineSearchActivity.this, PreferenceConnector.MOBILE_NUMBER, ""));

                        List<GetProfileResponse.Data.ContactPerson> list = getProfileResponse.getData().getContactPerson();
                        StringBuilder strContactPerson = new StringBuilder();
                        if (list != null) {
                            int length = list.size();
                            for (int i = 0; i < length; i++) {
                                strContactPerson.append("- " + list.get(i).getContactName() + " : " + list.get(i).getContactNumber() + "\n");
                            }
                            PreferenceConnector.writeString(MedicineSearchActivity.this, PreferenceConnector.CONTACT_PERSON, strContactPerson.toString());
                        }
                        setData();
                    } else {
                        if (getProfileResponse.getMessage().equalsIgnoreCase("Your session has been expired")) {
                            AppUtil.sessionLogout(getProfileResponse.getMessage(), MedicineSearchActivity.this);
                        } else if (getProfileResponse.getMessage().equalsIgnoreCase("Your account has been deactivated by administrator"))
                            AppUtil.sessionLogout(getProfileResponse.getMessage(), MedicineSearchActivity.this);
                        else
                            AppUtil.showAlert(getProfileResponse.getMessage(), MedicineSearchActivity.this);
                    }
                } else
                    AppUtil.showAlert(getString(R.string.serverError), MedicineSearchActivity.this);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                AppUtil.showAlert(getString(R.string.serverError), MedicineSearchActivity.this);
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });

    }

    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolBar);
    }

    public AutoCompleteTextView getToolbarSearch() {
        return (AutoCompleteTextView) findViewById(R.id.toolbarSearch);
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
        if (PreferenceConnector.readString(this, PreferenceConnector.CITY, "").length() < 4)
            PreferenceConnector.writeString(this, PreferenceConnector.CITY, "Indore");
        listDataHeader.add(PreferenceConnector.readString(this, PreferenceConnector.CITY, ""));
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
        Log.d("token", PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TOKEN, ""));


        listDataChild.put(listDataHeader.get(0), childList);


    }

    private void clearFragmentStack() {
        for (int i = 1; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
        MedicineHomeFragment.hideFragmentView();
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
                AppUtil.userLogout(MedicineSearchActivity.this);
                break;
        }

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (groupPosition == 0) {
            switch (childPosition) {

                case 0:
                    togglDrawer();
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
            }
        }
        if (groupPosition == 1) {


        }

        return false;

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.toolbarBackButton:
                onBackPressed();
                break;

        }
    }

    private void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setCancelable(false);
        builder.setTitle("ListApp").
                setMessage(msg).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


}

