package com.aslbekhar.aslbekharandroid.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.models.UserModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rey.material.widget.ProgressView;

import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOGIN_URL;

/**
 * Created by Amin on 14/05/2016.
 * <p>
 * This class will be used for
 */
public class MyStoreLoginFragment extends android.support.v4.app.Fragment implements Interfaces.NetworkListeners {

    View view;
    Interfaces.MainActivityInterface callBack;
    UserModel model = null;

    public MyStoreLoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mystore_login, container, false);

        view.findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                login();
            }
        });


        return view;
    }

    private void login() {

        String postJson = "{\"email\":\"";
        EditText editText = (EditText) view.findViewById(R.id.email);
        postJson = postJson + editText.getText().toString() + "\",\"password\":\"";
        editText = (EditText) view.findViewById(R.id.password);
        postJson = postJson + editText.getText().toString() + "\"}";

        ((ProgressView) view.findViewById(R.id.signInBtnProgress)).start();
        NetworkRequests.postRequest(LOGIN_URL, this, "login", postJson);
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
    public void onResponse(String response, String tag) {
        ((ProgressView) view.findViewById(R.id.signInBtnProgress)).start();
        List<UserModel> modelList = null;
        try {
            modelList = JSON.parseArray(response, UserModel.class);
        } catch (Exception ignored) {

        }
        if (modelList != null && modelList.size() > 0 && modelList.get(0).getErr().equals("")) {
            model = modelList.get(0);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater(null);
            final View dialogView = inflater.inflate(R.layout.dialog_login_confirm, null);


            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/theme.ttf");
            ((TextView) dialogView.findViewById(R.id.title)).setText(model.getBuStoreName());
            ((TextView) dialogView.findViewById(R.id.title)).setTypeface(tf);
            if (model.getBuStoreHours() != null && model.getBuStoreHours().length() > 1) {
                ((TextView) dialogView.findViewById(R.id.workHour)).setText("ساعات کار: " + model.getBuStoreHours());
                ((TextView) dialogView.findViewById(R.id.workHour)).setTypeface(tf);
            } else {
                dialogView.findViewById(R.id.workHour).setVisibility(View.GONE);
            }
            if (model.getBuTel() != null && model.getBuTel().length() > 1) {
                ((TextView) dialogView.findViewById(R.id.tell)).setText("تلفن: " + Constants.persianNumbers(model.getBuTel()));
                ((TextView) dialogView.findViewById(R.id.tell)).setTypeface(tf);
            } else {
                dialogView.findViewById(R.id.tell).setVisibility(View.GONE);
            }
            if (model.getBuStoreAddress() != null && model.getBuStoreAddress().length() > 1) {
                ((TextView) dialogView.findViewById(R.id.address)).setText("آدرس: " + model.getBuStoreAddress());
                ((TextView) dialogView.findViewById(R.id.address)).setTypeface(tf);
            } else {
                dialogView.findViewById(R.id.address).setVisibility(View.GONE);
            }

            Glide.with(this)
                    .load(Uri.parse("file:///android_asset/logos/" + model.getBuBrandLogoName() + ".png"))
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri uriModel, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(MyStoreLoginFragment.this)
                                    .load(Constants.BRAND_LOGO_URL + model.getBuBrandLogoName() + ".png")
                                    .into((ImageView) dialogView.findViewById(R.id.brandLogo));
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into((ImageView) dialogView.findViewById(R.id.brandLogo));

            dialogBuilder.setView(dialogView);

            dialogBuilder.setPositiveButton(R.string.yes_its_me, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    callBack.openNewContentFragment(new MyStoreAccountFragment());
                }
            });
            dialogBuilder.setNegativeButton(R.string.no_its_not_me, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        } else if(modelList != null && modelList.size() > 0 && modelList.get(0).getErr().equals(Constants.PASSWORD_ERROR)){
            Toast.makeText(getActivity(), R.string.invalid_password, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {
        ((ProgressView) view.findViewById(R.id.signInBtnProgress)).stop();
        Snackbar snackbar = Snackbar.make(view.findViewById(R.id.rootLayout),
                getString(R.string.connection_error), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login();
                    }
                }).setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void onOffline() {
        ((ProgressView) view.findViewById(R.id.signInBtnProgress)).stop();
        Snackbar snackbar = Snackbar.make(view.findViewById(R.id.rootLayout),
                getString(R.string.you_are_offline), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login();
                    }
                }).setActionTextColor(Color.YELLOW);
        snackbar.show();
    }
}
