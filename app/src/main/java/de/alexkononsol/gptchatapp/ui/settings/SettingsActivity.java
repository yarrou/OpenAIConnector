package de.alexkononsol.gptchatapp.ui.settings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.utils.SettingsManager;

public class SettingsActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void saveSetting() {
        SettingsManager.save();
        String toastTextSavedSettings = getString(R.string.saveSettingsToast);
        Toast.makeText(this, toastTextSavedSettings, Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if(item.getItemId()==R.id.action_save_settings){
            saveSetting();
            return true;
        }else */
        return super.onOptionsItemSelected(item);

    }
}