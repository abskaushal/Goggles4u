package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.data.StoreCreditData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreCreditDataActivity extends AppCompatActivity {

    private Context mContext;
    private static StoreCreditDataActivity sInstance;
    private TextView crntbalance;
    private ListView balanceList;
    private LinearLayout linearheader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_credit_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        sInstance = this;
        View empty = findViewById(R.id.empty);
        crntbalance = (TextView) findViewById(R.id.tv_crntbalance);
        balanceList = (ListView) findViewById(R.id.listView_balace);
        linearheader = (LinearLayout) findViewById(R.id.linear_storeheader);
        balanceList.setEmptyView(empty);
        init();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {


        JSONObject myorderjson = new JSONObject();
        try {
            myorderjson.put("customer_id", new PreferenceData().getCustomerId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_STORECREDIT);
        gogglesAsynctask.execute(myorderjson.toString());
    }

    public static void updateUi(String result, JSONObject receiveJSon) {

        if (result.equals(AppConstants.SUCCESSFUL)) {
            try {
                JSONObject resultjson = receiveJSon.getJSONObject("result");
                sInstance.displayData(resultjson);
                //  Log.v("resultjson", "" + resultjson);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(sInstance.mContext, "" + result, Toast.LENGTH_LONG).show();
        }


    }

    private void displayData(JSONObject resultjson) {
        JSONArray creditary = null;
        try {

            creditary = resultjson.getJSONArray("credits");
            Log.v("creditary", "" + creditary.length());
            if(creditary.length() != 0){
                linearheader.setVisibility(View.VISIBLE);
            }


            //[{"date":"2\/13\/2016 4:04 AM","balance_amount":"$987.10","action":"Used","balance_data":"-$12.90"}
            ArrayList<StoreCreditData> dataList = new ArrayList<>();
            for (int i = 0; i < creditary.length(); i++) {
                JSONObject balancejson = creditary.getJSONObject(i);
                StoreCreditData creditData = new StoreCreditData();
                creditData.setDate(balancejson.getString("date"));
                creditData.setBalancecharge(balancejson.getString("balance_data"));
                creditData.setBalanceremain(balancejson.getString("balance_amount"));
                creditData.setAction(balancejson.getString("action"));
                dataList.add(creditData);
            }
            String crediamnt = resultjson.getString("credit_balance");
            crntbalance.setText(crediamnt);
            balanceList.setAdapter(new BalanceAdapter(mContext, dataList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class BalanceAdapter extends BaseAdapter {
        Context cntx;
        ArrayList<StoreCreditData> dataList;

        public BalanceAdapter(Context mContext, ArrayList<StoreCreditData> dataLists) {
            cntx = mContext;
            dataList = dataLists;
        }

        @Override
        public int getCount() {
            return dataList.size();
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
                LayoutInflater inflater = (LayoutInflater) cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.balance_row, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.action = (TextView) convertView.findViewById(R.id.tv_action);
                viewHolder.balnccharge = (TextView) convertView.findViewById(R.id.tv_balcharged);
                viewHolder.balanceremain = (TextView) convertView.findViewById(R.id.tv_balremain);
                viewHolder.date = (TextView) convertView.findViewById(R.id.tv_dated);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.action.setText(dataList.get(position).getAction());
            viewHolder.balnccharge.setText(dataList.get(position).getBalancecharge());
            viewHolder.balanceremain.setText(dataList.get(position).getBalanceremain());
            viewHolder.date.setText(dataList.get(position).getDate());


            return convertView;
        }
    }

    static class ViewHolder {
        TextView action, balnccharge, balanceremain, date;
    }

}
