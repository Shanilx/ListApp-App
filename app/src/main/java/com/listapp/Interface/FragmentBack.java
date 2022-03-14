package com.listapp.Interface;

/**
 * Created by Nivesh on 6/23/2017.
 */

public interface FragmentBack {

    int VERY_LOW_BACK_PRIORITY = -2000;

    int LOW_BACK_PRIORITY = -1000;

    int NORMAL_BACK_PRIORITY = 0;

    int HIGH_BACK_PRIORITY = 1000;

    int VERY_HIGH_BACK_PRIORITY = 2000;

    boolean onBackPressed();

    int getBackPriority();

}
