package com.ts.mobilelab.goggles4u.apis;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.net.parser.ParseJsonData;
import com.ts.mobilelab.goggles4u.net.utils.HttpConnectionHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abhishek on 27-Oct-16.
 */
public class ProductListAsync extends AsyncTask<String, Void, String> {

    private Context mContext;
    private IWebService mCallback;
    private int mCode;
    public ProgressDialog mProgressDialog;
    private HttpConnectionHelper mHttpConnectionHelper;
    private JSONObject receiveJSon, sendJsonobj;
    private ParseJsonData mParseJsonData;
    private String result = "";
    private int mFlag;

    public ProductListAsync(Context context, IWebService callback, int code, int flag) {

        if (context == null || callback == null) {
            throw new IllegalArgumentException("Null args passed");
        }
        mContext = context;
        mCallback = callback;
        mCode = code;
        mFlag = flag;
        mHttpConnectionHelper = new HttpConnectionHelper(mContext);
        mProgressDialog = new ProgressDialog(mContext);
        mParseJsonData = new ParseJsonData(mContext);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Loading..");
        mProgressDialog.setCancelable(false);
        if (mFlag == 0)
            mProgressDialog.show();
    }


    @Override
    protected String doInBackground(String... params) {
        if (params.length != 0 && params[0] != null && !params[0].isEmpty()) {
            try {
                sendJsonobj = new JSONObject(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (mHttpConnectionHelper.isNetworkAvailable()) {
            if (mCode == AppConstants.CODE_FOR_PRODUCTLISTING) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.PRODUCT_LISTING, sendJsonobj);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseProductListingData(receiveJSon, mContext, mFlag);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCode == AppConstants.CODE_FOR_SORTOPTION_SUBMIT) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.PRODUCT_LISTING, sendJsonobj);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseProductListingData(receiveJSon, mContext, mFlag);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCode == AppConstants.CODE_FOR_FILTERDATA_SUBMIT) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.PRODUCT_LISTING, sendJsonobj);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }

            } else if (mCode == AppConstants.CODE_FOR_MARKTOFAVRITE) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.MARKTOFAV_URL, sendJsonobj);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            } else if (mCode == AppConstants.CODE_FOR_UNMARKTOFAVRITE) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.UNMARKTOFAV_URL, sendJsonobj);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }
        } else {
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
            e.printStackTrace();
        }

        // common wrapper class for all the different data. Clients will extract the required data
        // on their own.
        WebData data = new WebData();
        data.setResult(result);
        data.setSendJson(sendJsonobj);
        data.setReceiveJson(receiveJSon);
        data.setCode(mCode);
        mCallback.onDataReceived(data);
    }
}
