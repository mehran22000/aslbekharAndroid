package com.aslbekhar.aslbekharandroid.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.fragments.BrandVerificationFragment;
import com.aslbekhar.aslbekharandroid.models.BrandVerificationModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Amin on 12/1/2015.
 * <p/>
 * this adapter will be used in the Main Activity Recycle grid list, and or any other grid views
 * for the GroupModel
 */
public class BrandVerificationAdapter extends RecyclerView.Adapter<BrandVerificationAdapter.GroupViewHolder> {

    List<BrandVerificationModel> modelList;
    Context context;
    private BrandVerificationFragment fragment;

    public BrandVerificationAdapter(List<BrandVerificationModel> modelList,
                                    Context context, BrandVerificationFragment fragment) {
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
                .inflate(R.layout.item_brand_verification, viewGroup, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        final BrandVerificationModel model = modelList.get(position);
        final Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/theme.ttf");
        holder.title.setText(model.getTitle());
        holder.title.setTypeface(tf);
        if (model.getShortDesc() != null && model.getShortDesc().length() > 1) {
            holder.desc.setText(model.getShortDesc());
            holder.desc.setTypeface(tf);
        } else {
            holder.desc.setVisibility(View.INVISIBLE);
        }

        holder.readMore.setTypeface(tf);
        holder.readMore.setVisibility(View.VISIBLE);

        Glide.with(fragment)
                .load(Constants.BRAND_VERIFICATION_IMAGE + model.getSmallImage())
                .into(holder.image);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = fragment.getLayoutInflater(null);
                final View dialogView = inflater.inflate(R.layout.dialog_brand_verfication_item, null);

                Glide.with(fragment)
                        .load(Constants.BRAND_VERIFICATION_IMAGE + model.getLargeImage())
                        .into((ImageView) dialogView.findViewById(R.id.image));

                ((TextView) dialogView.findViewById(R.id.desc)).setText(model.getLongDesc());
                ((TextView) dialogView.findViewById(R.id.desc)).setTypeface(tf);

                ((TextView) dialogView.findViewById(R.id.title)).setText(model.getTitle());
                ((TextView) dialogView.findViewById(R.id.title)).setTypeface(tf);

                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();

                dialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            holder.cv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView desc;
        TextView readMore;
        ImageView image;

        GroupViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.itemCV);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
            readMore = (TextView) itemView.findViewById(R.id.readMore);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}