package com.ts.mobilelab.goggles4u;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class HelpRowActivity extends AppCompatActivity {

    private TextView contactUS;
    private Context mContext;
    private static HelpRowActivity sInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_row);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        String tag = getIntent().getStringExtra("identifier");

        mContext = this;
        sInstance = this;
        contactUS = (TextView) findViewById(R.id.tv_contactus);
        //contactUS.setText(Html.fromHtml("Mahesh"));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_helprow);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
                startActivity(new Intent(mContext,ShoppingCartActivity.class));
            }
        });
        init(tag);
    }
    private void init(String tag) {
        JSONObject json = new JSONObject();
        try {
            //{"user_id":"10","addresstype":"checkout"}
            json.put("page",tag);
            //json.put("page","helpcenter");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        GogglesAsynctask gogglesAsynctask = new GogglesAsynctask(mContext, AppConstants.CODE_FOR_HELPROW);
        gogglesAsynctask.execute(json.toString());
    }

    public static void updateData(String result, JSONObject receiveJSon) {
        if(result.equals(AppConstants.SUCCESSFUL)){
            try {
                String cntn = receiveJSon.getString("content");
                Log.v("cntn", "" + cntn);
                sInstance.setContent(cntn);
                sInstance.getSupportActionBar().setTitle(receiveJSon.getString("title"));

                //htmlTextView.setText(Html.fromHtml(htmlText, new ImageGetter(), null));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(sInstance, "" + result, Toast.LENGTH_LONG).show();
        }
    }

    private  void setContent(String cntn) {
        Spanned spanned = Html.fromHtml(cntn, new ImageGetter(), null);
        contactUS.setText(spanned);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(android.R.id.home == item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;
            Log.v("source", "imggetter" + source);

           /* if (source.contains("how-to-order.jpg")) {
                id = R.drawable.order_guide;
            }
            else if (source.equals("overflow.jpg")) {
                id = R.drawable.ic_favorite_outline_24dp;
            }
            else {
                return null;
            }*/
            LevelListDrawable d = new LevelListDrawable();
            Drawable dd = getResources().getDrawable(R.drawable.logo_g);
            d.addLevel(0, 0, dd);
            d.setBounds(0, 0, dd.getIntrinsicWidth(), dd.getIntrinsicHeight());
            new LoadImage().execute(source, d);
            return d;
        }
    };

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d("", "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d("", "onPostExecute drawable " + mDrawable);
            Log.d("", "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = contactUS.getText();
                contactUS.setText(t);
            }
        }
    }
}
