package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wohlig.blazennative.R;

public class SingleBlogFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_single_blog, container, false);

        initilizeViews();

        return view;
    }

    private void initilizeViews(){
        /*TextView textView = (TextView) view.findViewById(R.id.tvTitleIn);
        textView.setTypeface(getLinearFont(getActivity()));
        textView.setTextColor(getResources().getColor(R.color.toolBarColor));
        textView.setText("home home");*/
    }

    private static Typeface getLinearFont(Activity activity){
        return Typeface.createFromAsset(activity.getAssets(), "fonts/Linearicons.ttf");
    }
}
