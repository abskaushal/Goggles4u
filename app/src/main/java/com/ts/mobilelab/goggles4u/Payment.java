package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.Message;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.i.Result;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.ts.mobilelab.goggles4u.utils.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class Payment extends AppCompatActivity {


    private Context mContext;
    private static Payment sInstance;
    private PreferenceData mPreferenceData;
    RadioGroup rgPaymnent;
    String paymentjson;
    Button submitBtn;
    String selectVal;
    int chkflag = 0;
    private TextView grandTotal,credit_label;
    private JSONObject mainJson;
    private CheckBox creditBox;
    RelativeLayout labellt;
    private  Double tottaltopay,creditbalance = 0d,total;
    DecimalFormat dc = new DecimalFormat("#.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        paymentjson = getIntent().getStringExtra("paymentjson");
        submitBtn = (Button) findViewById(R.id.btn_paysubmit);
        rgPaymnent = (RadioGroup) findViewById(R.id.rg_pay);
        grandTotal = (TextView) findViewById(R.id.tv_totalpay);
        credit_label = (TextView) findViewById(R.id.tv_waletmsg);
        labellt = (RelativeLayout) findViewById(R.id.labllt);
        creditBox = (CheckBox) findViewById(R.id.cb_storecredit);
        displayPayemntOption();
        //Log.v("selectVal", "" + selectVal);
        Log.v("paymentjson", "" + paymentjson);
        if(!mPreferenceData.isLogincheck()){
            labellt.setVisibility(View.GONE);
        }
        //paymentjson: {"store_credits":[],"payment_methods":{"cashondelivery":{"value":"cashondelivery","label":"Cash On Delivery"},"free":{"value":"free","label":"No Payment Information Required"},"checkmo":{"value":"checkmo","label":"Check \/ Money order"}},"goto":"review","Message":"Address added successfully.","grand_total":6.95,"Error":false}
        //paymentjson: {"Error":false,"payment_methods":{"checkmo":{"label":"Check \/ Money order","value":"checkmo"},"free":{"label":"No Payment Information Required","value":"free"},"cashondelivery":{"label":"Cash On Delivery","value":"cashondelivery"}},"store_credits":{"use_customer_balance":{"label":"Use store credit ($100.00 available)","value":"use_customer_balance","balance":100}},"grand_total":0,"goto":"review","Message":"Address added successfully."}
        try {
            String amnttopay = mainJson.getString("grand_total");
            total = Double.valueOf(mainJson.getString("grand_total"));
            tottaltopay = Double.parseDouble(amnttopay);
            grandTotal.setText("Total amount to Pay "+amnttopay);
            if(mainJson.has("store_credits")) {
                labellt.setVisibility(View.VISIBLE);
                JSONObject storecreditjson = mainJson.getJSONObject("store_credits");
                JSONObject sjson = storecreditjson.getJSONObject("use_customer_balance");
                String label = sjson.getString("label");
                credit_label.setText(label);
                creditbalance = Double.parseDouble(sjson.getString("balance"));
            }else{
                labellt.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
      /*  if(creditbalance > tottaltopay){
            rgPaymnent.setVisibility(View.GONE);
            chkflag = 2;
        }*/
      /*  if(creditbalance > 0d) {
            creditBox.setChecked(true);
            if(creditbalance < tottaltopay){
                total = tottaltopay - creditbalance;
            }else{
                total =  creditbalance - tottaltopay;
                rgPaymnent.setVisibility(View.GONE);
                chkflag = 2;
            }
            grandTotal.setText("Total amount to Pay $"+total);
        }else{
            grandTotal.setText("Total amount to Pay $"+tottaltopay);

        }*/

        creditBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JSONObject json = new JSONObject();
                if(isChecked){
                    //[10-08-2016 19:24:05] Surjan Singh: htp://<hostname>/restapi/checkout/setStoreCredit
                   //[10-08-2016 19:24:29] Surjan Singh: {"quote_id":<>,"use_customer_balance":1}


                    if(creditbalance < tottaltopay){
                        total = tottaltopay - creditbalance;
                    }else{
                        total =  creditbalance - tottaltopay;
                        rgPaymnent.setVisibility(View.GONE);
                        chkflag = 2;
                        grandTotal.setVisibility(View.GONE);
                    }

                    grandTotal.setText("Total amount to Pay $"+dc.format(total));
                    try {
                        json.put("use_customer_balance", 1);
                        json.put("quote_id", mPreferenceData.getCartQuoteID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    rgPaymnent.setVisibility(View.VISIBLE);
                    grandTotal.setVisibility(View.VISIBLE);
                    try {
                       // json.remove("use_customer_balance");
                        json.put("use_customer_balance", 0);
                        json.put("quote_id", mPreferenceData.getCartQuoteID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    grandTotal.setText("Total amount to Pay $"+dc.format(total));
                }
                GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_STORECREDIT_UPDATE);
                gogglesAsynctask.execute(json.toString());
            }
        });

        rgPaymnent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                chkflag = 1;
                int checkedRadioButtonId = rgPaymnent.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) findViewById(checkedRadioButtonId);
                //selectVal = radioBtn.getText().toString();
                selectVal = valuellist.get(checkedRadioButtonId);
                Log.v("selectVal", "" + selectVal);


            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("selectVal", "" + selectVal);
                Log.v("chkflag", "" + chkflag);
                JSONObject payjson = new JSONObject();
                if (chkflag == 1) {

                    try {
                        payjson.put("paymentmethod", selectVal);
                        payjson.put("quote_id", mPreferenceData.getCartQuoteID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("payjson", "" + payjson);
                    GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_SAVEORDER);
                    gogglesAsynctask.execute(payjson.toString());

                }else if(chkflag == 2){
                    try {
                        payjson.put("paymentmethod", "");
                        payjson.put("quote_id", mPreferenceData.getCartQuoteID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("payjson", "" + payjson);
                    GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_SAVEORDER);
                    gogglesAsynctask.execute(payjson.toString());
                }

                else {
                    Toast.makeText(mContext, "Please select payment option to continue.", Toast.LENGTH_LONG).show();
                }
            }


        });


    }

    ArrayList<String> labellist = new ArrayList<>();
    ArrayList<String> valuellist = new ArrayList<>();

    private void displayPayemntOption() {
        //JSONObject receiveJSon = null;
        try {
            mainJson = new JSONObject(paymentjson);
            Log.v("receiveJSon", "11" + mainJson);

            JSONObject payoptjson = mainJson.getJSONObject("payment_methods");
            Log.v("payoptjson", "" + payoptjson);
            Iterator<String> itr = payoptjson.keys();
            while (itr.hasNext()) {
                String keys = itr.next();

                JSONObject val = payoptjson.getJSONObject(keys);

                String label = val.getString("label");
                String value = val.getString("value");
                Log.v("label", "" + label);
                if (!"null".equalsIgnoreCase(label)) {
                    labellist.add(label);
                }
                valuellist.add(value);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < labellist.size(); i++) {
            RadioButton rb = new RadioButton(mContext);
            rb.setId(i);
            rb.setText(labellist.get(i));
            rgPaymnent.addView(rb);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public static void setUIData(String result, JSONObject receiveJSon, JSONObject sendjSon) {

        Log.v("receiveJSon", "" + receiveJSon);
        Log.v("total", "" + sInstance.total);

        //{"Error":false,"OrderId":"100284256","Message":"Your order is successfully placed."}
        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.submitBtn.setVisibility(View.VISIBLE);
            try {
                if(sendjSon.getString("paymentmethod") != null) {
                    if (!"cashondelivery".equals(sendjSon.getString("paymentmethod"))) {
                        sInstance.startActivity(new Intent(sInstance, PaypalActivity.class).putExtra("orderidjson", receiveJSon.toString())
                                .putExtra("amnt", sInstance.total));
                    } else {
                        Toast.makeText(sInstance, "" + receiveJSon.getString("Message"), Toast.LENGTH_LONG).show();
                        sInstance.mPreferenceData.setCartQuoteID("");
                        sInstance.finish();
                        String orderId = receiveJSon.getString("OrderId");
                        sInstance.showReviewDialog(orderId);
                    }
                }else{
                    Toast.makeText(sInstance, "" + receiveJSon.getString("Message"), Toast.LENGTH_LONG).show();
                    sInstance.mPreferenceData.setCartQuoteID("");
                    sInstance.finish();
                    String orderId = receiveJSon.getString("OrderId");
                    sInstance.showReviewDialog(orderId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            sInstance.finish();
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }

    private  void showReviewDialog(final String orderId) {
        startActivity(new Intent(mContext, OrderCompleteActivity.class).putExtra("orderID", orderId));
    }




    public static void updateData(String result, JSONObject sendJsonobj, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            // Intent i = new Intent(sInstance, ShoppingCartActivity.class);
            //sInstance.startActivity(i);
            //sInstance.finish(); {"use_customer_balance":0,"quote_id":"77"}
            try {
                String flag = sendJsonobj.getString("use_customer_balance");
                if(flag.equals("1")){
                   // Toast.makeText(sInstance, "StoreCredit balance selected." + result, Toast.LENGTH_LONG).show();
                }else{
                   // Toast.makeText(sInstance, "StoreCredit balance unselected." + result, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }

}
