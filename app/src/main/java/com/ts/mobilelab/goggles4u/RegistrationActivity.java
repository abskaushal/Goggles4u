package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {


    private Context mContext;
    static RegistrationActivity sInstance;
    Button create_btn;
    private EditText mEmail, mPassword, mFname, mLname, mMiddleName, mCnfmPswd;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        sInstance = this;
        mEmail = (EditText) findViewById(R.id.edt_email);
        mFname = (EditText) findViewById(R.id.edt_fstname);
        mLname = (EditText) findViewById(R.id.edt_lstname);
        mPassword = (EditText) findViewById(R.id.edt_pswd);
        mCnfmPswd = (EditText) findViewById(R.id.edt_cnfpswd);
        create_btn = (Button) findViewById(R.id.btn_create);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.


        create_btn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {


        // Reset errors.
        mEmail.setError(null);
        mPassword.setError(null);
        mFname.setError(null);
        mLname.setError(null);
        mCnfmPswd.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String fname = mFname.getText().toString();
        String lname = mLname.getText().toString();
        String cnfrmpwd = mCnfmPswd.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.


        if (TextUtils.isEmpty(fname)) {
            mFname.setError(getString(R.string.errorfnamemsg));
            focusView = mFname;
            cancel = true;
            return;
        } else if (TextUtils.isEmpty(lname)) {
            mLname.setError(getString(R.string.errorlnamemsg));
            focusView = mLname;
            cancel = true;
            return;
            // Check for a valid email address.
        } else if(TextUtils.isEmpty(email) ){
            mEmail.setError(getString(R.string.erroremail));
            focusView = mEmail;
            cancel = true;
            return;
        }else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
            return;
        }

        else if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.errorpassword));
            focusView = mPassword;
            cancel = true;
            return;
        }else if (!isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
            return;
        }  else if (TextUtils.isEmpty(cnfrmpwd)) {
            mCnfmPswd.setError(getString(R.string.errorpassword));
            focusView = mPassword;
            cancel = true;
            return;
        }
        else if (!password.equals(cnfrmpwd)) {
            mCnfmPswd.setError(getString(R.string.pswdnotmatch));
            focusView = mCnfmPswd;
            cancel = true;
            return;
        } else if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            JSONObject registerJson = new JSONObject();
            try {
                registerJson.put("firstname", fname);
                registerJson.put("lastname", lname);
                registerJson.put("email", email);
                registerJson.put("password", password);
                registerJson.put("confirmpassword", cnfrmpwd);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_REGISTRATION);
            gogglesAsynctask.execute(registerJson.toString());


        }
    }    //mAuthTask = new UserLoginTask(email, password);

    //mAuthTask.execute((Void) null);


    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onStop() {
        super.onStop();


    }

    public static void updateUi(String result) {
        if(result.equals(AppConstants.SUCCESSFUL)){
            Toast.makeText(sInstance, "SuccessFully Registered,Thanks!", Toast.LENGTH_LONG).show();
            sInstance.startActivity(new Intent(sInstance,HomeActivity.class));
        }else{
            Toast.makeText(sInstance,""+result,Toast.LENGTH_LONG).show();
        }
        Log.v("result", "" + result);
    }
}
