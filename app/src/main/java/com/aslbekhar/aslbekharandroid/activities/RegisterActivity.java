package com.aslbekhar.aslbekharandroid.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;

import com.aslbekhar.aslbekharandroid.R;
import com.github.florent37.viewanimator.ViewAnimator;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRANSITION_DURATION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRUE;

public class RegisterActivity extends AppCompatActivity {

    View emailLay;
    View cityLay;
    View brandLay;
    int width;
    int statusStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        emailLay = findViewById(R.id.emailLay);
        cityLay = findViewById(R.id.cityLay);
        brandLay = findViewById(R.id.brandLay);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });
    }

    private void nextButton() {
        switch (statusStep){

            case 1:
                hideEmailShowCity();
                break;


            case 2:
                hideEmailShowCity();
                break;

        }
    }

    private void hideEmailShowCity() {
        if (validateEmail().equals(TRUE)) {
            formNext(emailLay, cityLay);
            statusStep = 2;
        } else {
            showError(validateEmail());
        }
    }

    private void hideCityShowBrand() {
        if (validateEmail().equals(TRUE)) {
            formNext(emailLay, cityLay);
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
        return TRUE;
    }

    private void formNext(View hideView, View showView){

        ViewAnimator.animate(hideView)
                .translationY(0, + width)
                .scaleY(1f, 0.8f)
                .duration(TRANSITION_DURATION)
                .alpha(1,0)
                .andAnimate(showView)
                .translationY(-width, 0)
                .duration(TRANSITION_DURATION)
                .alpha(0,1)
                .start();

    }

    private void formBack(View hideView, View showView){

        ViewAnimator.animate(hideView)
                .translationY(0, - width)
                .duration(TRANSITION_DURATION)
                .alpha(1,0)
                .andAnimate(showView)
                .translationY(+width, 0)
                .scaleY(0.8f, 1f)
                .duration(TRANSITION_DURATION)
                .alpha(0,1)
                .start();

    }
}
