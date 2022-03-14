package com.listapp.Fragment;

/**
 * Created by Nivesh on 6/22/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.listapp.API_Utility.Ratrofit_Implementation.APIClient;
import com.listapp.API_Utility.Ratrofit_Implementation.APIInterface;
import com.listapp.API_Utility.Ratrofit_Implementation.Model.banner.BannerModel;
import com.listapp.Activity.MedicineSearchActivity;
import com.listapp.Adapter.BannerAdapter;
import com.listapp.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MedicineHomeFragment extends Fragment {

    private static FrameLayout fragmentView;
    private static RelativeLayout parentView;
    private LinearLayout search;
//    private LinearLayout adContainerView;
//    private AdView adView;

    ///Banner impl
    private APIInterface apiInterface;
    ViewPager2 mViewPager;
    private BannerAdapter mBannerAdapter;
    RelativeLayout mBannerRL;
    DotsIndicator mIndicator;
    int delayMil = 5000;
    ProgressBar mProgressBar;
    final Handler sliderHandler = new Handler();
    final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (mBannerAdapter.getItemCount() - 1 == mViewPager.getCurrentItem()) {
                mViewPager.setCurrentItem(0);

            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        MedicineSearchActivity.heading.setText("HOME");
        Context context = getContext();
        fragmentView = view.findViewById(R.id.fragmentView);
        parentView = view.findViewById(R.id.parentView);
        search = view.findViewById(R.id.search);
        mViewPager = view.findViewById(R.id.viewPage22r);
        mBannerRL = view.findViewById(R.id.addLayout);
        mProgressBar = view.findViewById(R.id.progressBar);
        // dotItemLayout = view.findViewById(R.id.dotItemLayout);
        mIndicator = view.findViewById(R.id.dots_indicator);


//        MedicineSearchActivity.searchView.setVisibility(View.GONE);
//        MedicineSearchActivity.heading.setVisibility(View.VISIBLE);
//        view.findViewById(R.id.mlogo).setVisibility(View.VISIBLE);
//        view.findViewById(R.id.textview1).setVisibility(View.VISIBLE);
//        view.findViewById(R.id.textView2).setVisibility(View.VISIBLE);
//        search.setVisibility(View.VISIBLE);
//        fragmentView.setVisibility(View.GONE);
        hideFragmentView();
//        MedicineSearchActivity.heading.setVisibility(View.VISIBLE);
//        MedicineSearchActivity.searchView.setVisibility(View.GONE);
//        MedicineSearchActivity.backButton.setVisibility(View.GONE);
        MedicineSearchActivity.whichScreen = 1;
//        MedicineSearchActivity.search.setText("");
//        adContainerView = view.findViewById(R.id.ad_view_container);
//        // Step 1 - Create an AdView and set the ad unit ID on it.
//        adView = new AdView(this.getContext());
//        adView.setAdUnitId("ca-app-pub-7457423848084246/7709905183");
//        adContainerView.addView(adView);
//        loadBanner();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.search) {
                    MedicineSearchActivity.searchiconView.setVisibility(View.VISIBLE);
                    MedicineSearchActivity.backButton.setVisibility(View.VISIBLE);
                    MedicineSearchActivity.toolbarCloseIconView.setVisibility(View.GONE);
                    MedicineSearchActivity.heading.setVisibility(View.GONE);
                    MedicineSearchActivity.searchView.setVisibility(View.VISIBLE);
                    showFragmentView();
                    getFragmentManager().beginTransaction().replace(R.id.fragmentView, new MedicineSearchFragment(), "MedicineSearchFragment").addToBackStack("OrderSearchFragment").commit();
                    //setBannerAdapter();
                }
            }
        });

        setBannerAdapter();
    }

    private void showFragmentView() {
        fragmentView.setVisibility(View.VISIBLE);
        parentView.setVisibility(View.GONE);
    }

    public static void hideFragmentView() {
        parentView.setVisibility(View.VISIBLE);
        fragmentView.setVisibility(View.GONE);
    }


    public static void showKeyboard(Context context) {
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, delayMil);
    }


    void setBannerAdapter() {
        bannerApiCall();
//        //SnapHelper snapHelper = new PagerSnapHelper();
//        mBannerAdapter = new BannerAdapter(requireActivity());
//        mLayoutManager = new CenterZoomLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);


//        mBannerRY.setLayoutManager(mLayoutManager);
//        //  snapHelper.attachToRecyclerView(mBannerRY);
//        mBannerRY.setAdapter(mBannerAdapter);
//        mBannerAdapter.setAdapterData(mData);


//////////view pager

        mBannerAdapter = new BannerAdapter(requireActivity(), mViewPager);
        mViewPager.setAdapter(mBannerAdapter);
        mViewPager.setClipToPadding(false);
        mViewPager.setClipChildren(false);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        //  mIndicator.setViewPager2(mViewPager);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(15));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.9f + r * 0.15f);
            }
        });

        mViewPager.setPageTransformer(compositePageTransformer);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, delayMil); // slide duration 3 seconds
            }
        });

        mIndicator.setViewPager2(mViewPager);
    }


    void bannerApiCall() {

    Call<BannerModel> bannerCall = apiInterface.getBanner();
        bannerCall.enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                if (response.body() != null) {
                   // Log.i("bannerApi", "onResponse=> "+new Gson().toJson(response.body()));
                    BannerModel model = response.body();
                    mBannerAdapter.setAdapterData(model);
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                Log.i("bannerApi", "onFailure==> "+ t.getMessage());
                Toast.makeText(requireActivity(), "Server is busy ! Try Again", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

  /*  private void CallBannerApi(){
        ///banner is not showing simple get URL so we need to add random num in Api so don't remove
        final int min = 10;
        final int max = 1000;
        final int random = new Random().nextInt((max - min) + 1) + min;
        // RequestQueue queue = Volley.newRequestQueue(requireActivity(),new HurlStack(null, ClientSSLSocketFactory.getSocketFactory()));
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        String url = BASE_URL_BANNER.concat(String.valueOf(random));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if (response != null && !response.isEmpty()) {
                            mProgressBar.setVisibility(View.GONE);
                            try {
                                JSONObject object = new JSONObject(response);
                                // Toast.makeText(requireActivity(), object.toString(), Toast.LENGTH_SHORT).show();
                                Log.i("apicall-", "onResponse==> "+ object);
                                BannerModel model = new BannerModel();
                                ArrayList<BannerModel.Datum> strings = new ArrayList<>();
                                for (int i = 0; i < object.getJSONArray("data").length(); i++) {
                                    BannerModel.Datum datum = new BannerModel.Datum();
                                    datum.setImageUrl(object.getJSONArray("data").getJSONObject(i).getString("image_url"));
                                    datum.setLink(object.getJSONArray("data").getJSONObject(i).getString("link"));
                                    datum.setId(object.getJSONArray("data").getJSONObject(i).getString("id"));
                                    strings.add(datum);
                                }
                                //  model.setData(strings);
                                model.setData(strings);
                                mBannerAdapter.setAdapterData(model);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("apicall-","onErrorResponse===> " + error.getMessage());
                Toast.makeText(requireActivity(), "Server is busy ! Try Again", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }*/

}

