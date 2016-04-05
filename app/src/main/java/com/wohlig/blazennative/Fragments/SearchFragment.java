package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.view.IconicsImageView;
import com.wohlig.blazennative.ARC.Http.HttpCallback;
import com.wohlig.blazennative.ARC.Http.HttpInterface;
import com.wohlig.blazennative.Activities.SearchActivity;
import com.wohlig.blazennative.Adapters.SearchAdapter;
import com.wohlig.blazennative.POJOs.SearchPojo;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.InternetOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private static String TAG = "BLAZEN";
    private Activity activity;
    private View view;
    private EditText etSearch;
    private IconicsImageView ivCross, ivSearch;
    private ProgressBar progressBar;
    private RecyclerView rvSearchList;
    private List<SearchPojo> listSearch;
    private SearchAdapter searchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        activity = getActivity();

        //((SearchActivity) this.getActivity()).setToolbarText("Search");

        if(activity.getLocalClassName().equals("Activities.SearchActivity")) {
            ((SearchActivity) this.getActivity()).setToolbarText("Search");
        }/* else if (activity.getLocalClassName().equals("Activities.MainActivity")) {
            ((MainActivity) this.getActivity()).setToolbarText("IMAGE");
            albumId = ((MainActivity) this.getActivity()).getId();
        }*/

        initlizeViews();
        addListeners();

        return view;
    }

    private void initlizeViews() {

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        rvSearchList = (RecyclerView) view.findViewById(R.id.rvSearchList);

        listSearch = new ArrayList<SearchPojo>();

        searchAdapter = new SearchAdapter(listSearch);
        rvSearchList.setAdapter(searchAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearchList.setLayoutManager(llm);

        etSearch = (EditText) view.findViewById(R.id.etSearch);

        ivCross = (IconicsImageView) view.findViewById(R.id.ivCross);
        ivSearch = (IconicsImageView) view.findViewById(R.id.ivSearch);

        etSearch.requestFocus();
        openKeyboard(etSearch);
    }

    private void addListeners() {

        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                ivCross.setVisibility(View.GONE);
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ivCross.setVisibility(View.VISIBLE);
                } else {
                    ivCross.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    private void search() {
        if (etSearch.getText().length() > 0) {
            progressBar.setVisibility(View.VISIBLE);
            etSearch.clearFocus();
            etSearch.setCursorVisible(false);
            closeKeyBoard();
            //Toast.makeText(activity, etSearch.getText().toString(), Toast.LENGTH_SHORT).show();
            getSearchResponse(etSearch.getText().toString().trim());
        } else {
            Toast.makeText(activity, "Please enter text", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSearchResponse(String search){

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
        }, InternetOperations.SERVER_URL + "search/getResults?text="+search);

    }

    private void json(String response) {

        JSONArray jsonArray;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonArray = new JSONArray(response);

                if (jsonArray.length() > 0) {
                    listSearch.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                        String id = jsonObject.optString("id");
                        String title = jsonObject.optString("title");
                        String text = jsonObject.optString("text");
                        String image = jsonObject.optString("image");
                        String link = jsonObject.optString("link");
                        String type = jsonObject.optString("type");

                        populateSearch(id, title, text, image, link, type);
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

    public void populateSearch(String id, String title,String text, String image, String link, String type) {
        SearchPojo sp = new SearchPojo();
        sp.setId(id);
        sp.setTitle(title);
        sp.setText(text);
        sp.setImage(image);
        sp.setLink(link);
        sp.setType(type);
        sp.setContext(activity);
        listSearch.add(sp);
    }

    private void resetViews() {
        searchAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void openKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


}
