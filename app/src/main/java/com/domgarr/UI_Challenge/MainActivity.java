package com.domgarr.UI_Challenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.domgarr.UI_Challenge.models.Song;
import com.domgarr.UI_Challenge.models.Tag;
import com.domgarr.UI_Challenge.models.TopTagResponse;
import com.domgarr.UI_Challenge.models.Track;
import com.google.android.material.navigation.NavigationView;


import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SongFragment.OnListFragmentInteractionListener, CategoryFragment.OnListFragmentInteractionListener {
    public static List<Song> SONGS;
    public static List<Tag> tags;
    public static final int TOP_TRACK_LIMIT = 15;

    private NavigationView navView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private Integer tagSelected;
    private String appBarTitle;

    public static final String CATEGORY_SELECTED = "categorySelected";
    public static final String TAG_NAME = "tagName";
    private static final String APP_BAR_TITLE = "appBarTitle";

    private String tagName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarTitle = getString(R.string.home);
        if (savedInstanceState != null) {
            tagSelected = savedInstanceState.getInt(CATEGORY_SELECTED);
            appBarTitle = savedInstanceState.getString(APP_BAR_TITLE);
        }

        setContentView(R.layout.activity_main);
        initToolbar();

        //TODO: Refactor into two methods.
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Fragment categoryFragment = new CategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(CATEGORY_SELECTED, tagSelected);
            categoryFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_categories, categoryFragment).commit();
        } else {
            requestTopTags(); //Async. fetch top tags from LastFm.
            initDrawerSlider();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //TODO: When a category is selected, while already being selected, the song item selected is lost. Prevent app from re-initialzing an Fragment if category is clicked twice.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Re-instantiate the fragment only if a differnt category is chosen.
        if (tagSelected == null || item.getItemId() != tagSelected) {
            navView.setCheckedItem(item.getItemId());
            tagSelected = item.getItemId();
            tagName = tags.get(tagSelected).getName();

            Fragment songFragment = new SongFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TAG_NAME, tagName);
            songFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_songs, songFragment).commit();

            setTitle(tagName);
            appBarTitle = getTitle().toString();

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false; //If false is returned, no item will be selected.
    }

    private void initDrawerSlider() {
        drawer = findViewById(R.id.draw_layout);
        //Add Hamburger icon.
        //TODO: The strings for impaired should be stored in Resources.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState(); //Adds hamburger rotation as drawer opens.
        //Add dynamic items to NavigationView.
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        if (tagSelected != null) {
            navView.setCheckedItem(tagSelected);
        }
    }

    private void initToolbar() {
        //Get reference to tool bar and set tool bar as action bar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(appBarTitle);
    }

    private void requestTopTags() {
        Single<Response<TopTagResponse>> call = LastFm.getInstance().getLastFmService().topTags(LastFm.API_KEY);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<TopTagResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<TopTagResponse> topTagResponseResponse) {
                        tags = topTagResponseResponse.body().getTopTags().getTags();
                        populateDrawerMenu(tags);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void populateDrawerMenu(List<Tag> tags) {
        Menu menu = navView.getMenu();
        //Can safely use 0th index to grab submen since 'Categories' is the only Menu in menu's layout.
        SubMenu categorySubMenu = menu.getItem(0).getSubMenu();

        //Dynamically add MenuItems to SubMenu 'Categories'
        int itemId = 0;
        for (Tag tag : tags) {
            MenuItem newMenuItem = categorySubMenu.add(0, itemId++, Menu.NONE, tag.getName());
            newMenuItem.setCheckable(true);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (tagSelected != null) {
            outState.putInt(CATEGORY_SELECTED, tagSelected);
            outState.putString(APP_BAR_TITLE, appBarTitle);
            outState.putString(TAG_NAME, tagName);

        }
        super.onSaveInstanceState(outState);
    }

    /*
    The following two implementations are required by List Fragments generated via Android Studio
    */
    @Override
    public void onListFragmentInteraction(Track track) {
    }

    @Override
    public void onListFragmentInteraction(String tagName) {
        Log.d("TEST", tagName);
        this.tagName = tagName;

        Fragment songFragment = new SongFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG_NAME, tagName);
        songFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_songs, songFragment).commit();

        setTitle(tagName);
        appBarTitle = getTitle().toString();
    }

}