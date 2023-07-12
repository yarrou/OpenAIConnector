package de.alexkononsol.gptchatapp;

import static java.lang.String.format;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import de.alexkononsol.gptchatapp.utils.SettingsManager;
import de.alexkononsol.gptchatapp.utils.SharedPreferenceAssistant;


public class SplashActivity extends AppCompatActivity {
    private static boolean isFirstRun = true;
    private ShareActionProvider shareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        showVersionApp();
    }


    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    private void runMainProcess() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isFirstRun) {
            int secondsDelayed = 1;
            new Handler().postDelayed(() -> {
                isFirstRun = false;
                SharedPreferenceAssistant.initSharedPreferences(SplashActivity.this);
                SettingsManager.initSettings();
                //setSupportActionBar(toolbar);
                boolean viewHelpOnStart = SettingsManager.getSettings().isViewHelpOnStart();
                if (SettingsManager.getSettings().isNewInstallation()) {
                    SettingsManager.getSettings().setNewInstallation(false);
                    SettingsManager.save();
                    Intent intent = new Intent(SplashActivity.this, HelpActivity.class);
                    startActivity(intent);
                } else if (viewHelpOnStart) {
                    Intent intent = new Intent(SplashActivity.this, HelpActivity.class);
                    startActivity(intent);
                } else {
                    runMainProcess();
                }
            }, secondsDelayed * 1000);
        } else {
            runMainProcess();
        }

    }

    private void showVersionApp() {
        TextView versionTextView = (TextView) findViewById(R.id.version_text_view);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionTextView.setText(format(getString(R.string.version), version));

        } catch (PackageManager.NameNotFoundException e) {
            //Log.e(LogHelper.TAG,"the program version cannot be displayed",e);
            e.printStackTrace();
        }
    }

}