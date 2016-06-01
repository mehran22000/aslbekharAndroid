package com.aslbekhar.aslbekharandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.aslbekhar.aslbekharandroid.models.CategoryModel;
import com.aslbekhar.aslbekharandroid.models.StoreModel;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.RecyclerItemClickListener;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_STORE_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEFAULT_CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DOWNLOAD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;

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

        if (getArguments() != null && getArguments().getBoolean(OFFLINE_MODE, false)){
            view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        }

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
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Bundle bundle = new Bundle();
                            bundle.putString(CITY_CODE, getArguments().getString(CITY_CODE));
                            bundle.putString(CITY_NAME, getArguments().getString(CITY_NAME));
                            bundle.putString(CAT_NAME, modelListToShow.get(position).getTitle());

                            BrandListFragment fragment = new BrandListFragment();
                            fragment.setArguments(bundle);

                            callBack.openNewContentFragment(fragment);

                        }
                    })
            );
        }
        return view;
    }

    private void performSearch(String search) {
        modelListToShow.clear();
        if (!search.equals("")) {
            for (CategoryModel model : modelList) {
                if (model.getTitle().toLowerCase().contains(search.toLowerCase())){
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
        List<StoreModel> storeModelList = null;
        try {
            storeModelList = JSON.parseArray(response, StoreModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (storeModelList != null) {
                Snippets.setSP(cityCode + STORE_LIST, response);
            }
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline() {
        view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        offlineCallBack.offlineMode(true);
    }
}
