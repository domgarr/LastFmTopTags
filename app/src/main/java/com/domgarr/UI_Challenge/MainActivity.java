package com.domgarr.UI_Challenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.domgarr.UI_Challenge.models.Category;
import com.domgarr.UI_Challenge.models.Song;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SongFragment.OnListFragmentInteractionListener {
    public static List<Song> SONGS;

    private NavigationView navView;
    private DrawerLayout drawer;
    private Integer categorySelected;
    private static final String CATEGORY_SELECTED = "categorySelected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            categorySelected = savedInstanceState.getInt(CATEGORY_SELECTED);
        }

        setContentView(R.layout.activity_main);
        //TODO: Restore previously selected category after rotation.
        //Get reference to tool bar and set tool bar as action bar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);
        //Add Hamburger icon.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState(); //Adds hamburger rotation as drawer opens.

        //Add dynamic items to NavigationView.
        navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.setGroupCheckable(0,true,true);
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

        //TODO: Add a default fragment.
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
        SONGS = getCategories().get(item.getItemId()).getList();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SongFragment()).commit();

        navView.setCheckedItem(item.getItemId());
        categorySelected = item.getItemId();

        drawer.closeDrawer(GravityCompat.START);
        return true; //If false is returned, no item will be selected.
    }

    private List<Category> getCategories(){
        List<Category> categories = new ArrayList<>();

        List<com.domgarr.UI_Challenge.models.Song> rockSongs = new ArrayList<com.domgarr.UI_Challenge.models.Song>();
        rockSongs.add(new com.domgarr.UI_Challenge.models.Song("Stairway To Heaven."));
        rockSongs.add(new com.domgarr.UI_Challenge.models.Song("We Will Rock You."));
        Category rock = new Category("rock", rockSongs);
        categories.add(rock);

        List<com.domgarr.UI_Challenge.models.Song> classicalSongs = new ArrayList<com.domgarr.UI_Challenge.models.Song>();
        classicalSongs.add(new com.domgarr.UI_Challenge.models.Song("SIBELIUS Violin Concerto in D minor, Op. 47"));
        classicalSongs.add(new com.domgarr.UI_Challenge.models.Song("Clair De Lune"));

        Category classical = new Category("classical", classicalSongs);
        categories.add(classical);

        return categories;
    }

    @Override
    public void onListFragmentInteraction(Song item) {
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if(categorySelected != null){
            outState.putInt(CATEGORY_SELECTED, categorySelected);
        }

        super.onSaveInstanceState(outState);
    }
}
