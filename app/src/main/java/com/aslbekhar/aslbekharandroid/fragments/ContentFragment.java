package com.aslbekhar.aslbekharandroid.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aslbekhar.aslbekharandroid.R;

import java.lang.reflect.Field;

/**
 * Created by Amin on 15/05/2016.
 * <p/>
 * This class will be used for
 */
public class ContentFragment extends Fragment {
    private String text;
    private int depth;
    private int fontSize;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("depth")) {
                this.depth = args.getInt("depth");
            }
            if (args.containsKey("fontSize")) {
                this.fontSize = args.getInt("fontSize");
            }
        } else {
            depth = 1;
            fontSize = 12;
        }
        text = "Depth: " + depth;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(this.text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ContentFragment newInstance(int depth, int fontSize) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt("depth", depth);
        args.putInt("fontSize", fontSize);
        contentFragment.setArguments(args);
        return contentFragment;
    }
}