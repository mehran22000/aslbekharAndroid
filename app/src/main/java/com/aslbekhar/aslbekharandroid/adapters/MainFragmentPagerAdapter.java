package com.aslbekhar.aslbekharandroid.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aslbekhar.aslbekharandroid.activities.MainActivity;
import com.aslbekhar.aslbekharandroid.fragments.CitiesFragment;
import com.aslbekhar.aslbekharandroid.fragments.HostFragment;
import com.aslbekhar.aslbekharandroid.fragments.ListNearByFragment;
import com.aslbekhar.aslbekharandroid.fragments.MapNearByFragment;
import com.aslbekhar.aslbekharandroid.fragments.MyStoreAccountFragment;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.NORMAL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.NORMAL_OR_DEAL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.PLAY_SERVICES_ON_OR_OFF;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRUE;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;

/**
 * Created by Amin on 15/05/2016.
 * <p/>
 * This class will be used for
 */
public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private MainActivity activity;
    private final List<String> tabTitles;

    private List<Fragment> tabs = new ArrayList<>();

    public MainFragmentPagerAdapter(MainActivity activity,FragmentManager fragmentManager, List<String> tabTitles) {
        super(fragmentManager);
        this.activity = activity;
        this.tabTitles = tabTitles;

        initializeTabs();
    }

    private void initializeTabs() {
        tabs.add(HostFragment.newInstance(new CitiesFragment()));
        if (getSP(PLAY_SERVICES_ON_OR_OFF).equals(TRUE)) {
            MapNearByFragment fragment = new MapNearByFragment();
            tabs.add(HostFragment.newInstance(fragment));
            activity.storesTab = fragment;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(NORMAL_OR_DEAL, NORMAL);
            ListNearByFragment fragment = new ListNearByFragment();
            fragment.setArguments(bundle);
            activity.storesTab = fragment;
            tabs.add(fragment);
        }
        ListNearByFragment fragment = new ListNearByFragment();
        tabs.add(HostFragment.newInstance(fragment));
        activity.salesTab = fragment;
        tabs.add(HostFragment.newInstance(new MyStoreAccountFragment()));
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==  3){
            ((HostFragment)tabs.get(3)).fragment.onResume();
        }
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