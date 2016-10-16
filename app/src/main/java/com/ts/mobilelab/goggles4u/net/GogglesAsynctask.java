package com.ts.mobilelab.goggles4u.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ts.mobilelab.goggles4u.ChangePasswordActivity;
import com.ts.mobilelab.goggles4u.CheckoutActivity;
import com.ts.mobilelab.goggles4u.ContactUs;
import com.ts.mobilelab.goggles4u.EditAddress;
import com.ts.mobilelab.goggles4u.EditInformation;
import com.ts.mobilelab.goggles4u.FavListActivity;
import com.ts.mobilelab.goggles4u.FilterActivity;
import com.ts.mobilelab.goggles4u.ForGotPswdActivity;
import com.ts.mobilelab.goggles4u.HelpCenterActivity;
import com.ts.mobilelab.goggles4u.HelpRowActivity;
import com.ts.mobilelab.goggles4u.HomeActivity;
import com.ts.mobilelab.goggles4u.Login;
import com.ts.mobilelab.goggles4u.MyAccountActivity;
import com.ts.mobilelab.goggles4u.MyOrderActivity;

import com.ts.mobilelab.goggles4u.NewFilterActivity;
import com.ts.mobilelab.goggles4u.OrderReviewActivity;
import com.ts.mobilelab.goggles4u.Payment;
import com.ts.mobilelab.goggles4u.PaypalActivity;
import com.ts.mobilelab.goggles4u.PrescriptionAddActivity;
import com.ts.mobilelab.goggles4u.PrescriptionNextActivity;
import com.ts.mobilelab.goggles4u.PrescriptionView;

import com.ts.mobilelab.goggles4u.PrescriptionsActivity;
import com.ts.mobilelab.goggles4u.ProductDetailsActivity;
import com.ts.mobilelab.goggles4u.ProductListingActivity;
import com.ts.mobilelab.goggles4u.RegistrationActivity;
import com.ts.mobilelab.goggles4u.SelectAddressActivity;
import com.ts.mobilelab.goggles4u.ShippingActivity;
import com.ts.mobilelab.goggles4u.ShoppingCartActivity;
import com.ts.mobilelab.goggles4u.SortingActivity;
import com.ts.mobilelab.goggles4u.StoreCreditDataActivity;
import com.ts.mobilelab.goggles4u.ViewOrderActivity;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.net.parser.ParseJsonData;
import com.ts.mobilelab.goggles4u.net.utils.HttpConnectionHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mahesh on 16-03-2016.
 */
public class GogglesAsynctask extends AsyncTask<String, Void, String> {

    private Context mContext;

    public ProgressDialog mProgressDialog;

    private HttpConnectionHelper mHttpConnectionHelper;
    private int mCheckcode;

    private JSONObject receiveJSon, sendJsonobj;
    private ParseJsonData mParseJsonData;
    private String result = "";
int pageFlag = 0;


    public GogglesAsynctask(Context context, int activity_Code) {
        this.mContext = context;
        this.mCheckcode = activity_Code;
        mHttpConnectionHelper = new HttpConnectionHelper(mContext);
        mProgressDialog = new ProgressDialog(mContext);

        mParseJsonData = new ParseJsonData(mContext);
    }

    @Override
    protected void onPreExecute() {

      //  mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onPreExecute();
        if(mCheckcode == AppConstants.CODE_FOR_CATEGORY) {

            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Loading..");

            mProgressDialog.setCancelable(false);
            mProgressDialog.cancel();
            mProgressDialog.dismiss();
        }else{
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Loading..");

            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        Log.v("in async", "doInBackground" + params);
        if (params.length !=0 && params[0] != null && !params[0].isEmpty()) {

            try {
                sendJsonobj = new JSONObject(params[0]);
                Log.v("sendJsonobj",""+sendJsonobj);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (mHttpConnectionHelper.isNetworkAvailable()) {

            if (mCheckcode == AppConstants.CODE_FOR_REGISTRATION) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.USERREGISER_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseRegistrationData(receiveJSon, mContext);

                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_CATEGORY) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CATEGORYLOAD_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parsecategoryData(receiveJSon, mContext);

                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }


            else if (mCheckcode == AppConstants.CODE_FOR_FORGTPSWD) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.FORGETPSWD_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseaddressData(receiveJSon, mContext);

                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_CHANGEPSWD) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CHANGEPSWD_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    try {
                        String dd = receiveJSon.getString("Error");
                        Log.v("dd", "" + dd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_LOGIN) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.USERLOGIN_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseloginData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_ADDRESSLIST) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.ADDRESSLIST_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_ADDADRESS) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.ADDADDRESS_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_EDITADRESS) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.EDITADDRESS_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_DELETEADRESS) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.DELETEADDRESS_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_PRESCRIPTIONDATA) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.PRESCRIPTIONlIST_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parsePrescriptionData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }

            } else if (mCheckcode == AppConstants.CODE_FOR_VIEWPRESCRIPTION) {

                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.VIEWPRESCRIPTIONS_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_EDITPRESCRIPTION) {

                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.EDITPRESCRIPTION_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_SAVEPRESCRIPTION) {

                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.SAVEPRESCRIPTION_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }

            else if (mCheckcode == AppConstants.CODE_FOR_DELETEPRESCRIPTION) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.DELETEPRESCRIPTION_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_MYORDER) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.MYORDER_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_MYORDERROW) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.MYORDERROW_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_STORECREDIT) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.STORECREDIT_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCheckcode == AppConstants.CODE_FOR_EDITINFO) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.EDITINFO_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_PRODUCTLISTING){
                Log.v("sendJsonobj", "in11"+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.PRODUCT_LISTING, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseProductListingData(receiveJSon, mContext, 0);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_FILTERDATA){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CATEGORY_SUBMIT_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_SORTOPTION){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.GETSORTOPTION_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_SORTOPTION_SUBMIT){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.PRODUCT_LISTING, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    //String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    String res =  mParseJsonData.parseProductListingData(receiveJSon, mContext,1);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_PRODUCTDETAILS){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.PRODUCT_DETAILS, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_LENSE_DETAILS){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.LENSE_DETAILS_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseLenseData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_FILTERDATA_SUBMIT){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.PRODUCT_LISTING, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_GETPRESCRIPTION_LIST){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.GETPRESCRIPTION_LIST_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_PRESCRIPTION_OPTION){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.LENSE_DETAILS_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_CATEGORY){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CATEGORY_SUBMIT_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_LENSEOPTIONS){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.LENSE_OPTIONS_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_ADDTOCART){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.ADDTOCART_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseCartData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_CARTDETAILS){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CART_DETAILS_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseCartDetailsData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_CARTUPDATE){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CART_UPDATE_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_CART_REMOVE){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CART_REMOVE_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_APPLYCOUPON){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CART_COUPON_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_CARTCLEAR){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CART_CLEAR_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_LOADLENSOPTIONDATA){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.LOADLENSEOPION_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_LOAD_ADDRESSLIST) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.ADDRESSLIST_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_LOAD_REGIONLIST) {
                Log.v("CODE_FOR_LOAD_regiom","");
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.LOAD_REGIONLIST_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }
            else if (mCheckcode == AppConstants.CODE_FOR_LOAD_COUNTRYLIST) {
                Log.v("CODE_FOR_LOAD_COUNTRYLIST","");
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.LOAD_COUNTRYLIST_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_LOAD_ADDRESS) {
                Log.v("CODE_FOR_LOAD_COUNTRYLIST","");
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.ADDRESSLIST_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_CHECKOUT) {
                //Log.v("CODE_FOR_LOAD_COUNTRYLIST","");
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CHECKOUT_URL, sendJsonobj);
                //Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_CARTREVIEW){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CART_DETAILS_URL, sendJsonobj);
                //Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseCartDetailsData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if(mCheckcode == AppConstants.CODE_FOR_SAVEORDER){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.SAVEORDER_URL, sendJsonobj);
                //Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_LOAD_SHIPINGADRS) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.ADDRESSLIST_URL, sendJsonobj);
                //Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_CHECKOUTSHIPADRS) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.SAVESHIPPING_URL, sendJsonobj);
               // Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_ORDERSUCCESS) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.ORDERSUCCESS_URL, sendJsonobj);
               // Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_CONTACTUS) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CONTACTUS_URL, sendJsonobj);
                //Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_HELPCENTER) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.HELPCENTER_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_MARKTOFAVRITE) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.MARKTOFAV_URL, sendJsonobj);
                //Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_UNMARKTOFAVRITE) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.UNMARKTOFAV_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_FAVRITELIST) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.FAVLIST_URL, sendJsonobj);
                //Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_DELETEFAVITEM) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.UNMARKTOFAV_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_MARKTOFAVRITE_PRODCTDETAILS) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.MARKTOFAV_URL, sendJsonobj);
                //Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_UNMARKTOFAVRITE_PRODCTDETAILS) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.UNMARKTOFAV_URL, sendJsonobj);
               // Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_HELPROW) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CONTACTUS_URL, sendJsonobj);
               // Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }else if (mCheckcode == AppConstants.CODE_FOR_STORECREDIT_UPDATE) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.STORECREDIT_UPDATE_URL, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }





            else if (mCheckcode == AppConstants.CODE_FOR_HOME) {
                Log.v("CODE_FOR_HOME", "");
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.HOME_DATA_URL, null);
                if (receiveJSon != null) {

                    String res = mParseJsonData.parseHomeData(receiveJSon, mContext);
                   // Log.v("receiveJSon", "home" + receiveJSon);

                    result = res;
                }else{
                    result = AppConstants.UNABLE_TO_PROCESS;;
                }
            } else {
                result = AppConstants.UNABLE_TO_PROCESS;
            }



        } else {
            result = AppConstants.NETWORK_NOT_AVAILABLE;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        try {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mCheckcode == AppConstants.CODE_FOR_HOME) {

            HomeActivity.updateUi(result,receiveJSon);
        } else if (mCheckcode == AppConstants.CODE_FOR_REGISTRATION) {
            RegistrationActivity.updateUi(result);
        } else if (mCheckcode == AppConstants.CODE_FOR_LOGIN) {
           // Login.updateUi(result);
        } else if (mCheckcode == AppConstants.CODE_FOR_FORGTPSWD) {
            ForGotPswdActivity.updateUi(result,receiveJSon);
        } else if (mCheckcode == AppConstants.CODE_FOR_CHANGEPSWD) {
            ChangePasswordActivity.updateUi(result);
        } else if (mCheckcode == AppConstants.CODE_FOR_ADDADRESS) {
            EditAddress.updateUi(result);
        } else if (mCheckcode == AppConstants.CODE_FOR_PRESCRIPTIONDATA) {
            PrescriptionsActivity.updateUi(result);
        } else if (mCheckcode == AppConstants.CODE_FOR_VIEWPRESCRIPTION) {
            PrescriptionView.updateUi(result, receiveJSon);
        } else if (mCheckcode == AppConstants.CODE_FOR_DELETEPRESCRIPTION) {
            PrescriptionsActivity.updateUidelete(result, sendJsonobj);
        }  else if (mCheckcode == AppConstants.CODE_FOR_EDITPRESCRIPTION) {
            PrescriptionsActivity.updateUiEdit(result, receiveJSon);
        }   else if (mCheckcode == AppConstants.CODE_FOR_ADDRESSLIST) {
            MyAccountActivity.updateUi(result, receiveJSon);
        } else if (mCheckcode == AppConstants.CODE_FOR_EDITADRESS) {
            EditAddress.updateEditadrsUi(result);
        } else if (mCheckcode == AppConstants.CODE_FOR_DELETEADRESS) {
            MyAccountActivity.updateDeleteadrsUi(result);
        } else if (mCheckcode == AppConstants.CODE_FOR_MYORDER) {
            MyOrderActivity.updateUi(result, receiveJSon);
        } else if (mCheckcode == AppConstants.CODE_FOR_MYORDERROW) {
            ViewOrderActivity.updateUi(result, receiveJSon);
        } else if (mCheckcode == AppConstants.CODE_FOR_STORECREDIT) {
            StoreCreditDataActivity.updateUi(result, receiveJSon);
        } else if (mCheckcode == AppConstants.CODE_FOR_EDITINFO) {
            EditInformation.updateUi(result,sendJsonobj);
        }else if (mCheckcode == AppConstants.CODE_FOR_PRODUCTLISTING) {
            ProductListingActivity.updateUi(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_FILTERDATA){
          //  NewFilterActivity.updateUi(result, receiveJSon);
           // FilterActivity.parseJson(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_SORTOPTION){
            SortingActivity.updateSortDataUi(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_SORTOPTION_SUBMIT){
            ProductListingActivity.updateSortData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_PRODUCTDETAILS){
            ProductDetailsActivity.updateUi(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_LENSE_DETAILS){
            PrescriptionAddActivity.updateProductOptionUi(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_FILTERDATA_SUBMIT){
            ProductListingActivity.updateFilterData(result, receiveJSon, pageFlag);
        }else if(mCheckcode == AppConstants.CODE_FOR_GETPRESCRIPTION_LIST){
            PrescriptionAddActivity.updatePrescriptionListData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_SAVEPRESCRIPTION){
            PrescriptionAddActivity.savePrescriptionData(result);
        }else if(mCheckcode == AppConstants.CODE_FOR_PRESCRIPTION_OPTION){
            PrescriptionAddActivity.updateProductOptionUi(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_CATEGORY){
            HomeActivity.updateCategoryData(result);
        }else if(mCheckcode == AppConstants.CODE_FOR_CATEGORYSELECTION){
            HomeActivity.updateCategoryData(result);
        }else if(mCheckcode == AppConstants.CODE_FOR_LENSEOPTIONS){
            PrescriptionAddActivity.updateLensData(result,receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_ADDTOCART){
            PrescriptionNextActivity.updateData(result);
        }else if(mCheckcode == AppConstants.CODE_FOR_CARTDETAILS){
            ShoppingCartActivity.updateData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_CARTUPDATE){
            ShoppingCartActivity.updateCartData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_CART_REMOVE){
            ShoppingCartActivity.updateCartData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_APPLYCOUPON){
            ShoppingCartActivity.updateCartData(result, sendJsonobj);
        }else if(mCheckcode == AppConstants.CODE_FOR_CARTCLEAR){
            ShoppingCartActivity.clearCartData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_LOADLENSOPTIONDATA){
            PrescriptionNextActivity.setARCUpdateData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_LOAD_ADDRESSLIST){
            CheckoutActivity.setAdrsData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_LOAD_COUNTRYLIST){
            EditAddress.setCountryData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_LOAD_ADDRESS){
            SelectAddressActivity.setData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_LOAD_REGIONLIST){
            EditAddress.setRegionData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_CHECKOUT){
            CheckoutActivity.setData(result, receiveJSon, sendJsonobj);
        }else if(mCheckcode == AppConstants.CODE_FOR_CARTREVIEW){
            OrderReviewActivity.setUIData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_SAVEORDER){
            Payment.setUIData(result, receiveJSon,sendJsonobj);
        }else if(mCheckcode == AppConstants.CODE_FOR_LOAD_SHIPINGADRS){
            ShippingActivity.setAdrsData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_CHECKOUTSHIPADRS){
            ShippingActivity.setUIData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_ORDERSUCCESS){
            PaypalActivity.setUIData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_CONTACTUS){
            ContactUs.setUIData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_HELPCENTER){
            HelpCenterActivity.setUIData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_MARKTOFAVRITE){
            ProductListingActivity.updateMarkedData(result, receiveJSon, sendJsonobj);
        }else if(mCheckcode == AppConstants.CODE_FOR_UNMARKTOFAVRITE){
            ProductListingActivity.updateMarkedData(result, receiveJSon,sendJsonobj);
        }else if(mCheckcode == AppConstants.CODE_FOR_FAVRITELIST){
            FavListActivity.updateUiData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_DELETEFAVITEM){
            FavListActivity.updateDeleteData(result, receiveJSon, sendJsonobj);
        }else if(mCheckcode == AppConstants.CODE_FOR_MARKTOFAVRITE_PRODCTDETAILS){
            ProductDetailsActivity.updateMarkedData(result, receiveJSon, sendJsonobj);
        }else if(mCheckcode == AppConstants.CODE_FOR_UNMARKTOFAVRITE_PRODCTDETAILS){
            ProductDetailsActivity.updateMarkedData(result, receiveJSon,sendJsonobj);
        }else if(mCheckcode == AppConstants.CODE_FOR_HELPROW){
            HelpRowActivity.updateData(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_STORECREDIT_UPDATE){
            Payment.updateData(result,sendJsonobj, receiveJSon);
        }


        super.onPostExecute(result);
    }

}
