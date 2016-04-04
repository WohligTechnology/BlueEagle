package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.view.IconicsImageView;
import com.wohlig.blazennative.R;

public class SearchFragment extends Fragment {
    private static String TAG = "BLAZEN";
    private Activity activity;
    private View view;
    private EditText etSearch;
    private IconicsImageView ivCross, ivSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        activity = getActivity();

        initlizeViews();
        addListeners();

        return view;
    }

    private void initlizeViews() {

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
            etSearch.clearFocus();
            etSearch.setCursorVisible(false);
            closeKeyBoard();
            Toast.makeText(activity, etSearch.getText().toString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Please enter text", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void openKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(InputMethodManager.SHOW_IMPLICIT);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


}
