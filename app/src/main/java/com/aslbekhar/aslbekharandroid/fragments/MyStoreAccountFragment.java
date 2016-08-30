package com.aslbekhar.aslbekharandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.aslbekhar.aslbekharandroid.activities.MainActivity;
import com.aslbekhar.aslbekharandroid.activities.RegisterActivity;
import com.aslbekhar.aslbekharandroid.models.UserModel;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rey.material.widget.ProgressView;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_LOGO_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CHANGE_PASSWORD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CREATE_USER_OR_EDIT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.IS_LOGGED_IN;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.SUCCESS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.UPDATE_USER_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.USER_INFO;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setFontForActivity;
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
        TextView textView = (TextView) view.findViewById(R.id.brandName);
        textView.setText(model.getBuBrandName());

        textView = (TextView) view.findViewById(R.id.catName);
        textView.setText(model.getBuBrandCategory());

        textView = (TextView) view.findViewById(R.id.email);
        textView.setText("ایمیل: " + model.getBuEmail());

        textView = (TextView) view.findViewById(R.id.password);
        textView.setText("رمز ورود: •••••••••");

        view.findViewById(R.id.logOut).setOnClickListener(this);
        view.findViewById(R.id.changePassword).setOnClickListener(this);
        view.findViewById(R.id.editUser).setOnClickListener(this);

        textView = (TextView) view.findViewById(R.id.storeName);
        textView.setText("نام فروشگاه: " + model.getBuStoreName());


        textView = (TextView) view.findViewById(R.id.city);
        textView.setText("شهر: " + model.getBuCityNameFa());

        textView = (TextView) view.findViewById(R.id.address);
        textView.setText("آدرس: " + model.getBuStoreAddress());

        textView = (TextView) view.findViewById(R.id.tell);
        textView.setText("تلفن: " + model.getBuTel());

        textView = (TextView) view.findViewById(R.id.distributor);
        textView.setText("پخش کننده: " + model.getBuDistributor());

        textView = (TextView) view.findViewById(R.id.workHour);
        textView.setText("ساعات کار: " + model.getBuStoreHours());


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

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/theme.ttf");
        setFontForActivity(view.findViewById(R.id.root), tf);

        view.findViewById(R.id.discountManagement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiscountManagement();
            }
        });

    }

    private void openDiscountManagement() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater(null);
        final View dialogView = inflater.inflate(R.layout.dialog_discount_management, null);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/theme.ttf");
        Snippets.setFontForActivity(dialogView, tf);
        ((TextInputLayout) view.findViewById(R.id.saleNoteInputLay)).setTypeface(tf);

        ((EditText) dialogView.findViewById(R.id.saleDay)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 &&  Integer.parseInt(charSequence.toString()) > 0
                        &&  Integer.parseInt(charSequence.toString()) < 31){
                    dialogView.findViewById(R.id.saleMonth).requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((EditText) dialogView.findViewById(R.id.saleMonth)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 &&  Integer.parseInt(charSequence.toString()) > 0
                        &&  Integer.parseInt(charSequence.toString()) < 13){
                    dialogView.findViewById(R.id.saleYear).requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((EditText) dialogView.findViewById(R.id.saleYear)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 &&  Integer.parseInt(charSequence.toString()) > 94
                        &&  Integer.parseInt(charSequence.toString()) < 99){
                    dialogView.findViewById(R.id.saleEndDay).requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((EditText) dialogView.findViewById(R.id.saleEndDay)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 &&  Integer.parseInt(charSequence.toString()) > 0
                        &&  Integer.parseInt(charSequence.toString()) < 31){
                    dialogView.findViewById(R.id.saleEndMonth).requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((EditText) dialogView.findViewById(R.id.saleEndMonth)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 &&  Integer.parseInt(charSequence.toString()) > 0
                        &&  Integer.parseInt(charSequence.toString()) < 13){
                    dialogView.findViewById(R.id.saleEndYear).requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((EditText) dialogView.findViewById(R.id.saleEndYear)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 &&  Integer.parseInt(charSequence.toString()) > 94
                        &&  Integer.parseInt(charSequence.toString()) < 99){

                    InputMethodManager imm =
                            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        dialogView.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0);
                showFade(dialogView.findViewById(R.id.listOverLay), true, 500);
                ((ProgressView) dialogView.findViewById(R.id.progressBar)).start();
//                NetworkRequests.postRequest(UPDATE_USER_URL, MyStoreAccountFragment.this,
//                        CHANGE_PASSWORD, JSON.toJSONString(model));
            }
        });

        dialogView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
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
    public void onResume() {
        super.onResume();
        if (!getSP(IS_LOGGED_IN).equals(FALSE) && !getSP(USER_INFO).equals(FALSE)) {
            model = JSON.parseObject(getSP(USER_INFO), UserModel.class);
            if (model != null) {
                showUserInfo(model);
            } else {
                if (!((MainActivity) getActivity()).loginShown) {
                    ((MainActivity) getActivity()).loginShown = true;
                    callBack.openNewContentFragment(new MyStoreLoginFragment(), 3);
                } else {
                    getActivity().onBackPressed();

                }
            }
        } else {
            if (!((MainActivity) getActivity()).loginShown) {
                ((MainActivity) getActivity()).loginShown = true;
                callBack.openNewContentFragment(new MyStoreLoginFragment(), 3);
            } else {

                ((MainActivity) getActivity()).loginShown = false;
                getActivity().onBackPressed();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
                showChangePasswordDialog();
                break;

            case R.id.editUser:
                openEditUserActivity();
                break;
        }
    }

    private void openEditUserActivity() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        intent.putExtra(CREATE_USER_OR_EDIT, false);
        intent.putExtra(USER_INFO, JSON.toJSONString(model));
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK){
            try {
                model = JSON.parseObject(data.getExtras().getString(USER_INFO, FALSE), UserModel.class);
                setSP(USER_INFO, data.getExtras().getString(USER_INFO, FALSE));
            } catch (Exception e) {
                e.printStackTrace();
            }
            showUserInfo(model);

        }
    }

    private void logOut() {
        setSP(USER_INFO, FALSE);
        setSP(IS_LOGGED_IN, FALSE);
        callBack.openNewContentFragment(new MyStoreLoginFragment());
    }

    private void showChangePasswordDialog() {
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
                } else if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(getContext(), R.string.confirm_password_wrong, Toast.LENGTH_LONG).show();
                    return;
                } else if (newPassword.getText().toString().length() < 5) {
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


        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onResponse(String response, String tag) {
        if (tag.equals(CHANGE_PASSWORD)) {
            if (response.toLowerCase().contains(SUCCESS.toLowerCase())) {
                setSP(USER_INFO, JSON.toJSONString(model));
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline(String tag) {

    }
}
