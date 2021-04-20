package com.dotcom.rbs_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class RBS_Vendor_HomeScreen extends AppCompatActivity {
    private DrawerLayout vendor_drawer_layout;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    final Rbs_Vendor_Shop fragment_rbs_vendor_home = new Rbs_Vendor_Shop();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbs_vendor_home_screen);

        vendor_drawer_layout = (DrawerLayout)findViewById(R.id.vendor_drawer_layout);
        t = new ActionBarDrawerToggle(this, vendor_drawer_layout,R.string.Open, R.string.Close);

        vendor_drawer_layout.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.screenContainer,fragment_rbs_vendor_home).commit();


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.rbs_vendor_shop_nav:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.screenContainer,new Rbs_Vendor_Shop()).commit();
                        closeDrawer();
                        break;
                    case R.id.rbs_vendor_orders_nav:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.screenContainer,new Rbs_Vendor_Orders()).commit();
                        closeDrawer();
                        break;
                    case R.id.rbs_vendor_inbox_nav:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.screenContainer,new Rbs_Vendor_Inbox()).commit();
                        closeDrawer();
                        break;
                    case R.id.rbs_vendor_exit_nav:
                        closeDrawer();
                        Intent intent1 = new Intent(RBS_Vendor_HomeScreen.this,Rbs_options.class);
                        finish();
                        startActivity(intent1);
                        Toast.makeText(RBS_Vendor_HomeScreen.this, "Exit", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        return true;
                }


                return true;

            }
            private void closeDrawer() {
                if (vendor_drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    vendor_drawer_layout.closeDrawer(GravityCompat.START);
                }
            }

        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}