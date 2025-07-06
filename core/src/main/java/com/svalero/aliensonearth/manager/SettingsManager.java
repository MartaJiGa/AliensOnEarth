package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import static com.svalero.aliensonearth.util.Constants.GAME_NAME;

public class SettingsManager {

    //region properties

    private static Preferences prefs;

    //endregion

    //region constructor

    public SettingsManager(){
        loadPreferences();
    }

    //endregion

    //region methods

    private void loadPreferences(){
        prefs = Gdx.app.getPreferences(GAME_NAME);
    }

    public static boolean isMusicEnabled(){
        return prefs.getBoolean("music", true);
    }

    //endregion
}
