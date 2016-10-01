package com.ts.mobilelab.goggles4u.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.i.Result;

/**
 * Created by Mahesh on 08-08-2016.
 */
public  class CustomDialog {


    private Result resultListener ;
    public void  showDltDialog(String title, String message, Context context, Result result) {
        resultListener = result;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setIcon(R.drawable.logo_g);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resultListener.onResult(true);
                //deleteItem();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                resultListener.onResult(false);
            }
        });
        alertDialog.create().show();

    }

}
