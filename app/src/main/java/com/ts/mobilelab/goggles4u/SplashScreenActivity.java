package com.ts.mobilelab.goggles4u;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Space;

import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.logs.Logger;
import com.ts.mobilelab.goggles4u.utils.PermissionUtils;

public class SplashScreenActivity extends Activity {
    private Handler mSplashHandler;
    private int mSplashTime = 5000;

    private PreferenceData mPreferenceData;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContext = this;
        mPreferenceData = new PreferenceData();
        mSplashHandler = new Handler();
        if (!PermissionUtils.getNeedPermissionList(SplashScreenActivity.this).isEmpty()) {
            if (Build.VERSION.SDK_INT >= 23)
                PermissionUtils.verifyPermissions(SplashScreenActivity.this);
        } else {
            mSplashHandler.postDelayed(mRunnable, mSplashTime);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtils.getNeedPermissionList(SplashScreenActivity.this).isEmpty()) {
            //mSplashHandler.removeCallbacks(mRunnable);
            // mSplashHandler.postDelayed(mRunnable, mSplashTime);
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            checkIntent();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mSplashHandler.removeCallbacks(mRunnable);
            checkIntent();
        }
        return super.onTouchEvent(event);
    }


    void checkIntent() {
       // Logger.addRecordToLog("Splash screen Stopped and Home Screen started!");
        startActivity(new Intent(mContext, HomeActivity.class));
        //compile 'org.apache.httpcomponents:httpmime:4.5.1'
        finish();
    }
}
