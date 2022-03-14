package com.listapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.listapp.Activity.SupplierSearchActivity;
import com.listapp.R;

public class SupplierHomeFragment extends Fragment implements View.OnClickListener {

    private static FrameLayout fragmentView;
    private static RelativeLayout parentView;
    private TextView textview1;
    private TextView textView2;
    private LinearLayout search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_suppliers, null);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentView = view.findViewById(R.id.fragmentView);
        parentView = view.findViewById(R.id.parentView);
        textview1 = view.findViewById(R.id.textview1);
        textView2 = view.findViewById(R.id.textView2);
        search = view.findViewById(R.id.search);

        SupplierSearchActivity.searchView.setVisibility(View.GONE);
        SupplierSearchActivity.heading.setVisibility(View.VISIBLE);
        view.findViewById(R.id.slogo).setVisibility(View.VISIBLE);
        view.findViewById(R.id.textview1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.textView2).setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        fragmentView.setVisibility(View.GONE);
        hideFragmentView();
        SupplierSearchActivity.heading.setVisibility(View.VISIBLE);
        SupplierSearchActivity.searchView.setVisibility(View.GONE);
        SupplierSearchActivity.backButton.setVisibility(View.GONE);
        SupplierSearchActivity.whichScreen = 1;
        SupplierSearchActivity.search.setText("");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.search) {
                    SupplierSearchActivity.heading.setVisibility(View.GONE);
                    showFragmentView();
                    getFragmentManager().beginTransaction().replace(R.id.fragmentView,new SupplierSearchFragment(),"SupplierSearchFragment").addToBackStack("SupplierSearchFragment").commit();
                }}});}



    public static void hideFragmentView()
    {
        fragmentView.setVisibility(View.GONE);
        parentView.setVisibility(View.VISIBLE);
        SupplierSearchActivity.heading.setVisibility(View.VISIBLE);
        SupplierSearchActivity.backButton.setVisibility(View.GONE);
        SupplierSearchActivity.searchView.setVisibility(View.GONE);
        SupplierSearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
        SupplierSearchActivity.search.setVisibility(View.VISIBLE);
    }

    private void showFragmentView()
    {
        fragmentView.setVisibility(View.VISIBLE);
        parentView.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.search:
                showFragmentView();
                break;
        }
    }

//    private void loadBanner() {
//        // Create an ad request. Check your logcat output for the hashed device ID
//        // to get test ads on a physical device, e.g.,
//        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
//        // device."
//        AdRequest adRequest =
//                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                        .build();
//
//        AdSize adSize = new AdSize(320,100);
//        // Step 4 - Set the adaptive ad size on the ad view.
//        adView.setAdSize(adSize);
//
//        // Step 5 - Start loading the ad in the background.
//        adView.loadAd(adRequest);
//    }

    public static void showKeyboard(Context context) {
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
