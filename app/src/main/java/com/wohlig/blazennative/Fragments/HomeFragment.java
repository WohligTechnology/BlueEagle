package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.wohlig.blazennative.ARC.Http.HttpCallback;
import com.wohlig.blazennative.ARC.Http.HttpInterface;
import com.wohlig.blazennative.Activities.MainActivity;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;

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
    private SliderLayout sliderView;
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

        ((MainActivity) this.getActivity()).setToolbarText("Home");
        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        webView = (WebView) view.findViewById(R.id.webview);
        sliderView = (SliderLayout) view.findViewById(R.id.slider);

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
        }, InternetOperations.SERVER_URL + "home/get");

    }

    private void json(String response) {

        JSONObject jsonObject = null;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonObject = new JSONObject(response);

                html = jsonObject.optString("content");

                String slider = jsonObject.optString("slider");
                sliderView.removeAllSliders();

                sliderImageList.clear();
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
                }

                resetViews();
            }
        } catch (JSONException je) {
            Log.e(TAG, Log.getStackTraceString(je));
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

    }

    private void addSliderImage(String imageLink) {

        DefaultSliderView textSliderView = new DefaultSliderView(activity);
        textSliderView
                //.description("Slider")
                .image(imageLink);
        textSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderView.addSlider(textSliderView);

    }

    private void resetViews() {

        if (!html.equals("") || !html.isEmpty()) {
            webView.loadDataWithBaseURL("", html, MIME_TYPE, ENCODING, "");
            webView.setVisibility(View.VISIBLE);
        } else {
            webView.setVisibility(View.GONE);
        }

        if (!sliderImageList.isEmpty()) {
            for (int i = 0; i < sliderImageList.size(); i++) {
                addSliderImage(sliderImageList.get(i));
            }
            sliderView.setVisibility(View.VISIBLE);
        } else {
            sliderView.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.GONE);
    }

}
