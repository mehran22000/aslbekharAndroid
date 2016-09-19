package com.aslbekhar.aslbekharandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.aslbekhar.aslbekharandroid.activities.RegisterActivity;
import com.aslbekhar.aslbekharandroid.models.UserModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.rey.material.widget.ProgressView;

import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_LOGO_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CHANGE_PASSWORD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CREATE_USER_OR_EDIT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.EMAIL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.IS_LOGGED_IN;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOGIN_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.PASSWORD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.SUCCESS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRUE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.UPDATE_USER_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.USER_INFO;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getDisplayWidth;
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
    int width;
    boolean discountLayOrProfileLay = true;

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
        width = getDisplayWidth(getActivity());
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/theme.ttf");
        setFontForActivity(view.findViewById(R.id.root), tf);
        decideToShowLoginOrMyAccount();
        return view;
    }

    private void showMyAccountLay(int duration) {

        if (duration == 0) {
            view.findViewById(R.id.loginLay).setVisibility(View.GONE);
            view.findViewById(R.id.myStoreLay).setVisibility(View.VISIBLE);

            view.findViewById(R.id.myStoreLay).bringToFront();
        } else {
            changeView(view.findViewById(R.id.loginLay), view.findViewById(R.id.myStoreLay));
        }

        if (discountLayOrProfileLay) {
            switchToDiscountLay(0);
        } else {
            switchToProfileLay(0);
        }

        view.findViewById(R.id.salesTabLay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!discountLayOrProfileLay) {
                    switchToDiscountLay(400);
                }
            }
        });
        view.findViewById(R.id.profileTabLay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (discountLayOrProfileLay) {
                    switchToProfileLay(400);
                }
            }
        });

    }

    private void changeView(View hideView, final View showView) {
        ViewAnimator.animate(hideView)
                .alpha(1, 0)
                .scale(1f, 0.7f)
                .translationY(0, +100)
                .andAnimate(showView)
                .alpha(0, 1)
                .scale(0.7f, 1f)
                .translationY(+100, 0)
                .duration(500)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        showView.setVisibility(View.VISIBLE);
                        showView.bringToFront();
                    }
                })
                .start();
    }

    private void switchToProfileLay(int duration) {

        TextView textView = (TextView) view.findViewById(R.id.brandName);
        textView.setText(model.getBuBrandName());

        textView = (TextView) view.findViewById(R.id.catName);
        textView.setText(model.getBuBrandCategory());

        textView = (TextView) view.findViewById(R.id.email);
        textView.setText("ایمیل: " + model.getBuEmail());


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

        switchTab(view.findViewById(R.id.discountLay), view.findViewById(R.id.profileLay), duration, false);
    }

    private void switchToDiscountLay(int duration) {


        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/theme.ttf");
        ((TextInputLayout) view.findViewById(R.id.saleNoteInputLay)).setTypeface(tf);

        ((EditText) view.findViewById(R.id.saleDay)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 && Integer.parseInt(charSequence.toString()) > 0
                        && Integer.parseInt(charSequence.toString()) < 31) {
                    view.findViewById(R.id.saleMonth).requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((EditText) view.findViewById(R.id.saleMonth)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 && Integer.parseInt(charSequence.toString()) > 0
                        && Integer.parseInt(charSequence.toString()) < 13) {
                    view.findViewById(R.id.saleYear).requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((EditText) view.findViewById(R.id.saleYear)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 && Integer.parseInt(charSequence.toString()) > 94
                        && Integer.parseInt(charSequence.toString()) < 99) {
                    view.findViewById(R.id.saleEndDay).requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((EditText) view.findViewById(R.id.saleEndDay)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 && Integer.parseInt(charSequence.toString()) > 0
                        && Integer.parseInt(charSequence.toString()) < 31) {
                    view.findViewById(R.id.saleEndMonth).requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((EditText) view.findViewById(R.id.saleEndMonth)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 && Integer.parseInt(charSequence.toString()) > 0
                        && Integer.parseInt(charSequence.toString()) < 13) {
                    view.findViewById(R.id.saleEndYear).requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ((EditText) view.findViewById(R.id.saleEndYear)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 2 && Integer.parseInt(charSequence.toString()) > 94
                        && Integer.parseInt(charSequence.toString()) < 99) {

                    InputMethodManager imm =
                            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDiscount();
            }
        });

        switchTab(view.findViewById(R.id.profileLay), view.findViewById(R.id.discountLay), duration, true);
    }

    private void saveDiscount() {
        InputMethodManager imm =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        ((ProgressView) view.findViewById(R.id.saveProgress)).start();
    }

    private void switchTab(View hideView, View showView, int duration,
                           final boolean afterFinishedDiscountOrProfile) {

        InputMethodManager imm =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (!afterFinishedDiscountOrProfile) {
            ViewAnimator.animate(hideView)
                    .translationX(0, -width)
                    .scale(1f, 0.8f)
                    .duration(duration)
                    .alpha(1, 0)
                    .andAnimate(showView)
                    .translationX(+width, 0)
                    .scale(0.8f, 1f)
                    .alpha(0, 1)
                    .andAnimate(view.findViewById(R.id.profileImg1))
                    .alpha(0, 1)
                    .andAnimate(view.findViewById(R.id.profileTxt1))
                    .alpha(0, 1)
                    .andAnimate(view.findViewById(R.id.salesImg1))
                    .alpha(1, 0)
                    .andAnimate(view.findViewById(R.id.salesTxt1))
                    .alpha(1, 0)
                    .onStop(new AnimationListener.Stop() {
                        @Override
                        public void onStop() {
                            discountLayOrProfileLay = afterFinishedDiscountOrProfile;
                        }
                    })
                    .start();
        } else {
            ViewAnimator.animate(hideView)
                    .translationX(0, +width)
                    .scale(1f, 0.8f)
                    .duration(duration)
                    .alpha(1, 0)
                    .andAnimate(showView)
                    .translationX(-width, 0)
                    .scale(0.8f, 1f)
                    .alpha(0, 1)
                    .andAnimate(view.findViewById(R.id.profileImg1))
                    .alpha(1, 0)
                    .andAnimate(view.findViewById(R.id.profileTxt1))
                    .alpha(1, 0)
                    .andAnimate(view.findViewById(R.id.salesImg1))
                    .alpha(0, 1)
                    .andAnimate(view.findViewById(R.id.salesTxt1))
                    .alpha(0, 1)
                    .onStop(new AnimationListener.Stop() {
                        @Override
                        public void onStop() {
                            discountLayOrProfileLay = afterFinishedDiscountOrProfile;
                        }
                    })
                    .start();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (view != null) {
            decideToShowLoginOrMyAccount();
        }
    }

    private void decideToShowLoginOrMyAccount() {
        if (!getSP(IS_LOGGED_IN).equals(FALSE) && !getSP(USER_INFO).equals(FALSE)) {
            try {
                model = JSON.parseObject(getSP(USER_INFO), UserModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (model != null) {
                showMyAccountLay(0);
            } else {
                showLoginLay(0);
            }
        } else {
            showLoginLay(0);
        }
    }


    private void login() {

        String postJson = "{\"email\":\"";
        EditText editText = (EditText) view.findViewById(R.id.emailEt);
        postJson = postJson + editText.getText().toString() + "\",\"password\":\"";
        editText = (EditText) view.findViewById(R.id.password);
        postJson = postJson + editText.getText().toString() + "\"}";

        ((ProgressView) view.findViewById(R.id.signInBtnProgress)).start();
        NetworkRequests.postRequest(LOGIN_URL, this, "login", postJson);
    }

    private void showLoginLay(int duration) {
        if (duration == 0) {
            view.findViewById(R.id.myStoreLay).setVisibility(View.GONE);
            view.findViewById(R.id.loginLay).setVisibility(View.VISIBLE);
            view.findViewById(R.id.loginLay).bringToFront();
        } else {
            changeView(view.findViewById(R.id.myStoreLay), view.findViewById(R.id.loginLay));
        }
        view.findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                login();
            }
        });

        view.findViewById(R.id.signUpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra(CREATE_USER_OR_EDIT, true);
                startActivityForResult(intent, 101);
            }
        });


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/theme.ttf");
        ((EditText) view.findViewById(R.id.password)).setTypeface(tf);
        ((EditText) view.findViewById(R.id.emailEt)).setTypeface(tf);
        ((TextInputLayout) view.findViewById(R.id.emailInputLay)).setTypeface(tf);
        ((TextInputLayout) view.findViewById(R.id.passwordInputLay)).setTypeface(tf);
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
        InputMethodManager imm =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            try {
                model = JSON.parseObject(data.getExtras().getString(USER_INFO, FALSE), UserModel.class);
                setSP(USER_INFO, data.getExtras().getString(USER_INFO, FALSE));
            } catch (Exception e) {
                e.printStackTrace();
            }
            showMyAccountLay(0);
        } else {
            if (resultCode == Activity.RESULT_OK) {
                ((TextView) view.findViewById(R.id.emailEt)).setText(data.getExtras().getString(EMAIL, FALSE));
                ((TextView) view.findViewById(R.id.password)).setText(data.getExtras().getString(PASSWORD, FALSE));
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                login();
            }
        }
    }

    private void logOut() {
        setSP(USER_INFO, FALSE);
        setSP(IS_LOGGED_IN, FALSE);
        showLoginLay(400);
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
        } else if (tag.equals("login")) {
            onLoginResponse(response);
        }
    }

    private void onLoginResponse(String response) {
        ((ProgressView) view.findViewById(R.id.signInBtnProgress)).stop();
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
                            Glide.with(MyStoreAccountFragment.this)
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

                    Snippets.setSP(IS_LOGGED_IN, TRUE);
                    Snippets.setSP(USER_INFO, JSON.toJSONString(model));
                    showMyAccountLay(400);
                }
            });
            dialogBuilder.setNegativeButton(R.string.no_its_not_me, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            final AlertDialog dialog = dialogBuilder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            });

            dialog.show();
        } else if (modelList != null && modelList.size() > 0 && modelList.get(0).getErr().equals(Constants.PASSWORD_ERROR)) {
            Toast.makeText(getActivity(), R.string.invalid_password, Toast.LENGTH_LONG).show();
        } else {

        }
    }

    @Override
    public void onError(VolleyError error, String tag) {
        if (tag.equals("login")) {
            ((ProgressView) view.findViewById(R.id.signInBtnProgress)).stop();
            Snackbar snackbar = Snackbar.make(view.findViewById(R.id.root),
                    getString(R.string.connection_error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.try_again), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            login();
                        }
                    }).setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onOffline(String tag) {
        if (tag.equals("login")) {
            ((ProgressView) view.findViewById(R.id.signInBtnProgress)).stop();
            Snackbar snackbar = Snackbar.make(view.findViewById(R.id.root),
                    getString(R.string.connection_error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.try_again), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            login();
                        }
                    }).setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
