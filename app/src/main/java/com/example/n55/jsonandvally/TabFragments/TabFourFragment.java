package com.example.n55.jsonandvally.TabFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.n55.jsonandvally.ItemDecorationAlbumColumns;
import com.example.n55.jsonandvally.R;
import com.example.n55.jsonandvally.adater.RecyclerAdapter;
import com.example.n55.jsonandvally.model.Kala;

import java.util.List;
import java.util.Locale;

public class TabFourFragment extends Fragment {

    private RecyclerView recyclerview;
    private RecyclerAdapter adapter;
    private GridLayoutManager lLayout;
    private List<Kala> kalaList;

    public TabFourFragment()
    {

    }
    public TabFourFragment(List<Kala> data)
    {
        this.kalaList = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_four_fragment, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview4);
        //pari
        lLayout = new GridLayoutManager(getActivity(), 2);
        recyclerview.setLayoutManager(lLayout);
        recyclerview.addItemDecoration(new ItemDecorationAlbumColumns(
                getResources().getDimensionPixelSize(R.dimen.photos_list_spacing),
                getResources().getInteger(R.integer.photo_list_preview_columns)));

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //recyclerview.setLayoutManager(layoutManager);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        String[] locales = Locale.getISOCountries();


        adapter = new RecyclerAdapter(getActivity(),kalaList);
        recyclerview.setAdapter(adapter);
        view.setRotationY(180);
    }

}

