package com.wohlig.blazennative.util;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Jay on 26-01-2016.
 */
public class WebViewController extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}