package com.aslbekhar.aslbekharandroid.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.models.ResponseModel;
import com.aslbekhar.aslbekharandroid.models.UserModel;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rey.material.widget.ProgressView;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_LOGO_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CHANGE_PASSWORD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.IS_LOGGED_IN;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.UPDATE_USER_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.USER_INFO;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.showFade;

/**
 * Created by Amin on 14/05/2016.
 * <p/>
 * This class will be used for
 */
public class MyStoreAccountFragment extends android.support.v4.app.Fragment implements Interfaces.NetworkListeners, View.OnClickListener {

    View view;
    Interfaces.MainActivityInterface callBack;
    UserModel model;
    AlertDialog dialog;

    public MyStoreAccountFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_store_account, container, false);

        return view;
    }

    private void showUserInfo(final UserModel model) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/theme.ttf");
        TextView textView = (TextView) view.findViewById(R.id.brandName);
        textView.setText(model.getBuBrandName());
        textView.setTypeface(tf);

        textView = (TextView) view.findViewById(R.id.catName);
        textView.setText(model.getBuBrandCategory());
        textView.setTypeface(tf);

        textView = (TextView) view.findViewById(R.id.email);
        textView.setText("ایمیل: " + model.getBuEmail());
        textView.setTypeface(tf);

        textView = (TextView) view.findViewById(R.id.password);
        textView.setText("رمز ورود: •••••••••");

        view.findViewById(R.id.logOut).setOnClickListener(this);

        textView = (TextView) view.findViewById(R.id.city);
        textView.setText("شهر: " + model.getBuCityNameFa());
        textView.setTypeface(tf);

        textView = (TextView) view.findViewById(R.id.address);
        textView.setText("آدرس: " + model.getBuStoreAddress());
        textView.setTypeface(tf);

        textView = (TextView) view.findViewById(R.id.tell);
        textView.setText("تلفن: " + model.getBuTel());
        textView.setTypeface(tf);

        textView = (TextView) view.findViewById(R.id.distributor);
        textView.setText("پخش کننده: " + model.getBuDistributor());
        textView.setTypeface(tf);

        textView = (TextView) view.findViewById(R.id.workHour);
        textView.setText("ساعات کار: " + model.getBuStoreHours());
        textView.setTypeface(tf);


        Glide.with(this)
                .load(Uri.parse("file:///android_asset/logos/" + model.getBuBrandLogoName() + ".png"))
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri uriModel, Target<GlideDrawable> target, boolean isFirstResource) {
                        Glide.with(MyStoreAccountFragment.this)
                                .load(BRAND_LOGO_URL + model.getBuBrandLogoName() + ".png")
                                .into((ImageView) view.findViewById(R.id.brandLogo));
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into((ImageView) view.findViewById(R.id.brandLogo));
    }

    @Override
    public void onResume() {
        super.onResume();


        if (!getSP(IS_LOGGED_IN).equals(FALSE) && !getSP(USER_INFO).equals(FALSE)) {
            model = JSON.parseObject(getSP(USER_INFO), UserModel.class);
            showUserInfo(model);
        } else {
            if (getSP(IS_LOGGED_IN).equals(FALSE)) {
                callBack.openNewContentFragment(new MyStoreLoginFragment());
            }
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logOut:
                logOut();
                break;

            case R.id.changePassword:
                changePassword();
                break;
        }
    }

    private void logOut() {
        setSP(USER_INFO, FALSE);
        setSP(IS_LOGGED_IN, FALSE);
        callBack.openNewContentFragment(new MyStoreLoginFragment());
    }

    private void changePassword() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater(null);
        final View dialogView = inflater.inflate(R.layout.dialog_change_password, null);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/theme.ttf");

        final EditText oldPassword = (EditText) dialogView.findViewById(R.id.oldPassword);
        oldPassword.setTypeface(tf);
        final EditText newPassword = (EditText) dialogView.findViewById(R.id.newPassword);
        newPassword.setTypeface(tf);
        final EditText confirmPassword = (EditText) dialogView.findViewById(R.id.confirmPassword);
        confirmPassword.setTypeface(tf);

        dialogView.findViewById(R.id.changePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.getBuPassword().equals(oldPassword.getText().toString())) {
                    Toast.makeText(getContext(), R.string.invalid_password, Toast.LENGTH_LONG).show();
                    return;
                } else if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                    Toast.makeText(getContext(), R.string.confirm_password_wrong, Toast.LENGTH_LONG).show();
                    return;
                } else if(newPassword.getText().toString().length() <5){
                    Toast.makeText(getContext(), R.string.password_too_short, Toast.LENGTH_LONG).show();
                    return;
                }
                showFade(dialogView.findViewById(R.id.listOverLay), true, 500);
                ((ProgressView) dialogView.findViewById(R.id.progressBar)).start();
                model.setBuPassword(newPassword.getText().toString());
                NetworkRequests.postRequest(UPDATE_USER_URL, MyStoreAccountFragment.this,
                        CHANGE_PASSWORD, JSON.toJSONString(model));
            }
        });

        dialogView.findViewById(R.id.changePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onResponse(String response, String tag) {
        if (tag.equals(CHANGE_PASSWORD)){
            ResponseModel responseModel = JSON.parseObject(response, ResponseModel.class);

        }
    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline() {

    }
}
