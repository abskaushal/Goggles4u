package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.core.GogglesManager;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.data.PrescriptionData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.ts.mobilelab.goggles4u.net.GoogleAsyncTaskGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PrescriptionsActivity extends AppCompatActivity {

    private Context mContext;
    private ListView prescriptionList;
    private PrescriptionAdapter mPrescriptionAdapter;
    ArrayList<PrescriptionData> mDataList;
    private PreferenceData mPreferenceData;
    LinearLayout headerlt;
    private Button addPresBtn;
    private static PrescriptionsActivity sInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
       //getSupportActionBar().setTitle("Home");
        mPreferenceData = new PreferenceData();
        mContext = this;
        sInstance = this;
        View empty = findViewById(R.id.empty);
        prescriptionList = (ListView) findViewById(R.id.list_prescrpt);
        prescriptionList.setEmptyView(empty);
        headerlt = (LinearLayout) findViewById(R.id.headerlt);
        addPresBtn = (Button) findViewById(R.id.btn_addpresc);
        mDataList = new ArrayList<>();
        init();
        addPresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mContext,PrescriptionAddActivity.class).putExtra("type","fromadd"));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {

        JSONObject presjson = new JSONObject();
        try {
            presjson.put("customer_id",mPreferenceData.getCustomerId());
            //presjson.put("customer_id",6);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_PRESCRIPTIONDATA);
        gogglesAsynctask.execute(presjson.toString());

    }

    public static void updateUi(String result) {

        Log.v("result", "" + result);
        if(result.equals(AppConstants.SUCCESSFUL)){
            sInstance.showListdata();


        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private  void showListdata() {
        Log.v("mDataList size", "22" + GogglesManager.getInstance().getPrescriptionDataArrayList().size());
        mDataList = GogglesManager.getInstance().getPrescriptionDataArrayList();
        Log.v("mDataList",""+mDataList.size());
        if(mDataList.size() != 0){
            headerlt.setVisibility(View.VISIBLE);
        }
        Log.v("prescriptionList", "" + prescriptionList);
        mPrescriptionAdapter = new PrescriptionAdapter(mContext, mDataList);
        prescriptionList.setAdapter(mPrescriptionAdapter);
    }




    public static void updateUidelete(String result, JSONObject sendJsonobj) {

        if(result.equals(AppConstants.SUCCESSFUL)){
            //sInstance.prescriptionList.removeView(0);
            try {
                String id = sendJsonobj.getString("prescription_id");
                Log.v("uodste uiid",""+id);
                sInstance.mDataList = GogglesManager.getInstance().getPrescriptionDataArrayList();
                for (int i = 0; i < sInstance.mDataList.size()  ; i++) {
                    if(id.equals(sInstance.mDataList.get(i).getPrescription_id())) {
                        sInstance.mDataList.remove(i);
                        break;
                    }

                }

                sInstance.mPrescriptionAdapter.refresh(sInstance.mDataList);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public static void updateUiEdit(String result, JSONObject sendJsonobj) {
        Log.v("updateUiEdit",result+""+sendJsonobj);
        if(result.equals(AppConstants.SUCCESSFUL)){
            JSONArray sendjsonary = null;
            try {
                sendjsonary = sendJsonobj.getJSONArray("data");
                Log.v("updateUiEdit",result+""+sendJsonobj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(sInstance,PrescriptionAddActivity.class);
            i.putExtra("type","fromedit");
            i.putExtra("jsonData",sendjsonary.toString());
            sInstance.startActivity(i);
        }else{
            Toast.makeText(sInstance,""+result,Toast.LENGTH_LONG).show();
        }
    }

    public class PrescriptionAdapter extends BaseAdapter {

        ArrayList<PrescriptionData> prescriptionListdata;
        Context context;

        public PrescriptionAdapter(Context mContext, ArrayList<PrescriptionData> mDataList) {

            context = mContext;
            prescriptionListdata = mDataList;
        }

        public void refresh( ArrayList<PrescriptionData> mDataList){
            prescriptionListdata = mDataList;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return prescriptionListdata.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView =  inflater.inflate(R.layout.prescription_row,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.presName = (TextView) convertView.findViewById(R.id.tv_presname);
                viewHolder.prescDate = (TextView) convertView.findViewById(R.id.tv_presdate);
                viewHolder.presCreater = (TextView) convertView.findViewById(R.id.tv_precreater);
                viewHolder.edit = (TextView) convertView.findViewById(R.id.tv_edit);
                viewHolder.viewr = (TextView) convertView.findViewById(R.id.tv_view);
                viewHolder.delete = (TextView) convertView.findViewById(R.id.tv_dlt);


                convertView.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.presName.setText(mDataList.get(position).getPrescriptiion_name());
            viewHolder.presCreater.setText(mDataList.get(position).getModifiedby());
            viewHolder.prescDate.setText(mDataList.get(position).getCreatedate());

            String id = mDataList.get(position).getPrescription_id();
            //Log.v("idsss", "" + id);
            //Log.v("position", "" + position);


            viewHolder.viewr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = mDataList.get(position).getPrescription_id();
                   // Log.v("id",""+id);

                    startActivity(new Intent(context, PrescriptionView.class).putExtra("id",id));

                }
            });

            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //{"customer_id":6,"prescription_id":606611}
                    GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_EDITPRESCRIPTION);
                   JSONObject editjson = new JSONObject();
                    try {
                        editjson.put("customer_id",mPreferenceData.getCustomerId());
                        editjson.put("prescription_id", mDataList.get(position).getPrescription_id());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    gogglesAsynctask.execute(editjson.toString());

                   //

                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Googgles4u");
                    alertDialog.setMessage("Do you want to delete the prescription!");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Log.v("yesss","");
                            String id = mDataList.get(position).getPrescription_id();
                            Log.v("id", "" + id);
                            JSONObject jobj = new JSONObject();
                            try {
                                jobj.put("prescription_id", id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            GogglesAsynctask gasynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_DELETEPRESCRIPTION);
                            gasynctask.execute(jobj.toString());
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.create().show();





                }
            });


            return convertView;
        }

        class ViewHolder{

            TextView presName,prescDate,presCreater,edit,viewr,delete;

        }
    }
}
