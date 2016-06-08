package com.aslbekhar.aslbekharandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.utilities.BackStackFragment;
import com.aslbekhar.aslbekharandroid.utilities.Constants;

/**
 * Created by Amin on 15/05/2016.
 * <p/>
 * This class will be used for
 */
public class HostFragment extends BackStackFragment {

    private Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_host, container, false);
        if (fragment != null) {
            replaceFragment(fragment, false);
        }
        return view;
    }

    public void replaceFragment(Fragment fragment, boolean addToBackstack) {
        if (addToBackstack) {
            getChildFragmentManager().beginTransaction().replace(R.id.hosted_fragment, fragment).addToBackStack(null).commit();
        } else {
            getChildFragmentManager().beginTransaction().replace(R.id.hosted_fragment, fragment).commit();
        }
    }

    public static HostFragment newInstance(Fragment fragment, boolean addToBackOrNot) {
        HostFragment hostFragment = new HostFragment();
        hostFragment.fragment = fragment;

        if (hostFragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.ADD_TO_BACK, addToBackOrNot);
            hostFragment.setArguments(bundle);
        } else {
            hostFragment.getArguments().putBoolean(Constants.ADD_TO_BACK, addToBackOrNot);
        }
        return hostFragment;
    }
}