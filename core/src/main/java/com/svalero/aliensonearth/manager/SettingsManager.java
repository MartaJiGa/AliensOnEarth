package com.svalero.aliensonearth.manager;

import com.badlogic.gdx.Gdx;
import com.svalero.aliensonearth.util.enums.PrefsNamesEnum;

import static com.svalero.aliensonearth.util.Constants.*;
import static com.svalero.aliensonearth.Main.prefs;

public class SettingsManager {

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
        return prefs.getBoolean(PrefsNamesEnum.MUSIC.getPrefsName(), true);
    }

    //endregion
}
