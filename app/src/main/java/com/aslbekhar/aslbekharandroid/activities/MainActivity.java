package com.aslbekhar.aslbekharandroid.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.adapters.MainFragmentPagerAdapter;
import com.aslbekhar.aslbekharandroid.fragments.HostFragment;
import com.aslbekhar.aslbekharandroid.models.VersionCheckModel;
import com.aslbekhar.aslbekharandroid.utilities.BackStackFragment;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.APP_VERSION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CHECK_VERSION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEVICE_ID;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_CITY_ENGLISH_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOG_TAG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.REGISTERED_DEVICE_ID;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.REGISTER_ANDROID_DEVICE_LINK;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.REGISTRATION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.REGISTRATION_COMPLETE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.SAVED_ANALYTICS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.SEND_ANALYTICS_LINK;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.SUCCESS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRUE;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;

public class MainActivity extends AppCompatActivity implements Interfaces.MainActivityInterface,
        Interfaces.OfflineInterface, Interfaces.NetworkListeners, Interfaces.showProgressBar {

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

    // for keeping record of previous tabs, for clicking on back
    Stack<Integer> tabStack = new Stack<>();
    int currentTab = 0;
    Dialog dialog;

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

        String analyticSavedJson = Snippets.getSP(Constants.SAVED_ANALYTICS);
        if (!analyticSavedJson.equals(FALSE)) {
            NetworkRequests.postRequest(SEND_ANALYTICS_LINK, this, SEND_ANALYTICS_LINK,
                    "{\"interests\":\"" + Base64.encodeToString(analyticSavedJson.getBytes(), Base64.NO_WRAP) + "\"}");
        }

        if (!getSP(DEVICE_ID).equals(FALSE)) {
            String cityName = getSP(LAST_CITY_ENGLISH_NAME).toLowerCase();
//            if (cityName.equals(FALSE)) {
//                cityName = "tehran";
//            }
            String postJson = "{\"device\":\""
                    + getSP(DEVICE_ID) + "\",\"city\":\""
                    + cityName
                    + "\"}";
            Log.d(LOG_TAG, "registering token json: " + postJson);
            NetworkRequests.postRequest(REGISTER_ANDROID_DEVICE_LINK,
                    this, REGISTRATION, postJson);
        }

        NetworkRequests.getRequest(CHECK_VERSION, this, CHECK_VERSION);
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
//        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // adding previous tab to stack
                tabStack.push(currentTab);
                currentTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (!BackStackFragment.handleBackPressed(getSupportFragmentManager())) {
            if (tabStack.size() > 0) {
                currentTab = tabStack.pop();
                viewPager.setCurrentItem(currentTab, true);
                if (tabStack.size() > 0 && tabStack.peek() == currentTab) {
                    tabStack.pop();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    public void openNewContentFragment(Fragment targetFragment) {

        if (targetFragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(OFFLINE_MODE, offlineMode);
            targetFragment.setArguments(bundle);
        } else {
            targetFragment.getArguments().putBoolean(OFFLINE_MODE, offlineMode);
        }
        HostFragment hostFragment = (HostFragment) mainFragmentPagerAdapter.getItem(viewPager.getCurrentItem());
        hostFragment.replaceFragment(targetFragment, true);
    }

    public void openNewContentFragment(Fragment targetFragment, int position) {

        if (targetFragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(OFFLINE_MODE, offlineMode);
            targetFragment.setArguments(bundle);
        } else {
            targetFragment.getArguments().putBoolean(OFFLINE_MODE, offlineMode);
        }
        HostFragment hostFragment = (HostFragment) mainFragmentPagerAdapter.getItem(position);
        hostFragment.replaceFragment(targetFragment, true);
    }


    @Override
    public void offlineMode(boolean offline) {
        offlineMode = offline;
    }

    @Override
    public void onResponse(String response, String tag) {
        if (tag.equals(SEND_ANALYTICS_LINK)) {
            Log.d(LOG_TAG, SEND_ANALYTICS_LINK + " onResponse: " + response);
            if (response.toLowerCase().contains(SUCCESS.toLowerCase())){
                setSP(SAVED_ANALYTICS, FALSE);
            }
        } else if (tag.equals(CHECK_VERSION)) {
            VersionCheckModel versionCheckModel = null;
            try {
                versionCheckModel = JSON.parseObject(response, VersionCheckModel.class);
            } catch (Exception e) {
                return;
            }
            if (versionCheckModel.getCurrent().compareTo(APP_VERSION) > 0) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                final View dialogView = LayoutInflater.from(this).
                        inflate(R.layout.dialog_update_required, null);

                Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/theme.ttf");

                ((TextView) dialogView.findViewById(R.id.text)).setTypeface(tf);
                ((TextView) dialogView.findViewById(R.id.update)).setTypeface(tf);
                ((TextView) dialogView.findViewById(R.id.skip)).setTypeface(tf);

                dialogView.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String appPackageName = getPackageName();
                        try {
                            startActivity(new
                                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new
                                    Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });

                if (versionCheckModel.getMinSupport().compareTo(APP_VERSION) > 0) {
                    dialogBuilder.setCancelable(false);
                    dialogView.findViewById(R.id.skip).setVisibility(View.GONE);
                } else {
                    dialogView.findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });
                    dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                }
                dialogBuilder.setView(dialogView);
                dialog = dialogBuilder.create();

                dialog.show();
            }
        } else if (tag.equals(REGISTRATION)) {
            if (response.toLowerCase().contains(SUCCESS.toLowerCase())) {
                setSP(REGISTRATION_COMPLETE, TRUE);
                setSP(REGISTERED_DEVICE_ID, getSP(DEVICE_ID));
                Log.d(LOG_TAG, REGISTRATION_COMPLETE);
            } else {
                Log.d(LOG_TAG, "Problem in registration" + "onResponse: " + response);
            }
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {
        Log.d(LOG_TAG, tag + "onError: " + error.getMessage());
    }

    @Override
    public void onOffline(String tag) {

    }

    @Override
    public void showProgressBar(boolean showOrHide) {
        if (showOrHide) {
            ((ProgressView) findViewById(R.id.toolbar).findViewById(R.id.toolbarProgressBar)).start();
        } else {
            ((ProgressView) findViewById(R.id.toolbar).findViewById(R.id.toolbarProgressBar)).stop();
        }
    }
}
