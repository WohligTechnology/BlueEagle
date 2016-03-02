package com.wohlig.blazennative;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.wohlig.blazennative.util.WebViewController;

public class WebActivity extends AppCompatActivity {

    WebView webview;
    String webLink;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = this.getIntent();
        webLink = intent.getStringExtra("webLink");
        initilizeViews();
    }

    public void initilizeViews(){

        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewController());
        webview.getSettings().setJavaScriptEnabled(true);
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
            super.onBackPressed();
        }
    }

}
