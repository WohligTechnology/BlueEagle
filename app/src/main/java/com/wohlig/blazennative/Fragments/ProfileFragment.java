package com.wohlig.blazennative.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.wohlig.blazennative.ARC.Http.HttpCallback;
import com.wohlig.blazennative.ARC.Http.HttpInterface;
import com.wohlig.blazennative.ARC.Http.HttpSimple;
import com.wohlig.blazennative.Activities.MainActivity;
import com.wohlig.blazennative.Activities.SearchActivity;
import com.wohlig.blazennative.R;
import com.wohlig.blazennative.Util.FormValidation;
import com.wohlig.blazennative.Util.InternetOperations;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ProfileFragment extends Fragment {
    private View view;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private String id, email, name, number, location, dob, imageUrl;
    private static TextView tvHeaderName, tvEmail, tvNumber, tvLocation, tvDob;
    private static LinearLayout llEmail, llNumber, llLocation, llDob;
    private static LinearLayout llInfo, llEdit;
    private static ImageView ivPic;
    private static FloatingActionButton fabEdit;
    private static boolean edit;
    private static EditText etName, etEmail, etNumber, etLocation, etDob;
    private static EditText etOldPass, etNewPass, etNewPassConfirm;
    private static LinearLayout llProfileSave, llChangePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        activity = getActivity();
        //((MainActivity) this.getActivity()).setToolbarText("Profile");
        if(activity.getLocalClassName().equals("Activities.SearchActivity")) {
            ((SearchActivity) this.getActivity()).setToolbarText("Profile");
        } else if (activity.getLocalClassName().equals("Activities.MainActivity")) {
            ((MainActivity) this.getActivity()).setToolbarText("Profile");
        }

        initilizeViews();

        return view;
    }

    private void initilizeViews() {

        edit = false;

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        fabEdit = (FloatingActionButton) view.findViewById(R.id.fabEdit);

        ivPic = (ImageView) view.findViewById(R.id.ivPic);
        tvHeaderName = (TextView) view.findViewById(R.id.tvHeaderName);

        llInfo = (LinearLayout) view.findViewById(R.id.llInfo);
        llEdit = (LinearLayout) view.findViewById(R.id.llEdit);

        llEmail = (LinearLayout) view.findViewById(R.id.llEmail);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);

        llNumber = (LinearLayout) view.findViewById(R.id.llNumber);
        tvNumber = (TextView) view.findViewById(R.id.tvNumber);

        llLocation = (LinearLayout) view.findViewById(R.id.llLocation);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);

        llDob = (LinearLayout) view.findViewById(R.id.llDob);
        tvDob = (TextView) view.findViewById(R.id.tvDob);

        //edit
        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etNumber = (EditText) view.findViewById(R.id.etNumber);
        etLocation = (EditText) view.findViewById(R.id.etLocation);
        etDob = (EditText) view.findViewById(R.id.etDob);

        //change password
        etOldPass = (EditText) view.findViewById(R.id.etOldPass);
        etOldPass.setTransformationMethod(new PasswordTransformationMethod());
        etNewPass = (EditText) view.findViewById(R.id.etNewPass);
        etNewPass.setTransformationMethod(new PasswordTransformationMethod());
        etNewPassConfirm = (EditText) view.findViewById(R.id.etNewPassConfirm);
        etNewPassConfirm.setTransformationMethod(new PasswordTransformationMethod());

        llProfileSave = (LinearLayout) view.findViewById(R.id.llProfileSave);
        llChangePassword = (LinearLayout) view.findViewById(R.id.llChangePassword);

        setListeners();
        seeOrEdit();
        getContent();
    }

    private void seeOrEdit() {
        if (edit) {
            fabEdit.setIcon(getResources().getDrawable(R.drawable.ic_cross), false);
            llInfo.setVisibility(View.GONE);
            llEdit.setVisibility(View.VISIBLE);
            edit = false;
        } else {
            fabEdit.setIcon(getResources().getDrawable(R.drawable.ic_edit), false);
            llEdit.setVisibility(View.GONE);
            llInfo.setVisibility(View.VISIBLE);
            edit = true;
        }
    }

    private void setListeners() {
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeOrEdit();
            }
        });

        llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = v.getTag().toString().trim();
                checkPass(id);
            }
        });

        llProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = v.getTag().toString().trim();
                checkProfile(id);
            }
        });
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
        }, InternetOperations.SERVER_URL + "profile/get");
    }

    private void json(String response) {

        JSONObject jsonObject = null;

        try {
            if (!response.equals("")) {                 //check is the response empty
                jsonObject = new JSONObject(response);

                id = jsonObject.optString("id");
                email = jsonObject.optString("email");
                name = jsonObject.optString("name");
                number = jsonObject.optString("number");
                location = jsonObject.optString("location");
                dob = jsonObject.optString("dob");
                imageUrl = jsonObject.optString("image");

                resetViews();
            }
        } catch (JSONException je) {
            Log.e(TAG, Log.getStackTraceString(je));
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void resetViews() {
        //id

        //imageUrl
        if (!imageUrl.equals("") || !imageUrl.isEmpty()) {
            Picasso.with(activity)
                    .load(imageUrl)
                            //.placeholder(R.drawable.ic_placeholder) // optional
                            //.error(R.drawable.ic_error_fallback)         // optional
                    .into(ivPic);
            ivPic.setVisibility(View.VISIBLE);
        } else {
            ivPic.setVisibility(View.GONE);
        }

        //name
        if (!name.equals("") || !name.isEmpty()) {
            tvHeaderName.setText(name);
            tvHeaderName.setVisibility(View.VISIBLE);

            etName.setText(name);
            etName.setVisibility(View.VISIBLE);
        } else {
            tvHeaderName.setVisibility(View.GONE);
            etName.setVisibility(View.GONE);
        }

        //email
        if (!email.equals("") || !email.isEmpty()) {
            tvEmail.setText(email);
            llEmail.setVisibility(View.VISIBLE);

            etEmail.setText(email);
            etEmail.setVisibility(View.VISIBLE);
        } else {
            llEmail.setVisibility(View.GONE);
            etEmail.setVisibility(View.GONE);
        }

        //number
        if (!number.equals("") || !number.isEmpty()) {
            tvNumber.setText(number);
            llNumber.setVisibility(View.VISIBLE);

            etNumber.setText(number);
            etNumber.setVisibility(View.VISIBLE);
        } else {
            llNumber.setVisibility(View.GONE);
            etNumber.setVisibility(View.GONE);
        }

        //location
        if (!location.equals("") || !location.isEmpty()) {
            tvLocation.setText(location);
            llLocation.setVisibility(View.VISIBLE);

            etLocation.setText(location);
            etLocation.setVisibility(View.VISIBLE);
        } else {
            llLocation.setVisibility(View.GONE);
            etLocation.setVisibility(View.GONE);
        }

        //dob
        if (!dob.equals("") || !dob.isEmpty()) {
            tvDob.setText(dob);
            llDob.setVisibility(View.VISIBLE);

            etDob.setText(dob);
            etDob.setVisibility(View.VISIBLE);
        } else {
            llDob.setVisibility(View.GONE);
            etDob.setVisibility(View.GONE);
        }

        if (!id.equals("") || !id.isEmpty()) {
            llChangePassword.setTag(id);
            llProfileSave.setTag(id);
        }

        progressBar.setVisibility(View.GONE);
    }

    private void checkPass(final String id) {
        String oldPass = etOldPass.getText().toString().trim();
        String newPass = etNewPass.getText().toString().trim();
        String newPassConfirm = etNewPassConfirm.getText().toString().trim();

        boolean valid = true;

        //Old Pass
        if (oldPass.isEmpty()) {
            etOldPass.setError("Enter Old Password");
            valid = false;
        } else {
            etOldPass.setError(null);
        }

        //New Pass
        if (newPass.isEmpty()) {
            etNewPass.setError("Enter New Password");
            valid = false;
        } else {
            etNewPass.setError(null);
        }

        //Confirm New Pass
        if (newPassConfirm.isEmpty()) {
            etNewPassConfirm.setError("Confirm New Password");
            valid = false;
        } else {
            etNewPassConfirm.setError(null);
        }

        if (!newPass.equals(newPassConfirm)) {
            valid = false;
            Toast.makeText(activity, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        } else if (newPass.equals(oldPass) && !newPass.equals("") && !oldPass.equals("")) {
            valid = false;
            Toast.makeText(activity, "New and Old Password cannot be same!", Toast.LENGTH_SHORT).show();
        }


        if (valid && InternetOperations.checkIsOnlineViaIP()) {
            changePassword(id, oldPass, newPass);
        } else if (valid) {
            Toast.makeText(activity, "No Internet!", Toast.LENGTH_SHORT).show();
        }
    }

    private void changePassword(final String id, final String oldPassword, final String newPassword) {

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
                    String submitJson = getChangePasswordJson(id, oldPassword, newPassword).toString();
                    String response = HttpSimple.post(InternetOperations.SERVER_URL + "profile/changePassword", submitJson);

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
                    Toast.makeText(activity, "Password Successfully Changed!", Toast.LENGTH_LONG).show();
                    etOldPass.setText("");
                    etNewPass.setText("");
                    etNewPassConfirm.setText("");

                } else
                    Toast.makeText(activity, "Oops, Something went wrong!", Toast.LENGTH_LONG).show();

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }.execute(null, null, null);

    }

    private void checkProfile(final String id) {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String number = etNumber.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String dob = etDob.getText().toString().trim();

        boolean valid = true;

        //Name
        if (name.isEmpty()) {
            etName.setError("Enter Name");
            valid = false;
        } else {
            etName.setError(null);
        }

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

        //Number
        if (number.isEmpty()) {
            etNumber.setError("Enter Number");
            valid = false;
        } else {
            etNumber.setError(null);
        }

        //Location
        if (location.isEmpty()) {
            etLocation.setError("Enter Location");
            valid = false;
        } else {
            etLocation.setError(null);
        }

        //DOB
        if (dob.isEmpty()) {
            etDob.setError("Enter Date of Birth");
            valid = false;
        } else {
            etDob.setError(null);
        }


        if (valid && InternetOperations.checkIsOnlineViaIP()) {
            editProfile(id, email, name, number, location, dob);
        } else if (valid) {
            Toast.makeText(activity, "No Internet!", Toast.LENGTH_SHORT).show();
        }

    }

    private void editProfile(final String id, final String email, final String name, final String number, final String location, final String dob) {
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
                    String submitJson = getEditProfileJson(id, email, name, number, location, dob).toString();
                    String response = HttpSimple.post(InternetOperations.SERVER_URL + "profile/update", submitJson);

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
                    Toast.makeText(activity, "Profile Edited!", Toast.LENGTH_LONG).show();
                    etOldPass.setText("");
                    etNewPass.setText("");
                    etNewPassConfirm.setText("");

                } else
                    Toast.makeText(activity, "Oops, Something went wrong!", Toast.LENGTH_LONG).show();

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }.execute(null, null, null);
    }

    private JSONObject getChangePasswordJson(String id, String oldPass, String newPass) {
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("id", id);
            jsonObject.put("oldPassword", oldPass);
            jsonObject.put("newPassword", newPass);
        } catch (JSONException je) {
        }
        return jsonObject;
    }

    private JSONObject getEditProfileJson(String id, String email, String name, String number, String location, String dob) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("email", email);
            jsonObject.put("name", name);
            jsonObject.put("number", number);
            jsonObject.put("location", location);
            jsonObject.put("dob", dob);
        } catch (JSONException je) {
        }
        return jsonObject;
    }
}
