package com.aslbekhar.aslbekharandroid.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.models.BrandModel;
import com.aslbekhar.aslbekharandroid.models.StoreModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_ID;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_BANNER_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NUMBER;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_DETAILS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_DISCOUNT_PERCENTAGE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_DISTANCE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_SALE_END;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_SALE_START;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setFontForActivity;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.showSlideUp;

/**
 * Created by Amin on 19/05/2016.
 */
public class StoreFragment extends Fragment implements Interfaces.NetworkListeners {

    View view;
    Interfaces.MainActivityInterface callBack;
    Interfaces.OfflineInterface offlineCallBack;
    StoreModel model;
    String cityCode = "";
    String catName;
    String catNum;
    String brandName;
    String brandId;
    int discount = 0;
    boolean fragmentAlive;

    ImageView bannerAdImageView;

    public StoreFragment() {

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
        view = inflater.inflate(R.layout.fragment_store, container, false);


        cityCode = getArguments().getString(CITY_CODE);
        catName = getArguments().getString(CAT_NAME);
        catNum = getArguments().getString(CAT_NUMBER);
        brandName = getArguments().getString(BRAND_NAME);
        brandId = getArguments().getString(BRAND_ID);
        if (getArguments() != null && getArguments().getBoolean(OFFLINE_MODE, false)) {
            view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        }

        if (getArguments() != null && getArguments().getInt(STORE_DISCOUNT_PERCENTAGE, 0) > 0) {
            discount = getArguments().getInt(STORE_DISCOUNT_PERCENTAGE, 0);
        }

        bannerAdImageView = (ImageView) view.findViewById(R.id.bannerAdvertise);

        try {
            model = JSON.parseObject(getArguments().getString(STORE_DETAILS), StoreModel.class);
            showStoreInfo(model);
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/theme.ttf");
            setFontForActivity(view, tf);
            checkForBannerAdvertise();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    private void showStoreInfo(final StoreModel model) {

        final ImageView brandLogo;
        ImageView image;
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView workHour = (TextView) view.findViewById(R.id.workHour);
        TextView tell = (TextView) view.findViewById(R.id.tell);
        TextView tell2 = (TextView) view.findViewById(R.id.tell2);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView discountPercentage = (TextView) view.findViewById(R.id.discountPercentage);
        TextView saleStart = (TextView) view.findViewById(R.id.saleStart);
        TextView saleEnd = (TextView) view.findViewById(R.id.saleEnd);
        TextView distance = (TextView) view.findViewById(R.id.distance);
        image = (ImageView) view.findViewById(R.id.image);
        brandLogo = (ImageView) view.findViewById(R.id.brandLogo);

        title.setText(model.getsName());
        if (model.getsHour() != null && model.getsHour().length() > 1) {
            workHour.setText("ساعات کار: " + model.getsHour());
        }
        if (model.getsTel1() != null && model.getsTel1().length() > 1) {
            tell.setText("تلفن: " + Constants.persianNumbers(model.getsTel1()));
        } else {
            tell.setVisibility(View.INVISIBLE);
        }
        if (model.getsTel2() != null && model.getsTel2().length() > 1) {
            tell2.setText("تلفن: " + Constants.persianNumbers(model.getsTel2()));
        } else {
            tell2.setVisibility(View.INVISIBLE);
        }
        if (model.getsAddress() != null && model.getsAddress().length() > 1) {
            address.setText("آدرس: " + model.getsAddress());
        }
        if (discount > 0 && getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.getString(STORE_SALE_START, "").length() > 1) {
                saleStart.setText(getString(R.string.sale_start) + " " + bundle.getString(STORE_SALE_START, ""));
            }
            if (bundle.getString(STORE_SALE_END, "").length() > 1) {
                saleEnd.setText(getString(R.string.sale_end) + " " + bundle.getString(STORE_SALE_END, ""));
            }
            if (bundle.getString(STORE_DISTANCE, "").length() > 1) {
                distance.setText(getString(R.string.sale_end) + " " + bundle.getString(STORE_DISTANCE, ""));
            }
            discountPercentage.setText(String.valueOf(discount));
        } else {
            saleStart.setVisibility(View.INVISIBLE);
            saleEnd.setVisibility(View.INVISIBLE);
            distance.setVisibility(View.INVISIBLE);

            if (model.getsDiscount() > 0) {
                discountPercentage.setText(String.valueOf(model.getsDiscount()));
            } else {
                discountPercentage.setVisibility(View.GONE);
            }
        }

        if (model.getsVerified().equals(Constants.YES)) {
            if (model.getsDiscount() > 0) {
                image.setImageResource(R.drawable.discountverified);
            } else {
                image.setImageResource(R.drawable.verified);
            }
        } else if (model.getsDiscount() > 0 || discount > 0) {
            image.setImageResource(R.drawable.discount);
        }


        Picasso.with(getContext())
                .load(Uri.parse("file:///android_asset/logos/" + BrandModel.getBrandLogo(model.getbName()) + ".png"))
                .into(brandLogo, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getContext())
                                .load(Constants.BRAND_LOGO_URL + BrandModel.getBrandLogo(model.getbName()) + ".png")
                                .into(brandLogo);
                    }
                });
    }

    private void checkForBannerAdvertise() {

        String url = CAT_BANNER_AD + cityCode + ".cat" + catNum + "." + brandName + "." + model.getsId() + ".png";
        Picasso.with(getContext()).load(url).into(bannerAdImageView, new Callback() {
            @Override
            public void onSuccess() {
                showSlideUp(bannerAdImageView, true, getContext());
            }

            @Override
            public void onError() {
            }
        });
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
    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline(String tag) {
        view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        offlineCallBack.offlineMode(true);
    }
}
