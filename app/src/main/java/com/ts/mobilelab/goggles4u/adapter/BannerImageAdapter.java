package com.ts.mobilelab.goggles4u.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.data.AppConstants;

import java.util.ArrayList;

/**
 * Created by PC2 on 18-03-2016.
 */
public class BannerImageAdapter extends PagerAdapter {
   // private static final String[] IMAGE_URLS = AppConstants.IMAGES;

    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private ArrayList<String> imgList;
    private Context ctx;
    private String from;

    public BannerImageAdapter(Context context, ArrayList<String> bannerImgList, String fromhome) {
        inflater = LayoutInflater.from(context);
        //imgList = new ArrayList<>();
        imgList = bannerImgList;
        ctx = context;
        from = fromhome;
        //Log.v("in from",from+"BannerImageAdapter"+bannerImgList.size());
      /*  options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.lg)
                .showImageOnFail(R.drawable.logo_g)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
					*//*.imageScaleType(ImageScaleType.Fit)*//*
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();*/

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.lg)
                .showImageOnFail(R.drawable.logo_g)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
					/*.imageScaleType(ImageScaleType.Fit)*/
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

   /* @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
*/
    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout;
        if("fromhome".equals(from)) {
            imageLayout = inflater.inflate(R.layout.viewpager_item, view, false);
        }else{
            imageLayout = inflater.inflate(R.layout.viewpager_imgitem, view, false);
        }

       // View imageLayout = inflater.inflate(R.layout.viewpager_item, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imv_banner);
        final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.progressBar_banner);

        ImageLoader.getInstance().displayImage(imgList.get(position), imageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                spinner.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                Log.v("failReason", "in frag" + failReason.getType());

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
                //Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                spinner.setVisibility(View.GONE);
                Log.v("imageView",""+imageView);
                imageView.setImageDrawable(ctx.getResources().getDrawable(R.drawable.logo_g));
                imageView.setImageResource(R.drawable.male_big);
               // LayoutInflater inflater = ctx.getLay


            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                spinner.setVisibility(View.GONE);

                view.setVisibility(View.VISIBLE);

            }


        });

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
        //super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}

