package com.example.iclock;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton event_img_button;
    private ImageButton share_img_button;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Navigation controller
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        //calling the below init function for initializing navcontroller with UserInterface
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Adding the hamburger icon and syncing its state to the drawer layout for opening and closing of the navigation menu
        //step-1 create an drawerLayout Referernce from the activity page where you have put the navigation menu
        //step-2 create ActionBarDrawerToogle and give the constructor following parameter as shown in the below  function
        //step-3 sync the state of the toogle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //Setting Up Navigation Menu Button Controll (This is navigation View)
        navigationView = findViewById(R.id.nav_menu);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);

        //This will add the actiobar with nav controller like when you are dashboard then it will show three_line menu and when on next fragment it will show a back arrow
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);
        //But to control the above thing you need to setup it with navcontroller
        NavigationUI.setupWithNavController(toolbar,navController,drawerLayout);


        //This will create the message box which you are seeing on the dashboard page of our activity we call it FloatingActionButton.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                //here i will change fragment to home fragment using fragment transaction
                navController.navigate(R.id.dashboardFragment);
                break;
            case R.id.nav_add_events:
                navController.navigate(R.id.addEventFragment);
                break;
        }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
//        //if we return false from here then menu event can be handled but the item which
//        //we select will not show the select marker on that item
//        //that's why we are returning true from here.
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}