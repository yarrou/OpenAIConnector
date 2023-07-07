package de.alexkononsol.gptchatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import de.alexkononsol.gptchatapp.entity.Settings;
import com.google.gson.Gson;

public class SharedPreferenceAssistant {

    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;

    public static void initSharedPreferences(Context context){
        mSharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static Settings getSettingFromSharedPreferences(){

        String result = mSharedPreferences.getString(Constants.SHARED_PREFERENCES_SETTINGS, null);
        Gson gson = new Gson();
        return gson.fromJson(result, Settings.class);
    }
    public static void save(String result){
        Gson gson = new Gson();
        Settings settings = gson.fromJson(result, Settings.class);
        save(settings);
    }

    public static void save(Settings settings){

        try {

            Gson gson = new Gson();
            String result = gson.toJson(settings);

            mEditor.putString(Constants.SHARED_PREFERENCES_SETTINGS, result);
            mEditor.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            //LogHelper.logError(SharedPreferenceAssistant.class,e.getCause() + " - " + e.getMessage(),e);
        }
    }
    public static String getStringSettings(Settings settings){
        String result = null;
        try {
            Gson gson = new Gson();
            result = gson.toJson(settings);
        }
        catch (Exception e){
            //LogHelper.logError(SharedPreferenceAssistant.class,"the problem of getting the settings",e);

        }
        return result;
    }

}
