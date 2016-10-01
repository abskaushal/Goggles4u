package com.ts.mobilelab.goggles4u;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.Inflater;
import android.app.Activity;
import android.widget.Toast;

public class NewFilterActivity extends AppCompatActivity {

    private Context mContext;
    private static NewFilterActivity sInstance;
    private TextView prescriptionView;
    LinearLayout filterLt;
   // final CharSequence[] items = {" Easy ", " Medium ", " Hard ", " Very Hard "};
    TextView tv1;
    Button applyFilterBtn, resetFilterBtn;
    TextView view1, view2,msgview;
    JSONArray maindataary = null;
    JSONArray sendFilterArray;
    LinearLayout linear_btnsave;
    JSONObject reciveJson;
    String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
       /* CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("Filter Category");*/
        mContext = this;
        sInstance = this;
        cid = getIntent().getStringExtra("cat_id");
        Log.v("cid",""+cid);
        init(cid);
        sendFilterArray = new JSONArray();
       /* JSONObject defaultjson = new JSONObject();
        try {
            defaultjson.put("cat_id", "18");
            defaultjson.put("p", "1");
            defaultjson.put("c", "15");
            defaultjson.put("dir", "asc");
            defaultjson.put("sort", "price");

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //sendFilterArray.put(defaultjson);
        filterLt = (LinearLayout) findViewById(R.id.filtermainlt);
        applyFilterBtn = (Button) findViewById(R.id.btn_savefilter);
         resetFilterBtn = (Button) findViewById(R.id.btn_clearfilter);
        linear_btnsave = (LinearLayout) findViewById(R.id.linear_savefilter);
        msgview = (TextView) findViewById(R.id.tv_msg);
        applyFilterBtn.setOnClickListener(saveFilterListener);
        resetFilterBtn.setOnClickListener(resetFilterListener);
    }

    private View.OnClickListener saveFilterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v("sendFilterArray ", "total" + sendFilterArray);
            //{"cat_id":18,"p":1,"c":15,"sort":"price","dir":"asc"}

            if(sendFilterArray.length() == 0){
                Toast.makeText(mContext,"Please select above option to filter.",Toast.LENGTH_LONG).show();
            }else{
                Intent in = new Intent(mContext,ProductListingActivity.class);
                in.putExtra("FilterValue",sendFilterArray.toString());
                in.putExtra("cat_id",cid);

                startActivity(in);
            }

           // sInstance.setResult(RESULT_OK, in);
            //sInstance.finish();

            //GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_PRODUCTLISTING);
           // gogglesAsynctask.execute(filtrjson.toString());
        }
    };


    private View.OnClickListener resetFilterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v("resetFilterListener", "'");
            filterLt.removeAllViewsInLayout();
          /*  for (int i = 0; i < maindataary.length(); i++) {
                //
                String label = null;
                try {
                    filterjson = maindataary.getJSONObject(i);
                    label = filterjson.getString("frontend_label");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LayoutInflater inflater = getLayoutInflater();
                View convertView = inflater.inflate(R.layout.filter_item, null);
                view1 = (TextView) convertView.findViewById(R.id.tv_headerview);
                view2 = (TextView) convertView.findViewById(R.id.tv_spinerview);
                view1.setText(label);
                view2.setText("Select  " + label);
                filterLt.addView(convertView);
            }*/

            startActivity(new Intent(mContext,ProductListingActivity.class).putExtra("fromintent","filter"));
            finish();
            //GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_FILTERDATA);
            // gogglesAsynctask.execute();



        }
    };

    @Override
    protected void onResume() {
        Log.v("onResume","reciveJson"+reciveJson);
        super.onResume();
        if(reciveJson != null){
            displayData(reciveJson);
        }else{
           // Toast.makeText(mContext,"No Filter Data Found.Try Again",Toast.LENGTH_LONG).show();
        }

    }

    JSONObject filterjson, innerjson;

    private void displayData(JSONObject receiveJSon) {
       /* {
            "code": "prescription_type",
                "frontend_label": "Prescription Type",
                "attribute_id": "193",
                "inputtype": "multiselect",
                "options": {
            "Distance": "119",
                    "Reading": "120",
                    "Bifocal": "121",
                    "Progressive": "122"
        }*/

        try {
            maindataary = receiveJSon.getJSONArray("attributes");

            for (int i = 0; i < maindataary.length(); i++) {
                filterjson = maindataary.getJSONObject(i);
                final ArrayList optionList = new ArrayList();
                Log.v("filterjson", "" + filterjson);
                String label = filterjson.getString("frontend_label");
                Log.v("label", "" + label);
                LayoutInflater inflater = getLayoutInflater();
                View convertView =  inflater.inflate(R.layout.filter_item, null);
                view1 = (TextView) convertView.findViewById(R.id.tv_headerview);
                view2 = (TextView) convertView.findViewById(R.id.tv_spinerview);
                view1.setText(label);
                view2.setText("Select  " + label);
                view2.setTag(filterjson.getString("attribute_id"));

                view2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final TextView tv = (TextView) v;

                        for (int j = 0; j < maindataary.length(); j++) {
                            try {
                                innerjson = maindataary.getJSONObject(j);
                                String keys;
                                if (v.getTag().toString().equals(innerjson.getString("attribute_id"))) {
                                    //get option list from innerjson
                                    final JSONObject optionjson = innerjson.getJSONObject("options");
                                    Log.v("Options", "www" + optionjson);
                                    if (optionjson != null) {

                                        String inputype = innerjson.getString("inputtype");

                                        ArrayList<String> optionitemsList = new ArrayList<>();
                                        if (optionjson != null) {
                                            Iterator<?> iterator = optionjson.keys();
                                            while (iterator.hasNext()) {
                                                keys = (String) iterator.next();
                                                optionitemsList.add(keys);
                                            }
                                        }

                                        if (inputype.equals("select")) {

                                            String[] optionsAry;
                                            optionsAry = new String[optionitemsList.size()];
                                            optionsAry = optionitemsList.toArray(optionsAry);

                                            final String[] finalOptionsAry = optionsAry;

                                            new AlertDialog.Builder(mContext).setSingleChoiceItems(
                                                    optionsAry, 0,
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,
                                                                            int whichButton) {
                                                            dialog.dismiss();
                                                            String vall = null;
                                                            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                                                            Log.v("selected option", "" + finalOptionsAry[selectedPosition]);
                                                            try {
                                                                vall = optionjson.getString(finalOptionsAry[selectedPosition]);

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            tv.setText(finalOptionsAry[selectedPosition]);
                                                            JSONObject jj = new JSONObject();
                                                            try {
                                                                jj.put("attribute", innerjson.getString("code"));
                                                                jj.put("" + innerjson.getString("code"), vall);
                                                                jj.put("inputtype", "select");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            Log.v("selected json", "" + jj);
                                                            sendFilterArray.put(jj);

                                                        }
                                                    })/*.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //Your logic when OK button is clicked
                                                    }
                                                })
                                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                            }
                                                        })*/.show();

                                            break;

                                        } else {
                                            //Log.v("unselect", "" + "unselect" + optionitemsList.size());

                                            itemsSelected = new ArrayList();

                                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewFilterActivity.this);
                                            alertDialog.setTitle("Choose Options");


                                            itemAry = new String[optionitemsList.size()];

                                            itemAry = optionitemsList.toArray(itemAry);

                                            alertDialog.setMultiChoiceItems(itemAry, null, new DialogInterface.OnMultiChoiceClickListener() {

                                                //   int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                                                @Override
                                                public void onClick(DialogInterface dialog, int selectedItemId, boolean isSelected) {

                                                    //Log.v("selected option", "" + itemAry[selectedItemId]);
                                                    //vall = optionjson.getString(finalOptionsAry[selectedPosition]);

                                                    if (isSelected) {

                                                        try {
                                                            itemsSelected.add(optionjson.getString(itemAry[selectedItemId]));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        //itemsSelectedjson.put("values")
                                                        //itemsSelected.add(itemAry[selectedItemId]);
                                                        Log.v("isSelected", "bfr" + itemsSelected);

                                                    } else if (itemsSelected.contains(selectedItemId)) {
                                                        itemsSelected.remove(Integer.valueOf(selectedItemId));
                                                    }
                                                        Log.v("itemsSelected","bfr"+itemsSelected);
                                                 /* itemsSelected.clear();
                                                    try {
                                                        itemsSelected.add(optionjson.getString(itemAry[selectedItemId]));
                                                        itemsSelected.add(optionjson.getString(itemsSelected.get()));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Log.v("itemsSelected","aftr"+itemsSelected);*/
                                                }

                                            }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //Your logic when OK button is clicked

                                                    //String selectoption = ;


                                                    StringBuffer sb = new StringBuffer();
                                                    for (String s : itemsSelected) {
                                                        if (sb.length() != 0) sb.append(",");
                                                        sb.append(s);

                                                    }

                                                    JSONObject jj = new JSONObject();
                                                    try {
                                                        jj.put("attribute", innerjson.getString("code"));
                                                        jj.put("" + innerjson.getString("code"), sb.toString());
                                                        jj.put("inputtype", "Multiselect");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Log.v("selected jj", "" + jj);
                                                    sendFilterArray.put(jj);
                                                    tv.setText("Selected");
                                                }
                                            })
                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int id) {
                                                        }
                                                    }).show();


                                        }
                                        break;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                });
                Log.v("sendFilterArray ", "total" + sendFilterArray);
                filterLt.addView(convertView); //add to the layout


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void init(String cid) {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_FILTERDATA);
        JSONObject catejson = new JSONObject();
        try {
            //{"cat_id":18}
            catejson.put("cat_id",cid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gogglesAsynctask.execute(catejson.toString());

       // GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_FILTERDATA);

       // gogglesAsynctask.execute();
    }


    String itemAry[];
    ArrayList<String> itemsSelected;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void updateUi(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.reciveJson = receiveJSon;
            sInstance.msgview.setVisibility(View.GONE);
            sInstance.displayData(receiveJSon);


        }else{
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
            sInstance.linear_btnsave.setVisibility(View.GONE);
        }

    }
}
