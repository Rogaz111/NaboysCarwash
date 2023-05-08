package com.example.inc;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        toolbar = findViewById(R.id.toolbar2);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Set up the toolbar as the action bar
        setSupportActionBar(toolbar);
        toolbar.setTitle("");


        Intent intent = getIntent();

        // Retrieve the values passed from the previous activity
        String usernameValue = intent.getStringExtra("username");
        //String passwordValue = intent.getStringExtra("password");

        // Get the header view from the navigation view
        View headerView = navigationView.getHeaderView(0);

        // Find the TextView in the header view
        TextView usernameTextView = headerView.findViewById(R.id.nav_header_username);

        // Set the text of the TextView to the username value
        usernameTextView.setText("UserId: "+usernameValue);

        // Set up the navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the navigation view item click listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // Handle menu item clicks here
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        Toast.makeText(getApplicationContext(),"HOME",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_about:
                        Toast.makeText(getApplicationContext(),"ABOUT",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_galary:
                        GalleryFragment galleryFragment = new GalleryFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, galleryFragment)
                                .commit();
                        break;
                    case R.id.menu_prices:
                        PricesFragment pricesFragment = new PricesFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, pricesFragment)
                                .commit();
                        break;
                    case R.id.menu_staff:
                        Toast.makeText(getApplicationContext(),"STAFF TEAM",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_booking:
                        BookingFragment bookingFragment = new BookingFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, bookingFragment)
                                .commit();
                        break;
                }
                // Close the navigation drawer when an item is clicked
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}
