package com.aslbekhar.aslbekharandroid.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.adapters.MainFragmentPagerAdapter;
import com.aslbekhar.aslbekharandroid.fragments.HostFragment;
import com.aslbekhar.aslbekharandroid.utilities.BackStackFragment;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Interfaces.MainActivityInterface , Interfaces.OfflineInterface{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    MainFragmentPagerAdapter mainFragmentPagerAdapter;
    private int[] tabIcons = {
            R.drawable.tab_explore,
            R.drawable.tab_map_icon,
            R.drawable.tab_sale_icon,
            R.drawable.tab_mystore_icon
    };
    private List<String> tabTitles = null;
    private boolean offlineMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.findViewById(R.id.toolbarBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setTitle(R.string.app_name_persian);

        setupViewPagerAndTabs();
    }

    private void setupViewPagerAndTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabTitles = new ArrayList<String>() {{
            add(getResources().getString(R.string.tabExplore));
            add(getResources().getString(R.string.tabMap));
            add(getResources().getString(R.string.tabSale));
            add(getResources().getString(R.string.tabMyStore));
        }};
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), tabTitles);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mainFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    @Override
    public void onBackPressed()
    {
        if(!BackStackFragment.handleBackPressed(getSupportFragmentManager())){
            super.onBackPressed();
        }
    }

    public void openNewContentFragment(Fragment targetFragment) {
        targetFragment.getArguments().putBoolean(Constants.OFFLINE_MODE, offlineMode);
        HostFragment hostFragment = (HostFragment) mainFragmentPagerAdapter.getItem(viewPager.getCurrentItem());
        hostFragment.replaceFragment(targetFragment, true);
    }


    @Override
    public void offlineMode(boolean offline) {
            offlineMode = offline;
    }
}
