package com.aslbekhar.aslbekharandroid.utilities;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEBUG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOG_TAG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.VOLLEY_TIME_OUT;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.isOnline;

/**
 * Created by Amin on 20/05/2016.
 *
 */
public class NetworkRequests {

    public static void getRequest(final String url, final Interfaces.NetworkListeners listener, final String tag) {
        if (DEBUG) {
            Log.d(LOG_TAG, " url = " + url);
        }

        // creating volley string request
        final StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                if (!(response.contains("ا") || response.contains("ب") ||response.contains("س") ||
                        response.contains("پ") ||response.contains("ی") ||response.contains("ن"))) {
                    try {
                        response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                if (DEBUG) {
                    Log.d(LOG_TAG, " response for url " + url + " ===== " + response);
                }
                listener.onResponse(response, tag);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // something went wrong
                listener.onError(error, tag);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("token", "emFuYmlsZGFyYW5naGVybWV6DQo=");
                //..add other headers
                return params;
            }
        };


        strReq.setRetryPolicy(new DefaultRetryPolicy(
                VOLLEY_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (isOnline(AppController.applicationContext)) {
            AppController.getInstance().addToRequestQueue(strReq, "request");
        } else {
            listener.onOffline();
        }

    }
}
