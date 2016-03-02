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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.wohlig.blazennative.HttpCall.HttpCall;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.util.InternetOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jay on 23-02-2016.
 */
public class HomeFragment extends Fragment {
    private View view;
    private static final String MIME_TYPE = "text/html";
    private static final String ENCODING = "UTF-8";
    private SliderLayout slider;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private WebView webView;
    private String html;
    private ArrayList<String> sliderImageList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();

        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        webView = (WebView) view.findViewById(R.id.webview);
        slider = (SliderLayout) view.findViewById(R.id.slider);

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

                try {
                    response = HttpCall.getDataGet(InternetOperations.SERVER_URL + "home/get");

                    if (!response.equals("")) {                 //check is the response empty
                        jsonObject = new JSONObject(response);

                        html = jsonObject.optString("content");

                        String slider = jsonObject.optString("slider");

                        if (!slider.isEmpty()) {                //check if slider is empty
                            try {
                                JSONArray sliderArray = new JSONArray(slider);

                                if (sliderArray.length() > 0) {     //slider field there in json but no images inside sliderArray
                                    for (int i = 0; i < sliderArray.length(); i++) {
                                        sliderImageList.add(sliderArray.get(i).toString());
                                    }
                                }

                            } catch (JSONException je) {
                                Log.e(TAG, Log.getStackTraceString(je));
                            }
                            done = true;

                        } else {
                            done = true;
                        }
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

    private void addSliderImage(String imageLink) {

        TextSliderView textSliderView = new TextSliderView(activity);
        textSliderView
                //.description("Slider")
                .image(imageLink);

        slider.addSlider(textSliderView);

    }

    private void refresh() {

        if (!html.equals("") || !html.isEmpty()) {
            webView.loadDataWithBaseURL("", html, MIME_TYPE, ENCODING, "");
            webView.setVisibility(View.VISIBLE);
        }

        if (!sliderImageList.isEmpty()) {
            for (int i = 0; i < sliderImageList.size(); i++) {
                addSliderImage(sliderImageList.get(i));
            }
            slider.setVisibility(View.VISIBLE);
        }
    }

}
