package com.rsah.watermeter.Activity.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.rsah.watermeter.Fragment.HomeFragment;
import com.rsah.watermeter.Fragment.TasklistFragment;
import com.rsah.watermeter.Fragment.UpdateScanFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                UpdateScanFragment tab2 = new UpdateScanFragment();
                return  tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}