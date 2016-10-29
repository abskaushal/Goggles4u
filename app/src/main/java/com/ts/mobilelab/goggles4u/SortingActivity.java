package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SortingActivity extends AppCompatActivity {

    private Context mContext;
    private static SortingActivity sInstance;
    private Button done,cancel;
    private ArrayList<String> optionList;
    private  ArrayList<String> optionkeyList;
   // LinearLayout sortlayot;
    private RadioButton cb1;
    private RadioGroup radioGroupsort,rgsortOpt;
    private String type = "asc";
    private String selectedOption;
    private RelativeLayout homelt;
    private JSONObject sortjson;
    private PreferenceData mPreferenceData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sorting);
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        done = (Button) findViewById(R.id.btn_done);
        cancel = (Button) findViewById(R.id.btn_cancelsort);
        //sortlayot = (LinearLayout) findViewById(R.id.linear_sortoption );
        radioGroupsort = (RadioGroup) findViewById(R.id.radioGroup_sort);
        rgsortOpt = (RadioGroup) findViewById(R.id.rg_sortopt);
        homelt = (RelativeLayout) findViewById(R.id.relativ_hhome);
        optionkeyList = new ArrayList<>();
        homelt.setVisibility(View.GONE);
        init();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("selectedOption", "ww" + selectedOption);


                if (selectedOption != null && !selectedOption.isEmpty()) {
                    Intent in = new Intent();

                    in.putExtra("type", sInstance.type);
                    in.putExtra("sortoptions", sInstance.selectedOption);
                    sInstance.setResult(RESULT_OK, in);
                    JSONObject sortdata = new
                            JSONObject();
                    try {
                        sortdata.put("selctoption",checkedRadioButtonId);
                        sortdata.put("type",sInstance.type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mPreferenceData.setSELECTED_SORT_OPTION(sortdata.toString());
                    sInstance.finish();
                    /* sortjson = new JSONObject();
                    try {
                        sortjson.put("cat_id", "18");
                        sortjson.put("p", "1");
                        sortjson.put("c", "15");
                        sortjson.put("sort", selectedOption);
                        sortjson.put("dir", type);
                        //sortjson.put("sortkey", selctoptionsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("sortjson ", "" + sortjson);

                    GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_SORTOPTION_SUBMIT);
                    gogglesAsynctask.execute(sortjson.toString());*/
                    // setResult(RESULT_OK);
                    //finish();
                } else {
                    Toast.makeText(mContext, "Please Select Sorting Option", Toast.LENGTH_LONG).show();
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sInstance.finish();
            }
        });

        radioGroupsort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButton_ascending ){
                    type = "asc";
                }else{
                    type = "desc";
                }
            }
        });

    }


    private void init() {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_SORTOPTION);
        gogglesAsynctask.execute();
    }
    int checkedRadioButtonId;
    ArrayList<String> selctoptionsList = new ArrayList<>();
    String optionAry[];
    private void showSortOptions(JSONObject receiveJSon) {

        try {
            int selctid = 0;
            String saveoption = mPreferenceData.getSELECTED_SORT_OPTION();
            if(!saveoption.isEmpty()) {
                JSONObject savejson = new JSONObject(saveoption);
                Log.v("savejson", "" + savejson);
                 selctid = savejson.getInt("selctoption");
                String type = savejson.getString("type");
            }
            JSONObject datajson = receiveJSon.getJSONObject("data");
            Log.v("datajson", "" + datajson);

            optionList  = new ArrayList<>();
            Iterator<?> iterator = datajson.keys();
            while(iterator.hasNext()){
                String keys = (String) iterator.next();
                optionkeyList.add(keys);
                String vals = datajson.getString(keys);
                optionList.add(vals);


            }
           for(int i=0;i<optionList.size();i++){

               RadioButton radioButton = new RadioButton(this);
               radioButton.setText(optionList.get(i));
               radioButton.setId(i);

               rgsortOpt.addView(radioButton);
               if(!saveoption.isEmpty()) {
                   if (selctid == i) {
                       radioButton.setChecked(true);
                   }
               }

           }



            rgsortOpt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    checkedRadioButtonId = rgsortOpt.getCheckedRadioButtonId();
                    RadioButton radioBtn = (RadioButton) findViewById(checkedRadioButtonId);
                    selectedOption = optionkeyList.get(checkedRadioButtonId);
                    Log.v("selectedOption",""+selectedOption);
                    //Toast.makeText(SortingActivity.this, radioBtn.getText(), Toast.LENGTH_SHORT).show();
                }
            });

           Log.v("optionkeyList",""+optionkeyList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public static void updateSortDataUi(String result, JSONObject receiveJSon) {
        if(result.equals(AppConstants.SUCCESSFUL)){
            sInstance.homelt.setVisibility(View.VISIBLE);
            sInstance.showSortOptions(receiveJSon);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
