package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
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
import com.wohlig.blazennative.HttpCall.HttpCall;
import com.wohlig.blazennative.MainActivity;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.util.InternetOperations;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_article, container, false);
        activity = getActivity();

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

        new AsyncTask<Void, Void, String>() {
            boolean done = false;
            boolean noInternet = false;

            @Override
            protected String doInBackground(Void... params) {

                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                String response;
                JSONObject jsonObject = null;
                String id = "7";
                String url = InternetOperations.SERVER_URL + "article/get?id=" + id;

                try {
                    response = HttpCall.getDataGet(url);

                    if (!response.equals("")) {                 //check is the response empty
                        jsonObject = new JSONObject(response);

                        html = jsonObject.optString("content");
                        bannerUrl = jsonObject.optString("banner");
                        title = jsonObject.optString("title");

                        done = true;

                    } else {                                    //no internet and no cached copy also found in database
                        noInternet = true;
                    }
                } catch (JSONException je) {
                    Log.e(TAG, Log.getStackTraceString(je));
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                progressBar.setVisibility(View.GONE);
                if (done) {                         //everything went fine
                    refresh();
                } else if (noInternet) {            //if no internet and no cached copy found in database
                    Toast.makeText(activity, "No internet!", Toast.LENGTH_SHORT).show();
                } else {                            //some error
                    Toast.makeText(activity, "Oops, Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(null, null, null);
    }

    private void refresh() {

        if (!html.equals("") || !html.isEmpty()) {
            webView.loadDataWithBaseURL("", html, MIME_TYPE, ENCODING, "");
            webView.setVisibility(View.VISIBLE);
        }

        if (!bannerUrl.equals("") || !bannerUrl.isEmpty()) {
            Picasso.with(activity)
                    .load(bannerUrl)
                    //.placeholder(R.drawable.ic_placeholder) // optional
                    //.error(R.drawable.ic_error_fallback)         // optional
                    .into(ivBanner);
            ivBanner.setVisibility(View.VISIBLE);
        }

        if (!title.equals("") || !title.isEmpty()) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }

    }

}
