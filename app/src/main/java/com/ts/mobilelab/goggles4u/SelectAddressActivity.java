package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectAddressActivity extends AppCompatActivity {

    ListView addrsList;
    Button addAdrs,continueBtn;

    private Context mContext;
    private static SelectAddressActivity sInstance;
    private PreferenceData mPreferenceData;
    RadioGroup rgAdrs;
    LinearLayout ltadrs;
    int selctadrspos = 0;
    RadioButton radioButton;
    RadioGroup.LayoutParams rprms;
    JSONArray adrsjsonary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
       // addrsList = (ListView) findViewById(R.id.lisview_adrs);
        addAdrs = (Button) findViewById(R.id.btn_addadrs);
        rgAdrs = (RadioGroup) findViewById(R.id.rg_adrs);
        ltadrs = (LinearLayout) findViewById(R.id.linear_adrs);
        continueBtn = (Button) findViewById(R.id.btn_continueadrs);
        init();
       // displayData();
        addAdrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(mContext, EditAddress.class).putExtra("from", "addchkoutaddress"), AppConstants.CODE_FOR_ADDCHKOUTADRS);
            }
        });

        rgAdrs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = rgAdrs.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) findViewById(checkedRadioButtonId);
                //selectedOption = optionkeyList.get(checkedRadioButtonId);
                selctadrspos = radioBtn.getId();
                Log.v("id", "" + radioBtn.getId());

                //Toast.makeText(SortingActivity.this, radioBtn.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selctadrspos == 0){
                    Toast.makeText(mContext,"Select an address to continue",Toast.LENGTH_LONG).show();
                }else{

                    try {
                        JSONObject selctjson = adrsjsonary.getJSONObject(selctadrspos-1);
                        Log.v("selctjson", "" + selctjson);
                        Intent i = new Intent();
                        i.putExtra("selctjson", selctjson.toString());
                        setResult(RESULT_OK, i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();

                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == AppConstants.CODE_FOR_ADDCHKOUTADRS){
            if(resultCode == RESULT_OK){
                Log.v("onActivityResult", "in slctadrs"+intent.getStringExtra("chkouteditdata"));
                String chkoutadrs = intent.getStringExtra("chkouteditdata");
                Intent i = new Intent();
                i.putExtra("selctjson",chkoutadrs);
                setResult(RESULT_OK,i);
                finish();


              /*  try {
                    JSONObject chkoutjson = new JSONObject(chkoutadrs);
                    adrsjsonary.put(chkoutjson);
                    displayData(chkoutjson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/



            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        JSONObject json = new JSONObject();
        try {
            //{"user_id":"10","addresstype":"checkout"}
            json.put("user_id",mPreferenceData.getCustomerId());
            json.put("addresstype","checkout");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_LOAD_ADDRESS);
        gogglesAsynctask.execute(json.toString());
    }

    private void displayData(JSONObject receiveJSon) {

        try {
            adrsjsonary = receiveJSon.getJSONArray("address");

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            for(int i=0;i<adrsjsonary.length();i++){

                JSONObject shipngadrsjson = adrsjsonary.getJSONObject(i);
                Log.v("shipngadrsjson", "" + shipngadrsjson);
                String shipadrs = shipngadrsjson.getString("firstname") + " " + shipngadrsjson.getString("lastname") + "\n" + shipngadrsjson.getString("street") + "," + shipngadrsjson.getString("region") + "\n" + shipngadrsjson.getString("city") + "-" + shipngadrsjson.getString("postcode") + " ," + shipngadrsjson.getString("country_id") +"\n" +"Mob:" + shipngadrsjson.getString("telephone");

                //"Mob:" + sInstance.shipngadrsjson.getString("telephone")

               // LayoutInflater inflater = getLayoutInflater();
                //View adrsview = inflater.inflate(R.layout.address_row,null);

               // radioButton = (RadioButton) adrsview.findViewById(R.id.rb_adrs);
               radioButton =  new RadioButton(mContext);
               //radioButton.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
                View v = new View(mContext);
                v.setMinimumWidth(1);
                radioButton.setText(shipadrs);
                if(i%2 == 0)
                radioButton.setBackgroundColor(getResources().getColor(R.color.gray1));
               else
                radioButton.setBackgroundColor(getResources().getColor(R.color.gray2));
                radioButton.setTextSize(16);
                radioButton.setPadding(4, 4, 4, 4);
               // LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) radioButton.getLayoutParams();
                //params.setMargins(0,8,0,0);
                //radioButton.setLayoutParams(params);
                radioButton.setId(i+1);
                //rprms= new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                //rgAdrs.removeAllViews();
                Log.v("radioButton",""+radioButton.getId());
                rgAdrs.addView(radioButton);
                //ltadrs.addView(rgAdrs,p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void setData(String result, JSONObject receiveJSon) {

        if(result.equals(AppConstants.SUCCESSFUL)){
            sInstance.displayData(receiveJSon);
        }else{
            Toast.makeText(sInstance,""+result,Toast.LENGTH_LONG).show();
        }
    }




}
