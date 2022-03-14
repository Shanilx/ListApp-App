package com.listapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.listapp.Adapter.NavigationListAdapter;
import com.listapp.Fragment.AboutUSFragment;
import com.listapp.ListAppUtil.AppUtil;
import com.listapp.ListAppUtil.PreferenceConnector;
import com.listapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AboutUSActivity extends AppCompatActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {

    public static DrawerLayout drawer;
    private Toolbar toolbar;
    public static TextView heading, userName, mobileNumber, address;
    public static LinearLayout searchView, backButton;
    public static EditText search;
    public static ImageView searchIcon;
    private LinearLayout drawerIcon;
    public static ExpandableListView navigationList;
    private ArrayList<String> listDataHeader;
    private HashMap listDataChild;
    private NavigationListAdapter navigationListAdapter;
    public static int whichScreen = 0;
    private boolean isRestart = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        setTitle("");
        setUpView();
        whichScreen = 1;
        getSupportFragmentManager().beginTransaction().replace(R.id.homeActivityFragment, new AboutUSFragment(), "").addToBackStack("").commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userName.setText(PreferenceConnector.readString(this, PreferenceConnector.SHOP_NAME, ""));
        address.setText(PreferenceConnector.readString(this, PreferenceConnector.ADDRESS, ""));
        mobileNumber.setText(PreferenceConnector.readString(this, PreferenceConnector.MOBILE_NUMBER, ""));
        heading.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);

        if(isRestart)
        {
            startActivity(new Intent(this,AboutUSActivity.class));
            finish();
            isRestart = false;
        }
    }

    private void setUpView() {

        heading = getToolbarHeading();
        searchView = getToolbarSearchView();
        search = getToolbarSearch();
        searchIcon = getToolbarSearchIcon();
        backButton = getToolbarBackButton();
        heading.setText("ABOUT US");
        drawerIcon = getToolBarDrawerIcon();
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        userName.setText(PreferenceConnector.readString(this,PreferenceConnector.SHOP_NAME,""));
        address.setText(PreferenceConnector.readString(this,PreferenceConnector.ADDRESS,""));
        mobileNumber.setText(PreferenceConnector.readString(this,PreferenceConnector.MOBILE_NUMBER,""));

        navigationList = findViewById(R.id.expandableList);
        prepareDataNavigationList();
        navigationListAdapter = new NavigationListAdapter(AboutUSActivity.this, listDataHeader, listDataChild);
        navigationList.setAdapter(navigationListAdapter);
        navigationList.expandGroup(0,true);

        navigationList.setOnChildClickListener(this);
        navigationList.setOnGroupClickListener(this);
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


    }


    private LinearLayout getToolBarDrawerIcon() {
        return (LinearLayout) findViewById(R.id.drawerIcon);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else
            finish();
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

    private void clearFragmentStack()
    {
        for(int i=1;i<getSupportFragmentManager().getBackStackEntryCount();i++)
        {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        switch (groupPosition) {

            case 1:
                clearFragmentStack();
                togglDrawer();
                isRestart = true;
                AppUtil.hideKeyBoard(this);
                startActivity(new Intent(this, CitySearchActivity.class));
                break;


            case 2:
                togglDrawer();
                isRestart = true;
                startActivity(new Intent(this,ProfileActivity.class));
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
                togglDrawer();
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
                AppUtil.userLogout(AboutUSActivity.this);
                break;
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        switch (childPosition) {

            case 0:
                togglDrawer();
                isRestart = true;
                startActivity(new Intent(this,MedicineSearchActivity.class));
                break;

            case 1:
                togglDrawer();
                isRestart = true;
                startActivity(new Intent(this,SupplierSearchActivity.class));
                break;

            case 2:
                togglDrawer();
                isRestart = true;
                startActivity(new Intent(this, CompanySearchActivity.class));
                break;
        }

        return false;
    }

}
