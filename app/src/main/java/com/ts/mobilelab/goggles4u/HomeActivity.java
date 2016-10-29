package com.ts.mobilelab.goggles4u;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.ts.mobilelab.goggles4u.adapter.BannerImageAdapter;
import com.ts.mobilelab.goggles4u.adapter.CollectionAdapter;
import com.ts.mobilelab.goggles4u.adapter.CollectionImageAdapter;
import com.ts.mobilelab.goggles4u.adapter.ExpandableListAdapter;
import com.ts.mobilelab.goggles4u.adapter.HorizontalListAdapter;
import com.ts.mobilelab.goggles4u.apis.IWebService;
import com.ts.mobilelab.goggles4u.apis.WebData;
import com.ts.mobilelab.goggles4u.apis.WebServiceAsync;
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
        NavigationView.OnNavigationItemSelectedListener, IWebService {
    private ViewPager mViewPagerBanner;
    private TextView userName;
    private LinearLayout signInLinear, logoutLinear;
    private Button signUpBtn, loginBtn, logoutBtn, inviteFriendbtn;
    private LinearLayout menu_home, menu_myacnt, menu_contactus, menu_myorder, menu_helpcenter, menu_upload_prescription, menu_favlist;
    private ImageView imagereload;
    private Context mContext;
    private static final String STATE_POSITION = "STATE_POSITION";
    private ArrayList<NewArrivalData> newArrivalDataList;
    private ArrayList<String> bannerImgList;
    private CirclePageIndicator mPageindicator;
    private ArrayList<CollectionImageData> colectionList;
    private PreferenceData mPreferenceData;
    private DrawerLayout drawer;
    static int i = 0;
    private Handler mHandler;
    private ExpandableListView drawerexpandList;
    private ExpandableListAdapter expandlistAdapter;
    private LinearLayout homelt;
    private RelativeLayout nwlt;
    private ProgressBar mProgresbar;
    private TextView notifCountView;
    private static int mNotifCount = 0;
    private HashMap<String, ArrayList<ChildData>> categoryMapData;

    HorizontalListView listview_products;
    ListView listview_collections;

    private View mFooterView;
    public static final int RESULT_LOGIN = 100;
    private JSONObject homejson;
    private String cachflagserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        bannerImgList = new ArrayList<>();
        newArrivalDataList = new ArrayList<>();
        colectionList = new ArrayList<>();
        mPreferenceData = new PreferenceData();
        if (Build.VERSION.SDK_INT >= 23)
            PermissionUtils.verifyPermissions(HomeActivity.this);


        initUi();
        setUpNavigationDrawer();
        setListeners();
        getHomeData();

        if (!mPreferenceData.getCartQuoteID().isEmpty()) {
            mNotifCount = Integer.parseInt(mPreferenceData.getCartItemCount());
        } else {
            mNotifCount = 0;
        }

    }


    /**
     * Initialize the UI widgets.
     */
    private void initUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        bannerImgList = new ArrayList<>();
        colectionList = new ArrayList<CollectionImageData>();
        mHandler = new Handler();
        getSupportActionBar().setTitle("Home");


        listview_collections = (ListView) findViewById(R.id.listviewRecycler);
        View homeHeaderView = LayoutInflater.from(this).inflate(R.layout.home_activity_header, listview_collections, false);
        listview_collections.addHeaderView(homeHeaderView);
        listview_products = (HorizontalListView) homeHeaderView.findViewById(R.id.listview);
        mViewPagerBanner = (ViewPager) homeHeaderView.findViewById(R.id.viewPager_home);
        mPageindicator = (CirclePageIndicator) homeHeaderView.findViewById(R.id.indicator);
        homelt = (LinearLayout) homeHeaderView.findViewById(R.id.linearhome_header);
        nwlt = (RelativeLayout) findViewById(R.id.reltive_nwlt);
        imagereload = (ImageView) findViewById(R.id.imv_reload);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.setDrawerListener(mDrawerListener);
        toggle.syncState();
    }

    /**
     * Set up the navigation drawer.
     */
    private void setUpNavigationDrawer() {
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

        userName = (TextView) headerView.findViewById(R.id.tv_nav_profilename);
        signInLinear = (LinearLayout) headerView.findViewById(R.id.signin_linear);
        logoutLinear = (LinearLayout) headerView.findViewById(R.id.logout_linear);
        signUpBtn = (Button) headerView.findViewById(R.id.sign_up);
        loginBtn = (Button) headerView.findViewById(R.id.login);
        logoutBtn = (Button) headerView.findViewById(R.id.logout);
        inviteFriendbtn = (Button) headerView.findViewById(R.id.invite_friend);
        mProgresbar = (ProgressBar) headerView.findViewById(R.id.progressBar_banner);
        drawerexpandList = (ExpandableListView) headerView.findViewById(R.id.lvexp_categ);
        mFooterView = View.inflate(HomeActivity.this, R.layout.home_menu_view, null);
        drawerexpandList.addFooterView(mFooterView);

        menu_home = (LinearLayout) headerView.findViewById(R.id.nav_home);
        menu_myacnt = (LinearLayout) mFooterView.findViewById(R.id.nav_my_account);
        menu_myorder = (LinearLayout) mFooterView.findViewById(R.id.nav_myorder);
        menu_contactus = (LinearLayout) mFooterView.findViewById(R.id.nav_rateus);
        menu_helpcenter = (LinearLayout) mFooterView.findViewById(R.id.nav_help);
        menu_upload_prescription = (LinearLayout) mFooterView.findViewById(R.id.nav_upload_prescription);
        menu_favlist = (LinearLayout) mFooterView.findViewById(R.id.nav_favorite);

        menu_home.setOnClickListener(homeListener);
        menu_myacnt.setOnClickListener(myacntListener);
        menu_myorder.setOnClickListener(myorderListener);
        menu_favlist.setOnClickListener(fabListener);
        menu_helpcenter.setOnClickListener(helpListener);
        menu_upload_prescription.setOnClickListener(uploadPrescriptionHandler);

    }


    /**
     * Set the listeners on buttons and widgets
     */
    private void setListeners() {
        drawerexpandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                closeDrawer();
                String f = (String) v.getTag();

                startActivity(new Intent(mContext, ProductListingActivity.class).putExtra("cat_id", f).putExtra("fromintent", "category"));

                return true;
            }
        });

        loginBtn.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View v) {
                closeDrawer();
                startActivityForResult(new Intent(mContext, Login.class), RESULT_LOGIN);
            }
        });
        signUpBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegistrationActivity.class));
            }
        });
        logoutBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mPreferenceData.setLogincheck(false);
                mPreferenceData.setCustomerFName("");
                mPreferenceData.setCustomerLName("");
                mPreferenceData.setCustomerId("");
                mPreferenceData.setCartQuoteID("");
                mPreferenceData.setBillingAdrss("");

                closeDrawer();
                Toast.makeText(HomeActivity.this, "Successfully Logged out!", Toast.LENGTH_SHORT).show();
                loginLogoutcheck();

            }
        });
        loginLogoutcheck();


        listview_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String skuid = newArrivalDataList.get(position).getProductSku();
                startActivity(new Intent(mContext, ProductDetailsActivity.class).putExtra("skuid", skuid));
            }
        });
        imagereload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getHomeData();
            }
        });
    }


    private void setCategoryData(JSONObject receiveJSon) {
        homejson = receiveJSon;

    }

    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerStateChanged(int status) {

        }

        @Override
        public void onDrawerSlide(View view, float slideArg) {

        }

        @Override
        public void onDrawerOpened(View view) {

            if (homejson != null) {
                try {
                    cachflagserver = homejson.getString("navcache");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (mPreferenceData.getCategoryData().isEmpty()) {
                initCategoryData();

            } else if (homejson != null) {
                if (!cachflagserver.equals(mPreferenceData.getCategorycacheFlag())) {
                    initCategoryData();
                } else {
                    mProgresbar.setVisibility(View.GONE);
                    showCategoryData();
                }
            } else {
                mProgresbar.setVisibility(View.GONE);
                showCategoryData();
            }

        }

        @Override
        public void onDrawerClosed(View view) {
        }
    };

    private void showCategoryData() {
        String catgData = mPreferenceData.getCategoryData();
        HashMap<String, ArrayList<ChildData>> hashMap = new HashMap<>();

        try {
            JSONArray resultAry = new JSONArray(catgData);
            for (int i = 0; i < resultAry.length(); i++) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesManager.getInstance().setCategoryMap(hashMap);
        displayCategoryData();
    }


    private void initCategoryData() {

        WebServiceAsync categoryAsync = new WebServiceAsync(HomeActivity.this, HomeActivity.this, AppConstants.CODE_FOR_CATEGORY);
        categoryAsync.execute();

    }


    void loginLogoutcheck() {
        if (mPreferenceData.isLogincheck()) {


            userName.setVisibility(View.VISIBLE);
            logoutLinear.setVisibility(View.VISIBLE);
            signInLinear.setVisibility(View.GONE);
            userName.setText("Hi" + " " + mPreferenceData.getCustomerFName());

        } else {
            logoutLinear.setVisibility(View.GONE);
            signInLinear.setVisibility(View.VISIBLE);
            userName.setText("Hi Guest");

        }

    }


    private void showHomeData() {
        bannerImgList = GogglesManager.getInstance().getBannerImgList();
        newArrivalDataList = GogglesManager.getInstance().getNewArrivalDataList();
        colectionList = GogglesManager.getInstance().getImageDataList();

        mViewPagerBanner.setAdapter(new BannerImageAdapter(mContext, bannerImgList, "fromhome"));
        listview_products.setAdapter(new HorizontalListAdapter(mContext, newArrivalDataList));
        listview_collections.setAdapter(new CollectionImageAdapter(mContext, colectionList));
        mPageindicator.setViewPager(mViewPagerBanner);
        mPageindicator.setFillColor(getResources().getColor(R.color.maincolor));

    }

    /**
     * Get the home data from webservices
     */
    private void getHomeData() {
        WebServiceAsync webServiceAsync = new WebServiceAsync(HomeActivity.this, HomeActivity.this, AppConstants.CODE_FOR_HOME);
        webServiceAsync.execute();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
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


        if (item.getItemId() == R.id.menu_chkout) {
            startActivity(new Intent(mContext, ShoppingCartActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myorder) {

        } else if (id == R.id.nav_my_account) {


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private OnClickListener homeListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };

    private OnClickListener myacntListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
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
            closeDrawer();
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
            closeDrawer();
            startActivity(new Intent(mContext, FavListActivity.class));
        }
    };

    private OnClickListener helpListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
            startActivity(new Intent(mContext, HelpCenterActivity.class));
        }
    };
    //contactListener
    private OnClickListener contactListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
            startActivity(new Intent(mContext, ContactUs.class));
        }
    };
    //upload prescription Handler
    private OnClickListener uploadPrescriptionHandler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
            if (mPreferenceData.isLogincheck()) {
                startActivity(new Intent(mContext, UploadPrescriptionActivity.class));
            } else {
                showLoginDialog();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        homelt.setVisibility(View.VISIBLE);
        mViewPagerBanner.setAdapter(new BannerImageAdapter(mContext, bannerImgList, "fromhome"));
        listview_products.setAdapter(new HorizontalListAdapter(mContext, newArrivalDataList));
        listview_collections.setAdapter(new CollectionImageAdapter(mContext, colectionList));
        invalidateOptionsMenu();
        if (!mPreferenceData.getCartQuoteID().isEmpty()) {

            mNotifCount = Integer.parseInt(mPreferenceData.getCartItemCount());
        } else {
            mNotifCount = 0;
        }
    }

    ArrayList<String> headerList = new ArrayList<>();

    private void displayCategoryData() {
        categoryMapData = GogglesManager.getInstance().getCategoryMap();

        if (headerList != null) {
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

    public void updateUi(WebData data) {

        if (data.getResult().equals(AppConstants.SUCCESSFUL)) {
            nwlt.setVisibility(View.GONE);
            homelt.setVisibility(View.VISIBLE);
            showHomeData();
            setCategoryData(data.getReceiveJson());

        } else {
            homelt.setVisibility(View.GONE);
            nwlt.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "" + data.getResult(), Toast.LENGTH_LONG).show();
        }
    }


    public void updateCategoryData(String result) {

        if (result.equals(AppConstants.SUCCESSFUL)) {
            mProgresbar.setVisibility(View.GONE);
            displayCategoryData();
        } else {
            Toast.makeText(getApplicationContext(), "" + result, Toast.LENGTH_LONG).show();
        }
    }


    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Login")
                .setMessage("Please login first to upload the prescription")
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Close the drawer if opened.
     */
    private void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOGIN) {
            loginLogoutcheck();
        }

    }

    @Override
    public void onDataReceived(WebData data) {
        if (data.getCode() == AppConstants.CODE_FOR_HOME) {
            updateUi(data);
        } else if (data.getCode() == AppConstants.CODE_FOR_CATEGORY) {
            updateCategoryData(data.getResult());
        }
    }
}
