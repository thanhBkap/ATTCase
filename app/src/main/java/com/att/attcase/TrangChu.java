package com.att.attcase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class TrangChu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button btn_chuyen_trang_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowHomeEnabled(false);*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_camera);
      //  toggle.setDrawerIndicatorEnabled(true);     // this will disable your default haburger icon
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        addControls();
        adddEvents();
    }

    private void adddEvents() {
        btn_chuyen_trang_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mChuyenPage2 =new Intent(getApplicationContext(),ChonKhungLayout.class);
                startActivity(mChuyenPage2);
            }
        });
    }

    private void addControls() {
        btn_chuyen_trang_2= (Button) findViewById(R.id.btn_chuyen_trang_2);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(getApplicationContext(),"Tính năng sẽ được update trong thời gian sớm nhất",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(),"Tính năng sẽ được update trong thời gian sớm nhất",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_cart) {
            Toast.makeText(getApplicationContext(),"Tính năng sẽ được update trong thời gian sớm nhất",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_invite) {
            Toast.makeText(getApplicationContext(),"Tính năng sẽ được update trong thời gian sớm nhất",Toast.LENGTH_LONG).show();
        } /*else if (id == R.id.nav_log_out) {
            Toast.makeText(getApplicationContext(),"Tính năng sẽ được update trong thời gian sớm nhất",Toast.LENGTH_LONG).show();

        }*/ else if (id == R.id.nav_make_case) {
            Toast.makeText(getApplicationContext(),"Tính năng sẽ được update trong thời gian sớm nhất",Toast.LENGTH_LONG).show();
        }else if (id == R.id.nav_profile) {
            Toast.makeText(getApplicationContext(),"Tính năng sẽ được update trong thời gian sớm nhất",Toast.LENGTH_LONG).show();

        }else if (id == R.id.nav_promotion) {
            Toast.makeText(getApplicationContext(),"Tính năng sẽ được update trong thời gian sớm nhất",Toast.LENGTH_LONG).show();

        }else if (id == R.id.nav_store) {
            Toast.makeText(getApplicationContext(),"Tính năng sẽ được update trong thời gian sớm nhất",Toast.LENGTH_LONG).show();
        }else if (id == R.id.nav_view) {
            Toast.makeText(getApplicationContext(),"Tính năng sẽ được update trong thời gian sớm nhất",Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
