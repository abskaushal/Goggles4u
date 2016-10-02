package com.ts.mobilelab.goggles4u.filter;

import java.util.ArrayList;

/**
 * Created by Abhishek on 01-Oct-16.
 */
public class FilterItem {
    private String mName;
    private String mCode;
    private String mAttributeId;
    private String mInputType;
    private ArrayList<FilterOptionItem> mCategories;

    public String getFilterName() {
        return mName;
    }

    public ArrayList<FilterOptionItem> getFilterCategories() { return mCategories; }

    public String getmCode() { return mCode; }

    public String getmAttributeId() { return mAttributeId; }

    public String getmInputType() { return mInputType; }

    public void setFilterName(String mName) {
        this.mName = mName;
    }

    public void setFilterCategories(ArrayList<FilterOptionItem> mCategories) {
        this.mCategories = mCategories;
    }

    public void setCode(String mCode) { this.mCode = mCode; }

    public void setAttributeId(String mAttributeId) { this.mAttributeId = mAttributeId; }

    public void setInputType(String mInputType) { this.mInputType = mInputType; }
}
