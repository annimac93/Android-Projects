package com.appart.hp.nearme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.appart.hp.menuintegration.R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(com.appart.hp.menuintegration.R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        userid = i.getStringExtra("userid");

        Bundle arguments = new Bundle();
        HomeFragment homeFragment = new HomeFragment();
        arguments.putString("userid",userid);
        homeFragment.setArguments(arguments);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(com.appart.hp.menuintegration.R.id.content_menu,homeFragment,homeFragment.getTag()).commit();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });



        DrawerLayout drawer = (DrawerLayout) findViewById(com.appart.hp.menuintegration.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.appart.hp.menuintegration.R.string.navigation_drawer_open, com.appart.hp.menuintegration.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.appart.hp.menuintegration.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }

        int count = getFragmentManager().getBackStackEntryCount();
//        Toast.makeText(getApplicationContext(),"count ="+String.valueOf(count),Toast.LENGTH_SHORT).show();
        if (count == 1) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.appart.hp.menuintegration.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == com.appart.hp.menuintegration.R.id.nav_home) {
            Bundle arguments = new Bundle();
           HomeFragment homeFragment = new HomeFragment();
            arguments.putString("userid",userid);
            homeFragment.setArguments(arguments);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(com.appart.hp.menuintegration.R.id.content_menu,homeFragment,homeFragment.getTag()).commit();
        } else if (id == com.appart.hp.menuintegration.R.id.nav_map) {
            Bundle arguments = new Bundle();
            MapFragment mapFragment = new MapFragment();
            arguments.putString("userid",userid);
            mapFragment.setArguments(arguments);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(com.appart.hp.menuintegration.R.id.content_menu,mapFragment,mapFragment.getTag()).addToBackStack(mapFragment.getTag()).commit();

        } else if (id == com.appart.hp.menuintegration.R.id.nav_profile) {
            Bundle arguments = new Bundle();
            ProfileFragment profileFragment = new ProfileFragment();
            arguments.putString("userid",userid);
            profileFragment.setArguments(arguments);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(com.appart.hp.menuintegration.R.id.content_menu,profileFragment,profileFragment.getTag()).commit();

        } else if (id == com.appart.hp.menuintegration.R.id.nav_logout) {
            userid = "";

//            Intent i = new Intent(MenuActivity.this , FirstActivity.class);
//            startActivity(i);
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.appart.hp.menuintegration.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void setData(String placename, String placeImage) {
//
//        Toast.makeText(getApplicationContext(),placename + "," +placeImage, Toast.LENGTH_SHORT);
//        PlaceFragment placeFragment = new PlaceFragment();
//        FragmentManager manager = getSupportFragmentManager();
//        placeFragment.getData(placename,placeImage);
//        manager.beginTransaction().replace(R.id.content_menu,placeFragment,placeFragment.getTag()).commit();
//
//    }
}
