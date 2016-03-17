package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.wohlig.blazennative.ARC.Http.HttpCallback;
import com.wohlig.blazennative.ARC.Http.HttpInterface;
import com.wohlig.blazennative.ARC.Http.HttpSimple;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.FormValidation;
import com.wohlig.blazennative.Util.InternetOperations;
import com.wohlig.blazennative.Util.Size;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ContactFragment extends Fragment {
    private View view;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private View v1, v2;
    private LinearLayout ll1, ll2, llAddress;
    private ViewFlipper vfContact;
    private EditText etName, etEmail, etSub, etComments;
    private TextView tvSubmit;
    private View form;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        activity = getActivity();
        initilizeViews();
        setListeners();
        return view;
    }

    private void initilizeViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        ll1 = (LinearLayout) view.findViewById(R.id.ll1);
        ll2 = (LinearLayout) view.findViewById(R.id.ll2);

        llAddress = (LinearLayout) view.findViewById(R.id.llAddress);

        v1 = view.findViewById(R.id.v1);
        v2 = view.findViewById(R.id.v2);

        vfContact = (ViewFlipper) view.findViewById(R.id.vfContact);

        tvSubmit = (TextView) view.findViewById(R.id.tvSubmit);
        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etSub = (EditText) view.findViewById(R.id.etSub);
        etComments = (EditText) view.findViewById(R.id.etComments);

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
        }, InternetOperations.SERVER_URL + "contact/getAll");
    }

    private void json(String response) {

        JSONArray jsonArray = null;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonArray = new JSONArray(response);

                if (jsonArray.length() > 0) {

                    llAddress.removeAllViews();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.optString("title");
                        String address = jsonObject.optString("address");
                        String telephone = jsonObject.optString("telephone");
                        String mail = jsonObject.optString("mail");
                        String lat = jsonObject.optString("lat");
                        String lon = jsonObject.optString("long");

                        addLocation(title, address, telephone, mail, lat, lon);
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

    private void addLocation(String title, String address, String telephone, String mail, final String lat, final String lon) {

        LayoutInflater inflator = activity.getLayoutInflater();
        View viewLocation = inflator.inflate(R.layout.layout_location, null, false);

        TextView tvTitle, tvAddress, tvNumber, tvEmail;
        LinearLayout llTitle, llAddressIn, llNumber, llEmail, llDirection, llDirectionButton;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, Size.dpToPx(activity,10));
        viewLocation.setLayoutParams(lp);

        llTitle = (LinearLayout) viewLocation.findViewById(R.id.llTime);
        llAddressIn = (LinearLayout) viewLocation.findViewById(R.id.llAddressIn);
        llNumber = (LinearLayout) viewLocation.findViewById(R.id.llNumber);
        llEmail = (LinearLayout) viewLocation.findViewById(R.id.llEmail);
        llDirection = (LinearLayout) viewLocation.findViewById(R.id.llDirection);
        llDirectionButton = (LinearLayout) viewLocation.findViewById(R.id.llDirectionButton);

        tvTitle = (TextView) viewLocation.findViewById(R.id.tvTitle);
        tvAddress = (TextView) viewLocation.findViewById(R.id.tvAddress);
        tvNumber = (TextView) viewLocation.findViewById(R.id.tvNumber);
        tvEmail = (TextView) viewLocation.findViewById(R.id.tvEmail);

        //title
        if(!title.equals("") || !title.isEmpty()){
            tvTitle.setText(title);
        } else {
            llTitle.setVisibility(View.GONE);
        }

        //address
        if(!address.equals("") || !address.isEmpty()){
            tvAddress.setText(address);
        } else {
            llAddressIn.setVisibility(View.GONE);
        }

        //mail
        if(!mail.equals("") || !mail.isEmpty()){
            tvEmail.setText(mail);
        } else {
            llEmail.setVisibility(View.GONE);
        }

        //telephone
        if(!telephone.equals("") || !telephone.isEmpty()){
            tvNumber.setText(telephone);
        } else {
            llNumber.setVisibility(View.GONE);
        }

        if(!lat.equals("") && !lat.isEmpty() && !lon.equals("") && !lon.isEmpty()){

            llDirectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "http://maps.google.com/maps?&daddr="+ lat +","+lon;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(uri));
                    startActivity(intent);
                }
            });

        } else {
            llDirection.setVisibility(View.GONE);
        }

        llAddress.addView(viewLocation);
    }

    private void resetViews(){


        progressBar.setVisibility(View.GONE);
    }

    private void setListeners() {
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

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String subject = etSub.getText().toString().trim();
                String comments = etComments.getText().toString().trim();

                boolean valid = true;

                //Name
                if (name.isEmpty()) {
                    etName.setError("Enter Name");
                    valid = false;
                } else {
                    etName.setError(null);
                }

                /*if (phone.isEmpty()) {
                    etMob.setError("Enter Mobile Number");
                    valid = false;
                } else if (!FormValidation.isValidMobile(phone)) {
                    etMob.setError("Enter Valid Mobile Number");
                    valid = false;
                } else {
                    etMob.setError(null);
                }*/

                //Email
                if (email.isEmpty()) {
                    etEmail.setError("Enter Email");
                    valid = false;
                } else if (!FormValidation.isValidEmail(email)) {
                    etEmail.setError("Enter Valid Email");
                    valid = false;
                } else {
                    etEmail.setError(null);
                }


                //Subject
                if (subject.isEmpty()) {
                    etSub.setError("Enter Subject");
                    valid = false;
                } else {
                    etSub.setError(null);
                }

                //Comment
                if (comments.isEmpty()) {
                    etComments.setError("Enter Comment");
                    valid = false;
                } else {
                    etComments.setError(null);
                }


                if (valid && InternetOperations.checkIsOnlineViaIP()) {
                    submit(name, email, subject, comments);
                }
            }
        });
    }

    public void submit(final String name, final String email, final String subject, final String comment) {

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();


        new AsyncTask<Void, Void, String>() {
            boolean submit = false;

            @Override
            protected String doInBackground(Void... params) {

                try {
                    String submitJson = getSubmitJson(name, email, subject, comment).toString();
                    String response = HttpSimple.post(InternetOperations.SERVER_URL + "contact/contactForm", submitJson);

                    JSONObject jsonObject = new JSONObject(response);

                    String value = jsonObject.optString("value");
                    if (value.equals("true")) {
                        progressDialog.dismiss();
                        submit = true;
                    } else {
                        progressDialog.dismiss();
                        submit = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException io) {
                    io.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (submit) {
                    Toast.makeText(activity, "Thank you!", Toast.LENGTH_LONG).show();
                    etName.setText("");
                    etEmail.setText("");
                    etSub.setText("");
                    etComments.setText("");

                } else
                    Toast.makeText(activity, "Oops, Something went wrong!", Toast.LENGTH_LONG).show();

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }.execute(null, null, null);
    }

    public JSONObject getSubmitJson(String name, String email, String subject, String comments) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("subject", subject);
            jsonObject.put("comments", comments);
        } catch (JSONException je) {
        }
        return jsonObject;
    }

}
