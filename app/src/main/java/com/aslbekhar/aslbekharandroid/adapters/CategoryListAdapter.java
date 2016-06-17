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
import com.aslbekhar.aslbekharandroid.models.CategoryModel;
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
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.GroupViewHolder> {

    List<CategoryModel> modelList;
    Context context;
    private Fragment fragment;
    private String cityCode;

    public CategoryListAdapter(List<CategoryModel> modelList,
                               Context context, Fragment fragment, String cityCode) {
        this.modelList = modelList;
        this.context = context;
        this.fragment = fragment;
        this.cityCode = cityCode;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cat, viewGroup, false);
        GroupViewHolder pvh = new GroupViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final GroupViewHolder holder, int position) {
        final CategoryModel model = modelList.get(position);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/theme.ttf");
        holder.title.setText(model.getTitle());
        holder.title.setTypeface(tf);
        final List<String> brandLogos = model.getImages();

        if (brandLogos.size() > 0) {

            Picasso.with(context)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(0) + ".png"))
                    .into(holder.image1, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(Constants.BRAND_LOGO_URL + brandLogos.get(0) + ".png")
                                    .into(holder.image1);
                        }
                    });
        }
        if (brandLogos.size() > 1) {

            Picasso.with(context)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(1) + ".png"))
                    .into(holder.image2, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(Constants.BRAND_LOGO_URL + brandLogos.get(1) + ".png")
                                    .into(holder.image2);
                        }
                    });
        }
        if (brandLogos.size() > 2) {

            Picasso.with(context)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(2) + ".png"))
                    .into(holder.image3, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(Constants.BRAND_LOGO_URL + brandLogos.get(2) + ".png")
                                    .into(holder.image3);
                        }
                    });
        }
        if (brandLogos.size() > 3) {

            Picasso.with(context)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(3) + ".png"))
                    .into(holder.image4, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(Constants.BRAND_LOGO_URL + brandLogos.get(3) + ".png")
                                    .into(holder.image4);
                        }
                    });
        }
        if (brandLogos.size() > 4) {

            Picasso.with(context)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(4) + ".png"))
                    .into(holder.image5, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(Constants.BRAND_LOGO_URL + brandLogos.get(4) + ".png")
                                    .into(holder.image5);
                        }
                    });
        }
        if (brandLogos.size() > 5) {

            Picasso.with(context)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(5) + ".png"))
                    .into(holder.image6, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(Constants.BRAND_LOGO_URL + brandLogos.get(5) + ".png")
                                    .into(holder.image6);
                        }
                    });
        }
        if (brandLogos.size() > 6) {

            Picasso.with(context)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(6) + ".png"))
                    .into(holder.image7, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(Constants.BRAND_LOGO_URL + brandLogos.get(6) + ".png")
                                    .into(holder.image7);
                        }
                    });
        }
        if (brandLogos.size() > 7) {

            Picasso.with(context)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(7) + ".png"))
                    .into(holder.image8, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(Constants.BRAND_LOGO_URL + brandLogos.get(7) + ".png")
                                    .into(holder.image8);
                        }
                    });
        }

        holder.brandCount.setText(String.valueOf(brandLogos.size()));


    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;
        ImageView image5;
        ImageView image6;
        ImageView image7;
        ImageView image8;
        TextView brandCount;

        GroupViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.itemCV);
            title = (TextView) itemView.findViewById(R.id.title);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            image4 = (ImageView) itemView.findViewById(R.id.image4);
            image5 = (ImageView) itemView.findViewById(R.id.image5);
            image6 = (ImageView) itemView.findViewById(R.id.image6);
            image7 = (ImageView) itemView.findViewById(R.id.image7);
            image8 = (ImageView) itemView.findViewById(R.id.image8);
            brandCount = (TextView) itemView.findViewById(R.id.brandCount);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}