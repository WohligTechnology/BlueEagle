package com.wohlig.blazennative.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.wohlig.blazennative.R;

public class ContactFragment extends Fragment {
    private  View view, v1, v2;
    private LinearLayout ll1, ll2;
    private ViewFlipper vfContact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);

        initilizeViews();
        setListeners();
        return view;
    }

    private void initilizeViews(){
        ll1 = (LinearLayout) view.findViewById(R.id.ll1);
        ll2 = (LinearLayout) view.findViewById(R.id.ll2);

        v1 = view.findViewById(R.id.v1);
        v2 = view.findViewById(R.id.v2);

        vfContact = (ViewFlipper) view.findViewById(R.id.vfContact);
    }

    private void setListeners(){
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v2.setBackgroundColor(getResources().getColor(R.color.white));
                v1.setBackgroundColor(getResources().getColor(R.color.toolBarColor));

                if (vfContact.getDisplayedChild() != 0)
                    vfContact.showPrevious();

            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.setBackgroundColor(getResources().getColor(R.color.white));
                v2.setBackgroundColor(getResources().getColor(R.color.toolBarColor));

                if (vfContact.getDisplayedChild() != 1)
                    vfContact.showNext();
            }
        });
    }


}
