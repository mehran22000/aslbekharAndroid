package com.aslbekhar.aslbekharandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.adapters.CategoryListAdapter;
import com.aslbekhar.aslbekharandroid.models.AnalyticsAdvertisementModel;
import com.aslbekhar.aslbekharandroid.models.AnalyticsDataModel;
import com.aslbekhar.aslbekharandroid.models.CategoryModel;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_MAX_COUNT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_VIEW_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISE_CATEGORIES;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BANNER;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CATEGORY;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_BANNER_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NUMBER;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_STORE_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_TO_CAT_FULL_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEFAULT_CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DOWNLOAD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FULL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.StaticData.addShownCount;

/**
 * Created by Amin on 19/05/2016.
 */
public class CatListFragment extends Fragment implements Interfaces.NetworkListeners {

    View view;
    Interfaces.MainActivityInterface callBack;
    Interfaces.OfflineInterface offlineCallBack;
    RecyclerView recyclerView;
    CategoryListAdapter adapter;
    List<CategoryModel> modelList;
    List<CategoryModel> modelListToShow = new ArrayList<>();
    LinearLayoutManager layoutManager;
    String cityCode = "";
    boolean fragmentAlive;

    boolean fullScreenAdvertiseTimer = false;
    boolean fullScreenAdvertiseSecondTimer = false;
    ImageView fullScreenAdImageView;
    ImageView bannerAdImageView;
    ImageView listOverLay;
    ProgressView progressView;

    public CatListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        cityCode = getArguments().getString(CITY_CODE, "");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_search, container, false);

        setupUI(view);

        if (getArguments() != null && getArguments().getBoolean(OFFLINE_MODE, false)) {
            view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        }

        fullScreenAdImageView = (ImageView) view.findViewById(R.id.fullScreenAdvertise);
        progressView = (ProgressView) view.findViewById(R.id.progressBar);
        listOverLay = (ImageView) view.findViewById(R.id.listOverLay);

        bannerAdImageView = (ImageView) view.findViewById(R.id.bannerAdvertise);
        checkForBannerAdvertise();

        recyclerView = (RecyclerView) view.findViewById(R.id.listView);

        NetworkRequests.getRequest(CITY_STORE_URL + getArguments().getString(CITY_CODE, DEFAULT_CITY_CODE), this, DOWNLOAD);

        String json = getSP(getArguments().getString(CITY_CODE) + CAT_LIST);

        final EditText searchText = (EditText) view.findViewById(R.id.searchEditText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
                if (s.length() > 0) {
                    ((ImageView) view.findViewById(R.id.searchClear)).setImageResource(R.drawable.search_clear);
                    view.findViewById(R.id.searchClear).setTag(R.drawable.search_clear);
                } else {
                    ((ImageView) view.findViewById(R.id.searchClear)).setImageResource(R.drawable.search);
                    view.findViewById(R.id.searchClear).setTag(R.drawable.search);
                }
            }
        });
        view.findViewById(R.id.searchClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = 0;
                try {
                    tag = (int) v.getTag();
                } catch (Exception ignored) {
                }
                if (tag == R.drawable.search_clear) {
                    performSearch("");
                    searchText.setText("");
                }
            }
        });


        if (!json.equals(FALSE)) {

            modelList = JSON.parseArray(json, CategoryModel.class);

            modelListToShow.clear();
            modelListToShow.addAll(modelList);

            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(getActivity());

            // setting the layout manager of recyclerView
            recyclerView.setLayoutManager(layoutManager);

            adapter = new CategoryListAdapter(modelListToShow, getActivity(), this, cityCode);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    public void openBrandListFromAdapter(CategoryModel model) {
        AnalyticsDataModel.saveAnalytic(CATEGORY,
                model.getTitle());
        checkForAdvertisement(model);
    }

    private void checkForBannerAdvertise() {

        String url = CAT_BANNER_AD + cityCode + ".png";
        Picasso.with(getContext()).load(url).into(bannerAdImageView, new Callback() {
            @Override
            public void onSuccess() {
                AnalyticsAdvertisementModel.sendAdvertisementAnalytics(
                        new AnalyticsAdvertisementModel("ad." + cityCode + ".png", ADVERTISE_CATEGORIES, BANNER));
                bannerAdImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
            }
        });
    }

    private void openBrandListFragment(CategoryModel model) {
        Bundle bundle = new Bundle();
        bundle.putString(CITY_CODE, getArguments().getString(CITY_CODE));
        bundle.putString(CITY_NAME, getArguments().getString(CITY_NAME));
        bundle.putString(CAT_NAME, model.getTitle());
        bundle.putString(CAT_NUMBER, model.getcId());

        BrandListFragment fragment = new BrandListFragment();
        fragment.setArguments(bundle);

        callBack.openNewContentFragment(fragment, 0);
    }

    private void checkForAdvertisement(final CategoryModel model) {

        if (!Snippets.isOnline(getActivity())){
            openBrandListFragment(model);
            return;
        }

        if (addShownCount > ADVERTISEMENT_MAX_COUNT) {
            return;
        }


        fullScreenAdvertiseTimer = true;
        Snippets.showFade(listOverLay, true, 500);
        progressView.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fullScreenAdvertiseTimer) {
                    fullScreenAdvertiseTimer = false;
                    openBrandListFragment(model);
                }
            }
        }, ADVERTISEMENT_TIMEOUT);
        Picasso.with(getContext())
                .load(CITY_TO_CAT_FULL_AD + cityCode + ".cat" + model.getcId() + ".png")
                .into(fullScreenAdImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (fullScreenAdvertiseTimer) {
                            addShownCount++;
                            AnalyticsAdvertisementModel.sendAdvertisementAnalytics(
                                    new AnalyticsAdvertisementModel("ad." + cityCode + ".cat" + model.getcId() + ".png", ADVERTISE_CATEGORIES, FULL));
                            fullScreenAdvertiseTimer = false;
                            fullScreenAdImageView.setVisibility(View.VISIBLE);
                            progressView.stop();
                            listOverLay.setVisibility(View.GONE);
                            fullScreenAdImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fullScreenAdvertiseSecondTimer = false;
                                    openBrandListFragment(model);
                                }
                            });
                            fullScreenAdvertiseSecondTimer = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (fullScreenAdvertiseSecondTimer) {
                                        fullScreenAdvertiseSecondTimer = false;
                                        openBrandListFragment(model);
                                    }
                                }
                            }, ADVERTISEMENT_VIEW_TIMEOUT);

                        }
                    }

                    @Override
                    public void onError() {

                        if (fullScreenAdvertiseTimer) {
                            fullScreenAdvertiseTimer = false;
                            openBrandListFragment(model);
                        }

                    }
                });
    }

    private void performSearch(String search) {
        modelListToShow.clear();
        if (!search.equals("")) {
            for (CategoryModel model : modelList) {
                if (model.getTitle().toLowerCase().contains(search.toLowerCase())) {
                    modelListToShow.add(model);
                }
            }
        } else {
            modelListToShow.addAll(modelList);
        }
        adapter.notifyDataSetChanged();
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (getActivity().getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentAlive = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentAlive = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentAlive = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentAlive = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBack = (Interfaces.MainActivityInterface) getActivity();
        offlineCallBack = (Interfaces.OfflineInterface) getActivity();
    }

    @Override
    public void onResponse(String response, String tag) {
        view.findViewById(R.id.offlineLay).setVisibility(View.GONE);
        offlineCallBack.offlineMode(false);
        if (response.startsWith("[") && response.endsWith("]")) {
            Snippets.setSP(cityCode + STORE_LIST, response);
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline(String tag) {
        view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        offlineCallBack.offlineMode(true);
    }
}
