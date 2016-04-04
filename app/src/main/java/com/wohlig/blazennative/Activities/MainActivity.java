package com.wohlig.blazennative.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wohlig.blazennative.Fragments.ArticleFragment;
import com.wohlig.blazennative.Fragments.BlogFragment;
import com.wohlig.blazennative.Fragments.ContactFragment;
import com.wohlig.blazennative.Fragments.EventFragment;
import com.wohlig.blazennative.Fragments.HomeFragment;
import com.wohlig.blazennative.Fragments.NotificationFragment;
import com.wohlig.blazennative.Fragments.PhotoFragment;
import com.wohlig.blazennative.Fragments.PhotoGridFragment;
import com.wohlig.blazennative.Fragments.ProfileFragment;
import com.wohlig.blazennative.Fragments.SettingsFragment;
import com.wohlig.blazennative.Fragments.SingleBlogFragment;
import com.wohlig.blazennative.Fragments.SingleEventFragment;
import com.wohlig.blazennative.Fragments.TeamFragment;
import com.wohlig.blazennative.Fragments.VideoFragment;
import com.wohlig.blazennative.Fragments.VideoListFragment;
import com.wohlig.blazennative.Navigation.NavigationDrawerCallbacks;
import com.wohlig.blazennative.Navigation.NavigationDrawerFragment;
import com.wohlig.blazennative.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private static String TAG = "BLAZEN";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private static TextView tvTitle;
    private static String ID;
    private boolean doubleBackToExitPressedOnce = false;

    private static DrawerLayout mNavigationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        //tvTitle.setText("Home");

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);


        mNavigationLayout = mNavigationDrawerFragment.getDrawerLayout();
    }


    @Override
    public void onNavigationDrawerItemSelected(int position,final String type, final String link) {
        // update the main content by replacing fragments

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!type.equals("external")) {
                    setId(link);
                    goTo(type, false);
                } else {
                    external(link);
                }

            }
        }, 300);

    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else if (getFragmentManager().getBackStackEntryCount() == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit...", Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String title = item.getTitle().toString().toUpperCase();

        if (title.equals("SEARCH")) {
            startActivity(new Intent(MainActivity.this,SearchActivity.class));
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            //Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (goBack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public static void lockNavigationSlide() {
        mNavigationLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //back = true;
    }

    public static void unlockNavigationSlide() {
        mNavigationLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        //back = false;
    }

    public void external(String link){
        Intent intent = new Intent(MainActivity.this, WebActivity.class);
        intent.putExtra("webLink", link);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    public void goToPhotoGridFragment(View v) {
        String tag = v.getTag().toString();
        /*List<String> info = Arrays.asList(tag.split("!!!"));

        String id = info.get(0);
        String title = info.get(1);*/
        setId(tag);
        //setToolbarText(title);

        goTo("imageGallery", true);
    }

    public void goToSingleBlog(View v) {
        String id = v.getTag().toString();
        setId(id);

        goTo("singleBlog",true);
    }

    public void goToVideoList(View v) {
        String id = v.getTag().toString();
        setId(id);

        goTo("videoGallery",true);
    }

    public void goToSingleVideo(View v){
        String playLink = v.getTag().toString();
        Intent intent = new Intent(MainActivity.this, PlayVideoActivity.class);
        intent.putExtra("webLink", playLink);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
    }

    public void goToSingleEvent(View v) {
        String id = v.getTag().toString();
        setId(id);

        goTo("singleEvent", true);
    }

    public void goToSettings(View v){
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        }
        //goTo("settings", true);

        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

        //Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
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
}
