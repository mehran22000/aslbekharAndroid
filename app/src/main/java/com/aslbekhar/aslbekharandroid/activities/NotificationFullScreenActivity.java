package com.aslbekhar.aslbekharandroid.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.aslbekhar.aslbekharandroid.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEBUG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FULLSCREEN_ADD_IMAGE_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.IMAGE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOG_TAG;

/**
 * Created by Amin on 27/06/2016.
 */
public class NotificationFullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_full_screen);

        final String imageUrl = FULLSCREEN_ADD_IMAGE_URL + getIntent().getExtras().getString(IMAGE, FALSE);
        if (DEBUG) {
            Log.d(LOG_TAG, "Notification Add url = " + imageUrl);
        }
        Picasso.with(this)
                .load(imageUrl)
                .into((ImageView) findViewById(R.id.fullScreenAdvertise), new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        if (DEBUG) {
                            Log.d(LOG_TAG, "Notification Add url = " + imageUrl);
                        }
                        finish();
                    }
                });
    }
}
