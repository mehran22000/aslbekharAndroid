package com.aslbekhar.aslbekharandroid.models;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEVICE_ID;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_LAT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_LONG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOG_TAG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.SEND_ANALYTICS_ADVERTISEMENT_LINK;

/**
 * Created by Amin on 30/06/2016.
 */
public class AnalyticsAdvertisementModel implements Interfaces.NetworkListeners {

    String name;
    String screen;
    String type;
    String device;
    String lat;
    String lon;

    public AnalyticsAdvertisementModel(String name, String screen, String type) {
        this.name = name;
        this.screen = screen;
        this.type = type;
        device = Snippets.getSP(DEVICE_ID);
        lat = Snippets.getSP(LAST_LAT);
        lon = Snippets.getSP(LAST_LONG);
    }

    public static void sendAdvertisementAnalytics(AnalyticsAdvertisementModel model){
        NetworkRequests.postRequest(SEND_ANALYTICS_ADVERTISEMENT_LINK,
                model, SEND_ANALYTICS_ADVERTISEMENT_LINK, JSON.toJSONString(model));
    }

    @Override
    public void onResponse(String response, String tag) {
        Log.d(LOG_TAG, "onResponse: ");
    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline(String tag) {

    }
}
