package de.alexkononsol.gptchatapp.ui;

import static de.alexkononsol.gptchatapp.ui.fragments.ChatFragment.SEARCH_MODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.connectionUtils.Role;
import de.alexkononsol.gptchatapp.ui.fragments.ChatFragment;
import de.alexkononsol.gptchatapp.ui.login.LoginActivity;
import de.alexkononsol.gptchatapp.ui.settings.SettingsActivity;
import de.alexkononsol.gptchatapp.utils.SettingsManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ChatFragment chatFragment;
    private boolean showAllMessages = false;
    private TextView selectedRoleTextView;
    private Role selectedRole = Role.GPT_3_5_TURBO_0125;

    public Role getSelectedRole() {
        return selectedRole;
    }

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

                if (chatFragment == null) {
                    chatFragment = new ChatFragment();
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, chatFragment);
                ft.commit();
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem allMessagesItem = menu.findItem(R.id.action_all_messages);
        MenuItem favoritesItem = menu.findItem(R.id.action_favorites);

        allMessagesItem.setVisible(showAllMessages);
        favoritesItem.setVisible(!showAllMessages);

        MenuItem roleItem = menu.findItem(R.id.action_select_role);
        View actionView = roleItem.getActionView();
        selectedRoleTextView = actionView.findViewById(R.id.selected_role);
        selectedRoleTextView.setText(selectedRole.getRoleName());


        selectedRoleTextView.setOnClickListener(view -> showRoleSelectionDialog());

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorites) {
            showAllMessages = true;
            invalidateOptionsMenu();

            chatFragment.updateFragmentState(1, null);

            return true;
        } else if (id == R.id.action_all_messages) {
            showAllMessages = false;
            invalidateOptionsMenu();

            chatFragment.updateFragmentState(0, null);

            return true;
        } else if (id == R.id.action_search) {
            showSearchDialog();
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (chatFragment != null) {
                int initialMode = ChatFragment.NORMAL_MODE;
                chatFragment.updateFragmentState(initialMode, null);
            }

            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("chatgpt","method onSaveInstanceState() is started");
        outState.putString("SELECTED_ROLE", selectedRole.name());
        Log.d("chatgpt","selected role is " + selectedRole.name());
        //getSupportFragmentManager().putFragment(outState, "CHAT_FRAGMENT", chatFragment);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d("chatgpt","method onRestoreInstanceState() is started");

        if (savedInstanceState.containsKey("SELECTED_ROLE")) {
            selectedRole = Role.valueOf(savedInstanceState.getString("SELECTED_ROLE"));
            Log.d("chatgpt","saved selected role is " + selectedRole.name());
        }else {
            Log.d("chatgpt","selected role is not saved");
        }
        //chatFragment = (ChatFragment) getSupportFragmentManager().getFragment(savedInstanceState, "CHAT_FRAGMENT");
    }
    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.search));


        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(20,10,20,10);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.search), (dialog, which) -> {
            String searchQuery = input.getText().toString();
            chatFragment.updateFragmentState(SEARCH_MODE, searchQuery);
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showRoleSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Role");

        String[] roles = new String[Role.values().length];
        for (int i = 0; i < Role.values().length; i++) {
            roles[i] = Role.values()[i].getRoleName();
        }

        builder.setItems(roles, (dialog, which) -> {
            selectedRole = Role.values()[which];
            selectedRoleTextView.setText(selectedRole.getRoleName());
            Toast.makeText(this, "Role selected: " + selectedRole.getRoleName(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        builder.show();
    }

}

