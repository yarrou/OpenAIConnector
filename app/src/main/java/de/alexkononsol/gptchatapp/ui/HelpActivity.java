package de.alexkononsol.gptchatapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import de.alexkononsol.gptchatapp.R;
import de.alexkononsol.gptchatapp.utils.SettingsManager;


public class HelpActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //if(!DeviceTypeHelper.isTablet(this)) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_help);
        setSupportActionBar(toolbar);

        Log.d("ChatGpt", "starting HelpActivity method onStart()");
        float textSize = SettingsManager.getSettings().getTextSize();
        ((TextView) findViewById(R.id.textAbout)).setTextSize(textSize);
        ((TextView) findViewById(R.id.textTips)).setTextSize(textSize);

        //textAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

}