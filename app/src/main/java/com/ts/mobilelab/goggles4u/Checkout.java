package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Checkout extends AppCompatActivity {

    ListView addrsList;
    Button addAdrs, continueBtn;

    private Context mContext;
    private static Checkout sInstance;
    private PreferenceData mPreferenceData;
    RadioGroup rgAdrs;
    LinearLayout ltadrs;
    int selctadrspos = 0;
    RadioButton radioButton;
    RadioGroup.LayoutParams rprms;
    JSONArray adrsjsonary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        // addrsList = (ListView) findViewById(R.id.lisview_adrs);
        addAdrs = (Button) findViewById(R.id.btn_addadrs);
        rgAdrs = (RadioGroup) findViewById(R.id.rg_adrs);
        ltadrs = (LinearLayout) findViewById(R.id.linear_adrs);
        continueBtn = (Button) findViewById(R.id.btn_continueadrs);
        init();

    }

    private void init() {
        JSONObject json = new JSONObject();
        try {
            //{"user_id":"10","addresstype":"checkout"}
            json.put("user_id", mPreferenceData.getCustomerId());
            json.put("addresstype", "checkout");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_LOAD_ADDRESS);
        gogglesAsynctask.execute(json.toString());
    }
}
