package com.aslbekhar.aslbekharandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.aslbekhar.aslbekharandroid.adapters.BrandListAdapter;
import com.aslbekhar.aslbekharandroid.models.BrandModel;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.RecyclerItemClickListener;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_VIEW_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_ID;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_LIST_DOWNLOAD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_LIST_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_BANNER_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_TO_CAT_FULL_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.showSlideUp;

/**
 * Created by Amin on 19/05/2016.
 */
public class BrandListFragment extends Fragment implements Interfaces.NetworkListeners {

    View view;
    Interfaces.MainActivityInterface callBack;
    String cityCode;
    String catName;
    RecyclerView recyclerView;
    BrandListAdapter adapter;
    List<BrandModel> modelList;
    List<BrandModel> modelListToShow = new ArrayList<>();
    LinearLayoutManager layoutManager;

    boolean fullScreenAdvertiseTimer = false;
    boolean fullScreenAdvertiseSecondTimer = false;
    ImageView fullScreenAdImageView;
    ImageView bannerAdImageView;
    ImageView listOverLay;
    ProgressView progressView;

    public BrandListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityCode = getArguments().getString(CITY_CODE);
        catName = getArguments().getString(CAT_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_search, container, false);
        setupUI(view);


        if (getArguments() != null && getArguments().getBoolean(OFFLINE_MODE, false)) {
            view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        }


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

        fullScreenAdImageView = (ImageView) view.findViewById(R.id.fullScreenAdvertise);
        listOverLay = (ImageView) view.findViewById(R.id.listOverLay);
        progressView = (ProgressView) view.findViewById(R.id.progressBar);

        bannerAdImageView = (ImageView) view.findViewById(R.id.bannerAdvertise);
        checkForBannerAdvertise();

        recyclerView = (RecyclerView) view.findViewById(R.id.listView);

        modelList = BrandModel.getBrandListBasedOnCat(cityCode, catName);

        NetworkRequests.getRequest(BRAND_LIST_URL, this, BRAND_LIST_DOWNLOAD);

        modelListToShow.clear();
        modelListToShow.addAll(modelList);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());

        // setting the layout manager of recyclerView
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BrandListAdapter(modelListToShow, getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        checkForAdvertisement(modelListToShow.get(position));
                    }
                })
        );

        return view;
    }



    private void checkForBannerAdvertise(){
        Glide.with(this)
                .load(CAT_BANNER_AD + cityCode + "." + catName + ".png")
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String StringModel,
                                               Target<GlideDrawable> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String StringModel, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        showSlideUp(bannerAdImageView, true, getContext());
                        return false;
                    }
                }).into(bannerAdImageView);
    }


    private void checkForAdvertisement(final BrandModel model) {

        fullScreenAdvertiseTimer = true;
        Snippets.showFade(listOverLay, true, 500);
        progressView.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fullScreenAdvertiseTimer) {
                    fullScreenAdvertiseTimer = false;
                    openStoreListFragment(model);
                }
            }
        }, ADVERTISEMENT_TIMEOUT);


        Glide.with(this)
                .load(CITY_TO_CAT_FULL_AD + cityCode + '.' + catName + model.getbName() + ".png")
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String StringModel, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (fullScreenAdvertiseTimer) {
                            fullScreenAdvertiseTimer = false;
                            openStoreListFragment(model);
                        }
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String StringModel, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (fullScreenAdvertiseTimer) {
                            fullScreenAdvertiseTimer = false;
                            fullScreenAdImageView.setVisibility(View.VISIBLE);
                            progressView.stop();
                            listOverLay.setVisibility(View.GONE);
                            fullScreenAdImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fullScreenAdvertiseSecondTimer = false;
                                    openStoreListFragment(model);
                                }
                            });
                            fullScreenAdvertiseSecondTimer = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (fullScreenAdvertiseSecondTimer) {
                                        fullScreenAdvertiseSecondTimer = false;
                                        openStoreListFragment(model);
                                    }
                                }
                            }, ADVERTISEMENT_VIEW_TIMEOUT);
                            return false;

                        } else {
                            return true;
                        }
                    }
                }).into(fullScreenAdImageView);


    }


    private void performSearch(String search) {
        modelListToShow.clear();
        if (!search.equals("")) {
            for (BrandModel model : modelList) {
                if (model.getbName().toLowerCase().contains(search.toLowerCase())) {
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

    private void openStoreListFragment(BrandModel brandModel) {
        Bundle bundle = new Bundle();

        bundle.putString(CITY_CODE, cityCode);
        bundle.putString(CAT_NAME, getArguments().getString(CAT_NAME));
        bundle.putString(BRAND_ID, brandModel.getbId());
        bundle.putString(BRAND_NAME, brandModel.getbName());

        StoreListFragment fragment = new StoreListFragment();
        fragment.setArguments(bundle);
        callBack.openNewContentFragment(fragment);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBack = (Interfaces.MainActivityInterface) getActivity();
    }

    @Override
    public void onResponse(String response, String tag) {
        if (tag.equals(BRAND_LIST_DOWNLOAD)) {
            List<BrandModel> brandModelList = null;
            try {
                brandModelList = JSON.parseArray(response, BrandModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (brandModelList != null) {
                    Snippets.setSP(BRAND_LIST, response);
                }
            }
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline() {

    }
}
