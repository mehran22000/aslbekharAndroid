package com.example.n55.jsonandvally;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Created by N55 on 7/15/2017.
 */
public class MySharedPreferences {

    private SharedPreferences prefs;

    private static final String API_KEY = "apikey";
    private static final String SECRET_KEY = "secret_key";
    private static final String Url = "url";


    public MySharedPreferences(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //apikey
    public String getStringapikey() {

        return prefs.getString(API_KEY , "");
    }

    public void setStringapikey( String value) {
        Editor editor = prefs.edit();
        editor.putString(API_KEY, value);
        editor.commit();
    }

    //secret key
    public String getStringsecretkey() {

        return prefs.getString(SECRET_KEY , "");
    }

    public void setStringsecretkey( String value) {
        Editor editor = prefs.edit();
        editor.putString(SECRET_KEY, value);
        editor.commit();
    }

    //apikey
    public String getStringurl() {

        return prefs.getString(Url , "");
    }

    public void setStringurl( String value) {
        Editor editor = prefs.edit();
        editor.putString(Url, value);
        editor.commit();
    }
}
