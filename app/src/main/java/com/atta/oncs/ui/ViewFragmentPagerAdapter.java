package com.atta.oncs.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.atta.oncs.R;

import java.util.Arrays;
import java.util.List;

public class ViewFragmentPagerAdapter extends FragmentPagerAdapter {

    /** Context of the app */
    private Context mContext;

    List<String> categories, categoriesEnglish;

    public ViewFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;


        String[] categories = mContext.getResources().getStringArray(R.array.categories);
        String[] categoriesEnglish = mContext.getResources().getStringArray(R.array.categories_english);


        this.categories = Arrays.asList(categories);
        this.categoriesEnglish = Arrays.asList(categoriesEnglish);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("category", position);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        // this method will be called for every fragment in the ViewPager

            return POSITION_NONE;

    }

}
