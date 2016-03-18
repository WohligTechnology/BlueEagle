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
import com.wohlig.blazennative.Adapters.NotificationAdapter;
import com.wohlig.blazennative.POJOs.NotificationPojo;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private View view;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private RecyclerView rvNotificationList;
    private List<NotificationPojo> listNotification;
    private NotificationAdapter notificationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        activity = getActivity();

        ((MainActivity) this.getActivity()).setToolbarText("Notification");
        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        rvNotificationList = (RecyclerView) view.findViewById(R.id.rvNotificationList);

        listNotification = new ArrayList<NotificationPojo>();

        notificationAdapter = new NotificationAdapter(listNotification);
        rvNotificationList.setAdapter(notificationAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvNotificationList.setLayoutManager(llm);

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
        }, InternetOperations.SERVER_URL + "notification/getAll");

    }

    private void json(String response) {

        JSONArray jsonArray = null;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonArray = new JSONArray(response);

                listNotification.clear();
                if (jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        String id = jsonObject.optString("id");
                        String title = jsonObject.optString("title");
                        String text = jsonObject.optString("text");
                        String image = jsonObject.optString("image");
                        String link = jsonObject.optString("link");
                        String type = jsonObject.optString("type");

                        populateNotification(id, title, text, image, link, type);
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

    public void populateNotification(String id, String title,String text, String image, String link, String type) {
        NotificationPojo np = new NotificationPojo();
        np.setId(id);
        np.setTitle(title);
        np.setText(text);
        np.setImage(image);
        np.setLink(link);
        np.setType(type);
        np.setContext(activity);
        listNotification.add(np);
    }

    private void resetViews() {
        notificationAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

}
