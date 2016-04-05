package com.wohlig.blazennative.Activities;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
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
    private int thumbnailTop;
    private int thumbnailLeft;
    private int thumbnailWidth;
    private int thumbnailHeight;
    private static final int ANIM_DURATION = 600;
    private ColorDrawable colorDrawable;
    private Bundle savedInstance;
    private RelativeLayout rlMain;
    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;
    private ImageView ivPhoto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedInstance = savedInstanceState;
        setContentView(R.layout.activity_view_pager);

        Intent intent = getIntent();

        position = Integer.parseInt(intent.getStringExtra("position"));
        imagePaths = intent.getStringArrayListExtra("imageLinks");

        Bundle bundle = getIntent().getExtras();
        thumbnailTop = bundle.getInt("top");
        thumbnailLeft = bundle.getInt("left");
        thumbnailWidth = bundle.getInt("width");
        thumbnailHeight = bundle.getInt("height");

        initilizeViews();
        setListeners();
    }

    private void initilizeViews() {

        rlMain = (RelativeLayout) findViewById(R.id.rlMain);

        colorDrawable = new ColorDrawable(Color.BLACK);

        rlMain.setBackground(colorDrawable);

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
                onBackPressed();
            }
        });
    }

    private RelativeLayout addNewView(int i, String imageLink, String imageDesc) {
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.layout_slider, null);
        final PhotoView photoView = (PhotoView) v.findViewById(R.id.photoView);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        if (!imageLink.equals("") || !imageLink.isEmpty())
            Picasso.with(this).load(imageLink).into(photoView);

        TextView textView = (TextView) v.findViewById(R.id.tvDesc);
        if (!imageDesc.equals("") || !imageDesc.isEmpty()) {
            textView.setText(imageDesc);
        } else {
            textView.setVisibility(View.GONE);
        }

        if (i == position) {

            if (savedInstance == null) {
                ViewTreeObserver observer = photoView.getViewTreeObserver();
                observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        photoView.getViewTreeObserver().removeOnPreDrawListener(this);

                        // Figure out where the thumbnail and full size versions are, relative
                        // to the screen and each other
                        int[] screenLocation = new int[2];
                        photoView.getLocationOnScreen(screenLocation);
                        mLeftDelta = thumbnailLeft - screenLocation[0];
                        mTopDelta = thumbnailTop - screenLocation[1];

                        // Scale factors to make the large version the same size as the thumbnail
                        mWidthScale = (float) thumbnailWidth / photoView.getWidth();
                        mHeightScale = (float) thumbnailHeight / photoView.getHeight();

                        enterAnimation(photoView);
                        setPhotoView(photoView);
                        return true;
                    }
                });
            }

        }

        return v;
    }

    private void setPhotoView(ImageView imageView){
        ivPhoto = imageView;
    }

    public void enterAnimation(ImageView imageView) {

        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        imageView.setScaleX(mWidthScale);
        imageView.setScaleY(mHeightScale);
        imageView.setTranslationX(mLeftDelta);
        imageView.setTranslationY(mTopDelta);

        // interpolator where the rate of change starts out quickly and then decelerates.
        TimeInterpolator sDecelerator = new DecelerateInterpolator();

        // Animate scale and translation to go from thumbnail to full size
        imageView.animate().setDuration(ANIM_DURATION).scaleX(1).scaleY(1).
                translationX(0).translationY(0).setInterpolator(sDecelerator);

        // Fade in the black background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0, 255);
        bgAnim.setDuration(ANIM_DURATION);
        bgAnim.start();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_change,R.anim.slide_up);
    }

}
