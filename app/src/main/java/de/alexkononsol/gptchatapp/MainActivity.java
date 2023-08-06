package de.alexkononsol.gptchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.alexkononsol.gptchatapp.ui.fragments.ChatFragment;
import de.alexkononsol.gptchatapp.ui.login.LoginActivity;
import de.alexkononsol.gptchatapp.ui.settings.SettingsActivity;
import de.alexkononsol.gptchatapp.utils.SettingsManager;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment chatFragment = new ChatFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, chatFragment);
        ft.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (SettingsManager.getSettings().getAuthToken().equals("")) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);

                TextView userNameTextView = (TextView) headerView.findViewById(R.id.userNameNav);
                userNameTextView.setText(SettingsManager.getSettings().getUserName());
            }
        } catch (Exception e) {
            Log.e("chatgpt", e.getLocalizedMessage(), e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;
        if (id == R.id.nav_settings) {
            intent = new Intent(this, SettingsActivity.class);
        } else if (id == R.id.nav_help) {
            intent = new Intent(this, HelpActivity.class);
        } else {
            fragment = new ChatFragment();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
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

