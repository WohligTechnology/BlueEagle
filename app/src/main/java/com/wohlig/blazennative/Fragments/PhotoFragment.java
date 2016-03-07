package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.wohlig.blazennative.R;

public class PhotoFragment extends Fragment {

    private View view;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private RecyclerView rvImageAlbum;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photo, container, false);

        activity = getActivity();

        initilizeViews();

        return view;
    }

    private void initilizeViews(){

        rvImageAlbum = (RecyclerView) view.findViewById(R.id.rvImageAlbum);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

    }


}
