package com.domgarr.UI_Challenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.domgarr.UI_Challenge.models.Category;
import com.domgarr.UI_Challenge.models.Song;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SongFragment.OnListFragmentInteractionListener, CategoryFragment.OnListFragmentInteractionListener {
    public static List<Song> SONGS;
    public static List<Category> CATEGORIES;

    private NavigationView navView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private Integer categorySelected;
    private String appBarTitle;

    private static final String CATEGORY_SELECTED = "categorySelected";
    private static final String APP_BAR_TITLE = "appBarTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarTitle = getString(R.string.home);
        if(savedInstanceState != null){
            categorySelected = savedInstanceState.getInt(CATEGORY_SELECTED);
            appBarTitle = savedInstanceState.getString(APP_BAR_TITLE);
        }

        setContentView(R.layout.activity_main);

        //Get reference to tool bar and set tool bar as action bar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO: Title doesn't persist through rotation.
        setTitle(appBarTitle);


        //TODO: Refactor into two methods.
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_categories, new CategoryFragment()).commit();
        }else{
            drawer = findViewById(R.id.draw_layout);
            //Add Hamburger icon.
            //TODO: The strings for impaired should be stored in Resources.
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState(); //Adds hamburger rotation as drawer opens.
            //Add dynamic items to NavigationView.
            navView = findViewById(R.id.nav_view);
            Menu menu = navView.getMenu();

            //Can safely use 0th index to grab submen since 'Categories' is the only Menu in menu's layout.
            SubMenu categorySubMenu = menu.getItem(0).getSubMenu();

            ArrayList<Category> categories = (ArrayList) getCategories();

            //Dynamically add MenuItems to SubMenu 'Categories'
            int itemId = 0;
            for(Category category : categories) {
                MenuItem newMenuItem = categorySubMenu.add(0, itemId++, Menu.NONE, category.getName());
                newMenuItem.setCheckable(true);
            }

            navView.setNavigationItemSelectedListener(this);

            if(categorySelected != null){
                navView.setCheckedItem(categorySelected);
            }
        }

        //TODO: Add a default fragment?
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
    //TODO: When a category is selected, while already being selected, the song item selected is lost. Prevent app from re-initialzing an Fragment if category is clicked twice.

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Re-instantiate the fragment only if a differnt category is chosen.
        if(categorySelected == null || item.getItemId() != categorySelected) {
            //I decided to store the songs into a variable, instead of passing into a bundle to reduce complexity.
            SONGS = getCategories().get(item.getItemId()).getList();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_songs, new SongFragment()).commit();

            /* MenuItems are in a group containing single click behaviour meaning that deselecting
            items will be taking care of after.
             */
            navView.setCheckedItem(item.getItemId());
            categorySelected = item.getItemId();
            setTitle(getCategories().get(item.getItemId()).getName());
            appBarTitle = getTitle().toString();

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false; //If false is returned, no item will be selected.
    }

    /**
     *
     * @return List of Category models containing a list of Songs.
     */
    private List<Category> getCategories(){
        List<Category> categories = new ArrayList<>();

        List<com.domgarr.UI_Challenge.models.Song> rockSongs = new ArrayList<com.domgarr.UI_Challenge.models.Song>();
        rockSongs.add(new Song("Stairway To Heaven."));
        rockSongs.add(new Song("We Will Rock You."));
        Category rock = new Category("Rock", rockSongs);
        categories.add(rock);

        List<com.domgarr.UI_Challenge.models.Song> classicalSongs = new ArrayList<com.domgarr.UI_Challenge.models.Song>();
        classicalSongs.add(new Song("SIBELIUS Violin Concerto in D minor, Op. 47"));
        classicalSongs.add(new Song("Clair De Lune"));

        Category classical = new Category("Classical", classicalSongs);
        categories.add(classical);

        CATEGORIES = categories;
        return categories;
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if(categorySelected != null){
            outState.putInt(CATEGORY_SELECTED, categorySelected);
            outState.putString(APP_BAR_TITLE, appBarTitle);
        }

        super.onSaveInstanceState(outState);
    }

    /*
    The following two implementations are required by List Fragments generated via Android Studio
    */
    @Override
    public void onListFragmentInteraction(Song item) {
    }

    @Override
    public void onListFragmentInteraction(int categoryPosition) {
        Log.d("POS", categoryPosition + "");
        SONGS = getCategories().get(categoryPosition).getList();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_songs, new SongFragment()).commit();
        categorySelected = categoryPosition;
        setTitle(getCategories().get(categoryPosition).getName());
        appBarTitle = getTitle().toString();
    }
}
