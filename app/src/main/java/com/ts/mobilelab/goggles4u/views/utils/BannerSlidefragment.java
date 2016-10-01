package com.ts.mobilelab.goggles4u.views.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ts.mobilelab.goggles4u.HomeActivity;
import com.ts.mobilelab.goggles4u.R;
import com.ts.mobilelab.goggles4u.data.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link BannerSlidefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BannerSlidefragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String[] IMAGE_URLS = AppConstants.IMAGES;
    public final static String[] imageResIds = new String[] {"http://www.centurynovelty.com/catImages/039-059_small.jpg",
            "http://rlv.zcache.com/sdfsd_iphone_4_case_mate_case-r6a5c5f1401dc45e6b9c101e6670de8c9_a460e_8byvr_512.jpg", };

    public static final String ARG_PAGE = "page";
    private int mPageNumber;
    private static Context sContext;
    //private static TypedArray imgArray;
    ViewGroup view =null;
    //int[] bannerimage;
    ImageView imView;
    private DisplayImageOptions options;
    public BannerSlidefragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BannerSlidefragment newInstance(Context ctx, int position) {
        sContext = ctx;
        BannerSlidefragment fragment = new BannerSlidefragment();

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
       // args.putString(ARG_PARAM2, param2);
       // imgArray = sContext.getResources().obtainTypedArray(R.array.bannerimage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPageNumber = getArguments().getInt(ARG_PAGE);

        }
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_menu_gallery)
                .showImageOnFail(R.drawable.ic_close_24dp)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.gc();
        ViewGroup cview =null;
        // Inflate the layout for this fragment
        cview = (ViewGroup) inflater.inflate(R.layout.viewpager_item,
                container, false);
        Log.v("value in ", "from aboutus" );
        //imView = (ImageView) view.findViewById(R.id.imv_banner);
        assert cview != null;
        ImageView imageView = (ImageView) cview.findViewById(R.id.imv_banner);
        final ProgressBar spinner = (ProgressBar) cview.findViewById(R.id.progressBar_banner);

        ImageLoader.getInstance().displayImage(IMAGE_URLS[mPageNumber], imageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                spinner.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
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

                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                spinner.setVisibility(View.GONE);
            }
        });
       // cview.addView(view, 0);
        // set the image
        //imView.setImageResource(bannerimage.);
       // imView.setImageDrawable(imgArray.getDrawable(mPageNumber));

        //ImageLoader.getInstance().displayImage(imageResIds[0],);
        return cview;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
           /* if (mListener != null) {
                 mListener.onFragmentInteraction(uri);
        }*/
    }





}
