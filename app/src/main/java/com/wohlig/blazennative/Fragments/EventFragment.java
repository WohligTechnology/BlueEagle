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
import com.wohlig.blazennative.Adapters.EventListAdapter;
import com.wohlig.blazennative.POJOs.EventListPojo;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment {
    private View view;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private RecyclerView rvEventList;
    private List<EventListPojo> listEventList;
    private EventListAdapter eventListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event, container, false);

        activity = getActivity();

        //((MainActivity) this.getActivity()).setToolbarText("EVENTS");
        if(activity.getLocalClassName().equals("Activities.SearchActivity")) {
            ((SearchActivity) this.getActivity()).setToolbarText("EVENTS");
        } else if (activity.getLocalClassName().equals("Activities.MainActivity")) {
            ((MainActivity) this.getActivity()).setToolbarText("EVENTS");
        }

        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        rvEventList = (RecyclerView) view.findViewById(R.id.rvEventList);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        listEventList = new ArrayList<EventListPojo>();

        eventListAdapter = new EventListAdapter(listEventList);
        rvEventList.setAdapter(eventListAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvEventList.setLayoutManager(llm);

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
        }, InternetOperations.SERVER_URL + "event/getAll");


    }

    private void json(String response) {

        JSONArray jsonArray;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonArray = new JSONArray(response);

                if (jsonArray.length() > 0) {
                    listEventList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        String id = null, imageUrl = null, title = null, date = null, time = null;

                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                        id = jsonObject.optString("id");
                        imageUrl = jsonObject.optString("image");
                        title = jsonObject.optString("title");
                        date = jsonObject.optString("date");
                        time = jsonObject.optString("time");

                        populateEvents(id, imageUrl, title, date, time);
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

    public void populateEvents(String id, String imageUrl, String title, String date, String time) {

        EventListPojo elp = new EventListPojo();
        elp.setId(id);
        elp.setImageUrl(imageUrl);
        elp.setTitle(title);
        elp.setDate(date);
        elp.setTime(time);
        elp.setContext(activity);
        listEventList.add(elp);
    }

    private void resetViews() {
        eventListAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }
}
