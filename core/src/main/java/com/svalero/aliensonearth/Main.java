package com.svalero.aliensonearth;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.svalero.aliensonearth.manager.DatabaseManager;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.screen.SplashScreen;
import com.svalero.aliensonearth.util.enums.PrefsNamesEnum;

import java.sql.Statement;

import static com.svalero.aliensonearth.util.Constants.GAME_NAME;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static DatabaseManager db;
    public static Preferences prefs;

    @Override
    public void create() {
        db = new DatabaseManager();
        db.connect();
        db.createTables();

        prefs = Gdx.app.getPreferences(GAME_NAME);
        if (!prefs.contains(PrefsNamesEnum.MUSIC_VOLUME.getPrefsName())) {
            prefs.putFloat(PrefsNamesEnum.MUSIC_VOLUME.getPrefsName(), 0.5f);
        }

        String currentName = prefs.getString("playerName", "Anonymous");
        int playerId = db.getPlayerIdByName(currentName);
        if (playerId == -1) {
            prefs.putString("playerName", "Anonymous");
        }

        prefs.flush();

        setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        db.close();
        ResourceManager.dispose();
    }
}
