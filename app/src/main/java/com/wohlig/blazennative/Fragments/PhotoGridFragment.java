package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wohlig.blazennative.Activities.MainActivity;
import com.wohlig.blazennative.Activities.ViewPagerSliderActivity;
import com.wohlig.blazennative.Adapters.ImageGridAdapter;
import com.wohlig.blazennative.HttpCall.HttpCall;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotoGridFragment extends Fragment {
    private View view;
    private Activity activity;
    private static String TAG = "BLAZEN";
    private GridView gvImages;
    private static int NUM_OF_COLUMNS = 3;
    private ImageGridAdapter imageGridAdapter;
    private int columnWidth;
    private ArrayList<String> imageLinks = new ArrayList<String>();
    private ProgressBar progressBar;
    private TextView tvNoImages;
    private boolean noImages = false;
    private static String albumId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_photo_grid, container, false);

        //((MainActivity) this.getActivity()).setToolbarText("IMAGE");

        albumId = ((MainActivity) this.getActivity()).getId();

        activity = getActivity();

        initilizeViews();

        return view;
    }

    private void initilizeViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tvNoImages = (TextView) view.findViewById(R.id.tvNoImages);

        gvImages = (GridView) view.findViewById(R.id.glImages);
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, r.getDisplayMetrics());

        columnWidth = (int) ((getScreenWidth() - ((NUM_OF_COLUMNS + 1) * padding)) / NUM_OF_COLUMNS);

        gvImages.setNumColumns(NUM_OF_COLUMNS);
        gvImages.setColumnWidth(columnWidth);
        gvImages.setStretchMode(GridView.NO_STRETCH);
        gvImages.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gvImages.setHorizontalSpacing((int) padding);
        gvImages.setVerticalSpacing((int) padding);

        setListeners();

        getContent();

    }

    private void setListeners() {
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(activity, ViewPagerSliderActivity.class);
                intent.putExtra("position", String.valueOf(position));
                intent.putExtra("imageLinks", imageLinks);
                startActivity(intent);
            }
        });

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
                JSONArray jsonArray = null;
                //String albumId = "7";

                try {
                    response = HttpCall.getDataGet(InternetOperations.SERVER_URL + "image/getAlbumImages?id=" + albumId);
                    if (!response.equals("")) {                 //check is the response empty
                        jsonArray = new JSONArray(response);

                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                String image = null, text = null;

                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                                image = jsonObject.optString("image");
                                text = jsonObject.optString("text");

                                populateImages(i, image, text);
                            }
                            done = true;

                        } else {
                            done = true;
                            noImages = true;
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

    private int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    private void refresh() {
        if (noImages) {
            tvNoImages.setVisibility(View.VISIBLE);
        } else {
            imageGridAdapter = new ImageGridAdapter(activity, imageLinks, columnWidth);
            gvImages.setAdapter(imageGridAdapter);
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
