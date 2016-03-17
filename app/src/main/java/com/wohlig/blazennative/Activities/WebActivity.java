package com.wohlig.blazennative.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.WebViewController;

public class WebActivity extends AppCompatActivity {
    private WebView webview;
    private String webLink;
    private ImageView ivBack;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = this.getIntent();
        webLink = intent.getStringExtra("webLink");
        initilizeViews();
    }

    public void initilizeViews() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewController() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                webview.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webview.loadUrl(webLink);

        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.destroy();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            webview.destroy();
            finish();
        }
    }

}
