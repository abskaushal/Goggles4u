package com.ts.mobilelab.goggles4u.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.data.CollectionImageData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Abhishek on 16-Oct-16.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder>{


    private ArrayList<CollectionImageData> mCollectionImgList;
    private Context mContext;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public CollectionAdapter(Context context, ArrayList<CollectionImageData> collectionImgList){
        if(collectionImgList == null || context == null){
            throw new IllegalArgumentException("Null arguments passed");
        }
        mCollectionImgList = collectionImgList;
        mContext = context;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .showImageOnFail(R.drawable.ic_close_24dp)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v("Adapter", "View");
        View view = LayoutInflater.from(mContext).inflate(R.layout.collectionimg_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.v("Adapter", "BindView");
        holder.headerName.setText(mCollectionImgList.get(position).getBannerheaderText());
        holder.imagedetails.setText(mCollectionImgList.get(position).getBannerDetails());
        ImageLoader.getInstance().displayImage(mCollectionImgList.get(position).getBannerImageUrl(), holder.productimg, options, animateFirstListener);

    }

    @Override
    public int getItemCount() {
        return mCollectionImgList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView productimg;
        TextView headerName,imagedetails;
        ProgressBar spinner;

        public ViewHolder(View itemView) {
            super(itemView);
            productimg = (ImageView) itemView.findViewById(R.id.imv_colection);
            headerName = (TextView) itemView.findViewById(R.id.tv_header);
            imagedetails = (TextView) itemView.findViewById(R.id.tv_details);
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

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
    }
}
