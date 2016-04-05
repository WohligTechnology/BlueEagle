package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wohlig.blazennative.ARC.Http.HttpCallback;
import com.wohlig.blazennative.ARC.Http.HttpInterface;
import com.wohlig.blazennative.Activities.MainActivity;
import com.wohlig.blazennative.Activities.SearchActivity;
import com.wohlig.blazennative.Adapters.VideoListAdapter;
import com.wohlig.blazennative.POJOs.VideoListPojo;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoListFragment extends Fragment {
    private View view;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private RecyclerView rvVideoList;
    private List<VideoListPojo> listVideos;
    private VideoListAdapter videoListAdapter;
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video_list, container, false);

        //((MainActivity) this.getActivity()).setToolbarText("VIDEO");
        //id = ((MainActivity) this.getActivity()).getId();

        activity = getActivity();

        if(activity.getLocalClassName().equals("Activities.SearchActivity")) {
            ((SearchActivity) this.getActivity()).setToolbarText("VIDEO");
            id = ((SearchActivity) this.getActivity()).getId();
        } else if (activity.getLocalClassName().equals("Activities.MainActivity")) {
            ((MainActivity) this.getActivity()).setToolbarText("VIDEO");
            id = ((MainActivity) this.getActivity()).getId();
        }

        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        rvVideoList = (RecyclerView) view.findViewById(R.id.rvVideoList);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        listVideos = new ArrayList<VideoListPojo>();

        videoListAdapter = new VideoListAdapter(listVideos);
        rvVideoList.setAdapter(videoListAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvVideoList.setLayoutManager(llm);

        //rvVideoAlbum.addItemDecoration(new SpacesItemDecoration(Size.dpToPx(activity, 10)));

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
        }, InternetOperations.SERVER_URL + "video/getAlbumVideos?id=" + id);

    }


    private void json(String response) {

        JSONArray jsonArray;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonArray = new JSONArray(response);

                listVideos.clear();
                if (jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String videoId = null, link = null, title = null;

                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        videoId = jsonObject.optString("id");
                        title = jsonObject.optString("title");

                        populateVideoList(videoId, link, title);
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

    public void populateVideoList(String videoId, String link, String title) {

        VideoListPojo vap = new VideoListPojo();
        vap.setVideoId(videoId);
        vap.setTitle(title);
        vap.setContext(activity);
        listVideos.add(vap);
    }

    private void resetViews() {
        videoListAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

}
