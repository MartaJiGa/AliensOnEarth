package com.svalero.aliensonearth.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.svalero.aliensonearth.manager.*;
import com.svalero.aliensonearth.util.enums.MusicEnum;

public class GameScreen implements Screen {

    //region properties

    private LogicManager logicManager;
    private RenderManager renderManager;
    private LevelManager levelManager;

    private Game game;
    private com.badlogic.gdx.audio.Music backgroundMusic;

    //endregion

    //region constructor

    public GameScreen(Game game){
        this.game = game;
        loadManagers();
        loadBackgroundMusic();

        if(SettingsManager.isMusicEnabled())
            backgroundMusic.play();
    }

    //endregion

    //region override

    @Override
    public void show() {
        if (backgroundMusic != null && SettingsManager.isMusicEnabled() && !backgroundMusic.isPlaying())
            backgroundMusic.play();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0.741f, 0.89f, 0.973f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (logicManager.isPaused()) {
            pauseMusic();
            ((Game) Gdx.app.getApplicationListener()).setScreen(new PauseScreen(game, this, logicManager));
        } else if(logicManager.isFinished()){
            pauseMusic();
            ((Game) Gdx.app.getApplicationListener()).setScreen(new FinishScreen(game, this, logicManager, false));
        } else if(logicManager.isDead()){
            pauseMusic();
            ((Game) Gdx.app.getApplicationListener()).setScreen(new FinishScreen(game, this, logicManager, true));
        }
        else{
            logicManager.update(dt);
        }

        renderManager.render();
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
        logicManager.dispose();
        renderManager.dispose();

        if(SettingsManager.isMusicEnabled() && backgroundMusic != null)
            backgroundMusic.dispose();
    }

    //endregion

    //region methods

    public void loadManagers(){
        logicManager = new LogicManager();
        levelManager = new LevelManager(logicManager);
        renderManager = new RenderManager(logicManager, levelManager.getMap());
    }

    public void loadBackgroundMusic(){
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }

        backgroundMusic = ResourceManager.getMusic(MusicEnum.BACKGROUND);
        backgroundMusic.setLooping(true);
        backgroundMusic.setPosition(0); // If I activate the music from the settings menu and return to the game, the music starts from the beginning.
    }

    public void pauseMusic(){
        if(backgroundMusic != null && SettingsManager.isMusicEnabled())
            backgroundMusic.pause();
    }

    //endregion
}
