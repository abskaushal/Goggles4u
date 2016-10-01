package com.ts.mobilelab.goggles4u.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ts.mobilelab.goggles4u.core.GogglesManager;

/**
 * Created by PC2 on 16-03-2016.
 */


public class PreferenceData {

    public PreferenceData() {
        mgugglesManager = GogglesManager.getInstance();
        mContext = GogglesManager.getAppContext();
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();
    }


    public boolean isLogincheck() {

        return mSharedPreferences.getBoolean(Login_key, false);
    }

    public void setLogincheck(boolean logincheck) {
        mEditor.putBoolean(Login_key, logincheck).commit();
    }

    // should be in private mode
   // private boolean Logincheck = false;
    public String Login_key = "login";
    private Context mContext;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    private GogglesManager mgugglesManager;
    private String CustomerFName = "fname";
    private String CustomerLName = "lname";
    private String CustomerMailId = "emailid";
    private String CategoryData = "ctgdata";



    private String SELECTED_SORT_OPTION = "sortopt";
    private String DEVICE_ID = "";


    private String CategorycacheFlag = "ctgflag";

    private String BillingAdrss = "bilingadrs";
    private String ShippingAdrs = "shipadrs";


    private String CartItemCount ="size";
    private String CartQuoteID = "quoteid";

    public String getCustomerLName() {
        return mSharedPreferences.getString(CustomerLName,"");
    }

    public void setCustomerLName(String lname) {

        mEditor.putString(CustomerLName,lname).commit();
    }

    public String getCustomerFName() {

        return mSharedPreferences.getString(CustomerFName,"N/A");
    }

    public void setCustomerFName(String fname) {
        mEditor.putString(CustomerFName,fname).commit();
    }

    public String getCustomerMailId() {
        return mSharedPreferences.getString(CustomerMailId,"N/A");
    }

    public void setCustomerMailId(String mailId) {
        mEditor.putString(CustomerMailId,mailId).commit();
    }

    public String getCartQuoteID() {
        return mSharedPreferences.getString(CartQuoteID,"");
    }

    public void setCartQuoteID(String cartQuoteID) {

        mEditor.putString(CartQuoteID,cartQuoteID).commit();
    }

    public String getCartItemCount() {
        return mSharedPreferences.getString(CartItemCount,"0");
    }

    public void setCartItemCount(String cartItemCount) {
       mEditor.putString(CartItemCount,cartItemCount).commit();

    }
    public String getBillingAdrss() {
        return mSharedPreferences.getString(BillingAdrss,"");
    }

    public String getSELECTED_SORT_OPTION() {
        return mSharedPreferences.getString(SELECTED_SORT_OPTION,"");
    }

    public void setSELECTED_SORT_OPTION(String slctoptn) {
        mEditor.putString(SELECTED_SORT_OPTION,slctoptn).commit();

    }

    public void setBillingAdrss(String billingAdrss) {
        mEditor.putString(BillingAdrss,billingAdrss).commit();
    }

    public String getDEVICE_ID() {
        return  mSharedPreferences.getString(DEVICE_ID,"");
    }

    public void setDEVICE_ID(String device_ID) {
        mEditor.putString(DEVICE_ID,device_ID).commit();

    }

    public String getShippingAdrs() {
        return mSharedPreferences.getString(ShippingAdrs,"");
    }

    public void setShippingAdrs(String shippingAdrs) {
        mEditor.putString(ShippingAdrs,shippingAdrs).commit();
    }



    public String getCustomerId() {
        return mSharedPreferences.getString(CustomerId,"");
    }

    public void setCustomerId(String customerId) {
        mEditor.putString(CustomerId,customerId).commit();
    }

    private String CustomerId = "userid";

    public String getCategoryData() {
        return mSharedPreferences.getString(CategoryData,"");
    }

    public void setCategoryData(String categoryData) {
        mEditor.putString(CategoryData,categoryData).commit();

    }



    public String getCategorycacheFlag() {
        return mSharedPreferences.getString(CategorycacheFlag,"");

    }

    public void setCategorycacheFlag(String categorycacheFlag) {
       mEditor.putString(CategorycacheFlag,categorycacheFlag).commit();
    }


    /*public String getLogincheck() {
        return mSharedPreferences.getString(Logincheck,"");
    }

    public void setLogincheck(String login) {
        mEditor.putString(Logincheck,login).commit();
    }*/
}
