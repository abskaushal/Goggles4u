package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.ViewOrderItemData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

public class MyOrderRowActivity extends AppCompatActivity {

    private Context mContext;
    private static MyOrderRowActivity sInstance;
    private String orderid;
    private ListView orderlistview;
    private String pattern = "yy-MM-dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_row);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        sInstance = this;
        orderid = getIntent().getStringExtra("orderID");
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        Log.v("order_id", "" + orderid);
        orderIdview = (TextView) findViewById(R.id.tv_orderid);
        shipngadrs = (TextView) findViewById(R.id.tv_shipadrs);
        bilngadrs = (TextView) findViewById(R.id.tv_blngadrs);
        shpngmthd = (TextView) findViewById(R.id.tv_shipngmethods);
        pymntmthd = (TextView) findViewById(R.id.tv_pymntmthd);
        subtotal = (TextView) findViewById(R.id.tv_subtotal);
        shipngamt = (TextView) findViewById(R.id.tv_shipamnt);
        tax = (TextView) findViewById(R.id.tv_taxval);
        grandtotal = (TextView) findViewById(R.id.tv_grndttl);
        //shpngmthd = (TextView) findViewById(R.id.tv_shipadrs);

        orderlistview = (ListView) findViewById(R.id.listView_rowdata);


        init();
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

    public static void updateUi(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            Log.v("receiveJSon", "" + receiveJSon);

            if (result.equals(AppConstants.SUCCESSFUL)) {
                JSONObject myorderaray = null;

                try {
                    JSONObject myjson  = receiveJSon.getJSONObject("result");
                    Log.v("myorderjson", "parse" + myjson);
                    sInstance.displayData(myjson);

                   /* for(int i=0;i<myorderaray.length();i++){
                        JSONObject myjson = myorderaray.getJSONObject(i);
                        sInstance.displayData(myjson);
                        //orderIdview.setText(myjson.getString(""));
                    }*/





                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }
    }

    private  void displayData(JSONObject myjson) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");

        try {
            Date date = null;
            try {
                date = format.parse(myjson.getString("order_date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String dat = sdf.format(date);
            orderIdview.setText(dat);
            JSONObject addresjson = myjson.getJSONObject("addresses");
            //Log.v("addresjson", "" + addresjson);
            JSONObject bilngadrsjson  = addresjson.getJSONObject("billing");
           // Log.v("bilngadrsjson", "" + bilngadrsjson);
            Log.v("bilngadrs name", "" + bilngadrsjson.getString("lastname"));

            String biladrs = bilngadrsjson.getString("firstname") +" " +bilngadrsjson.getString("lastname") + "\n" + bilngadrsjson.getString("street")+ " \n" + bilngadrsjson.getString("region")+ "," + bilngadrsjson.getString("city")+ "\n" + bilngadrsjson.getString("postcode") +" ," +bilngadrsjson.getString("country_id") +"\n" +bilngadrsjson.getString("telephone") ;

            JSONObject shipngadrsjson  = addresjson.getJSONObject("shipping");
            //Log.v("shipngadrsjson", "" + shipngadrsjson);
            String shipadrs = shipngadrsjson.getString("firstname") +" " +shipngadrsjson.getString("lastname") + "\n" + shipngadrsjson.getString("street")+ " \n" + shipngadrsjson.getString("region")+ "," + shipngadrsjson.getString("city")+"\n"+ shipngadrsjson.getString("postcode") +" ," +shipngadrsjson.getString("country_id") +"\n" +shipngadrsjson.getString("telephone") ;
            JSONArray orderitemarray = myjson.getJSONArray("order_items");
            //Log.v("orderitems", "" + orderitemarray);
            ArrayList<ViewOrderItemData> viewOrderItemList = new ArrayList<>();
            for(int i=0;i<orderitemarray.length();i++){
                JSONObject orderjson = orderitemarray.getJSONObject(i);
                ViewOrderItemData itemdata = new ViewOrderItemData();
                itemdata.setSkuid(orderjson.getString("item_sku"));
                itemdata.setProductName(orderjson.getString("item_name"));
                itemdata.setQuantity(orderjson.getString("no_of_items_order"));
                itemdata.setTotal(orderjson.getString("total_amount"));
                itemdata.setUnit_price(orderjson.getString("item_unit_price"));
                viewOrderItemList.add(itemdata);
            }


            orderlistview.setAdapter(new OrderittemListAdapter(mContext,viewOrderItemList));

        shipngadrs.setText(shipadrs);
       bilngadrs.setText(biladrs);

            JSONObject pymntjson  = myjson.getJSONObject("paymentdetail");
            //Log.v("pymntjson", "" + pymntjson);

        pymntmthd.setText(pymntjson.getString("payment_code"));
            JSONObject shpngmthdjson  = myjson.getJSONObject("shippingmethod");
            //Log.v("shpngmthdjson", "" + shpngmthdjson);
            shpngmthd.setText(shpngmthdjson.getString("shipping_method"));
            subtotal.setText(myjson.getString("subtotal"));

        shipngamt.setText(myjson.getString("shipping_amount"));
        //tax.setText(myjson.getString("subtotal"));
        grandtotal.setText(myjson.getString("subtotal"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class OrderittemListAdapter extends BaseAdapter{
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
                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.vieworder_row,parent,false);
                    viewHolder = new ViewHolder();
                    viewHolder.productName = (TextView) convertView.findViewById(R.id.tv_vordername);
                    viewHolder.sku = (TextView) convertView.findViewById(R.id.tv_vordersku);
                    viewHolder.quantity = (TextView) convertView.findViewById(R.id.tv_vorderqty);
                    viewHolder.total = (TextView) convertView.findViewById(R.id.tv_vordertotal);
                    viewHolder.price = (TextView) convertView.findViewById(R.id.tv_vorderprice);
                    convertView.setTag(viewHolder);
                }else{
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
    class  ViewHolder {
        TextView productName,sku,quantity,price,total;
    }
    TextView orderIdview,shipngadrs,bilngadrs,shpngmthd,pymntmthd,subtotal,shipngamt,tax,grandtotal;
}
