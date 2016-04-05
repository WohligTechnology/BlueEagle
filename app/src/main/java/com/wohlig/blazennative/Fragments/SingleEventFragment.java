package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
import com.wohlig.blazennative.Activities.PlayVideoActivity;
import com.wohlig.blazennative.Activities.SearchActivity;
import com.wohlig.blazennative.Activities.ViewPagerSliderActivity;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;
import com.wohlig.blazennative.Util.Size;
import com.wohlig.blazennative.Util.Youtube;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleEventFragment extends Fragment {
    private View view;
    private static final String MIME_TYPE = "text/html";
    private static final String ENCODING = "UTF-8";
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private ImageView ivBanner;
    private TextView tvTitleIn, tvDate, tvVenue, tvVideo, tvImage;
    private LinearLayout llDate, llVenue, llVideo, llAddVideo, llImage, llAddImage;
    private WebView webView;
    private String id;
    private String title, bannerUrl, date, venue, html, videos, images;
    private boolean imageList = false, videoList = false;
    private ArrayList<String> imageLinks = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_single_event, container, false);
        activity = getActivity();
        //id = ((MainActivity) this.getActivity()).getId();
        //((MainActivity) this.getActivity()).setToolbarText("EVENT");

        if(activity.getLocalClassName().equals("Activities.SearchActivity")) {
            ((SearchActivity) this.getActivity()).setToolbarText("EVENT");
            id = ((SearchActivity) this.getActivity()).getId();
        } else if (activity.getLocalClassName().equals("Activities.MainActivity")) {
            ((MainActivity) this.getActivity()).setToolbarText("EVENT");
            id = ((MainActivity) this.getActivity()).getId();
        }

        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ivBanner = (ImageView) view.findViewById(R.id.ivBanner);

        tvTitleIn = (TextView) view.findViewById(R.id.tvTitleIn);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvVenue = (TextView) view.findViewById(R.id.tvVenue);
        tvVideo = (TextView) view.findViewById(R.id.tvVideo);
        tvImage = (TextView) view.findViewById(R.id.tvImage);

        llDate = (LinearLayout) view.findViewById(R.id.llDate);
        llVenue = (LinearLayout) view.findViewById(R.id.llVenue);
        llVideo = (LinearLayout) view.findViewById(R.id.llVideo);
        llAddVideo = (LinearLayout) view.findViewById(R.id.llAddVideo);
        llImage = (LinearLayout) view.findViewById(R.id.llImage);
        llAddImage = (LinearLayout) view.findViewById(R.id.llAddImage);

        webView = (WebView) view.findViewById(R.id.webview);

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
        }, InternetOperations.SERVER_URL + "event/get?id=" + id);

    }

    private void json(String response) {
        JSONObject jsonObject = null;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonObject = new JSONObject(response);

                title = jsonObject.optString("title");
                bannerUrl = jsonObject.optString("banner");
                date = jsonObject.optString("time");
                venue = jsonObject.optString("venue");
                html = jsonObject.optString("content");

                videos = jsonObject.optString("videos");
                images = jsonObject.optString("images");

                llAddVideo.removeAllViews();
                if (!videos.equals("") || !videos.isEmpty()) {
                    JSONArray jsonArray = new JSONArray(videos);
                    if (jsonArray.length() > 0) {
                        videoList = true;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject video = jsonArray.getJSONObject(i);
                            String id = video.optString("id");
                            addVideo(id);
                        }
                    } else {
                        videoList = false;
                    }
                } else {
                    videoList = false;
                }

                llAddImage.removeAllViews();
                imageLinks.clear();

                if (!images.equals("") || !images.isEmpty()) {
                    JSONArray jsonArray = new JSONArray(images);
                    if (jsonArray.length() > 0) {
                        imageList = true;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject image = jsonArray.getJSONObject(i);
                            String thumbnailUrl = image.optString("thumbnail");
                            String text = image.optString("text");
                            populateImages(i,thumbnailUrl,text);
                            addImage(i, thumbnailUrl);
                        }
                    }
                    else {
                        imageList = false;
                    }
                } else {
                    imageList = false;
                }

                resetViews();
            }
        } catch (JSONException je) {
            Log.e(TAG, Log.getStackTraceString(je));
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void resetViews() {

        //banner
        if (!bannerUrl.equals("") || !bannerUrl.isEmpty()) {
            Picasso.with(activity).load(bannerUrl).into(ivBanner);
            ivBanner.setVisibility(View.VISIBLE);
        } else {
            ivBanner.setVisibility(View.GONE);
        }

        //title
        if (!title.equals("") || !title.isEmpty()) {
            tvTitleIn.setText(title);
            tvTitleIn.setVisibility(View.VISIBLE);
        } else {
            tvTitleIn.setVisibility(View.GONE);
        }

        //date
        if (!date.equals("") || !date.isEmpty()) {
            tvDate.setText(date);
            llDate.setVisibility(View.VISIBLE);
        } else {
            llDate.setVisibility(View.GONE);
        }

        //venue
        if (!venue.equals("") || !venue.isEmpty()) {
            tvVenue.setText(venue);
            llVenue.setVisibility(View.VISIBLE);
        } else {
            llVenue.setVisibility(View.GONE);
        }

        //html
        if (!html.equals("") || !html.isEmpty()) {
            webView.loadDataWithBaseURL("", html, MIME_TYPE, ENCODING, "");
            webView.setVisibility(View.VISIBLE);
        } else {
            webView.setVisibility(View.GONE);
        }

        //video
        if(videoList){
            llVideo.setVisibility(View.VISIBLE);
        } else {
            llVideo.setVisibility(View.GONE);
        }

        //image
        if(imageList){
            llImage.setVisibility(View.VISIBLE);
        } else {
            llImage.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.GONE);
    }

    private void addVideo(final String youtubeId) {
        LayoutInflater inflator = activity.getLayoutInflater();
        View viewPointsRow = inflator.inflate(R.layout.layout_event_single_video_or_image, null, false);

        ImageView ivThumbnail = (ImageView) viewPointsRow.findViewById(R.id.ivThumbnail);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Size.dpToPx(activity,130), LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, Size.dpToPx(activity,10), 0);
        ivThumbnail.setLayoutParams(lp);

        if (!youtubeId.isEmpty() || !youtubeId.equals("")) {
            Picasso.with(activity)
                    .load(Youtube.getThumbnail(youtubeId))
                    .into(ivThumbnail);

            ivThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PlayVideoActivity.class);
                    intent.putExtra("webLink", Youtube.getUrl(youtubeId));
                    activity.startActivity(intent);
                }
            });
            llAddVideo.addView(ivThumbnail);
        }
    }

    private void addImage(final int position, String imageUrl){
        LayoutInflater inflator = activity.getLayoutInflater();
        View viewPointsRow = inflator.inflate(R.layout.layout_event_single_video_or_image, null, false);

        ImageView ivThumbnail = (ImageView) viewPointsRow.findViewById(R.id.ivThumbnail);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Size.dpToPx(activity,130), LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, Size.dpToPx(activity,10), 0);
        ivThumbnail.setLayoutParams(lp);

        if (!imageUrl.isEmpty() || !imageUrl.equals("")) {
            Picasso.with(activity)
                    .load(imageUrl)
                    .into(ivThumbnail);

            ivThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ViewPagerSliderActivity.class);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("imageLinks", imageLinks);
                    startActivity(intent);
                }
            });

            llAddImage.addView(ivThumbnail);
        }
    }

    private void populateImages(int position, String image, String text) {
        if (image.equals("") || image.isEmpty())
            image = "null";

        if (text.equals("") || text.isEmpty())
            text = "null";

        imageLinks.add(image + "!!!" + text);
    }
}
