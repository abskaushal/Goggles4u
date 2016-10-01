package com.ts.mobilelab.goggles4u.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ts.mobilelab.goggles4u.views.utils.BannerSlidefragment;

/**
 * Created by Mahesh on 16-03-2016.
 */



public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    private int mMaxScreen = 3;

    BannerSlidefragment bFragment;


    public ViewPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);

        bFragment = new BannerSlidefragment();

    }

    @Override
    public Fragment getItem(int position) {

        //return new BannerSlidefragment().newInstance(context,position);
        return  new BannerSlidefragment().newInstance(context,position);
    }


    @Override
    public int getCount() {
        return mMaxScreen;
    }
}
