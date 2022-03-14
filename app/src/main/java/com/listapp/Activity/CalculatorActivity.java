package com.listapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import com.listapp.API_Utility.Ratrofit_Implementation.Model.Get_Profile.GetProfileResponse;
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

//import com.listapp.Fragment.MedicineHomeFragment;

public class CalculatorActivity<ex> extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener, View.OnTouchListener {
    private float PQ;
    private float FQ;
    private float RATE;
    private float GST;
    private float CD;
    private float MRP;
    private float AMOUNT;
    private float MARPER;
    EditText PQtext;
    EditText FQtext;
    EditText RATEtext;
    EditText GSTtext;
    EditText CDtext;
    EditText MRPtext;
    TextView AMOUNTtext;
    TextView MARPERtext;
    ArrayList ids=new ArrayList<Integer>();
    ArrayList redoids=new ArrayList<Integer>();
    ArrayList actions=new ArrayList<String>();
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
    private ArrayList<String> undoactions=new ArrayList<String>();
    private boolean fromundo;
    private ColorStateList oldColors;
    private String color;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        setUpView();
        whichScreen = 1;
     //   getSupportFragmentManager().beginTransaction().replace(R.id.homeActivityFragment, new MedicineHomeFragment(), "").addToBackStack("").commit();
        heading.setVisibility(View.VISIBLE);
       // searchView.setVisibility(View.GONE);
      //  backButton.setVisibility(View.GONE);
        heading.setText("Margin calculator");

        fromundo=false;
        PQ=1f;
        FQ=0f;
        MRP=1f;
        RATE=0f;
        CD=1.5f;
        GST=12;
        PQtext=findViewById(R.id.paidqty);

        FQtext=findViewById(R.id.freeqty);
        RATEtext=findViewById(R.id.rate);
        GSTtext=findViewById(R.id.gst);
        CDtext=findViewById(R.id.cashdisc);

        CDtext.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    calc();
                    CDtext.clearFocus();
                    return true;
                }

                return false;
            }
        });
        MRPtext=findViewById(R.id.mrp);
        AMOUNTtext=findViewById(R.id.marginamount);
        MARPERtext=findViewById(R.id.marginonmrp);
        AMOUNTtext.setOnFocusChangeListener(this);
        MARPERtext.setOnFocusChangeListener(this);
        PQtext.setOnFocusChangeListener(this);
        FQtext.setOnFocusChangeListener(this);
        RATEtext.setOnFocusChangeListener(this);
        GSTtext.setOnFocusChangeListener(this);
        CDtext.setOnFocusChangeListener(this);
        MRPtext.setOnFocusChangeListener(this);

        oldColors =  AMOUNTtext.getTextColors();
        setUpView();

        PQtext.setOnClickListener(this);
        FQtext.setOnClickListener(this);
        RATEtext.setOnClickListener(this);
        GSTtext.setOnClickListener(this);
        CDtext.setOnClickListener(this);
        MRPtext.setOnClickListener(this);
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

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        AppUtil.hideKeyBoardWithAllView(this, event);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isRestart){
            Intent i= new Intent(this,CalculatorActivity.class);
            startActivity(i);
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
                AppUtil.hideKeyBoard(CalculatorActivity.this);
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
        navigationListAdapter = new NavigationListAdapter(CalculatorActivity.this, listDataHeader, listDataChild);
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
        userName.setText(PreferenceConnector.readString(CalculatorActivity.this, PreferenceConnector.SHOP_NAME, ""));
        String add = PreferenceConnector.readString(CalculatorActivity.this, PreferenceConnector.ADDRESS, "");
        address.setText(add);
        CalculatorActivity.this.mobileNumber.setText(PreferenceConnector.readString(CalculatorActivity.this, PreferenceConnector.MOBILE_NUMBER, ""));

    }

    private void getProfileDetail() {
        String mobileNumber = PreferenceConnector.readString(this, PreferenceConnector.MOBILE_NUMBER, "");
        String userId = PreferenceConnector.readString(this, PreferenceConnector.USER_ID, "");
        String deviceType = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TYPE, "Android");
        String deviceToken = PreferenceConnector.readString(this, PreferenceConnector.DEVICE_TOKEN, "");

        final ProgressDialog progressDialog = AppUtil.createProgressDialog(CalculatorActivity.this);
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
                        PreferenceConnector.writeBoolean(CalculatorActivity.this, "firstTimeGetProfile", true);
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.FULL_NAME, getProfileResponse.getData().getRetailerName());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.USER_ID, getProfileResponse.getData().getUserId());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.EMAIl, getProfileResponse.getData().getEmail());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.AREA, getProfileResponse.getData().getArea());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.ADDRESS, getProfileResponse.getData().getAddress());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.CITY, getProfileResponse.getData().getCity());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.STATE, getProfileResponse.getData().getState());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.SHOP_NAME, getProfileResponse.getData().getShopName());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.CONTACT_NUMBER, getProfileResponse.getData().getContactNumber());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.TIN_NUMBER, getProfileResponse.getData().getTinNumber());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.ESTD_YEAR, getProfileResponse.getData().getEstdYear());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.DL_NUMBER, getProfileResponse.getData().getDrugLicNo());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.CITY_ID, getProfileResponse.getData().getCityId());
                        PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.STATE_ID, getProfileResponse.getData().getStateId());
                        userName.setText(PreferenceConnector.readString(CalculatorActivity.this, PreferenceConnector.SHOP_NAME, ""));
                        String add = PreferenceConnector.readString(CalculatorActivity.this, PreferenceConnector.ADDRESS, "");
                        address.setText(add);
                        CalculatorActivity.this.mobileNumber.setText(PreferenceConnector.readString(CalculatorActivity.this, PreferenceConnector.MOBILE_NUMBER, ""));

                        List<GetProfileResponse.Data.ContactPerson> list = getProfileResponse.getData().getContactPerson();
                        StringBuilder strContactPerson = new StringBuilder();
                        if (list != null) {
                            int length = list.size();
                            for (int i = 0; i < length; i++) {
                                strContactPerson.append("- " + list.get(i).getContactName() + " : " + list.get(i).getContactNumber() + "\n");
                            }
                            PreferenceConnector.writeString(CalculatorActivity.this, PreferenceConnector.CONTACT_PERSON, strContactPerson.toString());
                        }
                        setData();
                    } else {
                        if (getProfileResponse.getMessage().equalsIgnoreCase("Your session has been expired")) {
                            AppUtil.sessionLogout(getProfileResponse.getMessage(), CalculatorActivity.this);
                        } else if (getProfileResponse.getMessage().equalsIgnoreCase("Your account has been deactivated by administrator"))
                            AppUtil.sessionLogout(getProfileResponse.getMessage(), CalculatorActivity.this);
                        else
                            AppUtil.showAlert(getProfileResponse.getMessage(), CalculatorActivity.this);
                    }
                } else
                    AppUtil.showAlert(getString(R.string.serverError), CalculatorActivity.this);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                AppUtil.showAlert(getString(R.string.serverError), CalculatorActivity.this);
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
                togglDrawer();
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
                AppUtil.userLogout(CalculatorActivity.this);
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


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //Mar Amount = MRP - (((PQ*Rate)(1-CD)(1+GST))/(PQ+FQ))
        //Mar % = 100*(MRP - Mar Amount)/MRP
        if(fromundo)
        {
            fromundo=false;
        }else{
            ids.add(v.getId());
            undoactions.add("");
            if(hasFocus) {
            if(!AMOUNTtext.getText().toString().isEmpty()&&!MARPERtext.getText().toString().isEmpty()){
                   calc();
                }

            }
        }
        EditText editText= (EditText) v;
        float f;
        if(!editText.getText().toString().isEmpty()) {
            if (editText.getId() == R.id.cashdisc || editText.getId() == R.id.gst) {
                String tmpcd= editText.getText().toString().replace("%","");
                f = Float.valueOf(tmpcd);
                if(f % 1 != 0)
                    editText.setText(String.format("%.2f", f) + "%");
                else
                    editText.setText(String.format("%.0f", f)+"%");
            } else {
                f = Float.parseFloat(editText.getText().toString());
                if(f % 1 != 0)
                    editText.setText(String.format("%.2f", f));
                else
                    editText.setText(String.format("%.0f", f));
            }
            editText.selectAll();
        }else{
            f=0f;
            if (editText.getId() == R.id.cashdisc || editText.getId() == R.id.gst) {
              if(editText.getId() != R.id.gst);
                else
                    f=12f;
                    editText.setText(String.format("%.0f", f)+"%");
            } else if (editText.getId() == R.id.paidqty || editText.getId() == R.id.mrp){
                    editText.setText("1");

            }else{

                editText.setText(String.valueOf(f));
            }
            editText.selectAll();
                    }

    }


    private void calc() {
        if (!FQtext.getText().toString().isEmpty() || FQtext.getText().toString() == "") {
            FQ = Float.valueOf(FQtext.getText().toString());
        }else{
            FQ=0f;
            FQtext.setText(String.valueOf(FQ));
        }
        if (!CDtext.getText().toString().isEmpty() || CDtext.getText().toString() == "") {
            String tmpcd= CDtext.getText().toString().replace("%","");
            CD = Float.valueOf(tmpcd);
        }else{
            CD=1.5f;
            CDtext.setText(CD+"%");
        }
        if (!GSTtext.getText().toString().isEmpty() || GSTtext.getText().toString() == "") {
            String tmpGST= GSTtext.getText().toString().replace("%","");
            GST = Float.valueOf(tmpGST);
        }else{
            GST=12;
            GSTtext.setText(GST +"%");
        }
        if (!MRPtext.getText().toString().isEmpty() || MRPtext.getText().toString() == "") {
            MRP = Float.valueOf(MRPtext.getText().toString());
        } else {
            MRP = 1f;
            MRPtext.setText(String.valueOf(MRP));

        }
        if (!PQtext.getText().toString().isEmpty() || PQtext.getText().toString() == "") {
            PQ = Float.valueOf(PQtext.getText().toString());
        } else {
            PQ = 1f;
            PQtext.setText(String.valueOf(PQ));
        }
        if (!RATEtext.getText().toString().isEmpty() || RATEtext.getText().toString() == "") {
            RATE = Float.valueOf(RATEtext.getText().toString());
        } else {
            RATE = 0f;
            RATEtext.setText(String.valueOf(RATE));
//
        }

        float subtotal=PQ*RATE;
        String tmpcd=String.valueOf(CD).replace("%","");
        String tmpGST=String.valueOf(GST).replace("%","");
        float TAdiscount=subtotal-subtotal*(Float.valueOf(tmpcd)/100) ;
        float TAtax=TAdiscount+TAdiscount*(Float.valueOf(tmpGST)/100);
        float perunitrate=TAtax/(PQ+FQ);

        AMOUNT = MRP - perunitrate;
        MARPER = 100 * ((AMOUNT) / MRP);
        AMOUNTtext.setTextSize(24);

        if(MARPER < 0){
        color="#ff6361";
        }
        else if(MARPER < 20){
           color="#ffba92";
        }else if(MARPER < 30){
           color="#94aa2a";
        }else{
            color="#494ca2";
        }

        String tmpamt=String.format("%.2f",AMOUNT);
        String text = "Margin "+"<b><font color="+color+">"+"\u20B9 "+tmpamt+"</font></b>";
        String mtext ="with "+"<b><font color="+color+">"+String.format("%.2f",(MARPER)) + "%"+"</font></b>"+" Margin on MRP";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AMOUNTtext.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
            MARPERtext.setText(Html.fromHtml(mtext,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            AMOUNTtext.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
            MARPERtext.setText(Html.fromHtml(mtext), TextView.BufferType.SPANNABLE);
        }

    //    Toast.makeText(this,"AMOUNT : "+TAtax,Toast.LENGTH_SHORT).show();
        return;
    }

    public void reset(View view) {
        PQtext.clearFocus();
        FQtext.clearFocus();
        MRPtext.clearFocus();
        AMOUNTtext.clearFocus();
        AMOUNTtext.setTextSize(16);
        GSTtext.clearFocus();
        RATEtext.clearFocus();
        MARPERtext.clearFocus();
        CDtext.clearFocus();
        PQtext.setText("");
        FQtext.setText("");
        MRPtext.setText("");
        AMOUNTtext.setText("Calculate profit margin per product and percentage margin on MRP");
        GSTtext.setText("");
        RATEtext.setText("");
        MARPERtext.setText("");
        CDtext.setText("");
        ids.clear();
        undoactions.clear();
        actions.clear();
        redoids.clear();
        AMOUNTtext.setTextColor(oldColors);
        MARPERtext.setTextColor(oldColors);
  //      AppUtil.hideKeyBoard(this);
    }

    public void redo(View view) {
        if(!redoids.isEmpty()){
            Integer tmp= (Integer) redoids.get(redoids.size()-1);
            EditText tmpview=findViewById(tmp);
            fromundo=true;
            tmpview.clearFocus();
            tmpview.requestFocus();
            String s= (String) actions.get(actions.size()-1);
            tmpview.setText(s);
            ids.add(tmp);
            undoactions.add(s);
            redoids.remove(redoids.size()-1);
            actions.remove(actions.size()-1);
        }else{
            Toast.makeText(this,"Cannot redo",Toast.LENGTH_SHORT).show();
        }
    }

    public void undo(View view) {
        if(!ids.isEmpty()) {
            Integer tmp = (Integer) ids.get(ids.size() - 1);
            EditText tmpview = findViewById(tmp);
            fromundo=true;
            tmpview.requestFocus();
            String redotxt = tmpview.getText().toString();
            redoids.add(tmp);
            actions.add(redotxt);
            if(undoactions.get(undoactions.size()-1)==null)
            tmpview.setText("");
            tmpview.setText(undoactions.get(undoactions.size()-1));
            ids.remove(ids.size() - 1);
            undoactions.remove(undoactions.size()-1);
        }else{
            Toast.makeText(this,"Cannot undo",Toast.LENGTH_SHORT).show();
        }

    }

    public void go(View view) {
        calc();
    }

    public abstract class TextValidator implements TextWatcher {
        private final TextView textView;

        public TextValidator(TextView textView) {
            this.textView = textView;
        }

        public abstract void validate(TextView textView, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = textView.getText().toString();
            validate(textView, text);
        }

        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
    }


}
