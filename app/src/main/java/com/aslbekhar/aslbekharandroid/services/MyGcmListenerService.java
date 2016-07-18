package com.aslbekhar.aslbekharandroid.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.activities.NotificationFullScreenActivity;
import com.aslbekhar.aslbekharandroid.activities.SplashScreen;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.NotificationUtils;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Amin on 17/07/2016.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        sendNotification((String) data.get("Type"), (String) data.get("AdUrl"), "", (String) data.get("AdUrl"));
    }


    private void sendNotification(String type, String title, String message, String url) {
        Intent intent;
        if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            intent = new Intent(this, SplashScreen.class);
            intent.putExtra(Constants.IS_FROM_NOTIFICATION, true);
        } else {
            intent = new Intent(this, NotificationFullScreenActivity.class);
        }
        intent.putExtra(Constants.IMAGE, url);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle(title)
                .setContentText("")
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://"
                        + getApplicationContext().getPackageName() + "/"
                        + R.raw.notification))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}