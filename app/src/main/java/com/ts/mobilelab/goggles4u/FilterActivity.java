package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.filter.FilterAdapter;
import com.ts.mobilelab.goggles4u.filter.FilterItem;
import com.ts.mobilelab.goggles4u.filter.FilterOptionsAdapter;
import com.ts.mobilelab.goggles4u.i.ICallback;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FilterActivity extends AppCompatActivity implements ICallback {
    public static final String LOG_TAG = FilterActivity.class.getSimpleName();
    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };
    private static ArrayList<FilterItem> mFilterItems;
    private static Context sContext;
    private static FilterActivity sInstance;
    private static RecyclerView mFilterListRecyclerView;
    private static RecyclerView mFilterOptionsRecyclerView;
    private FilterOptionsAdapter mOptionsAdapter;


    private  int genderflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);



        String cid = getIntent().getStringExtra("cat_id");
        initData(cid);

        // Filters List Recycler View
        mFilterListRecyclerView = (RecyclerView) findViewById(R.id.recycler_filter_list);
        mFilterListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager filterLayoutManager = new LinearLayoutManager(this);
        mFilterListRecyclerView.setLayoutManager(filterLayoutManager);
        sContext = this;
        sInstance = this;

        // Filter Options Recycler View
        mFilterOptionsRecyclerView = (RecyclerView) findViewById(R.id.recycler_filter_options);
        mFilterOptionsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager optionsLayoutManager = new LinearLayoutManager(this);
        mFilterOptionsRecyclerView.setLayoutManager(optionsLayoutManager);
        mOptionsAdapter = new FilterOptionsAdapter(this);
        mFilterOptionsRecyclerView.setAdapter(mOptionsAdapter);

        //Control Buttons
        Button btnClear = (Button) findViewById(R.id.btn_clearfilter);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionsAdapter.clearData();
            }
        });
    }

    private void initData(String cid) {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(this, AppConstants.CODE_FOR_FILTERDATA);
        JSONObject catejson = new JSONObject();
        try {
            catejson.put("cat_id",cid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gogglesAsynctask.execute(catejson.toString());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void parseJson(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            try {
                JSONArray jsonArray = receiveJSon.getJSONArray(AppConstants.FILTER_ATTRIBUTES);
                mFilterItems = new ArrayList<>(jsonArray.length());
                FilterItem filterItem;

                for (int i= 0; i< jsonArray.length(); ++i) {
                    JSONObject filterJson = jsonArray.getJSONObject(i);
                    JSONObject optionsJson = filterJson.getJSONObject(AppConstants.FILTER_OPTIONS);

                    filterItem = new FilterItem();

                    String filterName = filterJson.getString(AppConstants.FILTER_LABEL);
                    ArrayList<String> filterCategories = new ArrayList<>(optionsJson.length());
                    Iterator<String> keys = optionsJson.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        filterCategories.add(key);
                    }

                    //add data to filterItem
                    filterItem.setFilterName(filterName);
                    filterItem.setFilterCategories(filterCategories);

                    mFilterItems.add(filterItem);
                }

            } catch (JSONException je) {
                je.printStackTrace();
                Log.e(LOG_TAG, "Error in parsing Filter Item");
            }
            finally {
                if (mFilterItems != null && mFilterItems.size() > 0) {
                    FilterAdapter adapter = new FilterAdapter(sContext, mFilterItems, sInstance);
                    mFilterListRecyclerView.setAdapter(adapter);
                }
            }
//            sInstance.reciveJson = receiveJSon;
//            sInstance.msgview.setVisibility(View.GONE);
//            sInstance.displayData(receiveJSon);


        }else{
//            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
//            sInstance.linear_btnsave.setVisibility(View.GONE);
        }

    }


    @Override
    // Update OptionsAdapter's data when filter is selected
    public void onSelected(Object o) {
        if ((o != null) && (o instanceof ArrayList)) {
            mOptionsAdapter.setDataList((ArrayList<String>) o);
            Log.d(LOG_TAG, "filter selected");
        }
    }
}
