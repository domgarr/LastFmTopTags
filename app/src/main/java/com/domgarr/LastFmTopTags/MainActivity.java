package com.domgarr.LastFmTopTags;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.domgarr.LastFmTopTags.models.top_tag_response.Tag;
import com.domgarr.LastFmTopTags.models.top_tag_response.TopTagResponse;
import com.domgarr.LastFmTopTags.models.top_track_response.Track;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TrackFragment.OnListFragmentInteractionListener, TagFragment.OnListFragmentInteractionListener {
    public static final int TOP_TRACK_LIMIT = 15;
    private static int TABLET_MODE_WIDTH = 943;
    //Views
    private NavigationView navView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    public static List<Tag> tags;

    private String appBarTitle;
    //Bundle Data
    //Used in persisting tag selected state in the drawer menu after orientation change.
    public static final String TAG_SELECTED = "tagSelected";
    private Integer tagSelected;
    public static final String TAG_NAME = "tagName";

    private static final String APP_BAR_TITLE = "appBarTitle";

    private String tagName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarTitle = getString(R.string.home);
        if (savedInstanceState != null) {
            tagSelected = savedInstanceState.getInt(TAG_SELECTED);
            appBarTitle = savedInstanceState.getString(APP_BAR_TITLE);
        }
        /*
        This is to check for Tablets in portrait mode whose smallestScreenWidthDP smallest is
        greater TABLET_MODE_WIDTH. If it is, dispaly two fragments instead of the hamburger icon.
        *Note- Hard to test this, since emulators in Android Studio didn't have screenWidthDP this large.
         */
        Configuration config = getResources().getConfiguration();
        int smallestScreenWidthDp = config.smallestScreenWidthDp;

        setContentView(R.layout.activity_main);
        initToolbar();

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE || smallestScreenWidthDp > TABLET_MODE_WIDTH) {
            //Landscape Mode - Two side-by-side fragments
            Fragment tagFragment = new TagFragment();
            Bundle bundle = new Bundle();
            //This check is handled by the fragment, but an emulator I tested on crashed due to this.
            if(tagSelected != null) {
                bundle.putInt(TAG_SELECTED, tagSelected);
            }
            tagFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tags, tagFragment).commit();
        } else {
            //Portrait Mode - Hamburger - Drawer Menu with single fragment.
            requestTopTags(); //Async. fetch top tags from LastFm.
            initDrawerSlider();
        }
    }

    @Override
    public void onBackPressed() {
        //Closes drawer menu
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /* Re-instantiate the fragment only if a different tag is chosen.
        This maybe isn't the best? What if top tags change in ranking?
         */
        if (tagSelected == null || item.getItemId() != tagSelected) {
            navView.setCheckedItem(item.getItemId());
            //Store values fragment interactions and persistence upon orientation change.
            tagSelected = item.getItemId();
            tagName = tags.get(tagSelected).getName();

            //Communicate to fragment
            Fragment trackFragment = new TrackFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TAG_NAME, tagName);
            trackFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tracks, trackFragment).commit();

            //Set title of AppBar to newly selected tag and persist tagTitle.
            setTitle(tagName);
            appBarTitle = getTitle().toString();

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false; //If false is returned, no item will be selected.
    }

    private void initToolbar() {
        //Get reference to tool bar and set tool bar as action bar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(appBarTitle);
    }

    //Note: Data to populate drawer menu is done async. before this method is called.  (requestTopTags())
    private void initDrawerSlider() {
        drawer = findViewById(R.id.draw_layout);
        //Add Hamburger icon.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState(); //Adds hamburger rotation as drawer opens.

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
    }
    //Combining Retrofit and rxJava to make an asyn. api call to Last FM to gather 50 Top Tags in ascending order.
    private void requestTopTags() {
        Single<Response<TopTagResponse>> call = LastFm.getInstance().getLastFmService().topTags(LastFm.API_KEY);
        call.subscribeOn(Schedulers.io()) //Important that Async. call is done off main UI thread.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<TopTagResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //Here I can add a loading view.
                    }

                    @Override
                    public void onSuccess(Response<TopTagResponse> topTagResponseResponse) {
                        tags = topTagResponseResponse.body().getTopTags().getTags();
                        populateDrawerMenu(tags);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Send a message to user indicating so and retry call.
                    }
                });
    }

    // This is called by requestTopTags() in onSuccess() after asynchronously fetching top tag data from Last FM.
    private void populateDrawerMenu(List<Tag> tags) {
        Menu menu = navView.getMenu();
        //Can safely use 0th index to grab the sub menu since 'Categories' is the only Menu in menu's layout.
        SubMenu categorySubMenu = menu.getItem(0).getSubMenu();

        //Dynamically add MenuItems to SubMenu 'Categories'
        int itemId = 0; //Not too happy with this. Need to learn how to dynamically generate resource IDs.
        for (Tag tag : tags) {
            MenuItem newMenuItem = categorySubMenu.add(0, itemId++, Menu.NONE, tag.getName());
            newMenuItem.setCheckable(true); //Needed for a menuitem to be in a checked state.
        }
        //Persist tag selected in drawer menu if a value exists.
        if (tagSelected != null) {
            navView.setCheckedItem(tagSelected);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (tagSelected != null) {
            outState.putString(APP_BAR_TITLE, appBarTitle);

            outState.putInt(TAG_SELECTED, tagSelected);
            outState.putString(TAG_NAME, tagName);
        }
        super.onSaveInstanceState(outState);
    }

    //Not used but required by Recycler View.
    @Override
    public void onListFragmentInteraction(Track track) {
    }

    // This is called when an item on TagFragment RecyclerView list is clicked.
    @Override
    public void onListFragmentInteraction(String tagName) {
        //Save the state of tag name.
        this.tagName = tagName;

        //Update TrackFragment with newly selected Tag and update track list.
        Fragment trackFragment = new TrackFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG_NAME, tagName);
        trackFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tracks, trackFragment).commit();

        //Setting AppBar title as the tag chosen.
        setTitle(tagName);
        appBarTitle = getTitle().toString();
    }
}