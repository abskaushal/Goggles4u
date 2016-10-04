package com.ts.mobilelab.goggles4u;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.ts.mobilelab.goggles4u.adapter.BannerImageAdapter;
import com.ts.mobilelab.goggles4u.adapter.CollectionImageAdapter;
import com.ts.mobilelab.goggles4u.adapter.ExpandableListAdapter;
import com.ts.mobilelab.goggles4u.adapter.HorizontalListAdapter;
import com.ts.mobilelab.goggles4u.core.GogglesManager;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.ChildData;
import com.ts.mobilelab.goggles4u.data.CollectionImageData;
import com.ts.mobilelab.goggles4u.data.NewArrivalData;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.logs.Logger;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.ts.mobilelab.goggles4u.utils.HorizontalListView;
import com.ts.mobilelab.goggles4u.utils.PermissionUtils;
import com.ts.mobilelab.goggles4u.utils.ViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
        // implements NavigationView.OnNavigationItemSelectedListener
{
    private ImageLoader imageLoader;
    private ViewPager mViewPagerBanner;
    private ViewPagerAdapter mViewpagerAdater;

    private ImageView profileImg;
    private TextView userName, userid;
    private  TextView userlogin, usersignup, userlogout;
    private TextView menu_home,menu_myacnt, menu_contactus,menu_myorder, menu_helpcenter, menu_upload_prescription,menu_favlist;
    private  ImageView imagereload;
    private Context mContext;
    private PagerAdapter mAdapter;
    private static final String STATE_POSITION = "STATE_POSITION";
    private HorizontalListAdapter mHorizontalListAdapter;
    private ArrayList<NewArrivalData> newArrivalDataList;
    private ArrayList<String> bannerImgList;
    private CirclePageIndicator mPageindicator;
    private ArrayList<CollectionImageData> colectionList;
    private PreferenceData mPreferenceData;
    private DrawerLayout drawer;
    static int i = 0;
    private Handler mHandler;
    private ExpandableListView drawerexpandList;
    private HashMap<String, List<String>> listDataChild;
    private ExpandableListAdapter expandlistAdapter;
    private List<String> listDataHeader;
    private LinearLayout homelt;
    private RelativeLayout nwlt;
    private ProgressBar mProgresbar;
    private TextView notifCountView;
    private static int mNotifCount = 0;
    int cartitemcount;
    private HashMap<String, ArrayList<ChildData>> categoryMapData;

    HorizontalListView listview_products, listview_collections;

    private View mFooterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        if (Build.VERSION.SDK_INT >= 23)
            PermissionUtils.verifyPermissions(HomeActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        //toolbar.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_logo));

        imageLoader = ImageLoader.getInstance();
        mViewPagerBanner = (ViewPager) findViewById(R.id.viewPager_home);

        bannerImgList = new ArrayList<>();
        colectionList = new ArrayList<CollectionImageData>();
        mHandler = new Handler();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        listview_products = (HorizontalListView) findViewById(R.id.listview);
        listview_collections = (HorizontalListView) findViewById(R.id.listview_collection);
       mPageindicator = (CirclePageIndicator) findViewById(R.id.indicator);
        homelt = (LinearLayout) findViewById(R.id.linearhome_header);
        nwlt = (RelativeLayout) findViewById(R.id.reltive_nwlt);
        imagereload = (ImageView) findViewById(R.id.imv_reload);
        //getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.ic_home_24dp));
        bannerImgList = GogglesManager.getInstance().getBannerImgList();
        newArrivalDataList = GogglesManager.getInstance().getNewArrivalDataList();
        colectionList = GogglesManager.getInstance().getImageDataList();
        Log.v("bannerImgList ndata",newArrivalDataList.size()+"rr"+bannerImgList.size());
        if(bannerImgList.size() == 0 || newArrivalDataList.size() == 0){
            init();
        }else{
            sInstance.homelt.setVisibility(View.VISIBLE);
            showHomeData();
            //mViewPagerBanner.setAdapter(new BannerImageAdapter(mContext, bannerImgList, "fromhome"));
            //listview_products.setAdapter(new HorizontalListAdapter(mContext, newArrivalDataList));
            //listview_collections.setAdapter(new CollectionImageAdapter(mContext, colectionList));
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.setDrawerListener(mDrawerListener);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_home, navigationView, false);
        navigationView.addView(headerView);
        categoryMapData = new HashMap<>();

        Logger.addRecordToLog(mPreferenceData.getCustomerFName() + " customer detail in home" + mPreferenceData.getCustomerId());

        // toggle.onDrawerOpened(navigationView);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = telephonyManager.getDeviceId();
       // Log.v("device_id", "" + device_id);
        mPreferenceData.setDEVICE_ID(device_id);

        //profileImg = (ImageView) headerView.findViewById(R.id.imv_nav_profile);
        userName = (TextView) headerView.findViewById(R.id.tv_nav_profilename);
        userid = (TextView) headerView.findViewById(R.id.tv_nav_profileid);
        userlogin = (TextView) headerView.findViewById(R.id.btn_login);
        usersignup = (TextView) headerView.findViewById(R.id.btn_signup);
        userlogout = (TextView) headerView.findViewById(R.id.btn_logout);
        mProgresbar = (ProgressBar) headerView.findViewById(R.id.progressBar_banner);
        drawerexpandList = (ExpandableListView) headerView.findViewById(R.id.lvexp_categ);
        mFooterView = View.inflate(HomeActivity.this, R.layout.home_menu_view, null);
        drawerexpandList.addFooterView(mFooterView);

       // Log.v("cartid", "ww" + mPreferenceData.getCartQuoteID());
        //Log.v("itm count", "ww" + mPreferenceData.getCartItemCount());
        if(!mPreferenceData.getCartQuoteID().isEmpty()){

            //cartitemcount = 2;
            //tv.setText(""+mPreferenceData.getCartItemCount());
            //supportInvalidateOptionsMenu();
           // Log.v("pref",""+mPreferenceData.getCartItemCount());
            mNotifCount = Integer.parseInt(mPreferenceData.getCartItemCount());
            //Log.v("mNotifCount","1st"+mNotifCount);
        }else{
            mNotifCount = 0;
        }

        drawerexpandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
               // Log.v("getTag", childPosition + "" + v.getTag());
                String f = (String) v.getTag();

                startActivity(new Intent(mContext, ProductListingActivity.class).putExtra("cat_id", f).putExtra("fromintent","category"));

                return true;
            }
        });
         menu_home = (TextView) headerView.findViewById(R.id.nav_home);
        menu_myacnt = (TextView) mFooterView.findViewById(R.id.nav_my_account);
        menu_myorder = (TextView) mFooterView.findViewById(R.id.nav_myorder);
        menu_contactus = (TextView) mFooterView.findViewById(R.id.nav_rateus);
        menu_helpcenter = (TextView) mFooterView.findViewById(R.id.nav_help);
        menu_upload_prescription = (TextView) mFooterView.findViewById(R.id.nav_upload_prescription);
        menu_favlist = (TextView) mFooterView.findViewById(R.id.nav_favorite);

        menu_home.setOnClickListener(homeListener);
        menu_myacnt.setOnClickListener(myacntListener);
        menu_myorder.setOnClickListener(myorderListener);
        menu_favlist.setOnClickListener(fabListener);
        menu_helpcenter.setOnClickListener(helpListener);
        menu_upload_prescription.setOnClickListener(uploadPrescriptionHandler);
        menu_contactus.setOnClickListener(contactListener);

        userlogin.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View v) {
                startActivity(new Intent(mContext, Login.class));
            }
        });
        usersignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegistrationActivity.class));
            }
        });
        userlogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mPreferenceData.setLogincheck(false);
                userlogout.setVisibility(View.GONE);
                //userlogin.setVisibility(View.VISIBLE);
                // usersignup.setVisibility(View.VISIBLE);
                mPreferenceData.setCustomerFName("");
                mPreferenceData.setCustomerLName("");
                mPreferenceData.setCustomerId("");
                mPreferenceData.setCartQuoteID("");
                mPreferenceData.setBillingAdrss("");

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                Toast.makeText(HomeActivity.this, "Successfully Logged out!", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(mContext, Login.class));
                //finish();
                loginLogoutcheck();

            }
        });
        loginLogoutcheck();




        listview_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String skuid = newArrivalDataList.get(position).getProductSku();
               // Log.v("skuid",""+skuid);

                startActivity(new Intent(mContext, ProductDetailsActivity.class).putExtra("skuid", skuid));
                //.putExtra("fromintent","home"));
            }
        });
        imagereload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        int pagerPosition = savedInstanceState == null ? 0 : savedInstanceState.getInt(STATE_POSITION);

    }
    JSONObject homejson;
    private  void setCategoryData(JSONObject receiveJSon){
        homejson = receiveJSon;

    }

    String cachflagserver;
    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerStateChanged(int status) {

        }

        @Override
        public void onDrawerSlide(View view, float slideArg) {

        }

        @Override
        public void onDrawerOpened(View view) {
           // Log.v("categoryMapData",""+categoryMapData.size());
            //Log.v("mPreferenceData categoryData",""+mPreferenceData.getCategoryData());
            //Log.v("getCategorycacheFlag",""+mPreferenceData.getCategorycacheFlag());
            //Log.v("homejson",""+homejson);
            if(homejson != null) {
                try {
                    cachflagserver = homejson.getString("navcache");
                  //  Log.v("cachflagserver", "" + cachflagserver);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Log.v("cachflagserver", "" + cachflagserver);
            if(mPreferenceData.getCategoryData().isEmpty()){
                //if (categoryMapData.size() == 0) {
                    initCategoryData();
               // }

            } else if(homejson != null){
                if(!cachflagserver.equals(mPreferenceData.getCategorycacheFlag())) {
                    initCategoryData();
                }else{
                    mProgresbar.setVisibility(View.GONE);
                    showCategoryData();
                }
            }else{
                mProgresbar.setVisibility(View.GONE);
                showCategoryData();
            }


            /*else  if(!cachflagserver.equals(mPreferenceData.getCategorycacheFlag())){
                initCategoryData();
            }else{
                showCategoryData();
                ///displayCategoryData();
            }*/


        }

        @Override
        public void onDrawerClosed(View view) {
        }
    };

    private void showCategoryData() {
        String catgData = mPreferenceData.getCategoryData();
        HashMap<String,ArrayList<ChildData>> hashMap = new HashMap<>();

        try{
        JSONArray resultAry = new JSONArray(catgData);
        for(int i=0;i<resultAry.length();i++) {
            JSONObject json = resultAry.getJSONObject(i);

            ArrayList<ChildData> childList = new ArrayList<>();
            JSONArray childry = json.getJSONArray("child");

            for (int j = 0; j < childry.length(); j++) {
                JSONObject childjson = childry.getJSONObject(j);
                ChildData childData = new ChildData();
                childData.setParent(childjson.getString("parent"));
                childData.setName(childjson.getString("name"));
                childData.setId(childjson.getString("id"));
                childList.add(childData);

            }
            hashMap.put(json.getString("name"), childList);
        }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesManager.getInstance().setCategoryMap(hashMap);
        displayCategoryData();
    }


    private void initCategoryData() {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_CATEGORY);

        gogglesAsynctask.execute();
    }


    void loginLogoutcheck() {
        if (mPreferenceData.isLogincheck()) {


            userName.setVisibility(View.VISIBLE);
            userid.setVisibility(View.VISIBLE);
            userName.setText("Hi" + " " + mPreferenceData.getCustomerFName());
            userid.setText(mPreferenceData.getCustomerMailId());
            userlogin.setVisibility(View.GONE);
            usersignup.setVisibility(View.GONE);
            userlogout.setVisibility(View.VISIBLE);
        } else {
            // profileImg.setVisibility(View.GONE);
            userlogout.setVisibility(View.GONE);
            userlogin.setVisibility(View.VISIBLE);
            usersignup.setVisibility(View.VISIBLE);
            userName.setText("Hi Guest");
            userid.setVisibility(View.GONE);

        }

    }


    private void showHomeData() {
        bannerImgList = GogglesManager.getInstance().getBannerImgList();
        newArrivalDataList = GogglesManager.getInstance().getNewArrivalDataList();
        colectionList = GogglesManager.getInstance().getImageDataList();

        mViewPagerBanner.setAdapter(new BannerImageAdapter(mContext, bannerImgList,"fromhome"));
        listview_products.setAdapter(new HorizontalListAdapter(mContext, newArrivalDataList));
        listview_collections.setAdapter(new CollectionImageAdapter(mContext, colectionList));
         mPageindicator.setViewPager(mViewPagerBanner);
        mPageindicator.setFillColor(getResources().getColor(R.color.maincolor));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < bannerImgList.size() - 1; i++) {
                    final int value = i;
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mViewPagerBanner.setCurrentItem(value, true);
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();

    }

    private void init() {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_HOME);
        gogglesAsynctask.execute();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //  outState.putInt(STATE_POSITION, mViewPagerBanner.getCurrentItem());
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.menu_chkout){
            startActivity(new Intent(mContext, ShoppingCartActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Fragment fragment = null;

        if (id == R.id.nav_myorder) {

        } else if (id == R.id.nav_my_account) {


        }/*else if(id== R.id.nav_logout){
            mPreferenceData.setLogincheck(false);
            finish();
            //startActivity(new Intent(mContext,LoginActivity.class));
        }*/
       /* if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            //mDrawerList.setItemChecked(position, true);
            //mDrawerList.setSelection(position);
           // setTitle(mNavigationDrawerItemTitles[position]);
            //mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }*/

        drawer.closeDrawer(GravityCompat.START);
        //drawer.addDrawerListener(new ActionBarDrawerToggle());
        return true;
    }


    private OnClickListener homeListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, HomeActivity.class));
        }
    };

    private OnClickListener myacntListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPreferenceData.isLogincheck()) {
                startActivity(new Intent(mContext, MyAccountActivity.class));
            } else {
                startActivity(new Intent(mContext, Login.class));
            }
        }
    };

    private OnClickListener myorderListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPreferenceData.isLogincheck()) {

                startActivity(new Intent(mContext, MyOrderActivity.class));
            } else {
                Toast.makeText(HomeActivity.this, getString(R.string.login_alert), Toast.LENGTH_SHORT).show();
            }

        }
    };

    private OnClickListener fabListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, FavListActivity.class));
        }
    };

    private OnClickListener helpListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, HelpCenterActivity.class));
        }
    };
    //contactListener
    private OnClickListener contactListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, ContactUs.class));
        }
    };
    //upload prescription Handler
    private OnClickListener uploadPrescriptionHandler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, UploadPrescriptionActivity.class));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sInstance.homelt.setVisibility(View.VISIBLE);
        mViewPagerBanner.setAdapter(new BannerImageAdapter(mContext, bannerImgList, "fromhome"));
        listview_products.setAdapter(new HorizontalListAdapter(mContext, newArrivalDataList));
        listview_collections.setAdapter(new CollectionImageAdapter(mContext, colectionList));
        invalidateOptionsMenu();
        if(!mPreferenceData.getCartQuoteID().isEmpty()){

            mNotifCount = Integer.parseInt(mPreferenceData.getCartItemCount());
        }else{
            mNotifCount = 0;
        }
    }

    ArrayList<String> headerList = new ArrayList<>();

    private void displayCategoryData() {
        categoryMapData = GogglesManager.getInstance().getCategoryMap();

        if(headerList != null){
            headerList.clear();
        }
        //Set<Integer> keys = categoryMapData.keySet();
        for (Map.Entry<String, ArrayList<ChildData>> entry : categoryMapData.entrySet()) {
            String keys = entry.getKey();
            ArrayList<ChildData> valueList = entry.getValue();
            headerList.add(keys);

            expandlistAdapter = new ExpandableListAdapter(mContext, categoryMapData, headerList);
            drawerexpandList.setAdapter(expandlistAdapter);

        }
    }

    public static void updateUi(String result, JSONObject receiveJSon) {

        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.nwlt.setVisibility(View.GONE);
            sInstance.homelt.setVisibility(View.VISIBLE);
            sInstance.showHomeData();

            sInstance.setCategoryData(receiveJSon);

        } else {
            sInstance.homelt.setVisibility(View.GONE);
            sInstance.nwlt.setVisibility(View.VISIBLE);
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }


    static HomeActivity sInstance;

    public static void updateCategoryData(String result) {

        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.mProgresbar.setVisibility(View.GONE);
            sInstance.displayCategoryData();
        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }
}
