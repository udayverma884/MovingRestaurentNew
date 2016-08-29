package com.movingrestaurent.truckowner;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.movingrestaurent.MenuActivity;
import com.movingrestaurent.R;
import com.movingrestaurent.SelectionActivity;
import com.movingrestaurent.fragmentuser.AboutUsFragment;
import com.movingrestaurent.fragmentuser.ActiveDeactiveFragment;
import com.movingrestaurent.fragmentuser.EnterRestaurantMenu;
import com.movingrestaurent.fragmentuser.HomeFragmentUser;
import com.movingrestaurent.fragmentuser.ProfileFragment;
import com.movingrestaurent.fragmentuser.RestaurantFragment;
import com.movingrestaurent.utils.PreferenceConnector;

public class TruckOwnerNavigationActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_owner_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment fragment = new RestaurantFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new RestaurantFragment()).commit();

            // Handle the camera action
        } else if (id == R.id.nav_profile) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();


        } else if (id == R.id.nav_enter_menu_item) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new EnterRestaurantMenu()).commit();

        } else if (id == R.id.nav_about_us) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new AboutUsFragment()).commit();

        }else if(id == R.id.active_deactive){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ActiveDeactiveFragment()).commit();

        }else if(id == R.id.my_menu){
            Intent intent =new Intent(this,MenuActivity.class);
            intent.putExtra("restaurant_id", PreferenceConnector.readString(this,"UserId",""));
            startActivity(intent);

        }
        else if(id == R.id.logout){
            PreferenceConnector.writeString(this,"UserId","");
            PreferenceConnector.writeString(this,"firstName","");
            PreferenceConnector.writeString(this,"lastName","");
            PreferenceConnector.writeString(this,"restaurantName","");
            PreferenceConnector.writeString(this,"restaurantDescription","");
            PreferenceConnector.writeString(this,"mobile","");
            PreferenceConnector.writeString(this,"userType","");
            PreferenceConnector.writeString(this,"address","");
            PreferenceConnector.writeString(this,"email","");
            PreferenceConnector.writeString(this,"profile_pic","");
            PreferenceConnector.writeString(this,"address","");
            PreferenceConnector.writeBoolean(this,"isLogin",false);
            PreferenceConnector.writeString(this,"status","0");

            Intent intent=new Intent(this,SelectionActivity.class);
            startActivity(intent);
            finish();


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
