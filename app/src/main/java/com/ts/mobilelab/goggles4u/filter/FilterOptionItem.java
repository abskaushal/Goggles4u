package com.ts.mobilelab.goggles4u.filter;

/**
 * Created by Mohit on 02-10-2016.
 */
public class FilterOptionItem {
    private  String mOptionName;
    private  String mOptionValue;
    private boolean mSelected;

    public FilterOptionItem(String  name, String value, boolean isSelected) {
        this.mOptionName = name;
        this.mOptionValue = value;
        this.mSelected = isSelected;
    }

    public String getOptionName() { return mOptionName; }

    public boolean isSelected() { return mSelected; }

    public String getOptionValue() { return mOptionValue; }

    public void setOptionName(String mOptionName) { this.mOptionName = mOptionName; }

    public void setOptionValue(String mOptionValue) { this.mOptionValue = mOptionValue; }

    public void setSelected(boolean mSelected) { this.mSelected = mSelected; }
}
