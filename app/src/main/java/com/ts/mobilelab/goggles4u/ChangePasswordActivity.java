package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    private Context mContext;
    private EditText etnewpswd,etnewcnfpswd;
    private Button saveBtn,cancelBtn;
    private PreferenceData mPreferenceData;
    String data;
    String from = "test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        data = getIntent().getStringExtra("jsondata");
        Log.v("data",""+data);
        from = getIntent().getStringExtra("from");
        Log.v("from",""+from);
       // etcrntpswd = (EditText) findViewById(R.id.et_nwcnfpswd);
        etnewpswd = (EditText) findViewById(R.id.et_nwpswd);
        etnewcnfpswd = (EditText) findViewById(R.id.et_nwcnfpswd);
        saveBtn = (Button) findViewById(R.id.btn_save);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(saveListener);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String newpswd = etnewpswd.getText().toString().trim();
            String newcnfmpswd= etnewcnfpswd.getText().toString().trim();

            boolean cancel = false;
            View focusView = null;

            if(TextUtils.isEmpty(newpswd)){
                etnewpswd.setError(getString(R.string.errorpassword));
                focusView = etnewpswd;
                cancel = true;
            }else  if(TextUtils.isEmpty(newcnfmpswd)){
                etnewcnfpswd.setError(getString(R.string.errorpassword));
                focusView = etnewcnfpswd;
                cancel = true;
            }else if(!newpswd.equals(newcnfmpswd)){
                etnewcnfpswd.setError(getString(R.string.pswdnotmatch));
                focusView = etnewcnfpswd;
                cancel = true;
            } else if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                JSONObject registerJson = new JSONObject();
  //{"Error":false,"result":{"token":"0d7a6dc1f3741a2a63544864944ad6bb","userid":"1"}}
                if(from.equals("forgot")){

                    try {
                        JSONObject ssjson = new JSONObject(data);
                        Log.v("ssjson",""+ssjson);
                        JSONObject datajson = ssjson.getJSONObject("result");
                        Log.v("datajson",""+datajson);
                        registerJson.put("customer_id", datajson.getString("userid"));
                        registerJson.put("token", datajson.getString("token"));
                        registerJson.put("newpassword", newpswd);
                        registerJson.put("confirmnewpass", newcnfmpswd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                    //newpassword,confirmnewpass,customer_id

                try {
                    //the key are user_id,newpassword,confirmation
                    //registerJson.put("user_id", "4");
                    registerJson.put("customer_id",mPreferenceData.getCustomerId() );
                    registerJson.put("newpassword", newpswd);
                    registerJson.put("confirmnewpass", newcnfmpswd);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }
                GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_CHANGEPSWD);
                gogglesAsynctask.execute(registerJson.toString());
            }
        }
    };

    public static void updateUi(String result) {

        if(result.equals(AppConstants.SUCCESSFUL)){
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
           // Toast.makeText(sInstance, "Password Change Successfully", Toast.LENGTH_LONG).show();
            sInstance.finish();
        }else{
            Toast.makeText(sInstance," "+result,Toast.LENGTH_LONG).show();
        }
    }

    private static  ChangePasswordActivity sInstance;
}
