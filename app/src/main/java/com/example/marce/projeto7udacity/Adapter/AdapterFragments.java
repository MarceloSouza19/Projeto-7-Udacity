package com.example.marce.projeto7udacity.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.marce.projeto7udacity.Fragments.FragmentInfoApp;
import com.example.marce.projeto7udacity.Fragments.FragmentSale;
import com.example.marce.projeto7udacity.Fragments.FragmentStock;

public class AdapterFragments extends FragmentPagerAdapter {

    private String[] pageName;

    public AdapterFragments(FragmentManager fm, String[] pageName) {
        super(fm);
        this.pageName = pageName;
    }

    @Override
    public Fragment getItem(int count) {

        switch(count){
            case 0:
                return new FragmentStock();
            case 1:
                return new FragmentSale();
            case 2:
                return new FragmentInfoApp();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return this.pageName.length;
    }
}
