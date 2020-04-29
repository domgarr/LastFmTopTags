package com.domgarr.fromscratch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.drm.DrmStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.google.android.material.navigation.NavigationView;

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
        categorySubMenu.add(0,1,Menu.NONE, "Rock");
        categorySubMenu.add(0,2,Menu.NONE, "Classical");
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
}
