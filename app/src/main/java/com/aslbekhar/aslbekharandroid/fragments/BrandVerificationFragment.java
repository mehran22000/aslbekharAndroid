package com.aslbekhar.aslbekharandroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.adapters.BrandVerificationAdapter;
import com.aslbekhar.aslbekharandroid.models.BrandVerificationModel;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_ID;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.VERIFICATION_TIPS;

/**
 * Created by Amin on 19/05/2016.
 *
 */
public class BrandVerificationFragment extends Fragment implements Interfaces.NetworkListeners {

    View view;
    Interfaces.MainActivityInterface callBack;
    RecyclerView recyclerView;
    BrandVerificationAdapter adapter;
    List<BrandVerificationModel> modelList;
    List<BrandVerificationModel> modelListToShow = new ArrayList<>();
    LinearLayoutManager layoutManager;
    String cityCode;
    String catName;
    String brandName;
    String brandId;

    public BrandVerificationFragment() {

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

        if (getArguments() != null && getArguments().getBoolean(OFFLINE_MODE, false)) {
            view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.searchEditText).setVisibility(View.GONE);
        view.findViewById(R.id.searchClear).setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.listView);

        cityCode = getArguments().getString(CITY_CODE);
        catName = getArguments().getString(CAT_NAME);
        brandName = getArguments().getString(BRAND_NAME);
        brandId = getArguments().getString(BRAND_ID);

        modelList = JSON.parseArray(getArguments().getString(VERIFICATION_TIPS, "[]"), BrandVerificationModel.class);

        modelListToShow.clear();
        modelListToShow.addAll(modelList);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());

        // setting the layout manager of recyclerView
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BrandVerificationAdapter(modelListToShow, getActivity(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBack = (Interfaces.MainActivityInterface) getActivity();
    }

    @Override
    public void onResponse(String response, String tag) {

    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline(String tag) {

    }
}
