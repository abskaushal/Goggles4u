package com.ts.mobilelab.goggles4u;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paging.gridview.PagingGridView;

import com.ts.mobilelab.goggles4u.adapter.SortGridAdapter;
import com.ts.mobilelab.goggles4u.apis.IWebService;
import com.ts.mobilelab.goggles4u.apis.ProductListAsync;
import com.ts.mobilelab.goggles4u.apis.WebData;
import com.ts.mobilelab.goggles4u.apis.WebServiceAsync;
import com.ts.mobilelab.goggles4u.core.GogglesManager;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;

import com.ts.mobilelab.goggles4u.data.ProductData;
import com.ts.mobilelab.goggles4u.data.SortData;
import com.ts.mobilelab.goggles4u.net.NetAsyncTask;
import com.ts.mobilelab.goggles4u.utils.PermissionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ProductListingActivity extends AppCompatActivity implements IWebService {

    private GridView gridView;
    private Context mContext;
    SortGridAdapter mSortGridAdapter;

    private ArrayList<ProductData> mProductList = new ArrayList<ProductData>();
    private TextView filter, sorting;
    private int mPaginationenableFlag = 0;

    private int mEachTimeItem = 14;
    private int mTotalitemCount = 0;
    private int pagercount = 1;
    View loadMoreView;
   // private static ProductListingActivity sInstance;
    private PagingGridView gridViewPaging;
    private PreferenceData mPreferenceData;
    private RelativeLayout filterRel, sortRel;
    TextView notifCountView, emptyView;
    static int mNotifCount = 0;

    private final int ASC_ID = 10;
    private final int DESC_ID = 20;
    int searchflag = 0;

    String selectedSortOptn, selectedType;
    String filterData;


    String optionAry[];
    int flag;
    /*  1-for listing normal 2-for filter listing  3-for sortdata listing */


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


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);
        mContext = this;
       // sInstance = this;
        mPreferenceData = new PreferenceData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyView = (TextView) findViewById(R.id.empty);
        gridViewPaging = (PagingGridView) findViewById(R.id.gridView);
        gridViewPaging.setEmptyView(emptyView);
        loadMoreView = ((LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loadmore, null, false);


        intentfrom = getIntent().getStringExtra("fromintent");
        if (Build.VERSION.SDK_INT >= 23)
            PermissionUtils.verifyPermissions(ProductListingActivity.this);

        if (savedInstanceState == null) {

            categoryId = getIntent().getStringExtra("cat_id");
        } else {

            categoryId = savedInstanceState.getString("category_Id");
        }
        //Log.v("categoryId", "213" + categoryId);
        filterData = getIntent().getStringExtra("FilterValue");


        if (filterData != null && !filterData.isEmpty()) {
            filterSubmitserverCall(0);
        } else if ("category".equals(intentfrom)) {
            init();
        }


        filter = (TextView) findViewById(R.id.tv_filter);
        sorting = (TextView) findViewById(R.id.tv_sort);
        filterRel = (RelativeLayout) findViewById(R.id.filter_rel);
        sortRel = (RelativeLayout) findViewById(R.id.sort_rel);

        filter.setOnClickListener(filterListener);
        sorting.setOnClickListener(sortingListener);
        filterRel.setOnClickListener(filterListener);
        sortRel.setOnClickListener(sortingListener);
        gridViewPaging.setHasMoreItems(true);
        handleIntent(getIntent());
        if (!mPreferenceData.getCartQuoteID().isEmpty()) {


            mNotifCount = Integer.parseInt(mPreferenceData.getCartItemCount());
            // Log.v("mNotifCount", "1st" + mNotifCount);
        } else {
            mNotifCount = 0;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            gridViewPaging.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    Log.v("scrolling", "");
                }
            });
        }
        gridViewPaging.setPagingableListener(new PagingGridView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                Log.v("onLoadMoreItems pagercount", pagercount + "mTotalitemCount" + mTotalitemCount);
                Log.v("flag", "" + flag);

                if (flag == 1) { //for product listing

                    if (pagercount < mTotalitemCount) {


                        //NetAsyncTask nAsynctask = new NetAsyncTask(mContext, AppConstants.CODE_FOR_PRODUCTLISTING, 1);
                        ProductListAsync nAsynctask = new ProductListAsync(mContext, ProductListingActivity.this, AppConstants.CODE_FOR_PRODUCTLISTING, 1);
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


                        nAsynctask.execute(productjson.toString());

                    } else {

                        gridViewPaging.onFinishLoading(false, null);
                    }
                } else if (flag == 2) { //for filter listing
                    if (pagercount < mTotalitemCount) {

                        filterSubmitserverCall(flag);

                    } else {

                        gridViewPaging.onFinishLoading(false, null);
                    }
                } else if (flag == 3) { //for sorting listing

                    if (pagercount < mTotalitemCount) {

                        sortingServercall(flag);

                    } else {

                        gridViewPaging.onFinishLoading(false, null);
                    }
                } else {

                    if (pagercount < mTotalitemCount) { //for search data

                        sendResulttotServer(query);

                    } else {

                        gridViewPaging.onFinishLoading(false, null);
                    }
                }
            }
        });


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

    String query = null;

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

    private void sendResulttotServer(String query) {  //for search query

        //NetAsyncTask gosynctask = new NetAsyncTask(mContext, AppConstants.CODE_FOR_SORTOPTION_SUBMIT, mPaginationenableFlag);
        ProductListAsync gosynctask = new ProductListAsync(mContext,ProductListingActivity.this,AppConstants.CODE_FOR_SORTOPTION_SUBMIT, mPaginationenableFlag );
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

    private void init() {

        //NetAsyncTask gsAsynctask = new NetAsyncTask(mContext, AppConstants.CODE_FOR_PRODUCTLISTING, mPaginationenableFlag);

        ProductListAsync productListAsync = new ProductListAsync(mContext, ProductListingActivity.this, AppConstants.CODE_FOR_PRODUCTLISTING, mPaginationenableFlag);
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

        productListAsync.execute(productjson.toString());
    }


    private View.OnClickListener filterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // startActivityForResult(new Intent(mContext, NewFilterActivity.class).putExtra("cat_id", categoryId), 111);
            startActivityForResult(new Intent(mContext, FilterActivity.class).putExtra("cat_id", categoryId), 111);
        }
    };
    private View.OnClickListener sortingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v("sortingListener", "clicked");
            GogglesManager.getInstance().getProductDataArrayList().clear();


            WebServiceAsync webServiceAsync = new WebServiceAsync(ProductListingActivity.this, ProductListingActivity.this, AppConstants.CODE_FOR_SORTOPTION);
            webServiceAsync.execute();
            //startActivityForResult(new Intent(mContext, SortingActivity.class), AppConstants.CODE_FOR_SORTINGCLASS);


        }
    };


    private void displayData() {

        mProductList = GogglesManager.getInstance().getProductDataArrayList();
        Log.v("mProductList", "displayData" + mProductList.size());
        Log.v("mSortGridAdapter", "outsideside" + mSortGridAdapter);
        if (mSortGridAdapter == null) {
            Log.v("mSortGridAdapter", "inside" + mSortGridAdapter);
            mSortGridAdapter = new SortGridAdapter(mContext, mProductList);
            //gridViewPaging.invalidateViews();
            //mSortGridAdapter.notifyDataSetChanged();
            gridViewPaging.setAdapter(mSortGridAdapter);
            //mSortGridAdapter.refresh(mProductList);

        } else {
            gridViewPaging.onFinishLoading(true, mProductList);
        }
    }

    SearchView searchView;

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

    private void showSortDialog(final ArrayList<SortData> optionList, String type) {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sort_dialog);

        final RadioGroup sortOptRg = (RadioGroup) dialog.findViewById(R.id.rg_sortopt);
        final RadioGroup sortTypesRg = (RadioGroup) dialog.findViewById(R.id.radioGroup_sort);

        Button doneBtn = (Button) dialog.findViewById(R.id.done);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel);


        RadioButton ascRb = new RadioButton(mContext);
        ascRb.setId(ASC_ID);
        ascRb.setText("Ascending");
        sortTypesRg.addView(ascRb);

        RadioButton descRb = new RadioButton(mContext);
        descRb.setText("Descending");
        descRb.setId(DESC_ID);
        sortTypesRg.addView(descRb);

        if (type.equals("asc")) {
            ascRb.setChecked(true);
            descRb.setChecked(false);
        } else {
            ascRb.setChecked(false);
            descRb.setChecked(true);
        }

        for (int i = 0; i < optionList.size(); i++) {
            RadioButton rb = new RadioButton(mContext); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setId(i);
            rb.setText(optionList.get(i).value);
            rb.setChecked(optionList.get(i).selected);
            sortOptRg.addView(rb);
        }

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sortId = sortOptRg.getCheckedRadioButtonId();
                int sortTypeId = sortTypesRg.getCheckedRadioButtonId();

                String sortType = (sortTypeId == ASC_ID ? "asc" : "desc");
                JSONObject sortdata = new JSONObject();
                try {
                    sortdata.put("selctoption", sortId);
                    sortdata.put("type", sortType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPreferenceData.setSELECTED_SORT_OPTION(sortdata.toString());

                dialog.dismiss();
                pagercount = 1;
                mPaginationenableFlag = 0;
                selectedType = sortType;
                selectedSortOptn = optionList.get(sortId).key;
                sortingServercall(0);
                flag = 3;
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void showSortOptions(JSONObject receiveJSon) {


        int selectid = 0;
        String type = "asc";
        ArrayList<SortData> optionList = new ArrayList<>();
        ;
        try {
            String saveoption = mPreferenceData.getSELECTED_SORT_OPTION();
            if (!saveoption.isEmpty()) {
                JSONObject savejson = new JSONObject(saveoption);
                selectid = savejson.getInt("selctoption");
                type = savejson.getString("type");
            }
            JSONObject datajson = receiveJSon.getJSONObject("data");
            Log.v("datajson", "" + datajson);

            Iterator<?> iterator = datajson.keys();
            SortData data;
            int id = 0;
            while (iterator.hasNext()) {
                data = new SortData();
                data.key = (String) iterator.next();
                data.value = datajson.getString(data.key);
                if (id == selectid)
                    data.selected = true;
                else
                    data.selected = false;
                id++;
                optionList.add(data);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        showSortDialog(optionList, type);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.v("from filter resultCode", resultCode + "requestCode" + requestCode);
        // GridAdapter mGridAdapter = new GridAdapter(mContext,null);
        pagercount = 1;
        mPaginationenableFlag = 0;


        if (requestCode == AppConstants.CODE_FOR_SORTINGCLASS) { //"from sorting
            Log.v("from sorting", "onActivi");
            // displayData();

            if (resultCode == RESULT_OK) {

                //gridViewPaging.invalidateViews();
                //gridViewPaging.setAdapter(null);
                mGridAdapterSort = null;
                //mSortGridAdapter = null;

                selectedType = data.getStringExtra("type");
                selectedSortOptn = data.getStringExtra("sortoptions");
                sortingServercall(0);
                flag = 3;


            }


        } else if (requestCode == 111) { //"from filter
            if (resultCode == RESULT_OK) {
                filterData = data.getStringExtra("FilterValue");

                filterSubmitserverCall(0);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void filterSubmitserverCall(int i) {

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
            /*if( mPaginationenableFlag == 1){
                filtrjson.put("mPaginationenableFlag", 1);
            }else{
                filtrjson.put("mPaginationenableFlag", 0);
            }*/
            if (query != null && !query.isEmpty()) {
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
            //  GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_FILTERDATA_SUBMIT);
           // NetAsyncTask gAsynctask = new NetAsyncTask(mContext, AppConstants.CODE_FOR_FILTERDATA_SUBMIT, mPaginationenableFlag);

            ProductListAsync gAsynctask = new ProductListAsync(mContext,ProductListingActivity.this,AppConstants.CODE_FOR_FILTERDATA_SUBMIT, mPaginationenableFlag);
            gAsynctask.execute(filtrjson.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void sortingServercall(int i) {
        JSONObject sortjson = new JSONObject();
        try {
            sortjson.put("cat_id", categoryId);
            sortjson.put("p", pagercount);
            sortjson.put("c", mEachTimeItem);
            sortjson.put("sort", selectedSortOptn);
            sortjson.put("dir", selectedType);

            if (query != null && !query.isEmpty()) {
                sortjson.put("searchkey", query);
            }


            if (mPreferenceData.isLogincheck()) {
                sortjson.put("customer_id", mPreferenceData.getCustomerId());
                sortjson.put("session_id", "");
            } else {
                sortjson.put("customer_id", 0);
                sortjson.put("session_id", mPreferenceData.getDEVICE_ID());
            }
            //sortjson.put("sortkey", selctoptionsList);
        } catch (JSONException e) {
            e.printStackTrace();
        }


       // NetAsyncTask ssynctask = new NetAsyncTask(mContext, AppConstants.CODE_FOR_SORTOPTION_SUBMIT, i);
        ProductListAsync ssynctask = new ProductListAsync(mContext,ProductListingActivity.this,AppConstants.CODE_FOR_SORTOPTION_SUBMIT, i);
        ssynctask.execute(sortjson.toString());
    }



    public void updateUi(String result, JSONObject receiveJSon) {

        if (result.equals(AppConstants.SUCCESSFUL)) {
            try {
                mTotalitemCount = Integer.parseInt(receiveJSon.getString("page_count"));
                getSupportActionBar().setTitle(receiveJSon.getString("cat_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            flag = 1;
            pagercount = pagercount + 1;
            displayData();

        } else {
            Toast.makeText(mContext, "" + result, Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (android.R.id.home == item.getItemId()) {
            finish();
        } else if (id == R.id.menu_chkout) {
            startActivity(new Intent(mContext, ShoppingCartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    JSONArray homeAry = new JSONArray();
    //tempJsonAray = new JSONArray();
    private SortGridAdapter mGridAdapterSort;

    private void displaySortingData(JSONObject receiveJSon) {

        /*JSONArray resultjsonary = null;

            sInstance.mTotalitemCount = Integer.parseInt(receiveJSon.getString("page_count"));

            JSONArray tempJsonAray = receiveJSon.getJSONArray("data");
            Log.v("displaySortData", "homeAry" + homeAry.length());
            //Log.v("displaySata", "tempJsonAray" + tempJsonAray.length());

                if (homeAry != null && homeAry.length() != 0) {
                    // JSONArray  tempJsonAray = receiveJSon.getJSONArray("data");
                    for (int i = 0; i < tempJsonAray.length(); i++) {
                        homeAry.put(tempJsonAray.get(i));
                    }
                } else {
                    homeAry = tempJsonAray;
                }

           // homeAry = tempJsonAray;
            mProductList.clear();
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
            }*/
        try {
            mProductList = GogglesManager.getInstance().getProductDataArrayList();
            String cat = receiveJSon.getString("page_count");
            //Log.v("displaySortingData","mProductList"+mProductList.size());
            //Log.v("displaySortingData", "out" + mGridAdapterSort);
          /*  mGridAdapterSort = new SortGridAdapter(mContext, mProductList);
            gridViewPaging.setAdapter(mGridAdapterSort);*/
            if (mGridAdapterSort == null) {
                Log.v("displaySortingData", "in" + mGridAdapterSort);
                mGridAdapterSort = new SortGridAdapter(mContext, mProductList);
                gridViewPaging.setAdapter(mGridAdapterSort);

            } else {
                Log.v("displaySortingData", "not" + mGridAdapterSort);
                gridViewPaging.onFinishLoading(true, mProductList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void updateSortData(String result, JSONObject receiveJSon) {  //sorting response
        if (result.equals(AppConstants.SUCCESSFUL)) {
            flag = 3; //for sorting 3
            // Log.v("pagercount",""+sInstance.pagercount);
            pagercount = pagercount + 1;
            //Log.v("pagercount",""+sInstance.pagercount);
            try {
                getSupportActionBar().setTitle(receiveJSon.getString("cat_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            displaySortingData(receiveJSon);


        } else {
            View empty = gridViewPaging.getEmptyView();
            TextView ev = (TextView) empty.findViewById(R.id.empty);
            ev.setText(result);
            //Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }

    public  void updateFilterData(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            flag = 2; //for filter 2
            pagercount++;
            displaySortingData(receiveJSon);
        } else {
            Toast.makeText(mContext, "" + result, Toast.LENGTH_LONG).show();
        }
    }

    public void updateMarkedData(String result, JSONObject sendJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {

            setFavrData(sendJSon);
            Toast.makeText(mContext, "" + result, Toast.LENGTH_LONG).show();


        } else {
            Toast.makeText(mContext, "" + result, Toast.LENGTH_LONG).show();
        }
    }

    private void setFavrData(JSONObject sendJSon) {
        //Log.v("setFavrData", "" + sendJSon);
        String productID = "";
        try {
            productID = sendJSon.getString("product_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mProductList.size(); i++) {
            if (mProductList.get(i).getProductId().equals(productID)) {
                mProductList.get(i).setProductMarked(mProductList.get(i).isProductMarked() == true ? false : true);

                // mProductList.get(i).setFavSelected(mProductList.get(i).getFavSelected() == 1? 0 : 1); //fav selected
            }

        }

        if (mSortGridAdapter != null) {
            mSortGridAdapter.refresh(mProductList);
        }
    }

    @Override
    public void onDataReceived(WebData data) {
        if (data.getCode() == AppConstants.CODE_FOR_SORTOPTION) {
            if (data.getResult().equals(AppConstants.SUCCESSFUL)) {
                showSortOptions(data.getReceiveJson());
            }
        }else if(data.getCode() == AppConstants.CODE_FOR_PRODUCTLISTING){
            updateUi(data.getResult(), data.getReceiveJson());
        }else if (data.getCode() == AppConstants.CODE_FOR_SORTOPTION_SUBMIT){
            updateSortData(data.getResult(), data.getReceiveJson());
        }else if (data.getCode() == AppConstants.CODE_FOR_FILTERDATA_SUBMIT){
            updateFilterData(data.getResult(), data.getReceiveJson());
        }else if(data.getCode() == AppConstants.CODE_FOR_MARKTOFAVRITE){
            updateMarkedData(data.getResult(),data.getSendJson());
        }else if(data.getCode() == AppConstants.CODE_FOR_UNMARKTOFAVRITE){
            updateMarkedData(data.getResult(),data.getSendJson());
        }
    }
}
