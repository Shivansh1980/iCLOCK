package com.example.iclock;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageButton event_img_button;
    private ImageButton share_img_button;
    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Adding the hamburger icon and syncing its state to the drawer layout for opening and closing of the navigation menu
        //step-1 create an drawerLayout Referernce from the activity page where you have put the navigation menu
        //step-2 create ActionBarDrawerToogle and give the constructor following parameter as shown in the below  function
        //step-3 sync the state of the toogle
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();


        //Setting Up Navigation Menu Button Controll
        NavigationView navigationView = findViewById(R.id.nav_menu);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);

        //This will create the message box which you are seeing on the dashboard page of our activity we call it FloatingActionButton.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        //Loading the first Fragment on to the screen on first start
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container,new HomeFragment())
                .commit();
        navigationView.setCheckedItem(R.id.nav_home);

    }

    //the below declaration is for switching the fragment, i have declaared it here because when i tried to declare the Fragment temp inside the
    //below function it was giving me some error.
    Fragment temp;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavigationView navigationView = findViewById(R.id.nav_menu);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        switch (item.getItemId()){
            case R.id.nav_home:
                //here i will change fragment to home fragment using fragment transaction
                temp = new HomeFragment();
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case R.id.nav_events:
                temp = new SecondFragment();
                navigationView.setCheckedItem(R.id.nav_events);
                break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container,temp)
                .commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
        //if we return false from here then menu event can be handled but the item which
        // we select will not show the select marker on that item
        //that's why we are returning true from here.
    }
}