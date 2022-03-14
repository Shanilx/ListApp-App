package com.listapp.Interface;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Nivesh on 6/23/2017.
 */
public class BackFragmentHelper {

    private BackFragmentHelper() {
    }

    public static boolean fireOnBackPressedEvent(FragmentActivity gActivity) {
        List<Fragment> fragmentList = getAllActivityFragments(gActivity);
        return fireOnBackPressedEvent(fragmentList);
    }

    private static List<Fragment> getAllActivityFragments(FragmentActivity gActivity) {
        List<Fragment> fragmentList = gActivity.getSupportFragmentManager().getFragments();

        if(fragmentList != null && fragmentList.size() > 0) {
            List<Fragment> result = new ArrayList<>(fragmentList.size());

            for (Fragment f : fragmentList) {
                if(f != null) {
                    result.add(f);

                    List<Fragment> nestedFragmentList = f.getChildFragmentManager().getFragments();
                    if (nestedFragmentList != null && nestedFragmentList.size() > 0) {
                        result.addAll(nestedFragmentList);
                    }
                }
            }

            return result;
        } else {
            return new ArrayList<>(0);
        }
    }

    private static boolean fireOnBackPressedEvent(List<?> gFragmentList) {

        // find all fragments with back support
        List<FragmentBack> backFragmentList = new ArrayList<>(gFragmentList.size());
        for(Object f : gFragmentList) {
            if(f instanceof FragmentBack) {
                backFragmentList.add((FragmentBack)f);
            }
        }

        // sort fragments by back priority
        Collections.sort(backFragmentList, new Comparator<FragmentBack>() {
            @Override
            public int compare(FragmentBack lhs, FragmentBack rhs) {
                return rhs.getBackPriority() - lhs.getBackPriority();
            }
        });

        // send them onBackPressed event
        boolean handled = false;
        for (FragmentBack f: backFragmentList) {
            handled = f.onBackPressed();

            if(handled) {
                break;
            }
        }

        return handled;
    }
}