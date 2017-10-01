package com.mobisquid.mobicash.ecommerce.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.mobisquid.mobicash.ecommerce.fragments.ImageSwiperView;

import java.util.List;

/**
 * Created by ambre on 5/31/2017.
 */

public class ImagePagerAdapter extends FragmentPagerAdapter {
    private int[] images;
    private int initialPosition;
    private boolean isStart;
    private List<String> imagesList;
    private int mType;
    private static final int TYPE_STRING = 1;
    private static final int TYPE_INT = 2;

    public ImagePagerAdapter(FragmentManager fm, int... images) {
        super(fm);
        this.images = images;
        mType = TYPE_INT;

    }

    public ImagePagerAdapter(FragmentManager fm, List<String> images) {
        super(fm);
        this.imagesList = images;
        mType = TYPE_STRING;
    }

    @Override
    public int getCount() {
        return mType==TYPE_INT?images.length:imagesList.size();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        if (mType == TYPE_INT)
            bundle.putInt("image", images[position]);
        else
            bundle.putString("imagePath", imagesList.get(position));
        Fragment fragment = new ImageSwiperView();
        fragment.setArguments(bundle);
        return fragment;
    }
}