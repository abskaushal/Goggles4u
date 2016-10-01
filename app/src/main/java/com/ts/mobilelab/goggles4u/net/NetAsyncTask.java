package com.ts.mobilelab.goggles4u.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ts.mobilelab.goggles4u.ProductListingActivity;
import com.ts.mobilelab.goggles4u.ProductListingNew;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.net.parser.ParseJsonData;
import com.ts.mobilelab.goggles4u.net.utils.HttpConnectionHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

/**
 * Created by PC2 on 09-06-2016.
 */
public class NetAsyncTask extends AsyncTask<String,Void,String> {

    private Context mContext;
    public static Handler handler;
    public ProgressDialog mProgressDialog;

    private HttpConnectionHelper mHttpConnectionHelper;
    private int mCheckcode;
    //private final WeakReference<MainActivity> mainActivityWeakRef = null;
    public static Handler isFpwdHandler, isRegistrationHandler;
    JSONObject receiveJSon, sendJsonobj;
    ParseJsonData mParseJsonData;
    private String result = "";
    private int pageFlag ;

    public NetAsyncTask(Context context,  int activity_Code,int pflag) {
        this.mContext = context;
        this.mCheckcode = activity_Code;
        mHttpConnectionHelper = new HttpConnectionHelper(mContext);
        mProgressDialog = new ProgressDialog(mContext);
        pageFlag = pflag;
        mParseJsonData = new ParseJsonData(mContext);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(pageFlag == 1){
            mProgressDialog.hide();
        }else{
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
          Log.v("mCheckcode",""+mCheckcode);
            if(mCheckcode == AppConstants.CODE_FOR_FILTERDATA_SUBMIT){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.PRODUCT_LISTING, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }

            } else if (mCheckcode == AppConstants.CODE_FOR_PRODUCTLISTING){
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.PRODUCT_LISTING, sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseProductListingData(receiveJSon, mContext, pageFlag);
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
                    String res =  mParseJsonData.parseProductListingData(receiveJSon, mContext,pageFlag);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }
        }else {
            result = AppConstants.NETWORK_NOT_AVAILABLE;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mCheckcode == AppConstants.CODE_FOR_PRODUCTLISTING) {
            ProductListingActivity.updateUi(result, receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_SORTOPTION_SUBMIT){
           // ProductListingNew.updateUi(result, receiveJSon);
           // ProductListingNew.updateSortData(result, receiveJSon,pageFlag);
            ProductListingActivity.updateSortData(result,receiveJSon);
        }else if(mCheckcode == AppConstants.CODE_FOR_FILTERDATA_SUBMIT){
            ////ProductListingNew.updateUi(result, receiveJSon);
            ProductListingActivity.updateFilterData(result, receiveJSon,pageFlag);
        }
    }

}
