package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.ts.mobilelab.goggles4u.net.utils.HttpConnectionHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class ForGotPswdActivity extends AppCompatActivity {

    private EditText mEmailid;
    private Button submit;
    private String email;
    private static Context mContext;

   private static ForGotPswdActivity sInstance;
    public static String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_got_pswd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        mContext = this;
        sInstance = this;
        mEmailid = (EditText) findViewById(R.id.et_fpwdemail);
        submit = (Button) findViewById(R.id.btn_fpwdsubmit);
        submit.setOnClickListener(submitListener);
    }

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            email = mEmailid.getText().toString();
            if(TextUtils.isEmpty(email)){
                mEmailid.setError(getResources().getString(
                        R.string.erroremail));
                mEmailid.requestFocus();
            }
           else if ( !RegistrationActivity.isEmailValid(email)) {

                mEmailid.setError(getResources().getString(
                        R.string.error_invalid_email));
                mEmailid.requestFocus();
            }
            else {
                    JSONObject fpwdjsonobj = new JSONObject();
                    try {
                        fpwdjsonobj.put("email", email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Log.v("forgetpwd json", "" + fpwdjsonobj);
                    GogglesAsynctask mRegiAsyncTask = new GogglesAsynctask(
                            mContext, AppConstants.CODE_FOR_FORGTPSWD);
                    mRegiAsyncTask.execute(fpwdjsonobj.toString());
                }
            }

    };

    public static void updateUi(String result, JSONObject receiveJSon) {
        if(result.equals(AppConstants.SUCCESSFUL)){
            PrescriptionAddActivity.show_dialog(sInstance,"Check your Registered Email.");
           // sInstance.startActivityForResult(new Intent(sInstance, ChangePasswordActivity.class).putExtra("from", "forgot").putExtra("jsondata",receiveJSon.toString()),11);
            sInstance.finish();
            //{"Error":false,"result":{"token":"0844533172f7c2ef600049b15bfe4f8e","userid":"1"}}

        }else{
            Toast.makeText(sInstance,""+result,Toast.LENGTH_LONG).show();
            //PrescriptionAddActivity.show_dialog(sInstance, ""+result);
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
