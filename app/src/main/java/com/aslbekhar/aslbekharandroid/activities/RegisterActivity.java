package com.aslbekhar.aslbekharandroid.activities;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.adapters.CityListAdapter;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.aslbekhar.aslbekharandroid.models.UserModel;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRANSITION_DURATION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRUE;
import static com.aslbekhar.aslbekharandroid.utilities.StaticData.getCityModelList;

public class RegisterActivity extends AppCompatActivity {

    View emailLay;
    View cityLay;
    View brandLay;
    View nextBtn;
    boolean nextBtnEnable = false;
    int width;
    int statusStep = 1;
    UserModel registerUserModel;
    CityListAdapter adapter;
    List<CityModel> modelList = getCityModelList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        emailLay = findViewById(R.id.emailLay);
        cityLay = findViewById(R.id.cityLay);
        brandLay = findViewById(R.id.brandLay);

        nextBtn = findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        ((EditText) findViewById(R.id.email)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5 && ((TextView) findViewById(R.id.password)).getText().length() >5 && !nextBtnEnable){
                    enableNextBtn(true);
                } else {
                    if (nextBtnEnable) {
                        enableNextBtn(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.password)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5 && ((TextView) findViewById(R.id.email)).getText().length() >5 && !nextBtnEnable){
                    enableNextBtn(true);
                } else {
                    if (nextBtnEnable) {
                        enableNextBtn(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        emailLay.setAlpha(1);
        cityLay.setAlpha(0);
        brandLay.setAlpha(0);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/theme.ttf");
        ((EditText) findViewById(R.id.password)).setTypeface(tf);
        ((EditText) findViewById(R.id.email)).setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.emailInputLay)).setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.passwordInputLay)).setTypeface(tf);

        Snippets.setFontForActivity(findViewById(R.id.root), tf);
    }

    private void enableNextBtn(boolean enable) {

        if (enable) {
            nextBtnEnable = true;
            ViewAnimator.animate(nextBtn)
                    .backgroundColor(getResources().getColor(R.color.colorPrimary))
                    .textColor(getResources().getColor(R.color.white))
                    .duration(200)
                    .start();
        } else {
            nextBtnEnable = false;
            ViewAnimator.animate(nextBtn)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .textColor(getResources().getColor(R.color.gray))
                    .duration(200)
                    .start();
        }
    }

    private void nextButton() {

        if (!nextBtnEnable){
            return;
        }
        switch (statusStep){

            case 1:
                hideEmailShowCity(true);
                break;


            case 2:
                hideCityShowBrand();
                break;

        }
    }

    @Override
    public void onBackPressed() {

        switch (statusStep){
            case 1:
                super.onBackPressed();
                break;

            case 2:
                hideEmailShowCity(false);
                break;
        }
    }

    private void hideEmailShowCity(boolean shohOrHide) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailLay.getWindowToken(), 0);
        if (shohOrHide) {
            if (validateEmail().equals(TRUE)) {
                populateCityList();
                formNext(emailLay, cityLay);
                enableNextBtn(false);
                statusStep = 2;
            } else {
                showError(validateEmail());
            }
        } else {
            formBack(cityLay, emailLay, 1);
        }
    }

    private void populateCityList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cityRecyclerView);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);

        // setting the layout manager of recyclerView
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CityListAdapter(modelList, this, this);
        recyclerView.setAdapter(adapter);
    }

    private void hideCityShowBrand() {
        if (validateEmail().equals(TRUE)) {
            formNext(emailLay, cityLay);
            enableNextBtn(false);
            statusStep = 2;
        } else {
            showError(validateEmail());
        }
    }

    private void showError(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(errorMessage);
        builder.setPositiveButton(R.string.ok, null);
        builder.show();
    }

    private String validateEmail() {
        registerUserModel = new UserModel(((TextView) findViewById(R.id.email)).getText().toString(),
                ((TextView) findViewById(R.id.password)).getText().toString());

        if (registerUserModel.getBuEmail().length() < 5 || !registerUserModel.getBuEmail().contains("@")
                || !registerUserModel.getBuEmail().contains("."))
            return getString(R.string.incorrectEmail);


        if (registerUserModel.getBuPassword().length() < 6)
            return getString(R.string.password_too_short);


        return TRUE;
    }

    private void formNext(View hideView, View showView){

            ViewAnimator.animate(hideView)
                    .translationX(0, + width)
                    .scaleY(1f, 0.8f)
                    .duration(TRANSITION_DURATION)
                    .alpha(1,0)
                    .andAnimate(showView)
                    .translationX(-width, 0)
                    .alpha(0,1)
                    .start();

    }

    private void formBack(View hideView, View showView, final int nextStatus){

        ViewAnimator.animate(hideView)
                .translationX(0, - width)
                .duration(TRANSITION_DURATION)
                .alpha(1,0)
                .andAnimate(showView)
                .translationX(+width, 0)
                .scaleY(0.8f, 1f)
                .duration(TRANSITION_DURATION)
                .alpha(0,1)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        statusStep = nextStatus;
                        enableNextBtn(true);
                    }
                })
                .start();

    }

    public void onCityClicked(CityModel cityModel) {
        registerUserModel.setBuAreaCode(cityModel.getId());
        enableNextBtn(true);
    }
}
