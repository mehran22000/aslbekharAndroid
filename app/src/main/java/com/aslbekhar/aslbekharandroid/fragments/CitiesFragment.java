package com.aslbekhar.aslbekharandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.adapters.CityListAdapter;
import com.aslbekhar.aslbekharandroid.models.AnalyticsAdvertisementModel;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_MAX_COUNT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_VIEW_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISE_MAIN;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FULL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_CITY_ENGLISH_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOG_TAG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.REGISTRATION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.REGISTRATION_COMPLETE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.SUCCESS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRUE;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.showFade;
import static com.aslbekhar.aslbekharandroid.utilities.StaticData.addShownCount;
import static com.aslbekhar.aslbekharandroid.utilities.StaticData.getCityModelList;

/**
 * Created by Amin on 14/05/2016.
 * <p/>
 * This class will be used for
 */
public class CitiesFragment extends android.support.v4.app.Fragment implements Interfaces.NetworkListeners {

    View view;
    Interfaces.MainActivityInterface callBack;
    RecyclerView recyclerView;
    CityListAdapter adapter;
    List<CityModel> modelList = getCityModelList();
    List<CityModel> modelListToShow = new ArrayList<>();
    LinearLayoutManager layoutManager;

    boolean fullScreenAdvertiseTimer = false;
    boolean fullScreenAdvertiseSecondTimer = false;
    ImageView fullScreenAdImageView;
    ImageView listOverLay;
    ProgressView progressView;

    public CitiesFragment() {

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

        recyclerView = (RecyclerView) view.findViewById(R.id.listView);

        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(), 3);

        // setting the layout manager of recyclerView
        recyclerView.setLayoutManager(layoutManager);

        modelListToShow.clear();
        modelListToShow.addAll(modelList);

        adapter = new CityListAdapter(modelListToShow, getActivity(), this);
        recyclerView.setAdapter(adapter);
        if (getSP(LAST_CITY_CODE).equals(FALSE)) {
            setSP(LAST_CITY_CODE, "021");
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        view.findViewById(R.id.root).setBackgroundColor(getResources().getColor(R.color.white));
        view.findViewById(R.id.searchEditText).setVisibility(View.GONE);

        return view;
    }

    public void openCatFromAdapter(CityModel model) {
        setSP(LAST_CITY_CODE, model.getId());
        setSP(LAST_CITY_ENGLISH_NAME, model.getEnglishName());
        checkForAdvertisement(model);
    }


    private void performSearch(String search) {
        modelListToShow.clear();
        if (!search.equals("")) {
            for (CityModel model : modelList) {
                if (model.getPersianName().toLowerCase().contains(search) ||
                        model.getEnglishName().toLowerCase().contains(search)) {
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

    private void checkForAdvertisement(final CityModel model) {

        if (!Snippets.isOnline(getActivity())){
            openCatListFragment(model);
            return;
        }

        if (addShownCount > ADVERTISEMENT_MAX_COUNT) {
            openCatListFragment(model);
            return;
        }

        fullScreenAdvertiseTimer = true;
        showFade(listOverLay, true, 500);
        listOverLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        progressView.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fullScreenAdvertiseTimer) {
                    fullScreenAdvertiseTimer = false;
                    openCatListFragment(model);
                }
            }
        }, ADVERTISEMENT_TIMEOUT);

        Picasso.with(getContext())
                .load(Constants.CITY_TO_CAT_FULL_AD + model.getId() + ".png")
                .into(fullScreenAdImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (fullScreenAdvertiseTimer) {
                            addShownCount++;
                            AnalyticsAdvertisementModel.sendAdvertisementAnalytics(
                                    new AnalyticsAdvertisementModel("ad." + model.getId() + ".png", ADVERTISE_MAIN, FULL));
                            fullScreenAdvertiseTimer = false;
                            fullScreenAdImageView.setVisibility(View.VISIBLE);
                            progressView.stop();
                            listOverLay.setVisibility(View.GONE);
                            fullScreenAdImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fullScreenAdvertiseSecondTimer = false;
                                    openCatListFragment(model);
                                }
                            });
                            fullScreenAdvertiseSecondTimer = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (fullScreenAdvertiseSecondTimer) {
                                        fullScreenAdvertiseSecondTimer = false;
                                        openCatListFragment(model);
                                    }
                                }
                            }, ADVERTISEMENT_VIEW_TIMEOUT);

                        }
                    }

                    @Override
                    public void onError() {

                        if (fullScreenAdvertiseTimer) {
                            fullScreenAdvertiseTimer = false;
                            openCatListFragment(model);
                        }

                    }
                });
    }

    public void openCatListFragment(CityModel model) {

        fullScreenAdvertiseTimer = false;
        fullScreenAdvertiseSecondTimer = false;
        Bundle bundle = new Bundle();
        bundle.putString(CITY_CODE, model.getId());
        bundle.putString(CITY_NAME, model.getPersianName());
        bundle.putString(CITY_NAME, model.getPersianName());

        CatListFragment fragment = new CatListFragment();
        fragment.setArguments(bundle);

        callBack.openNewContentFragment(fragment, 0);

        if (fullScreenAdImageView.getVisibility() == View.VISIBLE) {
            fullScreenAdImageView.setVisibility(View.GONE);
        }
        listOverLay.setVisibility(View.GONE);
        progressView.stop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (Interfaces.MainActivityInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        callBack = (Interfaces.MainActivityInterface) getActivity();
    }

    @Override
    public void onResponse(String response, String tag) {
        if (tag.equals(REGISTRATION)) {
            if (response.toLowerCase().contains(SUCCESS.toLowerCase())) {
                setSP(REGISTRATION_COMPLETE, TRUE);
                Log.d(LOG_TAG, REGISTRATION_COMPLETE);
            } else {
                Log.d(LOG_TAG, "Problem in registration" + "onResponse: " + response);
            }
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline(String tag) {

    }
}
