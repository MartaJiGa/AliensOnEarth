package com.svalero.aliensonearth;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.svalero.aliensonearth.manager.ResourceManager;
import com.svalero.aliensonearth.screen.GameScreen;
import com.svalero.aliensonearth.screen.MainMenuScreen;
import com.svalero.aliensonearth.screen.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    @Override
    public void create() {
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
