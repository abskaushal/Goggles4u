package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.adapter.FavroiteListAdapter;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.FavoriteData;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavListActivity extends AppCompatActivity {

    private PreferenceData mPreferenceData;
    private Context mContext;
    private static FavListActivity sInstance;
    ListView favListView;
    FavroiteListAdapter favroiteListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        View empty = findViewById(R.id.empty);
        favListView = (ListView) findViewById(R.id.lv_favlist);
        favListView.setEmptyView(empty);

        init();

        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(mContext, ProductDetailsActivity.class);
                i.putExtra("pid", favList.get(position).getId());
                        i.putExtra("skuid", favList.get(position).getSkuid());
                startActivityForResult(i,22);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK){
            if(resultCode == 22){
                init();
            }
        }
    }

    private void init() {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_FAVRITELIST);
        JSONObject productjson = new JSONObject();
        try {
            if(mPreferenceData.isLogincheck()){
                productjson.put("customer_id", mPreferenceData.getCustomerId());
                productjson.put("session_id", "");
            }else{
                productjson.put("customer_id", 0);
                productjson.put("session_id", mPreferenceData.getDEVICE_ID());            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gogglesAsynctask.execute(productjson.toString());
    }


    public static void updateUiData(String result, JSONObject receiveJSon) {
        Log.v("favList", "updateUiData");
        if (result.equals(AppConstants.SUCCESSFUL)) {
           sInstance.showListData(receiveJSon);
        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }

    }
    ArrayList<FavoriteData> favList = new ArrayList<>();
    private  void showListData(JSONObject receiveJSon) {
        //{"data":[{"id":"47847","name":"G4U T3005","sku":"112752-c","price":"6.9500","special_price":null,
        // "image_url":"http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/image\/265x\/9df78eab33525d08d6e5fb8d27136e95\/1\/1\/112756a_1.jpg","small_imageurl":"http:\/\/www.giftbox.pk\/media\/catalog\/product\/cache\/1\/small_image\/9df78eab33525d08d6e5fb8d27136e95\/1\/1\/112756a_1.jpg","thumbnail":{},"mrp":6.95,"you_pay":6.95,"actual_price":"$6.95","formated_price":"$6.95"}],"Error":false}

        try {
            if(receiveJSon.has("data")) {
                JSONArray favjsonAry = receiveJSon.getJSONArray("data");
                for (int i = 0; i < favjsonAry.length(); i++) {
                    JSONObject favjson = favjsonAry.getJSONObject(i);
                    FavoriteData favdata = new FavoriteData();
                    favdata.setId(favjson.getString("id"));
                    favdata.setName(favjson.getString("name"));
                    favdata.setPrice(favjson.getString("price"));
                    favdata.setImgurl(favjson.getString("image_url"));
                    favdata.setSkuid(favjson.getString("sku"));
                    favList.add(favdata);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("favList", ""+favList.size());
        if(favroiteListAdapter == null){
            favroiteListAdapter = new FavroiteListAdapter(mContext, favList);
        }

        favListView.setAdapter(favroiteListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void updateDeleteData(String result, JSONObject receiveJSon, JSONObject sendJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.updateListData(sendJSon);
        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }

    private  void updateListData(JSONObject sendJSon) {

        try {
            String id = sendJSon.getString("product_id");
            for(int i=0;i<favList.size();i++){
                if(id.equals(favList.get(i).getId())){
                    favList.remove(i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(favroiteListAdapter!=null){
            favroiteListAdapter.refresh(favList);
        }
    }
}
