package com.listapp.Fragment;

/**
 * Created by Nivesh on 6/22/2017.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.listapp.Activity.CitySearchActivity;
import com.listapp.R;

public class CityHomeFragment extends Fragment{

    private static FrameLayout fragmentView;
    private static RelativeLayout parentView;
    private LinearLayout search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CitySearchActivity.heading.setText("Select City");

        fragmentView = view.findViewById(R.id.fragmentView);
        parentView = view.findViewById(R.id.parentView);
        search = view.findViewById(R.id.search);


                hideParentView();
                getFragmentManager().beginTransaction().replace(R.id.fragmentView,new CitySearchFragment(),"").addToBackStack("").commit();

    }

    public static void hideFragmentView()
    {
        parentView.setVisibility(View.VISIBLE);
        fragmentView.setVisibility(View.GONE);
        CitySearchActivity.search.setText("");
        CitySearchActivity.heading.setVisibility(View.VISIBLE);
        CitySearchActivity.searchView.setVisibility(View.GONE);
        CitySearchActivity.backButton.setVisibility(View.GONE);
    }

    private void hideParentView()
    {
        parentView.setVisibility(View.GONE);
        fragmentView.setVisibility(View.VISIBLE);
    }
}

