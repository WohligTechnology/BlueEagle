package com.wohlig.blazennative.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import com.wohlig.blazennative.Fragments.BlogFragment;
import com.wohlig.blazennative.Fragments.PhotoGridFragment;
import com.wohlig.blazennative.Fragments.SingleBlogFragment;
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
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    public static TextView tvTitle;
    public static String ID;
    public static String TITLE;
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
        show();
    }

    public void show() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BlogFragment fragment = new BlogFragment();

        //fragmentTransaction.add(R.id.container, homeFragment, "HOME");
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
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

    public static void setToolbarText(String text) {
        tvTitle.setText(text);
    }

    public static void setId(String id) {
        ID = id;
    }

    public static String getId() {
        return ID;
    }

    public void goToPhotoGridFragment(View v) {
        String tag = v.getTag().toString();
        List<String> info = Arrays.asList(tag.split("!!!"));

        String id = info.get(0);
        String title = info.get(1);
        setId(id);
        setToolbarText(title);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PhotoGridFragment fragment = new PhotoGridFragment();

        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToSingleBlog(View v){
        String id = v.getTag().toString();
        /*List<String> info = Arrays.asList(tag.split("!!!"));

        String id = info.get(0);
        String title = info.get(1);*/
        setId(id);
        //setToolbarText(title);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SingleBlogFragment fragment = new SingleBlogFragment();

        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
