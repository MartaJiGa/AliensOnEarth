package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.svalero.aliensonearth.manager.LogicManager;
import com.svalero.aliensonearth.manager.RenderManager;
import com.svalero.aliensonearth.manager.ResourceManager;

public class GameScreen implements Screen {

    //region properties

    private LogicManager logicManager;
    private RenderManager renderManager;
    private ResourceManager resourceManager;

    Music backgroundMusic;

    //endregion

    //region override

    @Override
    public void show() {
        resourceManager = new ResourceManager();
        resourceManager.loadAllResources();

        logicManager = new LogicManager();
        renderManager = new RenderManager(logicManager);

        backgroundMusic = resourceManager.getBackgroundMusic();
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.741f, 0.89f, 0.973f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        logicManager.update();
        renderManager.render();

        if (logicManager.shouldExit()) {
            dispose();
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        resourceManager.dispose();
        logicManager.dispose();
        renderManager.dispose();

        backgroundMusic.dispose();
    }

    //endregion
}
