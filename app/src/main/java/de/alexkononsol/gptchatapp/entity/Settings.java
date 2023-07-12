package de.alexkononsol.gptchatapp.entity;

import android.util.Log;

import de.alexkononsol.gptchatapp.utils.Constants;
import com.google.gson.Gson;

public class Settings {
    String hostName = Constants.DEFAULT_HOST_URL;
    private boolean viewHelpOnStart;
    private boolean isNewInstallation;

    public Settings() {
        Log.d("chatgpt","create new Settings");
        this.viewHelpOnStart = false;
        this.hostName = Constants.DEFAULT_HOST_URL;
        this.isNewInstallation = true;
    }
    public Settings(Settings settings){
        this.hostName = settings.getHostName();


    }

    public boolean isNewInstallation() {
        return isNewInstallation;
    }

    public void setNewInstallation(boolean newInstallation) {
        isNewInstallation = newInstallation;
    }

    public boolean isViewHelpOnStart() {
        return viewHelpOnStart;
    }

    public void setViewHelpOnStart(boolean viewHelpOnStart) {
        this.viewHelpOnStart = viewHelpOnStart;
    }

    public String getHostName() {
        return hostName;
    }


    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public static Settings getSettingsFromString(String value){
        Gson gson = new Gson();
        Settings settings = gson.fromJson(value, Settings.class);
        return settings;
    }
}