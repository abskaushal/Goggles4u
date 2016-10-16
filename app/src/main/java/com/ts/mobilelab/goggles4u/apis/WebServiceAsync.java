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
 * Common AsyncTAsk class for calling different webservices.
 *
 * Created by Abhishek on 15-Oct-16.
 */
public class WebServiceAsync extends AsyncTask<String, Void, Void> {

    private Context mContext;
    private IWebService mCallback;
    private int mCode;
    public ProgressDialog mProgressDialog;
    private HttpConnectionHelper mHttpConnectionHelper;
    private JSONObject receiveJSon, sendJsonobj;
    private ParseJsonData mParseJsonData;
    private String result = "";


    public WebServiceAsync(Context context, IWebService callback, int code) {
        if (context == null || callback == null) {
            throw new IllegalArgumentException("Null args passed");
        }
        mContext = context;
        mCallback = callback;
        mCode = code;
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
        mProgressDialog.show();
    }


    @Override
    protected Void doInBackground(String... params) {
        if (params.length != 0 && params[0] != null && !params[0].isEmpty()) {
            try {
                sendJsonobj = new JSONObject(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (mHttpConnectionHelper.isNetworkAvailable()) {

            if (mCode == AppConstants.CODE_FOR_LOGIN) {
                receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.USERLOGIN_URL, sendJsonobj);
                Log.v("receiveJSon", "" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseloginData(receiveJSon, mContext);
                    result = res;
                } else {
                    result = AppConstants.UNABLE_TO_PROCESS;
                }
            }
        } else {
            result = AppConstants.NETWORK_NOT_AVAILABLE;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
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
        mCallback.onDataReceived(data);
    }


}
