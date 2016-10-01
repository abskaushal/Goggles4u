package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.logs.Logger;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {


    private Context mContext;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private Button registerBtn,guestBtn;
    private TextView skipView,forgtpwd;
    PreferenceData mPreferenceData;
    private static Login sInstance;
    String intentfrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.lg));
        mContext = this;
        sInstance = this;
        mPreferenceData = new PreferenceData();
        intentfrom = getIntent().getStringExtra("loginintent");
        mPasswordView = (EditText) findViewById(R.id.password);
        guestBtn = (Button) findViewById(R.id.btn_guest);
        registerBtn = (Button) findViewById(R.id.btn_register);
        //skipView = (TextView) findViewById(R.id.tv_skip);
        forgtpwd = (TextView) findViewById(R.id.tv_frgtpswd);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if("addtocart".equals(intentfrom)){
            guestBtn.setVisibility(View.VISIBLE);
        }
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_signin);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // startActivity(new Intent(sInstance.mContext,HomeActivity.class));
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mContext,RegistrationActivity.class));
            }
        });

        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,CheckoutActivity.class));
            }
        });


        forgtpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,ForGotPswdActivity.class));
            }
        });
    }

//TODo Login from cart add change in navigation drawer

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            focusView = mEmailView;
            cancel = true;
            return;
        }else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
            mEmailView.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.errorpassword));
            focusView = mPasswordView;
            cancel = true;
            mPasswordView.requestFocus();
            return;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
           // focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            JSONObject registerJson = new JSONObject();
            try {

                registerJson.put("email", email);
                registerJson.put("password", password);

            } catch (JSONException e) {
                e.printStackTrace();
                Logger.addRecordToLog("Login : " + e.getMessage());
            }

            GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_LOGIN);
            gogglesAsynctask.execute(registerJson.toString());
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }


    }



    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
    }

    public static void updateUi(String result) {

        if(result.equals(AppConstants.SUCCESSFUL)){
            sInstance.mPreferenceData.setLogincheck(true);
            Log.v("intentfrom", ""+sInstance.intentfrom);

            if("prescriptionnext".equals(sInstance.intentfrom)){
                sInstance.setResult(RESULT_OK);
                sInstance.finish();
            }else if("addtocart".equals(sInstance.intentfrom)){
                sInstance.setResult(RESULT_OK);
                sInstance.finish();
            }else{
                sInstance.startActivity(new Intent(sInstance.mContext, HomeActivity.class));
            }


        }else{
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }



}
