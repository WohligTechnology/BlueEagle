package com.wohlig.blazennative.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wohlig.blazennative.Adapters.ViewPagerAdapter;
import com.wohlig.blazennative.CustomViews.Photoview.PhotoView;
import com.wohlig.blazennative.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewPagerSliderActivity extends Activity {
    private static String TAG = "BLAZEN";
    private ViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView ivBack;
    private static int position;
    private ArrayList<String> imagePaths;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        Intent intent = getIntent();

        position = Integer.parseInt(intent.getStringExtra("position"));
        imagePaths = intent.getStringArrayListExtra("imageLinks");

        initilizeViews();
        setListeners();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initilizeViews() {

        ivBack = (ImageView) findViewById(R.id.ivBack);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(viewPagerAdapter);

        for (int i = 0; i < imagePaths.size(); i++) {
            List<String> imageDetail = Arrays.asList(imagePaths.get(i).split("!!!"));

            String imageLink = imageDetail.get(0).trim();              //image url
            String imageDesc = imageDetail.get(1).trim();              //image desc

            if (imageLink.equals("null"))
                imageLink = "";

            if (imageDesc.equals("null"))
                imageDesc = "";

            viewPagerAdapter.addView(addNewView(i, imageLink, imageDesc));
        }

        viewPagerAdapter.notifyDataSetChanged();

        mViewPager.setCurrentItem(position);
    }

    private void setListeners() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private RelativeLayout addNewView(int i, String imageLink, String imageDesc) {
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.layout_slider, null);
        PhotoView photoView = (PhotoView) v.findViewById(R.id.photoView);

        if (!imageLink.equals("") || !imageLink.isEmpty())
            Picasso.with(this).load(imageLink).into(photoView);

        TextView textView = (TextView) v.findViewById(R.id.tvDesc);
        if (!imageDesc.equals("") || !imageDesc.isEmpty()) {
            textView.setText(imageDesc);
        }else {
            textView.setVisibility(View.GONE);
        }

        return v;
    }

}
