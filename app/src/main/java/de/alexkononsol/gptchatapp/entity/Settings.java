package de.alexkononsol.gptchatapp.entity;

import de.alexkononsol.gptchatapp.utils.Constants;
import com.google.gson.Gson;

public class Settings {
    String hostName = Constants.DEFAULT_HOST_URL;

    public Settings() {
    }
    public Settings(Settings settings){

        this.hostName = settings.getHostName();
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