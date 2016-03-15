package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.wohlig.blazennative.ARC.Http.HttpSimple;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.FormValidation;
import com.wohlig.blazennative.Util.InternetOperations;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ContactFragment extends Fragment {
    private  View view, v1, v2;
    private Activity activity;
    private static String TAG = "BLAZEN";
    private LinearLayout ll1, ll2;
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

    private void initilizeViews(){
        ll1 = (LinearLayout) view.findViewById(R.id.ll1);
        ll2 = (LinearLayout) view.findViewById(R.id.ll2);

        v1 = view.findViewById(R.id.v1);
        v2 = view.findViewById(R.id.v2);

        vfContact = (ViewFlipper) view.findViewById(R.id.vfContact);

        tvSubmit = (TextView) view.findViewById(R.id.tvSubmit);
        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etSub = (EditText) view.findViewById(R.id.etSub);
        etComments = (EditText) view.findViewById(R.id.etComments);
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


                if (valid) {
                    submit(name,email,subject,comments);
                }
            }
        });
    }


    public void submit(final String name, final String email, final String subject, final String comment){

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

                }else
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
