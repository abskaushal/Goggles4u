package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUs extends AppCompatActivity {

    private TextView contactUS;
    private Context mContext;
    private static ContactUs sInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        sInstance = this;
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        contactUS = (TextView) findViewById(R.id.tv_contactus);
        //contactUS.setText(Html.fromHtml("Mahesh"));
        init();


    }

    private void init() {
        JSONObject json = new JSONObject();
        try {
            //{"user_id":"10","addresstype":"checkout"}
            json.put("page","customer-service");
            //json.put("page","helpcenter");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_CONTACTUS);
        gogglesAsynctask.execute(json.toString());
    }

    public static void setUIData(String result, JSONObject receiveJSon) {

        if(result.equals(AppConstants.SUCCESSFUL)){
            try {
                String cntn = receiveJSon.getString("content");
                sInstance.contactUS.setText(Html.fromHtml(cntn));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
