package com.ts.mobilelab.goggles4u.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.FavoriteData;
import com.ts.mobilelab.goggles4u.data.Message;
import com.ts.mobilelab.goggles4u.data.PreferenceData;
import com.ts.mobilelab.goggles4u.data.ProductData;
import com.ts.mobilelab.goggles4u.i.Result;
import com.ts.mobilelab.goggles4u.net.GogglesAsynctask;
import com.ts.mobilelab.goggles4u.utils.CustomDialog;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Mahesh on 06-08-2016.
 */
public class FavroiteListAdapter extends BaseAdapter {
    private ArrayList<FavoriteData> favList;
    Context context;
    PreferenceData mPreferenceData;
    private DisplayImageOptions options;

    public FavroiteListAdapter(Context mContext, ArrayList<FavoriteData> favlist) {
        this.context = mContext;
        this.favList = favlist;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.logo_g)
                .showImageOnFail(R.drawable.glass)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                    /*.imageScaleType(ImageScaleType.Fit)*/
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        mPreferenceData = new PreferenceData();
    }

    public void refresh(ArrayList<FavoriteData> dataList){
        favList = dataList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        
        return favList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fav_list_row,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.frameimg = (ImageView) view.findViewById(R.id.imv_framefav);
            viewHolder.deleteImg = (ImageView) view.findViewById(R.id.imv_dltfav);
            viewHolder.nameView = (TextView) view.findViewById(R.id.tv_namefabv);
            viewHolder.priceView = (TextView) view.findViewById(R.id.tv_pricefav);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();

        }
        DecimalFormat dcf = new DecimalFormat("#.00");
        viewHolder.deleteImg.setTag(position);
        FavoriteData favData = favList.get(position);
        viewHolder.nameView.setText(favData.getName());
        viewHolder.priceView.setText("$"+dcf.format(Double.parseDouble(favData.getPrice())));
        //view.setTag(favData.getId());
        ImageLoader.getInstance().displayImage(favData.getImgurl(), viewHolder.frameimg, options);
        viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // boolean isDeleted = new CustomDialog().showDltDialog("Favorite List","Do you want to remove the item!",context);
                new CustomDialog().showDltDialog("Favorite List", "Do you want to remove the item!", context, new Result() {
                    @Override
                    public void onResult(boolean isSuccess) {

                        favList.remove(v.getTag());
                        notifyDataSetChanged();
                        Log.v("select ID", "" + favList.get(position).getId());
                        JSONObject dltjson = new JSONObject();
                        try {
                            dltjson.put("product_id", favList.get(position).getId());
                            if (mPreferenceData.isLogincheck()) {
                                dltjson.put("customer_id", mPreferenceData.getCustomerId());
                                dltjson.put("session_id", "");
                            } else {
                                dltjson.put("customer_id", 0);
                                dltjson.put("session_id", mPreferenceData.getDEVICE_ID());

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        deleteItem(dltjson);
                    }

                    @Override
                    public void onResult(Message message, boolean isSuccess) {

                    }


                });
            }
        });


        return view;

    }



    private  static class ViewHolder {

        ImageView frameimg,deleteImg;
        TextView nameView,priceView;

    }

    private void deleteItem(JSONObject dltjson) {
       // favList.remove(viewHolder.deleteImg.)
        GogglesAsynctask asynctask = new GogglesAsynctask(context, AppConstants.CODE_FOR_DELETEFAVITEM);
        asynctask.execute(dltjson.toString());
    }

}
