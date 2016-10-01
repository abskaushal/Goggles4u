package com.ts.mobilelab.goggles4u.net.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ts.mobilelab.goggles4u.net.parser.ParseJsonData;

import org.json.JSONObject;

import java.util.logging.Handler;

/**
 * Created by PC2 on 16-04-2016.
 */
public class NWAsyncTask extends AsyncTask<String,Void,String>{

    private Context mContext;
    public static Handler handler;
    public ProgressDialog mProgressDialog;

    private HttpConnectionHelper mHttpConnectionHelper;
    private int mCheckcode;
    //private final WeakReference<MainActivity> mainActivityWeakRef = null;
    public static Handler isFpwdHandler, isRegistrationHandler;
    JSONObject receiveJSon,sendJsonobj;
    ParseJsonData mParseJsonData;
    private String result = "";

   public  NWAsyncTask(Context context, int activity_Code){
       this.mContext = context;
       this.mCheckcode = activity_Code;
       mHttpConnectionHelper = new HttpConnectionHelper(mContext);
       mProgressDialog = new ProgressDialog(mContext);
       mParseJsonData = new ParseJsonData(mContext);
    }
    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        super.onPreExecute();

    }


    @Override
    protected String doInBackground(String... params) {

        Log.v("in async", "doInBackground" + params);
        return null;
    }
}
