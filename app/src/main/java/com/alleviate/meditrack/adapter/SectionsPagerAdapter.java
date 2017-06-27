package com.alleviate.meditrack.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alleviate.meditrack.fragments.AllFragment;
import com.alleviate.meditrack.fragments.TodayFragment;
import com.alleviate.meditrack.fragments.UserFragment;

import com.alleviate.meditrack.constants.Constants;

/**
 * Created by felix on 10/6/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0:
                return new TodayFragment();
            case 1:
                return new AllFragment();
            case 2:
                return new UserFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 5 total pages.
        return Constants.num_tab;
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return Constants.tab_title_today;
            case 1:
                return Constants.tab_title_all;
            case 2:
                return Constants.tab_title_user;
        }
        return null;
    }
}