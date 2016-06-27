package com.aslbekhar.aslbekharandroid.services;

import android.content.Intent;

import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Amin on 30/05/2016.
 *
 */


// public class GcmInstanceIDListenerService extends InstanceIDListenerService {
public class GcmInstanceIDListenerService extends FirebaseInstanceIdService {
    private static final String TAG = "MyInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     // [START refresh_token]
    @Override

    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, GcmRegistrationIntentService.class);
        startService(intent);
    }
    // [END refresh_token]
    **/


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        android.util.Log.d(TAG, "Refreshed token: " + refreshedToken);
        Snippets.setSP(Constants.DEVICE_ID, refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        Intent intent = new Intent(this, GcmRegistrationIntentService.class);
        startService(intent);
    }

}
