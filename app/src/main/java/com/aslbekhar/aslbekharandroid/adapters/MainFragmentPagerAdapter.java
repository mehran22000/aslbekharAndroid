package com.aslbekhar.aslbekharandroid.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aslbekhar.aslbekharandroid.fragments.CitiesFragment;
import com.aslbekhar.aslbekharandroid.fragments.DealsNearByFragment;
import com.aslbekhar.aslbekharandroid.fragments.HostFragment;
import com.aslbekhar.aslbekharandroid.fragments.MapNearByFragment;
import com.aslbekhar.aslbekharandroid.fragments.MyStoreAccountFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amin on 15/05/2016.
 * <p/>
 * This class will be used for
 */
public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final List<String> tabTitles;

    private List<Fragment> tabs = new ArrayList<>();

    public MainFragmentPagerAdapter(FragmentManager fragmentManager, List<String> tabTitles) {
        super(fragmentManager);
        this.tabTitles = tabTitles;

        initializeTabs();
    }

    private void initializeTabs() {
        tabs.add(HostFragment.newInstance(new CitiesFragment()));
        tabs.add(HostFragment.newInstance(new MapNearByFragment()));
        tabs.add(HostFragment.newInstance(new DealsNearByFragment()));
        tabs.add(HostFragment.newInstance(new MyStoreAccountFragment()));
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}