package com.ts.mobilelab.goggles4u.filter;

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
 * Created by Abhishek on 07-Oct-16.
 */
public class GetFilterAsync extends AsyncTask<String, Void, Void> {

    private Context mContext;
    public ProgressDialog mProgressDialog;
    private HttpConnectionHelper mHttpConnectionHelper;
    private JSONObject receiveJSon, sendJsonobj;
    private ParseJsonData mParseJsonData;
    private String result = "";
    private IFilterData mCallback;

    public GetFilterAsync(Context context, IFilterData callback){
        mContext = context;
        mCallback = callback;
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
            if (params.length !=0 && params[0] != null && !params[0].isEmpty()) {

                try {
                    sendJsonobj = new JSONObject(params[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            receiveJSon = mHttpConnectionHelper.getConnection(AppConstants.CATEGORY_SUBMIT_URL, sendJsonobj);
            Log.v("receiveJSon", "in bg" + receiveJSon);
            if (receiveJSon != null) {
                String res = mParseJsonData.parseJSONData(receiveJSon, mContext);
                result = res;
            } else {
                result = AppConstants.UNABLE_TO_PROCESS;
            }
        return null;
    }

    @Override
    protected void onPostExecute(Void Void) {
        super.onPostExecute(Void);
        try {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCallback.onFilterDataReceived(result, receiveJSon);
    }


}
