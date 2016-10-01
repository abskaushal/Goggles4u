package com.ts.mobilelab.goggles4u.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.app.Activity;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.service.GcmIntentService;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by PC2 on 02-08-2016.
 */
public class GcmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("onReceive", "success");
        Toast.makeText(context, "gcm fired", Toast.LENGTH_LONG).show();
        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
