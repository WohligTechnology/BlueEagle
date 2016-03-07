package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wohlig.blazennative.Adapters.ImageAlbumsAdapter;
import com.wohlig.blazennative.HttpCall.HttpCall;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photo, container, false);

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

        rvImageAlbum.addItemDecoration(new SpacesItemDecoration(Size.dpToPx(activity,10)));
        
        setListeners();
        getContent();
        
    }
    
    private void setListeners(){

        /*rvImageAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, v.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/
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
                //JSONObject jsonObject = null;

                JSONArray jsonArray = null;

                try {
                    response = HttpCall.getDataGet(InternetOperations.SERVER_URL + "image/getAllAlbums");
                    if (!response.equals("")) {                 //check is the response empty
                        jsonArray = new JSONArray(response);

                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                String id = null, image = null, title = null;

                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                                id = jsonObject.optString("id");
                                image = jsonObject.optString("image");
                                title = jsonObject.optString("title");

                                populateAlbums(id, image, title);
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

    public void populateAlbums(String id, String image, String title) {

        ImageAlbumsPojo iap = new ImageAlbumsPojo();
        iap.setId(id);
        iap.setImageUrl(image);
        iap.setTitle(title);
        iap.setContext(activity);
        listAlbums.add(iap);
    }

    private void refresh() {
        ImageAlbumsAdapter imageAlbumsAdapter = new ImageAlbumsAdapter(listAlbums);
        rvImageAlbum.setAdapter(imageAlbumsAdapter);
    }

}
