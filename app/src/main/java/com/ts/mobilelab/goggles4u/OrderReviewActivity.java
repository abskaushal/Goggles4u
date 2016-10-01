package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.adapter.ShoppingCartAdapter;
import com.ts.mobilelab.goggles4u.core.GogglesManager;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.CartData;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderReviewActivity extends AppCompatActivity {

    private ListView shoppingCartList;
    ShoppingCartAdapter mShoppingCartAdapter;
    private Context mContext;
    private static OrderReviewActivity sInstance;
    private PreferenceData mPreferenceData;
    private ArrayList<CartData> mCartDataList;
    private TextView grandTotal,subTotal,discamnt,shipamnt;
    JSONObject clrjson;
    Button cpnBtn,checkoutBtn;
    EditText cpnBox;
    LinearLayout dicountlt;
    ImageView applycpn,cancelcpn;
    private CardView card_subtotalView;
    int menu_clearflag = 0;
    LinearLayout linearCpn;
    String payjson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        init();

        payjson = getIntent().getStringExtra("paymentjson");
        shoppingCartList = (ListView) findViewById(R.id.listView_cart);

        mCartDataList = new ArrayList<>();
        grandTotal = (TextView) findViewById(R.id.tv_grandtotal);
        //shoppingCartList.setEmptyView(empty);
        cpnBox = (EditText) findViewById(R.id.edt_cpnbox);
        //cpnBtn = (Button) findViewById(R.id.btn_cpn);
        applycpn = (ImageView) findViewById(R.id.imv_apply);
        cancelcpn = (ImageView) findViewById(R.id.imv_cancel);
        subTotal = (TextView) findViewById(R.id.tv_subtotal);
        discamnt = (TextView) findViewById(R.id.tv_discamnt);
        dicountlt = (LinearLayout) findViewById(R.id.linear_disc);
        shipamnt = (TextView) findViewById(R.id.tv_shipamnt);
        card_subtotalView = (CardView) findViewById(R.id.card_subtotal);
        checkoutBtn = (Button) findViewById(R.id.btn_chkout);
        linearCpn = (LinearLayout) findViewById(R.id.linear_cpn);
        linearCpn.setVisibility(View.GONE);
        card_subtotalView.setVisibility(View.GONE);
        checkoutBtn.setText("Proceed to Payment");
        //applycpn.setOnClickListener(couponListener);
        //cancelcpn.setOnClickListener(removecpnListener);
        checkoutBtn.setOnClickListener(payListener);
       /* if(!mPreferenceData.getCartQuoteID().isEmpty()){
            menu_clearflag = 1;
        }
        invalidateOptionsMenu();*/
    }

    private void init() {

        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_CARTREVIEW);
        JSONObject productjson = new JSONObject();
        try {
            productjson.put("quote_id", mPreferenceData.getCartQuoteID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gogglesAsynctask.execute(productjson.toString());
    }

    private View.OnClickListener payListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, Payment.class).putExtra("paymentjson", payjson));
        }
    };
    private void showData(JSONObject receiveJSon) {


        mCartDataList = GogglesManager.getInstance().getCartDataArrayList();
        shoppingCartList.setAdapter(new ShoppingCartAdapter(mContext, mCartDataList));

        //Log.v("data json", "1111" + receiveJSon.getString("data"));

        String data = null;

        String shippingcost = null;
        try {
            data = receiveJSon.getString("data");
            if(receiveJSon.has("shipping")) {
                JSONObject shipjson = receiveJSon.getJSONObject("shipping");
                shippingcost = shipjson.getString("shipping_cost");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //if(str != null && !str.isEmpty()) {


        if (data != null && !data.isEmpty() && !"null".equalsIgnoreCase(data)) {
            try {

                card_subtotalView.setVisibility(View.VISIBLE);
                menu_clearflag = 1;
                Log.v("menu_clearflag", "insoide" + menu_clearflag);
                JSONObject mainjson = receiveJSon.getJSONObject("data");

                DecimalFormat dff = new DecimalFormat("#.00");
                Log.v("grandTotal", "" + mainjson.getString("grand_total"));
                grandTotal.setText("Grand Total $" + dff.format(Double.parseDouble(mainjson.getString("grand_total"))));
                subTotal.setText("Sub Total $" + dff.format(Double.parseDouble(mainjson.getString("subtotal"))));
                shipamnt.setText("Shipping amnt $" + dff.format(Double.parseDouble(shippingcost)));
                int discval =Integer.parseInt(mainjson.getString("discount_amount"));
                if (discval != 0) {
                    discamnt.setVisibility(View.VISIBLE);
                    discamnt.setText("Discount amnt $"+discval);

                } else {
                    discamnt.setVisibility(View.INVISIBLE);
                }
                //Double d = Double.parseDouble(mainjson.getString("subtotal")) - Double.parseDouble(mainjson.getString("grand_total"));
                //discamnt.setText("$"+dff.format(Double.parseDouble(mainjson.getString(""))));
                //
                JSONArray itemAry = mainjson.getJSONArray("items");
                Log.v("itemAry size", "" + itemAry.length());
                mPreferenceData.setCartItemCount("" + itemAry.length());

                for (int i = 0; i < itemAry.length(); i++) {
                    JSONObject cartjson = itemAry.getJSONObject(i);
                    if (cartjson.has("configurable_option")) {
                        JSONObject optionjson = cartjson.getJSONObject("configurable_option");
                        clrjson = optionjson.getJSONObject("attribute-value");
                     //  Log.v("clrjson", "" + clrjson);
                    }

                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }else{
            card_subtotalView.setVisibility(View.GONE);
            try {
                Toast.makeText(mContext, "" + receiveJSon.getString("Message"), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
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

    public static void setUIData(String result, JSONObject receiveJSon) {

        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.showData(receiveJSon);
        } else {

            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }
}
