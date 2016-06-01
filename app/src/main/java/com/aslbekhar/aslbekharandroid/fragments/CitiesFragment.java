package com.aslbekhar.aslbekharandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.adapters.CityListAdapter;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.RecyclerItemClickListener;
import com.aslbekhar.aslbekharandroid.utilities.StaticData;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;

/**
 * Created by Amin on 14/05/2016.
 * <p/>
 * This class will be used for
 */
public class CitiesFragment extends android.support.v4.app.Fragment {

    View view;
    Interfaces.MainActivityInterface callBack;
    RecyclerView recyclerView;
    CityListAdapter adapter;
    List<CityModel> modelList = StaticData.getCityModelList();
    List<CityModel> modelListToShow = new ArrayList<>();
    LinearLayoutManager layoutManager;

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

        recyclerView = (RecyclerView) view.findViewById(R.id.listView);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());

        // setting the layout manager of recyclerView
        recyclerView.setLayoutManager(layoutManager);

        modelListToShow.clear();
        modelListToShow.addAll(modelList);

        adapter = new CityListAdapter(modelListToShow, getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        openCityStoreList(modelListToShow.get(position));

                    }
                })
        );

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
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

    private void openCityStoreList(CityModel model) {

        Bundle bundle = new Bundle();
        bundle.putString(CITY_CODE, model.getId());
        bundle.putString(CITY_NAME, model.getPersianName());

        CatListFragment fragment = new CatListFragment();
        fragment.setArguments(bundle);

        callBack.openNewContentFragment(fragment);


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
}
