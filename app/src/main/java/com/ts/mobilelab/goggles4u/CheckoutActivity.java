package com.ts.mobilelab.goggles4u;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CheckoutActivity extends AppCompatActivity {

    private Context mContext;
    private static CheckoutActivity sInstance;
    private PreferenceData mPreferenceData;
    private Button paymntbtn;
    JSONObject shipngadrsjson;
    ImageView editAdrs;
    TextView shipAdrsView, addAdrsView, addshipngadrs,  shipngadrs3, slctadrs;

    private CardView bilingCardView, shipingCardView;
    RelativeLayout homelt;
    int shipngflag = 0;
    private CheckBox cbxslctship;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        paymntbtn = (Button) findViewById(R.id.btn_payment);
        editAdrs = (ImageView) findViewById(R.id.tv_edit);
        slctadrs = (TextView) findViewById(R.id.tv_slctadrs);
        cbxslctship = (CheckBox) findViewById(R.id.cb_saveshippng);
        homelt = (RelativeLayout) findViewById(R.id.relative_chkhomelt);
        if(mPreferenceData.isLogincheck()){
            init();
        }else{
            slctadrs.setVisibility(View.GONE);
            Intent i = new Intent(mContext, EditAddress.class);
            i.putExtra("from", "addchkoutaddress");
            startActivityForResult(i, AppConstants.CODE_FOR_ADDFSTCHKOUTADRS);
        }


        paymntbtn.setOnClickListener(paymentListener);
        editAdrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, EditAddress.class);
                i.putExtra("shipingadrs", shipngadrsjson.toString());
                i.putExtra("from", "chkoutedit");
                startActivityForResult(i, AppConstants.CODE_FOR_EDITCHKOUTADRS);
            }
        });
        shipingCardView = (CardView) findViewById(R.id.cardview_shiping);
        addAdrsView = (TextView) findViewById(R.id.addaddresview);

        addshipngadrs = (TextView) findViewById(R.id.tv_editshipaddrs);
        shipAdrsView = (TextView) findViewById(R.id.tv_shipadrsview);
        shipngadrs3 = (TextView) findViewById(R.id.tv_shipngadrs3);



        slctadrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPreferenceData.isLogincheck()) {
                    startActivityForResult(new Intent(mContext, SelectAddressActivity.class), AppConstants.CODE_FOR_SELCTADRS);
                }else{
                    Toast.makeText(mContext,"Login to select address",Toast.LENGTH_LONG).show();
                }

            }
        });
        cbxslctship.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    shipngflag = 1;
                }else{
                    shipngflag = 0;
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.v("onActivityResult", "in chkout" );
        homelt.setVisibility(View.VISIBLE);
        if (requestCode == AppConstants.CODE_FOR_EDITCHKOUTADRS) {
            if (resultCode == RESULT_OK) {
                String chkouteditadrs = intent.getStringExtra("chkouteditdata");

                displayAdrs(chkouteditadrs);
            }
        } else if (requestCode == AppConstants.CODE_FOR_SELCTADRS) {
            if (resultCode == RESULT_OK) {
                String chkoutadrs = intent.getStringExtra("selctjson");

                displayAdrs(chkoutadrs);

            }
        } else if (requestCode == AppConstants.CODE_FOR_ADDFSTCHKOUTADRS) {
            if (resultCode == RESULT_OK) {
                String chkoutadrs = intent.getStringExtra("selctjson");
                Log.v("CODE_FOR_ADDFSTCHKOUTADRS",""+chkoutadrs);
                displayAdrs(chkoutadrs);

            }else{
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {


            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener paymentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //{"quote_id","","customer_id":"","billing_address_id":"<id of the selected address , no need to send the id if its new address>",
            // "save_in_address_book" : <0 or 1>,"use_for_shipping":1,"firstname":"","lastname":"",
            // "street":"","postcode":"","city":"","region_id":"<if you select the region>","region":"if region is entered otherwise blank",
            // "country_id":"like US,GB","telephone":""}

            //{"region":"Alabama","entity_type_id":"2","middlename":null,"fax":null,"is_active":"1","country_id":"US","street":"123 Tets\n152 A,II FLR,24 CROSS 18 MAIN ,  HSR LAYOUT - 2,NEAR HSR CLUB","region_id":"1",
            //   "lastname":"Subudhi","firstname":"Mahesh","entity_id":"19","postcode":"560102","attribute_set_id":"0","city":"BANGALORE",
            // "updated_at":"2016-06-28 11:36:54","company":null,"created_at":"2016-06-28 15:36:54","telephone":"7576756775",
            // "customer_id":"10","increment_id":null,"parent_id":"10"}
            Log.v("shipngadrsjson paymentListener", "" + shipngadrsjson);
            JSONObject paymentjson = new JSONObject();
            try {
                paymentjson.put("quote_id", mPreferenceData.getCartQuoteID());
                paymentjson.put("customer_id", mPreferenceData.getCustomerId());
                /*if (shipngadrsjson.has("entity_id")) {
                    paymentjson.put("billing_address_id",shipngadrsjson.getString("entity_id"));
                }*/
                // paymentjson.put("billing_address_id",shipngadrsjson.getString(""));
                if (shipngadrsjson.has("save_in_address_book")) {
                    paymentjson.put("save_in_address_book", shipngadrsjson.getString("save_in_address_book"));
                }

                if (shipngadrsjson.has("use_for_shipping")) {
                    paymentjson.put("use_for_shipping", shipngadrsjson.getString("use_for_shipping"));
                }

                if (shipngadrsjson.has("entity_id")) {
                    paymentjson.put("billing_address_id", shipngadrsjson.getString("entity_id"));
                }
                paymentjson.put("firstname", shipngadrsjson.getString("firstname"));
                paymentjson.put("lastname", shipngadrsjson.getString("lastname"));
                paymentjson.put("street", shipngadrsjson.getString("street"));
                paymentjson.put("postcode", shipngadrsjson.getString("postcode"));
                paymentjson.put("city", shipngadrsjson.getString("city"));
                if (shipngadrsjson.has("region_id")) {
                    paymentjson.put("region_id", shipngadrsjson.getString("region_id"));
                }

                if (shipngadrsjson.has("region")) {
                    paymentjson.put("region", shipngadrsjson.getString("region"));
                }

                if (shipngadrsjson.has("country_id")) {
                    paymentjson.put("country_id", shipngadrsjson.getString("country_id"));
                }

                paymentjson.put("telephone", shipngadrsjson.getString("telephone"));
                paymentjson.put("use_for_shipping",shipngflag);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            GogglesAsynctask asynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_CHECKOUT);
            asynctask.execute(paymentjson.toString());

        }
    };

    private void init() {
        JSONObject addrsjson = new JSONObject();
        try {
            addrsjson.put("user_id", mPreferenceData.getCustomerId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_LOAD_ADDRESSLIST);
        gogglesAsynctask.execute(addrsjson.toString());

    }

    private void displayAdrs(String chkoutadrs) {
        try {
            shipngadrsjson = new JSONObject(chkoutadrs);
            sInstance.shipingCardView.setVisibility(View.VISIBLE);
            Log.v("shipngadrsjson in display",""+shipngadrsjson);
            // sInstance.shipingadrsid = shipngadrsjson.getString("entity_id");

            String shipadrs = shipngadrsjson.getString("firstname") + " " + shipngadrsjson.getString("lastname") + "\n" + shipngadrsjson.getString("street") + "," + shipngadrsjson.getString("region") + "\n" + shipngadrsjson.getString("city") + "-" + shipngadrsjson.getString("postcode") + " ," + sInstance.shipngadrsjson.getString("country_id");

            //String shipadrs = shipngadrsjson.getString("firstname") +"" +shipngadrsjson.getString("lastname") + "" + shipngadrsjson.getString("city")+ "" + shipngadrsjson.getString("region")+ "" + shipngadrsjson.getString("street");
            //String shipadrs2 = shipngadrsjson.getString("country_id") +"" +shipngadrsjson.getString("postcode") +"" +shipngadrsjson.getString("telephone") ;

            sInstance.addshipngadrs.setText(shipadrs);

            sInstance.shipngadrs3.setText("Mob:" + shipngadrsjson.getString("telephone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private void submitpayOption(JSONObject payjson) {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_SAVEORDER);
        gogglesAsynctask.execute(payjson.toString());

    }


    public static void setAdrsData(String result, JSONObject receiveJSon) {

        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.homelt.setVisibility(View.VISIBLE);
            String shipngadrs = null;
            try {
                shipngadrs = receiveJSon.getString("default_billing");
                //String biladrs = receiveJSon.getString("default_billing");

                Log.v("shipngadrs", "222" + shipngadrs);

                if (shipngadrs.isEmpty()) {
                    sInstance.shipingCardView.setVisibility(View.GONE);
                    Intent i = new Intent(sInstance, EditAddress.class);
                    i.putExtra("from", "addchkoutaddress");
                    sInstance.startActivityForResult(i, AppConstants.CODE_FOR_ADDFSTCHKOUTADRS);


                } else {

                    sInstance.displayAdrs(shipngadrs);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }




    public static void setData(String result, JSONObject receiveJSon, JSONObject sendJsonobj) {

        if (result.equals(AppConstants.SUCCESSFUL)) {
          //  Log.v("receiveJSon", "" + receiveJSon);
            //Log.v("receiveJSon", "" + receiveJSon.has("goto"));
            if(receiveJSon.has("goto")){
                try {
                    String go = receiveJSon.getString("goto");
                    if(go.equals("review")){
                        sInstance.finish();
                        sInstance.startActivity(new Intent(sInstance,OrderReviewActivity.class).putExtra("paymentjson",receiveJSon.toString()));
                    }else{
                        sInstance.startActivity(new Intent(sInstance,ShippingActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }  }


        } else {

            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }
}
//http://www.giftbox.pk/restapi/catalog/productsbycat
//{"cat_id":"7","p":1,"c":14,"sort":"price","dir":"asc","customer_id":0,"session_id":"911505003112097"}