package com.listapp.Models;

/**
 * Created by syscraft on 7/31/2017.
 */

public class YearModel
{
    String year;
    boolean isSleted;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isSleted() {
        return isSleted;
    }

    public void setSleted(boolean sleted) {
        isSleted = sleted;
    }

    public YearModel(String year, boolean isSleted) {
        this.year = year;
        this.isSleted = isSleted;
    }


}
