package com.ts.mobilelab.goggles4u.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.data.NewArrivalData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mahesh on 17-03-2016.
 */


public class HorizontalListAdapter extends BaseAdapter {

    private ArrayList<NewArrivalData> arrivalDataList;
    private Context context;
    private DisplayImageOptions options;
    //private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public HorizontalListAdapter(Context mContext, ArrayList<NewArrivalData> newArrivalDataList) {
       this.arrivalDataList = newArrivalDataList;
        this.context = mContext;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .showImageOnFail(R.drawable.glass)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
					/*.imageScaleType(ImageScaleType.Fit)*/
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();


    }

    @Override
    public int getCount() {
        return arrivalDataList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.newarrival_listitem, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.productimg = (ImageView) convertView.findViewById(R.id.imv_newarrivalitem);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.tv_newarrival);
            viewHolder.productprice = (TextView) convertView.findViewById(R.id.tv_newarrivalprice);
           // viewHolder.spinner = (ProgressBar) convertView.findViewById(R.id.progressBar_banner);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.productName.setText(arrivalDataList.get(position).getProductname());
        viewHolder.productimg.setTag(arrivalDataList.get(position).getProductId());
        //DecimalFormat df = new DecimalFormat("#.00");
        //viewHolder.productprice.setText("$"+df.format(Double.parseDouble(arrivalDataList.get(position).getProductprice())));
        viewHolder.productprice.setText(arrivalDataList.get(position).getFormated_price());

        ImageLoader.getInstance().displayImage(arrivalDataList.get(position).getProductimgurl(), viewHolder.productimg, options);
        return convertView;
    }
    static class ViewHolder{
        ImageView productimg;
        TextView productName,productprice;
         ProgressBar spinner;
    }

    /*private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }*/
}
