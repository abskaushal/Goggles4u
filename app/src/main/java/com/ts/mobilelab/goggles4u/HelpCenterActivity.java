package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.adapter.HelpListAdapter;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.HelpData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HelpCenterActivity extends AppCompatActivity {

    private Context mContext;
    private static HelpCenterActivity sInstance;
    ListView mHelpListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        mContext = this;
        sInstance = this;
        init();
        mHelpListView = (ListView) findViewById(R.id.lv_help);


        mHelpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("position", "" + helpdataList.get(position).getIdentifier());
                Intent i = new Intent(mContext,HelpRowActivity.class);
                i.putExtra("identifier",helpdataList.get(position).getIdentifier());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void init() {

        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_HELPCENTER);
        gogglesAsynctask.execute();
    }


    public static void setUIData(String result, JSONObject receiveJSon) {
        if(result.equals(AppConstants.SUCCESSFUL)){

               // String cntn = receiveJSon.getString("content");
            sInstance.displayData(receiveJSon);

        }else{
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }

    }
    ArrayList<HelpData> helpdataList = new ArrayList<>();

    private  void displayData(JSONObject receiveJSon) {

        try {
            JSONArray dataArray = receiveJSon.getJSONArray("content");
            for(int i=0;i<dataArray.length();i++){
                JSONObject helpjson = dataArray.getJSONObject(i);
                HelpData helpData = new HelpData();
                helpData.setHeader(helpjson.getString("label"));
                helpData.setContent(helpjson.getString("text"));
                helpData.setIdentifier(helpjson.getString("identifier"));
                helpdataList.add(helpData);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mHelpListView.setAdapter(new HelpListAdapter(mContext,helpdataList));

    }

    public void dialPhoneNumber(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:9663262419"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
}
