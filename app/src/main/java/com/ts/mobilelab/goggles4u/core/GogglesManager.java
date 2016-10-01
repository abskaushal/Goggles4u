package com.ts.mobilelab.goggles4u.core;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.CartData;
import com.ts.mobilelab.goggles4u.data.ChildData;
import com.ts.mobilelab.goggles4u.data.CollectionImageData;
import com.ts.mobilelab.goggles4u.data.NewArrivalData;
import com.ts.mobilelab.goggles4u.data.PrescriptionData;
import com.ts.mobilelab.goggles4u.data.ProductData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mahesh on 16-03-2016.
 */
public class GogglesManager extends Application {

    private static GogglesManager mGogglesManager;
    private static Context mContext;

    public ArrayList<NewArrivalData> getNewArrivalDataList() {
        if(newArrivalDataList == null){
            newArrivalDataList = new ArrayList<>();
        }
        return newArrivalDataList;
    }

    public void setNewArrivalDataList(ArrayList<NewArrivalData> newArrivalDataList) {
        this.newArrivalDataList = newArrivalDataList;
    }

    private ArrayList<NewArrivalData> newArrivalDataList;
    private ArrayList<CollectionImageData> imageDataList;
    private  ArrayList<PrescriptionData> prescriptionDataArrayList;

    public ArrayList<CartData> getCartDataArrayList() {
        if(cartDataArrayList == null){
            cartDataArrayList = new ArrayList<>();
        }
        return cartDataArrayList;
    }

    public void setCartDataArrayList(ArrayList<CartData> cartDataArrayList) {
        this.cartDataArrayList = cartDataArrayList;
    }

    private ArrayList<CartData> cartDataArrayList;

    public HashMap<String, ArrayList<ChildData>> getCategoryMap() {
        if(categoryMap == null){
            categoryMap = new HashMap<>();
        }
        return categoryMap;
    }

    public void setCategoryMap(HashMap<String, ArrayList<ChildData>> categoryMap) {
        this.categoryMap = categoryMap;
    }

    private HashMap<String,ArrayList<ChildData>> categoryMap;

    public ArrayList<ProductData> getProductDataArrayList() {
        if(productDataArrayList == null){
            productDataArrayList = new ArrayList<>();
        }
        return productDataArrayList;
    }

    public void setProductDataArrayList(ArrayList<ProductData> productDataArrayList) {
        this.productDataArrayList = productDataArrayList;
    }

    private  ArrayList<ProductData> productDataArrayList;

    public ArrayList<PrescriptionData> getPrescriptionDataArrayList() {
        if(prescriptionDataArrayList == null){
            prescriptionDataArrayList = new ArrayList<>();
        }
        return prescriptionDataArrayList;
    }

    public void setPrescriptionDataArrayList(ArrayList<PrescriptionData> prescriptionDataArrayList) {
        this.prescriptionDataArrayList = prescriptionDataArrayList;
    }

    public ArrayList<String> getBannerImgList() {
        if(bannerImgList == null){
            bannerImgList = new ArrayList<>();
        }
        return bannerImgList;
    }

    public void setBannerImgList(ArrayList<String> bannerImgList) {
        this.bannerImgList = bannerImgList;
    }

    public ArrayList<CollectionImageData> getImageDataList() {
        if(imageDataList == null){
            imageDataList = new ArrayList<>();
        }
        return imageDataList;
    }

    public void setImageDataList(ArrayList<CollectionImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }

    private ArrayList<String> bannerImgList;
    @Override
    public void onCreate() {
        mGogglesManager = new GogglesManager();
        Log.v("manager create", "");
    GogglesManager.mContext = getApplicationContext();

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(150 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);


        super.onCreate();
       // initImageLoader(getApplicationContext());
    }




    public static Context getAppContext() {
        return mContext;
    }

    public static GogglesManager getInstance(){
        return mGogglesManager;
    }


}
