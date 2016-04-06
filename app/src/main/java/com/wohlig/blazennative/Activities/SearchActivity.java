package com.wohlig.blazennative.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wohlig.blazennative.Fragments.ArticleFragment;
import com.wohlig.blazennative.Fragments.BlogFragment;
import com.wohlig.blazennative.Fragments.ContactFragment;
import com.wohlig.blazennative.Fragments.EventFragment;
import com.wohlig.blazennative.Fragments.HomeFragment;
import com.wohlig.blazennative.Fragments.NotificationFragment;
import com.wohlig.blazennative.Fragments.PhotoFragment;
import com.wohlig.blazennative.Fragments.PhotoGridFragment;
import com.wohlig.blazennative.Fragments.ProfileFragment;
import com.wohlig.blazennative.Fragments.SearchFragment;
import com.wohlig.blazennative.Fragments.SettingsFragment;
import com.wohlig.blazennative.Fragments.SingleBlogFragment;
import com.wohlig.blazennative.Fragments.SingleEventFragment;
import com.wohlig.blazennative.Fragments.TeamFragment;
import com.wohlig.blazennative.Fragments.VideoFragment;
import com.wohlig.blazennative.Fragments.VideoListFragment;
import com.wohlig.blazennative.R;

import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static String TAG = "BLAZEN";
    private ImageView ivBack;
    private static TextView tvTitle;
    private static String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initilizeViews();
    }

    private void initilizeViews(){

        ivBack = (ImageView) findViewById(R.id.ivBack);

        tvTitle = (TextView) findViewById(R.id.toolbar_title);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchFragment searchFragment = new SearchFragment();

        fragmentTransaction.add(R.id.container, searchFragment);
        //fragmentTransaction.replace(R.id.container, scheduleFragment);
        fragmentTransaction.commit();

    }

    public static void setToolbarText(String text) {
        tvTitle.setText(text);
    }

    public static void setId(String id) {
        ID = id;
    }

    public static String getId() {
        return ID;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            //hopenKeyBoard();
            closeKeyBoard();
            finish();
            overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
        } else {
            getFragmentManager().popBackStack();
        }

    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void openKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void notification(View v) {
        String tag = v.getTag().toString();

        List<String> notification = Arrays.asList(tag.split("!!!"));

        String type = notification.get(0);
        String link = notification.get(1);

        if(!type.equals("external")) {
            setId(link);
            goTo(type, true);
        }else {
            external(link);
        }
    }

    public void goTo(String fragmentName, boolean goBack) {

        Fragment fragment = null;

        if (fragmentName.equals("article")) {                   //Article
            fragment = new ArticleFragment();
        } else if (fragmentName.equals("blog")) {               //blog
            fragment = new BlogFragment();
        } else if (fragmentName.equals("contact")) {            //contact
            fragment = new ContactFragment();
        } else if (fragmentName.equals("event")) {              //event
            fragment = new EventFragment();
        } else if (fragmentName.equals("home")) {               //home
            fragment = new HomeFragment();
        } else if (fragmentName.equals("notification")) {       //notification
            fragment = new NotificationFragment();
        } else if (fragmentName.equals("photo")) {              //photo
            fragment = new PhotoFragment();
        } else if (fragmentName.equals("profile")) {            //profile
            fragment = new ProfileFragment();
        } else if (fragmentName.equals("team")) {               //team
            fragment = new TeamFragment();
        } else if (fragmentName.equals("video")) {              //video
            fragment = new VideoFragment();
        } else if (fragmentName.equals("singleBlog")){          //singleBlog
            fragment = new SingleBlogFragment();
        } else if (fragmentName.equals("videoGallery")){        //video gallery
            fragment = new VideoListFragment();
        } else if (fragmentName.equals("singleEvent")){         //singleEvent
            fragment = new SingleEventFragment();
        } else if (fragmentName.equals("imageGallery")){        //image gallery
            fragment = new PhotoGridFragment();
        } else if (fragmentName.equals("settings")){            //settings
            fragment = new SettingsFragment();
        } else {                                                //default Home
            fragment = new HomeFragment();
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit, R.animator.fragment_slide_right_enter, R.animator.fragment_slide_right_exit);

        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (goBack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void external(String link){
        Intent intent = new Intent(SearchActivity.this, WebActivity.class);
        intent.putExtra("webLink", link);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    public void goToPhotoGridFragment(View v) {
        String tag = v.getTag().toString();
        setId(tag);
        goTo("imageGallery", true);
    }

    public void goToSingleBlog(View v) {
        String id = v.getTag().toString();
        setId(id);

        goTo("singleBlog", true);
    }

    public void goToVideoList(View v) {
        String id = v.getTag().toString();
        setId(id);

        goTo("videoGallery",true);
    }

    public void goToSingleVideo(View v){
        String playLink = v.getTag().toString();
        Intent intent = new Intent(SearchActivity.this, PlayVideoActivity.class);
        intent.putExtra("webLink", playLink);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    public void goToSingleEvent(View v) {
        String id = v.getTag().toString();
        setId(id);

        goTo("singleEvent", true);
    }
}
