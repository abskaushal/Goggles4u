package com.ts.mobilelab.goggles4u.filter;

import java.util.ArrayList;

/**
 * Created by Abhishek on 01-Oct-16.
 */
public class FilterItem {
    private String mName;
    private ArrayList<String> mCategories;

    public String getFilterName() {
        return mName;
    }

    public ArrayList<String> getFilterCategories() {
        return mCategories;
    }

    public void setFilterName(String mName) {
        this.mName = mName;
    }

    public void setFilterCategories(ArrayList<String> mCategories) {
        this.mCategories = mCategories;
    }
}
