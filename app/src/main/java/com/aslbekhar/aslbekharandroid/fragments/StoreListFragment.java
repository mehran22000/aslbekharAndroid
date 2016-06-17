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
import com.aslbekhar.aslbekharandroid.adapters.StoreListAdapter;
import com.aslbekhar.aslbekharandroid.models.BrandVerificationModel;
import com.aslbekhar.aslbekharandroid.models.StoreModel;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADDRESS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_VIEW_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_ID;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_VERIFICATION_DOWNLOAD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_VERIFICATION_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_BANNER_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NUMBER;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_TO_CAT_FULL_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DISCOUNT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LATITUDE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOGO;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LONGITUDE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.MAP_TYPE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.MAP_TYPE_SHOW_SINGLE_STORE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_DETAILS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TELL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TITLE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.VERIFICATION_TIPS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.VERIFIED;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.WORK_HOUR;

/**
 * Created by Amin on 19/05/2016.
 */
public class StoreListFragment extends Fragment implements Interfaces.NetworkListeners {

    View view;
    Interfaces.MainActivityInterface callBack;
    RecyclerView recyclerView;
    StoreListAdapter adapter;
    List<StoreModel> modelList;
    List<StoreModel> modelListToShow = new ArrayList<>();
    LinearLayoutManager layoutManager;
    String cityCode;
    String catName;
    String catNum;
    String brandName;
    String brandId;


    boolean fullScreenAdvertiseTimer = false;
    boolean fullScreenAdvertiseSecondTimer = false;
    ImageView fullScreenAdImageView;
    ImageView bannerAdImageView;
    ImageView listOverLay;
    ProgressView progressView;

    public StoreListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                if (s.length() > 0){
                    ((ImageView)view.findViewById(R.id.searchClear)).setImageResource(R.drawable.search_clear);
                    view.findViewById(R.id.searchClear).setTag(R.drawable.search_clear);
                } else {
                    ((ImageView)view.findViewById(R.id.searchClear)).setImageResource(R.drawable.search);
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
                } catch (Exception ignored){
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

        cityCode = getArguments().getString(CITY_CODE);
        catName = getArguments().getString(CAT_NAME);
        catNum = getArguments().getString(CAT_NUMBER);
        brandName = getArguments().getString(BRAND_NAME);
        brandId = getArguments().getString(BRAND_ID);

        modelList = StoreModel.getStoreListBasedOnCatNameAndBrand(
                cityCode,
                catName,
                brandId);

        modelListToShow.clear();
        modelListToShow.addAll(modelList);

        // to download verification instructions for this brand, if any
        NetworkRequests.getRequest(BRAND_VERIFICATION_URL + getArguments().getString(BRAND_ID),
                this, BRAND_VERIFICATION_DOWNLOAD);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());

        // setting the layout manager of recyclerView
        recyclerView.setLayoutManager(layoutManager);

        adapter = new StoreListAdapter(modelListToShow, getActivity(), this, cityCode);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void checkForAdvertisement(final StoreModel model) {

        fullScreenAdvertiseTimer = true;
        Snippets.showFade(listOverLay, true, 500);
        progressView.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fullScreenAdvertiseTimer) {
                    fullScreenAdvertiseTimer = false;
                    openStoreFragment(model);
                }
            }
        }, ADVERTISEMENT_TIMEOUT);


        Picasso.with(getContext())
                .load(CITY_TO_CAT_FULL_AD + cityCode + ".cat" + catNum + "."+ brandName +"." + model.getsId() + ".png")
                .into(fullScreenAdImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (fullScreenAdvertiseTimer) {
                            fullScreenAdvertiseTimer = false;
                            fullScreenAdImageView.setVisibility(View.VISIBLE);
                            progressView.stop();
                            listOverLay.setVisibility(View.GONE);
                            fullScreenAdImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fullScreenAdvertiseSecondTimer = false;
                                    openStoreFragment(model);
                                }
                            });
                            fullScreenAdvertiseSecondTimer = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (fullScreenAdvertiseSecondTimer) {
                                        fullScreenAdvertiseSecondTimer = false;
                                        openStoreFragment(model);
                                    }
                                }
                            }, ADVERTISEMENT_VIEW_TIMEOUT);

                        }
                    }

                    @Override
                    public void onError() {

                        if (fullScreenAdvertiseTimer) {
                            fullScreenAdvertiseTimer = false;
                            openStoreFragment(model);
                        }

                    }
                });

        Glide.with(this)
                .load(CITY_TO_CAT_FULL_AD + cityCode + '.' + catNum + "."+ brandName +"." + model.getsId() + ".png")
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String StringModel, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (fullScreenAdvertiseTimer) {
                            fullScreenAdvertiseTimer = false;
                            openStoreFragment(model);
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
                                    openStoreFragment(model);
                                }
                            });
                            fullScreenAdvertiseSecondTimer = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (fullScreenAdvertiseSecondTimer) {
                                        fullScreenAdvertiseSecondTimer = false;
                                        openStoreFragment(model);
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

    public void openStoreFragment(StoreModel model) {


        Bundle bundle = new Bundle();

        bundle.putString(CITY_CODE, cityCode);
        bundle.putString(CAT_NAME, catName);
        bundle.putString(CAT_NUMBER, catNum);
        bundle.putString(BRAND_ID, model.getbId());
        bundle.putString(BRAND_NAME, model.getbName());
        bundle.putString(STORE_DETAILS, JSON.toJSONString(model));

        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(bundle);
        callBack.openNewContentFragment(fragment);

    }

    private void checkForBannerAdvertise(){

        String url = CAT_BANNER_AD + cityCode + ".cat" + catNum + "." + brandName + ".png";
        Picasso.with(getContext()).load(url).into(bannerAdImageView, new Callback() {
            @Override
            public void onSuccess() {
                bannerAdImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
            }
        });
    }

    private void performSearch(String search) {
        modelListToShow.clear();
        if (!search.equals("")) {
            for (StoreModel model : modelList) {
                if (model.getsName().contains(search) || model.getbName().contains(search)) {
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

    public void showOnMap(StoreModel model) {


        Bundle bundle = new Bundle();
        bundle.putString(CITY_CODE, cityCode);
        bundle.putString(LATITUDE, model.getsLat());
        bundle.putString(LONGITUDE, model.getsLong());
        bundle.putString(TITLE, model.getsName());
        bundle.putString(WORK_HOUR, model.getsHour());
        bundle.putString(ADDRESS, model.getsAddress());
        bundle.putString(TELL, model.getsTel1());
        bundle.putInt(DISCOUNT, model.getsDiscount());
        bundle.putString(VERIFIED, model.getsVerified());
        bundle.putString(LOGO, model.getbName());
        bundle.putInt(MAP_TYPE, MAP_TYPE_SHOW_SINGLE_STORE);

        MapNearByFragment fragment = new MapNearByFragment();
        fragment.setArguments(bundle);

        callBack.openNewContentFragment(fragment, 0);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBack = (Interfaces.MainActivityInterface) getActivity();
    }

    @Override
    public void onResponse(final String response, String tag) {
        if (tag.equals(BRAND_VERIFICATION_DOWNLOAD)) {
            List<BrandVerificationModel> brandVerificationModelList = null;
            try {
                brandVerificationModelList = JSON.parseArray(response, BrandVerificationModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (brandVerificationModelList != null && brandVerificationModelList.size() > 0) {
                view.findViewById(R.id.verificationTips).setVisibility(View.VISIBLE);
                view.findViewById(R.id.verificationTips).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openBrandVarificationFragment(response);
                    }
                });
            }

        }
    }

    private void openBrandVarificationFragment(String response) {

        Bundle bundle = new Bundle();

        bundle.putString(CITY_CODE, cityCode);
        bundle.putString(CAT_NAME, getArguments().getString(CAT_NAME));
        bundle.putString(BRAND_ID, brandId);
        bundle.putString(BRAND_NAME, brandName);
        bundle.putString(VERIFICATION_TIPS, response);

        BrandVerificationFragment fragment = new BrandVerificationFragment();
        fragment.setArguments(bundle);
        callBack.openNewContentFragment(fragment);
    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline(String tag) {

    }
}
