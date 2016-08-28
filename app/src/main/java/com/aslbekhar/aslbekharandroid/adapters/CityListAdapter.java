package com.aslbekhar.aslbekharandroid.adapters;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.activities.RegisterActivity;
import com.aslbekhar.aslbekharandroid.fragments.CitiesFragment;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Amin on 12/1/2015.
 * <p/>
 * this adapter will be used in the Main Activity Recycle grid list, and or any other grid views
 * for the GroupModel
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.GroupViewHolder> {

    List<CityModel> modelList;
    Context context;
    private Fragment fragment;
    private RegisterActivity activity;
    private String selectedCity;
    View selectedItem;

    public CityListAdapter(List<CityModel> modelList,
                           Context context, Fragment fragment) {
        this.modelList = modelList;
        this.context = context;
        this.fragment = fragment;
    }

    public CityListAdapter(List<CityModel> modelList, Context context, RegisterActivity activity, String selectedCity) {
        this.modelList = modelList;
        this.context = context;
        this.activity = activity;
        this.selectedCity = selectedCity;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_city, viewGroup, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final GroupViewHolder holder, final int position) {
        final CityModel model = modelList.get(position);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/theme.ttf");
        holder.title.setText(model.getPersianName());
        holder.title.setTypeface(tf);

        int imageResourceId = 0;
        switch (model.getId()) {

            case "021":
                imageResourceId = R.drawable.city_tehran;
                break;

            case "031":
                imageResourceId = R.drawable.city_isfahan;
                break;

            case "041":
                imageResourceId = R.drawable.city_tabriz;
                break;

            case "051":
                imageResourceId = R.drawable.city_mashhad;
                break;

            case "071":
                imageResourceId = R.drawable.city_shiraz;
                break;

            case "076":
                imageResourceId = R.drawable.city_kish;
                break;
        }


        Display display;
        if (fragment != null) {
            display = fragment.getActivity().getWindowManager().getDefaultDisplay();
        } else {
            display = activity.getWindowManager().getDefaultDisplay();
        }
        Point size = new Point();
        display.getSize(size);
        int width = (size.x / 3) - Snippets.dpToPixels(context, 11);
        Picasso.with(context)
                .load(imageResourceId)
                .resize(width, 0)
                .into(holder.image);

        if (selectedCity != null && selectedCity.equals(model.getEnglishName())) {
            if (selectedItem != null && !selectedItem.getTag().equals(position)){
                selectedItem.setVisibility(View.GONE);
            }
            holder.selectedIcon.setVisibility(View.VISIBLE);
            selectedItem = holder.selectedIcon;
        } else {
            holder.selectedIcon.setVisibility(View.GONE);
        }

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment != null) {
                    ((CitiesFragment) fragment).openCatFromAdapter(model);
                } else {
                    if (selectedItem != null) {
                        selectedItem.setVisibility(View.GONE);
                    }
                    selectedItem = holder.selectedIcon;
                    selectedItem.setVisibility(View.VISIBLE);
                    selectedItem.setTag(position);
                    selectedCity = model.getEnglishName();
                    activity.onCityClicked(model);
                }
            }
        });


    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        View cv;
        TextView title;
        ImageView image;
        ImageView selectedIcon;

        GroupViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.itemCV);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.image);
            selectedIcon = (ImageView) itemView.findViewById(R.id.selectedIcon);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}