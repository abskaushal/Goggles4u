package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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

public class EditInformation extends AppCompatActivity {

    private EditText edtfName,edtlName,edtemail;
    private Button saveBtn,cancelBtn;
    private PreferenceData mPreferenceData;
    private Context mContext;
    private static  EditInformation sInstance;

    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sInstance = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit info");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        edtfName = (EditText) findViewById(R.id.edt_fname);
        edtlName = (EditText) findViewById(R.id.edt_lname);
        edtemail = (EditText) findViewById(R.id.edt_email);
        mPreferenceData = new PreferenceData();
        mContext = this;
        edtfName.setText(mPreferenceData.getCustomerFName());
        edtlName.setText(mPreferenceData.getCustomerLName());
        edtemail.setText(mPreferenceData.getCustomerMailId());

        saveBtn = (Button) findViewById(R.id.btn_save);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveBtn.setOnClickListener(submitListener);
    }

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Reset errors.

            edtfName.setError(null);
            edtlName.setError(null);
            edtemail.setError(null);
            // Store values at the time of the login attempt.
            String email = edtemail.getText().toString().trim();
            String fname = edtfName.getText().toString().trim();
            String lname= edtlName.getText().toString().trim();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(fname)) {
                edtfName.setError(getString(R.string.error_invalid_fname));
                focusView = edtfName;
                cancel = true;
            }
            if (TextUtils.isEmpty(lname)) {
                edtlName.setError(getString(R.string.error_invalid_lname));
                focusView = edtfName;
                cancel = true;
            }

            // Check for a valid email address.
            if (TextUtils.isEmpty(email)) {
                edtemail.setError(getString(R.string.error_field_required));
                focusView = edtemail;
                cancel = true;
            } else if (!isEmailValid(email)) {
                edtemail.setError(getString(R.string.error_invalid_email));
                focusView = edtemail;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                JSONObject registerJson = new JSONObject();
                try {
                    registerJson.put("customer_id",mPreferenceData.getCustomerId());
                    registerJson.put("email", email);
                    registerJson.put("firstname", fname);
                    registerJson.put("lastname", lname);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_EDITINFO);
                gogglesAsynctask.execute(registerJson.toString());
            }
        }
    };
    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void updateUi(String result, JSONObject sendJsonobj) {

        if(result.equals(AppConstants.SUCCESSFUL)){
            Log.v("editinfo", "sendJsonobj" + sendJsonobj);

            PreferenceData mPreferenceData = new PreferenceData();
            try {
                mPreferenceData.setCustomerLName(sendJsonobj.getString("lastname"));
                mPreferenceData.setCustomerFName(sendJsonobj.getString("firstname"));
                mPreferenceData.setCustomerMailId(sendJsonobj.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sInstance.finish();

        }else{
           Log.v("in editinfo ui",""+result);
            Snackbar snackbar = Snackbar
                    .make(sInstance.coordinatorLayout, ""+result, Snackbar.LENGTH_LONG);

            snackbar.show();

        }

    }
}
