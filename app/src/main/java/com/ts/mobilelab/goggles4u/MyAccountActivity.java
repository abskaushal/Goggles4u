package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.adapter.ExpandableListAdapter;
import com.ts.mobilelab.goggles4u.core.GogglesManager;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.ChildData;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.logs.Logger;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAccountActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{


    private TextView editdetailinfo,uName,uEmailid,changepswd,addBilingAddress,bilingadrs2,bilingadrs3,addshipngadrs,shipngadrs2;
    private TextView      editbilingadrs,dltbilnadrs,storecreditView,prescriptionView,shipngadrs3,editshipadrs,dltshipngadrs,addAdrsView;
    private Context mContext;
    private PreferenceData mPreferenceData;
    private String shipingadrsid,bilngadrsid;
    private CardView bilingCardView,shipingCardView,adrsview2;
    private RelativeLayout adrslt1,adrslt2;
    DrawerLayout drawer;
    TextView menu_home,menu_myacnt,menu_rateus,menu_myorder, menu_helpcenter,menu_favlist,menu_contactus;
    TextView userName, userid;
    TextView userlogin, usersignup, userlogout;
    private ExpandableListView drawerexpandList;

    Toolbar toolbar;
    TextView addbiladrsBtn, addshipadrsBtn,myorderView,bilAdrsView,shipAdrsView,addadrs;
    private static MyAccountActivity sInstance;
    ExpandableListAdapter expandlistAdapter;
    private static final String STATE_POSITION = "STATE_POSITION";
    private View mFooterView;
    private ProgressBar mProgresbar;


    private HashMap<String, ArrayList<ChildData>> categoryMapData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        mContext = this;
        sInstance = this;
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPreferenceData = new PreferenceData();
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        init();
        drawerdetails();


        editdetailinfo = (TextView) findViewById(R.id.tv_editinfo);
        uName = (TextView) findViewById(R.id.tv_uname);
        uEmailid = (TextView) findViewById(R.id.tv_uemail);
        uEmailid.setText(mPreferenceData.getCustomerMailId());
        changepswd = (TextView) findViewById(R.id.tv_chngpswd);
        addBilingAddress = (TextView) findViewById(R.id.tv_bilingaddrs);
        bilingadrs2 = (TextView) findViewById(R.id.tv_bilngadrs2);
        bilingadrs3 = (TextView) findViewById(R.id.tv_bilngadrs3);
        addshipngadrs = (TextView) findViewById(R.id.tv_editshipaddrs);
        shipngadrs2 = (TextView) findViewById(R.id.tv_shipngadrs2);
        shipngadrs3 = (TextView) findViewById(R.id.tv_shipngadrs3);
        editbilingadrs = (TextView) findViewById(R.id.tv_editadrsbil);
        dltbilnadrs = (TextView) findViewById(R.id.tv_dltadrsbil);
        dltshipngadrs = (TextView) findViewById(R.id.tv_dltadrsshipng);
        storecreditView = (TextView) findViewById(R.id.tv_storecredit);
        prescriptionView = (TextView) findViewById(R.id.tv_prescriptions);
        editshipadrs = (TextView) findViewById(R.id.tv_editshipngadrs);
        adrslt2 = (RelativeLayout) findViewById(R.id.relative_adrs2);
        bilingCardView = (CardView) findViewById(R.id.cardview_biling);
        shipingCardView = (CardView) findViewById(R.id.cardview_shiping);
        addAdrsView = (TextView) findViewById(R.id.addaddresview);
        myorderView = (TextView) findViewById(R.id.tv_myorder);
       // bilAdrsView = (TextView) findViewById(R.id.tv_biladrsview);
        shipAdrsView = (TextView) findViewById(R.id.tv_shipadrsview);
        adrslt1 = (RelativeLayout) findViewById(R.id.relative_adrs1);
        adrslt2 = (RelativeLayout) findViewById(R.id.relative_adrs2);
        addadrs = (TextView) findViewById(R.id.adresview);
        adrsview2 = (CardView) findViewById(R.id.cv_adrsview2);


        editbilingadrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mContext, EditAddress.class).putExtra("from", "editbiling"));

            }
        });
        uName.setText(mPreferenceData.getCustomerFName() + " " + mPreferenceData.getCustomerLName());

        editshipadrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, EditAddress.class).putExtra("from", "editshiping"));

            }
        });
        myorderView.setOnClickListener(myOrderListener);
        addadrs.setOnClickListener(addAdrslistener);
        addAdrsView.setOnClickListener(addAdrslistener);
        dltshipngadrs.setOnClickListener(deleteShipngAdrsListener);
        prescriptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PrescriptionsActivity.class));
            }
        });
        dltbilnadrs.setOnClickListener(dltAddressrListener);


        editdetailinfo.setOnClickListener(editinfoListener);
        changepswd.setOnClickListener(changepswdListener);


        storecreditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,StoreCreditDataActivity.class));
        }
        });
        int pagerPosition = savedInstanceState == null ? 0 : savedInstanceState.getInt(STATE_POSITION);
    }

    private void drawerdetails() {


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_myacnt);
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

        //profileImg = (ImageView) headerView.findViewById(R.id.imv_nav_profile);
        userName = (TextView) headerView.findViewById(R.id.tv_nav_profilename);
        userid = (TextView) headerView.findViewById(R.id.tv_nav_profileid);
        userlogin = (TextView) headerView.findViewById(R.id.btn_login);
        usersignup = (TextView) headerView.findViewById(R.id.btn_signup);
        userlogout = (TextView) headerView.findViewById(R.id.btn_logout);

        mProgresbar = (ProgressBar) headerView.findViewById(R.id.progressBar_banner);
        sInstance.mProgresbar.setVisibility(View.GONE);
        drawerexpandList = (ExpandableListView) headerView.findViewById(R.id.lvexp_categ);
        mFooterView = View.inflate(MyAccountActivity.this, R.layout.home_menu_view, null);
        drawerexpandList.addFooterView(mFooterView);
        displayCategoryData();


        menu_home = (TextView) headerView.findViewById(R.id.nav_home);
        menu_myacnt = (TextView) mFooterView.findViewById(R.id.nav_my_account);
        menu_myorder = (TextView) mFooterView.findViewById(R.id.nav_myorder);
        menu_contactus = (TextView) mFooterView.findViewById(R.id.nav_rateus);
        menu_helpcenter = (TextView) mFooterView.findViewById(R.id.nav_help);
        menu_favlist = (TextView) mFooterView.findViewById(R.id.nav_favorite);
        //menu_privacy = (TextView) mFooterView.findViewById(R.id.nav_privacy);

        menu_home.setOnClickListener(homeListener);
        menu_myacnt.setOnClickListener(myacntListener);
        menu_myorder.setOnClickListener(myorderListener);
        menu_favlist.setOnClickListener(fabListener);
        menu_helpcenter.setOnClickListener(helpListener);
        menu_contactus.setOnClickListener(contactListener);


        //menu_privacy.setOnClickListener(privacyListener);

        drawerexpandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.v("getTag", childPosition + "" + v.getTag());
                String f = (String) v.getTag();
                startActivity(new Intent(mContext, ProductListingActivity.class).putExtra("cat_id", f).putExtra("fromintent","category"));
                //startActivity(new Intent(mContext, NewFilterActivity.class).putExtra("cat_id", f));
                return true;
            }
        });
        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, Login.class));
            }
        });
        usersignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegistrationActivity.class));
            }
        });
        userlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPreferenceData.setLogincheck(false);
                userlogout.setVisibility(View.GONE);

                mPreferenceData.setCustomerFName("");
                mPreferenceData.setCustomerLName("");
                mPreferenceData.setCustomerId("");

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                Toast.makeText(MyAccountActivity.this, "Successfully Logged out!", Toast.LENGTH_SHORT).show();

                loginLogoutcheck();

            }
        });
        loginLogoutcheck();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    void loginLogoutcheck(){
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
    private View.OnClickListener myOrderListener = new View.OnClickListener() {
     @Override
        public void onClick(View v) {
         startActivity(new Intent(mContext,MyOrderActivity.class));
     }
    };
    private View.OnClickListener dltAddressrListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject dltjson = new JSONObject();
            try {
                dltjson.put("address_id", bilngadrsid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_DELETEADRESS);
            gogglesAsynctask.execute(dltjson.toString());
        }
    };
    private void init() {


        JSONObject addrsjson = new JSONObject();
        try {
            addrsjson.put("user_id",mPreferenceData.getCustomerId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_ADDRESSLIST);
        gogglesAsynctask.execute(addrsjson.toString());
    }

    private View.OnClickListener deleteShipngAdrsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject dltjson = new JSONObject();
            try {
                dltjson.put("address_id", shipingadrsid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_DELETEADRESS);
            gogglesAsynctask.execute(dltjson.toString());
        }
    };
    private View.OnClickListener editinfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        startActivity(new Intent(mContext, EditInformation.class));

        }
    };

    private View.OnClickListener changepswdListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext,ChangePasswordActivity.class).putExtra("from","from"));
        }
    };

    private View.OnClickListener addAdrslistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(mContext, EditAddress.class).putExtra("from", "addaddress"));
        }
    };

    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerStateChanged(int status) {

        }

        @Override
        public void onDrawerSlide(View view, float slideArg) {

        }

        @Override
        public void onDrawerOpened(View view) {
            if (categoryMapData.size() == 0) {
                showCategoryData();
            }

        }

        @Override
        public void onDrawerClosed(View view) {
        }
    };

    /*private void initCategoryData() {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_CATEGORY);

        gogglesAsynctask.execute();
    }*/
    private void showCategoryData() {
        String catgData = mPreferenceData.getCategoryData();
        HashMap<String,ArrayList<ChildData>> hashMap = new HashMap<>();

        try{
            JSONArray resultAry = new JSONArray(catgData);
            for(int i=0;i<resultAry.length();i++) {
                JSONObject json = resultAry.getJSONObject(i);

                ArrayList<ChildData> childList = new ArrayList<>();
                JSONArray childry = json.getJSONArray("child");
                Log.v("inhome childry", "11" + resultAry);
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


    public static void updateUi(String result, JSONObject receiveJSon) {

//{"Error":false,"default_billing":{"entity_id":"1","entity_type_id":"2","attribute_set_id":"0","increment_id":null,"parent_id":"1","created_at":"2016-02-13 09:04:56","updated_at":"2016-02-13 09:04:56","is_active":"1",
// "firstname":"Farrukh","lastname":"Khan","city":"Cerritos","region":"California","postcode":"90703","country_id":"US","telephone":"5629260697","region_id":"12","street":"12611 Hiddenreek Way Ste F","customer_id":"1"},
// "default_shipping":{"entity_id":"1","entity_type_id":"2","attribute_set_id":"0","increment_id":null,"parent_id":"1","created_at":"2016-02-13 09:04:56","updated_at":"2016-02-13 09:04:56","is_active":"1",
// "firstname":"Farrukh","lastname":"Khan","city":"Cerritos","region":"California","postcode":"90703","country_id":"US","telephone":"5629260697","region_id":"12","street":"12611 Hiddenreek Way Ste F","customer_id":"1"}}
        if(result.equals(AppConstants.SUCCESSFUL)){
            //String defaultbiladrs = receiveJSon.get
            PreferenceData mPreferenceData = new PreferenceData();
            try {
                String blngadrs = receiveJSon.getString("default_billing");
                Log.v("blngadrs", "222" + blngadrs);
                if(blngadrs.isEmpty()){
                    Log.v("blngadrs", "ww" + blngadrs);
                    sInstance.bilingCardView.setVisibility(View.GONE);
                    //sInstance.addbiladrsBtn.setVisibility(View.VISIBLE);

                }else{
                sInstance.adrslt1.setVisibility(View.VISIBLE);
                    sInstance.adrsview2.setVisibility(View.GONE);

                JSONObject addrsjson = new JSONObject(blngadrs);


                String biladrs = addrsjson.getString("firstname") +" " +addrsjson.getString("lastname") + "\n" + addrsjson.getString("street")+ " ," + addrsjson.getString("region")+ "\n" + addrsjson.getString("city") + "-"+addrsjson.getString("postcode") +" ," +addrsjson.getString("country_id") ;
                sInstance.bilngadrsid = addrsjson.getString("entity_id");
                sInstance.addBilingAddress.setText(biladrs);
                sInstance.bilingadrs3.setText("Mob:"+addrsjson.getString("telephone"));

                mPreferenceData.setBillingAdrss(addrsjson.toString());
                    sInstance.bilingCardView.setVisibility(View.VISIBLE);
                }

                String shipngadrs = receiveJSon.getString("default_shipping");

                if(shipngadrs.isEmpty()){
                    sInstance.shipingCardView.setVisibility(View.GONE);

                }else {
                    sInstance.adrslt1.setVisibility(View.VISIBLE);
                    sInstance.shipAdrsView.setVisibility(View.VISIBLE);
                    sInstance.adrsview2.setVisibility(View.GONE);
                    JSONObject shipngadrsjson = new JSONObject(shipngadrs);
                    sInstance.shipingadrsid = shipngadrsjson.getString("entity_id");

                    String shipadrs = shipngadrsjson.getString("firstname") + " " + shipngadrsjson.getString("lastname") + "\n" + shipngadrsjson.getString("street") + "," + shipngadrsjson.getString("region") + "\n" + shipngadrsjson.getString("city") + "-" + shipngadrsjson.getString("postcode") + " ," + shipngadrsjson.getString("country_id");


                    sInstance.addshipngadrs.setText(shipadrs);
                    sInstance.shipngadrs3.setText("Mob:" + shipngadrsjson.getString("telephone"));


                    mPreferenceData.setShippingAdrs(shipngadrsjson.toString());
                    sInstance.shipingCardView.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Logger.addRecordToLog("MyAccount : " + e.getMessage());
            }

        }
    }
    private View.OnClickListener homeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, HomeActivity.class));
        }
    };

    private View.OnClickListener myacntListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPreferenceData.isLogincheck()) {
                startActivity(new Intent(mContext, MyAccountActivity.class));
            } else {
                startActivity(new Intent(mContext, Login.class));
            }
        }
    };

    private View.OnClickListener myorderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPreferenceData.isLogincheck()) {

                startActivity(new Intent(mContext, MyOrderActivity.class));
            } else {
                Toast.makeText(mContext, getString(R.string.login_alert), Toast.LENGTH_SHORT).show();
            }

        }
    };

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, FavListActivity.class));
        }
    };

    private View.OnClickListener helpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, HelpCenterActivity.class));
        }
    };//contactListener
    private View.OnClickListener contactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, ContactUs.class));
        }
    };


    ArrayList<String> headerList = new ArrayList<>();

    private void displayCategoryData() {
        categoryMapData = GogglesManager.getInstance().getCategoryMap();


        for (Map.Entry<String, ArrayList<ChildData>> entry : categoryMapData.entrySet()) {
            String keys = entry.getKey();
            ArrayList<ChildData> valueList = entry.getValue();
            headerList.add(keys);



            expandlistAdapter = new ExpandableListAdapter(mContext, categoryMapData, headerList);
            drawerexpandList.setAdapter(expandlistAdapter);

        }
    }

    public static void updateDeleteadrsUi(String result) {

        if(result.equals(AppConstants.SUCCESSFUL)){
            Toast.makeText(sInstance,"Address Deleted",Toast.LENGTH_LONG).show();
            sInstance.mPreferenceData.setBillingAdrss("");
            sInstance.mPreferenceData.setShippingAdrs("");
        }else{
            Toast.makeText(sInstance,""+result,Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_myorder) {
            if(mPreferenceData.isLogincheck()) {
                startActivity(new Intent(mContext, MyOrderActivity.class));
            }else{
                Toast.makeText(MyAccountActivity.this, getString(R.string.login_alert), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_my_account) {
            if(mPreferenceData.isLogincheck()){
                startActivity(new Intent(mContext, MyAccountActivity.class));
            }else{
                startActivity(new Intent(mContext, Login.class));
            }

        }else if(id== R.id.nav_home){
            startActivity(new Intent(mContext, HomeActivity.class));

        }


        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}
