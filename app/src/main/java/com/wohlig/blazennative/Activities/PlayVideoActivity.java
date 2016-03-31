package com.wohlig.blazennative.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.WebViewController;

public class PlayVideoActivity extends Activity {
    WebView webview;
    String webLink;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
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
                /*webview.destroy();
                finish();*/
                onBackPressed();
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
            overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
        }
    }
}
