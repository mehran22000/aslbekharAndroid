package com.aslbekhar.aslbekharandroid.utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by Amin on 15/05/2016.
 * <p/>
 * we needed this class in order to bring back compatibility to each tab
 * of our viewpager in main activity, kinda like instagram
 */
public class BackStackFragment extends Fragment {

    public static boolean handleBackPressed(FragmentManager fm)
    {
        if(fm.getFragments() != null){
            List<Fragment> fragments = fm.getFragments();
            for (int i = 0; i < fragments.size(); i++) {
                Fragment frag = fragments.get(i);
                if (frag != null && frag.isVisible() && frag instanceof BackStackFragment) {
                    if (frag.getArguments().getBoolean(Constants.ADD_TO_BACK)) {
                        if (((BackStackFragment) frag).onBackPressed()) {
                            return true;
                        }
                    } else {
                        i++;
                    }
                }
            }
        }
        return false;
    }

    protected boolean onBackPressed()
    {
        FragmentManager fm = getChildFragmentManager();
        if(handleBackPressed(fm)){
            return true;
        } else if(getUserVisibleHint() && fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
            return true;
        }
        return false;
    }
}
