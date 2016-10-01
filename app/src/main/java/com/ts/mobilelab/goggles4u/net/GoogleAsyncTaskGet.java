package com.ts.mobilelab.goggles4u.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ts.mobilelab.goggles4u.HomeActivity;
import com.ts.mobilelab.goggles4u.TryOnActivity;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.net.parser.ParseJsonData;
import com.ts.mobilelab.goggles4u.net.utils.HttpConnectionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

/**
 * Created by PC2 on 18-03-2016.
 */
public class GoogleAsyncTaskGet extends AsyncTask<String,Void,String> {


    private Context mContext;
    public static Handler handler;
    public ProgressDialog mProgressDialog;

    private HttpConnectionHelper mHttpConnectionHelper;
    private int mCheckcode;

    public static Handler isFpwdHandler, isRegistrationHandler;
    JSONObject receiveJSon, sendJsonobj;
    ParseJsonData mParseJsonData;
    private String result = "";
    JSONArray recivejsonArray;
    private String skuid;

    public GoogleAsyncTaskGet(Context context, int activity_Code, String sku){
        this.mContext = context;
        this.mCheckcode = activity_Code;
        mHttpConnectionHelper = new HttpConnectionHelper(mContext);
        mProgressDialog = new ProgressDialog(mContext);
        skuid = sku;
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
    protected String doInBackground(String... params) {

        try {
            sendJsonobj = new JSONObject(params[0]);
            Log.v("sendJsonobj",""+sendJsonobj);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mHttpConnectionHelper.isNetworkAvailable()) {
            if(mCheckcode == AppConstants.CODE_FOR_TTRYON){
                // Log.v("sendJsonobj", ""+sendJsonobj);
                //receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.TRYON_URL+skuid, sendJsonobj);
                receiveJSon = mHttpConnectionHelper.getConnection("http://goggles4u.info/frames/111551.png",sendJsonobj);
                Log.v("receiveJSon", "in bg" + receiveJSon);
                if (receiveJSon != null) {
                    String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        if (mCheckcode == AppConstants.CODE_FOR_TTRYON) {

           // TryOnActivity.updateUi(result, receiveJSon);
        }
    }
}
