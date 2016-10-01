package com.ts.mobilelab.goggles4u.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SERVER on 31-01-2016.
 */
public class PermissionUtils {
    private static final int REQUEST = 1;

   public static void verifyPermissions(Activity activity) {
        List<String> temp = getNeedPermissionList(activity);
        if (!temp.isEmpty()) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    temp.toArray(new String[temp.size()]),
                    REQUEST
            );
            Log.v("Permission Utils", "requestPermissions");
        }
    }

    public static List<String> getNeedPermissionList(Activity activity) {
        int permissionStorage = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisionPhone = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        int permisionCamera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        List<String> temp = new ArrayList<String>();
        
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            temp.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            temp.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
        if (permisionPhone != PackageManager.PERMISSION_GRANTED) {
            temp.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permisionCamera != PackageManager.PERMISSION_GRANTED) {
            temp.add(Manifest.permission.CAMERA);
        }

        Log.v("Permission Utils", "temp :" + temp);
        return temp;

    }
	
	//Build.VERSION.SDK_INT >= 23
        


}
