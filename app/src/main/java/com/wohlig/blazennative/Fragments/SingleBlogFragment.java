package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wohlig.blazennative.ARC.Http.HttpCallback;
import com.wohlig.blazennative.ARC.Http.HttpInterface;
import com.wohlig.blazennative.Activities.MainActivity;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleBlogFragment extends Fragment {

    private View view;
    private static final String MIME_TYPE = "text/html";
    private static final String ENCODING = "UTF-8";
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private ImageView ivBanner;
    private TextView tvTitle, tvTime, tvTitleIn;
    private WebView webView;
    private String id;
    private LinearLayout llBanner;
    private String title, bannerUrl, time, html;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_single_blog, container, false);
        activity = getActivity();
        id = ((MainActivity) this.getActivity()).getId();

        ((MainActivity) this.getActivity()).setToolbarText("BLOG");
        ((MainActivity) this. getActivity()).lockNavigationSlide();
        //((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //((MainActivity) getActivity()).getActionBar().setHomeButtonEnabled(false); // disable the button
        //((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); // remove the left caret
        //((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false); // remove the icon

        initilizeViews();

        return view;
    }

    private void initilizeViews(){
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        ivBanner = (ImageView) view.findViewById(R.id.ivBanner);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        tvTitleIn = (TextView) view.findViewById(R.id.tvTitleIn);
        webView = (WebView) view.findViewById(R.id.webView);

        llBanner = (LinearLayout) view.findViewById(R.id.llBanner);
        getContent();
    }

    private void getContent() {

        HttpCallback.get(new HttpInterface() {
            @Override
            public void refreshView(String response) {
                progressBar.setVisibility(View.VISIBLE);
                json(response);
            }

            @Override
            public void noInternet() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(activity, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(activity, "Oops! Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }, InternetOperations.SERVER_URL + "blog/get?id=" + id);

    }


    private void json(String response) {
        JSONObject jsonObject = null;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonObject = new JSONObject(response);

                title = jsonObject.optString("title");
                time = jsonObject.optString("time");
                html = jsonObject.optString("content");
                bannerUrl = jsonObject.optString("banner");

                resetViews();
            }
        } catch (JSONException je) {
            Log.e(TAG, Log.getStackTraceString(je));
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }


    private void resetViews() {

        if (!bannerUrl.equals("") || !bannerUrl.isEmpty()) {
            Picasso.with(activity).load(bannerUrl).into(ivBanner);
            ivBanner.setVisibility(View.VISIBLE);
        } else {
            ivBanner.setVisibility(View.GONE);
        }

        if (!title.equals("") || !title.isEmpty()) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);

            llBanner.setVisibility(View.VISIBLE);
            tvTitleIn.setText(title);
            tvTitleIn.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
            tvTitleIn.setVisibility(View.GONE);
        }

        if (!time.equals("") || !time.isEmpty()) {
            llBanner.setVisibility(View.VISIBLE);
            tvTime.setText(time);
            tvTime.setVisibility(View.VISIBLE);
        } else {
            tvTime.setVisibility(View.GONE);
        }

        if ((!title.equals("") || !title.isEmpty()) && (!time.equals("") || !time.isEmpty())){
            llBanner.setVisibility(View.GONE);
        }

        if (!html.equals("") || !html.isEmpty()) {
            webView.loadDataWithBaseURL("", html, MIME_TYPE, ENCODING, "");
            webView.setVisibility(View.VISIBLE);
        } else {
            webView.setVisibility(View.GONE);
        }

        if(tvTitle.getVisibility() == View.GONE && tvTime.getVisibility() == View.GONE ){
            llBanner.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.GONE);
    }


}
