package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.CountryData;
import com.ts.mobilelab.goggles4u.data.RegionData;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditAddress extends AppCompatActivity {


    private EditText fstName,lastName,company,emailid,addressedt,streetaddress,city,zip,telephone,stateEt;
    private BetterSpinner stateSpinner,countryspinner;
    private RadioButton shipthisBtn,shipdiffBtn;
    private Button shipinginfoBtn,continueBtn;
    private Context mContext;
    private static EditAddress sInstance;
    private String [] stateArray,countrysArray;
    private PreferenceData mprefrenceData;
    int isdefaultbiling = 0;
    int isdefaultshipping = 0;
    String from,addressType,shipadrs;
    private Switch bilingswitch,shipingswitch;
    private String shipingAdrs;
    private ArrayList<CountryData> countryList;
    private ArrayList<String> stateList;
    private ArrayList<RegionData> regionList;
    private CheckBox saveAdrsBox;
    private int savinflag = 0;
    String slctcuntry = "US";
    LinearLayout defaultshiplt,defaultbilllt;
    private int countryslctposi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        mContext = this;
        sInstance = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        countryList = new ArrayList<>();
        regionList = new ArrayList<>();
        stateList = new ArrayList<>();
        initcountryList();
        init(slctcuntry);
        fstName = (EditText) findViewById(R.id.edt_fname);
        lastName = (EditText) findViewById(R.id.edt_lname);
        saveAdrsBox = (CheckBox) findViewById(R.id.chkbox_saveadrs);
        defaultshiplt = (LinearLayout) findViewById(R.id.linear_defultship);
        defaultbilllt = (LinearLayout) findViewById(R.id.linear_defultbill);
       // company = (EditText) findViewById(R.id.edt_company);
        emailid = (EditText) findViewById(R.id.edt_email);
       // addressedt = (EditText) findViewById(R.id.edt_address);

        streetaddress = (EditText) findViewById(R.id.edt_adrs2);
        city = (EditText) findViewById(R.id.edt_city);
        zip = (EditText) findViewById(R.id.edt_postcode);
        telephone = (EditText) findViewById(R.id.edt_telephone);
        stateEt = (EditText) findViewById(R.id.edt_state);
        stateSpinner = (BetterSpinner) findViewById(R.id.spinner_state);
        countryspinner = (BetterSpinner) findViewById(R.id.spinner_country);
        shipinginfoBtn = (Button) findViewById(R.id.btn_shipingdata);
        continueBtn = (Button) findViewById(R.id.btn_continue);
        shipthisBtn = (RadioButton) findViewById(R.id.radioButton_shipthis);
        shipdiffBtn = (RadioButton) findViewById(R.id.radioButton_shipdiff);

        bilingswitch = (Switch) findViewById(R.id.switch_bilngadrs);
        shipingswitch = (Switch) findViewById(R.id.switch_shipngadrs);
        mprefrenceData = new PreferenceData();
         from =  getIntent().getStringExtra("from");
        shipingAdrs = getIntent().getStringExtra("shipingadrs");
        Log.v("from", "in editadrs" + from);
       /* shipadrs = getIntent().getStringExtra("f");
        Log.v("shipadrs", "" + shipadrs);*/
        //addressType = getIntent().getStringExtra("type");
        //Log.v("addressType", "" + addressType);
        //stateArray = countryList.toArray(new String[regionList.size()]);
        //stateArray = getResources().getStringArray(R.array.state);
        //countryArray = getResources().getStringArray(R.array.country);
        if("editbiling".equals(from)){
            getSupportActionBar().setTitle("Edit Address");
        }else if("editshiping".equals(from)) {
            getSupportActionBar().setTitle("Edit Address");
        }else if("chkoutedit".equals(from)){
            shipingAdrs = getIntent().getStringExtra("shipingadrs");
            getSupportActionBar().setTitle("Edit Address");

        }else if("addchkoutaddress".equals(from)){
            //saveAdrsBox.setVisibility(View.VISIBLE);
            defaultshiplt.setVisibility(View.GONE);
            defaultbilllt.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Add Address");
        }else if("addshipaddress".equals(from)){
           // saveAdrsBox.setVisibility(View.GONE);
            defaultshiplt.setVisibility(View.GONE);
            defaultbilllt.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Add Shipping Address");
        }else{
            getSupportActionBar().setTitle("Add Address");
        }


        if(!mprefrenceData.isLogincheck()){
            emailid.setVisibility(View.VISIBLE);
        }


        countryspinner.setText("United State");

        countryspinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                countryslctposi = position;
                init(countryList.get(position).getCountryID());
            }

        });

      shipingswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if (isChecked) {
                  isdefaultshipping = 1;
              } else {
                  isdefaultshipping = 0;
              }
          }
      });

        bilingswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isdefaultbiling = 1;
                } else {
                    isdefaultbiling = 0;
                }
            }
        });
        saveAdrsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    savinflag = 1;
                }else{
                    savinflag = 0;
                }
            }
        });
        continueBtn.setOnClickListener(submitListener);
        if("editbiling".equals(from)){

            shipthisBtn.setVisibility(View.GONE);
            shipdiffBtn.setVisibility(View.GONE);
            String bilingadrs = mprefrenceData.getBillingAdrss();
            Log.v("bilingadrs",""+bilingadrs);
            try {
                JSONObject blngadrsjson = new JSONObject(bilingadrs);

               // String biladrs = addrsjson.getString("firstname") +" " +addrsjson.getString("lastname") + "\n" + addrsjson.getString("street")+ " \n" + addrsjson.getString("region")+ "," + addrsjson.getString("city");
                //String biladrs2 = addrsjson.getString("postcode") +" ," +addrsjson.getString("country_id") +"\n" +addrsjson.getString("telephone") ;
                fstName.setText(blngadrsjson.getString("firstname"));
                lastName.setText(blngadrsjson.getString("lastname"));
                streetaddress.setText(blngadrsjson.getString("street"));
                city.setText(blngadrsjson.getString("city"));
                stateSpinner.setText(blngadrsjson.getString("region"));
                countryspinner.setText(blngadrsjson.getString("country_id"));
                zip.setText(blngadrsjson.getString("postcode"));
                telephone.setText(blngadrsjson.getString("telephone"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if("editshiping".equals(from)) {

            shipthisBtn.setVisibility(View.GONE);
            shipdiffBtn.setVisibility(View.GONE);
            String bilingadrs = mprefrenceData.getShippingAdrs();
            Log.v("bilingadrs",""+bilingadrs);
            try {
                JSONObject blngadrsjson = new JSONObject(bilingadrs);

                // String biladrs = addrsjson.getString("firstname") +" " +addrsjson.getString("lastname") + "\n" + addrsjson.getString("street")+ " \n" + addrsjson.getString("region")+ "," + addrsjson.getString("city");
                //String biladrs2 = addrsjson.getString("postcode") +" ," +addrsjson.getString("country_id") +"\n" +addrsjson.getString("telephone") ;
                fstName.setText(blngadrsjson.getString("firstname"));
                lastName.setText(blngadrsjson.getString("lastname"));
                streetaddress.setText(blngadrsjson.getString("street"));
                city.setText(blngadrsjson.getString("city"));
                stateSpinner.setText(blngadrsjson.getString("region"));
                countryspinner.setText(blngadrsjson.getString("country_id"));
                zip.setText(blngadrsjson.getString("postcode"));
                telephone.setText(blngadrsjson.getString("telephone"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if("chkoutedit".equals(from)) {

            shipthisBtn.setVisibility(View.GONE);
            shipdiffBtn.setVisibility(View.GONE);
            //String bilingadrs = mprefrenceData.getShippingAdrs();
            //Log.v("bilingadrs",""+bilingadrs);
            try {
                JSONObject blngadrsjson = new JSONObject(shipingAdrs);

                // String biladrs = addrsjson.getString("firstname") +" " +addrsjson.getString("lastname") + "\n" + addrsjson.getString("street")+ " \n" + addrsjson.getString("region")+ "," + addrsjson.getString("city");
                //String biladrs2 = addrsjson.getString("postcode") +" ," +addrsjson.getString("country_id") +"\n" +addrsjson.getString("telephone") ;
                fstName.setText(blngadrsjson.getString("firstname"));
                lastName.setText(blngadrsjson.getString("lastname"));
                streetaddress.setText(blngadrsjson.getString("street"));
                city.setText(blngadrsjson.getString("city"));
                stateSpinner.setText(blngadrsjson.getString("region"));
                countryspinner.setText(blngadrsjson.getString("country_id"));
                zip.setText(blngadrsjson.getString("postcode"));
                telephone.setText(blngadrsjson.getString("telephone"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if("addchkoutaddress".equals(from)){
           // Log.v("from","addadrs'"+from);
            shipthisBtn.setVisibility(View.GONE);
            shipdiffBtn.setVisibility(View.GONE);
        }else if("addaddress".equals(from)){
            shipthisBtn.setVisibility(View.VISIBLE);
            shipdiffBtn.setVisibility(View.VISIBLE);
        }


        else{
                shipthisBtn.setVisibility(View.VISIBLE);
                shipdiffBtn.setVisibility(View.VISIBLE);
            }


    }


    private void initcountryList() {
        JSONObject json = new JSONObject();

        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_LOAD_COUNTRYLIST);
        gogglesAsynctask.execute();
    }

    private void init(String slctcuntry) {
        JSONObject json = new JSONObject();
        try {
            //{"country_id":"US"}
            json.put("country_id",slctcuntry);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_LOAD_REGIONLIST);
        gogglesAsynctask.execute(json.toString());
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    boolean cancel = false;
    View focusView = null;
    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // submitData();
            Log.v("submitListener from",""+from);
            JSONObject slctadrsjson = new JSONObject();
            if("chkoutedit".equals(from) ||"addchkoutaddress".equals(from) ||"addshipaddress".equals(from) ) {
                String fname = fstName.getText().toString();
                String lname = lastName.getText().toString();
                String streetAds = streetaddress.getText().toString();
                // String cmpntname  = company.getText().toString();
                String cityname = city.getText().toString();
                String mobile = telephone.getText().toString();
                String emailId = emailid.getText().toString();
                String postcode = zip.getText().toString();
                String state = stateSpinner.getText().toString();

                String country = countryspinner.getText().toString();
               /* if(!mprefrenceData.isLogincheck()){
                  if(TextUtils.isEmpty(emailId)){
                      emailid.setError("Enter Email Id.");
                        focusView = emailid;
                        cancel = true;
                        return;
                    }
                }*/


                if (TextUtils.isEmpty(fname)) {
                    fstName.setError("Enter Name.");
                    focusView = fstName;
                    cancel = true;
                    return;
                } else if (TextUtils.isEmpty(lname)) {
                    lastName.setError("Enter last Name.");
                    focusView = lastName;
                    cancel = true;
                    return;
                } else if (!isValidEmail(emailId)) {
                    if (!mprefrenceData.isLogincheck()) {
                        emailid.setError("Enter Email Id.");
                        focusView = emailid;
                        cancel = true;
                        return;
                    }
                }else if (TextUtils.isEmpty(streetAds)) {
                    streetaddress.setError("Enter Street Name.");
                    focusView = streetaddress;
                    cancel = true;
                    return;
                } else if (TextUtils.isEmpty(cityname)) {
                    city.setError("Enter city Name.");
                    focusView = telephone;
                    cancel = true;
                    return;
                } else if (TextUtils.isEmpty(postcode)) {
                    zip.setError("Enter PostCode.");
                    focusView = zip;
                    cancel = true;
                    return;
                } else if (TextUtils.isEmpty(mobile)) {
                    telephone.setError("Enter Mobile No.");
                    focusView = city;
                    cancel = true;
                    return;
                }else if(state.equals("Select state")){
                   PrescriptionAddActivity.show_dialog(mContext, "Select State");
                    return;
                }else if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                    focusView.setFocusable(true);
                }
                cancel = false;
                 if(!cancel){
//"quote_id","","customer_id":"","billing_address_id":"<id of the selected address , no need to send the id if its new address>",
// "save_in_address_book" : <0 or 1>,"use_for_shipping":1,"firstname":"","lastname":"","street":"","postcode":"","city":"","region_id":"<if you select the region>",
// "region":"if region is entered otherwise blank","country_id":"like US,GB","telephone":""}
                    try {
                        slctadrsjson.put("firstname",fname);
                        slctadrsjson.put("lastname",lname);
                        slctadrsjson.put("street",streetAds);
                        slctadrsjson.put("postcode",postcode);
                        slctadrsjson.put("city",cityname);
                        slctadrsjson.put("region",state);
                        int rgpos = Arrays.asList(stateArray).indexOf(stateSpinner.getText().toString());
                        slctadrsjson.put("region_id",regionList.get(rgpos).getRegionid());

                        //int pos = Arrays.asList(countryArray).indexOf(countryspinner.getText().toString());
                        slctadrsjson.put("country_id",countryspinner.getText().toString());
                        slctadrsjson.put("telephone",mobile);
                        slctadrsjson.put("use_for_shipping",isdefaultshipping);
                        slctadrsjson.put("save_in_address_book",savinflag);
                        if(!mprefrenceData.isLogincheck()){
                            slctadrsjson.put("emailid",emailId);
                        }
                        //slctadrsjson.put(shipngadrsjson.getString("billing_address_id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Log.v("selctjson", "adrs" + slctadrsjson);
                Intent i = new Intent();
                i.putExtra("chkouteditdata",slctadrsjson.toString());
                i.putExtra("selctjson", slctadrsjson.toString());
                //Log.v("chkouteditdata", "" + slctadrsjson);
                setResult(RESULT_OK,i);
                finish();

            }else {
                submitData();
                }


        }
    };

boolean submitflag = false;

   private void submitData() {

        // Reset errors.
        fstName.setError(null);
        lastName.setError(null);

        streetaddress.setError(null);
        city.setError(null);
        zip.setError(null);
        telephone.setError(null);

        String fname = fstName.getText().toString();
        String lname = lastName.getText().toString();
        String streetAds = streetaddress.getText().toString();
       // String cmpntname  = company.getText().toString();
        String cityname = city.getText().toString();
        String mobile = telephone.getText().toString();
        String postcode = zip.getText().toString();
        String state = stateSpinner.getText().toString();
        String country =  countryspinner.getText().toString();


        if(TextUtils.isEmpty(fname)){
            fstName.setError("Enter Name.");
            focusView = fstName;
            submitflag = true;
            return;
        }else if(TextUtils.isEmpty(lname)){
            lastName.setError("Enter last Name.");
            focusView = lastName;
            submitflag = true;
            return;
        }else if(TextUtils.isEmpty(streetAds)){
            streetaddress.setError("Enter Strret Name.");
            focusView = streetaddress;
            submitflag = true;
            return;
        }else if(TextUtils.isEmpty(cityname)){
            city.setError("Enter city Name.");
            focusView = city;
            submitflag = true;
            return;
        }else if(TextUtils.isEmpty(postcode)){
            zip.setError("Enter PostCode.");
            focusView = zip;
            submitflag = true;
            return;
        }else if(TextUtils.isEmpty(mobile)){
            telephone.setError("Enter Mobile No.");
            focusView = telephone;
            submitflag = true;
            return;
        }else if(state.equals("Select state")){
            PrescriptionAddActivity.show_dialog(mContext, "Select State");
            return;
        }else if (submitflag) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            focusView.setFocusable(true);
        }
       submitflag = true;
        if(submitflag) {
            //Log.v("from", "else" + from);
             JSONObject chkoutjson = new JSONObject();
            //Request : {“user_id” : 1,”firstname”:”surjan”,”lastname”:”singh”,”country_id”:”US”,
            // ”postcode”,”560102”,”city”:”xyz”,”region”:”ppp”,” telephone”:”8686768776”,”save_in_address” : 1,”street” : “123 test”}
            try {
                chkoutjson.put("user_id",mprefrenceData.getCustomerId());
                chkoutjson.put("firstname",fname);
                chkoutjson.put("lastname",lname);
                chkoutjson.put("isdefaultshipping",isdefaultshipping);
                chkoutjson.put("isdefaultbiling",isdefaultbiling);
                chkoutjson.put("city",cityname);
                chkoutjson.put("street",streetAds);

                chkoutjson.put("addressType",addressType);
                chkoutjson.put("save_in_address","1");
                chkoutjson.put("postcode",postcode);
                chkoutjson.put("telephone",mobile);
                chkoutjson.put("country_id",country);
                chkoutjson.put("region", state);
                //chkoutjson.put("",faxno);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(from.equals("edit")){
               // Log.v("for edit","");
                try {
                    chkoutjson.put("address_id","4");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_EDITADRESS);
                gogglesAsynctask.execute(chkoutjson.toString());
            }else {
               // Log.v("for add","");
                GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_ADDADRESS);
                gogglesAsynctask.execute(chkoutjson.toString());
            }

        }




    }
    boolean showregionFlag;
    ArrayList<String> tempregionlist = new ArrayList<>();
    ArrayList<String> templist = new ArrayList<>();
    private void displayRegionData(JSONObject receiveJSon){

        try {// "region_id": "1",         "country_id": "US",  "code": "A     "default_name": "Alabama",     "name": "Alabama"

            //{"Error":true,"region":null,"show_region":false}
            showregionFlag = receiveJSon.getBoolean("show_region");
           // Log.v("showregionFlag","1111"+showregionFlag);
            if(showregionFlag){
                stateSpinner.setVisibility(View.VISIBLE);
                stateEt.setVisibility(View.GONE);
            JSONArray regionAry = receiveJSon.getJSONArray("region");
            for(int i=0;i<regionAry.length();i++) {
                JSONObject cjson = regionAry.getJSONObject(i);
                RegionData regionData = new RegionData();
                regionData.setCountry_id(cjson.getString("country_id"));
                regionData.setCountry_code(cjson.getString("code"));
                regionData.setName(cjson.getString("name"));
                regionData.setRegionid(cjson.getString("region_id"));
                regionList.add(regionData);
                tempregionlist.add(cjson.getString("name"));

                stateArray = tempregionlist.toArray(new String[tempregionlist.size()]);
                ArrayAdapter<String> stateadapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, stateArray);
                stateSpinner.setAdapter(stateadapter);
            }

            }else{
                stateSpinner.setVisibility(View.GONE);
                stateEt.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setCountryData(JSONObject receiveJSon) {
// {"Error":false,"data":[{"country_id":"AD","iso2_code":"AD","iso3_code":"AND","name":"Andorra"}
        try {
            JSONArray countryjsonAry = receiveJSon.getJSONArray("data");
            for(int i=0;i<countryjsonAry.length();i++){
                JSONObject cntryjson = countryjsonAry.getJSONObject(i);
                CountryData cdata = new CountryData();
                cdata.setCountryID(cntryjson.getString("country_id"));
                cdata.setCountryName(cntryjson.getString("name"));
                templist.add(cntryjson.getString("name"));
                countryList.add(cdata);



           /* for(int j=0;j<countryList.size();j++){

                countrysArray[j] = String.valueOf(countryList.get(j));
                 stateArray = tempregionlist.toArray(new String[tempregionlist.size()]);
                ArrayAdapter<String> stateadapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_dropdown_item_1line, stateArray);
                stateSpinner.setAdapter(stateadapter);
            }*/

            countrysArray = templist.toArray(new String[templist.size()]);
            ArrayAdapter<String> countryadapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, countrysArray);
            countryspinner.setAdapter(countryadapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void updateUi(String result) {


        if(result.equals(AppConstants.SUCCESSFUL)){
            Toast.makeText(sInstance,"SuccessFully Address Saved",Toast.LENGTH_LONG).show();
            sInstance.finish();

        }else{
            Toast.makeText(sInstance,""+result,Toast.LENGTH_LONG).show();
        }

    }

    public static void updateEditadrsUi(String result) {
        if(result.equals(AppConstants.SUCCESSFUL)){
            Toast.makeText(sInstance,"SuccessFully Address Saved",Toast.LENGTH_LONG).show();
            sInstance.finish();
        }else{
            Toast.makeText(sInstance,""+result,Toast.LENGTH_LONG).show();
        }

    }


    public static void setCountryData(String result, JSONObject receiveJSon) {
        if(result.equals(AppConstants.SUCCESSFUL)){

            sInstance.setCountryData(receiveJSon);
        }else{
            Toast.makeText(sInstance,""+result,Toast.LENGTH_LONG).show();
        }
    }

  public static void setRegionData(String result, JSONObject receiveJSon) {
        if(result.equals(AppConstants.SUCCESSFUL)){


            sInstance.displayRegionData(receiveJSon);
        }else{
           // Toast.makeText(sInstance,""+result,Toast.LENGTH_LONG).show();
        }
    }


}
