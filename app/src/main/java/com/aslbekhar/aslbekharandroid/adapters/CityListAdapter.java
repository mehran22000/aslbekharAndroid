package com.aslbekhar.aslbekharandroid.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.bumptech.glide.Glide;

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

    public CityListAdapter(List<CityModel> modelList,
                           Context context, Fragment fragment) {
        this.modelList = modelList;
        this.context = context;
        this.fragment = fragment;
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
    public void onBindViewHolder(GroupViewHolder holder, int position) {
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

        Glide.with(fragment).load(imageResourceId).into(holder.image);


    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        ImageView image;

        GroupViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.itemCV);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}