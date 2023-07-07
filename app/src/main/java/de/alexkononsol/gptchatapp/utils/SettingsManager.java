package de.alexkononsol.gptchatapp.utils;

import de.alexkononsol.gptchatapp.entity.Settings;

public class SettingsManager {

    private static Settings settings;


    public SettingsManager() {

    }

    public static void initSettings() {

        settings = SharedPreferenceAssistant.getSettingFromSharedPreferences();

        if (settings == null) {
            settings = new Settings();
        }
        settings = getSettings();
    }

    public static Settings getSettings() {
        //settings.setHostName(Constants.DEFAULT_HOST_URL);
        return settings;
    }

    public static void save() {
        SharedPreferenceAssistant.save(settings);
    }

    public static String getStringSettings() {
        return SharedPreferenceAssistant.getStringSettings(settings);
    }

    public static String getStringBackupSettings() {
        return SharedPreferenceAssistant.getStringSettings(new Settings(settings));
    }

    public static void restoreSettings(String stringSettings) {
        SettingsManager.settings = Settings.getSettingsFromString(stringSettings);
        save();
    }

    public static void restoreSettings(Settings settings) {
        SettingsManager.settings = settings;
        save();
    }

}