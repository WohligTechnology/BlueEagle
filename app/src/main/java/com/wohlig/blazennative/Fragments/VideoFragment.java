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
import com.wohlig.blazennative.Adapters.VideoAlbumsAdapter;
import com.wohlig.blazennative.POJOs.VideoAlbumsPojo;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {
    private View view;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private RecyclerView rvVideoAlbum;
    private List<VideoAlbumsPojo> listAlbums;
    private VideoAlbumsAdapter videoAlbumsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);

        //((MainActivity) this.getActivity()).setToolbarText("VIDEO");
        activity = getActivity();

        if(activity.getLocalClassName().equals("Activities.SearchActivity")) {
            ((SearchActivity) this.getActivity()).setToolbarText("VIDEO");
        } else if (activity.getLocalClassName().equals("Activities.MainActivity")) {
            ((MainActivity) this.getActivity()).setToolbarText("VIDEO");
        }

        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        rvVideoAlbum = (RecyclerView) view.findViewById(R.id.rvVideoAlbum);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        listAlbums = new ArrayList<VideoAlbumsPojo>();

        videoAlbumsAdapter = new VideoAlbumsAdapter(listAlbums);
        rvVideoAlbum.setAdapter(videoAlbumsAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvVideoAlbum.setLayoutManager(llm);

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
        }, InternetOperations.SERVER_URL + "video/getAllAlbums");

    }

    private void json(String response) {
        JSONArray jsonArray;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonArray = new JSONArray(response);

                listAlbums.clear();
                if (jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String id = null, image = null, title = null, subTitle = null;

                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                        id = jsonObject.optString("id");
                        image = jsonObject.optString("image");
                        title = jsonObject.optString("title");
                        subTitle = jsonObject.optString("subtitle");

                        populateAlbums(id, image, title, subTitle);
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

    public void populateAlbums(String id, String image, String title, String subTitle) {

        VideoAlbumsPojo vap = new VideoAlbumsPojo();
        vap.setId(id);
        vap.setImageUrl(image);
        vap.setTitle(title);
        vap.setSubTitle(subTitle);
        vap.setContext(activity);
        listAlbums.add(vap);
    }

    private void resetViews() {
        videoAlbumsAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }
}