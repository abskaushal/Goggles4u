package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONException;
import org.json.JSONObject;

public class PrescriptionView extends AppCompatActivity {

    private TextView presName,presType,sphrod,sphros,cyliod,cylios,axisod,axisos,addod,addos,prismod,prismos,
            baseod,baseos,pdod,pdos,singlepd,singlePDView,dualPDView;
    private Context mContext;
    private static PrescriptionView sInstance;
    RelativeLayout homelt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sInstance = this;
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        mContext = this;
        presName = (TextView) findViewById(R.id.tv_presnameview);
        presType = (TextView) findViewById(R.id.tv_presc_typeview);
        sphrod = (TextView) findViewById(R.id.tv_sphrod);
        sphros = (TextView) findViewById(R.id.tv_sphros);
        cyliod = (TextView) findViewById(R.id.tv_cyliod);
        cylios = (TextView) findViewById(R.id.tv_cylios);
        pdod = (TextView) findViewById(R.id.tv_pdod);
        pdos = (TextView) findViewById(R.id.tv_pdos);
        singlepd = (TextView) findViewById(R.id.tv_singlepd);
        singlePDView = (TextView) findViewById(R.id.tv_singlepdview);
        dualPDView = (TextView) findViewById(R.id.tv_dualpdview);
        axisod = (TextView) findViewById(R.id.tv_axisod);
        axisos = (TextView) findViewById(R.id.tv_axisos);
        homelt = (RelativeLayout) findViewById(R.id.relative_homelt);
        addod = (TextView) findViewById(R.id.tv_addod);
        addos = (TextView) findViewById(R.id.tv_addos);

        prismod = (TextView) findViewById(R.id.tv_prismod);
        prismos = (TextView) findViewById(R.id.tv_prismos);

        baseod = (TextView) findViewById(R.id.tv_basedirod);
        baseos = (TextView) findViewById(R.id.tv_basediros);

        String presid = getIntent().getExtras().getString("id");
       // Log.v("presid", "" + presid);

        JSONObject pjson = new JSONObject();
        try {
            pjson.put("prescription_id",presid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GogglesAsynctask gasyncTask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_VIEWPRESCRIPTION);
        gasyncTask.execute(pjson.toString());




    }

    public static void updateUi(String result, JSONObject receiveJSon) {
        //Log.v("receiveJSon",""+receiveJSon);
        if(result.equals(AppConstants.SUCCESSFUL)){
            sInstance.homelt.setVisibility(View.VISIBLE);
            sInstance.showdata(receiveJSon);


        }
    }

    private  void showdata(JSONObject receiveJSon) {

        try {
            //{"prescription_name":"Yaya","prescription_data":{"pd":{"option_value":"2426249","value":"SinglePD","label":"PD","custom_view":false,"option_id":"987940","option_type":"radio","print_value":"SinglePD"},"ossph":{"option_value":"-0.25","value":"-0.25","label":"OSSPH","custom_view":false,"option_id":"987936","option_type":"field","print_value":"-0.25"},"prescription_type":{"option_value":"2426254","value":"Progressive Lenses","label":"Prescription Type","custom_view":false,"option_id":"progressive","option_type":"drop_down","print_value":"Progressive Lenses"},"singlepd":{"option_value":"60","value":"60","label":"SinglePD","custom_view":false,"option_id":"987941","option_type":"field","print_value":"60"},"osadd":{"option_value":"+3.00","value":"+3.00","label":"OSADD","custom_view":false,"option_id":"987939","option_type":"field","print_value":"+3.00"},"anti_reflective_coating":{"option_value":"2426223","valu
            JSONObject datatjson = receiveJSon.getJSONObject("data");
            //Log.v("datatjson",""+datatjson);
           // Log.v("data",""+datatjson.getString("prescription_name"));
            sInstance.presName.setText(datatjson.getString("prescription_name"));
            JSONObject innerjson = datatjson.getJSONObject("prescription_data");
            // Log.v("types",""+types);
            // JSONObject innerjson = new JSONObject(types);
           // Log.v("innerjson", "" + innerjson);
            if(innerjson.has("prescription_type")){
                JSONObject prescrdata = innerjson.getJSONObject("prescription_type");
                //Log.v("prescrdata", "" + prescrdata.getString("value"));
                sInstance.presType.setText(prescrdata.getString("value"));
            }

            if(innerjson.has("odsph")) {
                JSONObject odsphrjson = innerjson.getJSONObject("odsph");
                sInstance.sphrod.setText(odsphrjson.getString("value"));
            }
            if(innerjson.has("ossph")) {
                JSONObject ossphrjson = innerjson.getJSONObject("ossph");
                sInstance.sphros.setText(ossphrjson.getString("value"));
            }
            if(innerjson.has("odcyl")) {
                JSONObject odcyljson = innerjson.getJSONObject("odcyl");
                sInstance.cyliod.setText(odcyljson.getString("value"));
            }
            String pdflag = null;
            if(innerjson.has("oscyl")){
                JSONObject oscyljson = innerjson.getJSONObject("oscyl");

                sInstance.cylios.setText(oscyljson.getString("value"));


            }

            if(innerjson.has("pd")){
                JSONObject pdjson = innerjson.getJSONObject("pd");
                pdflag = pdjson.getString("value");

           // Log.v("pdflag",""+pdflag);
            if(pdflag.equals("dual")){
                dualPDView.setVisibility(View.VISIBLE);
                singlePDView.setVisibility(View.GONE);
                sInstance.pdod.setVisibility(View.VISIBLE);
                sInstance.pdos.setVisibility(View.VISIBLE);
                sInstance.singlepd.setVisibility(View.GONE);
            }else{
                singlePDView.setVisibility(View.VISIBLE);
                dualPDView.setVisibility(View.GONE);
                sInstance.pdod.setVisibility(View.GONE);
                sInstance.pdos.setVisibility(View.GONE);
                sInstance.singlepd.setVisibility(View.VISIBLE);
            }
            }
            if(innerjson.has("singlepd")) {

               // sInstance.singlepd.setVisibility(View.VISIBLE);
                JSONObject odpdjson = innerjson.getJSONObject("singlepd");
                sInstance.singlepd.setText("SinglePD - "+odpdjson.getString("value"));
            }
            if(innerjson.has("leftpd")) {

                JSONObject odpdjson = innerjson.getJSONObject("leftpd");
                sInstance.pdod.setText("Left PD - "+odpdjson.getString("value"));
            }
            if(innerjson.has("rightpd")) {
                JSONObject ospdjson = innerjson.getJSONObject("rightpd");
                sInstance.pdos.setText("Right PD - "+ospdjson.getString("value"));
            }
            if(innerjson.has("odaxis")) {

                JSONObject odpdjson = innerjson.getJSONObject("odaxis");
                sInstance.axisod.setText(odpdjson.getString("value"));
            }
            if(innerjson.has("osdaxis")) {
                JSONObject ospdjson = innerjson.getJSONObject("osaxis");
                sInstance.axisos.setText(ospdjson.getString("value"));
            }
            if(innerjson.has("osadd")) {

                JSONObject odpdjson = innerjson.getJSONObject("osadd");
                sInstance.addos.setText(odpdjson.getString("value"));
            }
            if(innerjson.has("odadd")) {
                JSONObject ospdjson = innerjson.getJSONObject("odadd");
                sInstance.addod.setText(ospdjson.getString("value"));
            }

            if(innerjson.has("prismod")) {

                JSONObject odpdjson = innerjson.getJSONObject("prismod");
                sInstance.prismod.setText(odpdjson.getString("value"));
            }
            if(innerjson.has("prismos")) {
                JSONObject ospdjson = innerjson.getJSONObject("prismos");
                sInstance.prismos.setText(ospdjson.getString("value"));
            }
            if(innerjson.has("prismodbasedirection")) {

                JSONObject odpdjson = innerjson.getJSONObject("prismodbasedirection");
                sInstance.baseod.setText(odpdjson.getString("value"));
            }
            if(innerjson.has("prismosbasedirection")) {
                JSONObject ospdjson = innerjson.getJSONObject("prismosbasedirection");
                sInstance.baseos.setText(ospdjson.getString("value"));
            }





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
