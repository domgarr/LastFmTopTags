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
import android.view.ScaleGestureDetector;
import android.view.SubMenu;
import android.widget.ArrayAdapter;

import com.domgarr.UI_Challenge.models.Category;
import com.domgarr.UI_Challenge.models.Song;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get reference to tool bar and set tool bar as action bar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);
        //Add Hamburger icon.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState(); //Adds hamburger rotation as drawer opens.

        //Add dynamic items to NavigationView.
        NavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        SubMenu categorySubMenu = menu.getItem(0).getSubMenu();
        categorySubMenu.setGroupCheckable(0,true,true);

        ArrayList<Category> categories = (ArrayList) getCategories();

        int itemId = 0;
        for(Category category : categories) {
            categorySubMenu.add(0, itemId++, Menu.NONE, category.getName());
        }
        navView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case 1 :
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BlankFragment());
                break;
            case 2 :
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BlankFragment());
                break;
        }
        item.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        return true; //If false is returned, no item will be selected.
    }

    private List<Category> getCategories(){
        List<Category> categories = new ArrayList<>();

        List<Song> rockSongs = new ArrayList<Song>();
        rockSongs.add(new Song("Stair way to heaven"));
        Category rock = new Category("rock", rockSongs);
        categories.add(rock);

        List<Song> classicalSongs = new ArrayList<Song>();
        classicalSongs.add(new Song("SIBELIUS Violin Concerto in D minor, Op. 47"));
        Category classical = new Category("classical", classicalSongs);
        categories.add(classical);

        return categories;

    }
}
