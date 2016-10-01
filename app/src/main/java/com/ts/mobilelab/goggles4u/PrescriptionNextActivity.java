package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PrescriptionNextActivity extends AppCompatActivity {


    private Context mContext;
    private static PrescriptionNextActivity sInstance;
    private BetterSpinner spinner_coating, spinner_tinting;
    private String[] tintyAry, coatingAry;
    private RadioGroup rgtint;
    private RadioGroup rglense;
    private PreferenceData mPreferenceData;
    private JSONObject pricejson;
    private CheckBox cbsavepres;
    private  Button addtocartBtn;
    private String selectedLense = "";
   // private EditText quantityEt;
    private Double actulaprice, newprice;
    private int savepresdata = 0;
    private JSONObject mainjson, optJson;
    private int lensselectedpos;
    private JSONObject lensjson, glassjson;
    private TextView totalPrice,amnttoPay;
    private Double basePrice = 0d, lensTotalPrice = 0d, tintTotalPrice = 0d, coatingTotalPrice = 0d;
    private  int tintselected = 0, selectCoatingPosition, selectflag = 0;
    //JSONArray optJsonAry;

    int flag =0;
    String jsondata, lensData, priceData, productId;
    RelativeLayout relative_tint, relative_arc;
    int presssaveflag;
    String userpresdata;
    JSONObject prescjson;
    DecimalFormat dcf = new DecimalFormat("#.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_next);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        sInstance = this;
        optJson = new JSONObject();
        jsondata = getIntent().getStringExtra("mainjson");
        Log.v("pres next", "" + jsondata);
        lensData = getIntent().getStringExtra("lensjson");
        //Log.v("lens",""+lens);
        priceData = getIntent().getStringExtra("pricejsonData");

        presssaveflag = getIntent().getIntExtra("prescsaveflag", 1);
        userpresdata = getIntent().getStringExtra("userpresdata");
        Log.v("userpresdata", "" + userpresdata);
        mPreferenceData = new PreferenceData();
        if (userpresdata != null && !userpresdata.isEmpty()) {
            try {
                prescjson = new JSONObject(userpresdata);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        spinner_coating = (BetterSpinner) findViewById(R.id.spinner_coating);
        spinner_tinting = (BetterSpinner) findViewById(R.id.spinner_tinting);
        rgtint = (RadioGroup) findViewById(R.id.rg_tint);
        rglense = (RadioGroup) findViewById(R.id.rg_lensopt);
        cbsavepres = (CheckBox) findViewById(R.id.cbx_savepres);
        addtocartBtn = (Button) findViewById(R.id.btn_addtocart);
        //quantityEt = (EditText) findViewById(R.id.et_quantity);
        relative_tint = (RelativeLayout) findViewById(R.id.relative_tinting);
        relative_arc = (RelativeLayout) findViewById(R.id.relative_arc);
        totalPrice = (TextView) findViewById(R.id.tv_totalprice);
        amnttoPay = (TextView) findViewById(R.id.tv_amnttopay);
        addtocartBtn.setOnClickListener(submitListener);
        if (presssaveflag == 2) {
            cbsavepres.setChecked(true);
        }
        cbsavepres.setOnCheckedChangeListener(savePresDilaogListener);
        try {
            if (priceData != null && !priceData.isEmpty()) {
                try {
                    pricejson = new JSONObject(priceData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.v("pricejson", "11" + pricejson);
            actulaprice = Double.parseDouble(pricejson.getString("actual_price"));
            newprice = Double.parseDouble(pricejson.getString("changedprice"));
            optJson = pricejson.getJSONObject("options");

            basePrice = actulaprice + newprice;

            totalPrice.setText("$ " +dcf.format(basePrice));
            amnttoPay.setText("To pay $ " +dcf.format(basePrice));
            productId = pricejson.getString("product_id");
            lensjson = new JSONObject(lensData);
            Log.v("lensjson", "22" + lensjson);
            setLenseData(lensjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!mPreferenceData.getCartQuoteID().isEmpty()) {
            try {
                pricejson.put("quote_id", mPreferenceData.getCartQuoteID());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        rgtint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_tintyes) {
                    selectflag = 1;
                    flag = 1;
                    spinner_tinting.setVisibility(View.VISIBLE);
                    relative_arc.setVisibility(View.GONE);
                    spinner_coating.setVisibility(View.GONE);
                } else {
                    selectflag = 0;
                    relative_arc.setVisibility(View.VISIBLE);
                    spinner_coating.setVisibility(View.VISIBLE);
                    spinner_tinting.setVisibility(View.GONE);
                }
            }
        });
        setData(jsondata);

        rglense.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = rglense.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) findViewById(checkedRadioButtonId);
                selectedLense = radioBtn.getText().toString();
                lensselectedpos = (int) radioBtn.getTag();
                //selectedOption = optionkeyList.get(checkedRadioButtonId);
                Log.v("selectedLense", "11" + selectedLense);
                Log.v("lensselectedpos", "11" + lensselectedpos);
                //total += Double.parseDouble()

                JSONObject lensselctjson = new JSONObject();
                try {
                    Log.v("lensselectedpos", "" + glassjson.getJSONArray("options").getJSONObject(lensselectedpos).getString("price_val"));
                    // {"option_type_id" :"2416888","option_id":981278,"option_text":"1.57 CR39 Lenses","product_id":47742}
                    lensselctjson.put("option_type_id", glassjson.getJSONArray("options").getJSONObject(lensselectedpos).getString("option_value_id"));
                    lensselctjson.put("option_id", glassjson.getString("option_id"));
                    lensselctjson.put("option_text", selectedLense);
                    lensselctjson.put("product_id", productId);
                    lensTotalPrice = Double.parseDouble(glassjson.getJSONArray("options").getJSONObject(lensselectedpos).getString("price_val"));
                    Log.v("lensTotalPrice",""+lensTotalPrice);

                    updatePrice();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GogglesAsynctask asynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_LOADLENSOPTIONDATA);
                asynctask.execute(lensselctjson.toString());

            }
        });
        spinner_coating.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCoatingPosition = position;
                selectflag = 0;
                flag =1;
                Log.v("selectCoatingPosition", "222" + selectCoatingPosition);
                try {
                    coatingTotalPrice = Double.parseDouble(coatingjson.getJSONArray("arcdata").getJSONObject(selectCoatingPosition).getString("price"));

                    updatePrice();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //newprice += Double.parseDouble(mainjson.getJSONObject("tint").getJSONArray("vals").getJSONObject(Arrays.asList(tintyAry).indexOf(spinner_tinting.getText().toString()) + 1).getString("price_val"));

        spinner_tinting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("selecttintingPosition", "222" + position);
                try {
                    tintTotalPrice = Double.parseDouble(mainjson.getJSONObject("tint").getJSONArray("vals").getJSONObject(position).getString("price_val"));
                    updatePrice();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void updatePrice() {

        //Log.v("tintTotalPrice",""+tintTotalPrice);
       // Log.v("all",""+basePrice + lensTotalPrice + coatingTotalPrice + tintTotalPrice);
        Double total = basePrice + lensTotalPrice + coatingTotalPrice + tintTotalPrice;
       // Log.v("total", "" + total);

        totalPrice.setText("$ " + dcf.format(total));
        amnttoPay.setText("\"To pay $ " + dcf.format(total));

    }

    JSONObject coatingjson;
    JSONArray coatingjsonarray;

    private void setTintData(JSONObject receiveJSon) {
        try {
            String tintflag = receiveJSon.getString("show_tint");

            if (receiveJSon.has("options")) {

                coatingjson = receiveJSon.getJSONObject("options");
                coatingjsonarray = coatingjson.getJSONArray("arcdata");
                Log.v("coatingjsonarray", "11" + coatingjsonarray);
                coatingAry = new String[coatingjsonarray.length()];
                for (int i = 0; i < coatingjsonarray.length(); i++) {
                    JSONObject pjson = coatingjsonarray.getJSONObject(i);
                    coatingAry[i] = pjson.getString("label");
                }

                spinner_coating.setAdapter(getData(coatingAry));
            }


            if (tintflag.equals("1")) {
                relative_tint.setVisibility(View.VISIBLE);
                rgtint.setVisibility(View.VISIBLE);

            } else {
                relative_tint.setVisibility(View.GONE);
                rgtint.setVisibility(View.GONE);
                relative_arc.setVisibility(View.VISIBLE);
                spinner_coating.setVisibility(View.VISIBLE);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Boolean checkflag = true;
    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             String tintyingVal = spinner_tinting.getText().toString();
            String coatingVal = spinner_coating.getText().toString();

            if (selectedLense.isEmpty()) {
                PrescriptionAddActivity.show_dialog(mContext, "Select Lense option..");
                checkflag = false;
                return;
            } else if (flag == 0) {
                PrescriptionAddActivity.show_dialog(mContext, "Select all option.");
                checkflag = false;
                return;
            }else if(selectflag ==1){
                if(tintyingVal.isEmpty()){
                    PrescriptionAddActivity.show_dialog(mContext, "Select Tinting value.");
                    checkflag = false;
                    return;
                }


            }else if(selectflag == 0){
                if(coatingVal.isEmpty()){
                    PrescriptionAddActivity.show_dialog(mContext, "Select Coating  value.");
                    checkflag = false;
                    return;
                }


            }
            checkflag = true;

            if(checkflag){

                Log.v("else","onClick");
                try {
                    if (selectflag != 0) {
                        //  rightpdjson.put("option_id", mainjson.getJSONObject("rightpd").getString("option_id"));
                        // rightpdjson.put("option_value", pddualritspinr.getText().toString());
                        optJson.put(mainjson.getJSONObject("tint").getString("option_id"), spinner_tinting.getText().toString());
                        optJson.put(mainjson.getJSONObject("tinting").getString("option_id"), mainjson.getJSONObject("tinting").getJSONArray("vals").getJSONObject(1).getString("title"));
                        newprice += Double.parseDouble(mainjson.getJSONObject("tint").getJSONArray("vals").getJSONObject(Arrays.asList(tintyAry).indexOf(spinner_tinting.getText().toString()+1)).getString("price_val"));
                        //newprice += Double.parseDouble(mainjson.getJSONObject("tinting").getJSONArray("vals").getJSONObject(Arrays.asList(tintyAry).indexOf(spinner_tinting.getText().toString() + 1).getString("price_val"));
                    } else {
                        optJson.put(coatingjson.getString("option_id"), coatingjson.getJSONArray("arcdata").getJSONObject(selectCoatingPosition).getString("option_value_id"));
                        //optJson.put(mainjson.getJSONObject("anti_reflective_coating").getString("option_id"),mainjson.getJSONObject("anti_reflective_coating").getJSONObject(Arrays.asList(coatingAry).indexOf(spinner_coating.getText().toString()) + 1).getString("option_val_id"));
                        //optJson.put(coatingjson.getJSONObject("options").getString("option_id"),mainjson.getJSONObject("anti_reflective_coating").getJSONArray("vals").getJSONObject(Arrays.asList(coatingAry).indexOf(spinner_coating.getText().toString()) + 1).getString("option_val_id"));
                        newprice += Double.parseDouble(coatingjson.getJSONArray("arcdata").getJSONObject(selectCoatingPosition).getString("price"));


                    }
                    if (cbsavepres.isChecked()) {
                        pricejson.put("savepres", 1);
                        pricejson.put("prescription_id", prescjson.getString("prescription_id"));
                        pricejson.put("customer_id", mPreferenceData.getCustomerId());
                    }
                    // Log.v("newprice",""+newprice);
                    //Log.v("actulaprice", "" + actulaprice);
                    Double dp = newprice + actulaprice;
                    optJson.put(glassjson.getString("option_id"), glassjson.getJSONArray("options").getJSONObject(lensselectedpos).getString("option_value_id"));
                    //pricejson.put("qty", quantity);
                    pricejson.put("changedprice", dp);
                    pricejson.put("options", optJson);
                    pricejson.put("savepres", savepresdata);

                    Log.v("pricejson", "" + pricejson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                GogglesAsynctask googleAsyncTaskGet = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_ADDTOCART);
                googleAsyncTaskGet.execute(pricejson.toString());

            }
        }
    };
    private CompoundButton.OnCheckedChangeListener savePresDilaogListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (mPreferenceData.isLogincheck()) {
                    showSaveDialog();


                } else {
                    Toast.makeText(mContext, "Please Login to save", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(mContext, Login.class);
                    i.putExtra("loginintent", "prescriptionnext");
                    startActivityForResult(i, 114);
                    //i.putExtra("loginintent", "prescriptionnext");//mainjson,lensjson,pricejsonData
                    // i.putExtra("mainjson", jsondata);
                    //i.putExtra("lensjson", lensData);
                    //i.putExtra("pricejsonData", priceData);

                    //startActivity(new Intent(mContext, PrescriptionNextActivity.class).putExtra("lensjson", json).putExtra("mainjson", mainjson.toString()).putExtra("pricejsonData", pricejson.toString()));
                }
            }
        }
    };
    SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");

    private void showSaveDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Prescription Save Details");

        LayoutInflater inflater = getLayoutInflater();
        View cv = inflater.inflate(R.layout.pres_savealert, null);

        final EditText edtpresname = (EditText) cv.findViewById(R.id.et_prescname);
        final DatePicker datePicker = (DatePicker) cv.findViewById(R.id.datePicker);
        if (userpresdata != null) {
            try {
                // "2016-06-24",
                if (prescjson.has("name"))
                    edtpresname.setText(prescjson.getString("name"));
                if (prescjson.has("date")) {
                    String date_saved = prescjson.getString("date");
                    //"2016-06-24"
                    // Log.v("date_saved",""+date_saved);
                    int day = Integer.parseInt(date_saved.substring(8, 10));
                    //Log.v("day",""+date_saved.substring(8,10));
                    int year = Integer.parseInt(date_saved.substring(0, 4));
                    // Log.v("year",""+date_saved.substring(0,4));
                    int month = Integer.parseInt(date_saved.substring(5, 7));
                    //int month_code = getMonthInt(month);
                    // Log.v("date_saved",""+date_saved);


                    Log.v("month", "" + month);

                    datePicker.updateDate(year, month, day);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // Log.v("yesr", "" + datePicker.getYear());
        //Log.v("getDayOfMonth", "" + datePicker.getDayOfMonth() + 1);
        //Log.v("datePicker", "" + datePicker.getMonth());
        alertDialog.setView(cv);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Log.v("onClick", "" + datePicker);
                savepresdata = 1;

                Log.v("savepresdata", "" + savepresdata);
                if (edtpresname.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "Enter pres. name", Toast.LENGTH_LONG).show();
                } else {


                    String inspectiondate = df.format(new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth()));
                    Log.v("inspectiondate", "" + inspectiondate);
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth() + 1;
                    int year = datePicker.getYear();

                    try {
                        pricejson.put("prescription_name", edtpresname.getText().toString());
                        pricejson.put("prescription_date", year + "-" + month + "-" + day);
                        pricejson.put("savepres", savepresdata);
                        pricejson.put("prescription_id", prescjson.getString("prescription_id"));
                        pricejson.put("customer_id", mPreferenceData.getCustomerId());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // String day = String.valueof(datePicker.getDayOfMonth());
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savepresdata = 0;
                cbsavepres.setChecked(false);
                dialog.dismiss();
            }
        }).create().show();

    }
//


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("onActivityResult pres", "requestCode" + requestCode);
        Log.v("onActivityResult pres","resultCode"+resultCode);
        if(requestCode == 22){
            if (resultCode == RESULT_CANCELED) {
                setResult(RESULT_CANCELED);
                finish();

            }else{
                setResult(RESULT_OK);
                finish();
            }

            //setIntent(11);
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 114) {
                showSaveDialog();
            }
        }
    }

    private void setLenseData(JSONObject lensjson) {

//{"lens":{"option_id":"967074","options":{"option_id":"967074","option_value_id":"2384538","price_val":"0.00","sku":"1.57 CR39 Lenses",
// "title":"1.57 CR39 Lenses+$0.00","formated_price":"$0.00"},"1":{"option_id":"967074","option_value_id":"2384539","price_val":"23.00",
// "sku":"1.57 CR39 Premium Lens with EMI","title":"1.57 CR39 Premium Lens with EMI+$23.00","formated_price":"$23.00"},"
// 2":{"option_id":"967074","option_value_id":"2384541","price_val":"32.95","sku":"1.61 High Index Lenses","title":"1.61 High Index Lenses+$32.95","formated_price":"$32.95"},
// "3":{"option_id":"967074","option_value_id":"2384542","price_val":"47.95","sku":"1.67 High Index Lenses","title":"1.67 High Index Lenses+$47.95","formated_price":"$47.95"},
// "4":{"option_id":"967074","option_value_id":"2384543","price_val":"32.95","sku":"1.56 Photocromic Grey","title":"1.56 Photocromic Grey+$32.95","formated_price":"$32.95"},
// "5":{"option_id":"967074","option_value_id":"2384544","price_val":"32.95","sku":"1.56 Photocromic Brown","title":"1.56 Photocromic Brown+$32.95","formated_price":"$32.95"}},"Error":false}


        try {
            glassjson = lensjson.getJSONObject("lens");
            JSONArray lensoptionAry = glassjson.getJSONArray("options");

            for (int i = 0; i < lensoptionAry.length(); i++) {

                JSONObject json = lensoptionAry.getJSONObject(i);
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(json.getString("title"));
                radioButton.setId(i);
                radioButton.setTag(i);

                rglense.addView(radioButton);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void setData(String jsondata) {
        try {
            mainjson = new JSONObject(jsondata);
            // JSONObject  homejson = mainjson.getJSONObject("options");

            if (mainjson.has("tint")) {


                JSONObject tintyjson = mainjson.getJSONObject("tint");
                JSONArray presarray = tintyjson.getJSONArray("vals");
                Log.v("tintyjson", "11" + presarray);
                tintyAry = new String[presarray.length()];
                for (int i = 0; i < presarray.length(); i++) {
                    JSONObject pjson = presarray.getJSONObject(i);
                    tintyAry[i] = pjson.getString("title");
                }

                spinner_tinting.setAdapter(getData(tintyAry));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayAdapter<String> getData(String[] array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_row, array);

        return adapter;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void updateData(String result) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            Intent i = new Intent(sInstance, ShoppingCartActivity.class);
            sInstance.startActivityForResult(i,22);
            sInstance.finish();
        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }
    private final int  Code_PrescNext = 11;


    public static void setARCUpdateData(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.setTintData(receiveJSon);
        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }
}
