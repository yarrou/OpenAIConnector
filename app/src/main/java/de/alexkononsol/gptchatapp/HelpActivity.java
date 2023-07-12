package de.alexkononsol.gptchatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;


public class HelpActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //if(!DeviceTypeHelper.isTablet(this)) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_help);
        setSupportActionBar(toolbar);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        Log.d("ChatGpt","starting HelpActivity method onStart()");
        //String textSize = SettingsManager.getSettings().getTextSize();
        TextView textAbout = (TextView) findViewById(R.id.textAbout);
        //textAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //Menu of Toolbar
}