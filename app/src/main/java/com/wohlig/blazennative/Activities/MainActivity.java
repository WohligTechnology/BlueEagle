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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvTitle.setText("Home");

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        //goTo("notification", false);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position,String type, String link) {
        // update the main content by replacing fragments
        if(!type.equals("external")) {
            setId(link);
            goTo(type, false);
        }else {
            external(link);
        }
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
        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    public void test(String abc){

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
        } else {                                                //default Home
            fragment = new HomeFragment();
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (goBack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void external(String link){
        Intent intent = new Intent(MainActivity.this, WebActivity.class);
        intent.putExtra("webLink", link);
        startActivity(intent);
    }

    public void goToPhotoGridFragment(View v) {
        String tag = v.getTag().toString();
        /*List<String> info = Arrays.asList(tag.split("!!!"));

        String id = info.get(0);
        String title = info.get(1);*/
        setId(tag);
        //setToolbarText(title);

        goTo("imageGallery",true);
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

    public void goToSingleEvent(View v) {
        String id = v.getTag().toString();
        setId(id);

        goTo("singleEvent", true);
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
