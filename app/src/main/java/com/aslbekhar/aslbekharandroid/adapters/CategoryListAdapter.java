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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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
            Glide.with(fragment)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(0) + ".png"))
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(fragment).load(Constants.BRAND_LOGO_URL + brandLogos.get(0) + ".png").into(holder.image1);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.image1);
        }
        if (brandLogos.size() > 1) {
            Glide.with(fragment)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(1) + ".png"))
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(fragment).load(Constants.BRAND_LOGO_URL + brandLogos.get(1) + ".png").into(holder.image2);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.image2);
        }
        if (brandLogos.size() > 2) {
            Glide.with(fragment)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(2) + ".png"))
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(fragment).load(Constants.BRAND_LOGO_URL + brandLogos.get(2) + ".png").into(holder.image3);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.image3);
        }
        if (brandLogos.size() > 3) {
            Glide.with(fragment)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(3) + ".png"))
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(fragment).load(Constants.BRAND_LOGO_URL + brandLogos.get(3) + ".png").into(holder.image4);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.image4);
        }
        if (brandLogos.size() > 4) {
            Glide.with(fragment)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(4) + ".png"))
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(fragment).load(Constants.BRAND_LOGO_URL + brandLogos.get(4) + ".png").into(holder.image5);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.image5);
        }
        if (brandLogos.size() > 5) {
            Glide.with(fragment)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(5) + ".png"))
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(fragment).load(Constants.BRAND_LOGO_URL + brandLogos.get(5) + ".png").into(holder.image6);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.image6);
        }
        if (brandLogos.size() > 6) {
            Glide.with(fragment)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(6) + ".png"))
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(fragment).load(Constants.BRAND_LOGO_URL + brandLogos.get(6) + ".png").into(holder.image7);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.image7);
        }
        if (brandLogos.size() > 7) {
            Glide.with(fragment)
                    .load(Uri.parse("file:///android_asset/logos/" + brandLogos.get(7) + ".png"))
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(fragment).load(Constants.BRAND_LOGO_URL + brandLogos.get(7) + ".png").into(holder.image8);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.image8);
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