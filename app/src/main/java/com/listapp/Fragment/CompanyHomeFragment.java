package com.listapp.Fragment;

/**
 * Created by syscraft on 7/1/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.listapp.Activity.CompanySearchActivity;
import com.listapp.R;

public class CompanyHomeFragment extends Fragment {

    public static FrameLayout fragmentView;
    public static RelativeLayout parentView;
    private LinearLayout search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_company, null);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentView = view.findViewById(R.id.fragmentView);
        parentView = view.findViewById(R.id.parentView);
        search = view.findViewById(R.id.search);
        CompanySearchActivity.heading.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        fragmentView.setVisibility(View.GONE);
        CompanySearchActivity.whichScreen = 1;
        CompanySearchActivity.search.setText("");


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.search) {
                    CompanySearchActivity.searchView.setVisibility(View.VISIBLE);
                    CompanySearchActivity.backButton.setVisibility(View.VISIBLE);
                    showFragmentView();
                    CompanySearchActivity.heading.setVisibility(View.GONE);
//                    view.findViewById(R.id.clogo).setVisibility(View.GONE);
//                    view.findViewById(R.id.textview1).setVisibility(View.GONE);
//                    view.findViewById(R.id.textView2).setVisibility(View.GONE);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentView,new CompanySearchFragment(),"CompanySearchFragment").addToBackStack("CompanySearchFragment").commit();
                }}});

    }

    public static void hideFragment() {
        fragmentView.setVisibility(View.GONE);
        parentView.setVisibility(View.VISIBLE);

    }

    private void showFragmentView() {
        fragmentView.setVisibility(View.VISIBLE);
        parentView.setVisibility(View.GONE);
    }
    public static void showKeyboard(Context context) {
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
