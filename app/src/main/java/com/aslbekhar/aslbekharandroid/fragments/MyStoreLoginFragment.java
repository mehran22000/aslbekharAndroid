package com.aslbekhar.aslbekharandroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOGIN_URL;

/**
 * Created by Amin on 14/05/2016.
 * <p/>
 * This class will be used for
 */
public class MyStoreLoginFragment extends android.support.v4.app.Fragment implements Interfaces.NetworkListeners {

    View view;
    Interfaces.MainActivityInterface callBack;

    public MyStoreLoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mystore_login, container, false);

        view.findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        return view;
    }

    private void login() {

        String postJson = "{\"email\":\"";
        EditText editText = (EditText) view.findViewById(R.id.email);
        postJson = postJson + editText.getText().toString() + "\",\"password\":\"";
        editText = (EditText) view.findViewById(R.id.password);
        postJson = postJson + editText.getText().toString() + "\"}";

        NetworkRequests.postRequest(LOGIN_URL, this, "login",  postJson);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (Interfaces.MainActivityInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onResponse(String response, String tag) {

    }

    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline() {

    }
}
