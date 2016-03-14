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
import com.wohlig.blazennative.Adapters.ImageAlbumsAdapter;
import com.wohlig.blazennative.POJOs.ImageAlbumsPojo;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;
import com.wohlig.blazennative.Util.Size;
import com.wohlig.blazennative.Util.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoFragment extends Fragment {
    private View view;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private RecyclerView rvImageAlbum;
    private List<ImageAlbumsPojo> listAlbums;
    private ImageAlbumsAdapter imageAlbumsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photo, container, false);

        ((MainActivity) this.getActivity()).setToolbarText("IMAGE");

        activity = getActivity();

        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        rvImageAlbum = (RecyclerView) view.findViewById(R.id.rvImageAlbum);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        listAlbums = new ArrayList<ImageAlbumsPojo>();

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvImageAlbum.setLayoutManager(llm);

        rvImageAlbum.addItemDecoration(new SpacesItemDecoration(Size.dpToPx(activity, 10)));

        imageAlbumsAdapter = new ImageAlbumsAdapter(listAlbums);
        rvImageAlbum.setAdapter(imageAlbumsAdapter);

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
        }, InternetOperations.SERVER_URL + "image/getAllAlbums");
    }

    private void json(String response) {

        JSONArray jsonArray;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonArray = new JSONArray(response);

                listAlbums.clear();
                if (jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String id = null, image = null, title = null;

                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                        id = jsonObject.optString("id");
                        image = jsonObject.optString("image");
                        title = jsonObject.optString("title");

                        populateAlbums(id, image, title);
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

    public void populateAlbums(String id, String image, String title) {

        ImageAlbumsPojo iap = new ImageAlbumsPojo();
        iap.setId(id);
        iap.setImageUrl(image);
        iap.setTitle(title);
        iap.setContext(activity);
        listAlbums.add(iap);
    }

    private void resetViews() {
        imageAlbumsAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

}
