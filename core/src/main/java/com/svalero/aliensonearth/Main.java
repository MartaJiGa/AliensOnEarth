package com.svalero.aliensonearth;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.screen.GameScreen;
import com.svalero.aliensonearth.screen.MainMenuScreen;
import com.svalero.aliensonearth.screen.SplashScreen;
import com.svalero.aliensonearth.util.enums.PrefsNamesEnum;

import static com.svalero.aliensonearth.util.Constants.GAME_NAME;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static Preferences prefs;

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences(GAME_NAME);
        if (!prefs.contains(PrefsNamesEnum.MUSIC_VOLUME.getPrefsName())) {
            prefs.putFloat(PrefsNamesEnum.MUSIC_VOLUME.getPrefsName(), 0.5f);
            prefs.flush();
        }

        setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        ResourceManager.dispose();
    }
}
