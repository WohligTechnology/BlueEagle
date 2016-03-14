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

public class ArticleFragment extends Fragment {

    private View view;
    private static final String MIME_TYPE = "text/html";
    private static final String ENCODING = "UTF-8";
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private WebView webView;
    private String html, bannerUrl, title;
    private ImageView ivBanner;
    private TextView tvTitle;
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_article, container, false);
        activity = getActivity();

        id = "7";
        ((MainActivity) this.getActivity()).setToolbarText("About");
        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        webView = (WebView) view.findViewById(R.id.webview);
        ivBanner = (ImageView) view.findViewById(R.id.ivBanner);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

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
        }, InternetOperations.SERVER_URL + "article/get?id=" + id);

    }

    private void json(String response){
        JSONObject jsonObject = null;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonObject = new JSONObject(response);

                html = jsonObject.optString("content");
                bannerUrl = jsonObject.optString("banner");
                title = jsonObject.optString("title");

                resetViews();
            }
        } catch (JSONException je) {
            Log.e(TAG, Log.getStackTraceString(je));
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }


    private void resetViews() {

        if (!html.equals("") || !html.isEmpty()) {
            webView.loadDataWithBaseURL("", html, MIME_TYPE, ENCODING, "");
            webView.setVisibility(View.VISIBLE);
        } else {
            webView.setVisibility(View.GONE);
        }

        if (!bannerUrl.equals("") || !bannerUrl.isEmpty()) {
            Picasso.with(activity)
                    .load(bannerUrl)
                    //.placeholder(R.drawable.ic_placeholder) // optional
                    //.error(R.drawable.ic_error_fallback)         // optional
                    .into(ivBanner);
            ivBanner.setVisibility(View.VISIBLE);
        } else {
            ivBanner.setVisibility(View.GONE);
        }

        if (!title.equals("") || !title.isEmpty()) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.GONE);

    }

}
