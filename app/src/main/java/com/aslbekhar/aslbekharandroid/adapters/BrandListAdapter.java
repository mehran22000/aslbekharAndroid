package com.aslbekhar.aslbekharandroid.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.models.BrandModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Amin on 12/1/2015.
 * <p/>
 * this adapter will be used in the Main Activity Recycle grid list, and or any other grid views
 * for the GroupModel
 */
public class BrandListAdapter extends RecyclerView.Adapter<BrandListAdapter.GroupViewHolder> {

    List<BrandModel> modelList;
    Context context;
    private Fragment fragment;

    public BrandListAdapter(List<BrandModel> modelList, Context context, Fragment fragment) {
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
                .inflate(R.layout.item_brand, viewGroup, false);
        GroupViewHolder pvh = new GroupViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final GroupViewHolder holder, int position) {
        final BrandModel model = modelList.get(position);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/theme.ttf");
        holder.title.setText(model.getbName());
        holder.title.setTypeface(tf);


        Picasso.with(context)
                .load(Uri.parse("file:///android_asset/logos/" + model.getbLogo() + ".png"))
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(Constants.BRAND_LOGO_URL + model.getbLogo() + ".png")
                                .into(holder.image);
                    }
                });

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