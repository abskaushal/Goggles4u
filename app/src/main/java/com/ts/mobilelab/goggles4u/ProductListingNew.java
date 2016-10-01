package com.ts.mobilelab.goggles4u;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.paging.gridview.PagingGridView;
import com.ts.mobilelab.goggles4u.adapter.SortGridAdapter;
import com.ts.mobilelab.goggles4u.core.GogglesManager;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.data.ProductData;
import com.ts.mobilelab.goggles4u.net.NetAsyncTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListingNew extends AppCompatActivity {

    private Context mContext;
    SortGridAdapter mSortGridAdapter;

    private ArrayList<ProductData> mProductList = new ArrayList<ProductData>();
    private TextView filter, sorting;
    private TextView menview, womenView, kidsView;

    private int mCurrentPage = 0;
    private int mPreviousTotal = 0;
    private boolean mLoading = true;
    private boolean mLastPage = false;

    private int mEachTimeItem = 14;
    private int mTotalitemCount = 0;
    private int pagercount = 1;
    View loadMoreView;

    private PagingGridView gridViewPaging;
    private JSONObject sortingJson;
    private PreferenceData mPreferenceData;
    TextView notifCountView, emptyView;
    static int mNotifCount = 0;
    String query = null;
    SearchView searchView;
    String selectedSortOptn, selectedType;
    String filterData;

    private GoogleApiClient client;
    int searchflag = 0;
 int mPaginationenableFlag =0;
    private static ProductListingNew sInstance;

    String optionAry[];
    int flag;// 1-for listing normal 2-for filter listing  3-for sortdata listing
    // private ArrayList<ProductData> mSortingList;
    String categoryId, intentfrom;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("category_Id", categoryId);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        categoryId = savedInstanceState.getString("category_Id");

        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle();


        emptyView = (TextView) findViewById(R.id.empty);
        gridViewPaging = (PagingGridView) findViewById(R.id.gridView);
        gridViewPaging.setEmptyView(emptyView);
        loadMoreView = ((LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loadmore, null, false);
        intentfrom = getIntent().getStringExtra("fromintent");


        if (savedInstanceState == null) {

            categoryId = getIntent().getStringExtra("cat_id");
        } else {

            categoryId = savedInstanceState.getString("category_Id");
        }
        filter = (TextView) findViewById(R.id.tv_filter);
        sorting = (TextView) findViewById(R.id.tv_sort);

        filter.setOnClickListener(filterListener);
        sorting.setOnClickListener(sortingListener);
        gridViewPaging.setHasMoreItems(true);
        handleIntent(getIntent());

        Log.v("onCreate", "sInstance" + sInstance);
        //Log.v("categoryId", "213" + categoryId);
        filterData = getIntent().getStringExtra("FilterValue");


        if (filterData != null && !filterData.isEmpty()) {
            //filterSubmitserverCall();
        } else if ("category".equals(intentfrom)) {
            init();
        }

        if (!mPreferenceData.getCartQuoteID().isEmpty()) {

            mNotifCount = Integer.parseInt(mPreferenceData.getCartItemCount());

        } else {
            mNotifCount = 0;
        }


        gridViewPaging.setPagingableListener(new PagingGridView.Pagingable() {

            @Override
            public void onLoadMoreItems() {
                Log.v("onLoadMoreItems pagercount", pagercount + "mTotalitemCount" + mTotalitemCount);
                 Log.v("flag",""+flag);
                if (flag == 1) {
                    if (pagercount < mTotalitemCount) {
                        NetAsyncTask gogglesAsynctask = new NetAsyncTask(mContext, AppConstants.CODE_FOR_PRODUCTLISTING, flag);
                        JSONObject productjson = new JSONObject();
                        try {
                            productjson.put("cat_id", categoryId);
                            productjson.put("p", pagercount);
                            productjson.put("c", mEachTimeItem);
                            productjson.put("sort", "price");
                            productjson.put("dir", "asc");
                            if (mPreferenceData.isLogincheck()) {
                                productjson.put("customer_id", mPreferenceData.getCustomerId());
                                productjson.put("session_id", "");
                            } else {
                                productjson.put("customer_id", 0);
                                productjson.put("session_id", mPreferenceData.getDEVICE_ID());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        gogglesAsynctask.execute(productjson.toString());

                    } else {
                        // Log.v("onFinishLoading", "");
                        gridViewPaging.onFinishLoading(false, null);
                    }
                }else if (flag == 3) { //for sorting listing
                    if (pagercount < mTotalitemCount) {

                        sortingServercall(flag);

                    } else {
                        // Log.v("onFinishLoading", "");
                        gridViewPaging.onFinishLoading(false, null);
                    }
                }
            }
        });
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

            getSupportActionBar().setTitle(query);
            if (searchView != null) {
                searchView.setQueryHint(query);
            }
            searchflag++;
            sendResulttotServer(query);  //for search query


        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        invalidateOptionsMenu();
        if (!mPreferenceData.getCartQuoteID().isEmpty()) {
            mNotifCount = Integer.parseInt(mPreferenceData.getCartItemCount());
        } else {
            mNotifCount = 0;
        }

    }
    private void init() {
        Log.v("init", "sInstance" + sInstance);
        NetAsyncTask gsAsynctask = new NetAsyncTask(mContext, AppConstants.CODE_FOR_PRODUCTLISTING, mPaginationenableFlag);
        JSONObject productjson = new JSONObject();
        //{"cat_id":18,"p":1,"c":15,"sort":"price","dir":"asc"}
        try {
            productjson.put("cat_id", categoryId);
            productjson.put("p", pagercount);
            productjson.put("c", mEachTimeItem);
            productjson.put("sort", "price");
            productjson.put("dir", "asc");
            if (mPreferenceData.isLogincheck()) {
                productjson.put("customer_id", mPreferenceData.getCustomerId());
                productjson.put("session_id", "");
            } else {


                // Log.d("Android","Android ID : "+android_id);
                Log.d("DEVICE_ID()", " " + mPreferenceData.getDEVICE_ID());

                productjson.put("customer_id", 0);
                productjson.put("session_id", mPreferenceData.getDEVICE_ID());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gsAsynctask.execute(productjson.toString());
    }



    private View.OnClickListener filterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(mContext, NewFilterActivity.class).putExtra("cat_id", categoryId), 111);
        }
    };
    private View.OnClickListener sortingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GogglesManager.getInstance().getProductDataArrayList().clear();
            startActivityForResult(new Intent(mContext, SortingActivity.class), AppConstants.CODE_FOR_SORTINGCLASS);


        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("from filter resultCode", resultCode + "requestCode" + requestCode);
        // GridAdapter mGridAdapter = new GridAdapter(mContext,null);
        pagercount = 1;
        if (requestCode == AppConstants.CODE_FOR_SORTINGCLASS) { //from sorting
            Log.v("from sorting", "");
            // displayData();
            if (resultCode == RESULT_OK) {
                selectedType = data.getStringExtra("type");
                selectedSortOptn = data.getStringExtra("sortoptions");
                sortingServercall(flag);
                flag = 3;
            }
        } else if (requestCode == 111) {  //from filter
            Log.v("from filter", "");
            if (resultCode == RESULT_OK) {
                filterData = data.getStringExtra("FilterValue");

                filterSubmitserverCall();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void filterSubmitserverCall() {

        try {
            JSONArray filterdataary = new JSONArray(filterData);
            JSONObject filtrjson = new JSONObject();

            filtrjson.put("cat_id", categoryId);
            filtrjson.put("p", pagercount);
            filtrjson.put("c", mEachTimeItem);
            filtrjson.put("dir", "asc");
            filtrjson.put("sort", "price");
            filtrjson.put("Filter", filterdataary);
            // productjson.put("searchkey", query);
            if(query != null && !query.isEmpty()){
                filtrjson.put("searchkey", query);
            }

            if (mPreferenceData.isLogincheck()) {
                filtrjson.put("customer_id", mPreferenceData.getCustomerId());
                filtrjson.put("session_id", "");
            } else {
                filtrjson.put("customer_id", 0);
                filtrjson.put("session_id", mPreferenceData.getDEVICE_ID());

            }
            //sortjson.put("sortkey", selctoptionsList);

            Log.v("filtrjson", "" + filtrjson);
            NetAsyncTask gogglesAsynctask = new NetAsyncTask(mContext, AppConstants.CODE_FOR_FILTERDATA_SUBMIT,mPaginationenableFlag);

            gogglesAsynctask.execute(filtrjson.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void sendResulttotServer(String query) {  //for search query

        NetAsyncTask gosynctask = new NetAsyncTask(mContext, AppConstants.CODE_FOR_SORTOPTION_SUBMIT,mPaginationenableFlag);
        JSONObject productjson = new JSONObject();
        //{"cat_id":18,"p":1,"c":15,"sort":"price","dir":"asc"}
        try {
            productjson.put("cat_id", categoryId);
            productjson.put("p", pagercount);
            productjson.put("c", mEachTimeItem);
            productjson.put("sort", "price");
            productjson.put("searchkey", query);
            productjson.put("dir", "asc");
            if (mPreferenceData.isLogincheck()) {
                productjson.put("customer_id", mPreferenceData.getCustomerId());
                productjson.put("session_id", "");
            } else {
                productjson.put("customer_id", 0);
                productjson.put("session_id", mPreferenceData.getDEVICE_ID());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gosynctask.execute(productjson.toString());

    }
    private void sortingServercall(int flag) {
        JSONObject sortjson = new JSONObject();
        try {
            sortjson.put("cat_id", categoryId);
            sortjson.put("p", pagercount);
            sortjson.put("c", mEachTimeItem);
            sortjson.put("sort", selectedSortOptn);
            sortjson.put("dir", selectedType);
            if(query != null && !query.isEmpty()){
                sortjson.put("searchkey", query);
            }


            if (mPreferenceData.isLogincheck()) {
                sortjson.put("customer_id", mPreferenceData.getCustomerId());
                sortjson.put("session_id", "");
            } else {
                sortjson.put("customer_id", 0);
                sortjson.put("session_id", mPreferenceData.getDEVICE_ID());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("sortjson", "" + sortjson);
        NetAsyncTask gogglesAsynctask = new NetAsyncTask(mContext, AppConstants.CODE_FOR_SORTOPTION_SUBMIT,flag);

        gogglesAsynctask.execute(sortjson.toString());
    }
    public static void updateSortData(String result, JSONObject receiveJSon, int pageFlag) {  //sorting response
        if (result.equals(AppConstants.SUCCESSFUL)) {
            try {
                sInstance.mTotalitemCount = Integer.parseInt(receiveJSon.getString("page_count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v("updateUi", "mTotalitemCount" + sInstance.mTotalitemCount);
            sInstance.flag = 3; //for sorting 3
            sInstance.pagercount++;
           // sInstance.displaySortingData(receiveJSon);
            sInstance. displayData();

        } else {
            View empty = sInstance.gridViewPaging.getEmptyView();
            TextView ev = (TextView) empty.findViewById(R.id.empty);
            ev.setText(result);
            //Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }
    public static void updateUi(String result, JSONObject receiveJSon) {

        if (result.equals(AppConstants.SUCCESSFUL)) {
            try {

                Log.v("cat_name", "ui" + receiveJSon.getString("cat_name"));

               sInstance.mTotalitemCount = Integer.parseInt(receiveJSon.getString("page_count"));
                Log.v("updateUi", "mTotalitemCount" + sInstance.mTotalitemCount);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sInstance.flag = 1;
            sInstance.pagercount++;
            sInstance.displayData();
        } else {

            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }


    }
    private void displayData() {

        mProductList = GogglesManager.getInstance().getProductDataArrayList();
        Log.v("mProductList","displayData"+mProductList.size());
        Log.v("mSortGridAdapter","outsideside"+mSortGridAdapter);
        if (mSortGridAdapter == null) {
            Log.v("mSortGridAdapter","inside"+mSortGridAdapter);
            mSortGridAdapter = new SortGridAdapter(mContext, mProductList);
            gridViewPaging.invalidateViews();
            mSortGridAdapter.notifyDataSetChanged();
            gridViewPaging.setAdapter(mSortGridAdapter);
            //mSortGridAdapter.refresh(mProductList);

        } /*else if(mSortGridAdapter != null ) {
            mSortGridAdapter = new SortGridAdapter(mContext, mProductList);
            gridViewPaging.invalidateViews();
            mSortGridAdapter.notifyDataSetChanged();
            gridViewPaging.setAdapter(mSortGridAdapter);

        }*/else{
            gridViewPaging.onFinishLoading(true, mProductList);
        }
    }

    public static void updateFilterData(String result, JSONObject receiveJSon, int pageFlag) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.flag = 2; //for filter 2
            sInstance.pagercount++;
            sInstance.displaySortingData(receiveJSon);
        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }
    JSONArray homeAry = new JSONArray();
    //tempJsonAray = new JSONArray();
    private SortGridAdapter mGridAdapterSort;
    private void displaySortingData(JSONObject receiveJSon) {

        JSONArray resultjsonary = null;
        try {
            sInstance.mTotalitemCount = Integer.parseInt(receiveJSon.getString("page_count"));
            JSONArray tempJsonAray = receiveJSon.getJSONArray("data");
            //Log.v("displaySortData", "homeAry" + homeAry.length());
            //Log.v("displaySata", "tempJsonAray" + tempJsonAray.length());
            if (homeAry != null && homeAry.length() != 0) {
                // JSONArray  tempJsonAray = receiveJSon.getJSONArray("data");
                for (int i = 0; i < tempJsonAray.length(); i++) {
                    homeAry.put(tempJsonAray.get(i));
                }
            } else {
                homeAry = tempJsonAray;
            }

            Log.v("afteradd", "homeAry" + homeAry.length());
            for (int i = 0; i < homeAry.length(); i++) {
                JSONObject datajson = homeAry.getJSONObject(i);
                ProductData productData = new ProductData();
                productData.setProductId(datajson.getString("id"));
                productData.setProductName(datajson.getString("name"));
                productData.setProductSku(datajson.getString("sku"));
                productData.setPrice(datajson.getString("price"));
                productData.setFormated_given_price(datajson.getString("formated_price"));
                productData.setspecial_price(datajson.getString("special_price"));
                productData.setSmallimg_url(datajson.getString("small_imageurl"));
                productData.setProductMarked(Boolean.parseBoolean(datajson.getString("marked")));
                mProductList.add(productData);
            }

            if (mGridAdapterSort == null) {
                mGridAdapterSort = new SortGridAdapter(mContext, mProductList);
                gridViewPaging.setAdapter(mGridAdapterSort);
            } else {
                gridViewPaging.onFinishLoading(true, mProductList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home, menu);
        final View menu_view = menu.findItem(R.id.menu_chkout).getActionView();
        notifCountView = (TextView) menu_view.findViewById(R.id.actionbar_notifcation_textview);
        updateHotCount(mNotifCount);
        menu_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ShoppingCartActivity.class));
            }
        });
        //
        final MenuItem myActionMenuItem = menu.findItem(R.id.menu_search);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        MenuItemCompat.setActionView(myActionMenuItem, searchView);
        searchView.setQueryHint(query);
        //finish();
        return true;
    }


    public void updateHotCount(final int new_hot_number) {
        mNotifCount = new_hot_number;
        if (notifCountView == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (new_hot_number == 0)
                    notifCountView.setVisibility(View.INVISIBLE);
                else {
                    notifCountView.setVisibility(View.VISIBLE);
                    notifCountView.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }



}
