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
import com.ts.mobilelab.goggles4u.data.CollectionImageData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by PC2 on 19-03-2016.
 */


public class CollectionImageAdapter extends BaseAdapter {

    private ArrayList<CollectionImageData> collectionImgList;
    private Context context;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();


    public CollectionImageAdapter(Context mContext, ArrayList<CollectionImageData> collectionImgList) {

        this.collectionImgList = collectionImgList;
        this.context = mContext;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .showImageOnFail(R.drawable.ic_close_24dp)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
					/*.imageScaleType(ImageScaleType.Fit)*/
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

       /* options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_menu_gallery)
                .showImageForEmptyUri(R.drawable.ic_exit_to_app_24dp)
                .showImageOnFail(R.drawable.ic_menu_gallery)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                //.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();*/
    }

    @Override
    public int getCount() {
        return collectionImgList.size();
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
            convertView = inflater.inflate(R.layout.collectionimg_item,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.productimg = (ImageView) convertView.findViewById(R.id.imv_colection);
            viewHolder.headerName = (TextView) convertView.findViewById(R.id.tv_header);
            viewHolder.imagedetails = (TextView) convertView.findViewById(R.id.tv_details);
        // viewHolder.spinner = (ProgressBar) convertView.findViewById(R.id.progressBar_img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.headerName.setText(collectionImgList.get(position).getBannerheaderText());
        viewHolder.imagedetails.setText(collectionImgList.get(position).getBannerDetails());
       ImageLoader.getInstance().displayImage(collectionImgList.get(position).getBannerImageUrl(), viewHolder.productimg, options, animateFirstListener);

        /*ImageLoader.getInstance().displayImage(collectionImgList.get(position).getBannerImageUrl(), viewHolder.productimg, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                viewHolder.spinner.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                Log.v("failReason", "in frag" + failReason);
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "Input/Output error";
                        break;
                    case DECODING_ERROR:
                        message = "Image can't be decoded";
                        break;
                    case NETWORK_DENIED:
                        message = "Downloads are denied";
                        break;
                    case OUT_OF_MEMORY:
                        message = "Out Of Memory error";
                        break;
                    case UNKNOWN:
                        message = "Unknown error";
                        break;
                }
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                viewHolder.spinner.setVisibility(View.GONE);

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                viewHolder.spinner.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }


        });
*/
        return convertView;
    }
    static class ViewHolder{
        ImageView productimg;
        TextView headerName,imagedetails;
        ProgressBar spinner;
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
