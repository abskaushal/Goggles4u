package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.filter.FilterAdapter;
import com.ts.mobilelab.goggles4u.filter.FilterItem;
import com.ts.mobilelab.goggles4u.filter.FilterOptionItem;
import com.ts.mobilelab.goggles4u.filter.FilterOptionsAdapter;
import com.ts.mobilelab.goggles4u.filter.GetFilterAsync;
import com.ts.mobilelab.goggles4u.filter.IFilterData;
import com.ts.mobilelab.goggles4u.i.ICallback;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FilterActivity extends AppCompatActivity implements ICallback, IFilterData {

    public static final String LOG_TAG = FilterActivity.class.getSimpleName();
    private static ArrayList<FilterItem> mFilterItems;
    private static Context sContext;
    private static FilterActivity sInstance;
    private static RecyclerView mFilterListRecyclerView;
    private static RecyclerView mFilterOptionsRecyclerView;
    private FilterOptionsAdapter mOptionsAdapter;
    private String cid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);


        cid = getIntent().getStringExtra("cat_id");
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
        Button btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFilter();
            }
        });

        Button applyFilter = (Button) findViewById(R.id.btn_apply);
        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });
    }

    private void initData(String cid) {
        GetFilterAsync gogglesAsynctask = new GetFilterAsync(FilterActivity.this, this);
        JSONObject catejson = new JSONObject();
        try {
            catejson.put("cat_id", cid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gogglesAsynctask.execute(catejson.toString());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // Update OptionsAdapter's data when filter is selected
    public void onSelected(Object o, boolean multiSelect) {
        if ((o != null) && (o instanceof ArrayList)) {
            mOptionsAdapter.setDataList((ArrayList<FilterOptionItem>) o, multiSelect);
        }
    }

    @Override
    public void onFilterDataReceived(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            try {
                JSONArray jsonArray = receiveJSon.getJSONArray(AppConstants.FILTER_ATTRIBUTES);
                mFilterItems = new ArrayList<>(jsonArray.length());
                FilterItem filterItem;
                FilterOptionItem optionItem;

                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject filterJson = jsonArray.getJSONObject(i);
                    JSONObject optionsJson = filterJson.getJSONObject(AppConstants.FILTER_OPTIONS);

                    filterItem = new FilterItem();

                    String filterName = filterJson.getString(AppConstants.FILTER_LABEL);
                    String fCode = filterJson.getString(AppConstants.FILTER_CODE);
                    String fAttributeId = filterJson.getString(AppConstants.FILTER_ATTRIBUTE_ID);
                    String fInputType = filterJson.getString(AppConstants.FILTER_INPUT_TYPE);
                    ArrayList<FilterOptionItem> filterCategories = new ArrayList<>(optionsJson.length());
                    Iterator<String> keys = optionsJson.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = optionsJson.getString(key);
                        optionItem = new FilterOptionItem(key, value, false);
                        filterCategories.add(optionItem);
                    }

                    //add data to filterItem
                    filterItem.setFilterName(filterName);
                    filterItem.setCode(fCode);
                    filterItem.setAttributeId(fAttributeId);
                    filterItem.setInputType(fInputType);
                    filterItem.setFilterCategories(filterCategories);

                    mFilterItems.add(filterItem);
                }

            } catch (JSONException je) {
                je.printStackTrace();
                Log.e(LOG_TAG, "Error in parsing Filter Item");
            } finally {
                if (mFilterItems != null && mFilterItems.size() > 0) {
                    FilterAdapter adapter = new FilterAdapter(sContext, mFilterItems, sInstance);
                    mFilterListRecyclerView.setAdapter(adapter);
                    onSelected(mFilterItems.get(0).getFilterCategories(), false);
                }
            }
        }
    }


    /**
     * Prepare the filter array based on selected filters and apply it.
     */
    private void applyFilter() {
        JSONArray filterArray = new JSONArray();
        JSONObject filterObject = null;
        boolean filled = false;
        if (mFilterItems != null) {


            for (FilterItem item : mFilterItems) {
                ArrayList<FilterOptionItem> list = item.getFilterCategories();
                //fill the select input type

                if (item.getmInputType().equals("select")) {
                    filterObject = new JSONObject();
                    for (FilterOptionItem optionItem : list) {

                        if (optionItem.isSelected()) {

                            try {
                                filterObject.put("attribute", item.getmCode());
                                filterObject.put("" + item.getmCode(), optionItem.getOptionValue());
                                filterObject.put("inputtype", "select");
                                filterArray.put(filterObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {

                    //fill the MultiSelect input type
                    StringBuffer buffer = null;
                    filled = false;
                    filterObject = new JSONObject();
                    buffer = new StringBuffer();
                    for (FilterOptionItem optionItem : list) {

                        if (optionItem.isSelected()) {
                            filled = true;
                            if (buffer.length() != 0)
                                buffer.append(",");
                            buffer.append(optionItem.getOptionValue());

                        }
                    }

                    if (filled) {
                        try {
                            filterObject.put("attribute", item.getmCode());
                            filterObject.put("" + item.getmCode(), buffer.toString());
                            filterObject.put("inputtype", "Multiselect");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        filterArray.put(filterObject);
                    }

                }

            }

        }


        if (filterArray.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please select above option to filter.", Toast.LENGTH_LONG).show();
        } else {
            Intent in = new Intent(getApplicationContext(), ProductListingActivity.class);
            in.putExtra("FilterValue", filterArray.toString());
            in.putExtra("cat_id", cid);

            startActivity(in);
        }
    }

    /**
     * Clear the filter data and reset the adapter
     */
    private void clearFilter() {
        if (mFilterItems != null) {
            for (FilterItem item : mFilterItems) {
                ArrayList<FilterOptionItem> list = item.getFilterCategories();
                for (FilterOptionItem filterOptionItem : list) {
                    filterOptionItem.setSelected(false);
                }
            }
        }

        mOptionsAdapter.notifyDataSetChanged();
    }

}
