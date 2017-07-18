package com.example.n55.jsonandvally;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class Snippets {

    public static void setFontForActivity(View view) {

        Typeface tf = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/theme.ttf");
        Typeface tfb = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/themebold.ttf");
        Typeface tfl = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/theme_light.ttf");
        //Set up touch listener for non-text box views to hide keyboard.
        setFontRecursive(view, tf, tfb, tfl);

    }

    private static void setFontRecursive(View view, Typeface tf, Typeface tfb, Typeface tfl) {
        if (view instanceof TextView || view instanceof EditText) {
            if (view.getTag() != null && view.getTag().equals("bold")) {
                ((TextView) view).setTypeface(tfb);
            } else {
                if (view.getTag() != null && view.getTag().equals("light")) {
                    ((TextView) view).setTypeface(tfl);
                } else {
                    ((TextView) view).setTypeface(tf);
                }
            }
        } else {
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                    View innerView = ((ViewGroup) view).getChildAt(i);

                    setFontRecursive(innerView, tf, tfb, tfl);
                }
            }
        }
    }

}
