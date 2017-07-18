package com.example.n55.jsonandvally;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MyApiClient {

    private static MyApiInterface myApiInterface = null;

    public static MyApiInterface getClient(Context context) {

        final  MySharedPreferences sharedpreapi = new MySharedPreferences(context);

        if (myApiInterface==null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://"+sharedpreapi.getStringurl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            myApiInterface = retrofit.create(MyApiInterface.class);
        }
        return myApiInterface;
    }
}
