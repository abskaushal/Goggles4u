package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.ViewOrderItemData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.ts.mobilelab.goggles4u.views.utils.InVoiceFragment;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewOrderActivity extends AppCompatActivity {

    private Context mContext;
    private static ViewOrderActivity sInstance;
    private String orderid;
    private ListView orderlistview;


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView orderId, datev, shipingamnt, shipmethod, paydetail, storecredit, discount;
    private TextView shipadrs1, shipadrs2, biladrs1, biladrs2, subtotalv, grandtotalv, shipAmntv;

    JSONObject datajson;

    private String pattern = "yy-MM-dd";

    private RelativeLayout homelt;
    private View mHeaderView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        sInstance = this;
        orderid = getIntent().getStringExtra("orderID");
        Log.v("order_id", "" + orderid);
        viewPager = (ViewPager) findViewById(R.id.container);
        homelt = (RelativeLayout) findViewById(R.id.relaive_homelt);

        initialise();
        init();


    }

    private void initialise() {
        mHeaderView = View.inflate(mContext, R.layout.fragment_orderview, null);
        orderId = (TextView) mHeaderView.findViewById(R.id.tv_orderid);
        datev = (TextView) mHeaderView.findViewById(R.id.tv_date);
        shipingamnt = (TextView) mHeaderView.findViewById(R.id.tv_date);

        paydetail = (TextView) mHeaderView.findViewById(R.id.tv_pamentmethod);
        shipmethod = (TextView) mHeaderView.findViewById(R.id.tv_shipmethod);
        subtotalv = (TextView) findViewById(R.id.tv_subtotal);
        grandtotalv = (TextView) findViewById(R.id.tv_grandtotal);

        shipadrs1 = (TextView) mHeaderView.findViewById(R.id.tv_shipadrs1);

        biladrs1 = (TextView) mHeaderView.findViewById(R.id.tv_biladrs1);
       ;
        orderlistview = (ListView) findViewById(R.id.lv_items);
        shipAmntv = (TextView) findViewById(R.id.tv_shipamnt);
        storecredit = (TextView) findViewById(R.id.tv_strcrdt);
        discount = (TextView) findViewById(R.id.tv_discount);

        orderlistview.addHeaderView(mHeaderView);



    }
    private void init() {


        JSONObject myorderjson = new JSONObject();
        try {
            myorderjson.put("order_id", orderid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_MYORDERROW);
        gogglesAsynctask.execute(myorderjson.toString());
    }

    JSONObject orderDetailjson;

    public static void updateUi(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
           // Log.v("receiveJSon", "" + receiveJSon);
            sInstance.homelt.setVisibility(View.VISIBLE);
            sInstance.displayData(receiveJSon);


        }
    }@Override
     public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showData(JSONObject receiveJSon) {
        orderDetailjson = receiveJSon;
        //setupViewPager(viewPager);
    }

    private void displayData(JSONObject datajson) {


        //{"result":{"OrderId":"100284253","shipto":"Mahesh Subudhi","status":"pending","shipping_amount":"4.9500","subtotal":"169.6000","discount":"0.0000","total":"174.5500","qty_ordered":"4.0000","order_date":"2016-07-07 05:33:19","order_items":[{"item_sku":"111551-distance-distance-ARC-odsph-odcyl-odaxis-odadd-oscyl-osaxis-osadd
        try {
            JSONObject myjson = datajson.getJSONObject("result");
            orderId.setText(myjson.getString("OrderId"));
            datev.setText(myjson.getString("order_date"));
            JSONObject addresjson = myjson.getJSONObject("addresses");

            JSONObject bilngadrsjson = addresjson.getJSONObject("billing");

            String biladrs = bilngadrsjson.getString("firstname") + "" + bilngadrsjson.getString("lastname") + "\n" + bilngadrsjson.getString("street") + "," + bilngadrsjson.getString("region") + " \n" + bilngadrsjson.getString("city") + "," + bilngadrsjson.getString("postcode") + " ," + bilngadrsjson.getString("country_id") + "\n" + bilngadrsjson.getString("telephone");

            JSONObject shipngadrsjson = addresjson.getJSONObject("shipping");

            String shipadrs = shipngadrsjson.getString("firstname") + "" + shipngadrsjson.getString("lastname") + "\n" + shipngadrsjson.getString("street") + " ," + shipngadrsjson.getString("region") + "\n" + shipngadrsjson.getString("city") + "," + shipngadrsjson.getString("postcode") + " ," + shipngadrsjson.getString("country_id") + "\n" + shipngadrsjson.getString("telephone");
            JSONArray orderitemarray = myjson.getJSONArray("order_items");

            ArrayList<ViewOrderItemData> viewOrderItemList = new ArrayList<>();
            for (int i = 0; i < orderitemarray.length(); i++) {
                JSONObject orderjson = orderitemarray.getJSONObject(i);
                ViewOrderItemData itemdata = new ViewOrderItemData();
                itemdata.setSkuid(orderjson.getString("item_sku"));
                itemdata.setProductName(orderjson.getString("item_name"));
                itemdata.setQuantity(orderjson.getString("no_of_items_order"));
                itemdata.setTotal(orderjson.getString("total_amount"));
                itemdata.setUnit_price(orderjson.getString("item_unit_price"));
                viewOrderItemList.add(itemdata);
            }


            orderlistview.setAdapter(new OrderittemListAdapter(mContext, viewOrderItemList));

            shipadrs1.setText(shipadrs);

            biladrs1.setText(biladrs);

            JSONObject pymntjson = myjson.getJSONObject("paymentdetail");

            paydetail.setText(pymntjson.getString("payment_title"));

            JSONObject shpngmthdjson = myjson.getJSONObject("shippingmethod");

            shipmethod.setText(shpngmthdjson.getString("shipping_method"));
            shipAmntv.setText("Shiping Amont " + myjson.getString("shipping_amount"));
            subtotalv.setText("Sub Total " + myjson.getString("subtotal"));


            Double discamnt = myjson.getDouble("discount_amt");

            if (discamnt > 0d) {
                discount.setVisibility(View.VISIBLE);
                discount.setText("Discount Amont " + myjson.getString("discount"));
            } else {
                discount.setVisibility(View.GONE);
            }
            Double credit_bal = myjson.getDouble("srore_cedit");

            if (credit_bal > 0) {
                storecredit.setVisibility(View.VISIBLE);
                storecredit.setText("Credit Amont " + myjson.getString("store_credit_amt"));
            } else {
                storecredit.setVisibility(View.GONE);
            }
            grandtotalv.setText("Grand Total " + myjson.getString("total"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class OrderittemListAdapter extends BaseAdapter {
        Context context;
        ArrayList<ViewOrderItemData> orderitemList;

        public OrderittemListAdapter(Context mContext, ArrayList<ViewOrderItemData> viewOrderItemList) {
            this.context = mContext;
            this.orderitemList = viewOrderItemList;


        }

        @Override
        public int getCount() {
            return orderitemList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.vieworder_row, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.productName = (TextView) convertView.findViewById(R.id.tv_vordername);
                viewHolder.sku = (TextView) convertView.findViewById(R.id.tv_vordersku);
                viewHolder.quantity = (TextView) convertView.findViewById(R.id.tv_vorderqty);
                viewHolder.total = (TextView) convertView.findViewById(R.id.tv_vordertotal);
                viewHolder.price = (TextView) convertView.findViewById(R.id.tv_vorderprice);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.productName.setText(orderitemList.get(position).getProductName());
            viewHolder.price.setText(orderitemList.get(position).getUnit_price());
            viewHolder.sku.setText(orderitemList.get(position).getSkuid());
            viewHolder.quantity.setText(orderitemList.get(position).getQuantity());
            viewHolder.total.setText(orderitemList.get(position).getTotal());


            return convertView;
        }
    }

    class ViewHolder {
        TextView productName, sku, quantity, price, total;
    }
}
