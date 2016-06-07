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

import static com.android.volley.Request.Method.POST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEBUG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOG_TAG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.VOLLEY_TIME_OUT;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.isOnline;

/**
 * Created by Amin on 20/05/2016.
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



                if (!(response.contains("ا") || response.contains("ب") || response.contains("پ") ||
                        response.contains("ت") || response.contains("ث") || response.contains("ج")
                        || response.contains("چ") || response.contains("ح") || response.contains("خ")
                        || response.contains("د") || response.contains("ذ") || response.contains("ر")
                        || response.contains("ز") || response.contains("ژ") || response.contains("س")
                        || response.contains("ش") || response.contains("ص") || response.contains("ض")
                        || response.contains("ط") || response.contains("ظ") || response.contains("ع")
                        || response.contains("غ") || response.contains("ف") || response.contains("ق")
                        || response.contains("ک") || response.contains("گ") || response.contains("ل")
                        || response.contains("م") || response.contains("ن") || response.contains("و")
                        || response.contains("ه") || response.contains("ی"))) {
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
                params.put("token","emFuYmlsZGFyYW5naGVybWV6DQo=");
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

    public static void postRequest(final String url, final Interfaces.NetworkListeners listener,
                                   final String tag, final String postBody) {


        StringRequest strReq = new StringRequest(POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (!(response.contains("ا") || response.contains("ب") || response.contains("پ") ||
                        response.contains("ت") || response.contains("ث") || response.contains("ج")
                        || response.contains("چ") || response.contains("ح") || response.contains("خ")
                        || response.contains("د") || response.contains("ذ") || response.contains("ر")
                        || response.contains("ز") || response.contains("ژ") || response.contains("س")
                        || response.contains("ش") || response.contains("ص") || response.contains("ض")
                        || response.contains("ط") || response.contains("ظ") || response.contains("ع")
                        || response.contains("غ") || response.contains("ف") || response.contains("ق")
                        || response.contains("ک") || response.contains("گ") || response.contains("ل")
                        || response.contains("م") || response.contains("ن") || response.contains("و")
                        || response.contains("ه") || response.contains("ی"))) {
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

            }
        }) {
            public String getBodyContentType() {
                return "application/json; charset=" + getParamsEncoding();
            }

            public byte[] getBody() throws AuthFailureError {
                try {
                    return postBody.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected Map<String, String> getParams() throws AuthFailureError {
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
