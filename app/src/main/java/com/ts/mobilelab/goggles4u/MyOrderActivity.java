package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.MyOrderData;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyOrderActivity extends AppCompatActivity {

    private Context mContext;
    private static MyOrderActivity sInstance;
    private ListView listView_mydata;
    private MYOrderAdapter myOrderAdapter;
    private ArrayList<MyOrderData> myOrderDataArrayList = new ArrayList<>();
    private String pattern = "yy-MM-dd";
    private PreferenceData mPreferenceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        View empty = findViewById(R.id.empty);
        listView_mydata = (ListView) findViewById(R.id.listView_mydata);
        listView_mydata.setEmptyView(empty);
        init();

        //listView_mydata.setO
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {


        JSONObject myorderjson = new JSONObject();
        try {
            myorderjson.put("user_id",mPreferenceData.getCustomerId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_MYORDER);
        gogglesAsynctask.execute(myorderjson.toString());
    }

    public static void updateUi(String result, JSONObject receiveJSon) {


        if(result.equals(AppConstants.SUCCESSFUL)){
            JSONArray myorderaray = null;
            try {
                myorderaray = receiveJSon.getJSONArray("result");



                for(int i=0;i<myorderaray.length();i++){
                    JSONObject myjson = myorderaray.getJSONObject(i);
                    MyOrderData mydata = new MyOrderData();
                    mydata.setOrderId(myjson.getString("OrderId"));
                    mydata.setStatus(myjson.getString("status"));
                    mydata.setDate(myjson.getString("order_date"));
                    mydata.setQuantity(myjson.getString("qty_ordered"));
                    mydata.setShipto(myjson.getString("shipto"));
                    mydata.setTotalamount(myjson.getString("total"));

                    sInstance.myOrderDataArrayList.add(mydata);

                }
                sInstance.showdata();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else{
            Toast.makeText(sInstance,""+result,Toast.LENGTH_LONG).show();
        }
    }

    private  void showdata() {
        listView_mydata.setAdapter(new MYOrderAdapter(mContext, myOrderDataArrayList));
    }


    public class MYOrderAdapter extends BaseAdapter {
        ArrayList<MyOrderData> myorderListdata;
        Context context;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        public MYOrderAdapter(Context mContext, ArrayList<MyOrderData> myOrderDataArrayList) {
            context = mContext;
            myorderListdata = myOrderDataArrayList;
        }

        @Override
        public int getCount() {
            return myorderListdata.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.myorder_row,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.orderId = (TextView)convertView.findViewById(R.id.tv_myorderid);
                viewHolder.date = (TextView)convertView.findViewById(R.id.tv_dateview );
                viewHolder.shipto = (TextView)convertView. findViewById(R.id.tv_shiptoview);
                viewHolder.status = (TextView)convertView. findViewById(R.id.tv_statusview);
                viewHolder.total = (TextView)convertView. findViewById(R.id.tv_totlview);
                viewHolder.viewBtn = (Button) convertView.findViewById(R.id.btn_view);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.orderId.setText(myorderListdata.get(position).getOrderId());
           // Log.v("before", "" + myorderListdata.get(position).getDate());
            viewHolder.date.setText(myorderListdata.get(position).getDate());
           /* try {
                Date date = format.parse(myorderListdata.get(position).getDate());

                String dat = sdf.format(date);
                viewHolder.date.setText(""+dat);

            } catch (ParseException e) {
                e.printStackTrace();
            }*/

            viewHolder.shipto.setText(myorderListdata.get(position).getShipto());
            viewHolder.status.setText(myorderListdata.get(position).getStatus());
            viewHolder.total.setText(myorderListdata.get(position).getTotalamount());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    startActivity(new Intent(mContext, ViewOrderActivity.class).putExtra("orderID", myorderListdata.get(position).getOrderId()));
                }
            });

            /*viewHolder.viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, ViewOrderActivity.class).putExtra("orderID", myorderListdata.get(position).getOrderId()));
                }
            });*/


            return convertView;
        }

        class ViewHolder{
            TextView orderId,date,shipto,status,total;
            Button viewBtn;
        }
    }
}
