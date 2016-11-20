package com.aslbekhar.aslbekharandroid.utilities;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.AppCompatButton;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aslbekhar.aslbekharandroid.R;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.SP_FILE_NAME_BASE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRUE;

/**
 * Created by Amin on 3/29/2015.
 * this class is place to save all those methods that are
 * going to be used multiple time in application and in different activities
 */
public class Snippets {

    //md5 converter
    public static String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }

    /*
this function is used to download an file from URL
and save it to the base location of app, with a folder name taken from string.xml
*/
    public static void downloadFile(String URL, String name, Context c) {

        File direct = new File(Environment.getExternalStorageDirectory()
                + c.getResources().getString(R.string.app_name));
        //check if the folder already exists
        Boolean b = direct.exists();

        if (!b) {
            //if not, try to create it
            b = direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(URL);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true).setTitle(name + " " + URL)
                .setDescription(name)
                .setVisibleInDownloadsUi(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir("/" + c.getResources().getString(R.string.app_name),
                        name + URL)
                .allowScanningByMediaScanner();
        mgr.enqueue(request);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    public static void showSlideUp(final View v, boolean b, Context context) {
        if (b) {
            Animation animSlideIn;
            animSlideIn = AnimationUtils.loadAnimation(context,
                    R.anim.slide_in_animation_vertical);
            Animation animFadeIn;
            animFadeIn = AnimationUtils.loadAnimation(context,
                    R.anim.fade_in_fast);
            AnimationSet s = new AnimationSet(true);//false mean don't share interpolator
            s.addAnimation(animFadeIn);
            s.addAnimation(animSlideIn);
            s.setFillAfter(true);
            v.startAnimation(s);
            animFadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (v.getVisibility() != View.VISIBLE) {
                        v.setVisibility(View.VISIBLE);
                    }

                    v.setAlpha(1f);
                }


                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {
            Animation animSlideOut;
            animSlideOut = AnimationUtils.loadAnimation(context,
                    R.anim.slide_out_animation);
            v.startAnimation(animSlideOut);
            animSlideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
//                    v.setVisibility(View.GONE);
                    v.setAlpha(0);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

//    public static void showFlip(final View v, boolean b, Context context) {
//        if (b) {
//            Animation animSlideIn;
//            animSlideIn = AnimationUtils.loadAnimation(context,
//                    R.anim.from_middle_fade);
//            Animation animFadeIn;
//            animFadeIn = AnimationUtils.loadAnimation(context,
//                    R.anim.fade_in_fast);
//            v.bringToFront();
//            v.setVisibility(View.VISIBLE);
//            AnimationSet s = new AnimationSet(true);//false mean don't share interpolator
//            s.addAnimation(animSlideIn);
//            s.addAnimation(animFadeIn);
//            v.startAnimation(s);
//            animSlideIn.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                    v.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//                }
//            });
//        } else {
//            Animation animSlideOut;
//            animSlideOut = AnimationUtils.loadAnimation(context,
//                    R.anim.to_middle_fade);
//            Animation animFadeOut;
//            animFadeOut = AnimationUtils.loadAnimation(context,
//                    R.anim.fade_out_fast);
//            AnimationSet s = new AnimationSet(false);
//            s.addAnimation(animSlideOut);
//            s.addAnimation(animFadeOut);
//            v.startAnimation(s);
//            animSlideOut.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    v.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//                }
//            });
//        }
//    }

    public static void showFade(final View view, final boolean show, int duration) {

        AlphaAnimation alphaAnimation;
        if (show) {
            alphaAnimation = new AlphaAnimation(0, 1);
        } else {
            alphaAnimation = new AlphaAnimation(1, 0);
        }
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(false);
        if (show) {
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (view.getVisibility() != View.VISIBLE) {
                        view.setVisibility(View.VISIBLE);
                    }
                    view.setAlpha(1f);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setAlpha(0);
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        alphaAnimation.setDuration(duration);
        view.startAnimation(alphaAnimation);
    }

//    public static void showHideLoadMore(final View v, boolean b) {
//        ObjectAnimator iv1Anim;
//        if (b) {
//            iv1Anim = ObjectAnimator.ofFloat(v, "scaleX", 0f, 1f);
//            iv1Anim.setDuration(1000);
//            iv1Anim.setRepeatCount(ValueAnimator.INFINITE);
//            iv1Anim.setInterpolator(new AccelerateDecelerateInterpolator());
//            v.setVisibility(View.VISIBLE);
//            iv1Anim.start();
//            iv1Anim.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    v.bringToFront();
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            });
//        } else {
//            if (v.getAnimation() != null) {
//                v.getAnimation().cancel();
//            }
//            v.setVisibility(View.GONE);
//        }
//    }


    public static String getSP(String key) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        return sp.getString(key, FALSE);
    }

    public static boolean getSPboolean(String key) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        return sp.getString(key, FALSE).equals(TRUE);
    }

    public static void setSP(String key, String value) {
        SharedPreferences sp
                = AppController
                .applicationContext
                .getSharedPreferences(SP_FILE_NAME_BASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(key, value);
        spe.commit();
    }

    public static int dpToPixels(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int getDisplayWidth(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }


    public static void setupUI(final Activity activity, final View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (activity.getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    }
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(activity, innerView);
            }
        }
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void setFontForActivity(View view, Typeface tf) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (view instanceof TextView || view instanceof Button || view instanceof AppCompatButton) {
            ((TextView) view).setTypeface(tf);
        } else {
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                    View innerView = ((ViewGroup) view).getChildAt(i);

                    setFontForActivity(innerView, tf);
                }
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, (float) pixels, (float) pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
