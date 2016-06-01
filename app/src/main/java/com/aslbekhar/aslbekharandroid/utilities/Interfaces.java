package com.aslbekhar.aslbekharandroid.utilities;

import android.support.v4.app.Fragment;

import com.android.volley.VolleyError;

/**
 * Created by Amin on 20/05/2016.
 * this the interface between fragments and main activity
 */
public class Interfaces {

    public interface MainActivityInterface {
        public void openNewContentFragment(Fragment targetFragment);
    }

    public interface OfflineInterface {
        public void offlineMode(boolean offline);
    }


    public interface NetworkListeners {
        public void onResponse(String response, String tag);
        public void onError(VolleyError error, String tag);
        public void onOffline();
    }

}
