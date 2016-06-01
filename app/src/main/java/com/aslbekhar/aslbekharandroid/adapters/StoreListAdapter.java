package com.aslbekhar.aslbekharandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.fragments.StoreListFragment;
import com.aslbekhar.aslbekharandroid.models.StoreModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;

import java.util.List;

/**
 * Created by Amin on 12/1/2015.
 * <p/>
 * this adapter will be used in the Main Activity Recycle grid list, and or any other grid views
 * for the GroupModel
 */
public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.GroupViewHolder> {

    List<StoreModel> modelList;
    Context context;
    private StoreListFragment fragment;
    private String cityCode;

    public StoreListAdapter(List<StoreModel> modelList,
                            Context context, StoreListFragment fragment, String cityCode) {
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
                .inflate(R.layout.item_store, viewGroup, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        final StoreModel model = modelList.get(position);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/theme.ttf");
        holder.title.setText(model.getsName());
        holder.title.setTypeface(tf);
        if (model.getsHour() != null && model.getsHour().length() > 1) {
            holder.workHour.setText("ساعات کار: " + model.getsHour());
            holder.workHour.setTypeface(tf);
        } else {
            holder.workHour.setVisibility(View.INVISIBLE);
        }
        if (model.getsTel1() != null && model.getsTel1().length() > 1) {
            holder.tell.setText("تلفن: " + Constants.persianNumbers(model.getsTel1()));
            holder.tell.setTypeface(tf);
        } else {
            holder.tell.setVisibility(View.INVISIBLE);
        }
        if (model.getsAddress() != null && model.getsAddress().length() > 1) {
            holder.address.setText("آدرس: " + model.getsAddress());
            holder.address.setTypeface(tf);
        } else {
            holder.address.setVisibility(View.INVISIBLE);
        }

        if (model.getsVerified().equals(Constants.YES)){
            if (model.getsDiscount() > 0){
                holder.image.setImageResource(R.drawable.discountverified);
            } else {
                holder.image.setImageResource(R.drawable.verified);
            }
        } else if (model.getsDiscount() > 0){
            holder.image.setImageResource(R.drawable.discount);
        }

        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ cityCode + model.getsTel1()));
                context.startActivity(intent);
            }
        });

        holder.showOnMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showOnMap(model);
            }
        });


    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView workHour;
        TextView tell;
        TextView address;
        ImageView image;
        View callBtn;
        View showOnMapBtn;

        GroupViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.itemCV);
            title = (TextView) itemView.findViewById(R.id.title);
            workHour = (TextView) itemView.findViewById(R.id.workHour);
            tell = (TextView) itemView.findViewById(R.id.tell);
            address = (TextView) itemView.findViewById(R.id.address);
            image = (ImageView) itemView.findViewById(R.id.image);
            callBtn = itemView.findViewById(R.id.callLay);
            showOnMapBtn = itemView.findViewById(R.id.showOnMapLay);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}