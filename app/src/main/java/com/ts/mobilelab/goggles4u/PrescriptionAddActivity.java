package com.ts.mobilelab.goggles4u;


import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.app.DatePickerDialog;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.DatePickerDialog.OnDateSetListener;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class PrescriptionAddActivity extends AppCompatActivity {
    /* Copyright 2015 Wei Wang

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
     https://github.com/Lesilva/BetterSpinner
 */
    private Button nextBtn, cancelBtn;

    private EditText edtPrescName;
    private BetterSpinner prescriptiontypeSpinner, odRsphrSpinner, osLsphrSpinner, cyliodRspinner, cyliosLspinner, axisodRspinner;
    private BetterSpinner axisosLspinner, addodRspinner, addosLspinner, pdsinglespinner, pddualleftspinr, pddualritspinr;
    private BetterSpinner prsmodspnr, prsmosspnr, prismbaseoddirespnr, prismosbasedirspnr, selectSpinnerName;

    private LinearLayout prismlt;
    private Context mContext;
    private RadioGroup rgpd, rgprism;
    private static PrescriptionAddActivity sInstance;
    PreferenceData mPreferenceData;
    private RelativeLayout relative_prestype;
    String from, skuid;
    int selectPrescriptionPosition;
    private Double pd_min, pd_max;
    private RadioButton rbsinglepdbtn, rbdblpdbtn, rbyesbtn, rbnobtn;
    //JSONObject presuserdatajson;
    JSONObject edtjson;
    JSONArray reciveedtjsonAry;
    String productId, cartData;
    RelativeLayout homelt;
    private ScrollView scrollView;
    // Double pdmax,pdmin;
    int pressaveflag = 1;
    JSONArray optionArray = new JSONArray();
    JSONObject userpresdatajson;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    //int presSpinnerposition, odsphrpos, ossphpos, odcylp, oscylp, odaxp, osaxp, addodp, addosp, sglpdp, lpddp, rpddp, odprip, odbasep, osprimp, osbsep;
    JSONObject pricejson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPreferenceData = new PreferenceData();
        mContext = this;
        sInstance = this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        from = getIntent().getStringExtra("type");
        Log.v("from intent", "" + from);
        String reciveeditjson = getIntent().getStringExtra("jsonData");
       // Log.v("from reciveeditjson", "22" + reciveeditjson);
        initialise();
        userpresdatajson = new JSONObject();
        if (from.equals("fromedit")) {
            try {
                reciveedtjsonAry = new JSONArray(reciveeditjson);
                Log.v("from edit recivejson", "" + reciveedtjsonAry);

                nextBtn.setText("Save");
                setEditValue(reciveedtjsonAry);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (from.equals("products")) {
            //item_id":"111551","product_id":"46376"
            productId = getIntent().getStringExtra("productid");
            skuid = getIntent().getStringExtra("skuid");
            Log.v("productId", productId + "skuid" + skuid);

            nextBtn.setText("Select Lenses");
            cartData = getIntent().getStringExtra("cartdatajson");
            try {
                pricejson = new JSONObject(cartData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        init();


        selectSpinnerName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("onItemClick selectJsonAry", "" + prescTotalJsonAry.length());
                Log.v("val", "" + presNameList.get(position));
                String val = presNameList.get(position);
                if(!val.equals("None")){
                    setuserpresValue(position);
                }else{
                    try {
                        Log.v("totalmainjson",""+totalmainjson.getString("pdmax"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setPrescriptionOptionData(totalmainjson);
                }


                try {
                    userpresdatajson = prescTotalJsonAry.getJSONObject(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v("userpresdatajson",""+userpresdatajson);

                //pricejson.put(presNameList.get(position).get)


            }
        });

        prescriptiontypeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectPrescriptionPosition = position;
                Log.v("selectPrescriptionPosition", "222" + selectPrescriptionPosition);
            }
        });



    }

    private void setEditValue(JSONArray recivAry) throws JSONException {
        // [{"prescription_id":"606611","name":"7th ","modifiedby":null,"date":"5\/26\/2016 8:00 PM","prescription_data":{"prescription_type":{"label":"Prescription Type","value":"Distance Lenses","print_value":"Distance Lenses","opti
        for (int i = 0; i < recivAry.length(); i++) {
            edtjson = recivAry.getJSONObject(i);
            String name = edtjson.getString("name");
            edtPrescName.setText(name);
            JSONObject userdatajson = edtjson.getJSONObject("prescription_data");
            setjsonValue(userdatajson);
        }


    }

    private void setjsonValue(JSONObject userdatajson) throws JSONException {
        Log.v(" in setjsonValue",""+userdatajson);
        if (userdatajson.has("prescription_type")) {
            prescriptiontypeSpinner.setText(userdatajson.getJSONObject("prescription_type").getString("value"));
        }
        if (userdatajson.has("odsph")) {
            odRsphrSpinner.setText(userdatajson.getJSONObject("odsph").getString("value"));
        }
        if (userdatajson.has("ossph")) {
            osLsphrSpinner.setText(userdatajson.getJSONObject("ossph").getString("value"));
        }
        if (userdatajson.has("odcyl")) {
            cyliodRspinner.setText(userdatajson.getJSONObject("odcyl").getString("value"));
        }
        if (userdatajson.has("oscyl")) {
            cyliosLspinner.setText(userdatajson.getJSONObject("oscyl").getString("value"));
        }
        if (userdatajson.has("odaxis")) {
            axisodRspinner.setText(userdatajson.getJSONObject("odaxis").getString("value"));
        }
        if (userdatajson.has("osaxis")) {
            axisosLspinner.setText(userdatajson.getJSONObject("osaxis").getString("value"));
        }
        if (userdatajson.has("odadd")) {
            addodRspinner.setText(userdatajson.getJSONObject("odadd").getString("value"));
        }
        if (userdatajson.has("osadd")) {
            addosLspinner.setText(userdatajson.getJSONObject("osadd").getString("value"));
        }
        if (userdatajson.has("pd")) {
            Log.v("spd", "" + userdatajson.getJSONObject("pd").getString("value"));
            if (userdatajson.getJSONObject("pd").getString("value").equals("single")) {

                rbsinglepdbtn.setChecked(true);
                pddualleftspinr.setVisibility(View.GONE);
                pddualritspinr.setVisibility(View.GONE);
                pdsinglespinner.setVisibility(View.VISIBLE);
            } else {
                rbdblpdbtn.setChecked(true);
                pdsinglespinner.setVisibility(View.GONE);
                pddualleftspinr.setVisibility(View.VISIBLE);
                pddualritspinr.setVisibility(View.VISIBLE);
            }
            // pdsinglespinner.setText(userdatajson.getJSONObject("singlepd").getString("value"));
        }
        if (userdatajson.has("singlepd")) {
            pdsinglespinner.setText(userdatajson.getJSONObject("singlepd").getString("value"));
        }
        if (userdatajson.has("leftpd")) {
            pddualleftspinr.setText(userdatajson.getJSONObject("leftpd").getString("value"));
        }
        if (userdatajson.has("rightpd")) {
            pddualritspinr.setText(userdatajson.getJSONObject("rightpd").getString("value"));
        }
        if (userdatajson.getJSONObject("prism").getString("value").equals("yes")) {
            prismlt.setVisibility(View.VISIBLE);
            prismbaseoddirespnr.setText(userdatajson.getJSONObject("prismodbasedirection").getString("value"));
            prismosbasedirspnr.setText(userdatajson.getJSONObject("prismosbasedirection").getString("value"));
            prsmodspnr.setText(userdatajson.getJSONObject("prismod").getString("value"));
            prsmosspnr.setText(userdatajson.getJSONObject("prismos").getString("value"));

        } else {
            prismlt.setVisibility(View.GONE);
        }
    }

    private void setuserpresValue(int position) {

        try {
            // for(int i=0;i<selectJsonAry.length();i++){
            JSONObject selectpresjson = prescTotalJsonAry.getJSONObject(position);
           // pricejson.put("prescription_id",selectpresjson.getString("prescription_id"));
            Log.v("selectpresjson", selectpresjson+"eee");
            JSONObject userpresjson = selectpresjson.getJSONObject("prescription_data");

            Log.v("userdatajson", "eee" + userpresjson);
            setjsonValue(userpresjson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getStorePrescriptionData() {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_GETPRESCRIPTION_LIST);
        JSONObject productjson = new JSONObject();

        try {
            productjson.put("customer_id", mPreferenceData.getCustomerId());

            //{"sku" :"112921-c"}
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gogglesAsynctask.execute(productjson.toString());
    }


    private void init() {
        JSONObject productjson = new JSONObject();
        Log.v("init from", "" + from);
        if (from.equals("fromedit") || from.equals("fromadd")) {
            edtPrescName.setVisibility(View.VISIBLE);
            relative_prestype.setVisibility(View.GONE);
            prescriptiontypeSpinner.setVisibility(View.GONE);
            selectSpinnerName.setVisibility(View.GONE);

            GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_PRESCRIPTION_OPTION);
            try {
                productjson.put("sku", "112921-c");
                productjson.put("is_customer", "true");//will change
            } catch (JSONException e) {
                e.printStackTrace();
            }
            gogglesAsynctask.execute(productjson.toString());

        } else if (from.equals("products")) {
            Log.v("skuid", "products" + skuid);
            edtPrescName.setVisibility(View.GONE);
            GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_LENSE_DETAILS);

            try {
                productjson.put("sku", skuid);
                productjson.put("is_customer", "false");
            } catch (JSONException e) {
                e.printStackTrace();
            }
           Log.v("productjson", "products" + productjson);
            gogglesAsynctask.execute(productjson.toString());
        }
    }

    public static void show_dialog(Context cxt, String msg) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(cxt);
        alert.setMessage(msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alert.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean flag = false;
    private Double totalprice = 0.0;
    private boolean pdselectflag = false;
    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.v("in", "submitListener");

            String prescname = edtPrescName.getText().toString();

            String slctprecname = selectSpinnerName.getText().toString();
            if(slctprecname.contains("Select")){
                slctprecname = "";
            }

            String prestype = prescriptiontypeSpinner.getText().toString();
            if(prestype.contains("Prescription")){
                prestype = "";
            }
            String sphRod = odRsphrSpinner.getText().toString();
            if(sphRod.equals("Select")){
                sphRod = "";
            }
            String sphLos = osLsphrSpinner.getText().toString();
            if(sphLos.equals("Select")){
                sphLos = "";
            }
            String cyliRod = cyliosLspinner.getText().toString();
            if(cyliRod.equals("Select")){
                cyliRod = "";
            }

            String cyliLos = cyliodRspinner.getText().toString();
            if(cyliLos.equals("Select")){
                cyliLos = "";
            }
            String axisRod = axisodRspinner.getText().toString();
            if(axisRod.equals("Select")){
                axisRod = "";
            }
            String axisLos = axisosLspinner.getText().toString();
            if(axisLos.equals("Select")){
                axisLos = "";
            }
            String addLos = addosLspinner.getText().toString();
            if(addLos.equals("Select")){
                addLos = "";
            }
            String addRod = addodRspinner.getText().toString();
            if(addRod.equals("Select")){
                addRod = "";
            }
            String pdsingle = pdsinglespinner.getText().toString();
            if(pdsingle.equals("Select")){
                pdsingle = "";
            }
            String pddblLeft = pddualleftspinr.getText().toString();
            if(pddblLeft.contains("Select")){
                pddblLeft = "";
            }
            String pddblRight = pddualritspinr.getText().toString();
            if(pddblRight.contains("Select")){
                pddblRight = "";
            }

            String prismodval = prsmodspnr.getText().toString();
            if(prismodval.equals("Select")){
                prismodval = "";
            }
            String prismoddir = prismbaseoddirespnr.getText().toString();
            if(prismoddir.equals("Select")){
                prismoddir = "";
            }
            String baseosval = prsmosspnr.getText().toString();
            if(baseosval.equals("Select")){
                baseosval = "";
            }
            String baseosdir = prismosbasedirspnr.getText().toString();
            if(baseosdir.equals("Select")){
                baseosdir = "";
            }



            if(!slctprecname.isEmpty()){
                pressaveflag = 2;
            }
           // Log.v("slctprecname","22"+slctprecname);

         /*  if(prestype.equals("select")){
                show_dialog(mContext,"Select Prescriptiontype");

            }else*/
            //Log.v(" Double cyliRod",""+Double.parseDouble(cyliRod));
            // Log.v("prescriptiontypeSpinner ","posi"+prescriptiontypeSpinner.setOnItemSelectedListener()));
           // Log.v("rbsinglepdbtn",""+rbsinglepdbtn.isChecked());
            //Log.v("rbdblpdbtn",""+rbdblpdbtn.isChecked());
            Log.v("rbyesbtn","from"+from +"----"+rbyesbtn.isChecked());
            if (from.equals("products")) {
                if (TextUtils.isEmpty(prestype)) {
                    show_dialog(mContext, "Select Prescription type");
                    flag = false;
                    return;
                }
                Log.v("prestype","start"+prestype);
                if(prestype.equals("Bifocal Lenses") || prestype.equals("Progressive Lenses")){ //Condition 1 start
                    Log.v("prestype","addod"+addRod+"innner"+prestype);
                    if (TextUtils.isEmpty(addRod) || TextUtils.isEmpty(addLos)) {

                        show_dialog(mContext, "Select Add OD and OS Value");
                        return;                                                     //Condition 1 end
                    }
                }
            }

            if (from.equals("fromedit") || from.equals("fromadd")) {

                if (TextUtils.isEmpty(prescname)) {
                    show_dialog(mContext, "Enter Prescription name.");
                    flag = false;
                    return;
                }
            }


            if (TextUtils.isEmpty(sphRod) && TextUtils.isEmpty(sphLos)) {  //con 2
                    Log.v("sphRod inside", "" + sphRod);
                    show_dialog(mContext, "Select sphere Value");
                    return;
            }

            if ((!TextUtils.isEmpty(cyliRod) && TextUtils.isEmpty(cyliLos)) || (TextUtils.isEmpty(cyliRod) && !TextUtils.isEmpty(cyliLos))) {
                show_dialog(mContext, "Select Cylinder OD and OS Value");
                flag = false;
                return;
            }



            if (!TextUtils.isEmpty(cyliRod) && !TextUtils.isEmpty(cyliLos)) {
                Log.v("cyliRod inside", "353" + cyliRod);
                if ((Double.parseDouble(cyliRod) < 0d && Double.parseDouble(cyliLos) > 0d) || (Double.parseDouble(cyliLos) < 0d && Double.parseDouble(cyliRod) > 0d)) {
                    show_dialog(mContext, "Cylinder values must be both positive or negative");
                    flag = false;
                    return;
                } else if ((!cyliRod.isEmpty() && axisRod.isEmpty()) || (cyliRod.isEmpty() && !axisRod.isEmpty())) {
                    //Log.v("cyliRod inside", "" + cyliRod);
                    //Log.v("axisRod inside", "" + axisRod);
                    show_dialog(mContext, "Please Check Entered values or contact live chat available 24/7");
                    flag = false;
                    return;
                }
            }
            if ((!cyliLos.isEmpty() && axisLos.isEmpty()) || (cyliLos.isEmpty() && !axisLos.isEmpty())) {
                Log.v("cyliLos inside", "" + sphRod);
                show_dialog(mContext, "Please Check Entered values or contact live chat available 24/7");
                flag = false;
                return; // cyliLos  and axisLos is compolsory?
            }


            if ((!addRod.isEmpty() && addLos.isEmpty()) || (addRod.isEmpty() && !addLos.isEmpty())) {
                // Log.v("addRod inside", "33" + sphRod);
                show_dialog(mContext, "Please select OD & OS Add values");
                flag = false;
                return;
            }  // add od and os is compolsory?
            if (rbsinglepdbtn.isChecked()) {
                Log.v("pdsingle", "11111111111111111111111111" + pdsingle);

                if (TextUtils.isEmpty(pdsingle)) {
                    show_dialog(mContext, "Please select Single PD Value");
                    flag = false;
                    return;
                } else if (Double.parseDouble(pdsingle) < pd_min || (Double.parseDouble(pdsingle) > pd_max)) {
                    show_dialog(mContext, "This Frame is not suitable for your PD. Please select a different frame");
                    flag = false;
                    return;
                }
            }


             if (rbdblpdbtn.isChecked()) {

                // Log.v("rbdblpdbtn", pddblRight + "PD" + pddblLeft);
                if (TextUtils.isEmpty(pddblLeft) || TextUtils.isEmpty(pddblRight)) {
                    show_dialog(mContext, "Please select Left & Right PD");
                    flag = false;
                    return;
                } else if (Double.parseDouble(pddblLeft) < pd_min / 2 || Double.parseDouble(pddblLeft) > pd_max / 2) {
                    show_dialog(mContext, " This Frame is not suitable for your PD. Please select a different frame.");
                    flag = false;
                    return;
                } else if (Double.parseDouble(pddblRight) < pd_min / 2 || Double.parseDouble(pddblRight) > pd_max / 2) {
                    show_dialog(mContext, " This Frame is not suitable for your PD. Please select a different frame.");
                    flag = false;
                    return;
                }
            }

            //Log.v("pdselectflag",""+pdselectflag);
            if(!pdselectflag){
                show_dialog(mContext, "Please select  PD value");
                flag = false;
                return;
            }

            if(rbyesbtn.isChecked()) {

                if (TextUtils.isEmpty(prismodval) || TextUtils.isEmpty(baseosval)) {
                    show_dialog(mContext, "Please select prism options");
                    flag = false;
                    return;
                } else if (TextUtils.isEmpty(prismoddir) || TextUtils.isEmpty(baseosdir)) {
                    show_dialog(mContext, " Please select prism direction options");
                    flag = false;
                    return;
                }
            }





            Log.v("prescription mainjson", "  flag : " + flag + "  else : " + from);
            //else{


            // {"product_id":"ID","options[<option_id>]":"<option_val_id>","changedprice":"<price>"}
            //{"prescription_id":111,"prescription_name":"test","name":"surjan","customer_id":6,"email":"surjan.negi@gmail.com","prescription_options" : [{"code":"ODSPH","label":"ODSPH","option_id":981279,"option_value":"-6.00"},
            //   {"code":"PD","label":"PD","option_id":981287,"option_value":"SinglePD"},{"code":"SinglePD","label":"SinglePD","option_id":981288,"option_value":"61"}]}


           // if (flag) {
                //  x : " + Arrays.asList(prescriptionTypeArray).indexOf(prescriptiontypeSpinner.getText().toString()));
                Log.v("presc. ype", "" + Arrays.asList(prescriptionTypeArray).indexOf(prescriptiontypeSpinner.getText().toString()));
                Log.v("totalprice", "" + totalprice);


                try {
                   // Log.v("totalprice pres", "" + mainjson.getJSONObject("prescription_type").getJSONArray("vals").getJSONObject(Arrays.asList(prescriptionTypeArray).indexOf(prescriptiontypeSpinner.getText().toString())).getString("price"));

                    if (!prestype.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("prescription_type").getJSONArray("vals").getJSONObject(Arrays.asList(prescriptionTypeArray).indexOf(prescriptiontypeSpinner.getText().toString()) ).getString("price"));
                    } else if (!sphLos.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("ossph").getJSONArray("vals").getJSONObject(Arrays.asList(ossphrAray).indexOf(osLsphrSpinner.getText().toString()) ).getString("price_val"));
                    } else if (!sphRod.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("odsph").getJSONArray("vals").getJSONObject(Arrays.asList(odsphrArray).indexOf(odRsphrSpinner.getText().toString())).getString("price_val"));
                    } else if (!cyliRod.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("odcyl").getJSONArray("vals").getJSONObject(Arrays.asList(odcylAray).indexOf(cyliodRspinner.getText().toString())).getString("price_val"));
                    } else if (!cyliLos.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("oscyl").getJSONArray("vals").getJSONObject(Arrays.asList(oscylAray).indexOf(cyliosLspinner.getText().toString()) ).getString("price_val"));
                    } else if (!axisRod.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("odaxis").getJSONArray("vals").getJSONObject(Arrays.asList(odAxisAray).indexOf(odRsphrSpinner.getText().toString())).getString("price_val"));
                    } else if (!axisLos.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("odcyl").getJSONArray("vals").getJSONObject(Arrays.asList(osaxisAray).indexOf(osLsphrSpinner.getText().toString())).getString("price_val"));
                    } else if (!addRod.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("odadd").getJSONArray("vals").getJSONObject(Arrays.asList(odaddAray).indexOf(addodRspinner.getText().toString()) ).getString("price_val"));
                    } else if (!addLos.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("osadd").getJSONArray("vals").getJSONObject(Arrays.asList(osaddAray).indexOf(addosLspinner.getText().toString())).getString("price_val"));
                    } else if (!pdsingle.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("singlepd").getJSONArray("vals").getJSONObject(Arrays.asList(pdsinglAry).indexOf(pdsinglespinner.getText().toString()) ).getString("price_val"));
                    } else if (!pddblLeft.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("leftpd").getJSONArray("vals").getJSONObject(Arrays.asList(pddblLeft).indexOf(pddualleftspinr.getText().toString()) ).getString("price_val"));
                    } else if (!pddblRight.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("rightpd").getJSONArray("vals").getJSONObject(Arrays.asList(pddblRight).indexOf(pddualritspinr.getText().toString())).getString("price_val"));
                    } else if (!prismodval.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("prismod").getJSONArray("vals").getJSONObject(Arrays.asList(prismodAry).indexOf(prsmodspnr.getText().toString()) ).getString("price_val"));
                    } else if (!prismoddir.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("prismodbasedirection").getJSONArray("vals").getJSONObject(Arrays.asList(prismodbaseDirectionAry).indexOf(prismbaseoddirespnr.getText().toString())).getString("price_val"));
                    } else if (!baseosval.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("prismos").getJSONArray("vals").getJSONObject(Arrays.asList(prismosAry).indexOf(prsmosspnr.getText().toString()) ).getString("price_val"));
                    } else if (!baseosdir.isEmpty()) {
                        totalprice += Double.parseDouble(mainjson.getJSONObject("prismosbasedirection").getJSONArray("vals").getJSONObject(Arrays.asList(prismosbaseDirectionAry).indexOf(prismosbasedirspnr.getText().toString())).getString("price_val"));
                    }

                    Log.v("totalprice", "" + totalprice);
                    if (from.equals("products")) {
                        saveOptionData();
                        pricejson.put("changedprice", totalprice);
                        pricejson.put("options", presjson);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (from.equals("fromedit")) {
                    Log.v("prescription mainjson", "" + mainjson);

                    try {
                        savepresjson.put("prescription_id", edtjson.getString("prescription_id"));
                        savepresjson.put("prescription_name", edtPrescName.getText().toString());
                        savepresjson.put("name", mPreferenceData.getCustomerFName());
                        savepresjson.put("customer_id", mPreferenceData.getCustomerId());
                        savepresjson.put("email", mPreferenceData.getCustomerMailId());
                        //savepresjson.put("name",mPreferenceData.getCustomerFName());
                        sendSaveData(11);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("savepresjson", "" + savepresjson);


                } else if (from.equals("fromadd")) {
                    Log.v("prescription fromadd", "" + from);

                    try {
                        savepresjson.put("prescription_name", edtPrescName.getText().toString());
                        savepresjson.put("name", mPreferenceData.getCustomerFName());
                        savepresjson.put("customer_id", mPreferenceData.getCustomerId());
                        savepresjson.put("email", mPreferenceData.getCustomerMailId());
                        sendSaveData(11);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //savepresjson.put("name",mPreferenceData.getCustomerFName());
                    //{"option_value_id":2431991,"option_id":991892,"prescriptionType":"distance",
                    //"odsph":"-6.00","odcyl":"-2.25","ossph":"-6.00","oscyl":"","osaxis":"1","odaxis":"","product_id":49622,
                    // "is_cosmetic":0,"pdmax":"","pdmin":"","pdsinglechecked":1,"pddualchecked":0,"singlepd":"","pdleft":"",
                    // "pdright":"","prism":"","prismod":"","prismos":"","prismosdirection":"","prismoddirection":""}
                } else if (from.equals("products")) {

                    try {
                        savepresjson.put("option_id", mainjson.getJSONObject("prescription_type").getString("option_id"));
                        savepresjson.put("option_value_id", mainjson.getJSONObject("prescription_type").getJSONArray("vals").getJSONObject(selectPrescriptionPosition).getString("option_val_id"));
                        savepresjson.put("prescriptionType", mainjson.getJSONObject("prescription_type").getJSONArray("vals").getJSONObject(selectPrescriptionPosition).getString("sku"));


                        //savepresjson.put("option_value_id", mainjson.getJSONObject("prescription_type").getJSONArray("vals").getJSONObject(selectPrescriptionPosition).getString("option_val_id"));


                        savepresjson.put("odsph", sphRod);
                        savepresjson.put("ossph", sphLos);
                        savepresjson.put("odcyl", cyliRod);
                        savepresjson.put("oscyl", cyliLos);
                        savepresjson.put("odaxis", addRod);
                        savepresjson.put("osaxis", axisLos);
                        savepresjson.put("product_id", productId);
                        savepresjson.put("is_cosmetic", 0);
                        savepresjson.put("pdmax", "");
                        savepresjson.put("pdmin", "");
                        if (rbsinglepdbtn.isChecked()) {
                            savepresjson.put("pdsinglechecked", 1);
                            savepresjson.put("pddualchecked", 0);
                        } else {
                            savepresjson.put("pdsinglechecked", 0);
                            savepresjson.put("pddualchecked", 1);
                        }


                        savepresjson.put("singlepd", pdsingle);
                        savepresjson.put("pdleft", pddblLeft);
                        savepresjson.put("pdright", pddblRight);
                        if (rbyesbtn.isChecked()) {
                            savepresjson.put("prism", 1);
                        } else {
                            savepresjson.put("prism", 0);
                        }
                        savepresjson.put("prismod", prismodval);
                        savepresjson.put("prismos", baseosval);
                        savepresjson.put("prismosdirection", baseosdir);
                        savepresjson.put("prismoddirection", prismoddir);

                        sendProductData(savepresjson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            //}


        }
    };


    private void sendProductData(JSONObject savepresjson) {
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_LENSEOPTIONS);
        gogglesAsynctask.execute(savepresjson.toString());


    }


    private ArrayAdapter<String> getData(String[] array) {
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  R.layout.spinner_row, array);
       // array.ad
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_row, array);
        return adapter;
    }

    JSONObject presjson = new JSONObject();

    private void saveOptionData() {


        try {
            Log.v("mainjson", "" + mainjson);

            presjson.put(mainjson.getJSONObject("prescription_type").getString("option_id"), mainjson.getJSONObject("prescription_type").getJSONArray("vals").getJSONObject(Arrays.asList(prescriptionTypeArray).indexOf(prescriptiontypeSpinner.getText().toString())).getString("option_val_id"));
            Log.v("option_id", "" + mainjson.getJSONObject("prescription_type").getString("option_id"));
            presjson.put(mainjson.getJSONObject("odsph").getString("option_id"), odRsphrSpinner.getText().toString());
            Log.v("ossph", "" + mainjson.getJSONObject("ossph").getString("option_id"));
            Log.v("oscyl", "" + mainjson.getJSONObject("oscyl").getString("option_id"));
            Log.v("odcyl", "" + mainjson.getJSONObject("odcyl").getString("option_id"));


            presjson.put(mainjson.getJSONObject("ossph").getString("option_id"), osLsphrSpinner.getText().toString());


            presjson.put(mainjson.getJSONObject("oscyl").getString("option_id"), cyliosLspinner.getText().toString());


            presjson.put(mainjson.getJSONObject("odcyl").getString("option_id"), cyliodRspinner.getText().toString());


            presjson.put(mainjson.getJSONObject("odaxis").getString("option_id"), axisodRspinner.getText().toString());


            presjson.put(mainjson.getJSONObject("osaxis").getString("option_id"), axisosLspinner.getText().toString());


            presjson.put(mainjson.getJSONObject("odadd").getString("option_id"), addodRspinner.getText().toString());


            presjson.put(mainjson.getJSONObject("osadd").getString("option_id"), addosLspinner.getText().toString());


            if (rbsinglepdbtn.isChecked()) {

                presjson.put(mainjson.getJSONObject("singlepd").getString("option_id"), pdsinglespinner.getText().toString());

            }

            if (rbdblpdbtn.isChecked()) {

                presjson.put(mainjson.getJSONObject("leftpd").getString("option_id"), pddualleftspinr.getText().toString());

                presjson.put(mainjson.getJSONObject("rightpd").getString("option_id"), pddualritspinr.getText().toString());


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendSaveData(int i) throws JSONException {

        JSONArray saveAry = new JSONArray();


        JSONObject odsphr = new JSONObject();
        Log.v("odsph", "" + mainjson.getJSONObject("odsph").getString("option_id"));
        odsphr.put("code", mainjson.getJSONObject("odsph").getString("title"));
        odsphr.put("label", mainjson.getJSONObject("odsph").getString("title"));
        //odsphr.put("label",edtjson.getJSONObject("prescription_data").getJSONObject("prescription_type").getString("label"));
        odsphr.put("option_id", mainjson.getJSONObject("odsph").getString("option_id"));
        odsphr.put("option_value", odRsphrSpinner.getText().toString());
        saveAry.put(odsphr);

        JSONObject ossphrjson = new JSONObject();
        ossphrjson.put("code", mainjson.getJSONObject("ossph").getString("title"));
        ossphrjson.put("label", mainjson.getJSONObject("ossph").getString("title"));

        ossphrjson.put("option_id", mainjson.getJSONObject("ossph").getString("option_id"));
        ossphrjson.put("option_value", osLsphrSpinner.getText().toString());
        saveAry.put(odsphr);
        JSONObject oscyljson = new JSONObject();
        oscyljson.put("code", mainjson.getJSONObject("oscyl").getString("title"));
        oscyljson.put("label", mainjson.getJSONObject("oscyl").getString("title"));

        oscyljson.put("option_id", mainjson.getJSONObject("oscyl").getString("option_id"));
        oscyljson.put("option_value", cyliosLspinner.getText().toString());
        saveAry.put(oscyljson);
        JSONObject odcyljson = new JSONObject();
        odcyljson.put("code", mainjson.getJSONObject("odcyl").getString("title"));
        odcyljson.put("label", mainjson.getJSONObject("odcyl").getString("title"));

        odcyljson.put("option_id", mainjson.getJSONObject("odcyl").getString("option_id"));
        odcyljson.put("option_value", cyliodRspinner.getText().toString());
        saveAry.put(odcyljson);
        JSONObject odaxisjson = new JSONObject();
        odaxisjson.put("code", mainjson.getJSONObject("odaxis").getString("title"));
        odaxisjson.put("label", mainjson.getJSONObject("odaxis").getString("title"));
        odaxisjson.put("option_id", mainjson.getJSONObject("odaxis").getString("option_id"));
        odaxisjson.put("option_value", axisodRspinner.getText().toString());
        saveAry.put(odaxisjson);
        JSONObject osaxisjson = new JSONObject();
        osaxisjson.put("code", mainjson.getJSONObject("osaxis").getString("title"));
        osaxisjson.put("label", mainjson.getJSONObject("osaxis").getString("title"));
        osaxisjson.put("option_id", mainjson.getJSONObject("osaxis").getString("option_id"));
        osaxisjson.put("option_value", axisosLspinner.getText().toString());
        saveAry.put(osaxisjson);
        JSONObject odaddjson = new JSONObject();
        odaddjson.put("code", mainjson.getJSONObject("odadd").getString("title"));
        odaddjson.put("label", mainjson.getJSONObject("odadd").getString("title"));
        odaddjson.put("option_id", mainjson.getJSONObject("odadd").getString("option_id"));
        odaddjson.put("option_value", addodRspinner.getText().toString());
        saveAry.put(odaddjson);
        JSONObject osaddjson = new JSONObject();
        osaddjson.put("code", mainjson.getJSONObject("osadd").getString("title"));
        osaddjson.put("label", mainjson.getJSONObject("osadd").getString("title"));
        osaddjson.put("option_id", mainjson.getJSONObject("osadd").getString("option_id"));
        osaddjson.put("option_value", addosLspinner.getText().toString());
        saveAry.put(osaddjson);
        JSONObject pdjson = new JSONObject();
        if (rbsinglepdbtn.isChecked()) {
            pdjson.put("code", mainjson.getJSONObject("pd").getString("title"));
            pdjson.put("label", mainjson.getJSONObject("pd").getString("title"));
            pdjson.put("option_value", "single");
            saveAry.put(pdjson);
        } else {
            pdjson.put("code", mainjson.getJSONObject("pd").getString("title"));
            pdjson.put("label", mainjson.getJSONObject("pd").getString("title"));
            pdjson.put("option_value", "dual");
            saveAry.put(pdjson);
        }

        JSONObject singlepdjson = new JSONObject();
        singlepdjson.put("code", mainjson.getJSONObject("singlepd").getString("title"));
        singlepdjson.put("label", mainjson.getJSONObject("singlepd").getString("title"));
        singlepdjson.put("option_id", mainjson.getJSONObject("singlepd").getString("option_id"));
        singlepdjson.put("option_value", pdsinglespinner.getText().toString());
        saveAry.put(singlepdjson);

        JSONObject leftpdjson = new JSONObject();
        leftpdjson.put("code", mainjson.getJSONObject("leftpd").getString("title"));
        leftpdjson.put("label", mainjson.getJSONObject("leftpd").getString("title"));
        leftpdjson.put("option_id", mainjson.getJSONObject("leftpd").getString("option_id"));
        leftpdjson.put("option_value", pddualleftspinr.getText().toString());
        saveAry.put(leftpdjson);

        JSONObject rightpdjson = new JSONObject();
        rightpdjson.put("code", mainjson.getJSONObject("rightpd").getString("title"));
        rightpdjson.put("label", mainjson.getJSONObject("rightpd").getString("title"));
        rightpdjson.put("option_id", mainjson.getJSONObject("rightpd").getString("option_id"));
        rightpdjson.put("option_value", pddualritspinr.getText().toString());
        saveAry.put(rightpdjson);
        JSONObject prismjson = new JSONObject();
        if (rbyesbtn.isChecked()) {
            prismjson.put("code", "Prism");
            prismjson.put("label", "Prism");
            prismjson.put("value", "Yes");
            saveAry.put(prismjson);
        } else {
            prismjson.put("code", "Prism");
            prismjson.put("label", "Prism");
            prismjson.put("value", "No");
            saveAry.put(prismjson);
        }
        JSONObject prisomosjson = new JSONObject();
        prisomosjson.put("code", mainjson.getJSONObject("prismos").getString("title"));
        prisomosjson.put("label", mainjson.getJSONObject("prismos").getString("title"));
        prisomosjson.put("option_id", mainjson.getJSONObject("prismos").getString("option_id"));
        prisomosjson.put("option_value", prsmosspnr.getText().toString());
        saveAry.put(prisomosjson);

        JSONObject prisomosdirjson = new JSONObject();
        prisomosdirjson.put("code", mainjson.getJSONObject("prismosbasedirection").getString("title"));
        prisomosdirjson.put("label", mainjson.getJSONObject("prismosbasedirection").getString("title"));
        prisomosdirjson.put("option_id", mainjson.getJSONObject("prismosbasedirection").getString("option_id"));
        prisomosdirjson.put("option_value", prismosbasedirspnr.getText().toString());
        saveAry.put(prisomosdirjson);

        JSONObject prisomodjson = new JSONObject();
        prisomodjson.put("code", mainjson.getJSONObject("prismod").getString("title"));
        prisomodjson.put("label", mainjson.getJSONObject("prismod").getString("title"));
        prisomodjson.put("option_id", mainjson.getJSONObject("prismod").getString("option_id"));
        prisomodjson.put("option_value", prsmodspnr.getText().toString());
        saveAry.put(prisomodjson);


        JSONObject prisomoddirjson = new JSONObject();
        prisomoddirjson.put("code", mainjson.getJSONObject("prismodbasedirection").getString("title"));
        prisomoddirjson.put("label", mainjson.getJSONObject("prismodbasedirection").getString("title"));
        prisomoddirjson.put("option_id", mainjson.getJSONObject("prismodbasedirection").getString("option_id"));
        prisomoddirjson.put("option_value", prismbaseoddirespnr.getText().toString());
        saveAry.put(prisomoddirjson);

        savepresjson.put("prescription_options", saveAry);
        if (i == 111) {
            pricejson.put("options", saveAry);
        } else {
            GogglesAsynctask saveAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_SAVEPRESCRIPTION);
            saveAsynctask.execute(savepresjson.toString());
        }


    }
    JSONObject totalmainjson;
//mainjson
    private void setPrescriptionOptionData(JSONObject receiveJSon) {


       // Log.v("setData", "from " + from + " receiveJSon" + receiveJSon);
        try {
           // if (from.equals("fromedit") || from.equals("fromadd")) {
                pd_max = Double.valueOf(receiveJSon.getString("pdmax"));
                pd_min = Double.valueOf(receiveJSon.getString("pdmin"));
            //}
            mainjson = receiveJSon.getJSONObject("options");
            if (mainjson.has("prescription_type")) {
                //prescription json
                JSONObject presjson = mainjson.getJSONObject("prescription_type");
               // Log.v("presjson", "11" + presjson);
                JSONArray presarray = presjson.getJSONArray("vals");

                prescriptionTypeArray = new String[presarray.length()];
                for (int i = 0; i < presarray.length(); i++) {
                    JSONObject pjson = presarray.getJSONObject(i);
                    prescriptionTypeArray[i] = pjson.getString("title");

                }

                prescriptiontypeSpinner.setAdapter(getData(prescriptionTypeArray));

            }
            if (mainjson.has("prismod")) {
                //prisomod json
                JSONObject prismodjson = mainjson.getJSONObject("prismod");
                //Log.v("presjson", "11" + prismodjson);
                JSONArray prismodarray = prismodjson.getJSONArray("vals");

                prismodAry = new String[prismodarray.length()];
                for (int i = 0; i < prismodarray.length(); i++) {
                    JSONObject prismjson = prismodarray.getJSONObject(i);
                    // String label = pjson.getString("title");
                    prismodAry[i] = prismjson.getString("title");
                }
                // prsmodspnr,prsmosspnr,prismbaseoddirespnr,prismosbasedirspnr;
                prsmodspnr.setAdapter(getData(prismodAry));

            } else {
                prsmodspnr.setVisibility(View.GONE);
            }
            //prismodbasedirection json
            if (mainjson.has("prismodbasedirection")) {
                JSONObject prismodbasedirectionjson = mainjson.getJSONObject("prismodbasedirection");
                JSONArray prismodbasedirectionarray = prismodbasedirectionjson.getJSONArray("vals");

                prismodbaseDirectionAry = new String[prismodbasedirectionarray.length()];
                for (int i = 0; i < prismodbasedirectionarray.length(); i++) {
                    JSONObject prismjson = prismodbasedirectionarray.getJSONObject(i);
                    // String label = pjson.getString("title");
                    prismodbaseDirectionAry[i] = prismjson.getString("title");
                }
                prismbaseoddirespnr.setAdapter(getData(prismodbaseDirectionAry));
            } else {
                prismbaseoddirespnr.setVisibility(View.GONE);
            }

            //prisoms json
            if (mainjson.has("prismos")) {
                JSONObject prismosjson = mainjson.getJSONObject("prismos");
                //Log.v("presjson", "11" + prismosjson);
                JSONArray prismosarray = prismosjson.getJSONArray("vals");

                prismosAry = new String[prismosarray.length()];
                for (int i = 0; i < prismosarray.length(); i++) {
                    JSONObject prismjson = prismosarray.getJSONObject(i);
                    // String label = pjson.getString("title");
                    prismosAry[i] = prismjson.getString("title");
                }

                prsmosspnr.setAdapter(getData(prismosAry));
            } else {
                prsmosspnr.setVisibility(View.GONE);
            }

            //prismosbasedirection json
            if (mainjson.has("prismosbasedirection")) {
                JSONObject prismosbasedirectionjson = mainjson.getJSONObject("prismosbasedirection");
                //Log.v("presjson", "11" + prismosbasedirectionjson);
                JSONArray prismosbasedirectionjsonarray = prismosbasedirectionjson.getJSONArray("vals");

                prismosbaseDirectionAry = new String[prismosbasedirectionjsonarray.length()];
                for (int i = 0; i < prismosbasedirectionjsonarray.length(); i++) {
                    JSONObject prismjson = prismosbasedirectionjsonarray.getJSONObject(i);

                    prismosbaseDirectionAry[i] = prismjson.getString("title");
                }
                prismosbasedirspnr.setAdapter(getData(prismosbaseDirectionAry));
            } else {
                prismosbasedirspnr.setVisibility(View.GONE);
            }

            //odsphr json
            if (mainjson.has("odsph")) {
                JSONObject odsphjson = mainjson.getJSONObject("odsph");

                JSONArray odsphjsonarray = odsphjson.getJSONArray("vals");

                odsphrArray = new String[odsphjsonarray.length()];
                for (int i = 0; i < odsphjsonarray.length(); i++) {
                    JSONObject odsphrjson = odsphjsonarray.getJSONObject(i);
                    // String label = pjson.getString("title");
                    odsphrArray[i] = odsphrjson.getString("title");
                }

                odRsphrSpinner.setAdapter(getData(odsphrArray));
            } else {
                odRsphrSpinner.setVisibility(View.GONE);
            }
            //odcyl json
            if (mainjson.has("odcyl")) {
                JSONObject odcyljson = mainjson.getJSONObject("odcyl");
                //Log.v("presjson", "11" + odcyljson);
                JSONArray odcyljsonarray = odcyljson.getJSONArray("vals");

                odcylAray = new String[odcyljsonarray.length()];
                for (int i = 0; i < odcyljsonarray.length(); i++) {
                    JSONObject json = odcyljsonarray.getJSONObject(i);
                    odcylAray[i] = json.getString("title");
                }
                cyliodRspinner.setAdapter(getData(odcylAray));
            } else {
                cyliodRspinner.setVisibility(View.GONE);
            }

            //odaxis json
            if (mainjson.has("odaxis")) {
                JSONObject odaxisjson = mainjson.getJSONObject("odaxis");
                //Log.v("presjson", "11" + odaxisjson);
                JSONArray odaxisjsonjsonarray = odaxisjson.getJSONArray("vals");

                odAxisAray = new String[odaxisjsonjsonarray.length()];
                for (int i = 0; i < odaxisjsonjsonarray.length(); i++) {
                    JSONObject json = odaxisjsonjsonarray.getJSONObject(i);
                    odAxisAray[i] = json.getString("title");
                }

                axisodRspinner.setAdapter(getData(odAxisAray));
            } else {
                axisodRspinner.setVisibility(View.GONE);
            }
            //odadd json
            if (mainjson.has("odadd")) {
                JSONObject odaddjson = mainjson.getJSONObject("odadd");
                //Log.v("odaddjson", "11" + odaddjson);
                JSONArray odaddjsonarray = odaddjson.getJSONArray("vals");
                //Log.v("odaddjsonarray","11"+odaddjsonarray);
                odaddAray = new String[odaddjsonarray.length()];
                for (int i = 0; i < odaddjsonarray.length(); i++) {
                    JSONObject json = odaddjsonarray.getJSONObject(i);
                    odaddAray[i] = json.getString("title");
                }
                addodRspinner.setAdapter(getData(odaddAray));
            } else {
                addodRspinner.setVisibility(View.GONE);
            }
            //ossphere json
            if (mainjson.has("ossph")) {
                JSONObject ossphjson = mainjson.getJSONObject("ossph");
                //Log.v("presjson", "11" + ossphjson);
                JSONArray ossphjsonarray = ossphjson.getJSONArray("vals");

                ossphrAray = new String[ossphjsonarray.length()];
                for (int i = 0; i < ossphjsonarray.length(); i++) {
                    JSONObject json = ossphjsonarray.getJSONObject(i);
                    ossphrAray[i] = json.getString("title");
                }
                osLsphrSpinner.setAdapter(getData(ossphrAray));
            } else {
                osLsphrSpinner.setVisibility(View.GONE);
            }        //oscyl json
            if (mainjson.has("oscyl")) {
                JSONObject oscyljson = mainjson.getJSONObject("oscyl");
                //Log.v("presjson", "11" + oscyljson);
                JSONArray oscyljsonarray = oscyljson.getJSONArray("vals");

                oscylAray = new String[oscyljsonarray.length()];
                for (int i = 0; i < oscyljsonarray.length(); i++) {
                    JSONObject json = oscyljsonarray.getJSONObject(i);
                    oscylAray[i] = json.getString("title");
                }

                cyliosLspinner.setAdapter(getData(oscylAray));
            } else {
                cyliosLspinner.setVisibility(View.GONE);
            }
            //osaxis json
            if (mainjson.has("osaxis")) {
                JSONObject osaxisjson = mainjson.getJSONObject("osaxis");
                //Log.v("presjson", "11" + oscyljson);
                JSONArray osaxisjsonarray = osaxisjson.getJSONArray("vals");

                osaxisAray = new String[osaxisjsonarray.length()];
                for (int i = 0; i < osaxisjsonarray.length(); i++) {
                    JSONObject json = osaxisjsonarray.getJSONObject(i);
                    osaxisAray[i] = json.getString("title");
                }
                axisosLspinner.setAdapter(getData(osaxisAray));
            } else {
                axisosLspinner.setVisibility(View.GONE);
            }
            //osadd json
            if (mainjson.has("osadd")) {
                JSONObject osaddjson = mainjson.getJSONObject("osadd");
                // Log.v("presjson", "11" + oscyljson);
                JSONArray osaddjsonarray = osaddjson.getJSONArray("vals");

                osaddAray = new String[osaddjsonarray.length()];
                for (int i = 0; i < osaddjsonarray.length(); i++) {
                    JSONObject json = osaddjsonarray.getJSONObject(i);
                    osaddAray[i] = json.getString("title");
                }
                addosLspinner.setAdapter(getData(osaddAray));
            } else {
                addosLspinner.setVisibility(View.GONE);
            }
            //pd single json
            if (mainjson.has("singlepd")) {
                JSONObject pdsinglejson = mainjson.getJSONObject("singlepd");
                //Log.v("pdsinglejson", "11" + pdsinglejson);
                JSONArray singlepdjsonarray = pdsinglejson.getJSONArray("vals");

                pdsinglAry = new String[singlepdjsonarray.length()];
                for (int i = 0; i < singlepdjsonarray.length(); i++) {
                    JSONObject json = singlepdjsonarray.getJSONObject(i);
                    pdsinglAry[i] = json.getString("title");
                }
                pdsinglespinner.setAdapter(getData(pdsinglAry));
            } else {
                pdsinglespinner.setVisibility(View.GONE);
            }
            //pd double left json
            if (mainjson.has("leftpd")) {
                JSONObject leftpdjson = mainjson.getJSONObject("leftpd");
                //Log.v("leftpdjson", "11" + leftpdjson);
                JSONArray leftpdjsonarray = leftpdjson.getJSONArray("vals");

                leftpdAry = new String[leftpdjsonarray.length()];
                for (int i = 0; i < leftpdjsonarray.length(); i++) {
                    JSONObject json = leftpdjsonarray.getJSONObject(i);
                    leftpdAry[i] = json.getString("title");
                }
                pddualleftspinr.setAdapter(getData(leftpdAry));
            } else {
                pddualleftspinr.setVisibility(View.GONE);
            }

            //pd double right json
            if (mainjson.has("rightpd")) {
                JSONObject rightpdjson = mainjson.getJSONObject("rightpd");
                //Log.v("pdsinglejson", "11" + rightpdjson);
                JSONArray rightpdjsonarray = rightpdjson.getJSONArray("vals");

                rightpdAry = new String[rightpdjsonarray.length()];
                for (int i = 0; i < rightpdjsonarray.length(); i++) {
                    JSONObject json = rightpdjsonarray.getJSONObject(i);
                    rightpdAry[i] = json.getString("title");
                }
                pddualritspinr.setAdapter(getData(rightpdAry));
            } else {
                pddualritspinr.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayData(JSONObject receiveJSon) {


        try {
            prescTotalJsonAry = receiveJSon.getJSONArray("data");
            for (int i = 0; i < prescTotalJsonAry.length(); i++) {
                JSONObject namejson = prescTotalJsonAry.getJSONObject(i);
                presNameList.add(namejson.getString("name"));
            }
            presListNameAry = new String[presNameList.size()];
            presListNameAry = presNameList.toArray(presListNameAry);
            selectSpinnerName.setAdapter(getData(presListNameAry));
            // String name = selectSpinnerName.getText().toString();
            //Log.v("name",""+name);
            //int index = presNameList.indexOf(name);
            //  Log.v("index",""+index);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendLensData(String json) {
        //startActivity(new Intent(mContext, PrescriptionNextActivity.class).));

        Intent i = new Intent(mContext, PrescriptionNextActivity.class);

        i.putExtra("lensjson", json);
        i.putExtra("mainjson", mainjson.toString());
        i.putExtra("pricejsonData", pricejson.toString());
        i.putExtra("prescsaveflag", pressaveflag);
        i.putExtra("userpresdata", userpresdatajson.toString());
        startActivityForResult(i,Code_PrescNext);

    }

    private final int  Code_PrescNext = 11;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == Code_PrescNext){
                if (resultCode == RESULT_CANCELED) {
                   // setResult(RESULT_CANCELED);
                   // finish();

                }else{
                    finish();
                }


            }

    }

    public static void updateProductOptionUi(String result, JSONObject receiveJSon) {
        Log.v("updateUi", "" + result);
        if (result.equals(AppConstants.SUCCESSFUL)) {
           // sInstance.homelt.setVisibility(View.VISIBLE);
            sInstance.totalmainjson = receiveJSon;
            sInstance.setPrescriptionOptionData(receiveJSon);


            if (sInstance.mPreferenceData.isLogincheck()) {

                sInstance.getStorePrescriptionData();
                sInstance.selectSpinnerName.setVisibility(View.VISIBLE);
            }else{
                sInstance.selectSpinnerName.setVisibility(View.GONE);
            }



        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }

    private String[] prescriptionTypeArray, prismodAry, prismodbaseDirectionAry, odsphrArray, odcylAray, odAxisAray, ossphrAray,
            pdsinglAry, oscylAray, osaxisAray, osaddAray, odaddAray, leftpdAry, rightpdAry, prismosAry, presListNameAry, prismosbaseDirectionAry;
    JSONObject mainjson;
    JSONArray prescTotalJsonAry;
    ArrayList<String> presNameList = new ArrayList<>();


   public static void updatePrescriptionListData(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.displayData(receiveJSon);
        } else {
            sInstance.selectSpinnerName.setVisibility(View.GONE);
           // Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }

    }

    JSONObject savepresjson = new JSONObject();

    public static void savePrescriptionData(String result) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.finish();
            // sInstance.displayData(receiveJSon);
        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }

    }

    public static void updateLensData(String result, JSONObject receiveJSon) {
        if (result.equals(AppConstants.SUCCESSFUL)) {
            sInstance.sendLensData(receiveJSon.toString());
        } else {
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }

    private void initialise() {
        edtPrescName = (EditText) findViewById(R.id.et_edtprescname);
        prismlt = (LinearLayout) findViewById(R.id.linear_prism);
        homelt = (RelativeLayout) findViewById(R.id.relative_homepres);
        relative_prestype = (RelativeLayout) findViewById(R.id.relative_prestype);
        prescriptiontypeSpinner = (BetterSpinner) findViewById(R.id.spinner_prescr);
        selectSpinnerName = (BetterSpinner) findViewById(R.id.spinner_selctpresc);


        odRsphrSpinner = (BetterSpinner) findViewById(R.id.spinner_sphererod);
        osLsphrSpinner = (BetterSpinner) findViewById(R.id.spinner_spherelos);
        cyliodRspinner = (BetterSpinner) findViewById(R.id.spinner_cylinderos);
        cyliosLspinner = (BetterSpinner) findViewById(R.id.spinner_cylinderod);
        axisodRspinner = (BetterSpinner) findViewById(R.id.spinner_axisod);
        axisosLspinner = (BetterSpinner) findViewById(R.id.spinner_axisos);
        addodRspinner = (BetterSpinner) findViewById(R.id.spinner_addod);
        addosLspinner = (BetterSpinner) findViewById(R.id.spinner_addos);
        pdsinglespinner = (BetterSpinner) findViewById(R.id.spinner_singlepd);
        pddualleftspinr = (BetterSpinner) findViewById(R.id.spinner_doblepdl);
        pddualritspinr = (BetterSpinner) findViewById(R.id.spinner_doblepdr);
        scrollView = (ScrollView) findViewById(R.id.pres_scroll);
        prsmodspnr = (BetterSpinner) findViewById(R.id.spinner_prismr);
        prismbaseoddirespnr = (BetterSpinner) findViewById(R.id.spinner_baserdir);
        prsmosspnr = (BetterSpinner) findViewById(R.id.spinner_prisosl);
        prismosbasedirspnr = (BetterSpinner) findViewById(R.id.spinner_prisosbaseldir);

        rgpd = (RadioGroup) findViewById(R.id.rg_pd);
        rgprism = (RadioGroup) findViewById(R.id.rg_prism);
        rbsinglepdbtn = (RadioButton) findViewById(R.id.rbtn_singlepd);
        rbdblpdbtn = (RadioButton) findViewById(R.id.rbtn_dblpd);

        rbyesbtn = (RadioButton) findViewById(R.id.rbtn_prismyes);
        rbnobtn = (RadioButton) findViewById(R.id.rbtn_prismno);
        nextBtn = (Button) findViewById(R.id.btn_nextpres);
        cancelBtn = (Button) findViewById(R.id.btn_cancelpres);
        nextBtn.setOnClickListener(submitListener);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                reset();
                setPrescriptionOptionData(totalmainjson);


            }
        });
        rbsinglepdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                pdselectflag = true;
            }
        });

        rbdblpdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pdselectflag = true;
            }
        });
        rgpd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rbtn_singlepd) {
                    pddualleftspinr.setVisibility(View.GONE);
                    pddualritspinr.setVisibility(View.GONE);
                    pdsinglespinner.setVisibility(View.VISIBLE);
                    pddualleftspinr.setText("Select Left PD");
                    pddualritspinr.setText("Select Right PD");
                } else if (checkedId == R.id.rbtn_dblpd) {
                    pdsinglespinner.setVisibility(View.GONE);
                    pdsinglespinner.setText("Select");
                    pddualleftspinr.setVisibility(View.VISIBLE);
                    pddualritspinr.setVisibility(View.VISIBLE);

                }
            }
        });

        rgprism.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbtn_prismyes) {
                    prismlt.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                } else if (checkedId == R.id.rbtn_prismno) {
                    prismlt.setVisibility(View.GONE);
                }
            }
        });


    }


    private void reset(){
        // private BetterSpinner prescriptiontypeSpinner, odRsphrSpinner, osLsphrSpinner, cyliodRspinner, cyliosLspinner, axisodRspinner;
        //private BetterSpinner axisosLspinner, addodRspinner, addosLspinner, pdsinglespinner, pddualleftspinr, pddualritspinr;
       // private BetterSpinner prsmodspnr, prsmosspnr, prismbaseoddirespnr, prismosbasedirspnr, selectSpinnerName;
        prescriptiontypeSpinner.setText("Prescription Type ");
        odRsphrSpinner.setText("Select"); osLsphrSpinner.setText("Select");cyliodRspinner.setText("Select");
        cyliosLspinner.setText("Select"); cyliosLspinner.setText("Select");axisodRspinner.setText("Select");
        axisosLspinner.setText("Select");addodRspinner.setText("Select");addosLspinner.setText("Select");
        pdsinglespinner.setText("Select");pddualleftspinr.setText("Select Left PD");pddualritspinr.setText("Select Right PD");
        prsmodspnr.setText("Select");prsmosspnr.setText("Select");prismbaseoddirespnr.setText("Select");
        prismosbasedirspnr.setText("Select");selectSpinnerName.setText("Select Prescription");
        pdselectflag = false;

    }



}
