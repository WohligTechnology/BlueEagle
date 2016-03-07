package com.wohlig.blazennative.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.WebViewController;

public class SocialFragment extends Fragment {

    private WebView webview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        String webLink = "http://www.google.co.in/";
        webview = (WebView) view.findViewById(R.id.webview);

        webview.setWebViewClient(new WebViewController());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(webLink);

        return view;
    }
}
