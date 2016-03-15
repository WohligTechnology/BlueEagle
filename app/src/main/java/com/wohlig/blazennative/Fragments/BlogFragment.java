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
import com.wohlig.blazennative.Adapters.BlogAdapter;
import com.wohlig.blazennative.POJOs.BlogPojo;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;
import com.wohlig.blazennative.Util.Size;
import com.wohlig.blazennative.Util.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends Fragment {
    private View view;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private RecyclerView rvBlog;
    private List<BlogPojo> listBlogs;
    private BlogAdapter blogAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blog, container, false);
        ((MainActivity) this.getActivity()).setToolbarText("BLOG");
        activity = getActivity();

        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        listBlogs = new ArrayList<BlogPojo>();

        blogAdapter = new BlogAdapter(listBlogs);
        rvBlog = (RecyclerView) view.findViewById(R.id.rvBlog);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvBlog.setLayoutManager(llm);
        rvBlog.setAdapter(blogAdapter);
        rvBlog.addItemDecoration(new SpacesItemDecoration(Size.dpToPx(activity, 10)));

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
        }, InternetOperations.SERVER_URL + "blog/getAll");

    }

    private void json(String response) {
        JSONArray jsonArray;

        try {
            if (!response.equals("")) {                 //check is the response empty

                if (!listBlogs.isEmpty()) {
                    listBlogs.clear();
                }

                jsonArray = new JSONArray(response);

                if (jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String id = null, title = null, time = null, desc = null, userIcon = null;

                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                        id = jsonObject.optString("id");
                        title = jsonObject.optString("title");
                        time = jsonObject.optString("time");
                        desc = jsonObject.optString("desc");
                        userIcon = jsonObject.optString("userIcon");

                        populateBlog(id, title, time, desc, userIcon);
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

    public void populateBlog(String id, String title, String time, String desc, String iconUrl) {

        BlogPojo bp = new BlogPojo();
        bp.setId(id);
        bp.setTitle(title);
        bp.setTime(time);
        bp.setDesc(desc);
        bp.setIconUrl(iconUrl);
        bp.setContext(activity);
        listBlogs.add(bp);
    }

    private void resetViews() {
        blogAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

}
